package com.cognicx.AppointmentRemainder.dao;

import java.util.List;

import com.cognicx.AppointmentRemainder.Request.SkillsetRequest;

public interface SkillsetDao {
	String createSkillset(SkillsetRequest skillSetRequest) throws Exception;

	List<Object[]> getSkillsetDetail();

	boolean updateSkillset(SkillsetRequest skillSetRequest) throws Exception;
	//boolean validateCampaignName(SkillsetRequest skillSetRequest);


	
}
