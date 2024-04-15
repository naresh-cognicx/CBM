package com.cognicx.AppointmentRemainder.response;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cognicx.AppointmentRemainder.model.Reports;
import com.cognicx.AppointmentRemainder.Dto.SurveyTypeDto;
import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Dto.UserLeaveDetailsDto;
import com.cognicx.AppointmentRemainder.message.request.UserRegionRequest;

public class UsersResponse {
	    
	private BigInteger id;
	private BigInteger inventoryCategoryId;
	private String inventoryCategoryName;
	private String email;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String status;
	private String supervisorUsersName;
	private List<UserRegionRequest> userInventoryMaps = new ArrayList<>();
	private List<UserLeaveDetailsDto> userLeaveDetails = new ArrayList<>();
	private List<Reports> reports = new ArrayList<>();
    private String supervisorUsersId;    
    private BigInteger roleId;
    private String rolesName;
    private List<SurveyTypeDto> surveyTypes;
	private List<Map<String, String>> domain;
	private List<Map<String, String>> businessUnit;
	private List<Map<String, String>> roleDetailList;
	private String approvedBy;
	private boolean editFlag;
	private Date createdOn;
	private Date updatedOn;
	private String createdBy;
	private String approverComment;
	private String approvedUserId;
	private String updatedBy;
	private Date approvedOn;

	private String groupName;

	private String groupId;

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

	public UsersResponse() {}

	public UsersResponse(UserDto userDto) {
		this.id = userDto.autogenUsersId;
		this.inventoryCategoryId = userDto.inventoryCategoryId;
		this.inventoryCategoryName = userDto.inventoryCategoryName;
		this.email = userDto.email;
		this.employeeId = userDto.employeeId;
		this.firstName = userDto.firstName;
		this.lastName = userDto.lastName;
		this.mobileNumber = userDto.mobileNumber;
		this.status = userDto.status;
		this.supervisorUsersName = userDto.supervisorUsersName;
		this.userInventoryMaps = userDto.userInventoryMaps;
		this.reports = userDto.reports;
		this.supervisorUsersId = userDto.supervisorUsersId;
		this.inventoryCategoryName = userDto.inventoryCategoryName;
		this.roleId = userDto.autogenRolesId;
		this.rolesName = userDto.rolesName;
		this.userLeaveDetails = userDto.userLeaveDetailsDtoList;
		this.surveyTypes=userDto.surveyTypes;
		this.domain = userDto.getDomain();
		this.businessUnit = userDto.getBusinessUnit();
		this.roleDetailList = userDto.getRoleDetailList();
		this.approvedBy = userDto.getApprovedBy();
		this.editFlag = userDto.isEditFlag();
		this.createdBy=userDto.getCreatedBy();
		this.createdOn = userDto.getRecAddDt();
		this.updatedOn = userDto.getRecUpdateDt();
		this.approverComment = userDto.getApproverComment();
		this.approvedUserId = userDto.getApprovedUserId();
		this.updatedBy=userDto.getUpdatedBy();
		this.approvedOn = userDto.getApprovedOn();
		this.groupName = userDto.getGroupName();
		this.groupId = userDto.getGroupId();
	}
	
	public UsersResponse(BigInteger id, String employeeId) {
		this.id = id;
		this.employeeId = employeeId;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getSupervisorUsersId() {
		return supervisorUsersId;
	}

	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}

	public BigInteger getRoleId() {
		return roleId;
	}

	public void setRoleId(BigInteger roleId) {
		this.roleId = roleId;
	}

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public List<UserRegionRequest> getUserInventoryMaps() {
		return userInventoryMaps;
	}

	public void setUserInventoryMaps(List<UserRegionRequest> userInventoryMaps) {
		this.userInventoryMaps = userInventoryMaps;
	}

	public List<UserLeaveDetailsDto> getUserLeaveDetails() {
		return userLeaveDetails;
	}

	public void setUserLeaveDetails(List<UserLeaveDetailsDto> userLeaveDetails) {
		this.userLeaveDetails = userLeaveDetails;
	}

	public List<SurveyTypeDto> getSurveyTypes() {
		return surveyTypes;
	}

	public void setSurveyTypes(List<SurveyTypeDto> surveyTypes) {
		this.surveyTypes = surveyTypes;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
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

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

}
