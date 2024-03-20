package com.cognicx.AppointmentRemainder.Request;

public class SurveyDetRequest {
	//Added on 12032024
	private String phone;
	private String actionId;
	private String Survey_Lang;
	private String MainSkillset;
	private String subSkillset;
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
}

