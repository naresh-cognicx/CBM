package com.cognicx.AppointmentRemainder.service;

import java.util.List;

import java.util.Map;
import java.util.Optional;

import com.cognicx.AppointmentRemainder.Dto.UserGroupDto;

import org.springframework.security.core.GrantedAuthority;

import com.cognicx.AppointmentRemainder.model.Roles;
import com.cognicx.AppointmentRemainder.model.Users;
import com.cognicx.AppointmentRemainder.Dto.UserDto;

public interface RolesService {
	
	Optional<Roles> findByName(String roleName);
	
	public List<Object[]> getUserRoles(int userId);
	
	public List<GrantedAuthority> getGrantedAuthorities(Users users, List<Object[]> roles);
	
	public UserDto getRoles() throws Exception;
	public UserGroupDto getGroupRoles() throws Exception;

	public Map<?, ?> getRolesByKeyValuePair() throws Exception;

}
