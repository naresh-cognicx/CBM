package com.cognicx.AppointmentRemainder.Request;
public class DNCDetRequest {
		private String DNCID;
     private String DncName;
     private String description;
	
	public String getDncName() {
		return DncName;
	}
	public void setDncName(String dncName) {
		DncName = dncName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDNCID() {
		return DNCID;
	}
	public void setDNCID(String dNCID) {
		DNCID = dNCID;
	}
	@Override
	public String toString() {
		return "DNSDetRequest [DNCID=" + DNCID + ", DncName=" + DncName + ", description=" + description + "]";
	}
	
	
     
     
}