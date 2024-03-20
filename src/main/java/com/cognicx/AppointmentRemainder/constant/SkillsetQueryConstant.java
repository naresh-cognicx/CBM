package com.cognicx.AppointmentRemainder.constant;

public class SkillsetQueryConstant {

	public static final String INSERT_SKILLSET_DET = "INSERT INTO appointment_remainder.skillset_det(skillsetId,skillName,Language,TimeZone,ChannelType,ServiceLevelThreshold,ServiceLevelGoal,FirstCallResolution,AbandonedRateThreshold,ShortCallThreshold,ShortAbandonedThreshold,CountAbandonedSLA) "
			+ "VALUES (:skillsetId,:skillName,:Language,:TimeZone,:ChannelType,:ServiceLevelThreshold,:ServiceLevelGoal,:FirstCallResolution,:AbandonedRateThreshold,:ShortCallThreshold,:ShortAbandonedThreshold,:CountAbandonedSLA)";

	public static final String GET_SKILLSET_ID = "select max(SUBSTRING(skillsetId, 3, 100)) from appointment_remainder.skillset_det";

	public static final String GET_SKILLSET_DET = "SELECT skillsetId,skillName,Language,TimeZone,ChannelType,ServiceLevelThreshold,ServiceLevelGoal,FirstCallResolution,AbandonedRateThreshold,ShortCallThreshold,ShortAbandonedThreshold,CountAbandonedSLA FROM appointment_remainder.skillset_det";

	public static final String UPDATE_SKILLSET_DET = "UPDATE appointment_remainder.skillset_det SET skillsetId = :skillsetId,skillName=:skillName,Language=:Language,TimeZone=:TimeZone,ChannelType=:ChannelType,ServiceLevelThreshold=:ServiceLevelThreshold,ServiceLevelGoal=:ServiceLevelGoal,FirstCallResolution=:FirstCallResolution,AbandonedRateThreshold=:AbandonedRateThreshold,ShortCallThreshold=:ShortCallThreshold,ShortAbandonedThreshold=:ShortAbandonedThreshold,CountAbandonedSLA=:CountAbandonedSLA WHERE skillsetId = :skillsetId";

	public static final String VALIDATE_SKILLSET_NAME = "select count(1) from [appointment_remainder].[skillset_det] where skillsetId=:skillsetId";

	
}
