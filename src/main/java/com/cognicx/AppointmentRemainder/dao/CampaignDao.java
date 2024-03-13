package com.cognicx.AppointmentRemainder.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.CustomerDataDto;
import com.cognicx.AppointmentRemainder.Dto.RetryCountDto;
import com.cognicx.AppointmentRemainder.Dto.RetryDetailsDet;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.Request.*;

public interface CampaignDao {

	String createCampaign(CampaignDetRequest campaignDetRequest) throws Exception;

	Map<String, List<CampaignWeekDetRequest>> getCampaignWeekDet();

	List<Object[]> getCampaignDet();

	boolean updateCampaign(CampaignDetRequest campaignDetRequest) throws Exception;

	boolean updateCallDetail(UpdateCallDetRequest updateCallDetRequest) throws Exception;

	boolean createContact(ContactDetDto contactDetDto) throws Exception;

	Map<String, List<ContactDetDto>> getContactDet();

	boolean validateCampaignName(CampaignDetRequest campaignDetRequest);

	List<Object[]> getSummaryReportDet(ReportRequest reportRequest);

	List<Object[]> getContactDetailReport(ReportRequest reportRequest);

	Map<String, List<Map<Object, Object>>> getCallRetryDetail(List<String> contactIdList);

	List<Object[]> getUploadHistory(ReportRequest reportRequest);

	boolean deleteContactByHistory(UpdateCallDetRequest updateCallDetRequest) throws Exception;

	Integer getTotalContactNo(String HistoryId);

	BigInteger insertUploadHistory(UploadHistoryDto uploadHistoryDto) throws Exception;

	List<CustomerDataDto> getCustomerData();

	List<RetryDetailsDet> getCallRetryDetails(String contact_id);

	RetryCountDto getRetryReport(ReportRequest reportRequest);

	List<Object[]> getLeadWiseSummary(ReportRequest reportRequest);

	List<Object[]> getCallVolumeReport(ReportRequest reportRequest);

	boolean createDummyContact(ContactDetDto contactDetDto) throws Exception;

	//Added on 05/02/2024	
	boolean getCampaignStatus(CampaignStatus campaignStatus);
	//Added on 14/02/2024

	boolean insertCampaignStatus( CampaignDetRequest campDetRequest)throws Exception;
	boolean updateCampaignStatus(CampaignDetRequest campDetRequest) throws Exception;

	List<Object[]> getCampaignDetForRT() throws Exception;
	Integer getCampaignBasedContactCount(String campaignID) throws Exception;
	Integer getCampaginBasedContactStatus(String campaignID,String disposition) throws Exception;

	 boolean updateActiveContDetails(String calluid,String status,String productid,String connectedlinenum) throws Exception;
	 
	 
	 boolean insertActiveContDetails(String calluid,String status,String productid,String connectedlinenum) throws Exception;
	 
	 Integer getActiveContDetails(String campaignID) throws Exception;

	int getCountToCall(String actionId);


	void getMobileDialed(String contactId, String productId, String customerMobile, String status);

    TenantDetRequest createTenant(TenantDetRequest tenantDetRequest);
}
