package com.cognicx.AppointmentRemainder.controller;

import java.io.IOException;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.UserManagementService;
import com.cognicx.AppointmentRemainder.service.impl.UserManagementServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@CrossOrigin
@RequestMapping("/usermanagement")
public class UserManagementController {
    @Autowired
    UserManagementService userManagementService;
    private static Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    @PostMapping("/createUser")
    public ResponseEntity<GenericResponse> createUser(@RequestBody UserManagementDetRequest userDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("Invoking Create user Method:" + userDetRequest.getUserKey());
        return userManagementService.createUser(userDetRequest);
    }

    @GetMapping("/getUserDetail")
    public ResponseEntity<GenericResponse> getUserDetail(@RequestParam String userGroup)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("Invoking Get user Detail for the User Group ID :" + userGroup);
        logger.info("Invoking Get user Detail");
        return userManagementService.getUserDetail(userGroup);
    }
    @GetMapping("/getUserDetailAll")
    public ResponseEntity<GenericResponse> getUserDetail()
            throws ParseException, JsonParseException, JsonMappingException, IOException {
//            logger.info("Invoking Get user Detail for the User Group ID :" + userGroup);
        logger.info("Invoking Get user Detail");
        return userManagementService.getUserDetail();
    }
    @PostMapping("/updateUser")
    public ResponseEntity<GenericResponse> updateUser(@RequestBody UserManagementDetRequest userDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("Invoking Create user Method:" + userDetRequest.getUserKey());
        logger.info("Updating User Detail");
        return userManagementService.updateUser(userDetRequest);
    }

    @GetMapping("/getAvailAgents")
    public ResponseEntity<GenericResponse> getAvailAgents()
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("Invoking Avail Agent Detail");
        return userManagementService.getAvailAgents();
    }

    @GetMapping("/getRoleDetail")
    public ResponseEntity<GenericResponse> getRoleDetail()
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        logger.info("Invoking Avail role Detail");
        return userManagementService.getRoleDetail();
    }
}
