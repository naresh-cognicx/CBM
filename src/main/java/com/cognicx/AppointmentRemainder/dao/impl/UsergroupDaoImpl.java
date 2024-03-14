package com.cognicx.AppointmentRemainder.dao.impl;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Request.UsergroupDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.UsergroupQueryConstant;
import com.cognicx.AppointmentRemainder.dao.UsergroupDao;


@Repository("UsergroupDao")
@Transactional
public class UsergroupDaoImpl implements UsergroupDao {
	private Logger logger = LoggerFactory.getLogger(UsergroupDaoImpl.class);

	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;


	@Override
	public List<Object[]> getUsergroupDet() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UsergroupQueryConstant.GET_USERGROUP_DET);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in UsergroupDaoImpl::getUsergroupDet" + e);
			return resultList;
		}
		return resultList;
	}


	@Override
	public boolean updateUsergroup(UsergroupDetRequest usergroupDetRequest) throws Exception {
		boolean isupdated;
		int insertVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UsergroupQueryConstant.UPDATE_USERGROUP_DET);
			queryObj.setParameter("groupId", usergroupDetRequest.getUsergroupId());
			queryObj.setParameter("usergroupName", usergroupDetRequest.getUsergroupName());
			queryObj.setParameter("usergroupDesc", usergroupDetRequest.getUsergroupDesc());
			queryObj.setParameter("usergroupType", usergroupDetRequest.getUsergroupType());

			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				//	isupdated = updateCampaignWeek(usergroupDetRequest);
				//	if (isupdated)
				return true;
			}
		} catch (Exception e) {
			logger.error("Error occured in UsergroupDaoImpl::updateUsergroup" + e);
			return false;
		}
		return false;

	}


	@Override
	public String createUsergroup(UsergroupDetRequest usergroupDetRequest) throws Exception {
		int insertVal;
		String usergroupId = null;
		try {
			int idValue = usergroupId();
			if (idValue > 9)
				usergroupId = "U_" + String.valueOf(idValue);
			else
				usergroupId = "U_0" + String.valueOf(idValue);
			Query queryObj = firstEntityManager.createNativeQuery(UsergroupQueryConstant.INSERT_USERGROUP_DET);
			queryObj.setParameter("groupId", usergroupId);
			queryObj.setParameter("usergroupName", usergroupDetRequest.getUsergroupName());
			queryObj.setParameter("usergroupDesc", usergroupDetRequest.getUsergroupDesc());
			queryObj.setParameter("usergroupType", usergroupDetRequest.getUsergroupType());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				usergroupDetRequest.setUsergroupId(usergroupId);
				return usergroupId;
			}
		} catch (Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in UsergroupDaoImpl::createUsergroup" + str.toString());
			return null;
		}
		return null;
	}

	private Integer usergroupId() {
		String maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(UsergroupQueryConstant.GET_USERGROUP_ID);
			maxVal = (String) queryObj.getSingleResult();
			if(maxVal==null || maxVal.isEmpty()) {
				maxVal="0";
			}
		} catch (Exception e) {
			logger.error("Error occured in UsergroupDaoImpl::usergroupId" + e);
			return 1;
		}
		return Integer.valueOf(maxVal) + 1;
	}

}
