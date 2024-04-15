package com.cognicx.AppointmentRemainder.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.cognicx.AppointmentRemainder.model.*;
import com.cognicx.AppointmentRemainder.response.AgentResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponseReport;
import com.cognicx.AppointmentRemainder.response.GroupNameResponse;
import com.cognicx.AppointmentRemainder.response.ModuleListResponse;
import com.cognicx.AppointmentRemainder.response.QANameListResponse;
import com.cognicx.AppointmentRemainder.response.RolesResponse;
import com.cognicx.AppointmentRemainder.response.ScreenListResponse;
import com.cognicx.AppointmentRemainder.response.SupervisorsResponse;
import com.cognicx.AppointmentRemainder.response.UsersListResponse;
import com.cognicx.AppointmentRemainder.response.UsersResponse;
import com.cognicx.AppointmentRemainder.Dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cognicx.AppointmentRemainder.configuration.AuditLog;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.dao.UserDAO;
import com.cognicx.AppointmentRemainder.message.request.*;
import com.cognicx.AppointmentRemainder.message.request.RoleRequest;
import com.cognicx.AppointmentRemainder.Request.GetEmployeeIdsRequest;
import com.cognicx.AppointmentRemainder.message.request.SearchAuditTrailRequest;
import com.cognicx.AppointmentRemainder.message.request.UserCenterRequest;
import com.cognicx.AppointmentRemainder.message.request.UserClientRequest;
import com.cognicx.AppointmentRemainder.message.request.UserProcessRequest;
import com.cognicx.AppointmentRemainder.message.request.UserRegionRequest;
import com.cognicx.AppointmentRemainder.message.response.AgentDetResponse;
import com.cognicx.AppointmentRemainder.message.response.ModuleScreenMapResponse;
import com.cognicx.AppointmentRemainder.message.response.RoleResponse;
import com.cognicx.AppointmentRemainder.service.RolesService;
import com.cognicx.AppointmentRemainder.service.UserService;
import com.cognicx.AppointmentRemainder.util.CommonUtil;
import com.cognicx.AppointmentRemainder.util.UserInfo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userDAO;

	@Autowired
	RolesService rolesService;

	@Autowired
	UserInfo userInfo;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception {
		return userDAO.saveOrUpdateLoginDetails(insertFlag, loginInfo);
	}

	@Override
	@AuditLog
	public UserDto save(UserDto userDto) throws Exception {
		userDto.setEditFlag(true);
		userDAO.updateUserEditFlag(userDto);
		return userDAO.save(userDto);
	}

	// Utility function
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
		final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

		return t -> {
			final List<?> keys = Arrays.stream(keyExtractors).map(ke -> ke.apply(t)).collect(Collectors.toList());

			return seen.putIfAbsent(keys, Boolean.TRUE) == null;
		};
	}

	@Override
	public List<UsersResponse> getApprovedUsersList() throws Exception {
		List<UserDto> userList = userDAO.getApprovedUsersList("");
		List<UsersResponse> userResList = new ArrayList<>();
		for (UserDto userDto : userList) {
			List<UserInventoryMapDto> userInventoryMapList = userDto.getUserInventoryMapDtoList();

			if (userInventoryMapList != null && !userInventoryMapList.isEmpty()) {
				List<UserInventoryMapDto> regionList = userInventoryMapList.stream()
						.filter(distinctByKey(region -> region.getInventoryRegionId())).collect(Collectors.toList());

				List<UserRegionRequest> inventoryMapResponseList = new ArrayList<>();
				regionList.stream().forEach(regionObject -> {
					UserRegionRequest inventoryMapResponse = new UserRegionRequest();

					List<UserInventoryMapDto> centerList = userInventoryMapList.stream()
							.filter(center -> center.getInventoryRegionId().equals(regionObject.getInventoryRegionId()))
							.filter(distinctByKey(center -> center.getInventoryCenterId()))
							.collect(Collectors.toList());
					List<UserCenterRequest> userCenters = new ArrayList<>();
					centerList.stream().forEach(centerObject -> {
						UserCenterRequest centerRequest = new UserCenterRequest();
						List<UserInventoryMapDto> clientList = userInventoryMapList.stream().filter(
								client -> client.getInventoryRegionId().equals(regionObject.getInventoryRegionId())
										&& client.getInventoryCenterId().equals(centerObject.getInventoryCenterId()))
								.filter(distinctByKey(client -> client.getInventoryClientId()))
								.collect(Collectors.toList());
						List<UserClientRequest> userClients = new ArrayList<>();
						clientList.forEach(clientObject -> {

							UserClientRequest clientRequest = new UserClientRequest();
							List<UserInventoryMapDto> processList = userInventoryMapList.stream()
									.filter(process -> process.getInventoryRegionId()
											.equals(regionObject.getInventoryRegionId())
											&& process.getInventoryCenterId()
													.equals(centerObject.getInventoryCenterId())
											&& process.getInventoryClientId()
													.equals(clientObject.getInventoryClientId()))
									.filter(distinctByKey(process -> process.getInventoryProcessId()))
									.collect(Collectors.toList());
							List<UserProcessRequest> userProcesses = new ArrayList<>();
							processList.forEach(processObject -> {
								UserProcessRequest processRequest = new UserProcessRequest();
								processRequest.setInventoryProcessId(processObject.getInventoryProcessId());
								processRequest.setInventoryProcessName(processObject.getInventoryProcessName());
								processRequest.setInventoryCategoryId(processObject.getInventoryCategoryId());
								processRequest.setInventoryCategoryName(processObject.getInventoryCategoryName());
								userProcesses.add(processRequest);
							});
							clientRequest.setInventoryClientId(clientObject.getInventoryClientId());
							clientRequest.setInventoryClientName(clientObject.getInventoryClientName());
							clientRequest.setUserProcesses(userProcesses);
							userClients.add(clientRequest);
						});
						centerRequest.setUserClients(userClients);
						centerRequest.setInventoryCenterId(centerObject.getInventoryCenterId());
						centerRequest.setInventoryCenterName(centerObject.getInventoryCenterName());
						userCenters.add(centerRequest);
					});
					inventoryMapResponse.setUserCenters(userCenters);
					inventoryMapResponse.setInventoryRegionId(regionObject.getInventoryRegionId());
					inventoryMapResponse.setInventoryRegionName(regionObject.getInventoryRegionName());
					inventoryMapResponseList.add(inventoryMapResponse);
				});
				userDto.setUserInventoryMaps(inventoryMapResponseList);
			}
			userDto.setSurveyTypes(userDto.getSurveyTypes());
			userResList.add(new UsersResponse(userDto));
		}
		return userResList;
	}
	
	
	@Override
	public List<UsersResponse> getUsersList() throws Exception {
		List<UserDto> userList = userDAO.getUsersList("");
		List<UsersResponse> userResList = new ArrayList<>();
		for (UserDto userDto : userList) {
			List<UserInventoryMapDto> userInventoryMapList = userDto.getUserInventoryMapDtoList();

			if (userInventoryMapList != null && !userInventoryMapList.isEmpty()) {
				List<UserInventoryMapDto> regionList = userInventoryMapList.stream()
						.filter(distinctByKey(region -> region.getInventoryRegionId())).collect(Collectors.toList());

				List<UserRegionRequest> inventoryMapResponseList = new ArrayList<>();
				regionList.stream().forEach(regionObject -> {
					UserRegionRequest inventoryMapResponse = new UserRegionRequest();

					List<UserInventoryMapDto> centerList = userInventoryMapList.stream()
							.filter(center -> center.getInventoryRegionId().equals(regionObject.getInventoryRegionId()))
							.filter(distinctByKey(center -> center.getInventoryCenterId()))
							.collect(Collectors.toList());
					List<UserCenterRequest> userCenters = new ArrayList<>();
					centerList.stream().forEach(centerObject -> {

						UserCenterRequest centerRequest = new UserCenterRequest();
						List<UserInventoryMapDto> clientList = userInventoryMapList.stream().filter(
								client -> client.getInventoryRegionId().equals(regionObject.getInventoryRegionId())
										&& client.getInventoryCenterId().equals(centerObject.getInventoryCenterId()))
								.filter(distinctByKey(client -> client.getInventoryClientId()))
								.collect(Collectors.toList());
						List<UserClientRequest> userClients = new ArrayList<>();
						clientList.forEach(clientObject -> {

							UserClientRequest clientRequest = new UserClientRequest();
							List<UserInventoryMapDto> processList = userInventoryMapList.stream()
									.filter(process -> process.getInventoryRegionId()
											.equals(regionObject.getInventoryRegionId())
											&& process.getInventoryCenterId()
													.equals(centerObject.getInventoryCenterId())
											&& process.getInventoryClientId()
													.equals(clientObject.getInventoryClientId()))
									.filter(distinctByKey(process -> process.getInventoryProcessId()))
									.collect(Collectors.toList());

							List<UserProcessRequest> userProcesses = new ArrayList<>();
							processList.forEach(processObject -> {
								UserProcessRequest processRequest = new UserProcessRequest();
								processRequest.setInventoryProcessId(processObject.getInventoryProcessId());
								processRequest.setInventoryProcessName(processObject.getInventoryProcessName());
								processRequest.setInventoryCategoryId(processObject.getInventoryCategoryId());
								processRequest.setInventoryCategoryName(processObject.getInventoryCategoryName());
								userProcesses.add(processRequest);
							});

							clientRequest.setInventoryClientId(clientObject.getInventoryClientId());
							clientRequest.setInventoryClientName(clientObject.getInventoryClientName());
							clientRequest.setUserProcesses(userProcesses);
							userClients.add(clientRequest);
						});
						centerRequest.setUserClients(userClients);
						centerRequest.setInventoryCenterId(centerObject.getInventoryCenterId());
						centerRequest.setInventoryCenterName(centerObject.getInventoryCenterName());
						userCenters.add(centerRequest);
					});
					inventoryMapResponse.setUserCenters(userCenters);
					inventoryMapResponse.setInventoryRegionId(regionObject.getInventoryRegionId());
					inventoryMapResponse.setInventoryRegionName(regionObject.getInventoryRegionName());
					inventoryMapResponseList.add(inventoryMapResponse);
				});

				userDto.setUserInventoryMaps(inventoryMapResponseList);
			}
			userDto.setSurveyTypes(userDto.getSurveyTypes());
			userResList.add(new UsersResponse(userDto));
		}

		return userResList;
	}

	private Predicate<? super UserInventoryMapDto> distinctByKey(BigInteger inventoryRegionId, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateExistingUser(UserDto userDto) throws Exception {
		return userDAO.updateExistingUser(userDto);
	}

	@Override
	public List<SupervisorsResponse> getSupervisorsList() throws Exception {
		List<SupervisorsResponse> supervisorsResponseList = new ArrayList<>();
		UserDto userDto = userDAO.getSuperVisorUsersList();
		if (userDto.getResultObjList() != null && !userDto.getResultObjList().isEmpty()) {
			userDto.getResultObjList().stream().forEach(obj -> {
				supervisorsResponseList.add(new SupervisorsResponse(String.valueOf(obj[0]), String.valueOf(obj[1])));
			});
		}
		return supervisorsResponseList;
	}

	@Override
	public List<AgentResponse> getAgentList(AgentDto agentDto) throws Exception {
		agentDto = userDAO.getAgentList(agentDto);
		List<AgentResponse> agentList = new ArrayList<>();
		if (agentDto.getResultObjList() != null && !agentDto.getResultObjList().isEmpty()) {
			agentDto.getResultObjList().stream().forEach(agent -> {
				if (agent[2].getClass().getTypeName() != null
						&& "java.math.BigInteger".equalsIgnoreCase(agent[2].getClass().getTypeName())) {
					// BigInteger agentMapId = agent[2] != null ? (BigInteger)agent[2] : null;
					agentList.add(
							new AgentResponse(CommonUtil.nullRemove(agent[0]), CommonUtil.nullRemove(agent[1]), true));
				} else {
					agentList.add(
							new AgentResponse(CommonUtil.nullRemove(agent[0]), CommonUtil.nullRemove(agent[1]), false));
				}
			});
		}
		return agentList;
	}

	@Override
	public AgentDto agentMapping(AgentDto agentDto) throws Exception {
		return userDAO.agentMapping(agentDto);
	}

	@Override
	public List<UsersResponse> getAuditorList() throws Exception {
		List<UserDto> userList = userDAO.getUsersList("Supervisor - Agent/Team Lead");
		List<UsersResponse> userResList = new ArrayList<>();
		for (UserDto userDto : userList) {
			userDto.setUserInventoryMaps(marshallUserInventoryMaps(userDto.getUserInventoryMapDtoList()));
			userResList.add(new UsersResponse(userDto));
		}
		return userResList;
	}

	@Override
	public List<UsersListResponse> getEmployeeIdsByRole(GetEmployeeIdsRequest getEmployeeIdsRequest) throws Exception {
		List<UserDto> userList = userDAO.getUsersList(getEmployeeIdsRequest.getRolesName());
		List<UsersListResponse> userResList = new ArrayList<>();
		for (UserDto userDto : userList) {
			userResList.add(new UsersListResponse(userDto.getAutogenRolesId(), userDto.getEmployeeId()));
		}
		return userResList;
	}

	@Override
	public AgentDto getAgentDetList(AgentDto agentDto) throws Exception {
		agentDto = userDAO.getAgentDetList(agentDto);
		List<AgentDetResponse> agentDetResponseList = new ArrayList<>();
		agentDto.getResultObjList().stream().forEach(obj -> {
			List<String> categoryList = new ArrayList<>();
			if (obj[1] != null) {
				List<Object[]> cateories = (List<Object[]>) obj[1];
				for (Object innerObj : cateories) {
					categoryList.add((String) innerObj);
				}
			}
			if (obj[0] != null) {
				Object[] agentDet = (Object[]) obj[0];
				agentDetResponseList.add(
						new AgentDetResponse(CommonUtil.nullRemove(agentDet[0]), CommonUtil.nullRemove(agentDet[1]),
								CommonUtil.nullRemove(agentDet[2]), CommonUtil.nullRemove(agentDet[3]), categoryList));
			}
		});
		agentDto.setResultObj(null);
		agentDto.setResultObj(agentDetResponseList);
		return agentDto;

	}

	@Override
	public boolean updateUserStatus(UserDto userDto) throws Exception {
		return userDAO.UpdateUserStatus(userDto);
	}

	@Override
	public UserDto getModuleScreenDet(List<String> roleList) throws Exception {
		List<ModuleListResponse> moduleListResponse = null;
		UserDto userDto = userDAO.getModuleScreenDet(roleList);
		if (userDto != null && userDto.getResultObjList() != null && !userDto.getResultObjList().isEmpty()) {
			List<List<String>> uniqueModuleList = new ArrayList<>();
			String tempModuleUId = "";

			for (Object[] obj : userDto.getResultObjList()) {
				if (!tempModuleUId.equalsIgnoreCase(String.valueOf(obj[0]))) {
					tempModuleUId = String.valueOf(obj[0]);
					List<String> moduleList = new ArrayList<>();
					moduleList.add(tempModuleUId);
					moduleList.add(String.valueOf(obj[1]));
					uniqueModuleList.add(moduleList);
				}
			}
			moduleListResponse = new ArrayList<>();
			for (List<String> moduleObj : uniqueModuleList) {

				List<ScreenListResponse> screenListResponse = null;
				String tempScreenUId = "";
				for (Object[] obj : userDto.getResultObjList()) {
					if (moduleObj.get(0).equalsIgnoreCase(String.valueOf(obj[0]))) {
						if (tempScreenUId.equalsIgnoreCase(obj[0].toString())) {
							if (screenListResponse != null) {
								screenListResponse.add(new ScreenListResponse(String.valueOf(obj[2]),
										String.valueOf(obj[3]), String.valueOf(obj[4])));
							}
						} else {
							screenListResponse = new ArrayList<>();
							tempScreenUId = moduleObj.get(0);
							screenListResponse.add(new ScreenListResponse(String.valueOf(obj[2]),
									String.valueOf(obj[3]), String.valueOf(obj[4])));
						}
					}
				}
				moduleListResponse.add(new ModuleListResponse(moduleObj.get(0), moduleObj.get(1), screenListResponse));
			}

			userDto.setResultObj(moduleListResponse);
		}

		return userDto;
	}

	@Override
	public List<String> getUserRoles() throws Exception {
		List<String> roleList = new ArrayList<>();
		UserDto userDto = rolesService.getRoles();
		if (userDto.getResultObj() != null) {
			List<RolesResponse> roleResList = (List<RolesResponse>) userDto.getResultObj();
			roleResList.stream().forEach(obj -> {
				roleList.add(obj.getRolesName());
			});
		}

		return roleList;
	}


	public List<String> getGroupRoles() throws Exception {
		List<String> groupRoleList = new ArrayList<>();
		UserGroupDto userDto = rolesService.getGroupRoles();
		if (userDto.getResultObj() != null) {
			List<GroupNameResponse> roleResList = (List<GroupNameResponse>) userDto.getResultObj();
			roleResList.stream().forEach(obj -> {
				groupRoleList.add(obj.getGroupName());
			});
		}
		return groupRoleList;
	}

	@Override
	public List<QANameListResponse> getQANameList(AgentDto agentDto) throws Exception {
		agentDto = userDAO.getRoleAndInventoryByUsersList(agentDto, "QA");
		List<QANameListResponse> qaNameList = new ArrayList<>();
		if (agentDto.getResultObjList() != null && !agentDto.getResultObjList().isEmpty()) {
			agentDto.getResultObjList().stream().forEach(qaObj -> {
				qaNameList
						.add(new QANameListResponse(CommonUtil.nullRemove(qaObj[1]), CommonUtil.nullRemove(qaObj[0])));

			});
		}
		return qaNameList;
	}

	@Override
	public boolean resetPassword(UserDto userDto) throws Exception {
		return userDAO.resetPassword(userDto);
	}

	@Override
	public List<UserRegionRequest> getUserInventoryMapsList() throws Exception {
		BigInteger userdetailId = null;
		if (userInfo.getAutogenUserDetailsId() == null) {
			userdetailId = userDAO.findUserDetailIdUsername(userInfo.getEmployeeId());
			userInfo.setAutogenUserDetailsId(userdetailId);
		}
		List<UserRegionRequest> inventoryMapResponseList = new ArrayList<>();
		if (userdetailId != null) {
			List<UserInventoryMapDto> userInventoryMapList = userDAO.getUserInventoryMapList(userdetailId);
			inventoryMapResponseList = marshallUserInventoryMaps(userInventoryMapList);
		}
		return inventoryMapResponseList;
	}

	public List<UserRegionRequest> marshallUserInventoryMaps(List<UserInventoryMapDto> userInventoryMapList) {
		List<UserRegionRequest> inventoryMapResponseList = new ArrayList<>();
		if (userInventoryMapList != null && !userInventoryMapList.isEmpty()) {
			List<UserInventoryMapDto> regionList = userInventoryMapList.stream()
					.filter(distinctByKey(region -> region.getInventoryRegionId())).collect(Collectors.toList());

			regionList.stream().forEach(regionObject -> {
				UserRegionRequest inventoryMapResponse = new UserRegionRequest();

				List<UserInventoryMapDto> centerList = userInventoryMapList.stream()
						.filter(center -> center.getInventoryRegionId().equals(regionObject.getInventoryRegionId()))
						.filter(distinctByKey(center -> center.getInventoryCenterId())).collect(Collectors.toList());
				List<UserCenterRequest> userCenters = new ArrayList<>();
				centerList.stream().forEach(centerObject -> {

					UserCenterRequest centerRequest = new UserCenterRequest();
					List<UserInventoryMapDto> clientList = userInventoryMapList.stream()
							.filter(client -> client.getInventoryRegionId().equals(regionObject.getInventoryRegionId())
									&& client.getInventoryCenterId().equals(centerObject.getInventoryCenterId()))
							.filter(distinctByKey(client -> client.getInventoryClientId()))
							.collect(Collectors.toList());
					List<UserClientRequest> userClients = new ArrayList<>();
					clientList.forEach(clientObject -> {

						UserClientRequest clientRequest = new UserClientRequest();
						List<UserInventoryMapDto> processList = userInventoryMapList.stream().filter(
								process -> process.getInventoryRegionId().equals(regionObject.getInventoryRegionId())
										&& process.getInventoryCenterId().equals(centerObject.getInventoryCenterId())
										&& process.getInventoryClientId().equals(clientObject.getInventoryClientId()))
								.filter(distinctByKey(process -> process.getInventoryProcessId()))
								.collect(Collectors.toList());

						List<UserProcessRequest> userProcesses = new ArrayList<>();
						processList.forEach(processObject -> {
							UserProcessRequest processRequest = new UserProcessRequest();
							processRequest.setInventoryProcessId(processObject.getInventoryProcessId());
							processRequest.setInventoryProcessName(processObject.getInventoryProcessName());
							userProcesses.add(processRequest);
						});

						clientRequest.setInventoryClientId(clientObject.getInventoryClientId());
						clientRequest.setInventoryClientName(clientObject.getInventoryClientName());
						clientRequest.setUserProcesses(userProcesses);
						userClients.add(clientRequest);
					});
					centerRequest.setUserClients(userClients);
					centerRequest.setInventoryCenterId(centerObject.getInventoryCenterId());
					centerRequest.setInventoryCenterName(centerObject.getInventoryCenterName());
					userCenters.add(centerRequest);
				});
				inventoryMapResponse.setUserCenters(userCenters);
				inventoryMapResponse.setInventoryRegionId(regionObject.getInventoryRegionId());
				inventoryMapResponse.setInventoryRegionName(regionObject.getInventoryRegionName());
				inventoryMapResponseList.add(inventoryMapResponse);
			});
		}

		return inventoryMapResponseList;
	}

	@Override
	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		return userDAO.saveTokenDetails(tokenDetailsDto);
	}

	@Override
	public boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		return userDAO.checkExistingTokenDetails(tokenDetailsDto);
	}

	@Override
	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception {
		return userDAO.updateTokenStatus(tokenDetailsDto);
	}

	/*
	 * @Override public List<SurveyTypeDto> getUserSurveyTypeList(final String
	 * userId) throws Exception { return userDAO.getUserSurveyTypeList(userId); }
	 */

	@Override
	public Map<String, List<Map<String, String>>> getMakerCheckerDetails(Map<String, String> inputRequest)
			throws Exception {
		return userDAO.getMakerCheckerDetails(inputRequest);
	}

	@Override
	public UserDto getDomainDetails(String userId, String userDetId) throws Exception {
		return userDAO.getDomainDetails(userId, userDetId);
	}

	@Override
	public UserDto getApprovedDomainDetails(String userId, String userDetId) throws Exception {
		return userDAO.getApprovedDomainDetails(userId, userDetId);
	}

	@Override
	@AuditLog
	public UserDto approveUser(UserDto userDto) throws Exception {
		/*
		 * if ((!StringUtils.isEmpty(userInfo.getRolesName()) &&
		 * userInfo.getRolesName().contains(AppicationConstants.SUPER_ADMIN_ROLE)) ||
		 * userDAO.validateApproveUser(userDto.getAutogenUsersId(),
		 * userDto.getApprovedBy())) {
		 */
			if ("New".equalsIgnoreCase(userDto.getStatus())) {
				BigInteger unapprovedUserId = userDto.getAutogenUsersId();
				String password = userDAO.getPassword(unapprovedUserId);
				userDto.setPassword(password);
				userDto.setEditFlag(false);
				if (null == userDto.getCreatedBy()) {
					userDto.setCreatedBy("System");
					userDto.setUpdatedBy("System");
				}
				UserDto userDtoApproved = userDAO.saveNewUser(userDto);
				userDAO.deleteUnapprovedUsers(unapprovedUserId);
				return userDtoApproved;
			} else {
				BigInteger unapprovedUserId = userDto.getAutogenUsersId();
				String password = userDAO.getPassword(unapprovedUserId);
				userDto.setPassword(password);
				userDto.setEditFlag(false);
				// userDAO.updateUserEditFlag(userDto);
				userDto.setAutogenUsersId(new BigInteger(userDto.getApprovedUserId()));
				boolean isUpdated = userDAO.updateExistingUser(userDto);
				userDAO.deleteUnapprovedUsers(unapprovedUserId);
				return userDto;
			}
		/*} else {
			throw new Exception("User does not have priviledge to approve");
		}*/
	}

	@Override
	@AuditLog
	public boolean rejectUser(UserDto userDto) throws Exception {
		if ((!StringUtils.isEmpty(userInfo.getRolesName())
				&& !userInfo.getRolesName().contains(ApplicationConstant.SUPER_ADMIN_ROLE))
				&& !userDAO.validateApproveUser(userDto.getAutogenUsersId(), userDto.getApprovedBy())) {
			return false;
		}
		if (!("New".equalsIgnoreCase(userDto.getStatus()))) {
			userDto.setEditFlag(false);
			userDAO.updateUserEditFlag(userDto);
		}
		return userDAO.deleteUnapprovedUsers(userDto.getAutogenUsersId());
	}

	@Override
	public String getAppUserPassword(BigInteger userId) throws Exception {
		return userDAO.getAppUserPassword(userId);
	}

	@Override
	public List<ModuleScreenMapResponse> getModuleScreenMap() throws Exception {
		return userDAO.getModuleScreenMap();
	}

	@Override
	public List<RoleRequest> getUnapprovedRole(String module) throws Exception {
		List<RoleRequest> roleMapping = userDAO.getUnapprovedRole(module);
		return roleMapping;
	}

	@Override
	public List<RoleResponse> getRoles() throws Exception {
		return userDAO.getRoles();
	}

	@Override
	public List<RoleRequest> getApprovedRole() throws Exception {
		List<RoleRequest> roleMapping = userDAO.getApprovedRole();
		return roleMapping;
	}

	@AuditLog
	@Override
	public RoleRequest createRoleMapping(RoleRequest roleRequest) throws Exception {
		Roles role = new Roles();
		List<UserScreenMap> userScreenMap = null;
		BeanUtils.copyProperties(roleRequest, role);
		role.setUserScreenMap(null);
		role.setRecAddDt(new Timestamp(System.currentTimeMillis()));
		role.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
		userScreenMap = roleRequest.getUserScreenMap();
		role = userDAO.createRoleMapping(role);
		if (role.getAutogenRolesId() != null) {
			roleRequest.setAutogenRolesId(role.getAutogenRolesId());
			if (userScreenMap != null && !userScreenMap.isEmpty()) {
				for (UserScreenMap obj : userScreenMap) {
					obj.setRole(role);
					obj.setRecAddDt(new Timestamp(System.currentTimeMillis()));
					obj.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
					obj.setCreatedBy(roleRequest.getCreatedBy());
					obj.setUpdatedBy(roleRequest.getCreatedBy());
					userDAO.createRoleScreenMapping(obj);
				}
			}
		}
		BeanUtils.copyProperties(role, roleRequest);
		roleRequest.setUserScreenMap(userScreenMap);
		return roleRequest;
	}

	@AuditLog
	@Override
	public RoleRequest updateRoleMapping(RoleRequest roleRequest) throws Exception {
		try {
			userDAO.updateRoleMapping(roleRequest);
		} catch (Exception e) {
			logger.error("UserServiceImpl::updateRoleMapping::" + e);
			throw e;
		}
		return roleRequest;
	}

	@AuditLog
	@Override
	public boolean approveRoleMapping(RoleRequest roleRequest) {
		try {
			if ((!StringUtils.isEmpty(userInfo.getRolesName())
					&& !userInfo.getRolesName().contains(ApplicationConstant.SUPER_ADMIN_ROLE))
					&& !userDAO.validateApproveRole(roleRequest.getAutogenRolesId(), roleRequest.getApprovedBy())) {
				return false;
			}
			if ("New_Approve".equalsIgnoreCase(roleRequest.getRoleCreateStatus())) {
				insertAppRoleDetails(roleRequest.getAutogenRolesId(), roleRequest.getComment(),
						roleRequest.getApprovedBy());
			} else if ("Exist_Approve".equalsIgnoreCase(roleRequest.getRoleCreateStatus())) {
				userDAO.approveExistRoleMapping(roleRequest.getAutogenRolesId(), roleRequest.getComment(),
						roleRequest.getApprovedBy());
			}
		} catch (Exception e) {
			logger.error("UserServiceImpl::approveRoleMapping::", e);
			return false;
		}
		return true;
	}

	@AuditLog
	@Override
	public boolean rejectRoleMapping(RoleRequest roleRequest) {
		try {
			if ((!StringUtils.isEmpty(userInfo.getRolesName())
					&& !userInfo.getRolesName().contains(ApplicationConstant.SUPER_ADMIN_ROLE))
					&& !userDAO.validateApproveRole(roleRequest.getAutogenRolesId(), roleRequest.getApprovedBy())) {
				return false;
			}
			String status = roleRequest.getRoleCreateStatus();
			BeanUtils.copyProperties(userDAO.getUnappRoleById(roleRequest.getAutogenRolesId()), roleRequest);
			roleRequest.setUserScreenMap(userDAO.getScreenMappingByRoleId(roleRequest.getAutogenRolesId()));
			userDAO.deleteUserScreenMapByRole(roleRequest.getAutogenRolesId());
			if ("New_Approve".equalsIgnoreCase(status)) {
				userDAO.deleteUnAppRole(roleRequest.getAutogenRolesId());
			} else {
				userDAO.updateUnappRoleStatus(roleRequest.getAutogenRolesId(), "Rejected");
			}
			if ("Exist_Approve".equalsIgnoreCase(status)) {
				userDAO.updateRoleEditFlag(roleRequest.getAutogenRolesId(), false);
			}
			roleRequest.setRoleCreateStatus("Rejected");
		} catch (Exception e) {
			logger.error("UserServiceImpl::rejectRoleMapping::" + e);
			return false;
		}
		return true;
	}

	private void insertAppRoleDetails(Long roleMapId, String comment, String approvedBy) throws Exception {
		Roles role = null;
		role = userDAO.getUnappRoleById(roleMapId);
		List<UserScreenMapApproved> UserScreenMapList = new ArrayList<>();
		if (role != null) {
			RolesApproved rolesApproved = new RolesApproved();
			BeanUtils.copyProperties(role, rolesApproved);
			for (UserScreenMap obj : role.getUserScreenMap()) {
				UserScreenMapApproved UserScreenMapApproved = new UserScreenMapApproved();
				BeanUtils.copyProperties(obj, UserScreenMapApproved);
				UserScreenMapApproved.setRole(rolesApproved);
				UserScreenMapList.add(UserScreenMapApproved);
			}
			rolesApproved.setRoleCreateStatus("Approved");
			rolesApproved.setEditFlag(false);
			rolesApproved.setComment(comment);
			rolesApproved.setApprovedBy(approvedBy);
			rolesApproved.setUserScreenMap(UserScreenMapList);
			userDAO.createAppRoleMapping(rolesApproved);
		}
		userDAO.updateUnappRoleStatus(roleMapId, "Approved");
	}

	@AuditLog
	@Override
	public UserDto addUser(UserDto userDto) throws Exception {
		UserDto userDtoApproved = userDAO.addUser(userDto);
		return userDtoApproved;
	}

	@AuditLog
	@Override
	public UserDto updateUser(UserDto userDto) throws Exception {
		return userDAO.updateUser(userDto);
	}

	@AuditLog
	@Override
	public boolean disableUser(String userId) {
		return userDAO.disableUser(userId);
	}

	@Override
	public void disableLockOutUsers() {
		userDAO.disableLockOutUsers();
	}

	@Override
	public Boolean isEntityNameExists(String name, String entity) {
		Boolean entityExists = false;
		switch (entity) {
		case "user":
			entityExists = userDAO.isUserIdExists(name);
			break;
		case "role":
			entityExists = userDAO.isRoleNameExists(name);
			break;
		}
		logger.debug("isEntityNameExists {} {} exist = {}", entity, name, entityExists);
		return entityExists;
	}
	
	/*
	@Override
	public ResponseEntity<GenericResponseReport> findLoginDetails(SearchAuditTrailRequest search) {
		GenericResponseReport genericResponse = new GenericResponseReport();
		List<GenericHeaderResponse> headerlist = null;
		List<GenericHeaderResponse> subHeaderlist = null;
		List<Map<Object, Object>> valueList = null;
		try {
			List<Object[]> resultList = userDAO.findLoginDetails(search);
			if (resultList != null && !resultList.isEmpty()) {
				headerlist = new ArrayList<GenericHeaderResponse>();
				subHeaderlist = new ArrayList<GenericHeaderResponse>();
				valueList = new ArrayList<Map<Object, Object>>();
				subHeaderlist.add(new GenericHeaderResponse("Login ID", "loginId"));
				subHeaderlist.add(new GenericHeaderResponse("EmployeeId", "employeeId"));
				subHeaderlist.add(new GenericHeaderResponse("Login Time", "loginTime"));
				subHeaderlist.add(new GenericHeaderResponse("Logout Time", "logoutTime"));
				subHeaderlist.add(new GenericHeaderResponse("No Of Attempt", "attempt"));
				subHeaderlist.add(new GenericHeaderResponse("Remarks", "remarks"));
				//subHeaderlist.add(new GenericHeaderResponse("Created By", "createdBy"));
				//subHeaderlist.add(new GenericHeaderResponse("Updated By", "updatedBy"));
				subHeaderlist.add(new GenericHeaderResponse("Record Added Date", "date"));
				subHeaderlist.add(new GenericHeaderResponse("Source IP", "sourceIP"));
				subHeaderlist.add(new GenericHeaderResponse("User Email", "userEmail"));
				headerlist.add(new GenericHeaderResponse("Login Report", "", subHeaderlist));
				
				for (Object[] obj : resultList) {
					Map<Object, Object> valueMap = new LinkedHashMap<>();
					valueMap.put("loginId", obj[0]);
					valueMap.put("employeeId", obj[1]);
					valueMap.put("loginTime", obj[2]);
					valueMap.put("logoutTime", obj[3]);
					valueMap.put("attempt", obj[4]);
					valueMap.put("remarks", obj[5]);
					valueMap.put("createdBy", obj[6]);
					valueMap.put("updatedBy", obj[7]);
					valueMap.put("date", obj[8]);
					valueMap.put("sourceIP", obj[10]);
					valueMap.put("userEmail", obj[11]);
					valueList.add(valueMap);
				}
				genericResponse.setStatus(200);
				genericResponse.setHeader(headerlist);
				genericResponse.setValue(valueList);
				genericResponse.setMessage("Data fetched sucessfully");
			} else {
				genericResponse.setStatus(200);
				genericResponse.setValue(null);
				genericResponse.setMessage("No data found");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::findLoginDetails " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue(null);
			genericResponse.setMessage("Error occured generating report");
		}
		return new ResponseEntity<GenericResponseReport>(new GenericResponseReport(genericResponse), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<GenericResponseReport> findAuditLogDetails(SearchAuditTrailRequest search) {
		GenericResponseReport genericResponse = new GenericResponseReport();
		List<GenericHeaderResponse> headerlist = null;
		List<GenericHeaderResponse> subHeaderlist = null;
		List<Map<Object, Object>> valueList = null;
		try {
			List<Object[]> resultList = userDAO.findAuditLogDetails(search);
			if (resultList != null && !resultList.isEmpty()) {
				headerlist = new ArrayList<GenericHeaderResponse>();
				subHeaderlist = new ArrayList<GenericHeaderResponse>();
				valueList = new ArrayList<Map<Object, Object>>();
				subHeaderlist.add(new GenericHeaderResponse("Audit Id", "auditId"));
				subHeaderlist.add(new GenericHeaderResponse("Employee Id", "employeeId"));
				subHeaderlist.add(new GenericHeaderResponse("User Email", "userEmail"));
				subHeaderlist.add(new GenericHeaderResponse("Source IP", "sourceIP"));
				subHeaderlist.add(new GenericHeaderResponse("Record Added Date", "date"));
				subHeaderlist.add(new GenericHeaderResponse("Action", "action"));
				subHeaderlist.add(new GenericHeaderResponse("Domain", "domain"));
				//subHeaderlist.add(new GenericHeaderResponse("Old Value", "oldValue"));
				//subHeaderlist.add(new GenericHeaderResponse("New Value", "newValue"));
				headerlist.add(new GenericHeaderResponse("Audit Details", "", subHeaderlist));
				
				for (Object[] obj : resultList) {
					Map<Object, Object> valueMap = new LinkedHashMap<>();
					valueMap.put("auditId", obj[0]);
					valueMap.put("employeeId", obj[1]);
					valueMap.put("userEmail", obj[2]);
					valueMap.put("sourceIP", obj[3]);
					valueMap.put("date", obj[4]);
					valueMap.put("action", obj[5]);
					valueMap.put("domain", obj[6]);
					valueMap.put("oldValue", obj[7]);
					valueMap.put("newValue", obj[8]);
					valueList.add(valueMap);
				}
				genericResponse.setStatus(200);
				genericResponse.setHeader(headerlist);
				genericResponse.setValue(valueList);
				genericResponse.setMessage("Data fetched sucessfully");
			} else {
				genericResponse.setStatus(200);
				genericResponse.setValue(null);
				genericResponse.setMessage("No data found");
			}
		} catch (Exception e) {
			logger.error("Error in CampaignServiceImpl::findAuditLogDetails " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue(null);
			genericResponse.setMessage("Error occured generating report");
		}
		return new ResponseEntity<GenericResponseReport>(new GenericResponseReport(genericResponse), HttpStatus.OK);
	}*/

	@Override
	public boolean changePassword(UserDto userDto) throws Exception {
		return userDAO.changePassword(userDto);
	}

	@Override
	public String getEncodedPassword(UserDto userDto) throws Exception {
		return userDAO.getEncodedPassword(userDto);
	}
	

	
}
