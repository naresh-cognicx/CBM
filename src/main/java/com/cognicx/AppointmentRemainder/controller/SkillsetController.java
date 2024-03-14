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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognicx.AppointmentRemainder.Request.SkillsetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.SkillSetService;
import com.cognicx.AppointmentRemainder.service.impl.SkillSetServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/skillset")
public class SkillsetController {

	@Autowired
	SkillSetService skillsetService;
	
	private static Logger logger = LoggerFactory.getLogger(SkillSetServiceImpl.class);
	
	@PostMapping("/createSkillset")
	public ResponseEntity<GenericResponse> createSkillset(@RequestBody SkillsetRequest skillsetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return skillsetService.createSkillset(skillsetRequest);
	}

	@GetMapping("/getSkillsetDetail")
	public ResponseEntity<GenericResponse> getSkillsetDetail()
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		logger.info("Invoking Get skillset Detail");
		return skillsetService.getSkillsetDetail();
	}

	@PostMapping("/updateSkillset")
	public ResponseEntity<GenericResponse> updateSkillset(@RequestBody SkillsetRequest skillsetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		logger.info("Updating Skillset Detail");
		return skillsetService.updateSkillset(skillsetRequest);
	}

}
