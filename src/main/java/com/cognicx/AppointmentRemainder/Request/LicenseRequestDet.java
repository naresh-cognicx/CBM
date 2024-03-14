package com.cognicx.AppointmentRemainder.Request;

import java.time.LocalDateTime;
import java.util.Date;

public class LicenseRequestDet {
    private String tenantId;
    private LocalDateTime expireDate;
    private boolean crmIntegration;
    private boolean sipGateway;
    private boolean smsGateway;
    private boolean reports;
    private boolean voiceMail;
    private boolean callRecording;
    private boolean agentDesktop;
    private boolean agentPopup;

    private boolean emailGateway;

    private boolean ttsEngine;

    private boolean multiTimeZone;

    private boolean multiLevelIvr;

    private boolean dashboard;

//    private Channels channels;
//
//    private DialingMode dialingMode;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isCrmIntegration() {
        return crmIntegration;
    }

    public void setCrmIntegration(boolean crmIntegration) {
        this.crmIntegration = crmIntegration;
    }

    public boolean isSipGateway() {
        return sipGateway;
    }

    public void setSipGateway(boolean sipGateway) {
        this.sipGateway = sipGateway;
    }

    public boolean isSmsGateway() {
        return smsGateway;
    }

    public void setSmsGateway(boolean smsGateway) {
        this.smsGateway = smsGateway;
    }

    public boolean isReports() {
        return reports;
    }

    public void setReports(boolean reports) {
        this.reports = reports;
    }

    public boolean isVoiceMail() {
        return voiceMail;
    }

    public void setVoiceMail(boolean voiceMail) {
        this.voiceMail = voiceMail;
    }

    public boolean isCallRecording() {
        return callRecording;
    }

    public void setCallRecording(boolean callRecording) {
        this.callRecording = callRecording;
    }

    public boolean isAgentDesktop() {
        return agentDesktop;
    }

    public void setAgentDesktop(boolean agentDesktop) {
        this.agentDesktop = agentDesktop;
    }

    public boolean isAgentPopup() {
        return agentPopup;
    }

    public void setAgentPopup(boolean agentPopup) {
        this.agentPopup = agentPopup;
    }

    public boolean isEmailGateway() {
        return emailGateway;
    }

    public void setEmailGateway(boolean emailGateway) {
        this.emailGateway = emailGateway;
    }

    public boolean isTtsEngine() {
        return ttsEngine;
    }

    public void setTtsEngine(boolean ttsEngine) {
        this.ttsEngine = ttsEngine;
    }

    public boolean isMultiTimeZone() {
        return multiTimeZone;
    }

    public void setMultiTimeZone(boolean multiTimeZone) {
        this.multiTimeZone = multiTimeZone;
    }

    public boolean isMultiLevelIvr() {
        return multiLevelIvr;
    }

    public void setMultiLevelIvr(boolean multiLevelIvr) {
        this.multiLevelIvr = multiLevelIvr;
    }

    public boolean isDashboard() {
        return dashboard;
    }

    public void setDashboard(boolean dashboard) {
        this.dashboard = dashboard;
    }

//    public Channels getChannels() {
//        return channels;
//    }
//
//    public void setChannels(Channels channels) {
//        this.channels = channels;
//    }
//
//    public DialingMode getDialingMode() {
//        return dialingMode;
//    }
//
//    public void setDialingMode(DialingMode dialingMode) {
//        this.dialingMode = dialingMode;
//    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }
}
