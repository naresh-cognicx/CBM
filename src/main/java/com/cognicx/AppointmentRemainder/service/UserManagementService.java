package com.cognicx.AppointmentRemainder.service;

import java.util.List;

import com.cognicx.AppointmentRemainder.response.FeatureResponse;
import org.springframework.http.ResponseEntity;

import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;

public interface UserManagementService {
	ResponseEntity<GenericResponse> createUser(UserManagementDetRequest userDetRequest);
	ResponseEntity<GenericResponse> getUserDetail();

	ResponseEntity<GenericResponse> getUserDetail(String userGroup);

	ResponseEntity<GenericResponse> updateUser(UserManagementDetRequest userDetRequest);
	ResponseEntity<GenericResponse> getAvailAgents();
	List<UserManagementDetRequest> getUserDetList() throws Exception;
	ResponseEntity<GenericResponse> getRoleDetail();
	ResponseEntity<GenericResponse> validateUserId(UserManagementDetRequest userDetRequest);
	ResponseEntity<GenericResponse> validateUserExtn(UserManagementDetRequest userDetRequest);
	ResponseEntity<GenericResponse> validateUserEmail(UserManagementDetRequest userDetRequest);
	ResponseEntity<GenericResponse> validateUserPhone(UserManagementDetRequest userDetRequest);

	ResponseEntity<GenericResponse> getAgentRealTimeDashboard();

    FeatureResponse getFeatures();
}
