package com.cognicx.AppointmentRemainder.Request;

public class SkillsetRequest {
	private String skillsetId;
	private String skillName;
	private String Language;
	private String TimeZone;
	private String ChannelType;
	private String ServiceLevelThreshold;
	private String ServiceLevelGoal;
	private String FirstCallResolution;
	private String AbandonedRateThreshold;
	private String ShortCallThreshold;
	private String ShortAbandonedThreshold;
	private String CountAbandonedSLA;
	public String getSkillsetId() {
		return skillsetId;
	}
	public void setSkillsetId(String skillsetId) {
		this.skillsetId = skillsetId;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public String getLanguage() {
		return Language;
	}
	public void setLanguage(String language) {
		Language = language;
	}
	public String getTimeZone() {
		return TimeZone;
	}
	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}
	public String getChannelType() {
		return ChannelType;
	}
	public void setChannelType(String channelType) {
		ChannelType = channelType;
	}
	public String getServiceLevelThreshold() {
		return ServiceLevelThreshold;
	}
	public void setServiceLevelThreshold(String serviceLevelThreshold) {
		ServiceLevelThreshold = serviceLevelThreshold;
	}
	public String getServiceLevelGoal() {
		return ServiceLevelGoal;
	}
	public void setServiceLevelGoal(String serviceLevelGoal) {
		ServiceLevelGoal = serviceLevelGoal;
	}
	public String getFirstCallResolution() {
		return FirstCallResolution;
	}
	public void setFirstCallResolution(String firstCallResolution) {
		FirstCallResolution = firstCallResolution;
	}
	public String getAbandonedRateThreshold() {
		return AbandonedRateThreshold;
	}
	public void setAbandonedRateThreshold(String abandonedRateThreshold) {
		AbandonedRateThreshold = abandonedRateThreshold;
	}
	public String getShortCallThreshold() {
		return ShortCallThreshold;
	}
	public void setShortCallThreshold(String shortCallThreshold) {
		ShortCallThreshold = shortCallThreshold;
	}
	public String getShortAbandonedThreshold() {
		return ShortAbandonedThreshold;
	}
	public void setShortAbandonedThreshold(String shortAbandonedThreshold) {
		ShortAbandonedThreshold = shortAbandonedThreshold;
	}
	public String getCountAbandonedSLA() {
		return CountAbandonedSLA;
	}
	public void setCountAbandonedSLA(String countAbandonedSLA) {
		CountAbandonedSLA = countAbandonedSLA;
	}
	
}
