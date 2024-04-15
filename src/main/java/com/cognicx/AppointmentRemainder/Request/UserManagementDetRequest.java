package com.cognicx.AppointmentRemainder.Request;

import java.util.List;

public class UserManagementDetRequest {
	private String userKey;
	private String firstName;
	private String lastName;
	private String emailId;
	private String mobNum;
	private String userId;
	private String password;
	private String role;
	private String pbxExtn;
	private String skillSet;
	private String agent;

	private List<AgentDetail> agentDetails;

	private String status;

	
	public List<AgentDetail> getAgentDetails() {
		return agentDetails;
	}
	public void setAgentDetails(List<AgentDetail> agentDetails) {
		this.agentDetails = agentDetails;
	}
	private String userGroup;
	public String getUserKey() {
		return userKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobNum() {
		return mobNum;
	}
	public void setMobNum(String mobNum) {
		this.mobNum = mobNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPbxExtn() {
		return pbxExtn;
	}
	public void setPbxExtn(String pbxExtn) {
		this.pbxExtn = pbxExtn;
	}
	public String getSkillSet() {
		return skillSet;
	}
	public void setSkillSet(String skillSet) {
		this.skillSet = skillSet;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	
}
	
