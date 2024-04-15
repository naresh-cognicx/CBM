package com.cognicx.AppointmentRemainder.Dto;

public class SurveyContactDetDto {

	private String phone;
	private String actionId;
	private String Survey_Lang;
	private String MainSkillset;
	private String subSkillset;
	private String retryCount;
	private String rec_update_time;

	private String call_status;

	public String getCall_status() {
		return call_status;
	}

	public void setCall_status(String call_status) {
		this.call_status = call_status;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getSurvey_Lang() {
		return Survey_Lang;
	}
	public void setSurvey_Lang(String survey_Lang) {
		Survey_Lang = survey_Lang;
	}
	public String getMainSkillset() {
		return MainSkillset;
	}
	public void setMainSkillset(String mainSkillset) {
		MainSkillset = mainSkillset;
	}
	public String getSubSkillset() {
		return subSkillset;
	}
	public void setSubSkillset(String subSkillset) {
		this.subSkillset = subSkillset;
	}

	public String getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(String retryCount) {
		this.retryCount = retryCount;
	}

	public String getRec_update_time() {
		return rec_update_time;
	}

	public void setRec_update_time(String rec_update_time) {
		this.rec_update_time = rec_update_time;
	}

	@Override
	public String toString() {
		return "SurveyContactDetDto [phone=" + phone + ", actionId=" + actionId + ", Survey_Lang=" + Survey_Lang
				+ ", MainSkillset=" + MainSkillset + ", subSkillset=" + subSkillset + ", retryCount=" + retryCount
				+ ", rec_update_time=" + rec_update_time + ", call_status=" + call_status + "]";
	}
	
	
	
}
