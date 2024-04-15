package com.cognicx.AppointmentRemainder.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cognicx.AppointmentRemainder.Dto.AgentDto;
import com.cognicx.AppointmentRemainder.Dto.SurveyTypeDto;
import com.cognicx.AppointmentRemainder.Dto.TokenDetailsDto;
import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Dto.UserInventoryMapDto;
import com.cognicx.AppointmentRemainder.message.request.RoleRequest;
import com.cognicx.AppointmentRemainder.message.request.SearchAuditTrailRequest;
import com.cognicx.AppointmentRemainder.message.response.ModuleScreenMapResponse;
import com.cognicx.AppointmentRemainder.message.response.RoleResponse;
import com.cognicx.AppointmentRemainder.model.Roles;
import com.cognicx.AppointmentRemainder.model.RolesApproved;
import com.cognicx.AppointmentRemainder.model.UserScreenMap;
import com.cognicx.AppointmentRemainder.model.UserScreenMapApproved;
import com.cognicx.AppointmentRemainder.model.Users;
import com.cognicx.AppointmentRemainder.model.UsersApproved;

/*import com.ison.app.message.request.RoleRequest;
import com.ison.app.message.request.SearchAuditTrailRequest;
import com.ison.app.message.response.ModuleScreenMapResponse;
import com.ison.app.message.response.RoleResponse;
import com.ison.app.model.LoginDetails;
import com.ison.app.model.Roles;
import com.ison.app.model.RolesApproved;
import com.ison.app.model.UserScreenMap;
import com.ison.app.model.UserScreenMapApproved;
import com.ison.app.model.Users;
import com.ison.app.model.UsersApproved;
import com.ison.app.shared.dto.AgentDto;
import com.ison.app.shared.dto.SurveyTypeDto;
import com.ison.app.shared.dto.TokenDetailsDto;
import com.ison.app.shared.dto.UserDto;
import com.ison.app.shared.dto.UserInventoryMapDto;*/

public interface UserDAO {

	Optional<UserDto> findByUsername(String username) throws Exception;

	Boolean existsByUsername(String username) throws Exception;

	Boolean existsByEmail(String email) throws Exception;

	public UserDto save(UserDto userDto) throws Exception;

	public List<UserDto> getUsersList(String roleName) throws Exception;

	public boolean updateExistingUser(UserDto userDto) throws Exception;

	public boolean UpdateUserStatus(UserDto userDto) throws Exception;

	public UserDto getSuperVisorUsersList() throws Exception;

	public AgentDto getAgentList(AgentDto agentDto) throws Exception;

	public AgentDto agentMapping(AgentDto agentDto) throws Exception;

	public AgentDto getAgentDetList(AgentDto agentDto) throws Exception;

	public UserDto getModuleScreenDet(List<String> roleList) throws Exception;

	public AgentDto getRoleAndInventoryByUsersList(AgentDto agentDto, String roleName) throws Exception;

	Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception;

	public boolean resetPassword(UserDto userDto) throws Exception;
	
	public boolean changePassword(UserDto userDto) throws Exception;

	List<UserInventoryMapDto> getUserInventoryMapList(BigInteger userDetailsId) throws Exception;

	public BigInteger findUserDetailIdUsername(String username) throws Exception;

	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;

	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;

	boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;

	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception;

	public List<SurveyTypeDto> getUserSurveyTypeList(final String userId) throws Exception;

	public Map<String, List<Map<String, String>>> getMakerCheckerDetails(Map<String, String> inputRequest)
			throws Exception;

	UserDto getDomainDetails(String userId, String userDetId) throws Exception;

	List<UserDto> getApprovedUsersList(String roleName) throws Exception;

	UserDto saveNewUser(UserDto userDto) throws Exception;

	boolean deleteUnapprovedUsers(BigInteger unapprovedUserId) throws Exception;

	String getPassword(BigInteger userId) throws Exception;

	public String getAppUserPassword(BigInteger userId) throws Exception;

	UserDto getApprovedDomainDetails(String userId, String userDetId) throws Exception;

	Integer updateUserEditFlag(UserDto userDto);

	boolean validateApproveUser(BigInteger userId, String approvedBy);

	List<RoleRequest> getUnapprovedRole(String module) throws Exception;

	List<RoleRequest> getApprovedRole() throws Exception;

	Roles createRoleMapping(Roles role) throws Exception;

	Roles getUnappRoleById(Long roleId) throws Exception;

	Roles updateRoleMapping(Roles role) throws Exception;

	UserScreenMap getUserScreenMap(Long screenMapId) throws Exception;

	UserScreenMap updateRoleScreenMapping(UserScreenMap userScreenMap) throws Exception;

	void deleteRoleScreenMap(List<Long> roleScreenMapIds);

	UserScreenMap createRoleScreenMapping(UserScreenMap userScreenMap) throws Exception;

	RolesApproved getAppRoleById(Long roleId) throws Exception;

	RolesApproved createAppRoleMapping(RolesApproved role) throws Exception;

	void deleteAppRoleScreenMap(Long roleId);

	List<ModuleScreenMapResponse> getModuleScreenMap() throws Exception;

	List<RoleResponse> getRoles() throws Exception;

	UserDto addUser(UserDto userDto) throws Exception;

	boolean disableUser(String userId);

	/**
	 * This method returns the list of users approved given the employee ID.
	 *
	 * @param userId
	 * @return
	 */
	List<UsersApproved> getApprovedUsersByEmployeeId(String userId);

	Integer updateRoleEditFlag(Long roleId, boolean flag);

	Integer updateUnappRoleStatus(Long roleId, String status);

	RolesApproved updateAppRoleMapping(RolesApproved role) throws Exception;

	UserScreenMapApproved createAppRoleScreenMapping(UserScreenMapApproved userScreenMap) throws Exception;

	UserScreenMapApproved updateAppRoleScreenMapping(UserScreenMapApproved userScreenMap) throws Exception;

	UserScreenMapApproved getUserScreenMapApp(Long screenMapId) throws Exception;

	void deleteRoleScreenMapApp(List<Long> roleScreenMapIds);

	void approveExistRoleMapping(Long roleMapId, String comment, String approvedBy) throws Exception;

	void updateRoleMapping(RoleRequest roleRequest) throws Exception;

	UsersApproved getAppUserById(BigInteger userID) throws Exception;

	Users getUnAppUserById(BigInteger userID) throws Exception;

	boolean validateApproveRole(Long roleMapId, String approvedBy);

	UserDto updateUser(UserDto userDto) throws Exception;

	void disableLockOutUsers();

	/**
	 * This method is used to check whether the user exists by the given id.
	 * 
	 * @param name
	 * @return
	 */
	Boolean isUserIdExists(String name);

	/**
	 * This method is used to check whether the role exists by the role name.
	 * 
	 * @param name
	 * @return
	 */
	Boolean isRoleNameExists(String name);

	/**
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	List<UserScreenMapApproved> getAppScreenMappingByRoleId(Long roleId) throws Exception;

	List<UserScreenMap> getScreenMappingByRoleId(Long roleId) throws Exception;

	/**
	 * This method is used to remove the unapproved role screen map which are
	 * rejected.
	 * 
	 * @param roleId
	 */
	void deleteUserScreenMapByRole(Long roleId);

	void deleteUnAppRole(Long autogenRolesId);

	
	  List<Object[]> findLoginDetails(SearchAuditTrailRequest search) throws
	  Exception;
	  
	  List<Object[]> findAuditLogDetails(SearchAuditTrailRequest search) throws
	  Exception;
	  
	  String getEncodedPassword(UserDto userDto) throws Exception;
	 
}
