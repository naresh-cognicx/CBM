package com.cognicx.AppointmentRemainder.model;

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
 * The persistent class for the client_details database table.
 * 
 */
@Entity
@Table(name = "login_details", schema = "appointment_remainder")
@NamedQuery(name="LoginDetails.findAll", query="SELECT l FROM LoginDetails l")
public class LoginDetails {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_LOGIN_DETAILS_ID")
	private BigInteger autogenLoginDetailsId;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeId;
	
		
//	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOGIN_TIME")
	private Date loginTime;


	@Column(name="LOGOUT_TIME")
	private Date logoutTime;
	
	@Column(name="NO_OF_ATTEMPT")
	private BigInteger noOfAttempt;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "source_ip")
	private String sourceIP;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	//@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	public BigInteger getAutogenLoginDetailsId() {
		return autogenLoginDetailsId;
	}

	public void setAutogenLoginDetailsId(BigInteger autogenLoginDetailsId) {
		this.autogenLoginDetailsId = autogenLoginDetailsId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public BigInteger getNoOfAttempt() {
		return noOfAttempt;
	}

	public void setNoOfAttempt(BigInteger noOfAttempt) {
		this.noOfAttempt = noOfAttempt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	@Override
	public String toString() {
		return "LoginDetails [autogenLoginDetailsId=" + autogenLoginDetailsId + ", employeeId=" + employeeId
				+ ", loginTime=" + loginTime + ", logoutTime=" + logoutTime + ", noOfAttempt=" + noOfAttempt
				+ ", remarks=" + remarks + ", userEmail=" + userEmail + ", sourceIP=" + sourceIP + ", createdBy="
				+ createdBy + ", recAddDt=" + recAddDt + ", recUpdateDt=" + recUpdateDt + ", updatedBy=" + updatedBy
				+ "]";
	}

	
	
}
