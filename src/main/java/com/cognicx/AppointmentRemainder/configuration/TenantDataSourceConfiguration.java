
package com.cognicx.AppointmentRemainder.configuration;

import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
		"com.ison.app.JpaRepository" }, entityManagerFactoryRef = "TenantEntityManager", transactionManagerRef = "transactionManager")
public class TenantDataSourceConfiguration {

	private org.slf4j.Logger logger = LoggerFactory.getLogger(TenantDataSourceConfiguration.class);

	@Autowired
	private Environment environment;
	@Bean(destroyMethod = "close", name = ApplicationConstant.TENANT_DATA_SOURCE_BEAN_NAME)
	public DataSource tenantDataSource() {
		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setDriverClassName(environment.getRequiredProperty("db2.datasource.driver-class-name"));
		dataSourceConfig.setJdbcUrl(environment.getRequiredProperty("db2.datasource.url"));
		dataSourceConfig.setUsername(environment.getRequiredProperty("db2.datasource.username"));
		dataSourceConfig.setPassword(environment.getRequiredProperty("db2.datasource.password"));
		return new HikariDataSource(dataSourceConfig);
	}

	public JpaVendorAdapter tenantJpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}
	private Properties commonJpaProperties() {
		Properties properties = new Properties();
		properties.put(ApplicationConstant.HIBERNATE_DIALECT,
				environment.getRequiredProperty("db2.jpa.properties.hibernate.dialect"));
		properties.put(ApplicationConstant.HIBERNATE_SHOW_SQL, environment.getRequiredProperty("db2.jpa.show-sql"));
		properties.put(ApplicationConstant.HIBERNATE_FORMAT_SQL,
				environment.getRequiredProperty(ApplicationConstant.HIBERNATE_FORMAT_SQL));
		properties.put("hibernate.ddl-auto", environment.getRequiredProperty("db2.jpa.hibernate.ddl-auto"));
		return properties;
	}
	@Bean(name = ApplicationConstant.TENANT_ENTITY_MANAGER)
	public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory() {
		try {
			LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
			localContainerEntityManagerFactoryBean.setDataSource(tenantDataSource());
			localContainerEntityManagerFactoryBean.setPackagesToScan(ApplicationConstant.FIRST_MODAL_PACKAGE);
			localContainerEntityManagerFactoryBean
					.setPersistenceUnitName(ApplicationConstant.TENANT_PERSISTENCE_UNIT_NAME);
			localContainerEntityManagerFactoryBean.setJpaVendorAdapter(tenantJpaVendorAdapter());
			localContainerEntityManagerFactoryBean.setJpaProperties(commonJpaProperties());
			return localContainerEntityManagerFactoryBean;
		} catch (Exception e) {
			logger.error("DSConfiguration.LocalContainerEntityManagerFactoryBean(): " + e.getMessage());
		}
		return new LocalContainerEntityManagerFactoryBean();
	}

	@Bean(name = ApplicationConstant.TENANT_TRANSACTION_MANAGER)
	public PlatformTransactionManager tenantTransactionManager(
			javax.persistence.EntityManagerFactory tenantEntityManager) {
		try {
			return new JpaTransactionManager(tenantEntityManager);
		} catch (Exception e) {
			logger.error("DSConfiguration.tenantTransactionManager(): " + e.getMessage());
		}
		return new JpaTransactionManager();
	}

    @Primary
	@Bean(name = ApplicationConstant.TENANT_JDBC_TEMPLATE)
	public JdbcTemplate tenantJdbcTemplate(DataSource tenantDataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(tenantDataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		return jdbcTemplate;
	}

	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(tenantDataSource());
		sessionFactory.setPackagesToScan(new String[] { ApplicationConstant.FIRST_MODAL_PACKAGE });
		sessionFactory.setHibernateProperties(commonJpaProperties());
		return sessionFactory;
	}

	@PostConstruct
	public void checkDatabaseConnection() {
		try (Connection connection = tenantDataSource().getConnection()) {
			System.out.println("Connected to the database successfully.");
		} catch (SQLException e) {
			System.err.println("Failed to connect to the database. Error: " + e.getMessage());
			throw new IllegalStateException("Failed to connect to the database.", e);
		}
	}
}
