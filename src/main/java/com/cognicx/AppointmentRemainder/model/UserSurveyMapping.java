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
 * The persistent class for the user_survey_mapping database table.
 * 
 */
@Entity
@Table(name = "user_survey_mapping", schema = "user_rule")
@NamedQuery(name="UserSurveyMapping.findAll", query="SELECT u FROM UserSurveyMapping u")
public class UserSurveyMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="autogen_id")
	private String autogenId;

	@Column(name="autogen_user_id")
	private BigInteger autogenUserId;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="employee_id")
	private String employeeId;

	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="rec_add_dt")
	private Date recAddDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="rec_update_dt")
	private Date recUpdateDt;

	private String status;

	@Column(name="survey_id")
	private int surveyId;

	@Column(name="survey_name")
	private String surveyName;

	@Column(name="updated_by")
	private String updatedBy;

	public UserSurveyMapping() {
	}

	public String getAutogenId() {
		return this.autogenId;
	}

	public void setAutogenId(String autogenId) {
		this.autogenId = autogenId;
	}

	public BigInteger getAutogenUserId() {
		return this.autogenUserId;
	}

	public void setAutogenUserId(BigInteger autogenUserId) {
		this.autogenUserId = autogenUserId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSurveyId() {
		return this.surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyName() {
		return this.surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}