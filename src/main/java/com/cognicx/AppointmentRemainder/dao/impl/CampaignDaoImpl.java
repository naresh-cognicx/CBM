package com.cognicx.AppointmentRemainder.dao.impl;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cognicx.AppointmentRemainder.response.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.Dto.CallRetryReport;
import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.CustomerDataDto;
import com.cognicx.AppointmentRemainder.Dto.DncContactDto;
import com.cognicx.AppointmentRemainder.Dto.RetryCountDto;
import com.cognicx.AppointmentRemainder.Dto.RetryDetailsDet;
import com.cognicx.AppointmentRemainder.Dto.SurveyContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.CampaignStatus;
import com.cognicx.AppointmentRemainder.Request.CampaignWeekDetRequest;
import com.cognicx.AppointmentRemainder.Request.DNCDetRequest;
import com.cognicx.AppointmentRemainder.Request.ReportRequest;
import com.cognicx.AppointmentRemainder.Request.SurveyDetRequest;
import com.cognicx.AppointmentRemainder.Request.UpdateCallDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.CampaignQueryConstant;
import com.cognicx.AppointmentRemainder.dao.CampaignDao;
import com.cognicx.AppointmentRemainder.model.UploadHistoryDet;

@Repository("CampaignDao")
@Transactional
public class CampaignDaoImpl implements CampaignDao {
	private Logger logger = LoggerFactory.getLogger(CampaignDaoImpl.class);

	@PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;

	@Autowired
	@Qualifier("firstJdbcTemplate")
	JdbcTemplate firstJdbcTemplate;

	// private SessionFactory sessionFactory;

	@Override
	public String createCampaign(CampaignDetRequest campaignDetRequest) throws Exception {
		String campaignId = null;
		boolean isInserted;
		int insertVal;
		try {
			int idValue = getCampaignId();
			if (idValue > 9)
				campaignId = "C_" + String.valueOf(idValue);
			else
				campaignId = "C_0" + String.valueOf(idValue);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CAMPAIGN_DET);
			queryObj.setParameter("campaignId", campaignId);
			queryObj.setParameter("name", campaignDetRequest.getCampaignName());
			queryObj.setParameter("desc", campaignDetRequest.getCampaignName());
			if ("true".equalsIgnoreCase(campaignDetRequest.getCampaignActive()))
				queryObj.setParameter("status", 1);
			else
				queryObj.setParameter("status", 0);
			queryObj.setParameter("maxAdvTime", campaignDetRequest.getMaxAdvNotice());
			queryObj.setParameter("retryDelay", campaignDetRequest.getRetryDelay());
			queryObj.setParameter("retryCount", campaignDetRequest.getRetryCount());
			queryObj.setParameter("concurrentCall", campaignDetRequest.getConcurrentCall());
			queryObj.setParameter("startDate", campaignDetRequest.getStartDate());
			queryObj.setParameter("startTime", campaignDetRequest.getStartTime());
			queryObj.setParameter("endDate", campaignDetRequest.getEndDate());
			queryObj.setParameter("endTime", campaignDetRequest.getEndTime());
			queryObj.setParameter("ftpLocation", campaignDetRequest.getFtpLocation());
			queryObj.setParameter("dncId", campaignDetRequest.getDncId());
			queryObj.setParameter("DailingMode", campaignDetRequest.getDailingMode());
			queryObj.setParameter("Queue", campaignDetRequest.getQueue());
			queryObj.setParameter("dispositionID", campaignDetRequest.getDispositionID());
			queryObj.setParameter("groupname", campaignDetRequest.getUserGroup());
			if (!"".equalsIgnoreCase(campaignDetRequest.getFtpUsername())
					&& !"".equalsIgnoreCase(campaignDetRequest.getFtpPassword()))
				queryObj.setParameter("ftpCredentials",
						campaignDetRequest.getFtpUsername() + ";" + campaignDetRequest.getFtpPassword());
			else
				queryObj.setParameter("ftpCredentials", null);
			queryObj.setParameter("fileName", campaignDetRequest.getFileName());
			queryObj.setParameter("callBefore", campaignDetRequest.getCallBefore());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				campaignDetRequest.setCampaignId(campaignId);
				insertCampaignStatus(campaignDetRequest);
				isInserted = createCampaignWeek(campaignDetRequest);
				if (isInserted)
					return campaignId;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::createCampaign" + e);
			return null;
		}
		return null;
	}

	private Integer getCampaignId() {
		String maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_ID);
			maxVal = (String) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignId" + e);
			return 1;
		}
		return Integer.valueOf(maxVal) + 1;
	}

	private boolean createCampaignWeek(CampaignDetRequest campaignDetRequest) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CAMPAIGN_WEEK_DET);
			for (CampaignWeekDetRequest campaignWeekDetRequest : campaignDetRequest.getWeekDaysTime()) {
				queryObj.setParameter("campaignId", campaignDetRequest.getCampaignId());
				queryObj.setParameter("day", campaignWeekDetRequest.getDay());
				if ("true".equalsIgnoreCase(campaignWeekDetRequest.getActive()))
					queryObj.setParameter("status", 1);
				else
					queryObj.setParameter("status", 0);
				queryObj.setParameter("startTime", campaignWeekDetRequest.getStartTime());
				queryObj.setParameter("endTime", campaignWeekDetRequest.getEndTime());
				queryObj.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::createCampaignWeek" + e);
			throw e;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCampaignDet(String userGroup) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_DET_BY_USERGROUP);
			queryObj.setParameter("groupName", userGroup);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignDet" + e);
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCampaignDet() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_DET);
			//queryObj.setParameter("groupName", userGroup);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignDet" + e);
			return resultList;
		}
		return resultList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCampaignDetForRT() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_DET_RT);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignDet" + e);
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<CampaignWeekDetRequest>> getCampaignWeekDet() {
		List<Object[]> resultList;
		Map<String, List<CampaignWeekDetRequest>> campaignWeekDet = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_WEEK_DET);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				String preVal = "";
				campaignWeekDet = new LinkedHashMap<>();
				for (Object[] obj : resultList) {
					if (!preVal.equalsIgnoreCase(String.valueOf(obj[1]))) {
						preVal = String.valueOf(obj[1]);
						campaignWeekDet.put(preVal, new ArrayList<CampaignWeekDetRequest>());
					}
					CampaignWeekDetRequest campaignWeekDetRequest = new CampaignWeekDetRequest();
					campaignWeekDetRequest.setCampaignId(preVal);
					campaignWeekDetRequest.setCampaignWeekId(String.valueOf(obj[0]));
					campaignWeekDetRequest.setDay(String.valueOf(obj[2]));
					campaignWeekDetRequest.setActive(String.valueOf(obj[3]));
					campaignWeekDetRequest.setStartTime(String.valueOf(obj[4]));
					campaignWeekDetRequest.setEndTime(String.valueOf(obj[5]));
					campaignWeekDet.get(preVal).add(campaignWeekDetRequest);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignWeekDet" + e);
			return campaignWeekDet;
		}
		return campaignWeekDet;
	}

	@Override
	public boolean updateCampaign(CampaignDetRequest campaignDetRequest) throws Exception {
		boolean isupdated;
		int insertVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_CAMPAIGN_DET);
			queryObj.setParameter("name", campaignDetRequest.getCampaignName());
			if ("true".equalsIgnoreCase(campaignDetRequest.getCampaignActive()))
				queryObj.setParameter("status", 1);
			else
				queryObj.setParameter("status", 0);
			queryObj.setParameter("maxAdvTime", campaignDetRequest.getMaxAdvNotice());
			queryObj.setParameter("retryDelay", campaignDetRequest.getRetryDelay());
			queryObj.setParameter("retryCount", campaignDetRequest.getRetryCount());
			queryObj.setParameter("concurrentCall", campaignDetRequest.getConcurrentCall());
			queryObj.setParameter("startDate", campaignDetRequest.getStartDate());
			queryObj.setParameter("startTime", campaignDetRequest.getStartTime());
			queryObj.setParameter("endDate", campaignDetRequest.getEndDate());
			queryObj.setParameter("endTime", campaignDetRequest.getEndTime());
			queryObj.setParameter("ftpLocation", campaignDetRequest.getFtpLocation());
			queryObj.setParameter("dncId", campaignDetRequest.getDncId());
			queryObj.setParameter("DailingMode", campaignDetRequest.getDailingMode());
			queryObj.setParameter("Queue", campaignDetRequest.getQueue());
			queryObj.setParameter("dispositionID", campaignDetRequest.getDispositionID());
			queryObj.setParameter("groupname", campaignDetRequest.getUserGroup());
			if (!"".equalsIgnoreCase(campaignDetRequest.getFtpUsername())
					&& !"".equalsIgnoreCase(campaignDetRequest.getFtpPassword()))
				queryObj.setParameter("ftpCredentials",
						campaignDetRequest.getFtpUsername() + ";" + campaignDetRequest.getFtpPassword());
			else
				queryObj.setParameter("ftpCredentials", null);
			queryObj.setParameter("callBefore", campaignDetRequest.getCallBefore());
			queryObj.setParameter("fileName", campaignDetRequest.getFileName());
			queryObj.setParameter("campaignId", campaignDetRequest.getCampaignId());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				updateCampaignStatus(campaignDetRequest);
				isupdated = updateCampaignWeek(campaignDetRequest);
				if (isupdated)
					return true;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::updateCampaign" + e);
			return false;
		}
		return false;
	}

	private boolean updateCampaignWeek(CampaignDetRequest campaignDetRequest) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_CAMPAIGN_WEEK_DET);
			for (CampaignWeekDetRequest campaignWeekDetRequest : campaignDetRequest.getWeekDaysTime()) {
				queryObj.setParameter("day", campaignWeekDetRequest.getDay());
				if ("true".equalsIgnoreCase(campaignWeekDetRequest.getActive()))
					queryObj.setParameter("status", 1);
				else
					queryObj.setParameter("status", 0);
				queryObj.setParameter("startTime", campaignWeekDetRequest.getStartTime());
				queryObj.setParameter("endTime", campaignWeekDetRequest.getEndTime());
				queryObj.setParameter("campaignWeekId", campaignWeekDetRequest.getCampaignWeekId());
				queryObj.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::updateCampaignWeek" + e);
			throw e;
		}
		return true;
	}


	//    @Override
	//    public boolean updateCallDetail(UpdateCallDetRequest updateCallDetRequest) throws Exception {
	//        int insertVal, retryCount = 0;
	//        try {
	//            updateCallDetRequest.setRetryCount(getCallRetryCount(updateCallDetRequest.getContactId()));
	//            if (!"ANSWERED".equalsIgnoreCase(updateCallDetRequest.getCallStatus())) {
	//                retryCount = updateCallDetRequest.getRetryCount() + 1;
	//            } else if ("ANSWERED".equalsIgnoreCase(updateCallDetRequest.getCallStatus())) {
	//                if (updateCallDetRequest.getCallerResponse() == null
	//                        || updateCallDetRequest.getCallerResponse().isEmpty()) {
	//                    updateCallDetRequest.setCallerResponse("0");
	//                }
	//                retryCount = updateCallDetRequest.getRetryCount();
	//            }
	//            Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPADTE_SURVEY_CALL_DET);
	//            queryObj.setParameter("callerResponse", updateCallDetRequest.getCallerResponse());
	//            queryObj.setParameter("callStatus", updateCallDetRequest.getCallStatus());
	//            queryObj.setParameter("callDuration", updateCallDetRequest.getCallDuration());
	//            queryObj.setParameter("retryCount", retryCount);
	//            queryObj.setParameter("contactId", updateCallDetRequest.getContactId());
	//            insertVal = queryObj.executeUpdate();
	//            queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CALL_RETRY_DET);
	//            queryObj.setParameter("contactId", updateCallDetRequest.getContactId());
	//            queryObj.setParameter("callStatus", updateCallDetRequest.getCallStatus());
	//            queryObj.setParameter("retryCount", retryCount);
	//            queryObj.executeUpdate();
	//            if (insertVal > 0) {
	//                return true;
	//            }
	//        } catch (Exception e) {
	//            logger.error("Error occured in CampaignDaoImpl::updateCallDetail" + e);
	//            return false;
	//        }
	//        return false;
	//    }

	@Override
	public boolean updateCallDetail(UpdateCallDetRequest updateCallDetRequest) throws Exception {
		int insertVal, retryCount = 0;
		try {
			logger.info("Disposition :: "+updateCallDetRequest.getDisposition());
			updateCallDetRequest.setRetryCount(getCallRetryCount(updateCallDetRequest.getActionid()));
			if (!"ANSWERED".equalsIgnoreCase(updateCallDetRequest.getDisposition())) {
				retryCount = updateCallDetRequest.getRetryCount() + 1;
			} else if ("ANSWERED".equalsIgnoreCase(updateCallDetRequest.getDisposition())) {
				retryCount = updateCallDetRequest.getRetryCount();
			}
			// Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPADTE_SURVEY_CALL_DET);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPADTE_SURVEY_CALL_DET_CT);

			queryObj.setParameter("callStatus", updateCallDetRequest.getDisposition());
			queryObj.setParameter("callDuration", updateCallDetRequest.getCallduration());
			queryObj.setParameter("retryCount", retryCount);
			queryObj.setParameter("actionId", updateCallDetRequest.getActionid());
			insertVal = queryObj.executeUpdate();
			/*
			 * queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.
			 * INSERT_CALL_RETRY_DET); queryObj.setParameter("contactId",
			 * updateCallDetRequest.getActionid()); queryObj.setParameter("callStatus",
			 * updateCallDetRequest.getDisposition()); queryObj.setParameter("retryCount",
			 * retryCount); queryObj.executeUpdate();
			 */
			if (insertVal > 0) {
				return true;
			}
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::updateCallDetail" + str.toString());
			return false;
		}
		return false;
	}

	@Override
	public boolean createContact(ContactDetDto contactDetDto) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CONTACT_DET_CT);
			queryObj.setParameter("campaignId", contactDetDto.getCampaignId());
			queryObj.setParameter("campaignName", contactDetDto.getCampaignName());
			queryObj.setParameter("subskill_set", contactDetDto.getSubskill_set());
			queryObj.setParameter("customer_mobile_number", contactDetDto.getCustomerMobileNumber());
			//	queryObj.setParameter("contact_id", contactDetDto.getContactId());
			queryObj.setParameter("language", contactDetDto.getLanguage());
			queryObj.setParameter("callStatus", "NEW");
			queryObj.setParameter("historyId", contactDetDto.getHistoryId());
			queryObj.setParameter("due_date",getCampaignEndDate(contactDetDto.getCampaignName()));
			logger.info("campaign due date in insert contact det : "+getCampaignEndDate(contactDetDto.getCampaignName()));
			String seq=getActionSequence();
			logger.info("Action Sequence Value :"+seq);
			queryObj.setParameter("actionId",seq);
			queryObj.executeUpdate();
		} catch (Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::createContact" + str.toString());
			logger.error("Error occured in CampaignDaoImpl::createContact" + e);
			logger.error("campaignId" + contactDetDto.getCampaignId());
			logger.error("campaignName" + contactDetDto.getCampaignName());
			logger.error("last_four_digits" + contactDetDto.getLastFourDigits());
			logger.error("customer_mobile_number" + contactDetDto.getCustomerMobileNumber());
			logger.error("total_due" + contactDetDto.getTotalDue());
			logger.error("minimum_payment" + contactDetDto.getMinPayment());
			logger.error("due_date" + contactDetDto.getDueDate());
			logger.error("language" + "en-US");
			logger.error("callStatus" + "New");
			logger.error("historyId" + contactDetDto.getHistoryId());
			throw e;

		}
		return true;
	}

	private String getActionSequence() {
		String seq = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_ACTIONID_SEQUENCE);
			Object objResult = queryObj.getSingleResult();
			seq=String.valueOf(objResult);
		}catch(Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getActionSequence" + str.toString());
		}
		return seq;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<ContactDetDto>> getContactDet() {
		List<Object[]> resultList;
		Map<String, List<ContactDetDto>> campaignDetMap = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CONTACT_DET);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				String preVal = "";
				campaignDetMap = new LinkedHashMap<>();
				for (Object[] obj : resultList) {
					if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
						preVal = String.valueOf(obj[0]);
						campaignDetMap.put(preVal, new ArrayList<ContactDetDto>());
					}
					ContactDetDto contactDetDto = new ContactDetDto();
					contactDetDto.setCampaignId(preVal);
					contactDetDto.setCampaignName(String.valueOf(obj[1]));
					/*
					 * contactDetDto.setDoctorName(String.valueOf(obj[2]));
					 * contactDetDto.setPatientName(String.valueOf(obj[3]));
					 * contactDetDto.setContactNo(String.valueOf(obj[4]));
					 * contactDetDto.setAppointmentDate(String.valueOf(obj[5]));
					 */
					contactDetDto.setLastFourDigits(String.valueOf(obj[2]));
					contactDetDto.setCustomerMobileNumber(String.valueOf(obj[3]));
					contactDetDto.setTotalDue(String.valueOf(obj[4]));
					contactDetDto.setMinPayment(String.valueOf(obj[5]));
					contactDetDto.setDueDate(String.valueOf(obj[6]));
					contactDetDto.setLanguage(String.valueOf(obj[7]));
					contactDetDto.setContactId(String.valueOf(obj[8]));
					contactDetDto.setCallRetryCount(String.valueOf(obj[9]));
					contactDetDto.setUpdatedDate(String.valueOf(obj[10]));
					contactDetDto.setCallStatus(String.valueOf(obj[11]));
					campaignDetMap.get(preVal).add(contactDetDto);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getContactDet" + e);
			return campaignDetMap;
		}
		return campaignDetMap;
	}

	@Override
	public boolean validateCampaignName(CampaignDetRequest campaignDetRequest) {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.VALIDATE_CAMPAIGN_NAME);
			queryObj.setParameter("name", campaignDetRequest.getCampaignName());
			int result = (int) queryObj.getSingleResult();
			if (result > 0)
				return false;
			else
				return true;
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::validateCampaignName" + e);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getSummaryReportDet(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SUMMARY_REPORT_DET);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			queryObj.setParameter("name", reportRequest.getCampaignId());
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getSummaryReportDet" + e);
			return resultList;
		}
		return resultList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getContactDetailReport(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		StringBuilder query = null;
		try {
			query = new StringBuilder(CampaignQueryConstant.GET_CONTACT_DET_REPORT);
			if (reportRequest.getStartDate() != null && !reportRequest.getStartDate().isEmpty()
					&& reportRequest.getEndDate() != null && !reportRequest.getEndDate().isEmpty()) {
				query.append(" cast(due_date as date) between :startDate and :endDate and ");
			}
			if (reportRequest.getCampaignId() != null && !reportRequest.getCampaignId().isEmpty()) {
				query.append(" campaign_id=:name and ");
			}
			if (reportRequest.getContactNo() != null && !reportRequest.getContactNo().isEmpty()) {
				query.append(" customer_mobile_number=:customer_mobile_number and ");
			}
			//			if (reportRequest.getDoctorName() != null && !reportRequest.getDoctorName().isEmpty()) {
			//				query.append(" doctor_name=:doctorName and ");
			//			}
			if (reportRequest.getCallerChoice() != null && !reportRequest.getCallerChoice().isEmpty()) {
				query.append(" caller_response=:callerResponse and ");
			}
			query.append(" call_status is not null ");
			Query queryObj = firstEntityManager.createNativeQuery(query.toString());
			if (reportRequest.getStartDate() != null && !reportRequest.getStartDate().isEmpty()
					&& reportRequest.getEndDate() != null && !reportRequest.getEndDate().isEmpty()) {
				queryObj.setParameter("startDate", reportRequest.getStartDate());
				queryObj.setParameter("endDate", reportRequest.getEndDate());
			}
			if (reportRequest.getCampaignId() != null && !reportRequest.getCampaignId().isEmpty()) {
				queryObj.setParameter("name", reportRequest.getCampaignId());
			}
			if (reportRequest.getContactNo() != null && !reportRequest.getContactNo().isEmpty()) {
				queryObj.setParameter("customer_mobile_number", reportRequest.getContactNo());
			}
			/*
			 * if (reportRequest.getDoctorName() != null &&
			 * !reportRequest.getDoctorName().isEmpty()) {
			 * queryObj.setParameter("doctorName", reportRequest.getDoctorName()); }
			 */
			if (reportRequest.getCallerChoice() != null && !reportRequest.getCallerChoice().isEmpty()) {
				queryObj.setParameter("callerResponse", reportRequest.getCallerChoice());
			}

			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getContactDetailReport" + e);
			return resultList;
		}
		return resultList;
	}

	//	@SuppressWarnings("unchecked")
	//	@Override
	//	public Map<String, List<Map<Object, Object>>> getCallRetryDetail(List<String> contactIdList) {
	//		List<Object[]> resultList;
	//		Map<String, List<Map<Object, Object>>> callRetryDetMap = null;
	//		try {
	//			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_RETRY_DET);
	//			queryObj.setParameter("contactIdList", contactIdList);
	//			resultList = queryObj.getResultList();
	//			if (resultList != null && !resultList.isEmpty()) {
	//				String preVal = "";
	//				callRetryDetMap = new LinkedHashMap<>();
	//				for (Object[] obj : resultList) {
	//					if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
	//						preVal = String.valueOf(obj[0]);
	//						callRetryDetMap.put(preVal, new ArrayList<Map<Object, Object>>());
	//					}
	//					Map<Object, Object> retryDetailsMap = new LinkedHashMap<>();
	//					retryDetailsMap.put("callStatus", obj[1]);
	//					retryDetailsMap.put("date", obj[2]);
	//					callRetryDetMap.get(preVal).add(retryDetailsMap);
	//				}
	//			}
	//		} catch (Exception e) {
	//			logger.error("Error occured in CampaignDaoImpl::getCallRetryDetail" + e);
	//			return callRetryDetMap;
	//		}
	//		return callRetryDetMap;
	//	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Map<Object, Object>>> getCallRetryDetail(List<String> contactIdList) {
		System.out.println("In getCallRetryDetail");
		Map<String, List<Map<Object, Object>>> callRetryDetMap = new LinkedHashMap<>();

		try {
			int batchSize = 100; // Code change done by Naresh
			List<List<String>> batches = partitionList(contactIdList, batchSize);

			for (List<String> batch : batches) {
				Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_RETRY_DET);
				queryObj.setParameter("contactIdList", batch);
				List<Object[]> resultList = queryObj.getResultList();
				if (resultList != null && !resultList.isEmpty()) {
					String preVal = "";
					for (Object[] obj : resultList) {
						if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
							preVal = String.valueOf(obj[0]);
							callRetryDetMap.put(preVal, new ArrayList<>());
						}
						Map<Object, Object> retryDetailsMap = new LinkedHashMap<>();
						retryDetailsMap.put("callStatus", obj[1]);
						retryDetailsMap.put("date", obj[2]);
						callRetryDetMap.get(preVal).add(retryDetailsMap);
					}
				}
			}
			logger.info("Successfully completed the getCallRetryDetail process.");
		} catch (Exception e) {
			logger.error("Error occurred in CampaignDaoImpl::getCallRetryDetail", e);
			return callRetryDetMap;
		}
		return callRetryDetMap;
	}



	private <T> List<List<T>> partitionList(List<T> list, int batchSize) {
		List<List<T>> batches = new ArrayList<>();
		for (int i = 0; i < list.size(); i += batchSize) {
			int end = Math.min(i + batchSize, list.size());
			batches.add(new ArrayList<>(list.subList(i, end)));
		}
		return batches;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Map<Object, Object>>> getCallRetryDetailAll(List<String> contactIdList) {
		System.out.println("In getCallRetryDetail");
		Map<String, List<Map<Object, Object>>> callRetryDetMap = new LinkedHashMap<>();

		try {
			int batchSize = 100; // Code change done by Naresh
			List<List<String>> batches = partitionList(contactIdList, batchSize);

			for (List<String> batch : batches) {
				Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_RETRY_DET);
				queryObj.setParameter("contactIdList", batch);
				List<Object[]> resultList = queryObj.getResultList();
				if (resultList != null && !resultList.isEmpty()) {
					String preVal = "";
					for (Object[] obj : resultList) {
						if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
							preVal = String.valueOf(obj[0]);
							callRetryDetMap.put(preVal, new ArrayList<>());
						}
						Map<Object, Object> retryDetailsMap = new LinkedHashMap<>();
						retryDetailsMap.put("callStatus", obj[1]);
						retryDetailsMap.put("date", obj[2]);
						callRetryDetMap.get(preVal).add(retryDetailsMap);
					}
				}
			}
			logger.info("Successfully completed the getCallRetryDetail process.");
		} catch (Exception e) {
			logger.error("Error occurred in CampaignDaoImpl::getCallRetryDetail", e);
			return callRetryDetMap;
		}
		return callRetryDetMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUploadHistory(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_UPLOAD_HISTORY_DETIALS);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCallRetryDetail" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public boolean deleteContactByHistory(UpdateCallDetRequest updateCallDetRequest) throws Exception {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.DELETE_CONTACT_BY_HISTORY);
			queryObj.setParameter("historyId", updateCallDetRequest.getHistoryId());
			queryObj.executeUpdate();
			queryObj = firstEntityManager.createNativeQuery(
					"Update appointment_remainder.upload_history_det set status=0 where upload_history_id=:historyId");
			queryObj.setParameter("historyId", updateCallDetRequest.getHistoryId());
			queryObj.executeUpdate();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::deleteContactByHistory" + e);
			return false;
		}
		return true;
	}

	@Override
	public Integer getTotalContactNo(String HistoryId) {
		int count;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(
					"select count(*) from appointment_remainder.contact_det where upload_history_id=:historyId");
			queryObj.setParameter("historyId", HistoryId);
			count = (int) queryObj.getSingleResult();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getTotalContactNo" + e);
			return 0;
		}
		return count;
	}

	@Override
	public BigInteger insertUploadHistory(UploadHistoryDto uploadHistoryDto) throws Exception {
		UploadHistoryDet uploadHistoryDet = null;
		try {
			uploadHistoryDet = new UploadHistoryDet();
			uploadHistoryDet.setCampaignId(uploadHistoryDto.getCampaignId());
			uploadHistoryDet.setCampaignName(uploadHistoryDto.getCampaignName());
			// uploadHistoryDet.setUploadedOn(new Date());
			uploadHistoryDet.setFilename(uploadHistoryDto.getFilename());
			firstEntityManager.persist(uploadHistoryDet);
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::insertUploadHistory" + e);
			throw e;
		}
		return uploadHistoryDet.getUploadHistoryId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerDataDto> getCustomerData() {
		List<Object[]> resultList;
		List<CustomerDataDto> customerList = new ArrayList();
		;
		Map<String, List<ContactDetDto>> campaignDetMap = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CUSTOMER_DATA);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {

				for (Object[] obj : resultList) {

					CustomerDataDto customerDataDto = new CustomerDataDto();
					customerDataDto.setCutomerDataId(String.valueOf(obj[0]));
					customerDataDto.setLastFourDigits(String.valueOf(obj[0]));
					customerDataDto.setMobileNumber(String.valueOf(obj[0]));
					customerDataDto.setTotalDue(String.valueOf(obj[0]));
					customerDataDto.setMinimumPayment(String.valueOf(obj[0]));
					customerDataDto.setDueDate(String.valueOf(obj[0]));
					customerDataDto.setStatus(String.valueOf(obj[0]));
					customerList.add(customerDataDto);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCustomerData" + e);
			return customerList;
		}
		return customerList;
	}

	//    private Integer getCallRetryCount(String contactId) {
	//        Integer retryCount;
	//        try {
	//            Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_RETRY_COUNT);
	//            queryObj.setParameter("contact_id", contactId);
	//            retryCount = (Integer) queryObj.getSingleResult();
	//        } catch (Exception e) {
	//            logger.error("Error occured in CampaignDaoImpl::getCallRetryCount" + e);
	//            return 1;
	//        }
	//        return retryCount;
	//    }
	private Integer getCallRetryCount(String actionId) {
		Integer retryCount;
		try {
			// Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SURVEY_CALL_RETRY_COUNT);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SURVEY_CALL_RETRY_COUNT_CT);

			queryObj.setParameter("actionId", actionId);
			retryCount = (Integer) queryObj.getSingleResult();
			if (retryCount == null) {
				retryCount = 0;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCallRetryCount" + e);
			return 1;
		}
		return retryCount;
	}

	//	private Integer getCallRetryCount(String actionId) {
	//		Integer retryCount;
	//		try {
	//			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SURVEY_CALL_RETRY_COUNT);
	//			queryObj.setParameter("actionId", actionId);
	//			retryCount = (Integer) queryObj.getSingleResult();
	//			if(retryCount==null) {
	//				retryCount=0;
	//			}
	//		} catch (Exception e) {
	//			logger.error("Error occured in CampaignDaoImpl::getCallRetryCount" + e);
	//			return 1;
	//		}
	//		return retryCount;
	//	}

	@SuppressWarnings("unchecked")
	@Override
	public RetryCountDto getRetryReport(ReportRequest reportRequest) {
		List<Object[]> resultList;
		List<CallRetryReport> retryReportList = new ArrayList();
		RetryCountDto retryCountDto = new RetryCountDto();
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CONTACT_DET_BY_DATE_RANGE);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					CallRetryReport callRetryReport = new CallRetryReport();
					callRetryReport.setCampaignId(String.valueOf(obj[0]));
					callRetryReport.setCampaignName(String.valueOf(obj[1]));
					callRetryReport.setLastFourDigits(String.valueOf(obj[2]));
					callRetryReport.setCustomerMobileNumber(String.valueOf(obj[3]));
					callRetryReport.setTotalDue(String.valueOf(obj[4]));
					callRetryReport.setMinPayment(String.valueOf(obj[5]));
					callRetryReport.setDueDate(String.valueOf(obj[6]));
					callRetryReport.setLanguage(String.valueOf(obj[7]));
					callRetryReport.setContactId(String.valueOf(obj[8]));
					callRetryReport.setCallRetryCount(String.valueOf(obj[9]));
					callRetryReport.setUpdatedDate(String.valueOf(obj[10]));
					callRetryReport.setCallStatus(String.valueOf(obj[11]));
					//callRetryReport.setRetryList(getCallRetryDetails(reportRequest.getContactId()));
					allocateRetryCount(retryCountDto, Integer.parseInt(callRetryReport.getCallRetryCount()));
					retryReportList.add(callRetryReport);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getRetryReport" + e);
			return retryCountDto;
		}
		return retryCountDto;
	}

	@Override
	public RetryCountDto getRetryReport(ReportRequest reportRequest, String userGroup) {
		List<Object[]> resultList;
		List<CallRetryReport> retryReportList = new ArrayList();
		RetryCountDto retryCountDto = new RetryCountDto();
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CONTACT_DET_BY_DATE_RANGE_BY_USER_GROUP);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			queryObj.setParameter("userGroup", userGroup);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					CallRetryReport callRetryReport = new CallRetryReport();
					callRetryReport.setCampaignId(String.valueOf(obj[0]));
					callRetryReport.setCampaignName(String.valueOf(obj[1]));
					callRetryReport.setLastFourDigits(String.valueOf(obj[2]));
					callRetryReport.setCustomerMobileNumber(String.valueOf(obj[3]));
					callRetryReport.setTotalDue(String.valueOf(obj[4]));
					callRetryReport.setMinPayment(String.valueOf(obj[5]));
					callRetryReport.setDueDate(String.valueOf(obj[6]));
					callRetryReport.setLanguage(String.valueOf(obj[7]));
					callRetryReport.setContactId(String.valueOf(obj[8]));
					callRetryReport.setCallRetryCount(String.valueOf(obj[9]));
					callRetryReport.setUpdatedDate(String.valueOf(obj[10]));
					callRetryReport.setCallStatus(String.valueOf(obj[11]));
					//callRetryReport.setRetryList(getCallRetryDetails(reportRequest.getContactId()));
					allocateRetryCount(retryCountDto, Integer.parseInt(callRetryReport.getCallRetryCount()));
					retryReportList.add(callRetryReport);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getRetryReport" + e);
			return retryCountDto;
		}
		return retryCountDto;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RetryDetailsDet> getCallRetryDetails(String contact_id) {
		List<Object[]> resultList;
		List<RetryDetailsDet> retryList = new ArrayList();
		;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_RETRY_DETAILS);
			queryObj.setParameter("contact_id", contact_id);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {

				for (Object[] obj : resultList) {

					RetryDetailsDet retryDetailsDet = new RetryDetailsDet();
					retryDetailsDet.setCallRetryId(String.valueOf(obj[0]));
					retryDetailsDet.setContactId(String.valueOf(obj[1]));
					retryDetailsDet.setCallStatus(String.valueOf(obj[2]));
					retryDetailsDet.setRecAddedDate(String.valueOf(obj[3]));
					retryDetailsDet.setRetryCount(String.valueOf(obj[4]));
					retryList.add(retryDetailsDet);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCallRetryDetails" + e);
			return retryList;
		}
		return retryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getLeadWiseSummary(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_LEAD_WISE_SUMMARY_REPORT_DET);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getSummaryReportDet" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public List<Object[]> getLeadWiseSummary(ReportRequest reportRequest, String userGroup) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_LEAD_WISE_SUMMARY_REPORT_DET_BY_USER_GROUP);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			queryObj.setParameter("userGroup", userGroup);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getSummaryReportDet" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getCallVolumeReport(ReportRequest reportRequest) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_VOLUME_REPORT_DET);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getSummaryReportDet" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public List<Object[]> getCallVolumeReport(ReportRequest reportRequest, String userGroup) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CALL_VOLUME_REPORT_DET_BY_USER_GROUP);
			queryObj.setParameter("startDate", reportRequest.getStartDate());
			queryObj.setParameter("endDate", reportRequest.getEndDate());
			queryObj.setParameter("userGroup", userGroup);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getSummaryReportDet" + e);
			return resultList;
		}
		return resultList;
	}

	private RetryCountDto allocateRetryCount(RetryCountDto retryCountDto, Integer retryCount) {
		Integer temp = 0;
		switch (retryCount) {

		case 1:
			temp = retryCountDto.getRetryOne();
			retryCountDto.setRetryOne(++temp);
			break;
		case 2:
			temp = retryCountDto.getRetryTwo();
			retryCountDto.setRetryTwo(++temp);
			break;
		case 3:
			temp = retryCountDto.getRetryThree();
			retryCountDto.setRetryThree(++temp);
			break;
		case 4:
			temp = retryCountDto.getRetryFour();
			retryCountDto.setRetryFour(++temp);
			break;
		case 5:
			temp = retryCountDto.getRetryFive();
			retryCountDto.setRetryFive(++temp);
			break;
		case 6:
			temp = retryCountDto.getRetrySix();
			retryCountDto.setRetrySix(++temp);
			break;
		case 7:
			temp = retryCountDto.getRetrySeven();
			retryCountDto.setRetrySeven(++temp);
			break;
		case 8:
			temp = retryCountDto.getRetryEight();
			retryCountDto.setRetryEight(++temp);
			break;
		default:
			temp = retryCountDto.getAbove();
			retryCountDto.setAbove(++temp);
		}

		return retryCountDto;

	}


	@Override
	public boolean createDummyContact(ContactDetDto contactDetDto) throws Exception {

		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_DUMMY_CONTACT_DET);
			queryObj.setParameter("campaign_id", contactDetDto.getCampaignId());
			queryObj.setParameter("mobile_number", contactDetDto.getCampaignName());
			queryObj.setParameter("unix_time", contactDetDto.getContactNo());
			queryObj.setParameter("due_date", contactDetDto.getAppointmentDate());
			queryObj.setParameter("contact_id", contactDetDto.getCallStatus());
			queryObj.executeUpdate();

		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::createDummyContact" + e);
			logger.error("campaignId" + contactDetDto.getCampaignId());
			logger.error("mobile_number" + contactDetDto.getCampaignName());
			logger.error("unix_time" + contactDetDto.getContactNo());
			logger.error("due_date" + contactDetDto.getAppointmentDate());
			logger.error("contact_id" + contactDetDto.getCallStatus());
			throw e;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean getCampaignStatus(CampaignStatus campaignStatus) {
		List<Object[]> resultList = null;
		boolean campStatus = false;
		try {

			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_STATUS);
			queryObj.setParameter("campaign_id", campaignStatus.getCampaignId());
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object obj : resultList) {
					campStatus = (Boolean) obj;
				}
			}
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getCampaignStatus" + str.toString());
		}
		return campStatus;
	}

	//Added on 14/02/2024
	@Override
	public boolean insertCampaignStatus(CampaignDetRequest campDetRequest) {
		boolean insertionStatus;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CAMPAIGN_STATUS);
			queryObj.setParameter("campaignId", campDetRequest.getCampaignId());
			queryObj.setParameter("campaign_status", campDetRequest.isSchedulerEnabled());
			queryObj.executeUpdate();
			insertionStatus = true;
			logger.error("Insert Campaign Status successfully for the Campaign ID :" + campDetRequest.getCampaignId() + " and It's status :" + campDetRequest.getCampaignActive());
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl:: Insertion Campaign Status" + e);
			logger.error("campaign_id" + campDetRequest.getCampaignId());
			logger.error("campaign_status" + campDetRequest.isSchedulerEnabled());
			throw e;
		}
		return insertionStatus;
	}

	@Override
	public boolean updateCampaignStatus(CampaignDetRequest campDetRequest) {
		boolean updateStatus;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_CAMPAIGN_STATUS);
			queryObj.setParameter("campaign_status", campDetRequest.isSchedulerEnabled());
			queryObj.setParameter("campaignId", campDetRequest.getCampaignId());
			queryObj.executeUpdate();
			updateStatus = true;
			logger.error("Updated Campaign Status successfully for the Campaign ID :" + campDetRequest.getCampaignId() + " and It' Status " + campDetRequest.getCampaignActive());
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl:: Update Campaign Status" + e);
			logger.error("campaign_id" + campDetRequest.getCampaignId());
			logger.error("campaign_status" + campDetRequest.isSchedulerEnabled());
			throw e;
		}
		return updateStatus;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCampaignDetForRT(String userGroup) {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_DET_RT_BY_USERGROUP);
			queryObj.setParameter("userGroup", userGroup);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignDet" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public Integer getCampaignBasedContactCount(String campaignName) {
		Integer maxVal;
		try {
			// Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_BASED_COUNT);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_BASED_COUNT_CT);
			queryObj.setParameter("campaignname", campaignName);
			maxVal = (Integer) queryObj.getSingleResult();
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getCampaign based Contact Count" + str.toString());
			return 0;
		}
		return maxVal;
	}

	@Override
	public Integer getCampaginBasedContactStatus(String campaignName, String disposition) {
		Integer maxVal;
		try {
			// Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_BASED_CONT_STATUS);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_BASED_CONT_STATUS_CT);
			queryObj.setParameter("campaignname", campaignName);
			queryObj.setParameter("disposition", disposition);
			maxVal = (Integer) queryObj.getSingleResult();
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getCampaign based Contact Status" + str.toString());
			return 0;
		}
		return maxVal;
	}


	//    @Override
	//    public boolean updateActiveContDetails(String calluid, String status, String productid, String connectedlinenum, String errorcode) {
	//        boolean updateStatus;
	//        try {
	//            Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_ACTIVE_CONTACT_DET);
	//            queryObj.setParameter("calluid", calluid);
	//            queryObj.setParameter("status", status);
	//            queryObj.setParameter("productid", productid);
	//            queryObj.setParameter("connectedlinenum", connectedlinenum);
	//            queryObj.setParameter("errorcode", errorcode);
	//            queryObj.executeUpdate();
	//            updateStatus = true;
	//            logger.error("Updated Active Contact Details for the Product ID :" + productid);
	//        } catch (Exception e) {
	//            StringWriter str = new StringWriter();
	//            e.printStackTrace(new PrintWriter(str));
	//            logger.error("Error occured in CampaignDaoImpl:: Update Active Contact Status" + str.toString());
	//            throw e;
	//        }
	//        return updateStatus;
	//    }
	//

	public boolean updateActiveContDetails(String calluid, String status, String productid, String connectedlinenum, String errorcode, String campaignName) {
		boolean updateStatus;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_ACTIVE_CONTACT_DET);
			queryObj.setParameter("calluid", calluid);
			queryObj.setParameter("status", status);
			queryObj.setParameter("connectedlinenum", connectedlinenum);
			queryObj.setParameter("errorcode", errorcode);
			queryObj.setParameter("campaignname", campaignName);
			queryObj.executeUpdate();
			updateStatus = true;
			logger.error("Updated Active Contact Details for the Product ID :" + productid);
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl:: Update Active Contact Status" + str.toString());
			throw e;
		}
		return updateStatus;
	}
	//    @Override
	//    public boolean insertActiveContDetails(String calluid, String status, String productid, String connectedlinenum) {
	//        boolean updateStatus;
	//        try {
	//            Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_ACTIVE_CONTACT_DET);
	//            queryObj.setParameter("calluid", calluid);
	//            queryObj.setParameter("status", status);
	//            queryObj.setParameter("productid", productid);
	//            queryObj.setParameter("connectedlinenum", connectedlinenum);
	//            queryObj.executeUpdate();
	//            updateStatus = true;
	//            logger.error("Updated Active Contact Details for the Product ID :" + productid);
	//        } catch (Exception e) {
	//            StringWriter str = new StringWriter();
	//            e.printStackTrace(new PrintWriter(str));
	//            logger.error("Error occured in CampaignDaoImpl:: Insert Active Contact Status" + str.toString());
	//            throw e;
	//        }
	//        return updateStatus;
	//    }

	public boolean insertActiveContDetails(String calluid, String status, String productid, String connectedlinenum, String campaignName) {
		boolean updateStatus;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_ACTIVE_CONTACT_DET);
			queryObj.setParameter("calluid", calluid);
			queryObj.setParameter("status", status);
			queryObj.setParameter("productid", productid);
			queryObj.setParameter("connectedlinenum", connectedlinenum);
			queryObj.setParameter("campaignname", campaignName);
			queryObj.executeUpdate();
			updateStatus = true;
			logger.error("Updated Active Contact Details for the Product ID :" + productid);
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl:: Insert Active Contact Status" + str.toString());
			throw e;
		}
		return updateStatus;
	}

	//    @Override
	//    public Integer getActiveContDetails(String campaignID) throws Exception {
	//        Integer maxVal;
	//        try {
	//            Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_ACTIVE_CONTACT_DET);
	//            queryObj.setParameter("campaignId", campaignID);
	//            queryObj.setParameter("status", "Connected");
	//
	//            maxVal = (Integer) queryObj.getSingleResult();
	//        } catch (Exception e) {
	//            StringWriter str = new StringWriter();
	//            e.printStackTrace(new PrintWriter(str));
	//            logger.error("Error occured in CampaignDaoImpl::getCampaign based Contact Status" + str.toString());
	//            return 0;
	//        }
	//        return maxVal;
	//    }

	@Override
	public Integer getActiveContDetails(String campaignName) throws Exception {
		Integer maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_ACTIVE_CONTACT_DET);
			queryObj.setParameter("campaignname", campaignName);
			queryObj.setParameter("status", "Connected");

			maxVal = (Integer) queryObj.getSingleResult();
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getCampaign based Contact Status" + str.toString());
			return 0;
		}
		return maxVal;
	}

	@Override
	public Integer getActiveContErrorDetails(String campaignName, String[] errorcodes) throws Exception {
		Integer maxVal;
		try {

			StringBuilder sqlQuery = new StringBuilder(CampaignQueryConstant.GET_ACTIVE_CONTACT_ERR_DET);
			for (int i = 0; i < errorcodes.length; i++) {
				if (i > 0) {
					sqlQuery.append(", ");
				}
				sqlQuery.append(":id").append(i);
			}
			sqlQuery.append(")");


			Query queryObj = firstEntityManager.createNativeQuery(sqlQuery.toString());
			queryObj.setParameter("campaignname", campaignName);
			// Setting values for the IN clause
			for (int i = 0; i < errorcodes.length; i++) {
				queryObj.setParameter("id" + i, errorcodes[i]);
			}

			maxVal = (Integer) queryObj.getSingleResult();
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getCampaign based Contact Status" + str.toString());
			return 0;
		}
		return maxVal;
	}

	@Override
	public boolean insertSurveyContactDet(Map<String, Object> mapSurveyContact) {
		boolean insertionStatus = false;
		try {

			//Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_SURVEY_CONTACT_DET);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_SURVEY_CONTACT_DET_CT);
			queryObj.setParameter("phone", mapSurveyContact.get("phone"));
			queryObj.setParameter("actionId", mapSurveyContact.get("actionid"));
			queryObj.setParameter("Survey_Lang", mapSurveyContact.get("Survey_Lang"));
			queryObj.setParameter("MainSkillset", mapSurveyContact.get("MainSkillset"));
			queryObj.setParameter("subSkillset", mapSurveyContact.get("SubSkillset"));
			queryObj.setParameter("call_status", "NEW");
			String campaignEndDate=null;
			String campaignId=null;
			try {
				List<String> campDet=getCampaignEndDateAndId((String)mapSurveyContact.get("MainSkillset"));
				if(campDet!=null && campDet.size()>1) {
					campaignEndDate=campDet.get(0);
					campaignId=campDet.get(1);
				}
			}catch(Exception e) {

			}
			queryObj.setParameter("due_date",campaignEndDate);
			queryObj.setParameter("campaign_id",campaignId);

			queryObj.executeUpdate();
			insertionStatus = true;
			logger.error("Inserted survey contact Status successfully ");
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl:: Insertion contact detail" + e);
		}
		return insertionStatus;
	}



	public boolean createDnc(DNCDetRequest DNCDetRequest) {
		GenericResponse response = new GenericResponse();
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_dNS_DET);
			queryObj.setParameter("DNC_Name", DNCDetRequest.getDncName());
			queryObj.setParameter("description", DNCDetRequest.getDescription());

			int insertVal = queryObj.executeUpdate();
			return insertVal > 0;
		} catch (Exception e) {
			logger.error("Error occurred in CampaignDaoImpl::createDnc", e.getMessage());
			//            throw new RuntimeException("Failed to create DNC!", e); // or handle exception appropriately
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getdnsDet() {
		List<Object[]> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_dns_DET);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getdnsDet" + e);
			return resultList;
		}
		return resultList;
	}

	@Override
	public boolean updateDns(DNCDetRequest DNCDetRequest) {
		boolean isupdated;
		int insertVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_DNS_DET);
			queryObj.setParameter("DNC_Name", DNCDetRequest.getDncName());
			queryObj.setParameter("description", DNCDetRequest.getDescription());
			queryObj.setParameter("DNCID", DNCDetRequest.getDNCID());
			insertVal = queryObj.executeUpdate();
			if (insertVal > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::updateCampaign" + e);
			return false;
		}
		return false;
	}

	//	@Override
	//	public boolean createContactone(DncContactDto contactDetDto) {
	//		try {
	//			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CONTACT_DET1);
	//			// queryObj.setParameter("serialnumber", contactDetDto.getSerialnumber());
	//			queryObj.setParameter("DNCID", contactDetDto.getDNCID());
	//			queryObj.setParameter("contactNumber", contactDetDto.getContactNumber());
	//			logger.error("Value : " + contactDetDto.getDNCID() + "" + contactDetDto.getContactNumber());
	//
	//			queryObj.executeUpdate();
	//		} catch (Exception e) {
	//			StringWriter str=new StringWriter();
	//			e.printStackTrace(new PrintWriter(str));
	//			logger.error("Error occured in CampaignDaoImpl::createContact" + str.toString());
	//			logger.error("serialnumber" + contactDetDto.getSerialnumber());
	//			logger.error("DNCID", contactDetDto.getDNCID());
	//			logger.error("contactNumber" + contactDetDto.getContactNumber());
	//			logger.error("setFailureReason" + contactDetDto.getFailureReason());
	//
	//			throw e;
	//		}
	//		return true;
	//	}

	@Override
	public boolean createContactone(DncContactDto contactDetDto) {
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_CONTACT_DET1);
			// queryObj.setParameter("serialnumber", contactDetDto.getSerialnumber());
			queryObj.setParameter("DNCID", contactDetDto.getDNCID());
			queryObj.setParameter("contactNumber", contactDetDto.getContactNumber());
			logger.info("DNC Value : " + contactDetDto.getDNCID() + "and the Number" + contactDetDto.getContactNumber());
			int insertVal=queryObj.executeUpdate();
			if (insertVal > 0) {
				logger.info("DNC Insertion successful for the Number" + contactDetDto.getContactNumber());
				Query queryUpdatObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_CAMPAIGN_STATUS_BASED_ON_DNC);
				queryUpdatObj.setParameter("contactNumber", contactDetDto.getContactNumber());
				queryUpdatObj.executeUpdate();
				logger.info("DNC Update successful for the Number" + contactDetDto.getContactNumber());
			}
		} catch (Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::createContact" + str.toString());
			logger.error("serialnumber" + contactDetDto.getSerialnumber());
			logger.error("DNCID", contactDetDto.getDNCID());
			logger.error("contactNumber" + contactDetDto.getContactNumber());
			logger.error("setFailureReason" + contactDetDto.getFailureReason());

			throw e;
		}
		return true;
	}

	@Override
	public boolean DeleteContact(DncContactDto contactDetDto) {

		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.DELETE_CONTACT_DET1);
			// queryObj.setParameter("serialnumber", contactDetDto.getSerialnumber());
			queryObj.setParameter("DNCID", contactDetDto.getDNCID());
			queryObj.setParameter("contactNumber", contactDetDto.getContactNumber());
			logger.info("DNC ID : " + contactDetDto.getDNCID() + "and Removal DNC Number" + contactDetDto.getContactNumber()+" DNC Name :"+contactDetDto.getDncName());
			int delVal=queryObj.executeUpdate();
			if (delVal > 0) {
				logger.info("DNC Delete successful for the Number" + contactDetDto.getContactNumber());
				Query queryUpdatObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.DELETE_DNC_ON_CAMPAIGN_DET);
				queryUpdatObj.setParameter("contactNumber", contactDetDto.getContactNumber());
				queryUpdatObj.executeUpdate();
				logger.info("DNC Update successful for the Number" + contactDetDto.getContactNumber());
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::DeleteContact" + e);
			logger.error("serialnumber" + contactDetDto.getSerialnumber());
			logger.error("DNCID", contactDetDto.getDNCID());
			logger.error("contactNumber" + contactDetDto.getContactNumber());
			logger.error("setFailureReason" + contactDetDto.getFailureReason());
			return false;
		}
	}

	@Override
	public Integer getCampBasedDNCSize(String dncID) {
		Integer maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_DNC_CONTACT);
			queryObj.setParameter("DNCID", dncID);
			maxVal = (Integer) queryObj.getSingleResult();
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getCampaign based Contact Status" + str.toString());
			return 0;
		}
		return maxVal;
	}

	@Override
	public List<String> getCampaignBasedDNClist(String dncID) {
		List<String> resultList = null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_DNC_CONTACT_DET);
			queryObj.setParameter("DNCID", dncID);
			resultList = queryObj.getResultList();
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignDet" + e);
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<SurveyContactDetDto>> getSurveyContactDet() {
		//		List<Object[]> resultList;
		//		Map<String, List<SurveyContactDetDto>> campaignDetMap = null;
		//		try {
		//			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SURVEY_CONTACT_DET);
		//			resultList = queryObj.getResultList();
		//			if (resultList != null && !resultList.isEmpty()) {
		//				String preVal = "";
		//				campaignDetMap = new LinkedHashMap<>();
		//				for (Object[] obj : resultList) {
		//					if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
		//						preVal = String.valueOf(obj[0]);
		//						campaignDetMap.put(preVal, new ArrayList<SurveyContactDetDto>());
		//					}
		//					SurveyContactDetDto surveyConDto=new SurveyContactDetDto();
		//					surveyConDto.setSubSkillset(preVal);
		//					surveyConDto.setPhone(String.valueOf(obj[1]));
		//					surveyConDto.setActionId(String.valueOf(obj[2]));
		//					surveyConDto.setSurvey_Lang(String.valueOf(obj[3]));
		//					campaignDetMap.get(preVal).add(surveyConDto);
		//				}
		//			}
		//		} catch (Exception e) {
		//			logger.error("Error occured in CampaignDaoImpl::getSurveyContactDet" + e);
		//			return campaignDetMap;
		//		}
		//		return campaignDetMap;
		List<Object[]> resultList;
		Map<String, List<SurveyContactDetDto>> campaignDetMap = null;
		try {
			//     Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SURVEY_CONTACT_DET);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_SURVEY_CONTACT_DET_CT);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				String preVal = "";
				campaignDetMap = new LinkedHashMap<>();
				for (Object[] obj : resultList) {
					if (!preVal.equalsIgnoreCase(String.valueOf(obj[0]))) {
						preVal = String.valueOf(obj[0]);
						campaignDetMap.put(preVal, new ArrayList<SurveyContactDetDto>());
					}
					SurveyContactDetDto surveyConDto = new SurveyContactDetDto();
					//					surveyConDto.setSubSkillset(preVal);
					//					surveyConDto.setPhone(String.valueOf(obj[1]));
					//					surveyConDto.setActionId(String.valueOf(obj[2]));
					//					surveyConDto.setSurvey_Lang(String.valueOf(obj[3]));
					//					surveyConDto.setMainSkillset(String.valueOf(obj[4]));
					//					surveyConDto.setCall_status(String.valueOf(obj[5]));

					surveyConDto.setMainSkillset(preVal);
					surveyConDto.setPhone(String.valueOf(obj[1]));
					surveyConDto.setActionId(String.valueOf(obj[2]));
					surveyConDto.setSurvey_Lang(String.valueOf(obj[3]));
					surveyConDto.setSubSkillset(String.valueOf(obj[4]));
					surveyConDto.setCall_status(String.valueOf(obj[5]));
					surveyConDto.setRec_update_time(String.valueOf(obj[6]));
					try {
						surveyConDto.setRetryCount(String.valueOf(obj[7]));
					} catch (Exception e) {
						surveyConDto.setRetryCount("0");
					}
					campaignDetMap.get(preVal).add(surveyConDto);
					//  logger.info("TO DELETE --- Campaign Survey Contact Map:" + campaignDetMap);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getSurveyContactDet" + e);
			return campaignDetMap;
		}
		return campaignDetMap;
	}

	@Override
	public int getCountToCall(String productID) {
		Integer maxVal;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.COUNT_ACTIVE_CONTACT_DET);
			queryObj.setParameter("productId", productID);
			queryObj.setParameter("status", "HangUp");
			maxVal = (Integer) queryObj.getSingleResult();
		} catch (Exception e) {

			logger.error("Error occured in CampaignDaoImpl::getCampaign based Contact Status" + e);
			return 0;
		}
		return maxVal;
	}

	@Override
	public Integer getCompletedCountDetails(Integer campaigncount) throws Exception {
		Integer maxVal;
		try {
			//Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_COMPLETED_CALL_COUNT);
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_COMPLETED_CALL_COUNT_CT);
			queryObj.setParameter("campaigncount", campaigncount);
			maxVal = (Integer) queryObj.getSingleResult();
		} catch (Exception e) {
			StringWriter str = new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl::getCompletedCountDetails based Contact Status" + str.toString());
			return 0;
		}
		return maxVal;
	}
	//	@Override
	//	public boolean DeleteContact(DncContactDto contactDetDto) {
	//		try {
	//			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.DELETE_CONTACT_DET1);
	//			// queryObj.setParameter("serialnumber", contactDetDto.getSerialnumber());
	//			queryObj.setParameter("DNCID", contactDetDto.getDNCID());
	//			queryObj.setParameter("contactNumber", contactDetDto.getContactNumber());
	//			logger.error("Value : " + contactDetDto.getDNCID() + "" + contactDetDto.getContactNumber());
	//
	//			queryObj.executeUpdate();
	//		} catch (Exception e) {
	//			logger.error("Error occured in CampaignDaoImpl::DeleteContact" + e);
	//			logger.error("serialnumber" + contactDetDto.getSerialnumber());
	//			logger.error("DNCID", contactDetDto.getDNCID());
	//			logger.error("contactNumber" + contactDetDto.getContactNumber());
	//			logger.error("setFailureReason" + contactDetDto.getFailureReason());
	//
	//			throw e;
	//		}
	//		return true;
	//	}

	@Override
	public boolean updateDeviceEvent(String state,String extn)
	{
		boolean updateStatus=false;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_AGENT_DEVICE_STATUS);
			queryObj.setParameter("state",state);
			queryObj.setParameter("device",extn);
			queryObj.executeUpdate();
			updateStatus=true;
			logger.error("Updated agent device status");
		}catch(Exception e) {
			logger.error("Error occured in CampaignDaoImpl:: Updating agent device status" + e);
			throw e;
		}
		return updateStatus;
	}

	@Override
	public String getIdleAgentExtn() throws Exception {
		List<Object[]> resultList = null;
		String extn=null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_AGENT_LOG_IDLETIME);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					String userID=String.valueOf(obj[0]);
					extn=String.valueOf(obj[1]);
				}
			}

		} catch (Exception e) {
			logger.error("Error occured in CampaignDaoImpl::getCampaignDet" + e);
			return extn;
		}
		return extn;
	}

	@Override
	public boolean updateAgentLoginDetail(String state,String extn) throws Exception {
		String updateStatus=null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_AGENT_LOGIN_STATUS);
			queryObj.setParameter("state",state);
			queryObj.setParameter("device",extn);
			queryObj.executeUpdate();

			//	updateStatus=true;
			logger.error("Updated agent device status");
		}catch(Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl:: Updating agent login status" + str.toString());
			throw e;
		}
		return true;
	}


	@Override
	public boolean updateAgentLogoutDetail(String state,String extn) throws Exception {
		String updateStatus=null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.UPDATE_AGENT_LOGOUT_STATUS);
			queryObj.setParameter("state",state);
			queryObj.setParameter("device",extn);
			queryObj.executeUpdate();

			//	updateStatus=true;
			logger.error("Updated agent device status");
		}catch(Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl:: Updating agent logout status" + str.toString());
			throw e;
		}
		return true;
	}


	@Override
	public String getExtn() throws Exception {
		String phone=null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_AVAIL_AGENT_FORCAMPAIGN);
			phone = (String) queryObj.getSingleResult();
		}catch(Exception e) {
			StringWriter str=new StringWriter();
			e.printStackTrace(new PrintWriter(str));
			logger.error("Error occured in CampaignDaoImpl:: get Extn " + str.toString());
			throw e;
		}
		return phone;
	}

	public List<String> getCampaignEndDateAndId(String name) throws Exception {
		String result=null;
		List<String> listDate=new ArrayList<>();
		List<Object[]> resultList = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String campaignId=null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_END_DATE_ID);
			queryObj.setParameter("name",name);
			queryObj.getResultList();
			Date date=null;
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					date=(Date) obj[0];
					result=sdf.format(date);
					campaignId=(String) obj[1];
					listDate.add(result);
					listDate.add(campaignId);
				}
			}

		}catch(Exception e){
			logger.error("Error occured in CampaignDaoImpl:: getCampaignEndName"+e);
		}
		return listDate;
	}
	public String getCampaignEndDate(String name) throws Exception {
		String campaignEndDate=null;
		String formattedTime=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		List<Object[]> resultList = null;
		Date date=null;
		Time time=null;
		try {
			Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_CAMPAIGN_END_DATE);
			queryObj.setParameter("name",name);
			resultList = queryObj.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object[] obj : resultList) {
					date=(Date) obj[0];
					time=(Time) obj[1];
					LocalTime localTime = time.toLocalTime();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					formattedTime = localTime.format(formatter);
					campaignEndDate=sdf.format(date);
					logger.info("campaign end date : "+campaignEndDate);
					logger.info("campaign end time : "+formattedTime);
				}
			}
		}catch(Exception e){
			logger.error("Error occured in CampaignDaoImpl:: getCampaignEndName"+e);
		}
		return campaignEndDate+" "+formattedTime;
	}


}
