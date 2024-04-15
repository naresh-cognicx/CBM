package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class TokenDetailsDto {

	private BigInteger autogenTokenDetailsId;	
	private String employeeId;
	private String token;
	private String refreshToken;
	private Timestamp expiryDate;
	private int expirySeconds;
	private Date recAddDt;
	private Date recUpdateDt;
	private String status;
	private String createdBy;
	private String updatedBy;
	private boolean flag;
	
	public BigInteger getAutogenTokenDetailsId() {
		return autogenTokenDetailsId;
	}

	public void setAutogenTokenDetailsId(BigInteger autogenTokenDetailsId) {
		this.autogenTokenDetailsId = autogenTokenDetailsId;
	}

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

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getExpirySeconds() {
		return expirySeconds;
	}

	public void setExpirySeconds(int expirySeconds) {
		this.expirySeconds = expirySeconds;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}

