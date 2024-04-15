package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

public class CustomerDataDto {

	private String cutomerDataId;
	private String lastFourDigits;
	private String campaignName;
	private String mobileNumber;
	private String totalDue;
	private String minimumPayment;
	private String dueDate;
	private String status;
	public String getCutomerDataId() {
		return cutomerDataId;
	}
	public void setCutomerDataId(String cutomerDataId) {
		this.cutomerDataId = cutomerDataId;
	}
	public String getLastFourDigits() {
		return lastFourDigits;
	}
	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getTotalDue() {
		return totalDue;
	}
	public void setTotalDue(String totalDue) {
		this.totalDue = totalDue;
	}
	public String getMinimumPayment() {
		return minimumPayment;
	}
	public void setMinimumPayment(String minimumPayment) {
		this.minimumPayment = minimumPayment;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
