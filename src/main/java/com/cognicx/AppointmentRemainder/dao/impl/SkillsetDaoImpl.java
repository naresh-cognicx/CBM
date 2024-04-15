package com.cognicx.AppointmentRemainder.dao.impl;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Request.SkillsetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.CampaignQueryConstant;
import com.cognicx.AppointmentRemainder.constant.SkillsetQueryConstant;
import com.cognicx.AppointmentRemainder.dao.SkillsetDao;

@Repository("SkillsetDao")
@Transactional
public class SkillsetDaoImpl implements SkillsetDao {
	private Logger logger = LoggerFactory.getLogger(SkillsetDaoImpl.class);
	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;
	@Override
	public String createSkillset(SkillsetRequest skillSetRequest) throws Exception {
		String skillSetId = null;
		boolean isInserted;
		int insertVal;
		try {
			int idValue = getskillSetId();
			if (idValue > 9)
				skillSetId = "S_" + String.valueOf(idValue);
			else
				skillSetId = "S_0" + String.valueOf(idValue);
			Query queryObj = firstEntityManager.createNativeQuery(SkillsetQueryConstant.INSERT_SKILLSET_DET);
			queryObj.setParameter("skillsetId", skillSetId);
			queryObj.setParameter("skillName", skillSetRequest.getSkillName());
			queryObj.setParameter("Language", skillSetRequest.getLanguage());
			queryObj.setParameter("TimeZone", skillSetRequest.getTimeZone());
			queryObj.setParameter("ChannelType", skillSetRequest.getChannelType());
			queryObj.setParameter("ServiceLevelThreshold", skillSetRequest.getServiceLevelThreshold());
			queryObj.setParameter("ServiceLevelGoal", skillSetRequest.getServiceLevelGoal());
			queryObj.setParameter("FirstCallResolution", skillSetRequest.getFirstCallResolution());
			queryObj.setParameter("AbandonedRateThreshold", skillSetRequest.getAbandonedRateThreshold());
			queryObj.setParameter("ShortCallThreshold", skillSetRequest.getShortCallThreshold());
			queryObj.setParameter("ShortAbandonedThreshold", skillSetRequest.getShortAbandonedThreshold());
			queryObj.setParameter("CountAbandonedSLA", skillSetRequest.getCountAbandonedSLA());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				skillSetRequest.setSkillsetId(skillSetId);
					return skillSetId;
			}
		} catch (Exception e) {
			logger.error("Error occured in SkillsetDaoImpl::createSkillset" + e);
			return null;
		}
		return null;
	}
	
	private Integer getskillSetId() {
		String maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(SkillsetQueryConstant.GET_SKILLSET_ID);
			maxVal = (String) queryObj.getSingleResult();
			if(maxVal==null || maxVal.isEmpty()) {
				maxVal="0";
			}
		} catch (Exception e) {
			logger.error("Error occured in SkillsetDaoImpl::getskillSetId" + e);
			return 1;
		}
		return Integer.valueOf(maxVal) + 1;
	}
	
	@Override
	public List<Object[]> getSkillsetDetail() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(SkillsetQueryConstant.GET_SKILLSET_DET);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in SkillsetDaoImpl::getSkillsetDetail" + e);
			return resultList;
		}
		return resultList;
	}
	@Override
	public boolean updateSkillset(SkillsetRequest skillSetRequest) throws Exception {
		boolean isupdated;
		int insertVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(SkillsetQueryConstant.UPDATE_SKILLSET_DET);
			queryObj.setParameter("skillName", skillSetRequest.getSkillName());
			queryObj.setParameter("Language", skillSetRequest.getLanguage());
			queryObj.setParameter("TimeZone", skillSetRequest.getTimeZone());
			queryObj.setParameter("ChannelType", skillSetRequest.getChannelType());
			queryObj.setParameter("ServiceLevelThreshold", skillSetRequest.getServiceLevelThreshold());
			queryObj.setParameter("ServiceLevelGoal", skillSetRequest.getServiceLevelGoal());
			queryObj.setParameter("FirstCallResolution", skillSetRequest.getFirstCallResolution());
			queryObj.setParameter("AbandonedRateThreshold", skillSetRequest.getAbandonedRateThreshold());
			queryObj.setParameter("ShortCallThreshold", skillSetRequest.getShortCallThreshold());
			queryObj.setParameter("ShortAbandonedThreshold", skillSetRequest.getShortAbandonedThreshold());
			queryObj.setParameter("CountAbandonedSLA", skillSetRequest.getCountAbandonedSLA());
			queryObj.setParameter("skillsetId", skillSetRequest.getSkillsetId());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
					return true;
			}
		} catch (Exception e) {
			logger.error("Error occured in SkillsetDaoImpl::updateSkillset" + e);
			return false;
		}
		return false;
	}


}
