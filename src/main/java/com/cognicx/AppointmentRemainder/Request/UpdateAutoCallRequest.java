package com.cognicx.AppointmentRemainder.Request;

public class UpdateAutoCallRequest {

	private String actionid;
	private String phone;
	private String callstart;
	private String callanswer;
	private String callend;
	private String callduration;
	private String calltalktime;
	private String disposition;
	private String dialstatus;
	private String hangupcode;
	private String hangupreason;
	private String hanguptext;
	public String getActionid() {
		return actionid;
	}
	public void setActionid(String actionid) {
		this.actionid = actionid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCallstart() {
		return callstart;
	}
	public void setCallstart(String callstart) {
		this.callstart = callstart;
	}
	public String getCallanswer() {
		return callanswer;
	}
	public void setCallanswer(String callanswer) {
		this.callanswer = callanswer;
	}
	public String getCallend() {
		return callend;
	}
	public void setCallend(String callend) {
		this.callend = callend;
	}
	public String getCallduration() {
		return callduration;
	}
	public void setCallduration(String callduration) {
		this.callduration = callduration;
	}

	public String getCalltalktime() {
		return calltalktime;
	}
	public void setCalltalktime(String calltalktime) {
		this.calltalktime = calltalktime;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public String getDialstatus() {
		return dialstatus;
	}
	public void setDialstatus(String dialstatus) {
		this.dialstatus = dialstatus;
	}
	public String getHangupcode() {
		return hangupcode;
	}
	public void setHangupcode(String hangupcode) {
		this.hangupcode = hangupcode;
	}
	public String getHangupreason() {
		return hangupreason;
	}
	public void setHangupreason(String hangupreason) {
		this.hangupreason = hangupreason;
	}
	public String getHanguptext() {
		return hanguptext;
	}
	public void setHanguptext(String hanguptext) {
		this.hanguptext = hanguptext;
	}
}
