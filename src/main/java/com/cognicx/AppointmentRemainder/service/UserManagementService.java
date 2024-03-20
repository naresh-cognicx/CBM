package com.cognicx.AppointmentRemainder.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

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
}
