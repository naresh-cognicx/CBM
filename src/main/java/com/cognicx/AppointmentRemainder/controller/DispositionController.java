package com.cognicx.AppointmentRemainder.controller;

import java.io.IOException;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cognicx.AppointmentRemainder.Request.DispositionDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.DispositionService;
import com.cognicx.AppointmentRemainder.service.impl.DispositionServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/disposition")
public class DispositionController {
	private static Logger logger = LoggerFactory.getLogger(DispositionServiceImpl.class);

	@Autowired
	DispositionService dispositionService;
	

	@GetMapping("/getDispositionDetail")
	public ResponseEntity<GenericResponse> getDispositionDetail()
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		logger.info("Invoking Get Disposition Detail");
		return dispositionService.getDispositionDetail();
	}
	
	
	@PostMapping("/createDisposition")
	public ResponseEntity<GenericResponse> createDisposition(@RequestBody DispositionDetRequest dispostionDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		logger.info(" Create Dispostion Detail");
		return dispositionService.createDisposition(dispostionDetRequest);
	}
	

	@PostMapping("/updateDisposition")
	public ResponseEntity<GenericResponse> updateDisposition(@RequestBody DispositionDetRequest dispostionDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		logger.info("Updating Dispostion Detail");
		return dispositionService.updateDisposition(dispostionDetRequest);
	}
	
	
	
}
