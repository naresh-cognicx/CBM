package com.cognicx.AppointmentRemainder.response;

public class AgentResponse {

	private String AgentId;
	private String AgentName;
	private boolean mapped;
	
	public AgentResponse() {}
	public AgentResponse(String AgentId, String AgentName, boolean mapped) {
		this.AgentId =  AgentId;
		this.AgentName = AgentName;
		this.mapped = mapped;
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
	public boolean isMapped() {
		return mapped;
	}
	public void setMapped(boolean mapped) {
		this.mapped = mapped;
	}
	
}
