package com.cognicx.AppointmentRemainder.Request;

import java.math.BigInteger;

public class UpdateCallDetRequest {

	private String actionid;
	private String phone;
	private String disposition;
	private String callduration;
	private String hangupcode;
	private int retryCount;

	private BigInteger historyId;

	public BigInteger getHistoryId() {
		return historyId;
	}

	public void setHistoryId(BigInteger historyId) {
		this.historyId = historyId;
	}

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
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getHangupcode() {
		return hangupcode;
	}
	public void setHangupcode(String hangupcode) {
		this.hangupcode = hangupcode;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public String getCallduration() {
		return callduration;
	}
	public void setCallduration(String callduration) {
		this.callduration = callduration;
	}



}
