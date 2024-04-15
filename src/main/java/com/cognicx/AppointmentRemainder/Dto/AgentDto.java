package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import com.cognicx.AppointmentRemainder.model.Agent;

public class AgentDto {
	
	private String AuditorId;
	private String AuditorName;
	public String AgentId;
	public String AgentName;
	private List<BigInteger> regions;
	private List<BigInteger> centers;
	private List<BigInteger> clients;
	private List<BigInteger> processes;
	public List<Agent> agents = new ArrayList<>();
	public Object resultObj;
	public List<Object[]> resultObjList;
	public boolean flag;
	public String createdBy;
	public String updatedBy;
	
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
	
	
	public List<BigInteger> getRegions() {
		return regions;
	}
	public void setRegions(List<BigInteger> regions) {
		this.regions = regions;
	}
	public List<BigInteger> getCenters() {
		return centers;
	}
	public void setCenters(List<BigInteger> centers) {
		this.centers = centers;
	}
	public List<BigInteger> getClients() {
		return clients;
	}
	public void setClients(List<BigInteger> clients) {
		this.clients = clients;
	}
	public List<BigInteger> getProcesses() {
		return processes;
	}
	public void setProcesses(List<BigInteger> processes) {
		this.processes = processes;
	}
	public List<Agent> getAgents() {
		return agents;
	}
	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}
	public Object getResultObj() {
		return resultObj;
	}
	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}
	public List<Object[]> getResultObjList() {
		return resultObjList;
	}
	public void setResultObjList(List<Object[]> resultObjList) {
		this.resultObjList = resultObjList;
	}
	public String getAuditorId() {
		return AuditorId;
	}
	public void setAuditorId(String auditorId) {
		AuditorId = auditorId;
	}
	public String getAuditorName() {
		return AuditorName;
	}
	public void setAuditorName(String auditorName) {
		AuditorName = auditorName;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
