package com.cognicx.AppointmentRemainder.service.impl;

import java.math.BigInteger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cognicx.AppointmentRemainder.model.Roles;
import com.cognicx.AppointmentRemainder.Dto.UserDto;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private BigInteger autogenUsersId;
	private String email;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String password;
	private String status;
	public String autogenUsersDetailsId;
	private String usergroupName;
	

	private Set<Roles> roles = new HashSet<>();
	private Collection<? extends GrantedAuthority> authorities;
	private List<String> rolesList;

	public UserPrinciple(UserDto userDto) {
		this.autogenUsersId = userDto.autogenUsersId;
		this.email = userDto.email;
		this.employeeId = userDto.employeeId;
		this.firstName = userDto.firstName;
		this.lastName = userDto.lastName;
		this.mobileNumber = userDto.mobileNumber;
		this.usergroupName=userDto.getGroupName();
		this.password = userDto.password;
		this.status = userDto.status;
		this.roles = userDto.roles;
		this.authorities = userDto.getAuthorities();
		this.autogenUsersDetailsId = userDto.getAutogenUsersDetailsId();
		this.rolesList = userDto.getRolesList();
		
	}

	public static UserPrinciple build(UserDto userDto) {
		return new UserPrinciple(userDto);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autogenUsersId == null) ? 0 : autogenUsersId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPrinciple other = (UserPrinciple) obj;
		if (autogenUsersId == null) {
			if (other.autogenUsersId != null)
				return false;
		} else if (!autogenUsersId.equals(other.autogenUsersId))
			return false;
		return true;
	}

	public BigInteger getAutogenUsersId() {
		return autogenUsersId;
	}

	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAutogenUsersDetailsId() {
		return autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(String autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public List<String> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<String> rolesList) {
		this.rolesList = rolesList;
	}
	
	public String getUsergroupName() {
		return usergroupName;
	}

	public void setUsergroupName(String usergroupName) {
		this.usergroupName = usergroupName;
	}

}