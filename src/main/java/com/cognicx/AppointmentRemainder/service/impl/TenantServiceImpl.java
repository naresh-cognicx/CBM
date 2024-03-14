package com.cognicx.AppointmentRemainder.service.impl;

import com.cognicx.AppointmentRemainder.Dto.LicenseDto;
import com.cognicx.AppointmentRemainder.Request.LicenseRequestDet;
import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.dao.TenantDao;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.response.LicenseResponse;
import com.cognicx.AppointmentRemainder.response.TenantDetResponse;
import com.cognicx.AppointmentRemainder.service.TenantService;
import com.microsoft.sqlserver.jdbc.SQLServerBlob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    private static final String SECRET_KEY = "NAS NEURON";
    private static final String ALGORITHM = "AES";
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
            logger.error("Error in CampaignServiceImpl::updateTenant " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("Error occured while updating Tenant");
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

    public ResponseEntity<GenericResponse> generateLicense(LicenseRequestDet licenseRequestDet) {
        GenericResponse genericResponse = new GenericResponse();
        LicenseResponse licenseResponses = new LicenseResponse();
        String licenseKey;
        try {
            licenseKey = generateLicenseKey(licenseRequestDet);
            boolean isUpdated = tenantDao.updateLicenseKey(licenseKey, licenseRequestDet.getTenantId());
            licenseResponses.setTenantId(licenseRequestDet.getTenantId());
            licenseResponses.setExpiredate(licenseRequestDet.getExpireDate());
            licenseResponses.setLicenseKey(licenseKey);
            licenseResponses.setCreatedDate(LocalDateTime.now());
            if (isUpdated) {
                genericResponse.setStatus(200);
                genericResponse.setValue(licenseResponses);
                genericResponse.setMessage("Success");
            } else {
                logger.error("Error in TenantServiceImpl::generateLicense ");
                genericResponse.setStatus(400);
                genericResponse.setValue("Failure");
                genericResponse.setMessage("No data Found");
            }
        } catch (Exception e) {
            logger.error("Error in TenantServiceImpl::generateLicense " + e.getMessage());
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }
        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }


    private String generateLicenseKey(LicenseRequestDet licenseRequestDet) {
        String licenseKey = null;
        List<Object[]> values = new ArrayList<>();
        List<LicenseDto> licenseDtos = new ArrayList<>();
        try {
            values = tenantDao.getValuetoGeneratelicense(licenseRequestDet);
            for (Object[] obj : values) {
                LicenseDto licenseDto = new LicenseDto();
                licenseDto.setCrmIntegration(String.valueOf(obj[0]));
                licenseDto.setSipGateway(String.valueOf(obj[1]));
                licenseDto.setSmsGateway(String.valueOf(obj[2]));
                licenseDto.setReports(String.valueOf(obj[3]));
                licenseDto.setVoiceMail(String.valueOf(obj[4]));
                licenseDto.setCallRecording(String.valueOf(obj[5]));
                licenseDto.setAgentDesktop(String.valueOf(obj[6]));
                licenseDto.setAgentPopup(String.valueOf(obj[7]));
                licenseDto.setEmailGateway(String.valueOf(obj[8]));
                licenseDto.setTtsEngine(String.valueOf(obj[9]));
                licenseDto.setMultiTimeZone(String.valueOf(obj[10]));
                licenseDto.setMultiLevelIvr(String.valueOf(obj[11]));
                licenseDto.setDashboard(String.valueOf(obj[12]));
                licenseDtos.add(licenseDto);
            }
            logger.info("License values for generate : " + licenseDtos.get(0));
            licenseKey = createLicenseKey(licenseDtos.get(0), licenseRequestDet.getTenantId(), licenseRequestDet.getExpireDate());

//            System.out.println("Decrypt the license key : "+decryptLicenseKey(licenseKey));
        } catch (Exception e) {
            logger.error("Error on generateLicenseKey()" + e.getMessage());
        }
        return licenseKey;
    }

    private String createLicenseKey(LicenseDto values, String tenantId, LocalDateTime expireDate) throws Exception {
        String key = "";
        try {
            String data = values.toString() + tenantId + expireDate.toString();
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(),ALGORITHM);
            String encryptedData = encrypt(data);
            String hash = generateHash(data);
            key = encryptedData + hash;
        } catch (Exception e) {
            throw e;
        }
        return key;
    }

    private static String encrypt(String data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String generateHash(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
