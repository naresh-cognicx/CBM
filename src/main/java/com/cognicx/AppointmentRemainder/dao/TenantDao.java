package com.cognicx.AppointmentRemainder.dao;

import com.cognicx.AppointmentRemainder.Request.LicenseRequestDet;
import com.cognicx.AppointmentRemainder.Request.SipGatewayRequestDet;
import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.response.SipGatewayResponse;
import com.cognicx.AppointmentRemainder.response.TenantDetResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TenantDao {
    boolean updateTenantDet(TenantDetRequest tenantDetRequest);

    TenantDetResponse createTenant(TenantDetRequest tenantDetRequest);

    List<Object[]> getTenantDet();

    boolean updateLicenseKey(String licenseKey, String tenantId);

    List<Object[]> getValuetoGeneratelicense(LicenseRequestDet licenseRequestDet);

    List<Object[]> getSipGatewayDet();

    SipGatewayResponse createSipGateWayDetail(SipGatewayRequestDet sipGatewayRequestDet);

    boolean updateSipGateWayDet(SipGatewayRequestDet sipGatewayRequestDet);

    LocalDateTime getExpireDate(String tenantId);
}
