//package com.cognicx.AppointmentRemainder.controller;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cognicx.AppointmentRemainder.constant.StatusCodeConstants;
//import com.cognicx.AppointmentRemainder.response.GenericResponse;
//import com.cognicx.AppointmentRemainder.response.UsersResponse;
//import com.cognicx.AppointmentRemainder.service.UserService;
//
//
//
//
//@RestController
//@CrossOrigin
//@RequestMapping("/user")
//public class UsersController {
//	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
//
//
//	@Autowired
//	UserService userService;
//
//	@PostMapping(path = "/getApprovedUsersList")
//	public ResponseEntity<GenericResponse> getUserList() throws Exception {
//
//		GenericResponse genericResponse = new GenericResponse();
//		try {
//			List<UsersResponse> userList = userService.getApprovedUsersList();
//			if (!userList.isEmpty()) {
//				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
//				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
//				genericResponse.setMessage("User list found.");
//				genericResponse.setValue(userList);
//			} else {
//				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
//				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
//				genericResponse.setValue(null);
//				genericResponse.setMessage("User list not found.");
//			}
//		} catch (Exception e) {
//			genericResponse.setStatus(StatusCodeConstants.FAILURE);
//			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
//			genericResponse.setMessage("Unable to fetch user list. Please contact admin.");
//			genericResponse.setValue(null);
//			logger.error("Exception::UsersController.Class::getUserList()", e);
//		}
//		return ResponseEntity.ok(new GenericResponse(genericResponse));
//	}
//
//}
