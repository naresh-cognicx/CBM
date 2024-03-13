package com.cognicx.AppointmentRemainder.Dto;

public class SurveyTypeDto {
	private int id;
    private String label;
    private String userId;
    private String autogenId;
    private String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAutogenId() {
		return autogenId;
	}
	public void setAutogenId(String autogenId) {
		this.autogenId = autogenId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
