package com.cognicx.AppointmentRemainder.Request;

import javax.validation.constraints.Min;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ValidateOtpRequest {
	
	@NotNull
	@Min(6)
	@Min(6)
	private Integer otpNumber;
	@NotEmpty
	private String employeeId;
	
	public Integer getOtpNumber() {
		return otpNumber;
	}
	public void setOtpNumber(Integer otpNumber) {
		this.otpNumber = otpNumber;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
}
