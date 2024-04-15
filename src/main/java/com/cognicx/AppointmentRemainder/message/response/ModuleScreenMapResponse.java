package com.cognicx.AppointmentRemainder.message.response;

import java.util.Date;

import javax.persistence.Column;

public class ModuleScreenMapResponse {

	private Long moduleScreenMapId;
	private Long moduleMasterId;
	private String moduleUid;
	private String moduleName;
	private Long screenMasterId;
	private String screenUid;
	private String screenName;
	private String status;
	private Date recAddDt;
	private Date recUpdateDt;
	private String createdBy;
	private String updatedBy;
	private String access;
	private String superAdminAccess;

	public Long getModuleScreenMapId() {
		return moduleScreenMapId;
	}

	public void setModuleScreenMapId(Long moduleScreenMapId) {
		this.moduleScreenMapId = moduleScreenMapId;
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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
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
