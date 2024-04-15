package com.cognicx.AppointmentRemainder.ami;

import org.asteriskjava.manager.event.UserEvent;

public class progressiveStatusEvent extends UserEvent{
	public progressiveStatusEvent(Object source) {
		super(source);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 7542310714487513618L;
	/**
	 * 
	 */
	private String calluid;
	private String agent;
	private String phone;
	private String agentstate;
	private String customerstatus;
	private String callmode;
	private String campaingnname;
	public String getCalluid() {
		return calluid;
	}
	public void setCalluid(String calluid) {
		this.calluid = calluid;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAgentstate() {
		return agentstate;
	}
	public void setAgentstate(String agentstate) {
		this.agentstate = agentstate;
	}
	public String getCustomerstatus() {
		return customerstatus;
	}
	public void setCustomerstatus(String customerstatus) {
		this.customerstatus = customerstatus;
	}
	public String getCallmode() {
		return callmode;
	}
	public void setCallmode(String callmode) {
		this.callmode = callmode;
	}
	public String getCampaingnname() {
		return campaingnname;
	}
	public void setCampaingnname(String campaingnname) {
		this.campaingnname = campaingnname;
	}
	

}
