package com.cognicx.AppointmentRemainder.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

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

public interface CampaignDao {

	String createCampaign(CampaignDetRequest campaignDetRequest) throws Exception;

	Map<String, List<CampaignWeekDetRequest>> getCampaignWeekDet();

	List<Object[]> getCampaignDet(String userGroup);

	List<Object[]> getCampaignDet();

	
	boolean updateCampaign(CampaignDetRequest campaignDetRequest) throws Exception;

	boolean updateCallDetail(UpdateCallDetRequest updateCallDetRequest) throws Exception;

	boolean createContact(ContactDetDto contactDetDto) throws Exception;

	Map<String, List<ContactDetDto>> getContactDet();
	

	boolean validateCampaignName(CampaignDetRequest campaignDetRequest);

	List<Object[]> getSummaryReportDet(ReportRequest reportRequest);

	List<Object[]> getContactDetailReport(ReportRequest reportRequest);

	Map<String, List<Map<Object, Object>>> getCallRetryDetail(List<String> contactIdList);

	Map<String, List<Map<Object, Object>>> getCallRetryDetailAll(List<String> contactIdList);

	List<Object[]> getUploadHistory(ReportRequest reportRequest);

	boolean deleteContactByHistory(UpdateCallDetRequest updateCallDetRequest) throws Exception;

	Integer getTotalContactNo(String HistoryId);

	BigInteger insertUploadHistory(UploadHistoryDto uploadHistoryDto) throws Exception;

	List<CustomerDataDto> getCustomerData();

	List<RetryDetailsDet> getCallRetryDetails(String contact_id);

	RetryCountDto getRetryReport(ReportRequest reportRequest);
	RetryCountDto getRetryReport(ReportRequest reportRequest,String userGroup);
	List<Object[]> getLeadWiseSummary(ReportRequest reportRequest);
	List<Object[]> getLeadWiseSummary(ReportRequest reportRequest,String userGroup);

	List<Object[]> getCallVolumeReport(ReportRequest reportRequest);
	List<Object[]> getCallVolumeReport(ReportRequest reportRequest,String userGroup);
	boolean createDummyContact(ContactDetDto contactDetDto) throws Exception;

	//Added on 05/02/2024	
	boolean getCampaignStatus(CampaignStatus campaignStatus);
	//Added on 14/02/2024

	boolean insertCampaignStatus( CampaignDetRequest campDetRequest)throws Exception;
	boolean updateCampaignStatus(CampaignDetRequest campDetRequest) throws Exception;

	List<Object[]> getCampaignDetForRT(String userGroup) throws Exception;
	List<Object[]> getCampaignDetForRT();
	Integer getCampaignBasedContactCount(String campaignName) throws Exception;
	Integer getCampaginBasedContactStatus(String campaignName,String disposition) throws Exception;

//	boolean updateActiveContDetails(String calluid,String status,String productid,String connectedlinenum,String errorcode) throws Exception;
//
//	boolean insertActiveContDetails(String calluid,String status,String productid,String connectedlinenum) throws Exception;

//	Integer getActiveContDetails(String campaignID) throws Exception;

	Integer getActiveContErrorDetails(String campaignName,String[] errorcodes) throws Exception;
	
	boolean insertSurveyContactDet(Map<String,Object> mapSurveyContact)throws Exception; 
	
	boolean createDnc(DNCDetRequest dNCDetRequest);

	List<Object[]> getdnsDet();

	boolean updateDns(DNCDetRequest dNCDetRequest);

	boolean createContactone(DncContactDto contactDetDto); 
	
	 Integer getCampBasedDNCSize(String dncID);
	 List<String> getCampaignBasedDNClist(String dncID);
	 
	 Map<String, List<SurveyContactDetDto>> getSurveyContactDet();

	int getCountToCall(String productID);

	boolean updateActiveContDetails(String calluid,String status,String productid,String connectedlinenum,String errorcode,String campaignName) throws Exception;

	boolean insertActiveContDetails(String calluid,String status,String productid,String connectedlinenum,String campaignName) throws Exception;

	Integer getActiveContDetails(String campaignID) throws Exception;
	Integer getCompletedCountDetails(Integer campaigncount) throws Exception;

    boolean DeleteContact(DncContactDto contactDetDto);

	boolean updateDeviceEvent(String state,String extn) throws Exception;
	
	boolean updateAgentLoginDetail(String state,String extn)throws Exception;
	
	boolean updateAgentLogoutDetail(String state,String extn)throws Exception;
	
	String getIdleAgentExtn()throws Exception;
	
	String getExtn() throws Exception;
	
}
