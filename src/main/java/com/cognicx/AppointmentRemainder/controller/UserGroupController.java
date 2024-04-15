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
import com.cognicx.AppointmentRemainder.Request.UsergroupDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.UsergroupService;
import com.cognicx.AppointmentRemainder.service.impl.UsergroupServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/usergroup")
public class UserGroupController {
	@Autowired
	UsergroupService usergroupService;
	private static Logger logger = LoggerFactory.getLogger(UsergroupServiceImpl.class);

	@GetMapping("/getusergroupDetail")
	public ResponseEntity<GenericResponse> getUsergroupDetail()
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		System.out.println("Invoking Get User Details");
		logger.info("Invoking Get usergroup Detail");
		return usergroupService.getUsergroupDetail();
	}
	
	@PostMapping("/updateUsergroup")
	public ResponseEntity<GenericResponse> updateUsergroup(@RequestBody UsergroupDetRequest usergroupDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		logger.info("Updating usergroup Detail");
		return usergroupService.updateUsergroup(usergroupDetRequest);
	}
	
	@PostMapping("/createUsergroup")
	public ResponseEntity<GenericResponse> createUsergroup(@RequestBody UsergroupDetRequest usergroupDetRequest)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		return usergroupService.createUsergroup(usergroupDetRequest);
	}

	
}
