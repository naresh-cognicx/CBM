package com.cognicx.AppointmentRemainder.dao.impl;

import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
import com.cognicx.AppointmentRemainder.constant.CampaignQueryConstant;
import com.cognicx.AppointmentRemainder.dao.TenantDao;
import com.cognicx.AppointmentRemainder.response.TenantDetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("TenantDao")
@Transactional
public class TenantDaoImpl implements TenantDao {

    private static Logger logger = LoggerFactory.getLogger(TenantDaoImpl.class);

    @PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
    public EntityManager firstEntityManager;

    @Override
    public boolean updateTenantDet(TenantDetRequest tenantDetRequest) {
        int rowsAffected;
        try {
            String sql = CampaignQueryConstant.UPDATE_TENANT_DETAILS;
            Query query = firstEntityManager.createNativeQuery(sql);
            query.setParameter("tenantName", tenantDetRequest.getTenantName());
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
            if (rowsAffected > 0) {
                logger.info("Tenant details updated successfully");
                return true;
            } else {
                logger.error("Failed to updated tenant details");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred in CampaignDaoImpl::updateTenantDet" + e);
            return false;
        }
    }

    @Override
    public TenantDetResponse createTenant(TenantDetRequest tenantDetRequest) {
        TenantDetResponse response = new TenantDetResponse();
        int rowsAffected;
        String tenantId = tenantDetRequest.getTenantId();
        int isPresent = checkTenantIsPresentOrNot(tenantId);
        logger.error("Is Present : "+isPresent);
        try {
            if (tenantId != null && (!tenantId.isEmpty()) && (isPresent==0)) {
                Query query = firstEntityManager.createNativeQuery(CampaignQueryConstant.INSERT_TENANT_DETAILS);
                query.setParameter("tenantId", tenantDetRequest.getTenantId());
                query.setParameter("tenantName", tenantDetRequest.getTenantName());
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

                rowsAffected = query.executeUpdate();

                if (rowsAffected > 0) {
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
                response.setTenantName(null);
                response.setMessage("Tenant Id details is already present : " + tenantDetRequest.getTenantId());
                logger.error("Tenant Id details is already present : " + tenantDetRequest.getTenantId());
            }
        } catch (Exception e) {
            logger.error("Error invoked on the create TenantDet : " + e.getMessage());
        }
        return response;
    }

    public List<Object[]> getTenantDet() {
        List<Object[]> resultList = null;
        try {
            Query queryObj = firstEntityManager.createNativeQuery(CampaignQueryConstant.GET_TENANT_DETAILS);
            resultList = queryObj.getResultList();
            logger.info("Result list : " + resultList);
        } catch (Exception e) {
            logger.error("Error occured in TenantDaoImp ::getTenantDet" + e.getMessage());
            return resultList;
        }
        return resultList;
    }
    private int checkTenantIsPresentOrNot(String tenantId) {
        int count = 0; // Initialize count to 0
        try {
            String sql = "SELECT COUNT(*) FROM appointment_remainder.tenant_det WHERE tenantId = :tenantId";
            Query query = firstEntityManager.createNativeQuery(sql);
            query.setParameter("tenantId", tenantId);
            count = ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
            logger.error("Error on checkTenantIsPresentOrNot : " + e.getMessage());
        }
        return count;
    }

}
