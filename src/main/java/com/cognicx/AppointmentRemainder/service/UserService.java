package com.cognicx.AppointmentRemainder.service;

import java.math.BigInteger;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
 import com.cognicx.AppointmentRemainder.*;
import com.cognicx.AppointmentRemainder.Dto.AgentDto;
import com.cognicx.AppointmentRemainder.Dto.TokenDetailsDto;
import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Request.GetEmployeeIdsRequest;
import com.cognicx.AppointmentRemainder.message.request.RoleRequest;
import com.cognicx.AppointmentRemainder.message.request.UserRegionRequest;
import com.cognicx.AppointmentRemainder.message.response.ModuleScreenMapResponse;
import com.cognicx.AppointmentRemainder.message.response.RoleResponse;
import com.cognicx.AppointmentRemainder.response.AgentResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponseReport;
import com.cognicx.AppointmentRemainder.response.QANameListResponse;
import com.cognicx.AppointmentRemainder.response.SupervisorsResponse;
import com.cognicx.AppointmentRemainder.response.UsersListResponse;
import com.cognicx.AppointmentRemainder.response.UsersResponse;

/*import com.ison.app.message.request.GetEmployeeIdsRequest;
import com.ison.app.message.request.RoleRequest;
import com.ison.app.message.request.SearchAuditTrailRequest;
import com.ison.app.message.request.UserRegionRequest;
import com.ison.app.message.response.AgentResponse;
import com.ison.app.message.response.GenericResponseReport;
import com.ison.app.message.response.ModuleScreenMapResponse;
import com.ison.app.message.response.QANameListResponse;
import com.ison.app.message.response.RoleResponse;
import com.ison.app.message.response.SupervisorsResponse;
import com.ison.app.message.response.UsersListResponse;
import com.ison.app.message.response.UsersResponse;
import com.ison.app.shared.dto.AgentDto;
import com.ison.app.shared.dto.SurveyTypeDto;
import com.ison.app.shared.dto.TokenDetailsDto;
import com.ison.app.shared.dto.UserDto;*/

public interface UserService {

	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;

	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;

	boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;

	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception;

	public Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception;

	public UserDto save(UserDto userDto) throws Exception;

	public List<UsersResponse> getUsersList() throws Exception;

	public boolean updateExistingUser(UserDto userDto) throws Exception;

	public List<SupervisorsResponse> getSupervisorsList() throws Exception;

	public List<AgentResponse> getAgentList(AgentDto agentDto) throws Exception;

	public AgentDto agentMapping(AgentDto agentDto) throws Exception;

	public List<UsersResponse> getAuditorList() throws Exception;

	public List<UsersListResponse> getEmployeeIdsByRole(GetEmployeeIdsRequest getEmployeeIdsRequest) throws Exception;

	public AgentDto getAgentDetList(AgentDto agentDto) throws Exception;

	public boolean updateUserStatus(UserDto userDto) throws Exception;

	public UserDto getModuleScreenDet(List<String> roleList) throws Exception;

	public List<String> getUserRoles() throws Exception;

	public List<String> getGroupRoles() throws Exception;

	public List<QANameListResponse> getQANameList(AgentDto agentDto) throws Exception;

	public boolean resetPassword(UserDto userDto) throws Exception;

	List<UserRegionRequest> getUserInventoryMapsList() throws Exception;

//	public List<SurveyTypeDto> getUserSurveyTypeList(final String userId) throws Exception;

	public Map<String, List<Map<String, String>>> getMakerCheckerDetails(Map<String, String> inputRequest)
			throws Exception;

	UserDto getDomainDetails(String userId, String userDetId) throws Exception;

	List<UsersResponse> getApprovedUsersList() throws Exception;

	UserDto approveUser(UserDto userDto) throws Exception;

	boolean rejectUser(UserDto userDto) throws Exception;

	public String getAppUserPassword(BigInteger userId) throws Exception;

	UserDto getApprovedDomainDetails(String userId, String userDetId) throws Exception;

	List<RoleRequest> getUnapprovedRole(String module) throws Exception;

	List<RoleRequest> getApprovedRole() throws Exception;

	RoleRequest createRoleMapping(RoleRequest roleRequest) throws Exception;

	RoleRequest updateRoleMapping(RoleRequest roleRequest) throws Exception;

	/**
	 * This method is used to approve role mapping with appropriate comments.
	 * 
	 * @param roleRequest
	 * @return
	 */
	public boolean approveRoleMapping(RoleRequest roleRequest);

	public List<ModuleScreenMapResponse> getModuleScreenMap() throws Exception;

	List<RoleResponse> getRoles() throws Exception;

	UserDto addUser(UserDto userDto) throws Exception;

	public boolean disableUser(String userId);

	/**
	 * This method is used to reject role mapping with appropriate comments.
	 * 
	 * @param roleRequest
	 * @return
	 */
	boolean rejectRoleMapping(RoleRequest roleRequest);

	UserDto updateUser(UserDto userDto) throws Exception;

	/**
	 * This method is used to disable all users whose last logged in time exceeds
	 * the lock out period configured.
	 *
	 */
	public void disableLockOutUsers();

	/**
	 * This method is used to check whether entity of the given name already exists
	 * under given domain and BU. The entity can be user or role.
	 * 
	 * @param name
	 * @param entity
	 * @return
	 */
	public Boolean isEntityNameExists(String name, String entity);


	/*
	 * ResponseEntity<GenericResponseReport>
	 * findLoginDetails(SearchAuditTrailRequest search);
	 * 
	 * ResponseEntity<GenericResponseReport>
	 * findAuditLogDetails(SearchAuditTrailRequest search);
	 */
	public boolean changePassword(UserDto userDto) throws Exception;
	
	  String getEncodedPassword(UserDto userDto) throws Exception;
}
