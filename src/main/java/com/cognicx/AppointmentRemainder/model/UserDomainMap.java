package com.cognicx.AppointmentRemainder.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "user_domain_map", schema = "user_rule")
@NamedQuery(name = "UserDomainMap.findAll", query = "SELECT ui FROM UserDomainMap ui")
public class UserDomainMap implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUTOGEN_USER_DOMAIN_MAP_ID")
	private BigInteger autogenUserDomainMapId;

	@Column(name = "AUTOGEN_USERS_DETAILS_ID")
	private BigInteger autogenUsersDetailsId;

	@Column(name = "AUTOGEN_USERS_ID")
	private BigInteger autogenUsersId;

	@Column(name = "DOMAIN_ID")
	private BigInteger domainId;

	@Column(name = "DOMAIN_NAME")
	private String domainName;

	@Column(name = "BUSINESS_UNIT_ID")
	private BigInteger businessUnitId;

	@Column(name = "BUSINESS_UNIT_NAME")
	private String businessUnitName;

	@Column(name = "CATEGORY_ID")
	private BigInteger inventoryCategoryId;

	@Column(name = "CATEGORY_NAME")
	private String inventoryCategoryName;

	//@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_ADD_DT")
	private Date recAddDt;

	//@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	public UserDomainMap() {

	}

	public BigInteger getAutogenUserDomainMapId() {
		return autogenUserDomainMapId;
	}

	public void setAutogenUserDomainMapId(BigInteger autogenUserDomainMapId) {
		this.autogenUserDomainMapId = autogenUserDomainMapId;
	}

	public BigInteger getAutogenUsersDetailsId() {
		return autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(BigInteger autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public BigInteger getAutogenUsersId() {
		return autogenUsersId;
	}

	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
	}

	public BigInteger getDomainId() {
		return domainId;
	}

	public void setDomainId(BigInteger domainId) {
		this.domainId = domainId;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public BigInteger getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(BigInteger businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
	}

	public Date getRecAddDt() {
		return recAddDt;
	}

	public void setRecAddDt(Date recAddDt) {
		this.recAddDt = recAddDt;
	}

	public Date getRecUpdateDt() {
		return recUpdateDt;
	}

	public void setRecUpdateDt(Date recUpdateDt) {
		this.recUpdateDt = recUpdateDt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
