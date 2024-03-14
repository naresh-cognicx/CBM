package com.cognicx.AppointmentRemainder.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Request.UserManagementDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.CampaignQueryConstant;
import com.cognicx.AppointmentRemainder.constant.UserManagementQueryConstant;
import com.cognicx.AppointmentRemainder.dao.UserManagementDao;


@Repository("UserManagementDao")
@Transactional
public class UserManagementDaoImpl implements UserManagementDao {

	private Logger logger = LoggerFactory.getLogger(UserManagementDaoImpl.class);

	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;
	
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
			queryObj.setParameter("UserPassword",userDetRequest.getPassword());
			queryObj.setParameter("UserRole",userDetRequest.getRole());
			queryObj.setParameter("PBXExtn",userDetRequest.getPbxExtn());
			queryObj.setParameter("SkillSet",userDetRequest.getSkillSet());
			queryObj.setParameter("Agent",userDetRequest.getAgent());
			queryObj.setParameter("userGroup",userDetRequest.getUserGroup());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
					if(userDetRequest.getRole()!=null && userDetRequest.getRole().equalsIgnoreCase("Agent")) {
						Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.INSERT_AGENT_DET);
						queryUserMapObj.setParameter("Agent",userDetRequest.getAgent());
						queryUserMapObj.executeUpdate();
					}else if(userDetRequest.getRole()!=null && userDetRequest.getRole().equalsIgnoreCase("Supervisor")) {
						Query queryUserMapObj = firstEntityManager.createNativeQuery(UserManagementQueryConstant.UPDATE_AGENT_DET);
						queryUserMapObj.setParameter("Agent",userDetRequest.getAgent());
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
	
	private Integer getUserKey() {
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
			logger.error("Error occured in UserManagementDaoImpl::getUserDetail" + e);
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
	
}
