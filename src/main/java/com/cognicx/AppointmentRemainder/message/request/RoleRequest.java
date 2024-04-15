package com.cognicx.AppointmentRemainder.message.request;

import java.util.Date;

import java.util.List;

import com.cognicx.AppointmentRemainder.model.UserScreenMap;
import com.cognicx.AppointmentRemainder.model.UserScreenMapApproved;

public class RoleRequest {
	private Long autogenRolesId;
	private Date recAddDt;
	private Date recUpdateDt;
	private String rolesName;
	private String description;
	private String roleCreateStatus;
	private String status;
	private boolean editFlag;
	private List<UserScreenMap> userScreenMap;
	private List<UserScreenMapApproved> userScreenMapApproved;
	private String createdBy;
	private String updatedBy;
	private String comment;
	private String approvedBy;
	private Date approvedOn;
	private boolean approvedRole;

	public Long getAutogenRolesId() {
		return autogenRolesId;
	}

	public void setAutogenRolesId(Long autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
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

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleCreateStatus() {
		return roleCreateStatus;
	}

	public void setRoleCreateStatus(String roleCreateStatus) {
		this.roleCreateStatus = roleCreateStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<UserScreenMap> getUserScreenMap() {
		return userScreenMap;
	}

	public void setUserScreenMap(List<UserScreenMap> userScreenMap) {
		this.userScreenMap = userScreenMap;
	}

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public boolean isApprovedRole() {
		return approvedRole;
	}

	public void setApprovedRole(boolean approvedRole) {
		this.approvedRole = approvedRole;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public List<UserScreenMapApproved> getUserScreenMapApproved() {
		return userScreenMapApproved;
	}

	public void setUserScreenMapApproved(List<UserScreenMapApproved> userScreenMapApproved) {
		this.userScreenMapApproved = userScreenMapApproved;
	}

}
