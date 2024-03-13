package com.cognicx.AppointmentRemainder.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.cognicx.AppointmentRemainder.Request.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.cognicx.AppointmentRemainder.Dto.CallRetryReport;
import com.cognicx.AppointmentRemainder.Dto.ContactDetDto;
import com.cognicx.AppointmentRemainder.Dto.CustomerDataDto;
import com.cognicx.AppointmentRemainder.Dto.UploadHistoryDto;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.GenericResponseReport;

public interface CampaignService {

	ResponseEntity<GenericResponse> createCampaign(CampaignDetRequest campaignDetRequest);

	ResponseEntity<GenericResponse> getCampaignDetail();

	ResponseEntity<GenericResponse> updateCampaign(CampaignDetRequest campaignDetRequest);

	ResponseEntity<GenericResponse> updateCallDetail(UpdateCallDetRequest updateCallDetRequest);

	boolean createContact(ContactDetDto contactDetDto);

	List<CampaignDetRequest> getCampaignDetList();

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

	ResponseEntity<GenericResponseReport> getLeadWiseSummary(ReportRequest reportRequest);

	ResponseEntity<GenericResponseReport> getCallVolumeReport(ReportRequest reportRequest);

	boolean createDummyContact(ContactDetDto contactDetDto);
	
	//Added on 05/02/2024	
	boolean getCampaignStatus(CampaignStatus CampaignStatus);
	
	// Added on 1st March 2024
	ResponseEntity<GenericResponse> getRealTimeDashboard();
	List<CampaignRealTimeDashboard> getRealTimeData() throws Exception;


    int getCountToCall(String actionId);

    void getMobileDialed(String contactId,String productId,String customerMobile,String status);

    ResponseEntity<GenericResponse> createTenant(TenantDetRequest tenantDetRequest);
}
