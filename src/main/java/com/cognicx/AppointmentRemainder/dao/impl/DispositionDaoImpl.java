package com.cognicx.AppointmentRemainder.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Request.DispositionCodeDet;
import com.cognicx.AppointmentRemainder.Request.DispositionDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.DispositionQueryConstant;
import com.cognicx.AppointmentRemainder.dao.DispositionDao;
@Repository("DisposistionDao")
@Transactional
public class DispositionDaoImpl implements DispositionDao {
	private Logger logger = LoggerFactory.getLogger(DispositionDaoImpl.class);
	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;

	

	@Override
	public List<Object[]> getDispositionDetail() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.GET_DISPOSITION_DET);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in DispositionDaoImpl::getDispositionDetail" + e);
			return resultList;
		}
		return resultList;
	}


	@Override
	public List<Object[]> getDispositionCodeDetail(String DispId) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.GET_DISPOSITION_CODE_DET);
			queryObj.setParameter("dispId", DispId);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in DispositionDaoImpl::getDispositionDetail" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public String creatDispositionDetail(DispositionDetRequest dispositionDetRequest) {
		String status=null;
		String dispositionId = null;
		String dispCodeId=null;
		boolean isInserted;
		int insertVal;
		try {
			int idValue = getDispositionId();
			if (idValue > 9)
				dispositionId = "D_" + String.valueOf(idValue);
			else
				dispositionId = "D_0" + String.valueOf(idValue);
			
			Query queryObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.INSERT_DISPOSITION_DET);
			queryObj.setParameter("dispId", dispositionId);
			queryObj.setParameter("dispositionName",dispositionDetRequest.getDispositionName());
			queryObj.setParameter("description",dispositionDetRequest.getDescription());
			logger.info("DispositionCodeDet :: "+queryObj);
			Query queryDispCodeObj=null;
			int executeResult=queryObj.executeUpdate();
			if(executeResult>0) {
				status="created";
				List<DispositionCodeDet> dispCodelist=dispositionDetRequest.getDispCodeDetailsList();
				if(dispCodelist!=null && dispCodelist.size()>0) {
					for(DispositionCodeDet dispCodDet:dispCodelist) {
						int idCodeValue=getDispCodeId();
						if (idCodeValue > 9)
							dispCodeId = "C_" + String.valueOf(idCodeValue);
						else
							dispCodeId = "C_0" + String.valueOf(idCodeValue);
						
						queryDispCodeObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.INSERT_DISPOSITION_CODE_DET);
						queryDispCodeObj.setParameter("dispCode", dispCodDet.getCode());
						queryDispCodeObj.setParameter("dispItem", dispCodDet.getItemName());
						queryDispCodeObj.setParameter("dispId",dispositionId);
						queryDispCodeObj.setParameter("dispCodeId", dispCodeId);
						queryDispCodeObj.executeUpdate();
						logger.info("DispositionCodeDet :: "+queryDispCodeObj);
					}
				}
				return dispositionId;
			}
		}catch(Exception e) {
			logger.error("Error occured in DispositionDaoImpl::createDispositionDetail" + e);
			return null;
		}
		return status;
	}
	
	
	@Override
	public String updateDispositionDetail(DispositionDetRequest dispositionDetRequest) {
		String status=null;
		try {
		    String dispID = dispositionDetRequest.getDispId();
		    Query queryObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.UPDATE_DISPOSITION_DET);
		    queryObj.setParameter("dispId", dispID);
		    queryObj.setParameter("dispositionName", dispositionDetRequest.getDispositionName());
		    queryObj.setParameter("description", dispositionDetRequest.getDescription());
		    logger.info("DispositionCodeDet :: " + queryObj);

		    int executeResult = queryObj.executeUpdate();
		    if (executeResult > 0) {
		        status = "Updated";

		        List<DispositionCodeDet> dispCodelist = dispositionDetRequest.getDispCodeDetailsList();
		        if (dispCodelist != null && !dispCodelist.isEmpty()) {
		            for (DispositionCodeDet dispCodDet : dispCodelist) {
		            	String dispCodeID=dispCodDet.getDispCodeId();
		                if (dispCodeID!=null && !dispCodeID.isEmpty()) {
		                    Query queryDispCodeObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.UPDATE_DISPOSITION_CODE_DET);
		                    queryDispCodeObj.setParameter("dispCode", dispCodDet.getCode());
		                    queryDispCodeObj.setParameter("dispItem", dispCodDet.getItemName());
		                    queryDispCodeObj.setParameter("dispId", dispID);
		                    queryDispCodeObj.setParameter("dispCodeId", dispCodeID);
		                    int executeUpdateResult = queryDispCodeObj.executeUpdate();
		                    logger.info("Update DispositionCodeDet :: " + executeUpdateResult);
		                } else {
		                	int idCodeValue=getDispCodeId();
							if (idCodeValue > 9)
								dispCodeID = "C_" + String.valueOf(idCodeValue);
							else
								dispCodeID = "C_0" + String.valueOf(idCodeValue);
		                	
		                    Query updatequeryDispCodeObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.INSERT_DISPOSITION_CODE_DET);
		                    updatequeryDispCodeObj.setParameter("dispCode", dispCodDet.getCode());
		                    updatequeryDispCodeObj.setParameter("dispItem", dispCodDet.getItemName());
		                    updatequeryDispCodeObj.setParameter("dispId", dispID);
		                    updatequeryDispCodeObj.setParameter("dispCodeId", dispCodeID);
		                    updatequeryDispCodeObj.executeUpdate();
		                    logger.info("Inserted DispositionCodeDet :: " + updatequeryDispCodeObj);
		                }
		            }
		        }
		        return status;
		    }
		} catch (Exception e) {
		    logger.error("Error occurred while updating disposition details: " + e.getMessage(), e);
		}
		return status;
	}
	
	
	private Integer getDispositionId() {
		String maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.GET_DISPOSITION_ID);
			maxVal = (String) queryObj.getSingleResult();
			if(maxVal==null || maxVal.isEmpty()) {
				maxVal="0";
			}
		} catch (Exception e) {
			logger.error("Error occured in DispositionDaoImpl::getDispositionId" + e);
			return 1;
		}
		return Integer.valueOf(maxVal) + 1;
	}

	
	private Integer getDispCodeId() {
		String maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(DispositionQueryConstant.GET_DISP_CODE_ID);
			maxVal = (String) queryObj.getSingleResult();
			if(maxVal==null || maxVal.isEmpty()) {
				maxVal="0";
			}
		} catch (Exception e) {
			logger.error("Error occured in DispositionDaoImpl::getDispositionId" + e);
			return 1;
		}
		return Integer.valueOf(maxVal) + 1;
	}
	
}
