package com.cognicx.AppointmentRemainder.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognicx.AppointmentRemainder.Request.SkillsetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;

public interface SkillSetService {
	ResponseEntity<GenericResponse> createSkillset(SkillsetRequest skillSetRequest);

	ResponseEntity<GenericResponse> getSkillsetDetail();

	ResponseEntity<GenericResponse> updateSkillset(SkillsetRequest skillSetRequest);
	List<SkillsetRequest> getSkillsetDetList();
}
