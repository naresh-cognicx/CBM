package com.cognicx.AppointmentRemainder.service;

import org.springframework.http.ResponseEntity;

import com.cognicx.AppointmentRemainder.Request.UsergroupDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;

public interface UsergroupService {
	ResponseEntity<GenericResponse> getUsergroupDetail();
	ResponseEntity<GenericResponse> updateUsergroup(UsergroupDetRequest usergroupDetRequest);

	ResponseEntity<GenericResponse> createUsergroup(UsergroupDetRequest usergroupDetRequest);
}
