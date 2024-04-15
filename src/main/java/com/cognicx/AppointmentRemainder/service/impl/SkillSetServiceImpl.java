package com.cognicx.AppointmentRemainder.service.impl;

import java.util.ArrayList;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.Request.SkillsetRequest;
import com.cognicx.AppointmentRemainder.dao.SkillsetDao;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.SkillSetService;
@Service
public class SkillSetServiceImpl implements SkillSetService{
	@Autowired
	SkillsetDao skillSetDao;
	private Logger logger = LoggerFactory.getLogger(SkillSetServiceImpl.class);
	
	@Override
	public ResponseEntity<GenericResponse> createSkillset(SkillsetRequest skillSetRequest) {

		GenericResponse genericResponse = new GenericResponse();
		try {
			String skillSetId = skillSetDao.createSkillset(skillSetRequest);
			if (skillSetId != null) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("skill set created successfully, skillset Id: " + skillSetId);
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while creating skillSet");
			}
		} catch (Exception e) {
			logger.error("Error in SkillSetServiceImpl::createskillSetId " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while creating Skill Set");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	
	}

	@Override
	public ResponseEntity<GenericResponse> getSkillsetDetail() {
		GenericResponse genericResponse = new GenericResponse();
		List<SkillsetRequest> skillSetList = null;
		try {
			skillSetList = getSkillsetDetList();
			genericResponse.setStatus(200);
			genericResponse.setValue(skillSetList);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			logger.error("Error in SkillSetServiceImpl::getSkillsetDetail " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponse> updateSkillset(SkillsetRequest skillSetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			boolean isUpdated = skillSetDao.updateSkillset(skillSetRequest);
			if (isUpdated) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Skill set updated successfully");
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while updating Skill set");
			}
		} catch (Exception e) {
			logger.error("Error in SkillSetServiceImpl::updateSkillset " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while updating Skill set");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public List<SkillsetRequest> getSkillsetDetList() {

		List<SkillsetRequest> skillSetDetList;
		skillSetDetList = new ArrayList<>();
		List<Object[]> skillSetDetObjList = skillSetDao.getSkillsetDetail();
		if (skillSetDetObjList != null && !skillSetDetObjList.isEmpty()) {
			for (Object[] obj : skillSetDetObjList) {
				SkillsetRequest skillsetRequest = new SkillsetRequest();
				skillsetRequest.setSkillsetId(String.valueOf(obj[0]));
				skillsetRequest.setSkillName(String.valueOf(obj[1]));
				skillsetRequest.setLanguage(String.valueOf(obj[2]));
				skillsetRequest.setTimeZone(String.valueOf(obj[3]));
				skillsetRequest.setChannelType(String.valueOf(obj[4]));
				skillsetRequest.setServiceLevelThreshold(String.valueOf(obj[5]));
				skillsetRequest.setServiceLevelGoal(String.valueOf(obj[6]));
				skillsetRequest.setFirstCallResolution(String.valueOf(obj[7]));
				skillsetRequest.setAbandonedRateThreshold(String.valueOf(obj[8]));
				skillsetRequest.setShortCallThreshold(String.valueOf(obj[9]));
				skillsetRequest.setShortAbandonedThreshold(String.valueOf(obj[10]));
				skillsetRequest.setCountAbandonedSLA(String.valueOf(obj[11]));
				skillSetDetList.add(skillsetRequest);
				logger.info("Skill set Details :"+skillsetRequest.toString());
			}
		}
		return skillSetDetList;
	
	}
	
}
