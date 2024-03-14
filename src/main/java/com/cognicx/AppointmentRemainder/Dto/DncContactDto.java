package com.cognicx.AppointmentRemainder.Dto;

public class DncContactDto {
private String serialnumber;
private String contactNumber;
private String failureReason;
private String campaignId;
public String getCampaignId() {
	return campaignId;
}
public void setCampaignId(String campaignId) {
	this.campaignId = campaignId;
}
public String getFailureReason() {
	return failureReason;
}
public void setFailureReason(String failureReason) {
	this.failureReason = failureReason;
}
public String getSerialnumber() {
	return serialnumber;
}
public void setSerialnumber(String serialnumber) {
	this.serialnumber = serialnumber;
}
public String getContactNumber() {
	return contactNumber;
}
public void setContactNumber(String contactNumber) {
	this.contactNumber = contactNumber;
}
@Override
public String toString() {
	return "DncContactDto [serialnumber=" + serialnumber + ", contactNumber=" + contactNumber + ", failureReason="
			+ failureReason + ", campaignId=" + campaignId + "]";
}


}
