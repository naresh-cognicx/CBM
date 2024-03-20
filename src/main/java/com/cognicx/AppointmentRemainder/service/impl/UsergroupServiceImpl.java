package com.cognicx.AppointmentRemainder.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.Request.UsergroupDetRequest;
import com.cognicx.AppointmentRemainder.dao.UsergroupDao;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.UsergroupService;


@Service
public class UsergroupServiceImpl implements UsergroupService {
	private Logger logger = LoggerFactory.getLogger(DispositionServiceImpl.class);
	@Autowired
	UsergroupDao usergroupDao;	
	
	@Override
	public ResponseEntity<GenericResponse> getUsergroupDetail() {
		GenericResponse genericResponse = new GenericResponse();
		List<UsergroupDetRequest> usergroupDetList = null;
		try {
			usergroupDetList = getusergroupDetList();
			genericResponse.setStatus(200);
			genericResponse.setValue(usergroupDetList);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			logger.error("Error in UsergroupServiceImpl::getUsergroupDetail " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	//@Override
	public List<UsergroupDetRequest> getusergroupDetList() {
		List<UsergroupDetRequest> usergroupDetList;
		usergroupDetList = new ArrayList<>();
		List<Object[]> usergroupDetObjList = usergroupDao.getUsergroupDet();
		if (usergroupDetObjList != null && !usergroupDetObjList.isEmpty()) {
			for (Object[] obj : usergroupDetObjList) {
				UsergroupDetRequest usergroupDetRequest = new UsergroupDetRequest();
				usergroupDetRequest.setUsergroupId(String.valueOf(obj[0]));
				usergroupDetRequest.setUsergroupName(String.valueOf(obj[1]));
				usergroupDetRequest.setUsergroupDesc(String.valueOf(obj[2]));
				usergroupDetRequest.setUsergroupType(String.valueOf(obj[3]));
				usergroupDetList.add(usergroupDetRequest);
				logger.info("Usergroup Details :"+usergroupDetRequest.toString());
			}
		}
		return usergroupDetList;
	}

	@Override
	public ResponseEntity<GenericResponse> updateUsergroup(UsergroupDetRequest usergroupDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			boolean isUpdated = usergroupDao.updateUsergroup(usergroupDetRequest);
			if (isUpdated) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Usergroup updated successfully");
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while updating Usergroup");
			}
		} catch (Exception e) {
			logger.error("Error in UsergroupServiceImpl::updateUsergroup " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while updating Usergroup");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponse> createUsergroup(UsergroupDetRequest usergroupDetRequest) {

		GenericResponse genericResponse = new GenericResponse();
		try {
			String usergroupId = usergroupDao.createUsergroup(usergroupDetRequest);
			if (usergroupId != null) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Usergroup created successfully, usergroup ID: " + usergroupId);
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while creating Usergroup");
			}
		} catch (Exception e) {
			logger.error("Error in UsergroupServiceImpl::createCampaign " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while creating Usergroup");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	
	}
}
