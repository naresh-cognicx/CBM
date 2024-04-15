package com.cognicx.AppointmentRemainder.response;

import java.math.BigInteger;

public class RolesResponse {
	
	private BigInteger roleId;
	private String rolesName;
	
	public RolesResponse() {};
	
	public RolesResponse(BigInteger roleId, String rolesName) {
		this.roleId = roleId;
		this.rolesName = rolesName;
	}
	public BigInteger getRoleId() {
		return roleId;
	}
	public void setRoleId(BigInteger roleId) {
		this.roleId = roleId;
	}
	public String getRolesName() {
		return rolesName;
	}
	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}
	
}
