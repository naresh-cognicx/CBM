package com.cognicx.AppointmentRemainder.controller;

import com.cognicx.AppointmentRemainder.Request.LicenseRequestDet;
import com.cognicx.AppointmentRemainder.Request.SipGatewayRequestDet;
import com.cognicx.AppointmentRemainder.Request.TenantDetRequest;
import com.cognicx.AppointmentRemainder.Request.ValidRequest;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private final TenantService tenantService;
    private static Logger logger = LoggerFactory.getLogger(TenantService.class);

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/createTenant")
    public ResponseEntity<GenericResponse> createTenant(@RequestBody TenantDetRequest tenantDetRequest){
        logger.info("Invoked Create Tenant details API");
        return tenantService.createTenant(tenantDetRequest);
    }
    @PostMapping("/updateTenant")
    public ResponseEntity<GenericResponse> updateTenant(@RequestBody TenantDetRequest tenantDetRequest){
        logger.info("Invoked Update Tenant details API");
        return tenantService.updateTenant(tenantDetRequest);
    }

    @GetMapping("/getTenant/list")
    public ResponseEntity<GenericResponse> getTenantList(){
        logger.info("Invoked get list of Tenant details API");
        return tenantService.getTenantList();
    }

    @PostMapping("/generateLicensekey")
    public ResponseEntity<GenericResponse>generateLicense(@RequestBody LicenseRequestDet licenseRequestDet){
        return tenantService.generateLicense(licenseRequestDet);
    }

    @PostMapping("/validLicenseKey")
    public ResponseEntity<GenericResponse> validLicenseKey(@RequestBody ValidRequest validRequest){
        return tenantService.validLicenseKey(validRequest);
    }

    @PostMapping("/createSipGatewayDetails")
    public ResponseEntity<GenericResponse> createSipGateWayDetail(@RequestBody SipGatewayRequestDet sipGatewayRequestDet){
        logger.info("Invoked Create Sip GateWay Detail details API");
        return tenantService.createSipGateWayDetail(sipGatewayRequestDet);
    }
    @PostMapping("/updateSipGatewayDetails")
    public ResponseEntity<GenericResponse> updateSipGateWayDetail(@RequestBody SipGatewayRequestDet sipGatewayRequestDet){
        logger.info("Invoked Update sip gateway details API");
        return tenantService.updateSipGateWayDetail(sipGatewayRequestDet);
    }

    @GetMapping("/getSipGatewayDetails/list")
    public ResponseEntity<GenericResponse> getSipGateWayDetail(){
        logger.info("Invoked get list of Sip Gateway details API");
        return tenantService.getSipGateWayDetailList();
    }
}
