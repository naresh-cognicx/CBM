package com.cognicx.AppointmentRemainder.message.response;

import java.util.List;

public class AgentDetResponse {
	
	private String agentId;
	private String agentName;
	private String auditorId;
	private String auditorName;
	private List<String> categories;
	
	public AgentDetResponse(String agentId, String agentName, String auditorId, String auditorName, List<String> categories) {
		this.agentId = agentId;
		this.agentName = agentName;
		this.categories = categories;
		this.auditorId = auditorId;
		this.auditorName = auditorName;
	}
	
	public AgentDetResponse() {}
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	
	
}
