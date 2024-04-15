package com.cognicx.AppointmentRemainder.model;

import java.io.Serializable;
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
@Table(name = "screen_map", schema = "user_rule")
@NamedQuery(name = "ModuleScreenMap.findAll", query = "SELECT ui FROM ModuleScreenMap ui")
public class ModuleScreenMap implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUTOGEN_SCREEN_MAP_ID")
	private Long moduleScreenMapId;

	@Column(name = "AUTOGEN_MODULE_MASTER_ID")
	private Long moduleMasterId;

	@Column(name = "MODULE_UID")
	private String moduleUid;

	@Column(name = "module_Name")
	private String moduleName;

	@Column(name = "AUTOGEN_SCREEN_MASTER_ID")
	private Long screenMasterId;

	@Column(name = "SCREEN_UID")
	private String screenUid;

	@Column(name = "screen_Name")
	private String screenName;

	private String status;

	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_ADD_DT")
	private Date recAddDt;

	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "ACCESS_PERMISSION")
	private String access;
	
	@Column(name = "SUPER_ADMIN_ACCESS")
	private String superAdminAccess;

	public ModuleScreenMap() {

	}

	public Long getModuleMasterId() {
		return moduleMasterId;
	}

	public void setModuleMasterId(Long moduleMasterId) {
		this.moduleMasterId = moduleMasterId;
	}

	public String getModuleUid() {
		return moduleUid;
	}

	public void setModuleUid(String moduleUid) {
		this.moduleUid = moduleUid;
	}

	public Long getScreenMasterId() {
		return screenMasterId;
	}

	public void setScreenMasterId(Long screenMasterId) {
		this.screenMasterId = screenMasterId;
	}

	public String getScreenUid() {
		return screenUid;
	}

	public void setScreenUid(String screenUid) {
		this.screenUid = screenUid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Long getModuleScreenMapId() {
		return moduleScreenMapId;
	}

	public void setModuleScreenMapId(Long moduleScreenMapId) {
		this.moduleScreenMapId = moduleScreenMapId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getSuperAdminAccess() {
		return superAdminAccess;
	}

	public void setSuperAdminAccess(String superAdminAccess) {
		this.superAdminAccess = superAdminAccess;
	}

}
