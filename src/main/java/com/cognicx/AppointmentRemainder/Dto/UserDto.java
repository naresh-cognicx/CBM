package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.cognicx.AppointmentRemainder.message.request.UserRegionRequest;
import com.cognicx.AppointmentRemainder.model.Reports;
import com.cognicx.AppointmentRemainder.model.Roles;

@JsonInclude(Include.NON_EMPTY)
public class UserDto {

	public BigInteger autogenUsersId;
	public String autogenUsersDetailsId;
	public BigInteger inventoryCategoryId;
	public String inventoryCategoryName;
	public String email;
	public String employeeId;
	public String firstName;
	public String lastName;
	public int loginAttempt;
	public String mobileNumber;
	public String password;
	public Date recAddDt;
	public Date recUpdateDt;
	public String status;
	public String supervisorUsersName;
	public Set<Roles> roles = new HashSet<>();
	public List<UserInventoryMapDto> userInventoryMapDtoList = new ArrayList<>();
	public List<UserLeaveDetailsDto> userLeaveDetailsDtoList = new ArrayList<>();
	public List<UserRegionRequest> userInventoryMaps = new ArrayList<>();
	public List<Reports> reports = new ArrayList<>();
	public Collection<? extends GrantedAuthority> authorities;
	public String supervisorUsersId;
	public BigInteger autogenRolesId;
	public String rolesName;
	public String createdBy;
	public String updatedBy;
	public List<Object[]> resultObjList;
	public Object resultObj;
	public List<SurveyTypeDto> surveyTypes;
	public String userName;
	private List<Map<String, String>> domain;
	private List<Map<String, String>> businessUnit;
	private List<Map<String, String>> roleDetailList;
	private List<String> rolesList;
	private String approvedBy;
	private Date approvedOn;
	private boolean editFlag;
	private String approverComment;
	private String approvedUserId;
	private String groupId;
	private String groupName;
	private String userID;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	

	public UserDto() {
	}

	public UserDto(UserDto userDto) {
		this.autogenUsersId = userDto.autogenUsersId;
		this.autogenUsersDetailsId = userDto.autogenUsersDetailsId;
		this.inventoryCategoryId = userDto.inventoryCategoryId;
		this.inventoryCategoryName = userDto.inventoryCategoryName;
		this.email = userDto.email;
		this.employeeId = userDto.employeeId;
		this.firstName = userDto.firstName;
		this.lastName = userDto.lastName;
		this.loginAttempt = userDto.loginAttempt;
		this.mobileNumber = userDto.mobileNumber;
		this.password = userDto.password;
		this.recAddDt = userDto.recAddDt;
		this.recUpdateDt = userDto.recUpdateDt;
		this.status = userDto.status;
		this.supervisorUsersName = userDto.supervisorUsersName;
		this.roles = userDto.roles;
		this.userInventoryMapDtoList = userDto.userInventoryMapDtoList;
		this.userInventoryMaps = userDto.userInventoryMaps;
		this.userLeaveDetailsDtoList = userDto.userLeaveDetailsDtoList;
		this.authorities = userDto.getAuthorities();
		this.supervisorUsersId = userDto.getSupervisorUsersId();
		this.autogenRolesId = userDto.getAutogenRolesId();
		this.rolesName = userDto.getRolesName();
		this.surveyTypes = userDto.surveyTypes;
		this.userName = userDto.getUserName();
		this.domain = userDto.getDomain();
		this.businessUnit = userDto.getBusinessUnit();
		this.rolesList = userDto.getRolesList();
		this.approvedBy = userDto.getApprovedBy();
		this.editFlag = userDto.isEditFlag();
		this.approverComment = userDto.getApproverComment();
		this.approvedUserId = userDto.getApprovedUserId();
		this.groupName = userDto.groupName;
		this.groupId = userDto.groupId;
		this.userID=userDto.userID;
		this.oldPassword=userDto.oldPassword;
		this.newPassword=userDto.newPassword;
		this.confirmPassword=userDto.confirmPassword;
				
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public BigInteger getAutogenUsersId() {
		return autogenUsersId;
	}

	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
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

	public int getLoginAttempt() {
		return loginAttempt;
	}

	public void setLoginAttempt(int loginAttempt) {
		this.loginAttempt = loginAttempt;
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

	public Date getRecAddDt() {
		return recAddDt;
	}

	public void setRecAddDt(Date recAddDt) {
		this.recAddDt = recAddDt;
	}

	public Date getRecUpdateDt() {
		return recUpdateDt;
	}

	public void setRecUpdateDt(Date recUpdateDt) {
		this.recUpdateDt = recUpdateDt;
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

	public String getSupervisorUsersId() {
		return supervisorUsersId;
	}

	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}

	public String getAutogenUsersDetailsId() {
		return autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(String autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public BigInteger getAutogenRolesId() {
		return autogenRolesId;
	}

	public void setAutogenRolesId(BigInteger autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
	}

	public String getSupervisorUsersName() {
		return supervisorUsersName;
	}

	public void setSupervisorUsersName(String supervisorUsersName) {
		this.supervisorUsersName = supervisorUsersName;
	}

	public List<Reports> getReports() {
		return reports;
	}

	public void setReports(List<Reports> reports) {
		this.reports = reports;
	}

	public List<Object[]> getResultObjList() {
		return resultObjList;
	}

	public void setResultObjList(List<Object[]> resultObjList) {
		this.resultObjList = resultObjList;
	}

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}

	public List<UserInventoryMapDto> getUserInventoryMapDtoList() {
		return userInventoryMapDtoList;
	}

	public void setUserInventoryMapDtoList(List<UserInventoryMapDto> userInventoryMapDtoList) {
		this.userInventoryMapDtoList = userInventoryMapDtoList;
	}

	public List<UserRegionRequest> getUserInventoryMaps() {
		return userInventoryMaps;
	}

	public void setUserInventoryMaps(List<UserRegionRequest> userInventoryMaps) {
		this.userInventoryMaps = userInventoryMaps;
	}

	public List<UserLeaveDetailsDto> getUserLeaveDetailsDtoList() {
		return userLeaveDetailsDtoList;
	}

	public void setUserLeaveDetailsDtoList(List<UserLeaveDetailsDto> userLeaveDetailsDtoList) {
		this.userLeaveDetailsDtoList = userLeaveDetailsDtoList;
	}

	public List<SurveyTypeDto> getSurveyTypes() {
		return surveyTypes;
	}

	public void setSurveyTypes(List<SurveyTypeDto> surveyTypes) {
		this.surveyTypes = surveyTypes;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Map<String, String>> getDomain() {
		return domain;
	}

	public void setDomain(List<Map<String, String>> domain) {
		this.domain = domain;
	}

	public List<Map<String, String>> getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(List<Map<String, String>> businessUnit) {
		this.businessUnit = businessUnit;
	}

	public List<String> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<String> rolesList) {
		this.rolesList = rolesList;
	}

	public List<Map<String, String>> getRoleDetailList() {
		return roleDetailList;
	}

	public void setRoleDetailList(List<Map<String, String>> roleDetailList) {
		this.roleDetailList = roleDetailList;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	public String getApproverComment() {
		return approverComment;
	}

	public void setApproverComment(String approverComment) {
		this.approverComment = approverComment;
	}

	public String getApprovedUserId() {
		return approvedUserId;
	}

	public void setApprovedUserId(String approvedUserId) {
		this.approvedUserId = approvedUserId;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
