package com.cognicx.AppointmentRemainder.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.ValidationException;

import com.cognicx.AppointmentRemainder.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.dao.InventoryDAO;
//import com.cognicx.AppointmentRemainder.dao.InventoryDAO;
import com.cognicx.AppointmentRemainder.dao.RolesDAO;
import com.cognicx.AppointmentRemainder.dao.UserDAO;
import com.cognicx.AppointmentRemainder.message.request.RoleRequest;
import com.cognicx.AppointmentRemainder.message.request.SearchAuditTrailRequest;
import com.cognicx.AppointmentRemainder.message.response.ModuleScreenMapResponse;
import com.cognicx.AppointmentRemainder.message.response.RoleResponse;
import com.cognicx.AppointmentRemainder.Dto.AgentDto;
import com.cognicx.AppointmentRemainder.Dto.SurveyTypeDto;
import com.cognicx.AppointmentRemainder.Dto.TokenDetailsDto;
import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Dto.UserInventoryMapDto;
import com.cognicx.AppointmentRemainder.Dto.UserLeaveDetailsDto;
import com.cognicx.AppointmentRemainder.util.CommonUtil;
import com.cognicx.AppointmentRemainder.util.DateUtil;
import com.cognicx.AppointmentRemainder.util.UserInfo;

@Repository
public class UserDAOImpl implements UserDAO {

	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;

	@Autowired
	@Qualifier(ApplicationConstant.FIRST_JDBC_TEMPLATE)
	JdbcTemplate firstJdbcTemplate;

	@Autowired
	RolesDAOImpl rolesDAOImpl;

	@Autowired
	InventoryDAO inventoryDAO;

	@Autowired
	RolesDAO rolesDAO;

	@Autowired
	UserInfo userInfo;

	@Value("${app.lockout.days}")
	private String appLockOutDays = "30";

	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public Optional<UserDto> findByUsername(String username) throws Exception {
		StringBuilder sqlQry = null;
		Optional<UserDto> userOptional = null;
		UserDto userDto = new UserDto();
		List<Object[]> resultObj = null;
		try {
			sqlQry = new StringBuilder(
					"SELECT us.autogen_users_id, us.employee_id, us.password, us.status, us.CREATED_BY, us.email, us.first_name, us.last_name, us.mobile_number,usd.AUTOGEN_USERS_DETAILS_ID,ra.roles_name FROM appointment_remainder.Users_approved us, appointment_remainder.users_details_approved usd,appointment_remainder.user_roles_map_approved urm, appointment_remainder.roles_approved ra  WHERE employee_Id=:USERNAME and usd.AUTOGEN_USERS_ID=us.AUTOGEN_USERS_ID and usd.AUTOGEN_USERS_ID=urm.AUTOGEN_USERS_ID and ra.AUTOGEN_ROLES_ID=urm.AUTOGEN_ROLES_ID and us.status in ('ACTIVE','Approved','Existing')");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("USERNAME", username);
			resultObj = (List<Object[]>) queryObj.getResultList();

			if (resultObj != null && !resultObj.isEmpty()) {
				for (Object[] objects : resultObj) {
					userDto = new UserDto();
					userDto.setAutogenUsersId(new BigInteger(objects[0].toString()));
					userDto.setEmployeeId(objects[1].toString());
					userDto.setPassword(objects[2].toString());
					userDto.setStatus(objects[3].toString());
					userDto.setEmail(objects[5].toString());
					userDto.setFirstName(objects[6].toString());
					userDto.setLastName(objects[7].toString());
					if (null != objects[8]) {
						userDto.setMobileNumber(objects[8].toString());
					}
					userDto.setAutogenUsersDetailsId(objects[9].toString());
					// userDto.setDomainId(CommonUtil.nullRemoveInt(objects[9]));
					// userDto.setBuId(CommonUtil.nullRemoveInt(objects[10]));
					Set<Roles> roleset = new HashSet<>();
					Roles roles = new Roles();
					roles.setRolesName(objects[10].toString());
					roleset.add(roles);
					userDto.setRoles(roleset);
					List<String> rolesList = new ArrayList<String>();
					rolesList.add(roles.getRolesName());
					userDto.setRolesList(rolesList);

				}
			}

			/*
			 * sqlQry = new StringBuilder(
			 * "select AUTOGEN_ROLES_ID,ROLES_NAME	from user_rule.user_roles_map_approved where AUTOGEN_USERS_DETAILS_ID =:userDetailId and AUTOGEN_USERS_ID=:userId"
			 * ); queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			 * queryObj.setParameter("userDetailId", userDto.getAutogenUsersDetailsId());
			 * queryObj.setParameter("userId", userDto.getAutogenUsersId()); resultObj =
			 * (List<Object[]>) queryObj.getResultList(); List<String> roleList = new
			 * ArrayList<>(); if (!resultObj.isEmpty()) { for (Object[] obj : resultObj) {
			 * roleList.add(String.valueOf(obj[1])); } } userDto.setRolesList(roleList);
			 */
			userOptional = Optional.ofNullable(userDto);
		} catch (Exception e) {
			logger.error("Exception findByUsername() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userOptional;
	}

	@Override
	public BigInteger findUserDetailIdUsername(String username) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> result = null;
		BigInteger userdetId = null;
		try {
			sqlQry = new StringBuilder(
					"SELECT usd.AUTOGEN_USERS_DETAILS_ID FROM Users us, users_details usd WHERE us.employee_Id=:USERNAME and usd.AUTOGEN_USERS_ID=us.AUTOGEN_USERS_ID");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("USERNAME", username);
			result = (List<Object[]>) queryObj.getResultList();
			if (result != null && !result.isEmpty()) {
				userdetId = new BigInteger(CommonUtil.nullRemove(result.get(0)));
			}
		} catch (Exception e) {
			logger.error("Exception :: findUserDetailIdUsername() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userdetId;
	}

	@Override
	public Boolean existsByUsername(String username) throws Exception {
		StringBuilder sqlQry = null;
		boolean user = false;
		List<Object[]> result = null;
		try {
			sqlQry = new StringBuilder("SELECT 1 FROM [user_rule].[users_approved] WHERE EMPLOYEE_ID=:USERNAME");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("USERNAME", username);
			result = queryObj.getResultList();
			if (!result.isEmpty()) {
				user = true;
			}
		} catch (Exception e) {
			logger.error("Exception :: existsByUsername() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return user;
	}

	@Override
	public Boolean existsByEmail(String email) throws Exception {
		StringBuilder sqlQry = null;
		boolean user = false;
		List<Object[]> result = null;
		try {
			sqlQry = new StringBuilder("SELECT 1 FROM [user_rule].[users_approved] WHERE EMAIL=:EMAIL");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("EMAIL", email);
			result = queryObj.getResultList();
			if (!result.isEmpty()) {
				user = true;
			}
		} catch (Exception e) {
			logger.error("Exception :: existsByEmail() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return user;	
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception {
		Object[] resultObj = new Object[5];
		StringBuilder sqlQry = null;
		boolean insertStatus = false;
		List<LoginDetails> result = null;
		try {
			if (insertFlag) {
				logger.info("Insert Flag is :"+insertFlag);
				LoginDetails loginDetails = new LoginDetails();
				loginDetails.setEmployeeId(CommonUtil.nullRemove(loginInfo[0]));
				loginDetails.setSourceIP(CommonUtil.nullRemove(loginInfo[7]));
				loginDetails.setUserEmail(CommonUtil.nullRemove(loginInfo[8]));

				result = findLoginDetailsByEmployeeId(loginInfo);
				if (result != null && !result.isEmpty() && result.get(0) != null) {
					LoginDetails findLoginDetails = result.get(0);
					Date loginTime = findLoginDetails.getLoginTime();
					int diffInDays = (int) ((loginTime.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
					if (Integer.valueOf(appLockOutDays) <= diffInDays) {
						UpdateUserStatusByEmployeeId(CommonUtil.nullRemove(loginInfo[0]), "INACTIVE",
								CommonUtil.nullRemove(loginInfo[6]));
						loginDetails.setRemarks("Your account is inactive user not logged into the system last "
								+ appLockOutDays + " consecutive days. please contact the system administrator");

					}
				}else {
					logger.info("Find Login Details by Employee ID result is NULL");
				}

				if (loginInfo[3] != null && (!BigInteger.ZERO.equals(loginInfo[3]))) {
					if (result != null && !result.isEmpty() && result.get(0) != null) {
						LoginDetails findLoginDetails = result.get(0);
						if (!BigInteger.ZERO.equals(findLoginDetails.getNoOfAttempt())) {
							BigInteger totalAttempt = findLoginDetails.getNoOfAttempt();
							BigInteger val = new BigInteger(CommonUtil.nullRemove(loginInfo[3]));
							if (totalAttempt != null) {
								totalAttempt = totalAttempt.add(val);
								loginDetails.setNoOfAttempt(totalAttempt);
							} else {
								loginDetails.setNoOfAttempt(val);
							}
						} else {
							loginDetails.setNoOfAttempt(new BigInteger(CommonUtil.nullRemove(loginInfo[3])));
						}
					} else {
						loginDetails.setNoOfAttempt(new BigInteger(CommonUtil.nullRemove(loginInfo[3])));
					}
				}
				if ((loginDetails.getRemarks() == null || loginDetails.getRemarks().isEmpty()) && loginInfo[4] != null
						&& !loginInfo[4].toString().isEmpty()) {
					loginDetails.setRemarks(loginInfo[4].toString());
				}
				if (loginInfo[5] != null && !loginInfo[5].toString().isEmpty()) {
					loginDetails.setCreatedBy(CommonUtil.nullRemove(loginInfo[5]));
					loginDetails.setUpdatedBy(CommonUtil.nullRemove(loginInfo[5]));
				}
				loginDetails.setRecAddDt(new Timestamp(System.currentTimeMillis()));
				loginDetails.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
				/** Added on 16th March 2024*/
				logger.info("LoginDetails Bean Object before setting Login Time:"+loginDetails.toString());
				loginDetails.setLoginTime(new Timestamp(System.currentTimeMillis()));
				logger.info("LoginDetails Bean Object after setting Login Time:"+loginDetails.toString());
				firstEntityManager.persist(loginDetails);
				// Update the last login timestamp against the user.
				updateUserLastLogin(loginDetails.getEmployeeId());
				insertStatus = true;
			} else {
				insertStatus = updateLoginDetails(loginInfo);
			}
		} catch (Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Exception :: saveOrUpdateLoginDetails() : {}",str.toString());
		} finally {
			firstEntityManager.close();
		}
		resultObj[0] = insertStatus;
		return resultObj;
	}

	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateLoginDetails(Object[] loginInfo) throws Exception {
		List<LoginDetails> result = null;
		boolean insertStatus = false;
		try {
			result = findLoginDetailsByEmployeeId(loginInfo);
			if (!result.isEmpty()) {
				LoginDetails loginDetails = result.get(0);
				if (loginInfo[2] != null && (boolean) loginInfo[2]) {
					loginDetails.setLogoutTime(new Date());
					loginDetails.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
				}
				if (!CommonUtil.nullRemove(loginInfo[4]).isEmpty()) {
					loginDetails.setRemarks(CommonUtil.nullRemove(loginInfo[4]));
				}
				if (!CommonUtil.nullRemove(loginInfo[6]).isEmpty()) {
					loginDetails.setUpdatedBy(CommonUtil.nullRemove(loginInfo[6]));
				}
				insertStatus = true;
			}else {
				logger.info("Update Login Details Find Result is NULL ");
			}

		} catch (Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Exception :: updateLoginDetails() : {}", str.toString());
		} finally {
			firstEntityManager.close();
		}
		return insertStatus;
	}

	public List<LoginDetails> findLoginDetailsByEmployeeId(Object[] loginInfo) throws Exception {
		StringBuilder sqlQry = null;
		List<LoginDetails> result = null;
		try {
			sqlQry = new StringBuilder("SELECT MAX(l) FROM LoginDetails l WHERE l.employeeId=:EMPLOYEEID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("EMPLOYEEID", CommonUtil.nullRemove(loginInfo[0]));
			result = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Exception :: findLoginDetailsByEmployeeId() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return result;
	}

	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateUserLastLogin(String employeeId) {
		Query qryObj = firstEntityManager
				.createQuery("UPDATE UsersApproved SET lastLoginTime = :lastLoginTime WHERE employeeId = :employeeId");
		qryObj.setParameter("lastLoginTime", new Timestamp(System.currentTimeMillis()));
		qryObj.setParameter("employeeId", employeeId);
		qryObj.executeUpdate();
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void disableLockOutUsers() {
		Query qryObj = firstEntityManager.createNativeQuery(
				"update user_rule.users_approved set status='INACTIVE' where status='ACTIVE' and LAST_LOGIN_TIME < (CURRENT_TIMESTAMP - 90)");
		int rowsAffected = qryObj.executeUpdate();
		logger.info("disableLockOutUsers count : {}", String.valueOf(rowsAffected));

	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserDto save(UserDto userDto) throws Exception {
		try {
			if (userDto != null) {
				Users users = new Users();
				BeanUtils.copyProperties(userDto, users);
				users.setStatus(userDto.getStatus());
				users.setAutogenUsersId(null);
				firstEntityManager.persist(users);
				if (users.getAutogenUsersId() != null) {
					UsersDetail usersDet = new UsersDetail();
					BeanUtils.copyProperties(userDto, usersDet);
					usersDet.setAutogenUsersId(users.getAutogenUsersId());
					if (null != userDto.getBusinessUnit() && !userDto.getBusinessUnit().isEmpty()) {
						usersDet.setBuId((null != userDto.getBusinessUnit().get(0).get("buId"))
								? new Integer(userDto.getBusinessUnit().get(0).get("buId"))
								: Integer.valueOf(ApplicationConstant.DEFAULT_BU_ID));
					}
					if (null != userDto.getDomain() && !userDto.getDomain().isEmpty()) {
						usersDet.setDomainId((null != userDto.getDomain().get(0).get("domainId"))
								? new Integer(userDto.getDomain().get(0).get("domainId"))
								: Integer.valueOf(ApplicationConstant.DEFAULT_DOMAIN_ID));
					}
					if (userDto.getRoleDetailList() != null && !userDto.getRoleDetailList().isEmpty()) {
						usersDet.setAutogenRolesId(
								new BigInteger(userDto.getRoleDetailList().get(0).getOrDefault("roleId", "0")));
						usersDet.setRolesName(userDto.getRoleDetailList().get(0).getOrDefault("rolesName", ""));
					}
					usersDet.setAutogenUsersDetailsId(null);
					firstEntityManager.persist(usersDet);
					if (usersDet.getAutogenUsersDetailsId() != null) {
						if (userDto.getGroupId()!=null  || (!userDto.getGroupId().isEmpty())) {
							logger.info("Group id :" + userDto.getGroupId());
							UserGroupsMap groupRolesName = new UserGroupsMap();
							groupRolesName.setAutogenUsersId(users.getAutogenUsersId());
							groupRolesName.setAutogenUserGroupsId(new BigInteger(userDto.getGroupId()));
							groupRolesName.setStatus(userDto.getStatus());
							groupRolesName.setRecAddDt(userDto.getRecAddDt());
							groupRolesName.setRecUpdateDt(userDto.getRecUpdateDt());
							firstEntityManager.persist(groupRolesName);
						}
						if (userDto.getDomain() != null) {
							for (Map<String, String> buDet : userDto.getBusinessUnit()) {
							UserDomainMap userDomainMap = new UserDomainMap();
								userDomainMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userDomainMap.setAutogenUsersId(users.getAutogenUsersId());
								userDomainMap.setBusinessUnitId(new BigInteger(buDet.get("buId")));
								userDomainMap.setBusinessUnitName(buDet.get("buName"));
								userDomainMap.setDomainId(new BigInteger(userDto.getDomain().get(0).get("domainId")));
								userDomainMap.setDomainName(userDto.getDomain().get(0).get("domainName"));
								userDomainMap.setAutogenUserDomainMapId(null);
								userDomainMap.setCreatedBy(userDto.getCreatedBy());
								userDomainMap.setUpdatedBy(userDto.getUpdatedBy());
								userDomainMap.setRecAddDt(new Timestamp(System.currentTimeMillis()));
								userDomainMap.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
								firstEntityManager.persist(userDomainMap);
							}
						}
						if (userDto.getRoleDetailList() != null) {
							for (Map<String, String> roleDet : userDto.getRoleDetailList()) {
								UserRoleMap userRoleMap = new UserRoleMap();
								userRoleMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userRoleMap.setAutogenUsersId(users.getAutogenUsersId());
								userRoleMap.setRoleId(new BigInteger(roleDet.get("roleId")));
								userRoleMap.setRoleName(roleDet.get("rolesName"));
								userRoleMap.setAutogenUserDomainMapId(null);
								userRoleMap.setCreatedBy(userDto.getCreatedBy());
								userRoleMap.setUpdatedBy(userDto.getUpdatedBy());
								userRoleMap.setRecAddDt(new Timestamp(System.currentTimeMillis()));
								userRoleMap.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
								firstEntityManager.persist(userRoleMap);
							}
						}
					}
					if (usersDet.getAutogenUsersDetailsId() != null) {
						if (userDto.getUserInventoryMapDtoList() != null) {
							for (UserInventoryMapDto userInventoryMapDto : userDto.getUserInventoryMapDtoList()) {
								UserInventoryMap userInventoryMap = new UserInventoryMap();
								userInventoryMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userInventoryMap.setInventoryRegionId(userInventoryMapDto.getInventoryRegionId());
								userInventoryMap.setInventoryRegionName(userInventoryMapDto.getInventoryRegionName());
								userInventoryMap.setInventoryCenterId(userInventoryMapDto.getInventoryCenterId());
								userInventoryMap.setInventoryCenterName(userInventoryMapDto.getInventoryCenterName());
								userInventoryMap.setInventoryClientId(userInventoryMapDto.getInventoryClientId());
								userInventoryMap.setInventoryClientName(userInventoryMapDto.getInventoryClientName());
								userInventoryMap.setInventoryProcessId(userInventoryMapDto.getInventoryProcessId());
								userInventoryMap.setInventoryProcessName(userInventoryMapDto.getInventoryProcessName());
								userInventoryMap.setInventoryCategoryId(userInventoryMapDto.getInventoryCategoryId());
								userInventoryMap
										.setInventoryCategoryName(userInventoryMapDto.getInventoryCategoryName());
								userInventoryMap.setStatus("ACTIVE");
								userInventoryMap.setCreatedBy(userDto.getCreatedBy());
								firstEntityManager.persist(userInventoryMap);
							}
						}
						if (userDto.getSurveyTypes() != null) {
							for (SurveyTypeDto surveyTypeDto : userDto.getSurveyTypes()) {
								UserSurveyMapping surveyMapping = new UserSurveyMapping();
								surveyMapping.setAutogenUserId(usersDet.getAutogenUsersId());
								surveyMapping.setEmployeeId(users.getEmployeeId());
								surveyMapping.setSurveyId(surveyTypeDto.getId());
								surveyMapping.setSurveyName(surveyTypeDto.getLabel());
								surveyMapping.setCreatedBy(userDto.getCreatedBy());
								surveyMapping.setUpdatedBy(userDto.getCreatedBy());
								surveyMapping.setStatus("ACTIVE");
								firstEntityManager.persist(surveyMapping);
							}
						}

						if (userDto.getReports() != null) {
							UserReportMap userReportMap = null;
							for (Reports report : userDto.getReports()) {
								userReportMap = new UserReportMap();
								userReportMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userReportMap.setAutogenReportMasterId(report.getId());
								userReportMap.setReportName(report.getReportName());
								userReportMap.setCreatedBy(userDto.getCreatedBy());
								userReportMap.setStatus("ACTIVE");
								firstEntityManager.persist(userReportMap);
							}
						}

						if (userDto.getUserLeaveDetailsDtoList() != null) {
							for (UserLeaveDetailsDto userLeaveDetailsDto : userDto.getUserLeaveDetailsDtoList()) {
								UserLeaveDetails userLeaveDetails = new UserLeaveDetails();
								userLeaveDetails.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userLeaveDetails.setToDate(new Date());
								userLeaveDetails.setFromDate(new Date());
								userLeaveDetails.setNoOfDays(userLeaveDetailsDto.getNoOfDays());
								userLeaveDetails.setReasons(userLeaveDetailsDto.getReasons());
								userLeaveDetails.setComments(userLeaveDetailsDto.getComments());
								userLeaveDetails.setStatus("ACTIVE");
								userLeaveDetails.setCreatedBy(userDto.getCreatedBy());
								firstEntityManager.persist(userLeaveDetails);
							}
						}
					}
					BeanUtils.copyProperties(usersDet, userDto);
				}
				BeanUtils.copyProperties(users, userDto);
			}
		} catch (Exception e) {
			logger.error("Exception :: save() : {} ", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userDto;
	}

	@Override
	public List<UserDto> getApprovedUsersList(String roleName) throws Exception {
		StringBuilder sqlQry = null;
		List<UsersApproved> resultObj = new ArrayList<>();
		List<UserDto> userDtoList = new ArrayList<>();
		Query queryObj = null;
		try {
			sqlQry = new StringBuilder("SELECT us FROM UsersApproved us");
			if (!roleName.isEmpty()) {
				sqlQry = new StringBuilder(
						"SELECT us FROM Users us, UsersDetailApproved ud where us.status='ACTIVE' and ud.autogenUsersId=us.autogenUsersId and ud.autogenRolesId=(SELECT r.autogenRolesId FROM Roles r where r.rolesName='"
								+ roleName + "')");
			}
			queryObj = firstEntityManager.createQuery(sqlQry.toString());
			resultObj = queryObj.getResultList();

			for (UsersApproved users : resultObj) {
				UserDto userDto = new UserDto();
				BeanUtils.copyProperties(users, userDto);
				List<UsersDetailApproved> subResultObj = new ArrayList<>();
				sqlQry = new StringBuilder("SELECT usd FROM UsersDetailApproved usd where usd.autogenUsersId=:USERID");
				queryObj = null;
				queryObj = firstEntityManager.createQuery(sqlQry.toString());
				queryObj.setParameter("USERID", users.getAutogenUsersId());
				subResultObj = queryObj.getResultList();
				for (UsersDetailApproved users2 : subResultObj) {
					BeanUtils.copyProperties(users2, userDto);
					userDto.setRolesName(users2.getRolesName());
					List<Map<String, String>> roleDetList = getApprovedRoleDetails(
							String.valueOf(users.getAutogenUsersId()),
							String.valueOf(users2.getAutogenUsersDetailsId()));
					if (roleDetList != null) {
						userDto.setRoleDetailList(roleDetList);
						userDto.setRolesName(
								roleDetList.stream().map(a -> a.get("rolesName")).collect(Collectors.joining(",")));
					}
					String group = getApprovedGroupDetails(
							String.valueOf(users.getAutogenUsersId()));
					logger.info(users.getAutogenUsersId().toString());
					if (group != null) {
						logger.info("Group Name : "+group);
                        userDto.setGroupName(group);
					}
					UserDto userDtoDomainDet = getApprovedDomainDetails(String.valueOf(users.getAutogenUsersId()),
							String.valueOf(users2.getAutogenUsersDetailsId()));
					if (userDtoDomainDet != null) {
						userDto.setDomain(userDtoDomainDet.getDomain());
						userDto.setBusinessUnit(userDtoDomainDet.getBusinessUnit());
					}
					List<Object[]> resultObjList = new ArrayList<>();
					queryObj = firstEntityManager.createQuery(
							"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='ACTIVE'");
					queryObj.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList();
					List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>();
					for (UserInventoryMap userInventoryMap : userInventoryMapResult) {
						UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
						BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
						userInventoryMapDtos.add(userInventoryMapDto);
					}
					userDto.setUserInventoryMapDtoList(userInventoryMapDtos);

					resultObjList = new ArrayList<>();
					sqlQry = new StringBuilder(
							"SELECT im.AUTOGEN_REPORT_MASTER_ID, im.REPORT_NAME from user_rule.REPORT_MASTER im, user_rule.USER_REPORT_MAP usd where im.AUTOGEN_REPORT_MASTER_ID = usd.AUTOGEN_REPORT_MASTER_ID AND usd.AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID AND usd.STATUS=im.STATUS AND usd.STATUS='ACTIVE'");
					Query subQueryObj2 = firstEntityManager.createNativeQuery(sqlQry.toString());
					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					resultObjList = subQueryObj2.getResultList();
					List<Reports> reports = new ArrayList<>();
					for (Object[] userreportmap : resultObjList) {
						Reports report = new Reports();
						report.setId(new BigInteger((userreportmap[0].toString())));
						report.setReportName(String.valueOf(userreportmap[1]));
						reports.add(report);
					}
					userDto.setReports(reports);

					String grouprolesname = "";
					sqlQry = new StringBuilder(
							"SELECT im from UserLeaveDetails im where im.autogenUsersDetailsId=:USERDETAILSID AND im.status != 'INACTIVE'");
					subQueryObj2 = firstEntityManager.createQuery(sqlQry.toString());
					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());

					List<UserLeaveDetails> userLeaveDetailsList = new ArrayList<>();
					sqlQry = new StringBuilder(
							"SELECT im from UserLeaveDetails im where im.autogenUsersDetailsId=:USERDETAILSID AND im.status != 'INACTIVE'");
					subQueryObj2 = firstEntityManager.createQuery(sqlQry.toString());
					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					userLeaveDetailsList = subQueryObj2.getResultList();
					List<UserLeaveDetailsDto> uesrLeaveDetailsDtoList = new ArrayList<>();
					for (UserLeaveDetails userLeaveDetails : userLeaveDetailsList) {
						UserLeaveDetailsDto userLeaveDetailsDto = new UserLeaveDetailsDto();
						userLeaveDetailsDto.setLeaveDetailsId(userLeaveDetails.getAutogenUserLeaveDetailsId());
						userLeaveDetailsDto.setFromDate(DateUtil.convertDatetoString(userLeaveDetails.getFromDate(),
								DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
						userLeaveDetailsDto.setToDate(DateUtil.convertDatetoString(userLeaveDetails.getToDate(),
								DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
						userLeaveDetailsDto.setNoOfDays(userLeaveDetails.getNoOfDays());
						userLeaveDetailsDto.setReasons(userLeaveDetails.getReasons());
						userLeaveDetailsDto.setComments(userLeaveDetails.getComments());
						userLeaveDetailsDto.setStatus(userLeaveDetails.getStatus());
						uesrLeaveDetailsDtoList.add(userLeaveDetailsDto);
					}
					userDto.setUserLeaveDetailsDtoList(uesrLeaveDetailsDtoList);
					List<SurveyTypeDto> surveyType = getUserSurveyTypeList(users.getAutogenUsersId().toString());
					if (!surveyType.isEmpty()) {
						// Clearing the StringBuilder and result object list is unnecessary as you're reassigning them
						sqlQry = new StringBuilder(
								"SELECT survey_id, survey_name, employee_id, autogen_id, status FROM user_rule.user_survey_mapping WHERE autogen_user_id=:userId");

						subQueryObj2 = firstEntityManager.createNativeQuery(sqlQry.toString());
						subQueryObj2.setParameter("userId", users.getAutogenUsersId());
						resultObjList = subQueryObj2.getResultList();
						if (!resultObjList.isEmpty()) {
							for (Object[] surveyObj : resultObjList) {
								SurveyTypeDto surveyTypeDto = new SurveyTypeDto();
								surveyTypeDto.setId((int) surveyObj[0]);
								surveyTypeDto.setLabel((String) surveyObj[1]);
								surveyTypeDto.setUserId(CommonUtil.nullRemove(surveyObj[2]).toString());
								surveyTypeDto.setAutogenId(surveyObj[3].toString());
								surveyTypeDto.setStatus((String) surveyObj[4]);
								surveyType.add(surveyTypeDto);
							}
						}

						userDto.setSurveyTypes(surveyType);
					}
				}
				userDtoList.add(userDto);
			}
		} catch (Exception e) {
			logger.error("Exception :: getApprovedUsersList() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userDtoList;
	}

	@Override
	public List<UserDto> getUsersList(String roleName) throws Exception {
		StringBuilder sqlQry = null;
		List<Users> resultObj = new ArrayList<>();
		List<UserDto> userDtoList = new ArrayList<>();
		Query queryObj = null;
		try {
			sqlQry = new StringBuilder("SELECT us FROM Users us");
			if (!userInfo.getRolesName().contains(ApplicationConstant.SUPER_ADMIN_ROLE)
					&& null != userInfo.getEmployeeId()) {
				sqlQry = sqlQry.append(" where ( updatedBy !='"
						+ userInfo.getEmployeeId() + "')");
			}
			if (!roleName.isEmpty()) {
				sqlQry = new StringBuilder(
						"SELECT us FROM Users us, UsersDetail ud where us.status='ACTIVE' and ud.autogenUsersId=us.autogenUsersId and ud.autogenRolesId=(SELECT r.autogenRolesId FROM Roles r where r.rolesName='"
								+ roleName + "')");
			}
			queryObj = firstEntityManager.createQuery(sqlQry.toString());
			resultObj = queryObj.getResultList();
			for (Users users : resultObj) {
				UserDto userDto = new UserDto();
				BeanUtils.copyProperties(users, userDto);
				List<UsersDetail> subResultObj = new ArrayList<>();
				sqlQry = new StringBuilder("SELECT usd FROM UsersDetail usd where usd.autogenUsersId=:USERID");
				queryObj = null;
				queryObj = firstEntityManager.createQuery(sqlQry.toString());
				queryObj.setParameter("USERID", users.getAutogenUsersId());
				subResultObj = queryObj.getResultList();
				for (UsersDetail users2 : subResultObj) {
					BeanUtils.copyProperties(users2, userDto);
					userDto.setRolesName(users2.getRolesName());
					List<Map<String, String>> roleDetList = getRoleDetails(String.valueOf(users.getAutogenUsersId()),
							String.valueOf(users2.getAutogenUsersDetailsId()));
					if (roleDetList != null) {
						userDto.setRoleDetailList(roleDetList);
						userDto.setRolesName(
								roleDetList.stream().map(a -> a.get("rolesName")).collect(Collectors.joining(",")));
						userDto.setAutogenRolesId(new BigInteger(roleDetList.get(0).get("roleId")));
					}
					UserDto userDtoDomainDet = getDomainDetails(String.valueOf(users.getAutogenUsersId()),
							String.valueOf(users2.getAutogenUsersDetailsId()));
					if (userDtoDomainDet != null) {
						userDto.setDomain(userDtoDomainDet.getDomain());
						userDto.setBusinessUnit(userDtoDomainDet.getBusinessUnit());
					}
					List<Object[]> resultObjList = new ArrayList<>();
					queryObj = firstEntityManager.createQuery(
							"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='ACTIVE'");
					queryObj.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList();
					List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>();
					for (UserInventoryMap userInventoryMap : userInventoryMapResult) {
						UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
						BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
						userInventoryMapDtos.add(userInventoryMapDto);
					}
					userDto.setUserInventoryMapDtoList(userInventoryMapDtos);

					resultObjList = new ArrayList<>();
					sqlQry = new StringBuilder(
							"SELECT im.AUTOGEN_REPORT_MASTER_ID, im.REPORT_NAME from user_rule.REPORT_MASTER im, user_rule.USER_REPORT_MAP usd where im.AUTOGEN_REPORT_MASTER_ID = usd.AUTOGEN_REPORT_MASTER_ID AND usd.AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID AND usd.STATUS=im.STATUS AND usd.STATUS='ACTIVE'");
					Query subQueryObj2 = firstEntityManager.createNativeQuery(sqlQry.toString());
					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					resultObjList = subQueryObj2.getResultList();
					List<Reports> reports = new ArrayList<>();
					for (Object[] userreportmap : resultObjList) {
						Reports report = new Reports();
						report.setId(new BigInteger((userreportmap[0].toString())));
						report.setReportName(String.valueOf(userreportmap[1]));
						reports.add(report);
					}
					userDto.setReports(reports);

					List<UserLeaveDetails> userLeaveDetailsList = new ArrayList<>();
					sqlQry = new StringBuilder(
							"SELECT im from UserLeaveDetails im where im.autogenUsersDetailsId=:USERDETAILSID AND im.status != 'INACTIVE'");
					subQueryObj2 = firstEntityManager.createQuery(sqlQry.toString());
					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					userLeaveDetailsList = subQueryObj2.getResultList();
					List<UserLeaveDetailsDto> uesrLeaveDetailsDtoList = new ArrayList<>();
					for (UserLeaveDetails userLeaveDetails : userLeaveDetailsList) {
						UserLeaveDetailsDto userLeaveDetailsDto = new UserLeaveDetailsDto();
						userLeaveDetailsDto.setLeaveDetailsId(userLeaveDetails.getAutogenUserLeaveDetailsId());
						userLeaveDetailsDto.setFromDate(DateUtil.convertDatetoString(userLeaveDetails.getFromDate(),
								DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
						userLeaveDetailsDto.setToDate(DateUtil.convertDatetoString(userLeaveDetails.getToDate(),
								DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
						userLeaveDetailsDto.setNoOfDays(userLeaveDetails.getNoOfDays());
						userLeaveDetailsDto.setReasons(userLeaveDetails.getReasons());
						userLeaveDetailsDto.setComments(userLeaveDetails.getComments());
						userLeaveDetailsDto.setStatus(userLeaveDetails.getStatus());
						uesrLeaveDetailsDtoList.add(userLeaveDetailsDto);
					}
					userDto.setUserLeaveDetailsDtoList(uesrLeaveDetailsDtoList);
					List<SurveyTypeDto> surveyType = new ArrayList<>();
					sqlQry.setLength(0);
					resultObjList = new ArrayList<>();
					sqlQry = new StringBuilder(
							"SELECT survey_id,survey_name,employee_id,autogen_id,status from user_rule.user_survey_mapping where autogen_user_id=:userId");
					subQueryObj2 = firstEntityManager.createNativeQuery(sqlQry.toString());
					subQueryObj2.setParameter("userId", users.getAutogenUsersId());
					resultObjList = subQueryObj2.getResultList();
					if (resultObjList.size() > 0) {
						for (Object[] surveyObj : resultObjList) {
							SurveyTypeDto surveyTypeDto = new SurveyTypeDto();
							surveyTypeDto.setId((int) surveyObj[0]);
							surveyTypeDto.setLabel((String) surveyObj[1]);
							surveyTypeDto.setUserId(CommonUtil.nullRemove(surveyObj[2]).toString());
							surveyTypeDto.setAutogenId(surveyObj[3].toString());
							surveyTypeDto.setStatus((String) surveyObj[4]);
							surveyType.add(surveyTypeDto);
						}
					}
					userDto.setSurveyTypes(surveyType);
					userDto.setEditFlag(true);
				}
				userDtoList.add(userDto);
			}
		} catch (Exception e) {
			logger.error("Exception :: getUsersList() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userDtoList;
	}

	@Override
	public List<UserInventoryMapDto> getUserInventoryMapList(BigInteger userDetailsId) throws Exception {
		Query queryObj = null;
		List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>();
		try {
			List<Object[]> resultObjList = new ArrayList<>();
			queryObj = firstEntityManager.createQuery(
					"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='ACTIVE'");
			queryObj.setParameter("USERDETAILSID", userDetailsId);
			List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList();
			for (UserInventoryMap userInventoryMap : userInventoryMapResult) {
				UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
				BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
				userInventoryMapDtos.add(userInventoryMapDto);
			}
		} catch (Exception e) {
			logger.error("Exception :: getUserInventoryMapList() : {} ", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userInventoryMapDtos;
	}

	/*
	 * @Override public List<UserInventoryMapDto> getReportsByUser(String
	 * userDetailsId) throws Exception { Query queryObj = null;
	 * List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>(); try {
	 * List<Object[]> resultObjList = new ArrayList<>(); queryObj =
	 * firstEntityManager.createQuery(
	 * "SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='ACTIVE'"
	 * ); queryObj.setParameter("USERDETAILSID", userDetailsId);
	 * List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList(); for
	 * (UserInventoryMap userInventoryMap : userInventoryMapResult) {
	 * UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
	 * BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
	 * userInventoryMapDtos.add(userInventoryMapDto); } } catch (Exception e) {
	 * logger.info("Exception :: UserDAOImpl :: getUserInventoryMapList() : " + e);
	 * } finally { firstEntityManager.close(); } return userInventoryMapDtos; }
	 */

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean UpdateUserStatus(UserDto userDto) throws Exception {
		boolean result = false;
		try {
			Users user = firstEntityManager.find(Users.class, userDto.getAutogenUsersId());
			if (user != null) {
				if (!CommonUtil.nullRemove(userDto.getStatus()).isEmpty()) {
					user.setStatus(userDto.getStatus());
				}
				if (!CommonUtil.nullRemove(userDto.getUpdatedBy()).isEmpty()) {
					user.setUpdatedBy(userDto.getUpdatedBy());
				}
				firstEntityManager.merge(user);
				result = true;
			}
		} catch (Exception e) {
			logger.error("Exception :: UpdateUserStatus() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return result;
	}

	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean UpdateUserStatusByEmployeeId(String employeeId, String status, String updatedBy) throws Exception {
		List<Users> usersList = null;
		boolean result = false;
		try {
			StringBuilder sqlQry = new StringBuilder("SELECT u FROM Users u WHERE u.employeeId=:EMPLOYEEID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("EMPLOYEEID", CommonUtil.nullRemove(employeeId));
			usersList = queryObj.getResultList();
			if (usersList != null && !usersList.isEmpty()) {
				Users users = usersList.get(0);
				if (!CommonUtil.nullRemove(status).isEmpty()) {
					users.setStatus(status);
				}
				if (!CommonUtil.nullRemove(updatedBy).isEmpty()) {
					users.setUpdatedBy(updatedBy);
				}
				firstEntityManager.merge(users);
				result = true;
			}
		} catch (Exception e) {
			logger.error("Exception :: UpdateUserStatusByEmployeeId() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return result;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean updateExistingUser(UserDto userDto) throws Exception {
		StringBuilder sqlQry = null;
		boolean result = false;
		// List<Users> userList = null;
		try {
			UsersApproved user = firstEntityManager.find(UsersApproved.class, userDto.getAutogenUsersId());
			if (user != null) {
				if (!CommonUtil.nullRemove(userDto.getFirstName()).isEmpty()) {
					user.setFirstName(userDto.getFirstName());
				}
				if (!CommonUtil.nullRemove(userDto.getLastName()).isEmpty()) {
					user.setLastName(userDto.getLastName());
				}
				if (!CommonUtil.nullRemove(userDto.getEmail()).isEmpty()) {
					user.setEmail(userDto.getEmail());
				}
				if (!CommonUtil.nullRemove(userDto.getMobileNumber()).isEmpty()) {
					user.setMobileNumber(userDto.getMobileNumber());
				}

				if (!CommonUtil.nullRemove(userDto.getStatus()).isEmpty()) {
					user.setStatus(userDto.getStatus());
				}
				if (!CommonUtil.nullRemove(userDto.getPassword()).isEmpty()) {
					user.setPassword(userDto.getPassword());
				}
				if (!CommonUtil.nullRemove(userDto.getApproverComment()).isEmpty()) {
					user.setApproverComment(userDto.getApproverComment());
				}
				if (!CommonUtil.nullRemove(userDto.getApprovedBy()).isEmpty()) {
					user.setApprovedBy(userDto.getApprovedBy());
				}
				if (!CommonUtil.nullRemove(userDto.getApprovedOn()).isEmpty()) {
					user.setApprovedOn(userDto.getApprovedOn());
				}
				if (!CommonUtil.nullRemove(userDto.getRecUpdateDt()).isEmpty()) {
					user.setRecUpdateDt(userDto.getRecUpdateDt());
				}
				user.setEditFlag(userDto.isEditFlag());

				firstEntityManager.merge(user);

				List<UsersDetailApproved> subResultObj = new ArrayList<>();
				sqlQry = new StringBuilder("SELECT usd FROM UsersDetailApproved usd where usd.autogenUsersId=:USERID");
				Query subQueryObj = firstEntityManager.createQuery(sqlQry.toString());
				subQueryObj.setParameter("USERID", user.getAutogenUsersId());
				subResultObj = subQueryObj.getResultList();
				for (UsersDetailApproved usersDetail : subResultObj) {
					if (userDto.getAutogenRolesId() != null && userDto.getRolesName() != null) {
						usersDetail.setAutogenRolesId(userDto.getAutogenRolesId());
						usersDetail.setRolesName(userDto.getRolesName());
					}

					if (userDto.getSupervisorUsersId() != null && userDto.getSupervisorUsersName() != null) {
						usersDetail.setSupervisorUsersId(userDto.getSupervisorUsersId());
						usersDetail.setSupervisorUsersName(userDto.getSupervisorUsersName());
					}

					if (userDto.getUpdatedBy() != null) {
						usersDetail.setUpdatedBy(userDto.getUpdatedBy());
					}
					firstEntityManager.merge(usersDetail);
					if (usersDetail.getAutogenUsersDetailsId() != null) {
						if (userDto.getDomain() != null) {
							Query qryObj = null;
							qryObj = firstEntityManager.createNativeQuery(
									"delete FROM user_rule.user_domain_map_approved  WHERE AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID and AUTOGEN_USERS_ID=:USERID ");
							qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
							qryObj.setParameter("USERID", userDto.getAutogenUsersId());
							Integer updateVal = qryObj.executeUpdate();
							// if (updateVal != null && updateVal != 0) {
							for (Map<String, String> buMap : userDto.getBusinessUnit()) {
								UserDomainMapApproved userDomainMap = new UserDomainMapApproved();
								userDomainMap.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
								userDomainMap.setAutogenUsersId(userDto.getAutogenUsersId());
								userDomainMap.setBusinessUnitId(new BigInteger(buMap.get("buId")));
								userDomainMap.setBusinessUnitName(buMap.get("buName"));
								userDomainMap.setDomainId(new BigInteger(userDto.getDomain().get(0).get("domainId")));
								userDomainMap.setDomainName(userDto.getDomain().get(0).get("domainName"));
								firstEntityManager.persist(userDomainMap);
							}
							// }
						}
						if (userDto.getRoleDetailList() != null) {
							Query qryObj = null;
							qryObj = firstEntityManager.createNativeQuery(
									"delete FROM user_rule.user_roles_map_approved  WHERE AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID and AUTOGEN_USERS_ID=:USERID ");
							qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
							qryObj.setParameter("USERID", userDto.getAutogenUsersId());
							qryObj.executeUpdate();
							for (Map<String, String> roleDet : userDto.getRoleDetailList()) {
								UserRoleMapApproved userRoleMap = new UserRoleMapApproved();
								userRoleMap.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
								userRoleMap.setAutogenUsersId(userDto.getAutogenUsersId());
								userRoleMap.setRoleId(new BigInteger(roleDet.get("roleId")));
								userRoleMap.setRoleName(roleDet.get("rolesName"));
								firstEntityManager.persist(userRoleMap);
							}
						}
					}
					if (usersDetail.getAutogenUsersDetailsId() != null) {
						if (userDto.getUserInventoryMapDtoList() != null) {
							Query qryObj = null;
							qryObj = firstEntityManager.createQuery(
									"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID");
							qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
							List<UserInventoryMap> userInventoryMapResult = qryObj.getResultList();

							if (userInventoryMapResult != null) {
								qryObj = firstEntityManager.createQuery(
										"UPDATE UserInventoryMap SET status=:STATUS WHERE autogenUsersDetailsId=:USERDETAILSID");
								qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
								qryObj.setParameter("STATUS", "INACTIVE");
								qryObj.executeUpdate();

								List<UserInventoryMapDto> removeUserInventoryMapDtoList = new ArrayList<>();
								List<UserInventoryMapDto> newUserInventoryMapDtoList = userDto
										.getUserInventoryMapDtoList();
								/** Old UserInventoryMap Status Update */
								for (UserInventoryMapDto userInventoryMapDto : newUserInventoryMapDtoList) {
									userInventoryMapResult.forEach(p -> {
										if (p.getInventoryRegionId() == userInventoryMapDto.getInventoryRegionId()
												&& p.getInventoryCenterId() == userInventoryMapDto
														.getInventoryCenterId()
												&& p.getInventoryClientId() == userInventoryMapDto
														.getInventoryClientId()
												&& p.getInventoryProcessId() == userInventoryMapDto
														.getInventoryProcessId()) {
											p.setStatus("ACTIVE");
											p.setUpdatedBy(userDto.getUpdatedBy());
											firstEntityManager.merge(p);
											removeUserInventoryMapDtoList.add(userInventoryMapDto);
										}

									});
								}
								newUserInventoryMapDtoList.removeAll(removeUserInventoryMapDtoList);
								for (UserInventoryMapDto userInventoryMapDto : newUserInventoryMapDtoList) {

									UserInventoryMap userInventoryMap = new UserInventoryMap();
									userInventoryMap.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
									userInventoryMap.setInventoryRegionId(userInventoryMapDto.getInventoryRegionId());
									userInventoryMap
											.setInventoryRegionName(userInventoryMapDto.getInventoryRegionName());
									userInventoryMap.setInventoryCenterId(userInventoryMapDto.getInventoryCenterId());
									userInventoryMap
											.setInventoryCenterName(userInventoryMapDto.getInventoryCenterName());
									userInventoryMap.setInventoryClientId(userInventoryMapDto.getInventoryClientId());
									userInventoryMap
											.setInventoryClientName(userInventoryMapDto.getInventoryClientName());
									userInventoryMap.setInventoryProcessId(userInventoryMapDto.getInventoryProcessId());
									userInventoryMap
											.setInventoryProcessName(userInventoryMapDto.getInventoryProcessName());
									userInventoryMap
											.setInventoryCategoryId(userInventoryMapDto.getInventoryCategoryId());
									userInventoryMap
											.setInventoryCategoryName(userInventoryMapDto.getInventoryCategoryName());
									userInventoryMap.setStatus("ACTIVE");
									userInventoryMap.setCreatedBy(userDto.getCreatedBy());
									firstEntityManager.persist(userInventoryMap);

								}
							}
						}

						if (userDto.getReports() != null) {
							Query qryObj = null;
							qryObj = firstEntityManager.createQuery(
									"SELECT i FROM UserReportMap i WHERE autogenUsersDetailsId=:USERDETAILSID");
							qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
							List<UserReportMap> userReportMapResult = qryObj.getResultList();

							if (userReportMapResult != null) {
								qryObj = firstEntityManager.createQuery(
										"UPDATE UserReportMap SET status=:STATUS WHERE autogenUsersDetailsId=:USERDETAILSID");
								qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
								qryObj.setParameter("STATUS", "INACTIVE");
								qryObj.executeUpdate();
								List<Reports> newReports = userDto.getReports();
								List<Reports> removeReports = new ArrayList<>();
								/** Old Reports Status Update */
								for (Reports reports : userDto.getReports()) {
									try {
										userReportMapResult.forEach(p -> {
											if (p.getAutogenReportMasterId() == reports.getId()) {
												p.setStatus("ACTIVE");
												p.setUpdatedBy(userDto.getUpdatedBy());
												firstEntityManager.merge(p);
												removeReports.add(reports);
											}
										});
									} catch (Exception e) {
										logger.info(
												"Exception :: UserDAOImpl :: UpdateUser() Nested Try Exception : " + e);
									}
								}
								newReports.removeAll(removeReports);
								/** New Reports Insert */
								for (Reports reports : newReports) {
									UserReportMap userReportMap = new UserReportMap();
									userReportMap.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
									userReportMap.setAutogenReportMasterId(reports.getId());
									userReportMap.setReportName(reports.getReportName());
									userReportMap.setStatus("ACTIVE");
									userReportMap.setCreatedBy(userDto.getCreatedBy());
									firstEntityManager.persist(userReportMap);
								}
							}
						}

						if (userDto.getUserLeaveDetailsDtoList() != null) {
							for (UserLeaveDetailsDto userLeaveDetailsDto : userDto.getUserLeaveDetailsDtoList()) {
								if (userLeaveDetailsDto.getLeaveDetailsId() != null
										&& !BigInteger.ZERO.equals(userLeaveDetailsDto.getLeaveDetailsId())
										&& ("ACTIVE".equalsIgnoreCase(userLeaveDetailsDto.getStatus())
												|| "CANCEL".equalsIgnoreCase(userLeaveDetailsDto.getStatus()))) {
									UserLeaveDetails userLeaveDetails = firstEntityManager.find(UserLeaveDetails.class,
											userLeaveDetailsDto.getLeaveDetailsId());
									if (userLeaveDetails != null) {
										userLeaveDetails.setToDate(new Date());
										userLeaveDetails.setFromDate(new Date());
										userLeaveDetails.setReasons(userLeaveDetailsDto.getReasons());
										userLeaveDetails.setComments(userLeaveDetailsDto.getComments());
										userLeaveDetails.setStatus(userLeaveDetailsDto.getStatus());
										userLeaveDetails.setUpdatedBy(userDto.getUpdatedBy());
										firstEntityManager.merge(userLeaveDetails);
									}

								} else {
									UserLeaveDetails userLeaveDetails = new UserLeaveDetails();
									userLeaveDetails.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
									if (userLeaveDetailsDto.getFromDate() != null
											&& !userLeaveDetailsDto.getFromDate().isEmpty()) {
										userLeaveDetails.setFromDate(
												DateUtil.convertStringtoDate(userLeaveDetailsDto.getFromDate(),
														DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
									}
									if (userLeaveDetailsDto.getToDate() != null
											&& !userLeaveDetailsDto.getToDate().isEmpty()) {
										userLeaveDetails
												.setToDate(DateUtil.convertStringtoDate(userLeaveDetailsDto.getToDate(),
														DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
									}
									userLeaveDetails.setNoOfDays(userLeaveDetailsDto.getNoOfDays());
									userLeaveDetails.setReasons(userLeaveDetailsDto.getReasons());
									userLeaveDetails.setComments(userLeaveDetailsDto.getComments());
									userLeaveDetails.setStatus(userLeaveDetailsDto.getStatus());
									userLeaveDetails.setCreatedBy(userDto.getUpdatedBy());
									firstEntityManager.persist(userLeaveDetails);
								}
							}
						}
						if (userDto.getSurveyTypes() != null) {
							sqlQry.setLength(0);
							sqlQry = new StringBuilder(
									"delete from user_rule.user_survey_mapping where autogen_user_id=:userId");
							Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
							queryObj.setParameter("userId", usersDetail.getAutogenUsersId());
							queryObj.executeUpdate();
							for (SurveyTypeDto surveyType : userDto.getSurveyTypes()) {
								UserSurveyMapping surveyMapping = new UserSurveyMapping();
								surveyMapping.setAutogenUserId(userDto.getAutogenUsersId());
								surveyMapping.setEmployeeId(user.getEmployeeId());
								surveyMapping.setSurveyId(surveyType.getId());
								surveyMapping.setSurveyName(surveyType.getLabel());
								surveyMapping.setStatus("ACTIVE");
								surveyMapping.setCreatedBy(userDto.getCreatedBy());
								firstEntityManager.persist(surveyMapping);
							}
						}
					}
				}
				result = true;
			}
		} catch (Exception e) {
			logger.error("Exception :: updateExistingUser() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return result;
	}

	@Override
	public AgentDto getAgentDetList(AgentDto agentDto) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> newAgentList = new ArrayList<>();

		try {
			String agentIds = "";
			if (agentDto.getResultObj() != null) {
				List<String> agentList = (List<String>) agentDto.getResultObj();
				for (String agentId : agentList) {
					agentIds = agentIds + "'" + CommonUtil.nullRemove(agentId.trim()) + "'";
					if (!(agentList.size() - 1 == agentList.indexOf(agentId))) {
						agentIds = agentIds + ", ";
					}
				}
			}
			List<Object[]> resultObjList = new ArrayList<>();
			sqlQry = new StringBuilder(
					"SELECT AM.AGENT_ID, AM.AGENT_NAME, AM.AUDITOR_ID,AM.AUDITOR_NAME FROM USERS US, USERS_DETAILS USD, AGENT_MAPPING AM WHERE AM.AGENT_ID=US.EMPLOYEE_ID AND US.AUTOGEN_USERS_ID=USD.AUTOGEN_USERS_ID AND US.STATUS IN ('ACTIVE','NEW') AND TRIM(US.EMPLOYEE_ID) IN("
							+ agentIds + ")");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			resultObjList = queryObj.getResultList();
			if (resultObjList != null && !resultObjList.isEmpty()) {
				for (Object[] agentObj : resultObjList) {
					Object[] objArray = new Object[2];
					objArray[0] = agentObj;
					sqlQry = new StringBuilder(
							"SELECT DISTINCT UIM.INVENTORY_CATEGORY_NAME FROM USER_INVENTORY_MAP UIM, USERS US, USERS_DETAILS USD WHERE UIM.AUTOGEN_USERS_DETAILS_ID=USD.AUTOGEN_USERS_DETAILS_ID AND US.AUTOGEN_USERS_ID=USD.AUTOGEN_USERS_ID AND US.STATUS IN ('ACTIVE','NEW') AND TRIM(US.EMPLOYEE_ID) =:AGENTID");
					queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
					queryObj.setParameter("AGENTID", CommonUtil.nullRemove(agentObj[0]));
					List<Object[]> agentCategoryObjList = queryObj.getResultList();
					objArray[1] = agentCategoryObjList;
					newAgentList.add(objArray);
				}
			}
			agentDto.setResultObjList(newAgentList);
		} catch (Exception e) {
			logger.error("Exception :: getAgentDetList() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return agentDto;
	}

	@Override
	public UserDto getSuperVisorUsersList() throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> users = null;
		UserDto userDto = null;
		try {
			sqlQry = new StringBuilder(
					"SELECT US.EMPLOYEE_ID, concat(US.FIRST_NAME ,' ',US.LAST_NAME) AS NAME FROM USERS US WHERE US.STATUS='ACTIVE'");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			users = queryObj.getResultList();
			if (users != null && !users.isEmpty()) {
				userDto = new UserDto();
				userDto.setResultObjList(users);
			}
		} catch (Exception e) {
			logger.error("Exception :: getSuperVisorUsersList() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userDto;
	}

	@Override
	public AgentDto getAgentList(AgentDto agentDto) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> mappedAgentList = null;
		String employeeIds = ApplicationConstant.EMPTY_STR;
		String regionIds = ApplicationConstant.EMPTY_STR;
		String centerIds = ApplicationConstant.EMPTY_STR;
		String clientIds = ApplicationConstant.EMPTY_STR;
		String processesIds = ApplicationConstant.EMPTY_STR;
		try {
			sqlQry = new StringBuilder(
					"SELECT DISTINCT AGENT_ID AS AGENTID, AGENT_NAME AS NAME, AUTOGEN_AGENT_MAPPING_ID AS AGENTMAPID, STATUS FROM AGENT_MAPPING WHERE STATUS='ACTIVE' AND AUDITOR_ID=:AUDITORID ORDER BY NAME");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("AUDITORID", agentDto.getAuditorId());
			mappedAgentList = queryObj.getResultList();

			if (mappedAgentList != null && !mappedAgentList.isEmpty()) {
				for (Object[] obj : mappedAgentList) {
					employeeIds = employeeIds + "'" + CommonUtil.nullRemove(obj[0]) + "'";
					if (!(mappedAgentList.size() - 1 == mappedAgentList.indexOf(obj))) {
						employeeIds = employeeIds + ", ";
					}
				}
			}

			if (agentDto.getRegions() != null) {
				regionIds = StringUtils.arrayToCommaDelimitedString(agentDto.getRegions().toArray());
			}
			if (agentDto.getCenters() != null) {
				centerIds = StringUtils.arrayToCommaDelimitedString(agentDto.getCenters().toArray());
			}
			if (agentDto.getClients() != null) {
				clientIds = StringUtils.arrayToCommaDelimitedString(agentDto.getClients().toArray());
			}
			if (agentDto.getProcesses() != null) {
				processesIds = StringUtils.arrayToCommaDelimitedString(agentDto.getProcesses().toArray());
			}

			queryObj = null;
			sqlQry = new StringBuilder(
					"SELECT DISTINCT U.EMPLOYEE_ID AS AGENTID, CONCAT(U.FIRST_NAME,' ',U.LAST_NAME) AS NAME, '' AS AGENTMAPID, '' AS STATUS  FROM USERS U, USERS_DETAILS UD, USER_INVENTORY_MAP UIM "
							+ " WHERE  UD.AUTOGEN_ROLES_ID=(SELECT AUTOGEN_ROLES_ID FROM ROLES WHERE ROLES_NAME=:ROLESNAME) AND UD.AUTOGEN_USERS_ID=U.AUTOGEN_USERS_ID "
							+ " AND UIM.INVENTORY_REGION_ID IN(:REGIONID) AND UIM.INVENTORY_CENTER_ID IN (:CENTERID) AND UIM.INVENTORY_CLIENT_ID IN (:CLIENTID) AND UIM.INVENTORY_PROCESS_ID IN(:PROCESSID)"
							+ " AND UD.AUTOGEN_USERS_DETAILS_ID=UIM.AUTOGEN_USERS_DETAILS_ID " + " AND "
							+ " U.EMPLOYEE_ID NOT IN (:EMPLOYEEIDS) OR NULL AND UIM.STATUS='ACTIVE' AND U.STATUS IN('ACTIVE','NEW')");
			queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("ROLESNAME", "Agent");
			queryObj.setParameter("REGIONID", regionIds);
			queryObj.setParameter("CENTERID", centerIds);
			queryObj.setParameter("CLIENTID", clientIds);
			queryObj.setParameter("PROCESSID", processesIds);
			queryObj.setParameter("EMPLOYEEIDS", employeeIds);
			List<Object[]> agentList = queryObj.getResultList();
			if (agentList != null && !agentList.isEmpty()) {
				mappedAgentList = new ArrayList<>();
				mappedAgentList.addAll(agentList);
			}
		} catch (Exception e) {
			logger.error("Exception :: getAgentList() : {} ", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		agentDto.setResultObjList(mappedAgentList);
		return agentDto;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public AgentDto agentMapping(AgentDto agentDto) throws Exception {
		StringBuilder sqlQry = null;
		AgentMapping agentMapping = null;
		Query qryObj = null;
		try {

			if (agentDto.getAgents() != null) {
				sqlQry = new StringBuilder("UPDATE AGENT_MAPPING SET STATUS='INACTIVE' WHERE AUDITOR_ID=:AUDITORID");
				qryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
				qryObj.setParameter("AUDITORID", agentDto.getAuditorId());
				qryObj.executeUpdate();

				for (Agent agent : agentDto.getAgents()) {
					agentMapping = new AgentMapping();

					sqlQry = new StringBuilder(
							"SELECT am FROM AgentMapping am WHERE am.auditorId=:AUDITORID AND am.agentId=:AGENTID");
					qryObj = null;
					qryObj = firstEntityManager.createQuery(sqlQry.toString());
					qryObj.setParameter("AUDITORID", agentDto.getAuditorId());
					qryObj.setParameter("AGENTID", agent.getAgentId());
					List<AgentMapping> result = qryObj.getResultList();
					if (result != null && !result.isEmpty()) {
						agentMapping = result.get(0);
						agentMapping.setStatus("ACTIVE");
						agentMapping.setUpdatedBy(agentDto.getUpdatedBy());
						firstEntityManager.merge(agentMapping);
					} else {
						agentMapping.setAgentId(agent.getAgentId());
						agentMapping.setAgentName(agent.getAgentName());
						agentMapping.setAuditorId(agentDto.getAuditorId());
						agentMapping.setAuditorName(agentDto.getAuditorName());
						agentMapping.setCreatedBy(agentDto.getCreatedBy());
						agentMapping.setStatus("ACTIVE");
						firstEntityManager.persist(agentMapping);
					}
					agentDto.setFlag(true);
				}
			}

		} catch (Exception e) {
			agentDto.setFlag(false);
			logger.error("Exception :: agentMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return agentDto;
	}

	@Override
	public UserDto getModuleScreenDet(List<String> roleList) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> screenList = new ArrayList<>();
		UserDto userDto = null;
		try {
			sqlQry = new StringBuilder(
					"SELECT u.MODULE_UID,u.MODULE_NAME, u.SCREEN_UID, u.SCREEN_NAME, u.ACCESS_PERMISSION FROM user_rule.USER_SCREEN_MAP_APPROVED u join user_rule.ROLES_APPROVED r on u.AUTOGEN_ROLE_ID = r.AUTOGEN_ROLES_ID and r.ROLES_NAME in (:ROLESNAME) WHERE u.STATUS='ACTIVE' GROUP BY MODULE_UID,MODULE_NAME,SCREEN_UID,SCREEN_NAME,ACCESS_PERMISSION");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("ROLESNAME", roleList);
			screenList = queryObj.getResultList();
			if (screenList != null && !screenList.isEmpty()) {
				userDto = new UserDto();
				userDto.setResultObjList(screenList);
			}
		} catch (Exception e) {
			logger.error("Exception :: getModuleScreenDet() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userDto;
	}

	@Override
	public AgentDto getRoleAndInventoryByUsersList(AgentDto agentDto, String roleName) throws Exception {
		StringBuilder sqlQry = null;
		Query queryObj = null;
		List<Object[]> usersMapList = null;
		String regionIds = ApplicationConstant.EMPTY_STR;
		String centerIds = ApplicationConstant.EMPTY_STR;
		String clientIds = ApplicationConstant.EMPTY_STR;
		String processesIds = ApplicationConstant.EMPTY_STR;
		try {

			if (agentDto.getRegions() != null) {
				regionIds = StringUtils.arrayToCommaDelimitedString(agentDto.getRegions().toArray());
			}
			if (agentDto.getCenters() != null) {
				centerIds = StringUtils.arrayToCommaDelimitedString(agentDto.getCenters().toArray());
			}
			if (agentDto.getClients() != null) {
				clientIds = StringUtils.arrayToCommaDelimitedString(agentDto.getClients().toArray());
			}
			if (agentDto.getProcesses() != null) {
				processesIds = StringUtils.arrayToCommaDelimitedString(agentDto.getProcesses().toArray());
			}

			queryObj = null;
			sqlQry = new StringBuilder(
					"SELECT DISTINCT U.EMPLOYEE_ID AS USERID, CONCAT(U.FIRST_NAME,' ',U.LAST_NAME) AS NAME FROM USERS U, USERS_DETAILS UD, USER_INVENTORY_MAP UIM "
							+ " WHERE  UD.AUTOGEN_ROLES_ID=(SELECT AUTOGEN_ROLES_ID FROM ROLES WHERE ROLES_NAME=:ROLESNAME) AND UD.AUTOGEN_USERS_ID=U.AUTOGEN_USERS_ID "
							+ " AND UIM.INVENTORY_REGION_ID IN(:REGIONID) AND UIM.INVENTORY_CENTER_ID IN (:CENTERID) AND UIM.INVENTORY_CLIENT_ID IN (:CLIENTID) AND UIM.INVENTORY_PROCESS_ID IN(:PROCESSID)"
							+ " AND UD.AUTOGEN_USERS_DETAILS_ID=UIM.AUTOGEN_USERS_DETAILS_ID AND "
							+ " UIM.STATUS='ACTIVE' AND U.STATUS IN('ACTIVE','NEW')");
			queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("ROLESNAME", roleName);
			queryObj.setParameter("REGIONID", regionIds);
			queryObj.setParameter("CENTERID", centerIds);
			queryObj.setParameter("CLIENTID", clientIds);
			queryObj.setParameter("PROCESSID", processesIds);
			List<Object[]> usersist = queryObj.getResultList();
			if (usersist != null && !usersist.isEmpty()) {
				usersMapList = new ArrayList<>();
				usersMapList.addAll(usersist);
			}
		} catch (Exception e) {
			logger.error("Exception :: getRoleAndInventoryByUsersList() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		agentDto.setResultObjList(usersMapList);
		return agentDto;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean resetPassword(UserDto userDto) throws Exception {
		List<Users> usersList = null;
		boolean result = false;
		try {
			StringBuilder sqlQry = new StringBuilder("SELECT u FROM Users u WHERE u.employeeId=:EMPLOYEEID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("EMPLOYEEID", CommonUtil.nullRemove(userDto.getEmployeeId()));
			usersList = queryObj.getResultList();
			if (usersList != null && !usersList.isEmpty()) {
				Users users = usersList.get(0);
				if (!CommonUtil.nullRemove(userDto.getStatus()).isEmpty()) {
					users.setStatus(userDto.getStatus());
				}
				if (!CommonUtil.nullRemove(userDto.getEmployeeId()).isEmpty()) {
					users.setUpdatedBy(userDto.getEmployeeId());
				}
				if (!CommonUtil.nullRemove(userDto.getPassword()).isEmpty()) {
					users.setPassword(userDto.getPassword());
				}
				firstEntityManager.merge(users);
				result = true;
			}
		} catch (Exception e) {
			logger.error("Exception :: resetPassword() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return result;
	}

	
	
	
	
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {

		StringBuilder sqlQry = null;
		List<Object[]> tokenDet = new ArrayList<>();
		boolean result = false;
		try {
			sqlQry = new StringBuilder(
					"select max(autogen_token_details_id) from appointment_remainder.token_details where EMPLOYEE_ID=:EMPLOYEEID and token=:TOKEN and STATUS='ACTIVE';");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("EMPLOYEEID", tokenDetailsDto.getEmployeeId());
			queryObj.setParameter("TOKEN", tokenDetailsDto.getToken());
			tokenDet = queryObj.getResultList();
			if (tokenDet != null && !tokenDet.isEmpty() && tokenDet.get(0) != null) {
				result = true;
			}
		} catch (Exception e) {
			logger.error("Exception :: checkExistingTokenDetails() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return result;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		try {
			tokenDetailsDto.setFlag(false);
			if (tokenDetailsDto != null) {
				TokenDetails tokenDetails = new TokenDetails();
				BeanUtils.copyProperties(tokenDetailsDto, tokenDetails);
				firstEntityManager.persist(tokenDetails);
				tokenDetailsDto.setFlag(true);
			}
		} catch (Exception e) {
			logger.error("Exception :: saveTokenDetails() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return tokenDetailsDto;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception {
		List<TokenDetails> tokenList = null;
		try {
			StringBuilder sqlQry = new StringBuilder(
					"SELECT u FROM TokenDetails u WHERE u.employeeId=:EMPLOYEEID and token=:TOKEN");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("EMPLOYEEID", tokenDetailsDto.getEmployeeId());
			queryObj.setParameter("TOKEN", tokenDetailsDto.getToken());
			tokenList = queryObj.getResultList();
			if (tokenList != null && !tokenList.isEmpty()) {
				TokenDetails tokenDetails = tokenList.get(0);
				tokenDetails.setStatus("COMPLETE");
				tokenDetails.setUpdatedBy(tokenDetailsDto.getEmployeeId());
				firstEntityManager.persist(tokenDetails);
				tokenDetailsDto.setFlag(true);
			}
		} catch (Exception e) {
			logger.error("Exception :: updateTokenDetails() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return tokenDetailsDto;
	}

	@Override
	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SurveyTypeDto> getUserSurveyTypeList(final String userId) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> result = new ArrayList<>();
		List<SurveyTypeDto> surveyList = new ArrayList<>();
		try {
			sqlQry = new StringBuilder(
					"SELECT survey_id,survey_name from user_survey_mapping where employee_id=:userId");
			Query query = firstEntityManager.createNativeQuery(sqlQry.toString());
			query.setParameter("userId", userId);
			result = query.getResultList();
			if (!result.isEmpty()) {
				for (Object[] surveyObj : result) {
					SurveyTypeDto surveyTypeDto = new SurveyTypeDto();
					surveyTypeDto.setId((int) surveyObj[0]);
					surveyTypeDto.setLabel((String) surveyObj[1]);
					surveyList.add(surveyTypeDto);
				}
			}
		} catch (Exception e) {
			logger.error("Exception :: getUserSurveyTypeList() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return surveyList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Map<String, String>>> getMakerCheckerDetails(Map<String, String> inputRequest)
			throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> makerResultList = new ArrayList<>();
		List<Object[]> checkerResultList = new ArrayList<>();
		List<Map<String, String>> makerList = new ArrayList<>();
		List<Map<String, String>> checkerList = new ArrayList<>();
		Map<String, List<Map<String, String>>> resultMap = new LinkedHashMap<>();
		try {
			sqlQry = new StringBuilder(
					"select employee_id,first_name from user_rule.users us join user_rule.user_domain_map usd on us.autogen_users_id=usd.AUTOGEN_USERS_ID join user_rule.user_roles_map ur on  usd.AUTOGEN_USERS_DETAILS_ID=ur.AUTOGEN_USERS_DETAILS_ID where ur.ROLES_NAME='Maker' ");
			if (inputRequest != null && !inputRequest.isEmpty())
				sqlQry.append(" and usd.DOMAIN_ID=:domainId and usd.BUSINESS_UNIT_ID=:buId");

			Query query = firstEntityManager.createNativeQuery(sqlQry.toString());
			if (inputRequest != null && !inputRequest.isEmpty()) {
				query.setParameter("domainId", inputRequest.get("domainId"));
				query.setParameter("buId", inputRequest.get("buId"));
			}
			makerResultList = query.getResultList();
			if (!makerResultList.isEmpty()) {
				for (Object[] tempObj : makerResultList) {
					Map<String, String> makerDet = new LinkedHashMap<String, String>();
					makerDet.put("id", String.valueOf(tempObj[0]));
					makerDet.put("name", String.valueOf(tempObj[1]));
					makerList.add(makerDet);
				}
			}
			sqlQry = new StringBuilder(
					"select employee_id,first_name from user_rule.users us join user_rule.user_domain_map usd on us.autogen_users_id=usd.AUTOGEN_USERS_ID join user_rule.user_roles_map ur on  usd.AUTOGEN_USERS_DETAILS_ID=ur.AUTOGEN_USERS_DETAILS_ID where ur.ROLES_NAME='Checker'");
			if (inputRequest != null && !inputRequest.isEmpty())
				sqlQry.append(" and usd.DOMAIN_ID=:domainId and usd.BUSINESS_UNIT_ID=:buId");
			query = firstEntityManager.createNativeQuery(sqlQry.toString());
			if (inputRequest != null && !inputRequest.isEmpty()) {
				query.setParameter("domainId", inputRequest.get("domainId"));
				query.setParameter("buId", inputRequest.get("buId"));
			}
			checkerResultList = query.getResultList();
			if (!checkerResultList.isEmpty()) {
				for (Object[] tempObj : checkerResultList) {
					Map<String, String> checkerDet = new LinkedHashMap<String, String>();
					checkerDet.put("id", String.valueOf(tempObj[0]));
					checkerDet.put("name", String.valueOf(tempObj[1]));
					checkerList.add(checkerDet);
				}
			}
			resultMap.put("checkerDet", checkerList);
			resultMap.put("makerDet", makerList);
			return resultMap;
		} catch (Exception e) {
			logger.error("Exception :: getMakerCheckerDetails() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserDto getApprovedDomainDetails(String userId, String userDetId) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> resultList = new ArrayList<>();
		List<Map<String, String>> domain = null;
		List<Map<String, String>> businessUnit = null;
		Map<String, String> domainMap = new LinkedHashMap();
		UserDto userDto;
		try {
			domain = new ArrayList<>();
			businessUnit = new ArrayList<>();
			sqlQry = new StringBuilder(
					"select DOMAIN_ID,DOMAIN_NAME,BUSINESS_UNIT_ID,BUSINESS_UNIT_NAME from user_rule.user_domain_map_approved where AUTOGEN_USERS_DETAILS_ID=:uerDetId and AUTOGEN_USERS_ID=:userId");
			Query query = firstEntityManager.createNativeQuery(sqlQry.toString());
			query.setParameter("uerDetId", userDetId);
			query.setParameter("userId", userId);
			resultList = query.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					if (domainMap.isEmpty()) {
						domainMap.put("domainId", obj[0].toString());
						domainMap.put("domainName", obj[1].toString());
					}
					Map<String, String> buMap = new LinkedHashMap<>();
					buMap.put("buId", obj[2].toString());
					buMap.put("buName", obj[3].toString());
					businessUnit.add(buMap);
				}
			}
			userDto = new UserDto();
			domain.add(domainMap);
			userDto.setDomain(domain);
			userDto.setBusinessUnit(businessUnit);
			return userDto;
		} catch (Exception e) {
			logger.error("Exception :: getApprovedDomainDetails() : {}", e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserDto getDomainDetails(String userId, String userDetId) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> resultList = new ArrayList<>();
		List<Map<String, String>> domain = null;
		List<Map<String, String>> businessUnit = null;
		Map<String, String> domainMap = new LinkedHashMap();
		UserDto userDto;
		try {
			domain = new ArrayList<>();
			businessUnit = new ArrayList<>();
			sqlQry = new StringBuilder(
					"select DOMAIN_ID,DOMAIN_NAME,BUSINESS_UNIT_ID,BUSINESS_UNIT_NAME from user_rule.user_domain_map where AUTOGEN_USERS_DETAILS_ID=:uerDetId and AUTOGEN_USERS_ID=:userId");
			Query query = firstEntityManager.createNativeQuery(sqlQry.toString());
			query.setParameter("uerDetId", userDetId);
			query.setParameter("userId", userId);
			resultList = query.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					if (domainMap.isEmpty()) {
						domainMap.put("domainId", obj[0].toString());
						domainMap.put("domainName", obj[1].toString());
					}
					Map<String, String> buMap = new LinkedHashMap<>();
					buMap.put("buId", obj[2].toString());
					buMap.put("buName", obj[3].toString());
					businessUnit.add(buMap);
				}
			}
			userDto = new UserDto();
			domain.add(domainMap);
			userDto.setDomain(domain);
			userDto.setBusinessUnit(businessUnit);
			return userDto;
		} catch (Exception e) {
			logger.error("Exception :: getDomainDetails() : {}", e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getApprovedRoleDetails(String userId, String userDetId) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> resultList = new ArrayList<>();
		List<Map<String, String>> roleDetails = null;
		Map<String, String> roleMap = null;
		try {
			roleDetails = new ArrayList<>();
			sqlQry = new StringBuilder(
					"select rma.AUTOGEN_ROLES_ID,ra.ROLES_NAME	from user_rule.user_roles_map_approved rma, user_rule.roles_approved ra where ra.AUTOGEN_ROLES_ID=rma.AUTOGEN_ROLES_ID and rma.AUTOGEN_USERS_DETAILS_ID =:userDetailId and rma.AUTOGEN_USERS_ID=:userId");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("userDetailId", userDetId);
			queryObj.setParameter("userId", userId);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					roleMap = new LinkedHashMap<>();
					roleMap.put("roleId", obj[0].toString());
					roleMap.put("rolesName", obj[1].toString());
					roleDetails.add(roleMap);
				}
			}
			return roleDetails;
		} catch (Exception e) {
			logger.error("Exception :: getApprovedRoleDetails() : {}", e.getMessage());
			return null;
		}
	}
//	private String getApprovedGroupDetails(String userId) {
//		StringBuilder sqlQry = null;
//		List<Object[]> resultList = new ArrayList<>();
////		List<Map<String, String>> roleDetails = null;
////		Map<String, String> roleMap = null;
//		String groupName = "";
//		try {
////			roleDetails = new ArrayList<>();
//			logger.info("In Rolename getapprovel userid "+userId);
//			sqlQry = new StringBuilder(
//					"SELECT GROUP_NAME FROM user_rule.user_groups WHERE GROUP_ID =:group_id");
//			logger.info("In Rolename getapprovel userId "+userId);
//			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
//			queryObj.setParameter("group_id", userId);
//			resultList = queryObj.getResultList();
//			logger.info("Group name : "+resultList);
//			if (resultList != null && !resultList.isEmpty()) {
//				for (Object[] obj : resultList) {
//					resultList.get("groupName", obj[0].toString());
//
//				}
//			}
//			return groupName;
//		} catch (Exception e) {
//			logger.error("Exception :: getApprovedRoleDetails() : {}", e.getMessage());
//			return null;
//		}
//	}
	private String getApprovedGroupDetails(String group_id) {
		StringBuilder sqlQry = null;
		List resultList = new ArrayList<>();
		List<String> groupNames = new ArrayList<>();
		try {
			logger.info("In Rolename getapprovel userid " + group_id);
			sqlQry = new StringBuilder(
					"SELECT t2.GROUP_NAME\n" +
							"FROM user_rule.user_groups_map t1\n" +
							"JOIN user_rule.user_groups t2 ON t1.Autogen_user_groups_Id = t2.Autogen_user_groups_Id\n" +
							"WHERE t1.Autogen_users_Id =:group_id");
			logger.info("In Rolename getapprovel userId " + group_id);
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("group_id", group_id);
			resultList = queryObj.getResultList();
			logger.info("Group name : " + resultList);
			if (resultList != null && !resultList.isEmpty()) {
				for (Object obj : resultList) {
					groupNames.add((String) obj);
				}
			}
			return String.join(", ", groupNames);
		} catch (Exception e) {
			logger.error("Exception :: getApprovedRoleDetails() : {}", e.getMessage());
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getRoleDetails(String userId, String userDetId) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> resultList = new ArrayList<>();
		List<Map<String, String>> roleDetails = null;
		Map<String, String> roleMap = null;
		try {
			roleDetails = new ArrayList<>();
			sqlQry = new StringBuilder(
					"select urm.AUTOGEN_ROLES_ID,ra.ROLES_NAME from user_rule.user_roles_map urm, user_rule.roles_approved ra where ra.AUTOGEN_ROLES_ID= urm.AUTOGEN_ROLES_ID and urm.AUTOGEN_USERS_DETAILS_ID =:userDetailId and urm.AUTOGEN_USERS_ID=:userId");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("userDetailId", userDetId);
			queryObj.setParameter("userId", userId);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					roleMap = new LinkedHashMap<>();
					roleMap.put("roleId", obj[0].toString());
					roleMap.put("rolesName", obj[1].toString());
					roleDetails.add(roleMap);
				}
			}
			return roleDetails;
		} catch (Exception e) {
			logger.error("Exception :: getRoleDetails() : {} ", e.getMessage());
			return null;
		}
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserDto saveNewUser(UserDto userDto) throws Exception {
		try {
			if (userDto != null) {
				UsersApproved users = new UsersApproved();
				BeanUtils.copyProperties(userDto, users);
				users.setStatus("ACTIVE");
				users.setAutogenUsersId(null);
				firstEntityManager.persist(users);
				if (users.getAutogenUsersId() != null) {
					UsersDetailApproved usersDet = new UsersDetailApproved();
					BeanUtils.copyProperties(userDto, usersDet);
					usersDet.setAutogenUsersId(users.getAutogenUsersId());
					usersDet.setAutogenUsersDetailsId(null);
					firstEntityManager.persist(usersDet);
					if (usersDet.getAutogenUsersDetailsId() != null) {
						if (userDto.getDomain() != null) {
							for (Map<String, String> buDet : userDto.getBusinessUnit()) {
								UserDomainMapApproved userDomainMap = new UserDomainMapApproved();
								userDomainMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userDomainMap.setAutogenUsersId(users.getAutogenUsersId());
								userDomainMap.setBusinessUnitId(new BigInteger(buDet.get("buId")));
								userDomainMap.setBusinessUnitName(buDet.get("buName"));
								userDomainMap.setDomainId(new BigInteger(userDto.getDomain().get(0).get("domainId")));
								userDomainMap.setDomainName(userDto.getDomain().get(0).get("domainName"));
								userDomainMap.setAutogenUserDomainMapId(null);
								firstEntityManager.persist(userDomainMap);
							}
						}
						if (userDto.getRoleDetailList() != null) {
							for (Map<String, String> roleDet : userDto.getRoleDetailList()) {
								UserRoleMapApproved userRoleMap = new UserRoleMapApproved();
								userRoleMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userRoleMap.setAutogenUsersId(users.getAutogenUsersId());
								userRoleMap.setRoleId(new BigInteger(roleDet.get("roleId")));
								userRoleMap.setRoleName(roleDet.get("rolesName"));
								userRoleMap.setAutogenUserDomainMapId(null);
								firstEntityManager.persist(userRoleMap);
							}
						}
					}
					if (usersDet.getAutogenUsersDetailsId() != null) {
						if (userDto.getUserInventoryMapDtoList() != null) {
							for (UserInventoryMapDto userInventoryMapDto : userDto.getUserInventoryMapDtoList()) {
								UserInventoryMap userInventoryMap = new UserInventoryMap();
								userInventoryMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userInventoryMap.setInventoryRegionId(userInventoryMapDto.getInventoryRegionId());
								userInventoryMap.setInventoryRegionName(userInventoryMapDto.getInventoryRegionName());
								userInventoryMap.setInventoryCenterId(userInventoryMapDto.getInventoryCenterId());
								userInventoryMap.setInventoryCenterName(userInventoryMapDto.getInventoryCenterName());
								userInventoryMap.setInventoryClientId(userInventoryMapDto.getInventoryClientId());
								userInventoryMap.setInventoryClientName(userInventoryMapDto.getInventoryClientName());
								userInventoryMap.setInventoryProcessId(userInventoryMapDto.getInventoryProcessId());
								userInventoryMap.setInventoryProcessName(userInventoryMapDto.getInventoryProcessName());
								userInventoryMap.setInventoryCategoryId(userInventoryMapDto.getInventoryCategoryId());
								userInventoryMap
										.setInventoryCategoryName(userInventoryMapDto.getInventoryCategoryName());
								userInventoryMap.setStatus("ACTIVE");
								userInventoryMap.setCreatedBy(userDto.getCreatedBy());
								firstEntityManager.persist(userInventoryMap);
							}
						}
						if (userDto.getSurveyTypes() != null) {
							for (SurveyTypeDto surveyTypeDto : userDto.getSurveyTypes()) {
								UserSurveyMapping surveyMapping = new UserSurveyMapping();
								surveyMapping.setAutogenUserId(usersDet.getAutogenUsersId());
								surveyMapping.setEmployeeId(users.getEmployeeId());
								surveyMapping.setSurveyId(surveyTypeDto.getId());
								surveyMapping.setSurveyName(surveyTypeDto.getLabel());
								surveyMapping.setCreatedBy(userDto.getCreatedBy());
								surveyMapping.setUpdatedBy(userDto.getCreatedBy());
								surveyMapping.setStatus("ACTIVE");
								firstEntityManager.persist(surveyMapping);
							}
						}

						if (userDto.getReports() != null) {
							UserReportMap userReportMap = null;
							for (Reports report : userDto.getReports()) {
								userReportMap = new UserReportMap();
								userReportMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userReportMap.setAutogenReportMasterId(report.getId());
								userReportMap.setReportName(report.getReportName());
								userReportMap.setCreatedBy(userDto.getCreatedBy());
								userReportMap.setStatus("ACTIVE");
								firstEntityManager.persist(userReportMap);
							}
						}

						if (userDto.getUserLeaveDetailsDtoList() != null) {
							for (UserLeaveDetailsDto userLeaveDetailsDto : userDto.getUserLeaveDetailsDtoList()) {
								UserLeaveDetails userLeaveDetails = new UserLeaveDetails();
								userLeaveDetails.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userLeaveDetails.setToDate(new Date());
								userLeaveDetails.setFromDate(new Date());
								userLeaveDetails.setNoOfDays(userLeaveDetailsDto.getNoOfDays());
								userLeaveDetails.setReasons(userLeaveDetailsDto.getReasons());
								userLeaveDetails.setComments(userLeaveDetailsDto.getComments());
								userLeaveDetails.setStatus("ACTIVE");
								userLeaveDetails.setCreatedBy(userDto.getCreatedBy());
								firstEntityManager.persist(userLeaveDetails);
							}
						}

					}
					BeanUtils.copyProperties(usersDet, userDto);
				}
				BeanUtils.copyProperties(users, userDto);
			}
		} catch (Exception e) {
			logger.error("Exception :: saveNewUser() : {} ", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userDto;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteUnapprovedUsers(BigInteger unapprovedUserId) throws Exception {
		StringBuilder sqlQry = null;
		int delVal = 0;
		try {
			Query query;
			sqlQry = new StringBuilder("delete from user_rule.user_domain_map where AUTOGEN_USERS_ID=:userId");
			query = firstEntityManager.createNativeQuery(sqlQry.toString());
			query.setParameter("userId", unapprovedUserId);
			delVal = query.executeUpdate();

			sqlQry = new StringBuilder("delete from user_rule.user_roles_map where AUTOGEN_USERS_ID=:userId");
			query = firstEntityManager.createNativeQuery(sqlQry.toString());
			query.setParameter("userId", unapprovedUserId);
			delVal = query.executeUpdate();

			sqlQry = new StringBuilder("delete from user_rule.users_details where AUTOGEN_USERS_ID=:userId");
			query = firstEntityManager.createNativeQuery(sqlQry.toString());
			query.setParameter("userId", unapprovedUserId);
			delVal = query.executeUpdate();

			sqlQry = new StringBuilder("delete from user_rule.users where AUTOGEN_USERS_ID=:userId");
			query = firstEntityManager.createNativeQuery(sqlQry.toString());
			query.setParameter("userId", unapprovedUserId);
			delVal = query.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error("Exception :: deleteUnapprovedUsers() : {}", e.getMessage());
			return false;
		}
	}

	@Override
	public String getPassword(BigInteger userId) throws Exception {
		StringBuilder sqlQry = null;
		String password;
		try {
			sqlQry = new StringBuilder("select password from user_rule.users where AUTOGEN_USERS_ID=:userId");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("userId", userId);
			password = (String) queryObj.getSingleResult();
			return password;
		} catch (Exception e) {
			logger.error("Exception :: getPassword() : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public String getAppUserPassword(BigInteger userId) throws Exception {
		StringBuilder sqlQry = null;
		String password;
		try {
			sqlQry = new StringBuilder("select password from user_rule.users_approved where AUTOGEN_USERS_ID=:userId");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("userId", userId);
			password = (String) queryObj.getSingleResult();
			return password;
		} catch (Exception e) {
			logger.error("Exception :: getAppUserPassword() : {}", e.getMessage());
			return null;
		}
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer updateUserEditFlag(UserDto userDto) {
		Integer returnVal = 0;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(
					"update user_rule.users_approved set edit_flag=:editFlag where AUTOGEN_USERS_ID=:userId");
			queryObj.setParameter("editFlag", userDto.isEditFlag());
			queryObj.setParameter("userId", userDto.getApprovedUserId());
			returnVal = queryObj.executeUpdate();

		} catch (Exception e) {
			logger.error("Exception :: updateUserEditFlag() : {}", e.getMessage());
			throw e;
		} finally {
			firstEntityManager.close();
		}
		return returnVal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean validateApproveUser(BigInteger userId, String approvedBy) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(
					"select CREATED_BY,EMPLOYEE_ID from user_rule.users where AUTOGEN_USERS_ID=:userId and"
							+ " ( UPDATED_BY=:approveUser ) ");
			queryObj.setParameter("userId", userId);
			queryObj.setParameter("approveUser", approvedBy);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty())
				return false;
			else
				return true;
		} catch (Exception e) {
			logger.error("Exception :: validateApproveUser() : {}", e.getMessage());
			return false;
		} finally {
			firstEntityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean validateApproveRole(Long roleMapId, String approvedBy) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createQuery(
					"SELECT r FROM Roles r WHERE r.autogenRolesId=:roleMapId and ( r.updatedBy=:approveUser )");
			queryObj.setParameter("roleMapId", roleMapId);
			queryObj.setParameter("approveUser", approvedBy);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty())
				return false;
			else
				return true;
		} catch (Exception e) {
			logger.error("Exception :: validateApproveRole() : {}", e.getMessage());
			return false;
		} finally {
			firstEntityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleResponse> getRoles() throws Exception {
		StringBuilder sqlQry = null;
		List<RolesApproved> roles = null;
		List<RoleResponse> roleMapping = new ArrayList<>();
		try {
			sqlQry = new StringBuilder(
					"SELECT r FROM RolesApproved r WHERE r.status='ACTIVE' and roleCreateStatus in ('Approved')");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			roles = queryObj.getResultList();
			if (roles != null && !roles.isEmpty()) {
				for (RolesApproved obj : roles) {
					RoleResponse roleResponse = new RoleResponse();
					roleResponse.setRoleId(obj.getAutogenRolesId());
					BeanUtils.copyProperties(obj, roleResponse);
					roleMapping.add(roleResponse);
				}
			}
		} catch (Exception e) {
			logger.error("Exception :: getRoles() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return roleMapping;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleRequest> getUnapprovedRole(String module) throws Exception {
		StringBuilder sqlQry = null;
		List<Roles> roles = null;
		List<RoleRequest> roleMapping = new ArrayList<>();
		try {

			if (module != null && "unapproved".equalsIgnoreCase(module)) {
				sqlQry = new StringBuilder("SELECT r FROM Roles r WHERE roleCreateStatus in ('New','Exist')");
			} else if (module != null && "checker".equalsIgnoreCase(module)) {
				sqlQry = new StringBuilder(
						"SELECT r FROM Roles r WHERE roleCreateStatus in ('New_Approve','Exist_Approve')");
				if (!userInfo.getRolesName().contains(ApplicationConstant.SUPER_ADMIN_ROLE)
						&& null != userInfo.getEmployeeId()) {
					sqlQry = sqlQry.append("  and (updatedBy!='" + userInfo.getEmployeeId() + "')");
				}
			}
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			roles = queryObj.getResultList();
			if (roles != null && !roles.isEmpty()) {
				for (Roles obj : roles) {
					RoleRequest roleRequest = new RoleRequest();
					BeanUtils.copyProperties(obj, roleRequest);
					roleMapping.add(roleRequest);
				}
			}
		} catch (Exception e) {
			logger.error("Exception :: getUnapprovedRole() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return roleMapping;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleRequest> getApprovedRole() throws Exception {
		StringBuilder sqlQry = null;
		List<RolesApproved> roles = null;
		List<RoleRequest> roleMapping = new ArrayList<>();
		try {
			sqlQry = new StringBuilder("SELECT r FROM RolesApproved r WHERE roleCreateStatus in ('Approved')");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			roles = queryObj.getResultList();
			if (roles != null && !roles.isEmpty()) {
				for (RolesApproved obj : roles) {
					RoleRequest roleRequest = new RoleRequest();
					BeanUtils.copyProperties(obj, roleRequest);
					roleMapping.add(roleRequest);
				}
			}
		} catch (Exception e) {
			logger.error("Exception :: getApprovedRole() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return roleMapping;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ModuleScreenMapResponse> getModuleScreenMap() throws Exception {
		StringBuilder sqlQry = null;
		List<ModuleScreenMap> moduleScreenList = null;
		List<ModuleScreenMapResponse> moduleScreenMapList = new ArrayList<>();
		try {
			sqlQry = new StringBuilder(
					"SELECT r FROM ModuleScreenMap r WHERE r.status='ACTIVE' order by r.moduleScreenMapId");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			moduleScreenList = queryObj.getResultList();
			if (moduleScreenList != null && !moduleScreenList.isEmpty()) {
				for (ModuleScreenMap obj : moduleScreenList) {
					ModuleScreenMapResponse moduleScreenResponse = new ModuleScreenMapResponse();
					BeanUtils.copyProperties(obj, moduleScreenResponse);
					moduleScreenMapList.add(moduleScreenResponse);
				}
			}
		} catch (Exception e) {
			logger.error("Exception :: getModuleScreenMap() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return moduleScreenMapList;
	}

	@Override
	@Transactional
	public Roles createRoleMapping(Roles role) throws Exception {
		try {
			if (role != null) {
				firstEntityManager.persist(role);
			}
		} catch (Exception e) {
			if (e instanceof EntityExistsException) {
				throw e;
			}
			logger.error("Exception :: createRoleMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserScreenMap getUserScreenMap(Long screenMapId) throws Exception {
		StringBuilder sqlQry = null;
		UserScreenMap userScreenMap = null;
		try {
			sqlQry = new StringBuilder("SELECT r FROM UserScreenMap r WHERE r.roleScreenMapId=:id");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("id", screenMapId);
			userScreenMap = (UserScreenMap) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception :: getUserScreenMap() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userScreenMap;
	}

	@Override
	public UserScreenMapApproved getUserScreenMapApp(Long screenMapId) throws Exception {
		StringBuilder sqlQry = null;
		UserScreenMapApproved userScreenMap = null;
		try {
			sqlQry = new StringBuilder("SELECT r FROM UserScreenMapApproved r WHERE r.roleScreenMapId=:id");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("id", screenMapId);
			userScreenMap = (UserScreenMapApproved) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception :: getUserScreenMapApp() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userScreenMap;
	}

	@Override
	@Transactional
	public Roles updateRoleMapping(Roles role) throws Exception {
		try {
			if (role != null) {
				firstEntityManager.merge(role);
				firstEntityManager.flush();
			}
		} catch (Exception e) {
			logger.error("Exception :: updateRoleMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return role;
	}

	@Override
	@Transactional
	public UserScreenMap createRoleScreenMapping(UserScreenMap userScreenMap) throws Exception {
		try {
			if (userScreenMap != null) {
				firstEntityManager.persist(userScreenMap);
			}
		} catch (Exception e) {
			logger.error("Exception :: createRoleScreenMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userScreenMap;
	}

	@Override
	@Transactional
	public UserScreenMapApproved createAppRoleScreenMapping(UserScreenMapApproved userScreenMap) throws Exception {
		try {
			if (userScreenMap != null) {
				firstEntityManager.persist(userScreenMap);
			}
		} catch (Exception e) {
			logger.error("Exception :: createAppRoleScreenMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userScreenMap;
	}

	@Override
	@Transactional
	public UserScreenMap updateRoleScreenMapping(UserScreenMap userScreenMap) throws Exception {
		try {
			if (userScreenMap != null) {
				firstEntityManager.merge(userScreenMap);
				firstEntityManager.flush();
			}
		} catch (Exception e) {
			logger.error("Exception :: updateRoleScreenMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userScreenMap;
	}

	@Override
	@Transactional
	public UserScreenMapApproved updateAppRoleScreenMapping(UserScreenMapApproved userScreenMap) throws Exception {
		try {
			if (userScreenMap != null) {
				firstEntityManager.merge(userScreenMap);
			}
		} catch (Exception e) {
			logger.error("Exception :: updateAppRoleScreenMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userScreenMap;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer updateRoleEditFlag(Long roleId, boolean flag) {
		Integer returnVal = 0;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(
					"update [user_rule].[roles_approved] set edit_flag=:editFlag where AUTOGEN_ROLES_ID=:roleId");
			queryObj.setParameter("editFlag", flag);
			queryObj.setParameter("roleId", roleId);
			returnVal = queryObj.executeUpdate();

		} catch (Exception e) {
			logger.error("Exception :: updateRoleEditFlag() : {}", e.getMessage());
			throw e;
		} finally {
			firstEntityManager.close();
		}
		return returnVal;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer updateUnappRoleStatus(Long roleId, String status) {
		Integer returnVal = 0;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(
					"update [user_rule].[roles] set ROLE_CREATE_STATUS=:status, REC_UPDATE_DT = :updatedOn where AUTOGEN_ROLES_ID=:roleId");
			queryObj.setParameter("status", status);
			queryObj.setParameter("roleId", roleId);
			queryObj.setParameter("updatedOn", new Timestamp(System.currentTimeMillis()));
			returnVal = queryObj.executeUpdate();

		} catch (Exception e) {
			logger.error("Exception :: updateUnappRoleStatus() : {} ", e.getMessage());
			throw e;
		} finally {
			firstEntityManager.close();
		}
		return returnVal;
	}

	@Override
	@Transactional
	public void deleteRoleScreenMap(List<Long> roleScreenMapIds) {
		try {
			StringBuilder sqlQry = null;
			sqlQry = new StringBuilder(
					"delete FROM user_rule.user_screen_map  WHERE AUTOGEN_USER_SCREEN_MAP_ID in (:ids)");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("ids", roleScreenMapIds);
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception :: deleteRoleScreenMap() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
	}

	@Override
	@Transactional
	public void deleteRoleScreenMapApp(List<Long> roleScreenMapIds) {
		try {
			StringBuilder sqlQry = null;
			sqlQry = new StringBuilder(
					"delete FROM user_rule.user_screen_map_approved  WHERE AUTOGEN_USER_SCREEN_MAP_ID in (:ids)");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("ids", roleScreenMapIds);
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception :: deleteRoleScreenMap() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
	}

	@Override
	public Roles getUnappRoleById(Long roleId) throws Exception {
		StringBuilder sqlQry = null;
		Roles role = null;
		try {
			sqlQry = new StringBuilder("SELECT r FROM Roles r WHERE autogenRolesId =:roleId");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("roleId", roleId);
			role = (Roles) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception :: getUnappRoleById() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return role;
	}

	@Override
	public RolesApproved getAppRoleById(Long roleId) throws Exception {
		StringBuilder sqlQry = null;
		RolesApproved role = null;
		try {
			sqlQry = new StringBuilder("SELECT r FROM RolesApproved r WHERE autogenRolesId =:roleId");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("roleId", roleId);
			role = (RolesApproved) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception :: getAppRoleById() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return role;
	}

	@Override
	public List<UserScreenMapApproved> getAppScreenMappingByRoleId(Long roleId) throws Exception {
		StringBuilder sqlQry = null;
		List<UserScreenMapApproved> userScreenMapAppList = null;
		try {
			sqlQry = new StringBuilder("SELECT ui FROM UserScreenMapApproved ui WHERE ui.role.autogenRolesId =:roleId");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("roleId", roleId);
			userScreenMapAppList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Exception :: UserScreenMapApproved() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userScreenMapAppList;
	}

	@Override
	public List<UserScreenMap> getScreenMappingByRoleId(Long roleId) throws Exception {
		StringBuilder sqlQry = null;
		List<UserScreenMap> userScreenMapList = null;
		try {
			sqlQry = new StringBuilder("SELECT ui FROM UserScreenMap ui WHERE ui.role.autogenRolesId =:roleId");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("roleId", roleId);
			userScreenMapList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Exception :: UserScreenMap() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return userScreenMapList;
	}

	@Override
	@Transactional
	public RolesApproved createAppRoleMapping(RolesApproved role) throws Exception {
		try {
			if (role != null) {
				firstEntityManager.persist(role);
			}
		} catch (Exception e) {
			logger.error("Exception :: createRoleMapping() : {} ", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return role;
	}

	@Override
	@Transactional
	public RolesApproved updateAppRoleMapping(RolesApproved role) throws Exception {
		try {
			if (role != null) {
				firstEntityManager.merge(role);
				firstEntityManager.flush();
			}
		} catch (Exception e) {
			logger.error("Exception :: updateAppRoleMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return role;
	}

	@Override
	@Transactional
	public void deleteAppRoleScreenMap(Long roleId) {
		try {
			StringBuilder sqlQry = null;
			sqlQry = new StringBuilder("delete FROM user_rule.roles_approved  WHERE AUTOGEN_ROLES_ID in (:id)");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("id", roleId);
			queryObj.executeUpdate();
			queryObj = firstEntityManager.createNativeQuery(
					"delete FROM user_rule.user_screen_map_approved  WHERE AUTOGEN_ROLE_ID in (:id)");
			queryObj.setParameter("id", roleId);
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception :: deleteAppRoleScreenMap() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserDto addUser(UserDto userDto) throws Exception {
		try {
			if (userDto != null) {
				UsersApproved users = new UsersApproved();
				BeanUtils.copyProperties(userDto, users);
				users.setStatus(ApplicationConstant.ACTIVE_STATUS);
				users.setAutogenUsersId(null);
				users.setApprovedOn(new Timestamp(System.currentTimeMillis()));
				users.setApproverComment("API");
				users.setRecAddDt(new Timestamp(System.currentTimeMillis()));
				users.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
				firstEntityManager.persist(users);
				if (users.getAutogenUsersId() != null) {
					UsersDetailApproved usersDet = new UsersDetailApproved();
					BeanUtils.copyProperties(userDto, usersDet);
					usersDet.setAutogenUsersId(users.getAutogenUsersId());
					usersDet.setAutogenUsersDetailsId(null);
					if (null != userDto.getBusinessUnit() && !userDto.getBusinessUnit().isEmpty()) {
						usersDet.setBuId(new Integer(userDto.getBusinessUnit().get(0).getOrDefault("buId",
								ApplicationConstant.DEFAULT_BU_ID)));
						usersDet.setBuName(userDto.getBusinessUnit().get(0).getOrDefault("buName",
								ApplicationConstant.DEFAULT_BU));
					} else {
						/*
						 * Default domain if not provided
						 */
						usersDet.setBuId(Integer.valueOf(ApplicationConstant.DEFAULT_BU_ID));
						usersDet.setBuName(ApplicationConstant.DEFAULT_BU);
					}
					if (null != userDto.getDomain() && !userDto.getDomain().isEmpty()) {
						usersDet.setBuId(new Integer(userDto.getDomain().get(0).getOrDefault("domainId",
								ApplicationConstant.DEFAULT_DOMAIN_ID)));
						usersDet.setBuName(userDto.getDomain().get(0).getOrDefault("domainName",
								ApplicationConstant.DEFAULT_DOMAIN));
						/*
						 * Default domain and BU if not provided
						 */
						usersDet.setDomainId(Integer.valueOf(ApplicationConstant.DEFAULT_DOMAIN_ID));
						usersDet.setDomainName(ApplicationConstant.DEFAULT_DOMAIN);
					}
					if (userDto.getRoleDetailList() != null && !userDto.getRoleDetailList().isEmpty()) {
						usersDet.setAutogenRolesId(
								new BigInteger(userDto.getRoleDetailList().get(0).getOrDefault("roleId", "0")));
						usersDet.setRolesName(userDto.getRoleDetailList().get(0).getOrDefault("rolesName", ""));
					}
					usersDet.setRecAddDt(new Timestamp(System.currentTimeMillis()));
					usersDet.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
					usersDet.setCreatedBy(users.getCreatedBy());
					usersDet.setUpdatedBy(users.getUpdatedBy());
					firstEntityManager.persist(usersDet);
					if (usersDet.getAutogenUsersDetailsId() != null) {
						UserDomainMapApproved userDomainMap = new UserDomainMapApproved();
						userDomainMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
						userDomainMap.setAutogenUsersId(users.getAutogenUsersId());
						userDomainMap.setRecAddDt(new Timestamp(System.currentTimeMillis()));
						userDomainMap.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
						userDomainMap.setCreatedBy(users.getCreatedBy());
						userDomainMap.setUpdatedBy(users.getUpdatedBy());
						userDomainMap.setAutogenUserDomainMapId(null);

						if (userDto.getDomain() != null && null != userDto.getBusinessUnit()) {

							for (Map<String, String> buDet : userDto.getBusinessUnit()) {

								userDomainMap.setBusinessUnitId(
										new BigInteger(buDet.getOrDefault("buId", ApplicationConstant.DEFAULT_BU_ID)));
								userDomainMap.setBusinessUnitName(
										buDet.getOrDefault("buName", ApplicationConstant.DEFAULT_BU));
								userDomainMap.setDomainId(null != userDto.getDomain().get(0)
										? new BigInteger(userDto.getDomain().get(0).getOrDefault("domainId",
												ApplicationConstant.DEFAULT_DOMAIN_ID))
										: new BigInteger(ApplicationConstant.DEFAULT_DOMAIN_ID));
								userDomainMap.setDomainName(
										!StringUtils.isEmpty(userDto.getDomain().get(0).get("domainName"))
												? userDto.getDomain().get(0).getOrDefault("domainName",
														ApplicationConstant.DEFAULT_DOMAIN)
												: ApplicationConstant.DEFAULT_DOMAIN);
								firstEntityManager.persist(userDomainMap);
							}
						} else {
							/*
							 * Default domain and BU if not provided
							 */
							userDomainMap.setBusinessUnitId(new BigInteger(ApplicationConstant.DEFAULT_BU_ID));
							userDomainMap.setBusinessUnitName(ApplicationConstant.DEFAULT_BU);
							userDomainMap.setDomainId(new BigInteger(ApplicationConstant.DEFAULT_DOMAIN_ID));
							userDomainMap.setDomainName(ApplicationConstant.DEFAULT_DOMAIN);
						}
						if (userDto.getRoleDetailList() != null) {
							for (Map<String, String> roleDet : userDto.getRoleDetailList()) {
								UserRoleMapApproved userRoleMap = new UserRoleMapApproved();
								userRoleMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userRoleMap.setAutogenUsersId(users.getAutogenUsersId());
								userRoleMap.setRoleId(new BigInteger(roleDet.getOrDefault("roleId", "0")));
								userRoleMap.setRoleName(roleDet.get("rolesName"));
								userRoleMap.setAutogenUserDomainMapId(null);
								userRoleMap.setRecAddDt(new Timestamp(System.currentTimeMillis()));
								userRoleMap.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
								userRoleMap.setCreatedBy(users.getCreatedBy());
								userRoleMap.setUpdatedBy(users.getUpdatedBy());
								firstEntityManager.persist(userRoleMap);
							}
						}
					}
					BeanUtils.copyProperties(usersDet, userDto);
				}
				BeanUtils.copyProperties(users, userDto);
			}
		} catch (Exception e) {
			logger.error("Exception :: addUser() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userDto;
	}

	@Override
	@Transactional
	public boolean disableUser(String userId) {
		try {
			StringBuilder sqlQry = null;
			sqlQry = new StringBuilder(
					"update  user_rule.users_approved set status='Inactive'  WHERE employee_id=:userId");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("userId", userId);
			int val = queryObj.executeUpdate();
			if (val > 0)
				return true;
		} catch (Exception e) {
			logger.error("Exception :: disableUser() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return false;
	}

	@Override
	@Transactional
	public List<UsersApproved> getApprovedUsersByEmployeeId(String userId) {
		StringBuilder sqlQry = null;
		List<UsersApproved> usersApproved = null;
		try {
			sqlQry = new StringBuilder("SELECT u FROM UsersApproved u WHERE employeeId =:userId");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("userId", userId);
			usersApproved = (List<UsersApproved>) queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Exception :: getApprovedUsersByEmployeeId() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return usersApproved;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void approveExistRoleMapping(Long roleMapId, String comment, String approvedBy) throws Exception {
		RolesApproved rolesApproved = firstEntityManager.find(RolesApproved.class, roleMapId);
		if (rolesApproved != null) {
			// userDAO.deleteAppRoleScreenMap(roleMapId);
			Roles role = firstEntityManager.find(Roles.class, roleMapId);
			if (role != null) {
				List<Long> roleScreenMapIds = rolesApproved.getUserScreenMap().stream()
						.map(UserScreenMapApproved::getRoleScreenMapId).collect(Collectors.toList());
				BeanUtils.copyProperties(role, rolesApproved);
				rolesApproved.setRoleCreateStatus("Approved");
				rolesApproved.setEditFlag(false);
				rolesApproved.setComment(comment);
				rolesApproved.setApprovedBy(approvedBy);
				rolesApproved.setApprovedOn(new Timestamp(System.currentTimeMillis()));
				rolesApproved.setUserScreenMap(null);
				firstEntityManager.merge(rolesApproved);
				for (UserScreenMap obj : role.getUserScreenMap()) {
					UserScreenMapApproved userScreenMap = null;
					if (null != obj.getRoleScreenMapId()) {
						userScreenMap = firstEntityManager.find(UserScreenMapApproved.class, obj.getRoleScreenMapId());
					}
					if (null == userScreenMap) {
						userScreenMap = new UserScreenMapApproved();
						BeanUtils.copyProperties(obj, userScreenMap);
						userScreenMap.setRole(rolesApproved);
						firstEntityManager.persist(userScreenMap);
					} else {
						BeanUtils.copyProperties(obj, userScreenMap);
						userScreenMap.setRole(rolesApproved);
						userScreenMap = firstEntityManager.merge(userScreenMap);
						roleScreenMapIds.remove(obj.getRoleScreenMapId());
					}
				}
				if (roleScreenMapIds != null && !roleScreenMapIds.isEmpty()) {
					deleteRoleScreenMapApp(roleScreenMapIds);
				}
				role.setRoleCreateStatus("Approved");
				firstEntityManager.merge(role);
			}
		}

	}

	@Override
	@Transactional
	public void updateRoleMapping(RoleRequest roleRequest) throws Exception {
		try {
			Roles role = firstEntityManager.find(Roles.class, roleRequest.getAutogenRolesId());
			if (role != null) {
				role.setDescription(roleRequest.getDescription());
				role.setUpdatedBy(roleRequest.getUpdatedBy());
				role.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
				role.setRoleCreateStatus(roleRequest.getRoleCreateStatus());
				role.setRolesName(roleRequest.getRolesName());
				role.setStatus(roleRequest.getStatus());
				List<Long> roleScreenMapIds = role.getUserScreenMap().stream().map(UserScreenMap::getRoleScreenMapId)
						.collect(Collectors.toList());
				role.setUserScreenMap(null);
				role = firstEntityManager.merge(role);
				for (UserScreenMap obj : roleRequest.getUserScreenMap()) {
					UserScreenMap userScreenMap = null;
					if (null != obj.getRoleScreenMapId()) {
						userScreenMap = firstEntityManager.find(UserScreenMap.class, obj.getRoleScreenMapId());
					}
					if (null == userScreenMap) {
						userScreenMap = new UserScreenMap();
						BeanUtils.copyProperties(obj, userScreenMap);
						userScreenMap.setRoleScreenMapId(null);
						userScreenMap.setRole(role);
						firstEntityManager.persist(userScreenMap);
					} else {
						BeanUtils.copyProperties(obj, userScreenMap);
						userScreenMap.setRole(role);
						userScreenMap.setRoleName(role.getRolesName());
						userScreenMap = firstEntityManager.merge(userScreenMap);
						roleScreenMapIds.remove(obj.getRoleScreenMapId());
					}
				}
				if (roleScreenMapIds != null && !roleScreenMapIds.isEmpty()) {
					deleteRoleScreenMap(roleScreenMapIds);
				}
				if ("Exist".equalsIgnoreCase(roleRequest.getRoleCreateStatus())
						|| "Exist_Approve".equalsIgnoreCase(roleRequest.getRoleCreateStatus())) {
					updateRoleEditFlag(roleRequest.getAutogenRolesId(), true);
				}
			} else {
				throw new Exception("Role Not Found");
			}
		} catch (

		Exception e) {
			logger.error("Exception :: updateRoleMapping() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
	}

	@Override
	public UsersApproved getAppUserById(BigInteger userID) throws Exception {
		StringBuilder sqlQry = null;
		UsersApproved user = null;
		try {
			sqlQry = new StringBuilder("SELECT u FROM UsersApproved u WHERE autogenUsersId =:userID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("userID", userID);
			user = (UsersApproved) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception :: getAppUserById() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return user;
	}

	@Override
	public Users getUnAppUserById(BigInteger userID) throws Exception {
		StringBuilder sqlQry = null;
		Users user = null;
		try {
			sqlQry = new StringBuilder("SELECT u FROM Users u WHERE autogenUsersId =:userID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("userID", userID);
			user = (Users) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception :: getUnAppUserById() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}

		return user;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public UserDto updateUser(UserDto userDto) throws Exception {
		StringBuilder sqlQry = null;
		boolean result = false;
		try {
			UsersApproved user = null;
			sqlQry = new StringBuilder("SELECT u FROM UsersApproved u ");

			if (!StringUtils.isEmpty(userDto.getEmployeeId())) {
				sqlQry.append(" WHERE employeeId = '" + userDto.getEmployeeId() + "'");
			}
			if (null != userDto.getAutogenUsersId()) {
				sqlQry.append(" and autogenUsersId = " + userDto.getAutogenUsersId());
			}

			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			user = (UsersApproved) queryObj.getSingleResult();

			if (user == null) {
				throw new ValidationException("User does not exist", "400");
			}
			if (!CommonUtil.nullRemove(userDto.getFirstName()).isEmpty()) {
				user.setFirstName(userDto.getFirstName());
			}
			if (!CommonUtil.nullRemove(userDto.getLastName()).isEmpty()) {
				user.setLastName(userDto.getLastName());
			}
			if (!CommonUtil.nullRemove(userDto.getEmail()).isEmpty()) {
				/*
				 * if (!user.getEmail().equalsIgnoreCase(userDto.getEmail())) { sqlQry = new
				 * StringBuilder(
				 * "SELECT 1 FROM [user_rule].[users_approved] WHERE EMAIL=:email and AUTOGEN_USERS_ID != :id"
				 * ); queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
				 * queryObj.setParameter("email", userDto.getEmail());
				 * queryObj.setParameter("id", user.getAutogenUsersId()); List<Object[]>
				 * resultObj = queryObj.getResultList(); if (!resultObj.isEmpty()) { throw new
				 * ValidationException("Email is already in use", "400"); } }
				 */
				user.setEmail(userDto.getEmail());
			}
			if (!CommonUtil.nullRemove(userDto.getMobileNumber()).isEmpty()) {
				user.setMobileNumber(userDto.getMobileNumber());
			}
			if (!CommonUtil.nullRemove(userDto.getStatus()).isEmpty()) {
				user.setStatus(userDto.getStatus());
			}
			user.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
			user.setUpdatedBy(userDto.getUpdatedBy());
			user.setApprovedBy(userDto.getUpdatedBy());
			user.setApprovedOn(new Timestamp(System.currentTimeMillis()));
			user.setApproverComment("API");
			user = firstEntityManager.merge(user);

			List<UsersDetailApproved> subResultObj = new ArrayList<>();
			sqlQry = new StringBuilder("SELECT usd FROM UsersDetailApproved usd where usd.autogenUsersId=:USERID");
			Query subQueryObj = firstEntityManager.createQuery(sqlQry.toString());
			subQueryObj.setParameter("USERID", user.getAutogenUsersId());
			subResultObj = subQueryObj.getResultList();
			for (UsersDetailApproved usersDetail : subResultObj) {
				if (userDto.getAutogenRolesId() != null && userDto.getRolesName() != null) {
					usersDetail.setAutogenRolesId(userDto.getAutogenRolesId());
					usersDetail.setRolesName(userDto.getRolesName());
				}
				if (null != userDto.getBusinessUnit() && !userDto.getBusinessUnit().isEmpty()) {
					usersDetail.setBuId(new Integer(
							userDto.getBusinessUnit().get(0).getOrDefault("buId", ApplicationConstant.DEFAULT_BU_ID)));
					usersDetail.setBuName(
							userDto.getBusinessUnit().get(0).getOrDefault("buName", ApplicationConstant.DEFAULT_BU));
				} 
				if (null != userDto.getDomain() && !userDto.getDomain().isEmpty()) {
					usersDetail.setBuId(new Integer(userDto.getDomain().get(0).getOrDefault("domainId",
							ApplicationConstant.DEFAULT_DOMAIN_ID)));
					usersDetail.setBuName(
							userDto.getDomain().get(0).getOrDefault("domainName", ApplicationConstant.DEFAULT_DOMAIN));
				} 
				if (userDto.getRoleDetailList() != null && !userDto.getRoleDetailList().isEmpty()) {
					usersDetail.setAutogenRolesId(
							new BigInteger(userDto.getRoleDetailList().get(0).getOrDefault("roleId", "0")));
					usersDetail.setRolesName(userDto.getRoleDetailList().get(0).get("rolesName"));
				}
				if (userDto.getSupervisorUsersId() != null && userDto.getSupervisorUsersName() != null) {
					usersDetail.setSupervisorUsersId(userDto.getSupervisorUsersId());
					usersDetail.setSupervisorUsersName(userDto.getSupervisorUsersName());
				}

				usersDetail.setUpdatedBy(userDto.getUpdatedBy());
				usersDetail.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
				firstEntityManager.merge(usersDetail);
				if (usersDetail.getAutogenUsersDetailsId() != null) {
					if (userDto.getDomain() != null) {
						Query qryObj = null;
						qryObj = firstEntityManager.createNativeQuery(
								"delete FROM user_rule.user_domain_map_approved  WHERE AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID and AUTOGEN_USERS_ID=:USERID ");
						qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
						qryObj.setParameter("USERID", usersDetail.getAutogenUsersId());
						Integer updateVal = qryObj.executeUpdate();
						if (null != userDto.getBusinessUnit()) {
							for (Map<String, String> buMap : userDto.getBusinessUnit()) {
								UserDomainMapApproved userDomainMap = new UserDomainMapApproved();
								userDomainMap.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
								userDomainMap.setAutogenUsersId(usersDetail.getAutogenUsersId());
								userDomainMap.setBusinessUnitId(
										new BigInteger(buMap.getOrDefault("buId", ApplicationConstant.DEFAULT_BU_ID)));
								userDomainMap.setBusinessUnitName(
										buMap.getOrDefault("buName", ApplicationConstant.DEFAULT_BU));
								userDomainMap.setDomainId(null != userDto.getDomain().get(0)
										? new BigInteger(userDto.getDomain().get(0).getOrDefault("domainId",
												ApplicationConstant.DEFAULT_DOMAIN_ID))
										: new BigInteger(ApplicationConstant.DEFAULT_DOMAIN_ID));
								userDomainMap.setDomainName(
										!StringUtils.isEmpty(userDto.getDomain().get(0).get("domainName"))
												? userDto.getDomain().get(0).getOrDefault("domainName",
														ApplicationConstant.DEFAULT_DOMAIN)
												: ApplicationConstant.DEFAULT_DOMAIN);
								userDomainMap.setCreatedBy(userDto.getUpdatedBy());
								userDomainMap.setRecAddDt(new Timestamp(System.currentTimeMillis()));
								userDomainMap.setUpdatedBy(userDto.getUpdatedBy());
								userDomainMap.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
								firstEntityManager.persist(userDomainMap);
							}
						}
					}
					if (userDto.getRoleDetailList() != null) {
						Query qryObj = null;
						qryObj = firstEntityManager.createNativeQuery(
								"delete FROM user_rule.user_roles_map_approved  WHERE AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID and AUTOGEN_USERS_ID=:USERID ");
						qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
						qryObj.setParameter("USERID", usersDetail.getAutogenUsersId());
						qryObj.executeUpdate();
						for (Map<String, String> roleDet : userDto.getRoleDetailList()) {
							UserRoleMapApproved userRoleMap = new UserRoleMapApproved();
							userRoleMap.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
							userRoleMap.setAutogenUsersId(usersDetail.getAutogenUsersId());
							userRoleMap.setRoleId(new BigInteger(roleDet.getOrDefault("roleId", "0")));
							userRoleMap.setRoleName(roleDet.get("rolesName"));
							userRoleMap.setCreatedBy(userDto.getUpdatedBy());
							userRoleMap.setRecAddDt(new Timestamp(System.currentTimeMillis()));
							userRoleMap.setUpdatedBy(userDto.getUpdatedBy());
							userRoleMap.setRecUpdateDt(new Timestamp(System.currentTimeMillis()));
							firstEntityManager.persist(userRoleMap);
						}
					}

				}
				result = true;
				BeanUtils.copyProperties(usersDetail, userDto);
			}
			BeanUtils.copyProperties(user, userDto);
		} finally {
			firstEntityManager.close();
		}
		return userDto;
	}

	@Override
	public Boolean isUserIdExists(String name) {
		boolean exists = true;
		List<Object[]> result = null;
		try {
			String isUserIdExists = "select 1 from user_rule.users_approved " + "where employee_id = :name union "
					+ "select 1 from user_rule.users where employee_id = :name ";

			Query queryObj = firstEntityManager.createNativeQuery(isUserIdExists);
			queryObj.setParameter("name", name);
			result = queryObj.getResultList();
			if (result.isEmpty()) {
				exists = false;
			}
		} catch (Exception e) {
			logger.error("Exception :: isUserIdExists() : {} ", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return exists;
	}

	@Override
	public Boolean isRoleNameExists(String name) {
		boolean exists = true;
		List<Object[]> result = null;
		try {
			String isRoleNameExists = "select 1 from user_rule.roles_approved "
					+ "where roles_name = :name union select 1 from user_rule.roles where roles_name = :name ";

			Query queryObj = firstEntityManager.createNativeQuery(isRoleNameExists);
			queryObj.setParameter("name", name);
			result = queryObj.getResultList();
			if (result.isEmpty()) {
				exists = false;
			}
		} catch (Exception e) {
			logger.error("Exception :: isRoleNameExists() : {} ", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return exists;
	}

	@Override
	@Transactional
	public void deleteUserScreenMapByRole(Long roleId) {
		try {
			StringBuilder sqlQry = null;
			sqlQry = new StringBuilder("delete FROM user_rule.user_screen_map  WHERE AUTOGEN_ROLE_ID = :roleId");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("roleId", roleId);
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception :: deleteUserScreenMapByRole() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
	}

	@Override
	@Transactional
	public void deleteUnAppRole(Long roleId) {
		try {
			StringBuilder sqlQry = null;
			sqlQry = new StringBuilder("delete FROM user_rule.roles  WHERE AUTOGEN_ROLES_ID = :roleId");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("roleId", roleId);
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception :: deleteUnAppRole() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
	}
	
	
	//Audit Report Changes
	@Override	
	public List<Object[]> findLoginDetails(SearchAuditTrailRequest search) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> result = null;
		try {
			sqlQry = new StringBuilder("SELECT AUTOGEN_LOGIN_DETAILS_ID,EMPLOYEE_ID,format(LOGIN_TIME,'dd-MMM-yyyy hh:mm:ss tt') as LOGIN_TIME,format(LOGOUT_TIME,'dd-MMM-yyyy hh:mm:ss tt') as LOGOUT_TIME,NO_OF_ATTEMPT,REMARKS,CREATED_BY,UPDATED_BY,cast(REC_ADD_DT as date) as REC_ADD_DT,cast(REC_UPDATE_DT as date) as REC_UPDATE_DT,"
					+ " source_ip,user_email FROM user_rule.login_details WHERE  ");
				
			if (!StringUtils.isEmpty(search.getEmployeeId())) {
				sqlQry.append(" EMPLOYEE_ID = :employeeId AND ");
			}	
			if (!StringUtils.isEmpty(search.getStartDate())) {
						sqlQry.append(" cast(REC_ADD_DT as date) between :fromDate and :toDate order by AUTOGEN_LOGIN_DETAILS_ID desc ");
					}			
					
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			
			if (!StringUtils.isEmpty(search.getEmployeeId())) {
				queryObj.setParameter("employeeId", search.getEmployeeId());
			}	
			if (!StringUtils.isEmpty(search.getStartDate())) {
				queryObj.setParameter("fromDate", search.getStartDate());
				queryObj.setParameter("toDate", search.getEndDate());
				}	
			result = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Exception :: findLoginDetails() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return result;
	}
	
	@Override
	public List<Object[]> findAuditLogDetails(SearchAuditTrailRequest search) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> result = null;
		try {
			sqlQry = new StringBuilder("SELECT id,user_id,user_email,source_ip,cast(created_at as date),action,domain,old_value,new_value  FROM user_rule.audit_log WHERE ");
				
			if (!StringUtils.isEmpty(search.getEmployeeId())) {
				sqlQry.append(" user_id = :employeeId AND ");
			}	
			if (!StringUtils.isEmpty(search.getStartDate())) {
						sqlQry.append(" cast(created_at as date) between :fromDate and :toDate order by id desc ");
					}			
					
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			
			if (!StringUtils.isEmpty(search.getEmployeeId())) {
				queryObj.setParameter("employeeId", search.getEmployeeId());
			}	
			if (!StringUtils.isEmpty(search.getStartDate())) {
				queryObj.setParameter("fromDate", search.getStartDate());
				queryObj.setParameter("toDate", search.getEndDate());
				}	
			result = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Exception :: findAuditLogDetails() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return result;
	}

	
	
	@Override
	public String getEncodedPassword(UserDto userDto) throws Exception{
		String encodedPass=null;
		try {
			Query queryVal=firstEntityManager.createNativeQuery("select UserPassword from appointment_remainder.usermanagement_det WHERE UserId = :userId");
			queryVal.setParameter("userId", userDto.getEmployeeId());
			encodedPass = (String) queryVal.getSingleResult();
		}catch(Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Exception :: getEncodedPassword() : {}", str.toString());
		}
		return encodedPass;
	}
	
	
	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean changePassword(UserDto userDto) throws Exception {
		List<Users> usersList = null;
		boolean result = false;
		try {
			StringBuilder sqlQry = new StringBuilder("update appointment_remainder.usermanagement_det SET UserPassword = :newPassword WHERE UserId = :userId ");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("newPassword", userDto.getPassword());
			queryObj.setParameter("userId", userDto.getEmployeeId());
			int resultdata = queryObj.executeUpdate();
			if(resultdata>0) {
				result=true;
			}
		} catch (Exception e) {
			logger.error("Exception :: changePassword() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return result;
	}

	
}
