package com.cognicx.AppointmentRemainder.Request;

public class SipGatewayRequestDet {
    private String sipGatewayId;

    private String sipGatewayName;

    private String serviceProviderName;

    private String ipAddress;

    private String sipUserName;

    private String sipUserPassword;

    private String authenticateFromUser;

    private String authenticateFromDomain;

    private String registrationUserName;

    private String inSecure;

    private String qualify;

    private String dtmfMode;

    private String dialPlanEntry;

    private String allow;

    private String disAllow;

    private String status;

    public String getSipGatewayId() {
        return sipGatewayId;
    }

    public void setSipGatewayId(String sipGatewayId) {
        this.sipGatewayId = sipGatewayId;
    }

    public String getSipGatewayName() {
        return sipGatewayName;
    }

    public void setSipGatewayName(String sipGatewayName) {
        this.sipGatewayName = sipGatewayName;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSipUserName() {
        return sipUserName;
    }

    public void setSipUserName(String sipUserName) {
        this.sipUserName = sipUserName;
    }

    public String getSipUserPassword() {
        return sipUserPassword;
    }

    public void setSipUserPassword(String sipUserPassword) {
        this.sipUserPassword = sipUserPassword;
    }

    public String getAuthenticateFromUser() {
        return authenticateFromUser;
    }

    public void setAuthenticateFromUser(String authenticateFromUser) {
        this.authenticateFromUser = authenticateFromUser;
    }

    public String getAuthenticateFromDomain() {
        return authenticateFromDomain;
    }

    public void setAuthenticateFromDomain(String authenticateFromDomain) {
        this.authenticateFromDomain = authenticateFromDomain;
    }

    public String getRegistrationUserName() {
        return registrationUserName;
    }

    public void setRegistrationUserName(String registrationUserName) {
        this.registrationUserName = registrationUserName;
    }

    public String getInSecure() {
        return inSecure;
    }

    public void setInSecure(String inSecure) {
        this.inSecure = inSecure;
    }

    public String getQualify() {
        return qualify;
    }

    public void setQualify(String qualify) {
        this.qualify = qualify;
    }

    public String getDtmfMode() {
        return dtmfMode;
    }

    public void setDtmfMode(String dtmfMode) {
        this.dtmfMode = dtmfMode;
    }

    public String getDialPlanEntry() {
        return dialPlanEntry;
    }

    public void setDialPlanEntry(String dialPlanEntry) {
        this.dialPlanEntry = dialPlanEntry;
    }

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public String getDisAllow() {
        return disAllow;
    }

    public void setDisAllow(String disAllow) {
        this.disAllow = disAllow;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
