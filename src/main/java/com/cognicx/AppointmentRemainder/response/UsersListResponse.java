package com.cognicx.AppointmentRemainder.response;

import java.math.BigInteger;

public class UsersListResponse {
	
	private BigInteger id;
	private String employeeId;
	
	public UsersListResponse() {}
	
	public UsersListResponse(BigInteger id, String employeeId) {
		this.id = id;
		this.employeeId = employeeId;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

}
