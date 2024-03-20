package com.cognicx.AppointmentRemainder.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cognicx.AppointmentRemainder.service.LicenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Dto.UserDto;
import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.CampaignQueryConstant;
import com.cognicx.AppointmentRemainder.constant.UserManagementQueryConstant;
import com.cognicx.AppointmentRemainder.dao.UserManagementDao;
import com.cognicx.AppointmentRemainder.model.Roles;


@Repository("UserManagementDao")
@Transactional
public class UserManagementDaoImpl implements UserManagementDao {

	private Logger logger = LoggerFactory.getLogger(UserManagementDaoImpl.class);

	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;

	@Autowired
	LicenseService licenseService;
	
	@Override
	public String createUser(UserManagementDetRequest userDetRequest) throws Exception {
		String userKey = null;
		boolean isInserted;
		int insertVal;
		try {
			int idValue = getUserKey();
			if (idValue > 9)
				userKey = "U_" + String.valueOf(idValue);
			else
				userKey = "U_0" + String.valueOf(idValue);
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.INSERT_USER_DET);
			queryObj.setParameter("userKey", userKey);
			queryObj.setParameter("FirstName",userDetRequest.getFirstName());
			queryObj.setParameter("LastName",userDetRequest.getLastName());
			queryObj.setParameter("EmailId",userDetRequest.getEmailId());
			queryObj.setParameter("MobNum",userDetRequest.getMobNum());
			queryObj.setParameter("UserId",userDetRequest.getUserId());
			queryObj.setParameter("UserPassword",new BCryptPasswordEncoder().encode(userDetRequest.getPassword()));
//			queryObj.setParameter("UserPassword",licenseService.encrypt(userDetRequest.getPassword()));
			queryObj.setParameter("UserRole",userDetRequest.getRole());
			queryObj.setParameter("PBXExtn",userDetRequest.getPbxExtn());
			queryObj.setParameter("SkillSet",userDetRequest.getSkillSet());
			queryObj.setParameter("Agent",userDetRequest.getAgent());
			queryObj.setParameter("userGroup",userDetRequest.getUserGroup());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
					if(userDetRequest.getRole()!=null && userDetRequest.getRole().equalsIgnoreCase("Agent")) {
						Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.INSERT_AGENT_DET);
						queryUserMapObj.setParameter("Agent",userDetRequest.getUserId());
						queryUserMapObj.executeUpdate();
					}else if(userDetRequest.getRole()!=null && userDetRequest.getRole().equalsIgnoreCase("Supervisor")) {
						Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_AGENT_DET);
						queryUserMapObj.setParameter("Agent",userDetRequest.getUserId());
						queryUserMapObj.setParameter("Supervisor",userDetRequest.getUserId());
						queryUserMapObj.executeUpdate();
					}
					return userKey;
			}
		} catch (Exception e) {
			logger.error("Error occured in UserManagementDaoImpl::createuser" + e);
			return null;
		}
		return null;
	}
	
	public Integer getUserKey() {
		String maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_ID);
			maxVal = (String) queryObj.getSingleResult();
			if(maxVal==null || maxVal.isEmpty()) {
				maxVal="0";
			}
		} catch (Exception e) {
			logger.error("Error occured in UserManagementDaoImpl::getUserKey" + e);
			return 1;
		}
		return Integer.valueOf(maxVal) + 1;
	}

	@Override
	public List<Object[]> getUserDetail() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_DET);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in UserManagementDaoImpl::getUserDetail" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public List<Object[]> getUserDetail(String userGroup) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_DET_USERGROUP);
			queryObj.setParameter("groupName", userGroup);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in UserManagementDaoImpl::getUserDetail" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public boolean updateUser(UserManagementDetRequest userDetRequest) throws Exception {
		boolean isupdated;
		int insertVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_USER_DET);
			queryObj.setParameter("userKey", userDetRequest.getUserKey());
			queryObj.setParameter("FirstName",userDetRequest.getFirstName());
			queryObj.setParameter("LastName",userDetRequest.getLastName());
			queryObj.setParameter("EmailId",userDetRequest.getEmailId());
			queryObj.setParameter("MobNum",userDetRequest.getMobNum());
			queryObj.setParameter("UserId",userDetRequest.getUserId());
			queryObj.setParameter("UserPassword",userDetRequest.getPassword());
			queryObj.setParameter("UserRole",userDetRequest.getRole());
			queryObj.setParameter("PBXExtn",userDetRequest.getPbxExtn());
			queryObj.setParameter("SkillSet",userDetRequest.getSkillSet());
			queryObj.setParameter("Agent",userDetRequest.getAgent());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
					return true;
			}
		} catch (Exception e) {
			logger.error("Error occured in UserManagementImpl::updateUser" + e);
			return false;
		}
		return false;
	}

	
	@Override
	public List<Object[]> getAvailAgent() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_AVAIL_AGENT_DETAILS);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in UserManagementDaoImpl::getAvailAgent" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public List<Object[]> getRoleDetail() {
		List<Object[]>resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_ROLE_DET);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in UserManagementDaoImpl::getRoleDetail" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public Optional<UserDto> findByUsername(String username) throws Exception {
		StringBuilder sqlQry = null;
		Optional<UserDto> userOptional = null;
		UserDto userDto = new UserDto();
		List<Object[]> resultObj = null;
		try {
			logger.info("Getting user details by Id:"+username);
			Query queryObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.GET_USER_DET_BYID);
			queryObj.setParameter("UserId", username);
			resultObj = (List<Object[]>) queryObj.getResultList();

			if (resultObj != null && !resultObj.isEmpty()) {
				for (Object[] objects : resultObj) {
					userDto = new UserDto();
					userDto.setAutogenUsersId(new BigInteger(String.valueOf(1)));
					userDto.setEmployeeId(objects[1].toString());
					userDto.setPassword(objects[2].toString());
					userDto.setStatus("ACTIVE");
					userDto.setEmail(objects[4].toString());
					userDto.setFirstName(objects[5].toString());
					userDto.setLastName(objects[6].toString());
					if (null != objects[7]) {
						userDto.setMobileNumber(objects[7].toString());
					}
					userDto.setAutogenUsersDetailsId(objects[9].toString());
					userDto.setGroupName(objects[8].toString());
					Set<Roles> roleset = new HashSet<>();
					Roles roles = new Roles();
					roles.setRolesName(objects[3].toString());
					roleset.add(roles);
					userDto.setRoles(roleset);
					List<String> rolesList = new ArrayList<String>();
					rolesList.add(roles.getRolesName());
					userDto.setRolesList(rolesList);

				}
			}
			userOptional = Optional.ofNullable(userDto);
		} catch (Exception e) {
			logger.error("Exception findByUsername() : {}", e.getMessage());
		} finally {
			firstEntityManager.close();
		}
		return userOptional;
	}
	
}
