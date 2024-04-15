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


/**
 * The persistent class for the users_details database table.
 * 
 */
@Entity
@Table(name = "users_details_approved", schema = "user_rule")
@NamedQuery(name = "UsersDetailApproved.findAll", query = "SELECT u FROM UsersDetailApproved u")
public class UsersDetailApproved implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_USERS_DETAILS_ID")
	private BigInteger autogenUsersDetailsId;

	@Column(name="AUTOGEN_USERS_ID")
	private BigInteger autogenUsersId;
	
	@Column(name="AUTOGEN_ROLES_ID")
	private BigInteger autogenRolesId;
	
	@Column(name="ROLES_NAME")
	private String rolesName;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="INVENTORY_CATEGORY_ID")
	private BigInteger inventoryCategoryId;
	
	@Column(name="INVENTORY_CATEGORY_NAME")
	private String inventoryCategoryName;

	//@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	//@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name="SUPERVISOR_USERS_ID")
	private String supervisorUsersId;
	
	@Column(name="SUPERVISOR_USERS_NAME")
	private String supervisorUsersName;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name = "DOMAIN_ID")
	private int domainId;

	@Column(name = "BU_ID")
	private int buId;

	@Column(name = "BU_NAME")
	private String buName;

	@Column(name = "DOMAIN_NAME")
	private String domainName;

	public UsersDetailApproved() {
	}

	public BigInteger getAutogenUsersDetailsId() {
		return this.autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(BigInteger autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public BigInteger getAutogenUsersId() {
		return this.autogenUsersId;
	}

	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public BigInteger getInventoryCategoryId() {
		return this.inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}


	public Date getRecAddDt() {
		return this.recAddDt;
	}

	public void setRecAddDt(Date recAddDt) {
		this.recAddDt = recAddDt;
	}

	public Date getRecUpdateDt() {
		return this.recUpdateDt;
	}

	public void setRecUpdateDt(Date recUpdateDt) {
		this.recUpdateDt = recUpdateDt;
	}

	public String getSupervisorUsersId() {
		return this.supervisorUsersId;
	}

	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public BigInteger getAutogenRolesId() {
		return autogenRolesId;
	}

	public void setAutogenRolesId(BigInteger autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
	}

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
	}

	public String getSupervisorUsersName() {
		return supervisorUsersName;
	}

	public void setSupervisorUsersName(String supervisorUsersName) {
		this.supervisorUsersName = supervisorUsersName;
	}

	public int getDomainId() {
		return domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	public int getBuId() {
		return buId;
	}

	public void setBuId(int buId) {
		this.buId = buId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
}