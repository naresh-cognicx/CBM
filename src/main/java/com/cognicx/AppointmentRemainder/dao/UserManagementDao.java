package com.cognicx.AppointmentRemainder.dao;

import java.util.List;
import java.util.Map;

import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;


public interface UserManagementDao {

	String createUser(UserManagementDetRequest userDetRequest) throws Exception;
	List<Object[]> getUserDetail();
	boolean updateUser(UserManagementDetRequest userDetRequest) throws Exception;
	 List<Object[]> getAvailAgent();
	 List<Object[]> getRoleDetail();
}
