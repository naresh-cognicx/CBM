package com.cognicx.AppointmentRemainder.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cognicx.AppointmentRemainder.jwt.JwtAuthEntryPoint;
import com.cognicx.AppointmentRemainder.jwt.JwtAuthTokenFilter;
import com.cognicx.AppointmentRemainder.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@PropertySource("${user.prop.location}")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	JwtAuthEntryPoint unauthorizedHandler;
	
	@Value("${app.auth.enabled}")
	private boolean authEnabled;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public JwtAuthTokenFilter authenticationJwtTokenFilter() {
		return new JwtAuthTokenFilter();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setHideUserNotFoundExceptions(false);
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (authEnabled) {
			http.cors().and().csrf().disable().authorizeRequests()
					.antMatchers("/api/token/authenticate", "/user/usersList", "/api/login", "/api/logout",
							"/api/forgetpassword/request", "/api/forgetpassword/reset",
							"/api/forgetpassword/validateOTP", "/v2/api-docs", "/configuration/**", "/swagger*/**",
							"/webjars/**", "/user/getRoles", "/user/addUser", "/user/updateUser","/user/findLoginDetails","/userRule/api/login","/api/changepassword","/campaign/updateCallDetail")
					.permitAll().anyRequest().authenticated().and().exceptionHandling()
					.authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		} else {

			http.cors().and().csrf().disable().authorizeRequests().antMatchers("*").permitAll().and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}
}
