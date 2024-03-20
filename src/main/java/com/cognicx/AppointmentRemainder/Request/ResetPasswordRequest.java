package com.cognicx.AppointmentRemainder.Request;

import javax.validation.constraints.NotEmpty;

public class ResetPasswordRequest {
	
	@NotEmpty
	private String employeeId;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String confirmPassword;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
