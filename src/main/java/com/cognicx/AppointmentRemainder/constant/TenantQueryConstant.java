package com.cognicx.AppointmentRemainder.constant;

public class TenantQueryConstant {

//    public static final String WRONG_LATER_USE = "INSERT INTO license_generate_values (tenantId, crmIntegration, sipGateway, smsGateway, reports, voiceMail, callRecording, agentDesktop, agentPopup, emailGateway, ttsEngine, multiTimeZone, multiLevelIvr, dashboard) VALUES (:tenantId, :crmIntegration, :sipGateway, :smsGateway, :reports, :voiceMail, :callRecording, :agentDesktop, :agentPopup, :emailGateway, :ttsEngine, :multiTimeZone, :multiLevelIvr, :dashboard)";
    public static final String UPDATE_TENANT_DETAILS = "UPDATE cbm_tenant_management_db.tenant_management.tenant_det " +
            "SET tenantName = :tenantName, loginUrl = :loginUrl, adminUser = :adminUser, " +
            "password = :password, address = :address, contactPerson = :contactPerson, " +
            "contactNumber = :contactNumber, contactEmail = :contactEmail, partnerId = :partnerId, " +
            "partnerName = :partnerName, partnerEmail = :partnerEmail, onBoarding = :onBoarding, " +
            "startContract = :startContract, endContract = :endContract, billedTo = :billedTo, " +
            "billedCycle = :billedCycle, paymentTerms = :paymentTerms, concurrency = :concurrency, " +
            "noOflines = :noOflines, noOfUsers = :noOfUsers, licenseKey = :licenseKey, " +
            "deploymentModel = :deploymentModel, serviceStatus = :serviceStatus " +
            "WHERE tenantId = :tenantId";
    public static final String INSERT_TENANT_DETAILS = "INSERT INTO cbm_tenant_management_db.tenant_management.tenant_det "
            + "(tenantId, tenantName, loginUrl, adminUser, password, address, "
            + "contactPerson, contactNumber, contactEmail, partnerId, partnerName, "
            + "partnerEmail, onBoarding, startContract, endContract, billedTo, "
            + "billedCycle, paymentTerms, concurrency, noOflines, noOfUsers, "
            + "licenseKey, deploymentModel, serviceStatus) "
            + "VALUES "
            + "(:tenantId, :tenantName, :loginUrl, :adminUser, :password, :address, "
            + ":contactPerson, :contactNumber, :contactEmail, :partnerId, :partnerName, "
            + ":partnerEmail, :onBoarding, :startContract, :endContract, :billedTo, "
            + ":billedCycle, :paymentTerms, :concurrency, :noOflines, :noOfUsers, "
            + ":licenseKey, :deploymentModel, :serviceStatus)";

    public static final String GET_TENANT_DETAILS = "SELECT tenant_auto_id, tenantId, tenantName, loginUrl, adminUser, password, address, contactPerson, contactNumber, contactEmail, partnerId, partnerName, partnerEmail, onBoarding, startContract, endContract, billedTo, billedCycle, paymentTerms, concurrency, noOflines, noOfUsers, licenseKey, deploymentModel, serviceStatus FROM cbm_tenant_management_db.tenant_management.tenant_det";

    public static final String UPDATE_LICENSE_KEY = "UPDATE cbm_tenant_management_db.tenant_management.tenant_det SET licenseKey =:licenseKey WHERE tenantId =:tenantId";
    public static final String CHECK_TENANT_IS_PRESENT_OR_NOT = "SELECT COUNT(*) FROM cbm_tenant_management_db.tenant_management.tenant_det WHERE tenantId = :tenantId";

    public static final String GENERATE_LICENSE_VALUES = "SELECT " +
            "ISNULL((SELECT crmIntegrationValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE crmIntegration = ?), '') AS crmIntegrationValue, " +
            "ISNULL((SELECT sipGatewayValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE sipGateway = ?), '') AS sipGatewayValue, " +
            "ISNULL((SELECT smsGatewayValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE smsGateway = ?), '') AS smsGatewayValue, " +
            "ISNULL((SELECT reportsValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE reports = ?), '') AS reportsValue, " +
            "ISNULL((SELECT voiceMailValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE voiceMail = ?), '') AS voiceMailValue, " +
            "ISNULL((SELECT callRecordingValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE callRecording = ?), '') AS callRecordingValue, " +
            "ISNULL((SELECT agentDesktopValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE agentDesktop = ?), '') AS agentDesktopValue, " +
            "ISNULL((SELECT agentPopupValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE agentPopup = ?), '') AS agentPopupValue, " +
            "ISNULL((SELECT emailGatewayValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE emailGateway = ?), '') AS emailGatewayValue, " +
            "ISNULL((SELECT ttsEngineValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE ttsEngine = ?), '') AS ttsEngineValue, " +
            "ISNULL((SELECT multiTimeZoneValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE multiTimeZone = ?), '') AS multiTimeZoneValue, " +
            "ISNULL((SELECT multiLevelIvrValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE multiLevelIvr = ?), '') AS multiLevelIvrValue, " +
            "ISNULL((SELECT dashboardValue FROM cbm_tenant_management_db.tenant_management.generate_license_values WHERE dashboard = ?), '') AS dashboardValue " +
            "FROM cbm_tenant_management_db.tenant_management.generate_license_values";

    public static final String CHANNELS_VALUE = "";

    public static final String DIALING_VALUE = "";

    public static final String GET_SIP_GATEWAY_DETAILS = "SELECT sipGatewayId, sipGatewayName, serviceProviderName, ipAddress, sipUserName, sipUserPassword, authenticateFromUser, authenticateFromDomain, registrationUserName, inSecure, qualify, dtmfMode, dialPlanEntry, allow, disAllow, status FROM cbm_tenant_management_db.tenant_management.SIPGateway";

    public static final String INSERT_SIP_GATEWAY_DETAILS =
            "INSERT INTO cbm_tenant_management_db.tenant_management.SIPGateway " +
                    "(sipGatewayId, sipGatewayName, serviceProviderName, ipAddress, sipUserName, sipUserPassword, " +
                    "authenticateFromUser, authenticateFromDomain, registrationUserName, inSecure, qualify, dtmfMode, " +
                    "dialPlanEntry, allow, disAllow, status) " +
                    "VALUES " +
                    "(:sipGatewayId, :sipGatewayName, :serviceProviderName, :ipAddress, :sipUserName, :sipUserPassword, " +
                    ":authenticateFromUser, :authenticateFromDomain, :registrationUserName, :inSecure, :qualify, :dtmfMode, " +
                    ":dialPlanEntry, :allow, :disAllow, :status)";

    public static final String UPDATE_SIP_GATEWAY_DETAILS =
            "UPDATE cbm_tenant_management_db.tenant_management.SIPGateway " +
                    "SET sipGatewayName = :sipGatewayName, " +
                    "serviceProviderName = :serviceProviderName, " +
                    "ipAddress = :ipAddress, " +
                    "sipUserName = :sipUserName, " +
                    "sipUserPassword = :sipUserPassword, " +
                    "authenticateFromUser = :authenticateFromUser, " +
                    "authenticateFromDomain = :authenticateFromDomain, " +
                    "registrationUserName = :registrationUserName, " +
                    "inSecure = :inSecure, " +
                    "qualify = :qualify, " +
                    "dtmfMode = :dtmfMode, " +
                    "dialPlanEntry = :dialPlanEntry, " +
                    "allow = :allow, " +
                    "disAllow = :disAllow, " +
                    "status = :status " +
                    "WHERE sipGatewayId = :sipGatewayId";


    public static final String INSERT_TENANT_USER_PASSWORD  = "insert into appointment_remainder.usermanagement_det(userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup) values(:userKey,:contactPerson,:tenantId,:contactEmail,:contactNumber,:adminUser,:password,:UserRole,'','','','')";

    public static final String UPDATE_TENANT_USER_PASSWORD = "UPDATE appointment_remainder.usermanagement_det SET FirstName = :contactPerson,EmailId = :contactEmail,MobNum = :contactNumber,UserId = :adminUser,UserPassword = :password, WHERE userKey = :tenantId";
    public static final String GET_TENANT_USER_PASSWORD = "";

    public static final String INSERT_USER_DET = "insert into appointment_remainder.usermanagement_det(userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup) values (:userKey,:FirstName,:LastName,:EmailId,:MobNum,:UserId,:UserPassword,:UserRole,:PBXExtn,:SkillSet,:Agent,:userGroup)";

    public static final String GET_USER_DET = "SELECT userKey,FirstName,LastName,EmailId,MobNum,UserId,UserPassword,UserRole,PBXExtn,SkillSet,Agent,userGroup from appointment_remainder.usermanagement_det";

    public static final String UPDATE_USER_DET = "UPDATE appointment_remainder.usermanagement_det SET FirstName = :FirstName,LastName = :LastName,EmailId = :EmailId,MobNum = :MobNum,UserId = :UserId,UserPassword = :UserPassword,UserRole = :UserRole,PBXExtn = :PBXExtn,SkillSet = :SkillSet, Agent=:Agent WHERE userKey = :userKey";

    public static final String GET_TENANT_EXPIRE_DATE = "SELECT TOP 1 endContract FROM cbm_tenant_management_db.tenant_management.tenant_det WHERE tenantId =:tenantId";
}
