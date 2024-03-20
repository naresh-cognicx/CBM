package com.cognicx.AppointmentRemainder.dao.impl;

import com.cognicx.AppointmentRemainder.Request.LicenseRequestDet;
import com.cognicx.AppointmentRemainder.Request.SipGatewayRequestDet;
import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.TenantQueryConstant;
import com.cognicx.AppointmentRemainder.dao.TenantDao;
import com.cognicx.AppointmentRemainder.response.SipGatewayResponse;
import com.cognicx.AppointmentRemainder.response.TenantDetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository("TenantDao")
@Transactional
public class TenantDaoImpl implements TenantDao {

    private static Logger logger = LoggerFactory.getLogger(TenantDaoImpl.class);

    @Value("${tenant-login-url}")
    private String LOGIN_URL;

    @Qualifier(ApplicationConstant.FIRST_ENTITY_MANAGER)
    @PersistenceContext(name = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
    public final EntityManager firstEntityManager;


    @Qualifier(ApplicationConstant.TENANT_ENTITY_MANAGER)
    @PersistenceContext(name = ApplicationConstant.TENANT_PERSISTENCE_UNIT_NAME)
    public final EntityManager tenantEntityManager;

    public TenantDaoImpl(@Qualifier(ApplicationConstant.FIRST_ENTITY_MANAGER) EntityManager firstEntityManager, @Qualifier(ApplicationConstant.TENANT_ENTITY_MANAGER) EntityManager tenantEntityManager) {
        this.firstEntityManager = firstEntityManager;
        this.tenantEntityManager = tenantEntityManager;
    }

    @Autowired
    public UserManagementDaoImpl userManagementDao;


    @Override
    public boolean updateTenantDet(TenantDetRequest tenantDetRequest) {
        int rowsAffected;
        try {
            String sql = TenantQueryConstant.UPDATE_TENANT_DETAILS;
            String sql1 = TenantQueryConstant.UPDATE_TENANT_USER_PASSWORD;
            Query query = tenantEntityManager.createNativeQuery(sql);
            query.setParameter("tenantName", tenantDetRequest.getTenantName());
//            query.setParameter("loginUrl", LOGIN_URL+tenantDetRequest.getLoginUrl());
//            query.setParameter("loginUrl", tenantDetRequest.getLoginUrl()+"/?tenantID="+tenantDetRequest.getTenantId());
            query.setParameter("loginUrl", tenantDetRequest.getLoginUrl());
            query.setParameter("adminUser", tenantDetRequest.getAdminUser());
            query.setParameter("password", tenantDetRequest.getPassword());
            query.setParameter("address", tenantDetRequest.getAddress());
            query.setParameter("contactPerson", tenantDetRequest.getContactPerson());
            query.setParameter("contactNumber", tenantDetRequest.getContactNumber());
            query.setParameter("contactEmail", tenantDetRequest.getContactEmail());
            query.setParameter("partnerId", tenantDetRequest.getPartnerId());
            query.setParameter("partnerName", tenantDetRequest.getPartnerName());
            query.setParameter("partnerEmail", tenantDetRequest.getPartnerEmail());
            query.setParameter("onBoarding", tenantDetRequest.getOnBoarding());
            query.setParameter("startContract", tenantDetRequest.getStartContract());
            query.setParameter("endContract", tenantDetRequest.getEndContract());
            query.setParameter("billedTo", tenantDetRequest.getBilledTo());
            query.setParameter("billedCycle", tenantDetRequest.getBilledCycle());
            query.setParameter("paymentTerms", tenantDetRequest.getPaymentTerms());
            query.setParameter("concurrency", tenantDetRequest.getConcurrency());
            query.setParameter("noOflines", tenantDetRequest.getNoOflines());
            query.setParameter("noOfUsers", tenantDetRequest.getNoOfUsers());
            query.setParameter("licenseKey", tenantDetRequest.getLicenseKey());
            query.setParameter("deploymentModel", tenantDetRequest.getDeploymentModel());
            query.setParameter("serviceStatus", tenantDetRequest.isServiceStatus());
            query.setParameter("tenantId", tenantDetRequest.getTenantId());
            rowsAffected = query.executeUpdate();

            boolean isDone = updateTenantUser(tenantDetRequest);

            if (isDone && rowsAffected > 0) {
                logger.info("Tenant details updated successfully");
                return true;
            } else {
                logger.error("Failed to updated tenant details");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred in tenantDaoImpl::updateTenantDet" + e);
            return false;
        }
    }
    public boolean updateTenantUser(TenantDetRequest tenantDetRequest) {
        int rowsAffected;
        try {
            String sql = TenantQueryConstant.UPDATE_TENANT_USER_PASSWORD;
            Query query = firstEntityManager.createNativeQuery(sql);
//            query.setParameter("loginUrl", LOGIN_URL+tenantDetRequest.getLoginUrl());
//            query.setParameter("loginUrl", tenantDetRequest.getLoginUrl()+"/?tenantID="+tenantDetRequest.getTenantId());

            query.setParameter("contactPerson", tenantDetRequest.getContactPerson());
            query.setParameter("contactEmail", tenantDetRequest.getContactEmail());
            query.setParameter("contactNumber", tenantDetRequest.getContactNumber());
            query.setParameter("adminUser", tenantDetRequest.getAdminUser());
            query.setParameter("password", new BCryptPasswordEncoder().encode(tenantDetRequest.getPassword()));

            query.setParameter("tenantId", tenantDetRequest.getTenantId());
            rowsAffected = query.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Tenant user details updated successfully");
                return true;
            } else {
                logger.error("Failed to updated tenant user details");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred in tenantDaoImpl::updateTenantDet in user" + e);
            return false;
        }
    }


    @Override
    public TenantDetResponse createTenant(TenantDetRequest tenantDetRequest) {
        TenantDetResponse response = new TenantDetResponse();
        int rowsAffected;
        String tenantId = tenantDetRequest.getTenantId();
        int isPresent = checkTenantIsPresentOrNot(tenantId);
        logger.error("Is Present : " + isPresent);

//        try {
//
//        }catch (Exception e)
//        {
//            logger.error(null);
//        }
        try {
            if (tenantId != null && (!tenantId.isEmpty()) && (isPresent == 0)) {
                Query query = tenantEntityManager.createNativeQuery(TenantQueryConstant.INSERT_TENANT_DETAILS);
                query.setParameter("tenantId", tenantDetRequest.getTenantId());
                query.setParameter("tenantName", tenantDetRequest.getTenantName());
                //            query.setParameter("loginUrl", LOGIN_URL+tenantDetRequest.getLoginUrl());
//            query.setParameter("loginUrl", tenantDetRequest.getLoginUrl()+"/?tenantID="+tenantDetRequest.getTenantId());
                query.setParameter("loginUrl", tenantDetRequest.getLoginUrl());
                query.setParameter("adminUser", tenantDetRequest.getAdminUser());
                query.setParameter("password", new BCryptPasswordEncoder().encode(tenantDetRequest.getPassword()));
                query.setParameter("address", tenantDetRequest.getAddress());
                query.setParameter("contactPerson", tenantDetRequest.getContactPerson());
                query.setParameter("contactNumber", tenantDetRequest.getContactNumber());
                query.setParameter("contactEmail", tenantDetRequest.getContactEmail());
                query.setParameter("partnerId", tenantDetRequest.getPartnerId());
                query.setParameter("partnerName", tenantDetRequest.getPartnerName());
                query.setParameter("partnerEmail", tenantDetRequest.getPartnerEmail());
                query.setParameter("onBoarding", tenantDetRequest.getOnBoarding());
                query.setParameter("startContract", tenantDetRequest.getStartContract());
                query.setParameter("endContract", tenantDetRequest.getEndContract());
                query.setParameter("billedTo", tenantDetRequest.getBilledTo());
                query.setParameter("billedCycle", tenantDetRequest.getBilledCycle());
                query.setParameter("paymentTerms", tenantDetRequest.getPaymentTerms());
                query.setParameter("concurrency", tenantDetRequest.getConcurrency());
                query.setParameter("noOflines", tenantDetRequest.getNoOflines());
                query.setParameter("noOfUsers", tenantDetRequest.getNoOfUsers());
                query.setParameter("licenseKey", tenantDetRequest.getLicenseKey());
                query.setParameter("deploymentModel", tenantDetRequest.getDeploymentModel());
                query.setParameter("serviceStatus", tenantDetRequest.isServiceStatus());

                rowsAffected = query.executeUpdate();

                boolean isDone = createTenantUser(tenantDetRequest);
                if (isDone && rowsAffected > 0) {
                    logger.info("Tenant details inserted successfully");
                    response.setTenantId(tenantDetRequest.getTenantId());
                    response.setTenantName(tenantDetRequest.getTenantName());
                    response.setMessage("Tenant details created successfully");
                } else {
                    response.setTenantId(tenantDetRequest.getTenantId());
                    response.setTenantName(tenantDetRequest.getTenantName());
                    response.setMessage("Failed to insert tenant details");
                    logger.error("Failed to insert tenant details");
                }
            } else {
                response.setTenantId(tenantId);
                response.setTenantName(tenantDetRequest.getTenantName());
                response.setMessage("Tenant Id details is empty or invalid : " + tenantDetRequest.getTenantId());
                logger.error("Tenant Id details is empty or invalid : " + tenantDetRequest.getTenantId());
            }
        } catch (Exception e) {
            logger.error("Error invoked on the create TenantDet : " + e.getMessage());
        }
        return response;
    }

    private boolean createTenantUser(TenantDetRequest tenantDetRequest) {
        int rowsAffected;
        String userKey;
        try {
            int idValue = userManagementDao.getUserKey();
            if (idValue > 9)
                userKey = "U_" + String.valueOf(idValue);
            else
                userKey = "U_0" + String.valueOf(idValue);
            String sql = TenantQueryConstant.INSERT_TENANT_USER_PASSWORD;
            Query query = firstEntityManager.createNativeQuery(sql);
//            query.setParameter("loginUrl", LOGIN_URL+tenantDetRequest.getLoginUrl());
//            query.setParameter("loginUrl", tenantDetRequest.getLoginUrl()+"/?tenantID="+tenantDetRequest.getTenantId());
//            (userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup)
//            (:tenantId,:contactPerson,'',:contactEmail,:contactNumber,:adminUser,:password,'','','','',''
            query.setParameter("userKey",userKey);
            query.setParameter("contactPerson", tenantDetRequest.getContactPerson());
            query.setParameter("tenantId",tenantDetRequest.getTenantId());
            query.setParameter("contactEmail", tenantDetRequest.getContactEmail());
            query.setParameter("contactNumber", tenantDetRequest.getContactNumber());
            query.setParameter("adminUser", tenantDetRequest.getAdminUser());
            query.setParameter("password", new BCryptPasswordEncoder().encode(tenantDetRequest.getPassword()));
            query.setParameter("UserRole","Admin");
            query.setParameter("tenantId", tenantDetRequest.getTenantId());
            rowsAffected = query.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Tenant user details insert successfully");
                return true;
            } else {
                logger.error("Failed to insert tenant user details");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred in tenantDaoImpl::createTenantUser in user" + e);
            return false;
        }
    }

    public List<Object[]> getTenantDet() {
        List<Object[]> resultList = null;
        try {
            Query queryObj = tenantEntityManager.createNativeQuery(TenantQueryConstant.GET_TENANT_DETAILS);
            resultList = queryObj.getResultList();
            logger.info("Result list : " + resultList);
        } catch (Exception e) {
            logger.error("Error occured in TenantDaoImp ::getTenantDet" + e.getMessage());
            return resultList;
        }
        return resultList;
    }

    @Override
    public boolean updateLicenseKey(String licenseKey, String tenantId) {
        int rowsAffected;
        try {
            String sql = TenantQueryConstant.UPDATE_LICENSE_KEY;
//            String sql = "UPDATE appointment_remainder.tenant_det SET licenseKey =:licenseKey WHERE tenantId =:tenantId";
            Query query = tenantEntityManager.createNativeQuery(sql);
            query.setParameter("licenseKey", licenseKey);
            query.setParameter("tenantId", tenantId);
            rowsAffected = query.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("license key updated successfully");
                return true;
            } else {
                logger.error("Failed to updated licensekey details");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error license key updating " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object[]> getValuetoGeneratelicense(LicenseRequestDet licenseRequestDet) {
        List<Object[]> list = new ArrayList<>();
        try {
            String sql = TenantQueryConstant.GENERATE_LICENSE_VALUES;
            Query query = tenantEntityManager.createNativeQuery(sql);
            query.setParameter(1,licenseRequestDet.isCrmIntegration());
            query.setParameter(2,licenseRequestDet.isSipGateway());
            query.setParameter(3,licenseRequestDet.isSmsGateway());
            query.setParameter(4,licenseRequestDet.isReports());
            query.setParameter(5,licenseRequestDet.isVoiceMail());
            query.setParameter(6,licenseRequestDet.isCallRecording());
            query.setParameter(7,licenseRequestDet.isAgentDesktop());
            query.setParameter(8,licenseRequestDet.isAgentPopup());
            query.setParameter(9,licenseRequestDet.isEmailGateway());
            query.setParameter(10,licenseRequestDet.isTtsEngine());
            query.setParameter(11,licenseRequestDet.isMultiTimeZone());
            query.setParameter(12,licenseRequestDet.isMultiLevelIvr());
            query.setParameter(13,licenseRequestDet.isDashboard());
            list = query.getResultList();
        }catch (Exception e){
            logger.error("Error on tenant DAO Impl getValuetoGeneratelicense() "+e.getMessage());
        }
        return list;
    }

    private int checkTenantIsPresentOrNot(String tenantId) {
        int count = 0; // Initialize count to 0
        try {
//            String sql = "SELECT COUNT(*) FROM appointment_remainder.tenant_det WHERE tenantId = :tenantId";
            String sql = TenantQueryConstant.CHECK_TENANT_IS_PRESENT_OR_NOT;
            Query query = tenantEntityManager.createNativeQuery(sql);
            query.setParameter("tenantId", tenantId);
            count = ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
            logger.error("Error on checkTenantIsPresentOrNot : " + e.getMessage());
        }
        return count;
    }

    public List<Object[]> getSipGatewayDet() {
        List<Object[]> resultList = null;
        try {
            Query queryObj = tenantEntityManager.createNativeQuery(TenantQueryConstant.GET_SIP_GATEWAY_DETAILS);
            resultList = queryObj.getResultList();
            logger.info("Result list : " + resultList);
        } catch (Exception e) {
            logger.error("Error occurred in TenantDaoImp ::getSipGatewayDet" + e.getMessage());
            return resultList;
        }
        return resultList;
    }

    @Override
    public SipGatewayResponse createSipGateWayDetail(SipGatewayRequestDet sipGatewayRequestDet) {
        SipGatewayResponse response = new SipGatewayResponse();
        int rowsAffected;
        String sipGatewayId = sipGatewayRequestDet.getSipGatewayId();
        try {
            if (sipGatewayId != null && (!sipGatewayId.isEmpty())) {
                Query query = tenantEntityManager.createNativeQuery(TenantQueryConstant.INSERT_SIP_GATEWAY_DETAILS);
                query.setParameter("sipGatewayId", sipGatewayRequestDet.getSipGatewayId());
                query.setParameter("sipGatewayName", sipGatewayRequestDet.getSipGatewayName());
                query.setParameter("serviceProviderName", sipGatewayRequestDet.getServiceProviderName());
                query.setParameter("ipAddress", sipGatewayRequestDet.getIpAddress());
                query.setParameter("sipUserName", sipGatewayRequestDet.getSipUserName());
                query.setParameter("sipUserPassword", sipGatewayRequestDet.getSipUserPassword());
                query.setParameter("authenticateFromUser", sipGatewayRequestDet.getAuthenticateFromUser());
                query.setParameter("authenticateFromDomain", sipGatewayRequestDet.getAuthenticateFromDomain());
                query.setParameter("registrationUserName", sipGatewayRequestDet.getRegistrationUserName());
                query.setParameter("inSecure", sipGatewayRequestDet.getInSecure());
                query.setParameter("qualify", sipGatewayRequestDet.getQualify());
                query.setParameter("dtmfMode", sipGatewayRequestDet.getDtmfMode());
                query.setParameter("dialPlanEntry", sipGatewayRequestDet.getDialPlanEntry());
                query.setParameter("allow", sipGatewayRequestDet.getAllow());
                query.setParameter("disAllow", sipGatewayRequestDet.getDisAllow());
                query.setParameter("status", sipGatewayRequestDet.getStatus());
                rowsAffected = query.executeUpdate();

                if (rowsAffected > 0) {
                    logger.info("sip gateway details inserted successfully");
                    response.setSipGatewayId(sipGatewayRequestDet.getSipGatewayId());
                    response.setSipGatewayName(sipGatewayRequestDet.getSipGatewayName());
                    response.setSipUserName(sipGatewayRequestDet.getSipUserName());
                    response.setMessage("sip gateway details created successfully");
                } else {
                    response.setSipGatewayId(sipGatewayRequestDet.getSipGatewayId());
                    response.setSipGatewayName(sipGatewayRequestDet.getSipGatewayName());
                    response.setSipUserName(sipGatewayRequestDet.getSipUserName());
                    response.setMessage("Failed to insert sip gateway details");
                    logger.error("Failed to insert sip gateway details");
                }
            } else {
                response.setSipGatewayId(sipGatewayId);
                response.setSipGatewayName(sipGatewayRequestDet.getSipGatewayName());
                response.setMessage("Sip gateway Id details is empty or invalid : " + sipGatewayId);
                logger.error("Sip gateway Id details is empty or invalid : " + sipGatewayId);
            }
        } catch (Exception e) {
//            logger.error("Error invoked on the create sip gateway details : " + e.getMessage());
            throw e;
        }
        return response;
    }

    @Override
    public boolean updateSipGateWayDet(SipGatewayRequestDet sipGatewayRequestDet) {
        int rowsAffected;
        try {
            String sql = TenantQueryConstant.UPDATE_SIP_GATEWAY_DETAILS;
//            String sql = "UPDATE appointment_remainder.tenant_det SET licenseKey =:licenseKey WHERE tenantId =:tenantId";
            Query query = tenantEntityManager.createNativeQuery(sql);
            query.setParameter("sipGatewayName", sipGatewayRequestDet.getSipGatewayName());
            query.setParameter("serviceProviderName", sipGatewayRequestDet.getServiceProviderName());
            query.setParameter("ipAddress", sipGatewayRequestDet.getIpAddress());
            query.setParameter("sipUserName", sipGatewayRequestDet.getSipUserName());
            query.setParameter("sipUserPassword", sipGatewayRequestDet.getSipUserPassword());
            query.setParameter("authenticateFromUser", sipGatewayRequestDet.getAuthenticateFromUser());
            query.setParameter("authenticateFromDomain", sipGatewayRequestDet.getAuthenticateFromDomain());
            query.setParameter("registrationUserName", sipGatewayRequestDet.getRegistrationUserName());
            query.setParameter("inSecure", sipGatewayRequestDet.getInSecure());
            query.setParameter("qualify", sipGatewayRequestDet.getQualify());
            query.setParameter("dtmfMode", sipGatewayRequestDet.getDtmfMode());
            query.setParameter("dialPlanEntry", sipGatewayRequestDet.getDialPlanEntry());
            query.setParameter("allow", sipGatewayRequestDet.getAllow());
            query.setParameter("disAllow", sipGatewayRequestDet.getDisAllow());
            query.setParameter("status", sipGatewayRequestDet.getStatus());
            query.setParameter("sipGatewayId", sipGatewayRequestDet.getSipGatewayId());
            rowsAffected = query.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("sip gateway details updated successfully");
                return true;
            } else {
                logger.error("Failed to updated sip gateway details");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error sip gateway details updating " + e.getMessage());
            return false;
        }
    }

    @Override
    public LocalDateTime getExpireDate(String tenantId) {
        LocalDateTime expireDateTime = null; // Initialize to null in case of failure
        try {
            Query queryObj = tenantEntityManager.createNativeQuery(TenantQueryConstant.GET_TENANT_EXPIRE_DATE);
            queryObj.setParameter("tenantId",tenantId);
            Timestamp result = (Timestamp) queryObj.getSingleResult();
            expireDateTime = result.toLocalDateTime(); // Convert Timestamp to LocalDateTime
        } catch (NoResultException e) {
            // Handle no result found
            logger.warn("No expire date found for tenantId: " + tenantId);
        } catch (Exception e) {
            logger.error("Error occurred in TenantDaoImp ::getExpireDate: " + e.getMessage());
        }
        return expireDateTime;
    }
}
