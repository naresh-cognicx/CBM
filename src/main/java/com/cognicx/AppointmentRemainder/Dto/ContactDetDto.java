package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

public class ContactDetDto {

	private String contactId;
	private String campaignId;
	private String campaignName;
	private String productID;
	private String language;
	private String subskill_set;
	private String contactNo;
	private String appointmentDate;
	private String callRetryCount;
	private String updatedDate;
	private String callStatus;
	private String failureReason;
	private BigInteger historyId;
	
	private String customerMobileNumber;
	private String lastFourDigits;
	private String totalDue;
	private String minPayment;
	private String dueDate;

	

	public String getSubskill_set() {
		return subskill_set;
	}

	public void setSubskill_set(String subskill_set) {
		this.subskill_set = subskill_set;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String doctorName) {
		this.productID = doctorName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getCallRetryCount() {
		return callRetryCount;
	}

	public void setCallRetryCount(String callRetryDelay) {
		this.callRetryCount = callRetryDelay;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public BigInteger getHistoryId() {
		return historyId;
	}

	public void setHistoryId(BigInteger historyId) {
		this.historyId = historyId;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}

	public String getTotalDue() {
		return totalDue;
	}

	public void setTotalDue(String totalDue) {
		this.totalDue = totalDue;
	}

	public String getMinPayment() {
		return minPayment;
	}

	public void setMinPayment(String minPayment) {
		this.minPayment = minPayment;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "ContactDetDto [contactId=" + contactId + ", campaignId=" + campaignId + ", campaignName=" + campaignName
				+ ", productID=" + productID + ", language=" + language + ", patientName=" + subskill_set
				+ ", contactNo=" + contactNo + ", appointmentDate=" + appointmentDate + ", callRetryCount="
				+ callRetryCount + ", updatedDate=" + updatedDate + ", callStatus=" + callStatus + ", failureReason="
				+ failureReason + ", historyId=" + historyId + ", customerMobileNumber=" + customerMobileNumber
				+ ", lastFourDigits=" + lastFourDigits + ", totalDue=" + totalDue + ", minPayment=" + minPayment
				+ ", dueDate=" + dueDate + "]";
	}
	
	
	
}
