package com.cognicx.AppointmentRemainder.Request;

import javax.validation.constraints.NotEmpty;

public class LogoutRequest {
	
	@NotEmpty
	private String employeeId;
	@NotEmpty
	private String token;
	
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
