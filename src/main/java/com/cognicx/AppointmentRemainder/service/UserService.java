package com.cognicx.AppointmentRemainder.service;

import java.util.List;

import com.cognicx.AppointmentRemainder.response.UsersResponse;

public interface UserService {
	List<UsersResponse> getApprovedUsersList() throws Exception;
}
