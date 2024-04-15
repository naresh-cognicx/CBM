package com.cognicx.AppointmentRemainder.message.response;

import java.util.Date;

public class RoleResponse {
	private Long roleId;
	private Date recAddDt;
	private Date recUpdateDt;
	private String rolesName;
	private String description;
	private String roleCreateStatus;
	private String status;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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
}
