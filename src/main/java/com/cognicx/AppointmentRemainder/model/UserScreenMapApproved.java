package com.cognicx.AppointmentRemainder.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_screen_map_approved", schema = "user_rule")
@NamedQuery(name = "UserScreenMapApproved.findAll", query = "SELECT ui FROM UserScreenMapApproved ui")
public class UserScreenMapApproved implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUTOGEN_USER_SCREEN_MAP_ID")
	private Long roleScreenMapId;

//	@Column(name = "AUTOGEN_ROLE_ID")
//	private Long roleId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "AUTOGEN_ROLE_ID", nullable = false)
	private RolesApproved role;

	@Column(name = "ROLES_NAME")
	private String roleName;

	@Column(name = "AUTOGEN_MODULE_MASTER_ID")
	private Long moduleMasterId;

	@Column(name = "MODULE_UID")
	private String moduleUid;

	@Column(name = "MODULE_NAME")
	private String moduleName;

	@Column(name = "AUTOGEN_SCREEN_MASTER_ID")
	private Long screenMasterId;

	@Column(name = "SCREEN_UID")
	private String screenUid;

	@Column(name = "SCREEN_NAME")
	private String screenName;

	@Column(name = "ACCESS_PERMISSION")
	private String accessPermission;

	private String status;

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

	public UserScreenMapApproved() {

	}

	public Long getRoleScreenMapId() {
		return roleScreenMapId;
	}

	public void setRoleScreenMapId(Long roleScreenMapId) {
		this.roleScreenMapId = roleScreenMapId;
	}

//	public Long getRoleId() {
//		return roleId;
//	}
//
//	public void setRoleId(Long roleId) {
//		this.roleId = roleId;
//	}

	public String getRoleName() {
		return roleName;
	}

	public RolesApproved getRole() {
		return role;
	}

	public void setRole(RolesApproved role) {
		this.role = role;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getAccessPermission() {
		return accessPermission;
	}

	public void setAccessPermission(String accessPermission) {
		this.accessPermission = accessPermission;
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
}
