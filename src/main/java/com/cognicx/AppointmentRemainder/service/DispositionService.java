package com.cognicx.AppointmentRemainder.service;

import org.springframework.http.ResponseEntity;

import com.cognicx.AppointmentRemainder.Request.DispositionDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;

public interface DispositionService {
	ResponseEntity<GenericResponse> getDispositionDetail();
	
	
	ResponseEntity<GenericResponse> createDisposition(DispositionDetRequest dispostionDetRequest);
	ResponseEntity<GenericResponse> updateDisposition(DispositionDetRequest dispostionDetRequest);

}
