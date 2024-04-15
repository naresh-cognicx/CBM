package com.cognicx.AppointmentRemainder.dao;

import java.util.List;

import com.cognicx.AppointmentRemainder.Request.UsergroupDetRequest;


public interface UsergroupDao {
	List<Object[]> getUsergroupDet();
	boolean updateUsergroup(UsergroupDetRequest usergroupDetRequest) throws Exception;
	String createUsergroup(UsergroupDetRequest usergroupDetRequest) throws Exception;
}
