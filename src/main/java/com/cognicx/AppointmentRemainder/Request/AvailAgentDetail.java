package com.cognicx.AppointmentRemainder.Request;

public class AvailAgentDetail {
	private String Agent;
	private String UserId;
	private String FirstName;
	private String LastName;
	
	public String getAgent() {
		return Agent;
	}
	
	public void setAgent(String agent) {
		Agent = agent;
	}
	
	public String getUserId() {
		return UserId;
	}
	
	public void setUserId(String userId) {
		UserId = userId;
	}
	
	public String getFirstName() {
		return FirstName;
	}
	
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	
	public String getLastName() {
		return LastName;
	}
	
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
}
