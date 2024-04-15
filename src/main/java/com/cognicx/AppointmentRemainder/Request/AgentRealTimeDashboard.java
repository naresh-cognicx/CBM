package com.cognicx.AppointmentRemainder.Request;

public class AgentRealTimeDashboard {
	private String userId;
	private String device;
	private String currentStatus;
	private String currentDuration;
	private boolean isLogin;
	private String lastLogoutTime;
	private String activeloginDuration;
	private String ShortBreak;
	private String Notready;
	private String Aftercallwork;
	private String loginTime;
	private String callsOffered;
	private String callsAnswered;
	private String callsAbondend;
	private String avgTalkTime;
	private String avgHandlingTime;
	private String campiagnSkillset;
	private String staffTime;

	public String getCallsOffered() {
		return callsOffered;
	}

	public void setCallsOffered(String callsOffered) {
		this.callsOffered = callsOffered;
	}

	public String getCallsAnswered() {
		return callsAnswered;
	}

	public void setCallsAnswered(String callsAnswered) {
		this.callsAnswered = callsAnswered;
	}

	public String getCallsAbondend() {
		return callsAbondend;
	}

	public void setCallsAbondend(String callsAbondend) {
		this.callsAbondend = callsAbondend;
	}

	public String getAvgTalkTime() {
		return avgTalkTime;
	}

	public void setAvgTalkTime(String avgTalkTime) {
		this.avgTalkTime = avgTalkTime;
	}

	public String getAvgHandlingTime() {
		return avgHandlingTime;
	}


	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public void setAvgHandlingTime(String avgHandlingTime) {
		this.avgHandlingTime = avgHandlingTime;
	}

	public String getCampiagnSkillset() {
		return campiagnSkillset;
	}

	public void setCampiagnSkillset(String campiagnSkillset) {
		this.campiagnSkillset = campiagnSkillset;
	}

	public String getStaffTime() {
		return staffTime;
	}

	public void setStaffTime(String staffTime) {
		this.staffTime = staffTime;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getCurrentDuration() {
		return currentDuration;
	}
	public void setCurrentDuration(String currentDuration) {
		this.currentDuration = currentDuration;
	}
	public String getLastLogoutTime() {
		return lastLogoutTime;
	}
	public void setLastLogoutTime(String lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}
	public String getActiveloginDuration() {
		return activeloginDuration;
	}
	public void setActiveloginDuration(String activeloginDuration) {
		this.activeloginDuration = activeloginDuration;
	}

	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getShortBreak() {
		return ShortBreak;
	}
	public void setShortBreak(String shortBreak) {
		ShortBreak = shortBreak;
	}
	public String getNotready() {
		return Notready;
	}
	public void setNotready(String notready) {
		Notready = notready;
	}
	public String getAftercallwork() {
		return Aftercallwork;
	}
	public void setAftercallwork(String aftercallwork) {
		Aftercallwork = aftercallwork;
	}
	
}
