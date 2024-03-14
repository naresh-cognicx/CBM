package com.cognicx.AppointmentRemainder.Request;


import java.util.Date;

public class TenantDetRequest {

    private Integer tenant_autogen_id;
    private String tenantId;
    private String tenantName;
    private String loginUrl;
    private String adminUser;
    private String password;
    private String address;
    private String contactId;

    private String contactPerson;
    private String contactNumber;

    private String contactEmail;
    private String partnerId;
    private String partnerName;

    private String partnerEmail;
    private Date onBoarding;
    private Date startContract;
    private Date endContract;
    private String billedTo;
    private String billedCycle;
    private String paymentTerms;

    private int concurrency;
    private int noOflines;
    private int noOfUsers;
    private String licenseKey;

    private String deploymentModel;

    private boolean serviceStatus;


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public Integer getTenant_autogen_id() {
        return tenant_autogen_id;
    }

    public void setTenant_autogen_id(Integer tenant_autogen_id) {
        this.tenant_autogen_id = tenant_autogen_id;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOnBoarding() {
        return onBoarding;
    }

    public void setOnBoarding(Date onBoarding) {
        this.onBoarding = onBoarding;
    }

    public Date getStartContract() {
        return startContract;
    }

    public void setStartContract(Date startContract) {
        this.startContract = startContract;
    }

    public Date getEndContract() {
        return endContract;
    }

    public void setEndContract(Date endContract) {
        this.endContract = endContract;
    }

    public String getBilledTo() {
        return billedTo;
    }

    public void setBilledTo(String billedTo) {
        this.billedTo = billedTo;
    }

    public String getBilledCycle() {
        return billedCycle;
    }

    public void setBilledCycle(String billedCycle) {
        this.billedCycle = billedCycle;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public int getNoOflines() {
        return noOflines;
    }

    public void setNoOflines(int noOflines) {
        this.noOflines = noOflines;
    }

    public int getNoOfUsers() {
        return noOfUsers;
    }

    public void setNoOfUsers(int noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getDeploymentModel() {
        return deploymentModel;
    }

    public void setDeploymentModel(String deploymentModel) {
        this.deploymentModel = deploymentModel;
    }

    public boolean isServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(boolean serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public void setPartnerEmail(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }

    @Override
    public String toString() {
        return "TenantDetRequest{" +
                "tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                ", adminUser='" + adminUser + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", contactId='" + contactId + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", partnerEmail='" + partnerEmail + '\'' +
                ", onBoarding=" + onBoarding +
                ", startContract=" + startContract +
                ", endContract=" + endContract +
                ", billedTo='" + billedTo + '\'' +
                ", billedCycle='" + billedCycle + '\'' +
                ", paymentTerms='" + paymentTerms + '\'' +
                ", concurrency=" + concurrency +
                ", noOflines=" + noOflines +
                ", noOfUsers=" + noOfUsers +
                ", licenseKey='" + licenseKey + '\'' +
                ", deploymentModel='" + deploymentModel + '\'' +
                ", serviceStatus=" + serviceStatus +
                '}';
    }
}
