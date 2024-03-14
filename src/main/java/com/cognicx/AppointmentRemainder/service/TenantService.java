package com.cognicx.AppointmentRemainder.service;

import com.cognicx.AppointmentRemainder.Request.LicenseRequestDet;
import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import org.springframework.http.ResponseEntity;

public interface TenantService {
    ResponseEntity<GenericResponse> createTenant(TenantDetRequest tenantDetRequest);

    ResponseEntity<GenericResponse> updateTenant(TenantDetRequest tenantDetRequest);

    ResponseEntity<GenericResponse> getTenantList();

    ResponseEntity<GenericResponse> generateLicense(LicenseRequestDet licenseRequestDet);
}
