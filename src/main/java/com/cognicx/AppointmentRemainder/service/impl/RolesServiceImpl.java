package com.cognicx.AppointmentRemainder.service.impl;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import  com.cognicx.AppointmentRemainder.response.GroupNameResponse;
import  com.cognicx.AppointmentRemainder.Dto.UserGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.dao.RolesDAO;
import  com.cognicx.AppointmentRemainder.response.RolesResponse;
import com.cognicx.AppointmentRemainder.dao.RolesDAO;
import com.cognicx.AppointmentRemainder.model.Roles;
import com.cognicx.AppointmentRemainder.model.Users;
import com.cognicx.AppointmentRemainder.service.RolesService;
import com.cognicx.AppointmentRemainder.Dto.UserDto;

@Service
public class RolesServiceImpl implements RolesService {

	@Autowired
	RolesDAO rolesDAO;

	@Override
	public Optional<Roles> findByName(String roleName) {
		return rolesDAO.findByName(roleName);
	}

	@Override
	public List<Object[]> getUserRoles(int userId) {
		return rolesDAO.getUserRoles(userId);
	}

	@Override
	public List<GrantedAuthority> getGrantedAuthorities(Users users, List<Object[]> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		for (Object[] role : roles) {
			authorities.add(new SimpleGrantedAuthority(Arrays.toString(role)));
		}
		return authorities;
	}

	@Override
	public UserDto getRoles() throws Exception {
		UserDto userDto = null;
		List<Object[]> rolesObjList = rolesDAO.getRoles();
		if (rolesObjList != null && !rolesObjList.isEmpty()) {
			List<RolesResponse> rolesResList = new ArrayList<>();
			rolesObjList.stream().forEach(obj -> {
				rolesResList.add(new RolesResponse(new BigInteger(String.valueOf(obj[0])), String.valueOf(obj[1])));
			});
			userDto = new UserDto();
			userDto.setResultObj(rolesResList);
		}
		return userDto;
	}

	@Override
	public UserGroupDto getGroupRoles() throws Exception {
		UserGroupDto userDto = null;
		List<Object[]> groupRolesObjList = rolesDAO.getGroupRoles();
		if (groupRolesObjList != null && !groupRolesObjList.isEmpty()) {
			List<GroupNameResponse> groupRolesResList = new ArrayList<>();
			groupRolesObjList.stream().forEach(obj -> {
				groupRolesResList.add(new GroupNameResponse(new BigInteger(String.valueOf(obj[0])), String.valueOf(obj[1])));
			});
			userDto = new UserGroupDto();
			userDto.setResultObj(groupRolesResList);
		}
		return userDto;
	}

	@Override
	public Map<?, ?> getRolesByKeyValuePair() throws Exception {
		Map<String, BigInteger> resultMap = new HashMap<>();
		List<Object[]> rolesObjList = rolesDAO.getRoles();
		if (rolesObjList != null && !rolesObjList.isEmpty()) {
			rolesObjList.stream().forEach(obj -> {
				resultMap.put(String.valueOf(obj[1]), new BigInteger((String) obj[0]));
			});
		}
		return resultMap;
	}

}
