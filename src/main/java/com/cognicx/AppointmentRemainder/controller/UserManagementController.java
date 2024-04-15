package com.cognicx.AppointmentRemainder.controller;

import java.io.IOException;

import java.text.ParseException;

import com.cognicx.AppointmentRemainder.response.FeatureResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.ChangePasswordRequest;
import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.constant.StatusCodeConstants;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.UserManagementService;
import com.cognicx.AppointmentRemainder.service.UserService;
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
    
    
    @Autowired
	PasswordEncoder encoder;
    

	@Autowired
	UserService userService;


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
    
    

	public ResponseEntity<GenericResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			logger.info("Change Password Request New Password:"+changePasswordRequest.getNewPassword());
			if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
				UserDto userDto = new UserDto();
				userDto.setEmployeeId(changePasswordRequest.getUserId());
				userDto.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
				boolean resetStatus = userService.changePassword(userDto);
				if (resetStatus) {
					genericResponse.setStatus(StatusCodeConstants.SUCCESS);
					genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
					genericResponse.setMessage("Change Password successfully.");
					genericResponse.setValue(null);
				} else {
					genericResponse.setStatus(StatusCodeConstants.FAILURE);
					genericResponse.setError(StatusCodeConstants.FAILURE_STR);
					genericResponse.setMessage("Change password failure. Please contact admin.");
					genericResponse.setValue(null);
					logger.error("Exception::Change password failure. Please contact admin.");
				}
			} else {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("The Confirm password confirmation does not match.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Change password failure. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UserAuthentication.Class:changePassword()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    @PostMapping("/validateUserId")
    public ResponseEntity<GenericResponse> validateUserId(@RequestBody UserManagementDetRequest userDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return userManagementService.validateUserId(userDetRequest);
    }
    
    @PostMapping("/validateUserExtn")
    public ResponseEntity<GenericResponse> validateUserExtn(@RequestBody UserManagementDetRequest userDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return userManagementService.validateUserExtn(userDetRequest);
    }
    @PostMapping("/validateUserEmail")
    public ResponseEntity<GenericResponse> validateUserEmail(@RequestBody UserManagementDetRequest userDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return userManagementService.validateUserEmail(userDetRequest);
    }
    @PostMapping("/validateUserPhone")
    public ResponseEntity<GenericResponse> validateUserPhone(@RequestBody UserManagementDetRequest userDetRequest)
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return userManagementService.validateUserPhone(userDetRequest);
    }
    
    @GetMapping("/getAgentRealTimeDashboard")
    public ResponseEntity<GenericResponse> getAgentRealTimeDashboard()
            throws ParseException, JsonParseException, JsonMappingException, IOException {
        return userManagementService.getAgentRealTimeDashboard();
    }
    @GetMapping("/features")
    public ResponseEntity<GenericResponse> getFeatures(){
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse.setValue(userManagementService.getFeatures());
            genericResponse.setStatus(200);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in UserManagementServiceImpl::getFeatures " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }
}
