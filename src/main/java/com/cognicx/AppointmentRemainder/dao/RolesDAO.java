package com.cognicx.AppointmentRemainder.dao;

import java.math.BigInteger;

import java.util.List;
import java.util.Optional;

import com.cognicx.AppointmentRemainder.model.Roles;

public interface RolesDAO {
	
	Optional<Roles> findByName(String roleName);
	
	public List<Object[]> getUserRoles(int userId);
	
	public String getRoleById(BigInteger roleId) throws Exception;
	
	public List<Object[]> getRoles() throws Exception;
	public List<Object[]> getGroupRoles() throws Exception;

}
