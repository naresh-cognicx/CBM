package com.cognicx.AppointmentRemainder.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.Request.AvailAgentDetail;
import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.dao.UserManagementDao;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.UserManagementService;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	UserManagementDao userManagementDao;
	private Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);
	
	@Override
	public ResponseEntity<GenericResponse> createUser(UserManagementDetRequest userDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			String userKey = userManagementDao.createUser(userDetRequest);
			if (userKey != null) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("user created successfully, user key:  " + userKey);
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while creating user");
			}
		} catch (Exception e) {
			logger.error("Error in UserManagementServiceImpl::createUser " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while creating User");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GenericResponse> getUserDetail() {

		GenericResponse genericResponse = new GenericResponse();
		List<UserManagementDetRequest> userDetList = null;
		try {
			userDetList = getUserDetList();
			genericResponse.setStatus(200);
			genericResponse.setValue(userDetList);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			logger.error("Error in UserManagementServiceImpl::getUserDetail " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}




	@Override
	public ResponseEntity<GenericResponse> updateUser(UserManagementDetRequest userDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			boolean isUpdated = userManagementDao.updateUser(userDetRequest);
			if (isUpdated) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("user updated successfully");
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while updating user");
			}
		} catch (Exception e) {
			logger.error("Error in UserManagementServiceImpl::updateUser " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while updating user");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	@Override
	public List<UserManagementDetRequest> getUserDetList() {
		List<UserManagementDetRequest> userDetList;
		userDetList = new ArrayList<>();
		List<Object[]> userDetObjList = userManagementDao.getUserDetail();
		if (userDetObjList != null && !userDetObjList.isEmpty()) {
			for (Object[] obj : userDetObjList) {
				UserManagementDetRequest userDetRequest = new UserManagementDetRequest();
				userDetRequest.setUserKey(String.valueOf(obj[0]));
				userDetRequest.setFirstName(String.valueOf(obj[1]));
				userDetRequest.setLastName(String.valueOf(obj[2]));
				userDetRequest.setEmailId(String.valueOf(obj[3]));
				userDetRequest.setMobNum(String.valueOf(obj[4]));
				userDetRequest.setUserId(String.valueOf(obj[5]));
				userDetRequest.setPassword(String.valueOf(obj[6]));
				userDetRequest.setRole(String.valueOf(obj[7]));
				userDetRequest.setPbxExtn(String.valueOf(obj[8]));
				userDetRequest.setSkillSet(String.valueOf(obj[9]));
				userDetRequest.setAgent(String.valueOf(obj[10]));
				userDetRequest.setUserGroup(String.valueOf(obj[11]));
				userDetList.add(userDetRequest);
				logger.info("user Details :"+userDetRequest.toString());
			}
		}
		return userDetList;
	}

	
	@Override
	public ResponseEntity<GenericResponse> getAvailAgents() {

		GenericResponse genericResponse = new GenericResponse();
		List<AvailAgentDetail> availAgentList = null;
		try {
			availAgentList = getAvailAgentList();
			genericResponse.setStatus(200);
			genericResponse.setValue(availAgentList);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error in UserManagementServiceImpl::get Avail Agent " + str.toString());
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	
	
	public List<AvailAgentDetail> getAvailAgentList() {
		List<AvailAgentDetail> availAgentList;
		availAgentList = new ArrayList<>();
		 List<Object[]> agentObjList = userManagementDao.getAvailAgent();
		if (agentObjList != null && !agentObjList.isEmpty()) {
			
			for (Object[] obj:agentObjList) {
				AvailAgentDetail agentDetail=new AvailAgentDetail();
				agentDetail.setFirstName(String.valueOf(obj[0]));
				agentDetail.setLastName(String.valueOf(obj[1]));
				agentDetail.setUserId(String.valueOf(obj[2]));
				agentDetail.setAgent(String.valueOf(obj[0]));
				availAgentList.add(agentDetail);
			}
			logger.info("Available Details :"+availAgentList.toString());
		}
		return availAgentList;
	}

	@Override
	public ResponseEntity<GenericResponse> getRoleDetail() {

		GenericResponse genericResponse = new GenericResponse();
		List<Map<String,String>> availRoles = null;
		try {
			availRoles = getAvailRole();
			genericResponse.setStatus(200);
			genericResponse.setValue(availRoles);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error in UserManagementServiceImpl::get role detail " + str.toString());
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

	
	private List<Map<String,String>>  getAvailRole(){
		 Map<String,String> rolemap=null;
		List<Map<String,String>> rolesMapList=new ArrayList<>();
		 try {
			
			 List<Object[]> roleDetails =  userManagementDao.getRoleDetail();
				if (roleDetails != null && !roleDetails.isEmpty()) {
					for (Object[] obj : roleDetails) {
						rolemap=new HashMap<>();
						rolemap.put("role_id",String.valueOf(obj[0]));
						rolemap.put("role",String.valueOf(obj[1]));
						rolesMapList.add(rolemap);
					}
				}
				logger.info("Roles Map List :"+rolesMapList.toString());
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return rolesMapList;
	}
	
}
