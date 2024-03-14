package com.cognicx.AppointmentRemainder.constant;

public class TenantQueryConstant {

//    public static final String WRONG_LATER_USE = "INSERT INTO license_generate_values (tenantId, crmIntegration, sipGateway, smsGateway, reports, voiceMail, callRecording, agentDesktop, agentPopup, emailGateway, ttsEngine, multiTimeZone, multiLevelIvr, dashboard) VALUES (:tenantId, :crmIntegration, :sipGateway, :smsGateway, :reports, :voiceMail, :callRecording, :agentDesktop, :agentPopup, :emailGateway, :ttsEngine, :multiTimeZone, :multiLevelIvr, :dashboard)";
    public static final String UPDATE_TENANT_DETAILS = "UPDATE appointment_remainder.tenant_det " +
            "SET tenantName = :tenantName, loginUrl = :loginUrl, adminUser = :adminUser, " +
            "password = :password, address = :address, contactPerson = :contactPerson, " +
            "contactNumber = :contactNumber, contactEmail = :contactEmail, partnerId = :partnerId, " +
            "partnerName = :partnerName, partnerEmail = :partnerEmail, onBoarding = :onBoarding, " +
            "startContract = :startContract, endContract = :endContract, billedTo = :billedTo, " +
            "billedCycle = :billedCycle, paymentTerms = :paymentTerms, concurrency = :concurrency, " +
            "noOflines = :noOflines, noOfUsers = :noOfUsers, licenseKey = :licenseKey, " +
            "deploymentModel = :deploymentModel, serviceStatus = :serviceStatus " +
            "WHERE tenantId = :tenantId";
    public static final String INSERT_TENANT_DETAILS = "INSERT INTO appointment_remainder.tenant_det "
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

    public static final String GET_TENANT_DETAILS = "SELECT tenant_auto_id, tenantId, tenantName, loginUrl, adminUser, password, address, contactPerson, contactNumber, contactEmail, partnerId, partnerName, partnerEmail, onBoarding, startContract, endContract, billedTo, billedCycle, paymentTerms, concurrency, noOflines, noOfUsers, licenseKey, deploymentModel, serviceStatus FROM appointment_remainder.tenant_det";

    public static final String UPDATE_LICENSE_KEY = "UPDATE appointment_remainder.tenant_det SET licenseKey =:licenseKey WHERE tenantId =:tenantId";
    public static final String CHECK_TENANT_IS_PRESENT_OR_NOT = "SELECT COUNT(*) FROM appointment_remainder.tenant_det WHERE tenantId = :tenantId";

    public static final String GENERATE_LICENSE_VALUES = "SELECT " +
            "ISNULL((SELECT crmIntegrationValue FROM dbo.generate_license_values WHERE crmIntegration = ?), '') AS crmIntegrationValue, " +
            "ISNULL((SELECT sipGatewayValue FROM dbo.generate_license_values WHERE sipGateway = ?), '') AS sipGatewayValue, " +
            "ISNULL((SELECT smsGatewayValue FROM dbo.generate_license_values WHERE smsGateway = ?), '') AS smsGatewayValue, " +
            "ISNULL((SELECT reportsValue FROM dbo.generate_license_values WHERE reports = ?), '') AS reportsValue, " +
            "ISNULL((SELECT voiceMailValue FROM dbo.generate_license_values WHERE voiceMail = ?), '') AS voiceMailValue, " +
            "ISNULL((SELECT callRecordingValue FROM dbo.generate_license_values WHERE callRecording = ?), '') AS callRecordingValue, " +
            "ISNULL((SELECT agentDesktopValue FROM dbo.generate_license_values WHERE agentDesktop = ?), '') AS agentDesktopValue, " +
            "ISNULL((SELECT agentPopupValue FROM dbo.generate_license_values WHERE agentPopup = ?), '') AS agentPopupValue, " +
            "ISNULL((SELECT emailGatewayValue FROM dbo.generate_license_values WHERE emailGateway = ?), '') AS emailGatewayValue, " +
            "ISNULL((SELECT ttsEngineValue FROM dbo.generate_license_values WHERE ttsEngine = ?), '') AS ttsEngineValue, " +
            "ISNULL((SELECT multiTimeZoneValue FROM dbo.generate_license_values WHERE multiTimeZone = ?), '') AS multiTimeZoneValue, " +
            "ISNULL((SELECT multiLevelIvrValue FROM dbo.generate_license_values WHERE multiLevelIvr = ?), '') AS multiLevelIvrValue, " +
            "ISNULL((SELECT dashboardValue FROM dbo.generate_license_values WHERE dashboard = ?), '') AS dashboardValue " +
            "FROM dbo.generate_license_values";

    public static final String CHANNELS_VALUE = "";

    public static final String DIALING_VALUE = "";
}
