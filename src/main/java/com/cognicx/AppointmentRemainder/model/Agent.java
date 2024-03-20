package com.cognicx.AppointmentRemainder.model;

public class Agent {
	
	private String AgentId;
	private String AgentName;
	
	public Agent(String AgentId, String AgentName) {
		this.AgentId =  AgentId;
		this.AgentName = AgentName;
	}
	public String getAgentId() {
		return AgentId;
	}
	public void setAgentId(String agentId) {
		AgentId = agentId;
	}
	public String getAgentName() {
		return AgentName;
	}
	public void setAgentName(String agentName) {
		AgentName = agentName;
	}
}
