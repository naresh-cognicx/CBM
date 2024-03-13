package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

public class UserLeaveDetailsDto {
	
	private BigInteger leaveDetailsId;	
	
	private String fromDate;
	
	private String toDate;
	
	private BigInteger noOfDays;
	
	private String reasons;
	
	private String comments;
	
	private String status;
	
	

	public BigInteger getLeaveDetailsId() {
		return leaveDetailsId;
	}

	public void setLeaveDetailsId(BigInteger leaveDetailsId) {
		this.leaveDetailsId = leaveDetailsId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public BigInteger getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(BigInteger noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
