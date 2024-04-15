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
 * The persistent class for the agent_mapping database table.
 * 
 */
@Entity
@Table(name="agent_mapping")
@NamedQuery(name="AgentMapping.findAll", query="SELECT i FROM AgentMapping i")
public class AgentMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_AGENT_MAPPING_ID")
	private BigInteger autogenAgentMappingId;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="AGENT_ID")
	private String agentId;

	@Column(name="AGENT_NAME")
	private String agentName;
	
	@Column(name="AUDITOR_ID")
	private String auditorId;

	@Column(name="AUDITOR_NAME")
	private String auditorName;
	
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

	public AgentMapping() {
	}

	public BigInteger getAutogenAgentMappingId() {
		return autogenAgentMappingId;
	}

	public void setAutogenAgentMappingId(BigInteger autogenAgentMappingId) {
		this.autogenAgentMappingId = autogenAgentMappingId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
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