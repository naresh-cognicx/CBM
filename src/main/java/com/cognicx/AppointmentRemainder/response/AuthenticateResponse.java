package com.cognicx.AppointmentRemainder.response;

import java.math.BigInteger;

public class AuthenticateResponse {
	
	private BigInteger usersId;
	private String email;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String status;
	
	public AuthenticateResponse(BigInteger usersId, String email, String employeeId, String firstName, String lastName,
			String mobileNumber, String status) {
		this.usersId = usersId;
		this.email = email;
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.status = status;
	}
	
	public BigInteger getUsersId() {
		return usersId;
	}
	public void setUsersId(BigInteger usersId) {
		this.usersId = usersId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
