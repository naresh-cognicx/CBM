package com.cognicx.AppointmentRemainder.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.cognicx.AppointmentRemainder.Dto.CallRetryReport;
import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.CustomerDataDto;
import com.cognicx.AppointmentRemainder.Dto.DncContactDto;
import com.cognicx.AppointmentRemainder.Dto.SurveyContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.Request.CampaignDetRequest;
import com.cognicx.AppointmentRemainder.Request.CampaignRealTimeDashboard;
import com.cognicx.AppointmentRemainder.Request.CampaignStatus;
import com.cognicx.AppointmentRemainder.Request.DNCDetRequest;
import com.cognicx.AppointmentRemainder.Request.ReportRequest;
import com.cognicx.AppointmentRemainder.Request.SurveyDetRequest;
import com.cognicx.AppointmentRemainder.Request.UpdateCallDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponseReport;

public interface CampaignService {

	ResponseEntity<GenericResponse> createCampaign(CampaignDetRequest campaignDetRequest);

	ResponseEntity<GenericResponse> getCampaignDetail(String userGroup);

	ResponseEntity<GenericResponse> updateCampaign(CampaignDetRequest campaignDetRequest);

	ResponseEntity<GenericResponse> updateCallDetail(UpdateCallDetRequest updateCallDetRequest);

	boolean createContact(ContactDetDto contactDetDto);

	List<CampaignDetRequest> getCampaignDetList(String userGroup);
	
	List<CampaignDetRequest> getCampaignDetList();
	List<CampaignRealTimeDashboard> getRealTimeDashboard();

	Map<String, List<ContactDetDto>> getContactDet();

	ResponseEntity<GenericResponse> validateCampaignName(CampaignDetRequest campaignDetRequest);

	ResponseEntity<GenericResponseReport> summaryReport(ReportRequest reportRequest);
	ResponseEntity<GenericResponseReport> detailReport(ReportRequest reportRequest);

	ResponseEntity<InputStreamResource> downloadDetailReport(ReportRequest reportRequest);

	ResponseEntity<InputStreamResource> downloadSummaryReport(ReportRequest reportRequest);

	ResponseEntity<GenericResponse> getUploadHistory(ReportRequest reportRequest);

	ResponseEntity<GenericResponse> deleteContactByHistory(UpdateCallDetRequest updateCallDetRequest);

	BigInteger insertUploadHistory(UploadHistoryDto uploadHistoryDto);

	List<CustomerDataDto> getCustomerData();

	ResponseEntity<GenericResponse> getRetryReport(ReportRequest reportRequest) throws Exception;

	ResponseEntity<GenericResponse> getRetryReport(ReportRequest reportRequest,String userGroup) throws Exception;
	ResponseEntity<GenericResponseReport> getLeadWiseSummary(ReportRequest reportRequest);
	ResponseEntity<GenericResponseReport> getLeadWiseSummary(ReportRequest reportRequest,String userGroup);
	ResponseEntity<GenericResponseReport> getCallVolumeReport(ReportRequest reportRequest);
	ResponseEntity<GenericResponseReport> getCallVolumeReport(ReportRequest reportRequest,String userGroup);
	boolean createDummyContact(ContactDetDto contactDetDto);
	
	//Added on 05/02/2024	
	boolean getCampaignStatus(CampaignStatus CampaignStatus);
	
	// Added on 1st March 2024
	ResponseEntity<GenericResponse> getRealTimeDashboard(String userGroup);
	List<CampaignRealTimeDashboard> getRealTimeData(String userGroup) throws Exception;

	boolean insertSurveyContactDet(List<Map<String,Object>> listSurveyContact);
	

	
	String getDummySurveyResponse();
	ResponseEntity<GenericResponse> createDnc(DNCDetRequest dNCDetRequest);

	ResponseEntity<GenericResponse> getDnsDetail();

	ResponseEntity<GenericResponse> updateDns(DNCDetRequest dNCDetRequest);

	boolean createContactone(DncContactDto contactDetDto);
	
	List<String> getDNSDetList(String dncID);
	
	 Map<String, List<SurveyContactDetDto>> getSurveyContDet();

	int getCountToCall(String productID);

	List<DNCDetRequest> getDNSDetailList();

	ResponseEntity<GenericResponse> updateContact(DncContactDto contactDetDto);

	ResponseEntity<GenericResponse> DeleteContact(DncContactDto contactDetDto);
}
