//package com.cognicx.AppointmentRemainder.dao.impl;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Repository;
//
//import com.cognicx.AppointmentRemainder.Dto.SurveyTypeDto;
//import com.cognicx.AppointmentRemainder.Dto.UserDto;
//import com.cognicx.AppointmentRemainder.Dto.UserInventoryMapDto;
//import com.cognicx.AppointmentRemainder.Dto.UserLeaveDetailsDto;
//import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
//import com.cognicx.AppointmentRemainder.dao.UserDAO;
//import com.cognicx.AppointmentRemainder.model.Reports;
//import com.cognicx.AppointmentRemainder.model.UserInventoryMap;
//import com.cognicx.AppointmentRemainder.model.UserLeaveDetails;
//import com.cognicx.AppointmentRemainder.model.UsersApproved;
//import com.cognicx.AppointmentRemainder.model.UsersDetailApproved;
//import com.cognicx.AppointmentRemainder.util.DateUtil;
//
//
//@Repository
//public class UserDAOImpl implements UserDAO{
//
//	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
//	public EntityManager firstEntityManager;
//
//
//	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
//
//	@Override
//	public List<UserDto> getApprovedUsersList(String roleName) throws Exception {
//		StringBuilder sqlQry = null;
//		List<UsersApproved> resultObj = new ArrayList<>();
//		List<UserDto> userDtoList = new ArrayList<>();
//		Query queryObj = null;
//		try {
//			sqlQry = new StringBuilder("SELECT us FROM UsersApproved us");
//			if (!roleName.isEmpty()) {
//				sqlQry = new StringBuilder(
//						"SELECT us FROM Users us, UsersDetailApproved ud where us.status='ACTIVE' and ud.autogenUsersId=us.autogenUsersId and ud.autogenRolesId=(SELECT r.autogenRolesId FROM Roles r where r.rolesName='"
//								+ roleName + "')");
//			}
//			queryObj = firstEntityManager.createQuery(sqlQry.toString());
//			resultObj = queryObj.getResultList();
//
//			for (UsersApproved users : resultObj) {
//				UserDto userDto = new UserDto();
//				BeanUtils.copyProperties(users, userDto);
//				List<UsersDetailApproved> subResultObj = new ArrayList<>();
//				sqlQry = new StringBuilder("SELECT usd FROM UsersDetailApproved usd where usd.autogenUsersId=:USERID");
//				queryObj = null;
//				queryObj = firstEntityManager.createQuery(sqlQry.toString());
//				queryObj.setParameter("USERID", users.getAutogenUsersId());
//				subResultObj = queryObj.getResultList();
//				for (UsersDetailApproved users2 : subResultObj) {
//					BeanUtils.copyProperties(users2, userDto);
//					userDto.setRolesName(users2.getRolesName());
//					List<Map<String, String>> roleDetList = getApprovedRoleDetails(
//							String.valueOf(users.getAutogenUsersId()),
//							String.valueOf(users2.getAutogenUsersDetailsId()));
//					if (roleDetList != null) {
//						userDto.setRoleDetailList(roleDetList);
//						userDto.setRolesName(
//								roleDetList.stream().map(a -> a.get("rolesName")).collect(Collectors.joining(",")));
//					}
//					String group = getApprovedGroupDetails(
//							String.valueOf(users.getAutogenUsersId()));
//					logger.info(users.getAutogenUsersId().toString());
//					if (group != null) {
//						logger.info("Group Name : "+group);
//                        userDto.setGroupName(group);
//					}
//					UserDto userDtoDomainDet = getApprovedDomainDetails(String.valueOf(users.getAutogenUsersId()),
//							String.valueOf(users2.getAutogenUsersDetailsId()));
//					if (userDtoDomainDet != null) {
//						userDto.setDomain(userDtoDomainDet.getDomain());
//						userDto.setBusinessUnit(userDtoDomainDet.getBusinessUnit());
//					}
//					List<Object[]> resultObjList = new ArrayList<>();
//					queryObj = firstEntityManager.createQuery(
//							"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='ACTIVE'");
//					queryObj.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
//					List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList();
//					List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>();
//					for (UserInventoryMap userInventoryMap : userInventoryMapResult) {
//						UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
//						BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
//						userInventoryMapDtos.add(userInventoryMapDto);
//					}
//					userDto.setUserInventoryMapDtoList(userInventoryMapDtos);
//
//					resultObjList = new ArrayList<>();
//					sqlQry = new StringBuilder(
//							"SELECT im.AUTOGEN_REPORT_MASTER_ID, im.REPORT_NAME from user_rule.REPORT_MASTER im, user_rule.USER_REPORT_MAP usd where im.AUTOGEN_REPORT_MASTER_ID = usd.AUTOGEN_REPORT_MASTER_ID AND usd.AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID AND usd.STATUS=im.STATUS AND usd.STATUS='ACTIVE'");
//					Query subQueryObj2 = firstEntityManager.createNativeQuery(sqlQry.toString());
//					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
//					resultObjList = subQueryObj2.getResultList();
//					List<Reports> reports = new ArrayList<>();
//					for (Object[] userreportmap : resultObjList) {
//						Reports report = new Reports();
//						report.setId(new BigInteger((userreportmap[0].toString())));
//						report.setReportName(String.valueOf(userreportmap[1]));
//						reports.add(report);
//					}
//					userDto.setReports(reports);
//
//					String grouprolesname = "";
//					sqlQry = new StringBuilder(
//							"SELECT im from UserLeaveDetails im where im.autogenUsersDetailsId=:USERDETAILSID AND im.status != 'INACTIVE'");
//					subQueryObj2 = firstEntityManager.createQuery(sqlQry.toString());
//					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
//
//					List<UserLeaveDetails> userLeaveDetailsList = new ArrayList<>();
//					sqlQry = new StringBuilder(
//							"SELECT im from UserLeaveDetails im where im.autogenUsersDetailsId=:USERDETAILSID AND im.status != 'INACTIVE'");
//					subQueryObj2 = firstEntityManager.createQuery(sqlQry.toString());
//					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
//					userLeaveDetailsList = subQueryObj2.getResultList();
//					List<UserLeaveDetailsDto> uesrLeaveDetailsDtoList = new ArrayList<>();
//					for (UserLeaveDetails userLeaveDetails : userLeaveDetailsList) {
//						UserLeaveDetailsDto userLeaveDetailsDto = new UserLeaveDetailsDto();
//						userLeaveDetailsDto.setLeaveDetailsId(userLeaveDetails.getAutogenUserLeaveDetailsId());
//						userLeaveDetailsDto.setFromDate(DateUtil.convertDatetoString(userLeaveDetails.getFromDate(),
//								DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
//						userLeaveDetailsDto.setToDate(DateUtil.convertDatetoString(userLeaveDetails.getToDate(),
//								DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
//						userLeaveDetailsDto.setNoOfDays(userLeaveDetails.getNoOfDays());
//						userLeaveDetailsDto.setReasons(userLeaveDetails.getReasons());
//						userLeaveDetailsDto.setComments(userLeaveDetails.getComments());
//						userLeaveDetailsDto.setStatus(userLeaveDetails.getStatus());
//						uesrLeaveDetailsDtoList.add(userLeaveDetailsDto);
//					}
//					userDto.setUserLeaveDetailsDtoList(uesrLeaveDetailsDtoList);
//					List<SurveyTypeDto> surveyType = getUserSurveyTypeList(users.getAutogenUsersId().toString());
//					if (!surveyType.isEmpty()) {
//						// Clearing the StringBuilder and result object list is unnecessary as you're reassigning them
//						sqlQry = new StringBuilder(
//								"SELECT survey_id, survey_name, employee_id, autogen_id, status FROM user_rule.user_survey_mapping WHERE autogen_user_id=:userId");
//
//						subQueryObj2 = firstEntityManager.createNativeQuery(sqlQry.toString());
//						subQueryObj2.setParameter("userId", users.getAutogenUsersId());
//						resultObjList = subQueryObj2.getResultList();
//						if (!resultObjList.isEmpty()) {
//							for (Object[] surveyObj : resultObjList) {
//								SurveyTypeDto surveyTypeDto = new SurveyTypeDto();
//								surveyTypeDto.setId((int) surveyObj[0]);
//								surveyTypeDto.setLabel((String) surveyObj[1]);
////								surveyTypeDto.setUserId(CommonUtil.nullRemove(surveyObj[2]).toString());
//								surveyTypeDto.setAutogenId(surveyObj[3].toString());
//								surveyTypeDto.setStatus((String) surveyObj[4]);
//								surveyType.add(surveyTypeDto);
//							}
//						}
//
//						userDto.setSurveyTypes(surveyType);
//					}
//				}
//				userDtoList.add(userDto);
//			}
//		} catch (Exception e) {
//			logger.error("Exception :: getApprovedUsersList() : {}", e.getMessage());
//		} finally {
//			firstEntityManager.close();
//		}
//		return userDtoList;
//	}
//
//	@SuppressWarnings("unchecked")
//	private List<Map<String, String>> getApprovedRoleDetails(String userId, String userDetId) throws Exception {
//		StringBuilder sqlQry = null;
//		List<Object[]> resultList = new ArrayList<>();
//		List<Map<String, String>> roleDetails = null;
//		Map<String, String> roleMap = null;
//		try {
//			roleDetails = new ArrayList<>();
//			sqlQry = new StringBuilder(
//					"select rma.AUTOGEN_ROLES_ID,ra.ROLES_NAME	from user_rule.user_roles_map_approved rma, user_rule.roles_approved ra where ra.AUTOGEN_ROLES_ID=rma.AUTOGEN_ROLES_ID and rma.AUTOGEN_USERS_DETAILS_ID =:userDetailId and rma.AUTOGEN_USERS_ID=:userId");
//			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
//			queryObj.setParameter("userDetailId", userDetId);
//			queryObj.setParameter("userId", userId);
//			resultList = queryObj.getResultList();
//			if (resultList != null && !resultList.isEmpty()) {
//				for (Object[] obj : resultList) {
//					roleMap = new LinkedHashMap<>();
//					roleMap.put("roleId", obj[0].toString());
//					roleMap.put("rolesName", obj[1].toString());
//					roleDetails.add(roleMap);
//				}
//			}
//			return roleDetails;
//		} catch (Exception e) {
//			logger.error("Exception :: getApprovedRoleDetails() : {}", e.getMessage());
//			return null;
//		}
//	}
//
//	private String getApprovedGroupDetails(String group_id) {
//		StringBuilder sqlQry = null;
//		List resultList = new ArrayList<>();
//		List<String> groupNames = new ArrayList<>();
//		try {
//			logger.info("In Rolename getapprovel userid " + group_id);
//			sqlQry = new StringBuilder(
//					"SELECT t2.GROUP_NAME\n" +
//							"FROM user_rule.user_groups_map t1\n" +
//							"JOIN user_rule.user_groups t2 ON t1.Autogen_user_groups_Id = t2.Autogen_user_groups_Id\n" +
//							"WHERE t1.Autogen_users_Id =:group_id");
//			logger.info("In Rolename getapprovel userId " + group_id);
//			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
//			queryObj.setParameter("group_id", group_id);
//			resultList = queryObj.getResultList();
//			logger.info("Group name : " + resultList);
//			if (resultList != null && !resultList.isEmpty()) {
//				for (Object obj : resultList) {
//					groupNames.add((String) obj);
//				}
//			}
//			return String.join(", ", groupNames);
//		} catch (Exception e) {
//			logger.error("Exception :: getApprovedRoleDetails() : {}", e.getMessage());
//			return null;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public UserDto getApprovedDomainDetails(String userId, String userDetId) throws Exception {
//		StringBuilder sqlQry = null;
//		List<Object[]> resultList = new ArrayList<>();
//		List<Map<String, String>> domain = null;
//		List<Map<String, String>> businessUnit = null;
//		Map<String, String> domainMap = new LinkedHashMap();
//		UserDto userDto;
//		try {
//			domain = new ArrayList<>();
//			businessUnit = new ArrayList<>();
//			sqlQry = new StringBuilder(
//					"select DOMAIN_ID,DOMAIN_NAME,BUSINESS_UNIT_ID,BUSINESS_UNIT_NAME from user_rule.user_domain_map_approved where AUTOGEN_USERS_DETAILS_ID=:uerDetId and AUTOGEN_USERS_ID=:userId");
//			Query query = firstEntityManager.createNativeQuery(sqlQry.toString());
//			query.setParameter("uerDetId", userDetId);
//			query.setParameter("userId", userId);
//			resultList = query.getResultList();
//			if (resultList != null && !resultList.isEmpty()) {
//				for (Object[] obj : resultList) {
//					if (domainMap.isEmpty()) {
//						domainMap.put("domainId", obj[0].toString());
//						domainMap.put("domainName", obj[1].toString());
//					}
//					Map<String, String> buMap = new LinkedHashMap<>();
//					buMap.put("buId", obj[2].toString());
//					buMap.put("buName", obj[3].toString());
//					businessUnit.add(buMap);
//				}
//			}
//			userDto = new UserDto();
//			domain.add(domainMap);
//			userDto.setDomain(domain);
//			userDto.setBusinessUnit(businessUnit);
//			return userDto;
//		} catch (Exception e) {
//			logger.error("Exception :: getApprovedDomainDetails() : {}", e.getMessage());
//			return null;
//		}
//	}
//
//	@Override
//	public List<SurveyTypeDto> getUserSurveyTypeList(final String userId) throws Exception {
//		StringBuilder sqlQry = null;
//		List<Object[]> result = new ArrayList<>();
//		List<SurveyTypeDto> surveyList = new ArrayList<>();
//		try {
//			sqlQry = new StringBuilder(
//					"SELECT survey_id,survey_name from user_survey_mapping where employee_id=:userId");
//			Query query = firstEntityManager.createNativeQuery(sqlQry.toString());
//			query.setParameter("userId", userId);
//			result = query.getResultList();
//			if (!result.isEmpty()) {
//				for (Object[] surveyObj : result) {
//					SurveyTypeDto surveyTypeDto = new SurveyTypeDto();
//					surveyTypeDto.setId((int) surveyObj[0]);
//					surveyTypeDto.setLabel((String) surveyObj[1]);
//					surveyList.add(surveyTypeDto);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("Exception :: getUserSurveyTypeList() : {}", e.getMessage());
//		} finally {
//			firstEntityManager.close();
//		}
//
//		return surveyList;
//	}
//
//
//}
