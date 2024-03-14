package com.cognicx.AppointmentRemainder.dao;

import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.response.TenantDetResponse;

import java.util.List;

public interface TenantDao {
    boolean updateTenantDet(TenantDetRequest tenantDetRequest);

    TenantDetResponse createTenant(TenantDetRequest tenantDetRequest);

    List<Object[]> getTenantDet();
}
