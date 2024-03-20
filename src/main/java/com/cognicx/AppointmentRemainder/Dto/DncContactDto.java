package com.cognicx.AppointmentRemainder.Dto;

public class DncContactDto {
    private String serialnumber;
    private String contactNumber;
    private String DNCID;
    private String failureReason;
    private String campaignId;
    private String dncName;

    public String getDncName() {
        return dncName;
    }

    public void setDncName(String dncName) {
        this.dncName = dncName;
    }

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

    public String getDNCID() {
        return DNCID;
    }

    public void setDNCID(String dNCID) {
        DNCID = dNCID;
    }

    @Override
    public String toString() {
        return "DncContactDto [serialnumber=" + serialnumber + ", contactNumber=" + contactNumber + ", failureReason="
                + failureReason + ", campaignId=" + campaignId + "]";
    }


}
