package com.cognicx.AppointmentRemainder.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.response.FeatureResponse;


public interface UserManagementDao {

	String createUser(UserManagementDetRequest userDetRequest) throws Exception;
	List<Object[]> getUserDetail();
	List<Object[]> getUserDetail(String userGroup);
	boolean updateUser(UserManagementDetRequest userDetRequest) throws Exception;
	 List<Object[]> getAvailAgent();
	 List<Object[]> getRoleDetail();
	 List<Object[]> getRTAgentDetail();
	 /* Added on 16th March 2024 */
	 Optional<UserDto> findByUsername(String username) throws Exception;
	 
	 List<Object[]> getAgentDetail(String userID);
	 boolean validateUserId(UserManagementDetRequest userDetRequest) throws Exception;
	 boolean validateUserExtn(UserManagementDetRequest userDetRequest) throws Exception;
	 boolean validateUserEmail(UserManagementDetRequest userDetRequest) throws Exception;
	 boolean validateUserPhone(UserManagementDetRequest userDetRequest) throws Exception;

	boolean updateUserByPassword(UserManagementDetRequest userDetRequest);

    FeatureResponse getFeatures();
}
