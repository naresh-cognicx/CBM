package com.cognicx.AppointmentRemainder.util;

import java.io.Serializable;
import java.math.BigInteger;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Scope(value= WebApplicationContext.SCOPE_REQUEST, proxyMode= ScopedProxyMode.TARGET_CLASS)
@Component
public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3148161002061901806L;
	private BigInteger autogenUsersId;
	private BigInteger autogenUserDetailsId;
	private String email;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String supervisorUsersId;    
	private BigInteger autogenRolesId;
	private String rolesName;
	
	public BigInteger getAutogenUsersId() {
		return autogenUsersId;
	}
	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
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
	public String getSupervisorUsersId() {
		return supervisorUsersId;
	}
	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}
	public BigInteger getAutogenRolesId() {
		return autogenRolesId;
	}
	public void setAutogenRolesId(BigInteger autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
	}
	public String getRolesName() {
		return rolesName;
	}
	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigInteger getAutogenUserDetailsId() {
		return autogenUserDetailsId;
	}
	public void setAutogenUserDetailsId(BigInteger autogenUserDetailsId) {
		this.autogenUserDetailsId = autogenUserDetailsId;
	}
	
}
