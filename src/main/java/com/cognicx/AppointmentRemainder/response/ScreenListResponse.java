package com.cognicx.AppointmentRemainder.response;

public class ScreenListResponse {
	
	private String screenUId;
	private String screenName;
	private String accessPermission;
	public ScreenListResponse() {}
	
	public ScreenListResponse(String screenUId, String screenName, String accessPermission) {
		this.screenUId = screenUId;
		this.screenName = screenName;
		this.accessPermission = accessPermission;
	}
	
	public String getScreenUId() {
		return screenUId;
	}
	public void setScreenUId(String screenUId) {
		this.screenUId = screenUId;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getAccessPermission() {
		return accessPermission;
	}
	public void setAccessPermission(String accessPermission) {
		this.accessPermission = accessPermission;
	}
	
	
	

}
