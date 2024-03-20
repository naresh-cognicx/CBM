package com.cognicx.AppointmentRemainder.service.impl;

import com.cognicx.AppointmentRemainder.Dto.LicenseDto;
import com.cognicx.AppointmentRemainder.Request.LicenseRequestDet;
import com.cognicx.AppointmentRemainder.Request.SipGatewayRequestDet;
import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.Request.ValidRequest;
import com.cognicx.AppointmentRemainder.dao.TenantDao;
import com.cognicx.AppointmentRemainder.response.*;
import com.cognicx.AppointmentRemainder.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
            logger.error("Error in TenantServiceImpl::createTenant " + e);
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
            logger.error("Error in TenantServiceImpl::updateTenant " + e);
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
            logger.error("Error in TenantServiceImpl::getTenantList " + e);
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
            LocalDateTime expireDate = tenantDao.getExpireDate(licenseRequestDet.getTenantId());
            logger.error("Expire : " + expireDate);
            licenseKey = generateLicenseKey(licenseRequestDet, expireDate);
            boolean isUpdated = tenantDao.updateLicenseKey(licenseKey, licenseRequestDet.getTenantId());
            licenseResponses.setTenantId(licenseRequestDet.getTenantId());
            licenseResponses.setExpiredate(expireDate);
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

    @Override
    public ResponseEntity<GenericResponse> createSipGateWayDetail(SipGatewayRequestDet sipGatewayRequestDet) {
        GenericResponse genericResponse = new GenericResponse();
        SipGatewayResponse sipGatewayResponse = new SipGatewayResponse();
        try {
            sipGatewayResponse = tenantDao.createSipGateWayDetail(sipGatewayRequestDet);
            genericResponse.setStatus(200);
            genericResponse.setValue(sipGatewayResponse);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in TenantServiceImpl::SipGateWayDetail " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue(sipGatewayResponse);
            genericResponse.setMessage("error on while creating Sip GateWay Detail");
        }
        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> updateSipGateWayDetail(SipGatewayRequestDet sipGatewayRequestDet) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            boolean isUpdated = tenantDao.updateSipGateWayDet(sipGatewayRequestDet);
            if (isUpdated) {
                genericResponse.setStatus(200);
                genericResponse.setValue("Success");
                genericResponse.setMessage("Sip GateWay details updated successfully");
            } else {
                genericResponse.setStatus(400);
                genericResponse.setValue("Failure");
                genericResponse.setMessage("Error occured while updating Sip GateWay details");
            }
        } catch (Exception e) {
            logger.error("Error in TenantServiceImpl::updateSipGateWayDetail " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("Error occured while updateSipGateWayDetail ");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> getSipGateWayDetailList() {
        GenericResponse genericResponse = new GenericResponse();
        List<SipGatewayRequestDet> sipGatewayRequestDetList = null;
        try {
            sipGatewayRequestDetList = getSipGateWayList();
            genericResponse.setStatus(200);
            genericResponse.setValue(sipGatewayRequestDetList);
            genericResponse.setMessage("Success");
        } catch (Exception e) {
            logger.error("Error in TenantServiceImpl::getSipGateWayDetailList " + e);
            genericResponse.setStatus(400);
            genericResponse.setValue("Failure");
            genericResponse.setMessage("No data Found");
        }

        return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> validLicenseKey(ValidRequest request) {
        GenericResponse response = new GenericResponse();
        String value;
        try {
            value = decrypt(request.getLicenseKey());
            if (value.isEmpty()) {
                response.setValue("Error on decrypt");
                response.setPath("/tenant/validLicenseKey");
                response.setMessage("Error on Decryption license key");
                response.setStatus(400);
            }else {
                LicenseValidResponse licenseValidResponse = checkTheValueAndReturn(request.getTenantId(), value);
                response.setValue(licenseValidResponse);
                response.setPath("/tenant/validLicenseKey");
                response.setMessage("Valid License Key for this tenantId : " + request.getTenantId());
                response.setStatus(200);
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
            logger.error("Error on valid license key : "+e.getMessage());
        }
        return new ResponseEntity<GenericResponse>(new GenericResponse(response), HttpStatus.OK);
    }

    private LicenseValidResponse checkTheValueAndReturn(String tenantId, String value) {
        LicenseValidResponse response  = new LicenseValidResponse();
        if (value.contains(tenantId)) {
            response.setTenantId(tenantId);
            if (value.contains("sipGatewayValue")) {
                response.setSipGateway(true);
            }
            LocalDateTime expireDateTime = tenantDao.getExpireDate(tenantId);
            if (value.contains(expireDateTime.toString())) {
                response.setExpireDate(expireDateTime);
                LocalDateTime currentDateTime = LocalDateTime.now();
                if (currentDateTime.isAfter(expireDateTime)) {
                    logger.error("Error: Expiry date reached. Current date: " + currentDateTime + ", Expiry date: " + expireDateTime);
                } else {
                    logger.info("Expiry date not reached yet. Current date: " + currentDateTime + ", Expiry date: " + expireDateTime);
                }
            }
        }else {
            logger.error("Tenant Id is not found in licenseKey");
        }
        return response;
    }

    private List<SipGatewayRequestDet> getSipGateWayList() {
        List<SipGatewayRequestDet> list = new ArrayList<>();
        List<Object[]> sipObjects = tenantDao.getSipGatewayDet();
        for (Object[] obj : sipObjects) {
            SipGatewayRequestDet sipGatewayRequestDet = new SipGatewayRequestDet();
            sipGatewayRequestDet.setSipGatewayId(String.valueOf(obj[0]));
            sipGatewayRequestDet.setSipGatewayName(String.valueOf(obj[1]));
            sipGatewayRequestDet.setServiceProviderName(String.valueOf(obj[2]));
            sipGatewayRequestDet.setIpAddress(String.valueOf(obj[3]));
            sipGatewayRequestDet.setSipUserName(String.valueOf(obj[4]));
            sipGatewayRequestDet.setSipUserPassword(String.valueOf(obj[5]));
            sipGatewayRequestDet.setAuthenticateFromUser(String.valueOf(obj[6]));
            sipGatewayRequestDet.setAuthenticateFromDomain(String.valueOf(obj[7]));
            sipGatewayRequestDet.setRegistrationUserName(String.valueOf(obj[8]));
            sipGatewayRequestDet.setInSecure(String.valueOf(obj[9]));
            sipGatewayRequestDet.setQualify(String.valueOf(obj[10]));
            sipGatewayRequestDet.setDtmfMode(String.valueOf(obj[11]));
            sipGatewayRequestDet.setDialPlanEntry(String.valueOf(obj[12]));
            sipGatewayRequestDet.setAllow(String.valueOf(obj[13]));
            sipGatewayRequestDet.setDisAllow(String.valueOf(obj[14]));
            sipGatewayRequestDet.setStatus(String.valueOf(obj[15]));
            list.add(sipGatewayRequestDet);
        }
        return list;
    }


    private String generateLicenseKey(LicenseRequestDet licenseRequestDet, LocalDateTime expireDate) {
        String licenseKey = null;
        List<Object[]> values = new ArrayList<>();
        List<LicenseDto> licenseDtos = new ArrayList<>();
        try {
            values = tenantDao.getValuetoGeneratelicense(licenseRequestDet);
            for (Object[] obj : values) {
                LicenseDto licenseDto = setLicenseDto(obj);
                licenseDtos.add(licenseDto);
            }
            logger.info("License values for generate : " + licenseDtos.get(0));
            licenseKey = createLicenseKey(licenseDtos.get(0), licenseRequestDet.getTenantId(), expireDate);
        } catch (Exception e) {
            logger.error("Error on generateLicenseKey()" + e.getMessage());
        }
        return licenseKey;
    }

    private static LicenseDto setLicenseDto(Object[] obj) {
        LicenseDto licenseDto = new LicenseDto();
        try {
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
        } catch (Exception e) {
            logger.error("error on setLicenseDto :" + e.getMessage());
        }
        return licenseDto;
    }

    public String createLicenseKey(LicenseDto values, String tenantId, LocalDateTime expireDate) throws Exception {
        StringBuilder combinedFields = new StringBuilder();
        appendFieldIfNotEmpty(combinedFields, values.getCrmIntegration());
        appendFieldIfNotEmpty(combinedFields, values.getSipGateway());
        appendFieldIfNotEmpty(combinedFields, values.getSmsGateway());
        appendFieldIfNotEmpty(combinedFields, values.getReports());
        appendFieldIfNotEmpty(combinedFields, values.getVoiceMail());
        appendFieldIfNotEmpty(combinedFields, values.getCallRecording());
        appendFieldIfNotEmpty(combinedFields, values.getAgentDesktop());
        appendFieldIfNotEmpty(combinedFields, values.getAgentPopup());
        appendFieldIfNotEmpty(combinedFields, values.getEmailGateway());
        appendFieldIfNotEmpty(combinedFields, values.getTtsEngine());
        appendFieldIfNotEmpty(combinedFields, values.getMultiTimeZone());
        appendFieldIfNotEmpty(combinedFields, values.getMultiLevelIvr());
        appendFieldIfNotEmpty(combinedFields, values.getDashboard());
        combinedFields.append("_" + tenantId);
        combinedFields.append("_" + expireDate);
        logger.error("Value : " + combinedFields);
        initFromStrings();
        String value = combinedFields.toString();
        String key = encrypt(combinedFields.toString());
        logger.error("format:" + formatLicenseKey(key));
        return key;
    }

    private static void appendFieldIfNotEmpty(StringBuilder combinedFields, String fieldValue) {
        if (fieldValue != null && !fieldValue.isEmpty()) {
            logger.error("Values : " + fieldValue);
            combinedFields.append(fieldValue);
        }
    }

    private static String formatLicenseKey(String licenseKey) {
        StringBuilder formattedKey = new StringBuilder();
        formattedKey.append(licenseKey, 0, 5).append("-");
        formattedKey.append(licenseKey, 5, 10).append("-");
        formattedKey.append(licenseKey, 10, 15).append("-");
        formattedKey.append(licenseKey, 15, 20).append("-");
        formattedKey.append(licenseKey, 20, 25);
        return formattedKey.toString();
    }

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private static final String PRIVATE_KEY_STRING = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJhBgzcXBm5A0srvFFu4FsBy+LLW+X0sH/9RvP40VIGOCusY0/CqA65YXWqyQE5jQCegBmnAeVYSvK+3PU4Y1fmr1uiquE6sZB5sl96T0ka+PKzPf4oKoAi6nwLUSenj5xTFjLsFGiuMXrCpMCPImf9JBVk89TJV43Xs3DSNKoj1AgMBAAECgYBsDysCgVv2ChnRH4eSZP/4zGCIBR0C4rs+6RM6U4eaf2ZuXqulBfUg2uRKIoKTX8ubk+6ZRZqYJSo3h9SBxgyuUrTehhOqmkMDo/oa9v7aUqAKw/uoaZKHlj+3p4L3EK0ZBpz8jjs/PXJc77Lk9ZKOUY+T0AW2Fz4syMaQOiETzQJBANF5q1lntAXN2TUWkzgir+H66HyyOpMu4meaSiktU8HWmKHa0tSB/v7LTfctnMjAbrcXywmb4ddixOgJLlAjEncCQQC6Enf3gfhEEgZTEz7WG9ev/M6hym4C+FhYKbDwk+PVLMVR7sBAtfPkiHVTVAqC082E1buZMzSKWHKAQzFL7o7zAkBye0VLOmLnnSWtXuYcktB+92qh46IhmEkCCA+py2zwDgEiy/3XSCh9Rc0ZXqNGD+0yQV2kpb3awc8NZR8bit9nAkBo4TgVnoCdfbtq4BIvBQqR++FMeJmBuxGwv+8n63QkGFQwVm6vCuAqFHBtQ5WZIGFbWk2fkKkwwaHogfcrYY/ZAkEAm5ibtJx/jZdPEF9VknswFTDJl9xjIfbwtUb6GDMc0KH7v+QTBW4GsHwt/gL+kGvLOLcEdLL5rau3IC7EQT0ZYg==";
    private static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYQYM3FwZuQNLK7xRbuBbAcviy1vl9LB//Ubz+NFSBjgrrGNPwqgOuWF1qskBOY0AnoAZpwHlWEryvtz1OGNX5q9boqrhOrGQebJfek9JGvjysz3+KCqAIup8C1Enp4+cUxYy7BRorjF6wqTAjyJn/SQVZPPUyVeN17Nw0jSqI9QIDAQAB";


    public void init() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ignored) {
        }
    }

    public void initFromStrings() {
        try {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        } catch (Exception ignored) {
        }
    }


    public void printKeys() {
        System.err.println("Public key\n" + encode(publicKey.getEncoded()));
        System.err.println("Private key\n" + encode(privateKey.getEncoded()));
    }

    public String encrypt(String message) {
        try {
            if (publicKey == null) {
                throw new Exception("Public key is null.");
            }

            logger.info("Message to encrypt: " + message);

            byte[] messageToBytes = message.getBytes();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(messageToBytes);

            return encode(encryptedBytes);
        } catch (Exception e) {
            logger.error("Error during encryption: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return null; // Or handle the error appropriately
        }
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String decrypt(String encryptedMessage) {
        try {
            initFromStrings();
            if (privateKey == null) {
                throw new Exception("Private key is null.");
            }

            byte[] encryptedBytes = decode(encryptedMessage);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedMessage = cipher.doFinal(encryptedBytes);

            return new String(decryptedMessage, "UTF8");
        } catch (Exception e) {
            logger.error("Error during decryption: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return null; // Or handle the error appropriately
        }
    }
}