package com.cognicx.AppointmentRemainder.Request;

public class CampaignRealTimeDashboard {

	private String campaignId;
	private String campaignName;
	private String campaignStatus;
	private String startDate;
	private String endDate;
	private String listLength;
	private Integer completed;
	private Integer pending;
	private Integer answered;
	private Integer linebusy;
	private Integer noanswer;
	private int oncall;
	private int totalline;
	private int error;
	private String ETC;
	private int DND;
	
	
	

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

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignActive) {
		this.campaignStatus = campaignActive;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getListLength() {
		return listLength;
	}

	public void setListLength(String listLength) {
		this.listLength = listLength;
	}

	public Integer getCompleted() {
		return completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Integer getPending() {
		return pending;
	}

	public void setPending(Integer pending) {
		this.pending = pending;
	}

	public Integer getAnswered() {
		return answered;
	}

	public void setAnswered(Integer answered) {
		this.answered = answered;
	}

	public Integer getLinebusy() {
		return linebusy;
	}

	public void setLinebusy(Integer linebusy) {
		this.linebusy = linebusy;
	}

	public Integer getNoanswer() {
		return noanswer;
	}

	public void setNoanswer(Integer noanswer) {
		this.noanswer = noanswer;
	}

	public int getOncall() {
		return oncall;
	}

	public void setOncall(int oncall) {
		this.oncall = oncall;
	}

	public int getTotalline() {
		return totalline;
	}

	public void setTotalline(int totalline) {
		this.totalline = totalline;
	}

	public int getDND() {
		return DND;
	}

	public void setDND(int dND) {
		DND = dND;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getETC() {
		return ETC;
	}

	public void setETC(String eTC) {
		ETC = eTC;
	}

	
	
}
