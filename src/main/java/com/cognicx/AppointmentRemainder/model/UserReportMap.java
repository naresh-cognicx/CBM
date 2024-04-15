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
 * The persistent class for the user_report_map database table.
 * 
 */
@Entity
@Table(name = "user_report_map", schema = "user_rule")
@NamedQuery(name="UserReportMap.findAll", query="SELECT u FROM UserReportMap u")
public class UserReportMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_USER_REPORT_MAP_ID")
	private BigInteger autogenUserReportMapId;

	@Column(name="AUTOGEN_USERS_DETAILS_ID")
	private BigInteger autogenUsersDetailsId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="AUTOGEN_REPORT_MASTER_ID")
	private BigInteger autogenReportMasterId;

	@Column(name="REPORT_NAME")
	private String reportName;
	
	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	private String status;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	public UserReportMap() {
	}

	public BigInteger getAutogenUserReportMapId() {
		return autogenUserReportMapId;
	}

	public void setAutogenUserReportMapId(BigInteger autogenUserReportMapId) {
		this.autogenUserReportMapId = autogenUserReportMapId;
	}

	public BigInteger getAutogenUsersDetailsId() {
		return autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(BigInteger autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public BigInteger getAutogenReportMasterId() {
		return autogenReportMasterId;
	}

	public void setAutogenReportMasterId(BigInteger autogenReportMasterId) {
		this.autogenReportMasterId = autogenReportMasterId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}