package com.cognicx.AppointmentRemainder.service.impl;

import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.dao.TenantDao;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.TenantDetResponse;
import com.cognicx.AppointmentRemainder.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    private static Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final TenantDao tenantDao;

    @Autowired
    public TenantServiceImpl(TenantDao tenantDao) {
        this.tenantDao = tenantDao;
    }

    @Override
    public ResponseEntity<GenericResponse> createTenant(TenantDetRequest tenantDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        TenantDetResponse response = new TenantDetResponse();
        try {
            response = tenantDao.createTenant(tenantDetRequest);
            genericResponse.setStatus(200);
            genericResponse.setValue(response);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in CampaignServiceImpl::createTenant " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue(response);
            genericResponse.setMessage("error on while creating tenant");
        }
        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> updateTenant(TenantDetRequest tenantDetRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            boolean isUpdated = tenantDao.updateTenantDet(tenantDetRequest);
            if (isUpdated) {
                genericResponse.setStatus(200);
                genericResponse.setValue("Success");
                genericResponse.setMessage("Tenant details updated successfully");
            } else {
                genericResponse.setStatus(400);
                genericResponse.setValue("Failure");
                genericResponse.setMessage("Error occured while updating tenant details");
            }
        } catch (Exception e) {
            logger.error("Error in CampaignServiceImpl::updateCampaign " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("Error occured while updating Campaign");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    public ResponseEntity<GenericResponse> getTenantList() {
        GenericResponse genericResponse = new GenericResponse();
        List<TenantDetRequest> tenantDetRequestList = null;
        try {
            tenantDetRequestList = getTenantDetList();
            genericResponse.setStatus(200);
            genericResponse.setValue(tenantDetRequestList);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in CampaignServiceImpl::getTenantList " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }
    private List<TenantDetRequest> getTenantDetList() throws ParseException {
        List<TenantDetRequest> tenantDetRequestList = new ArrayList<>();
        List<Object[]> tenantDetRequestListObj = tenantDao.getTenantDet();
        for (Object[] obj : tenantDetRequestListObj) {
            TenantDetRequest tenantDetRequest = new TenantDetRequest();
            tenantDetRequest.setTenant_autogen_id((Integer) obj[0]);
            tenantDetRequest.setTenantId(String.valueOf(obj[1]));
            tenantDetRequest.setTenantName(String.valueOf(obj[2]));
            tenantDetRequest.setLoginUrl(String.valueOf(obj[3]));
            tenantDetRequest.setAddress(String.valueOf(obj[4]));
            tenantDetRequest.setAdminUser(String.valueOf(obj[5]));
            tenantDetRequest.setPassword(String.valueOf(obj[6]));
            tenantDetRequest.setAddress(String.valueOf(obj[7]));
            tenantDetRequest.setContactPerson(String.valueOf(obj[8]));
            tenantDetRequest.setContactEmail(String.valueOf(obj[9]));
            tenantDetRequest.setPartnerId(String.valueOf(obj[10]));
            tenantDetRequest.setPartnerName(String.valueOf(obj[11]));
            tenantDetRequest.setPartnerEmail(String.valueOf(obj[12]));

            tenantDetRequest.setOnBoarding((Timestamp) obj[13]);
            tenantDetRequest.setStartContract((Timestamp) obj[14]);
            tenantDetRequest.setEndContract((Timestamp) obj[15]);

            tenantDetRequest.setBilledTo(String.valueOf(obj[16]));
            tenantDetRequest.setBilledCycle(String.valueOf(obj[17]));
            tenantDetRequest.setPaymentTerms(String.valueOf(obj[18]));
            tenantDetRequest.setConcurrency(Integer.parseInt(String.valueOf(obj[19])));
            tenantDetRequest.setNoOflines((Integer) obj[20]);
            tenantDetRequest.setNoOfUsers((Integer) obj[21]);
            tenantDetRequest.setLicenseKey((String) obj[22]);
            tenantDetRequest.setDeploymentModel(String.valueOf(obj[23]));
            tenantDetRequest.setServiceStatus((Boolean) obj[24]);
            tenantDetRequestList.add(tenantDetRequest);
        }
        return tenantDetRequestList;
    }
}
