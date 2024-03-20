package com.cognicx.AppointmentRemainder.Dto;

public class LicenseDto {
    private String crmIntegration;
    private String sipGateway;
    private String smsGateway;

    private String reports;

    private String voiceMail;

    private String callRecording;

    private String agentDesktop;

    private String agentPopup;

    private String emailGateway;

    private String ttsEngine;

    private String multiTimeZone;

    private String multiLevelIvr;

    private String dashboard;

    public String getCrmIntegration() {
        return crmIntegration;
    }

    public void setCrmIntegration(String crmIntegration) {
        this.crmIntegration = crmIntegration;
    }

    public String getSipGateway() {
        return sipGateway;
    }

    public void setSipGateway(String sipGateway) {
        this.sipGateway = sipGateway;
    }

    public String getSmsGateway() {
        return smsGateway;
    }

    public void setSmsGateway(String smsGateway) {
        this.smsGateway = smsGateway;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public String getVoiceMail() {
        return voiceMail;
    }

    public void setVoiceMail(String voiceMail) {
        this.voiceMail = voiceMail;
    }

    public String getCallRecording() {
        return callRecording;
    }

    public void setCallRecording(String callRecording) {
        this.callRecording = callRecording;
    }

    public String getAgentDesktop() {
        return agentDesktop;
    }

    public void setAgentDesktop(String agentDesktop) {
        this.agentDesktop = agentDesktop;
    }

    public String getAgentPopup() {
        return agentPopup;
    }

    public void setAgentPopup(String agentPopup) {
        this.agentPopup = agentPopup;
    }

    public String getEmailGateway() {
        return emailGateway;
    }

    public void setEmailGateway(String emailGateway) {
        this.emailGateway = emailGateway;
    }

    public String getTtsEngine() {
        return ttsEngine;
    }

    public void setTtsEngine(String ttsEngine) {
        this.ttsEngine = ttsEngine;
    }

    public String getMultiTimeZone() {
        return multiTimeZone;
    }

    public void setMultiTimeZone(String multiTimeZone) {
        this.multiTimeZone = multiTimeZone;
    }

    public String getMultiLevelIvr() {
        return multiLevelIvr;
    }

    public void setMultiLevelIvr(String multiLevelIvr) {
        this.multiLevelIvr = multiLevelIvr;
    }

    public String getDashboard() {
        return dashboard;
    }

    public void setDashboard(String dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public String toString() {
        return "{" +
                "crmIntegration='" + crmIntegration + '\'' +
                ", sipGateway='" + sipGateway + '\'' +
                ", smsGateway='" + smsGateway + '\'' +
                ", reports='" + reports + '\'' +
                ", voiceMail='" + voiceMail + '\'' +
                ", callRecording='" + callRecording + '\'' +
                ", agentDesktop='" + agentDesktop + '\'' +
                ", agentPopup='" + agentPopup + '\'' +
                ", emailGateway='" + emailGateway + '\'' +
                ", ttsEngine='" + ttsEngine + '\'' +
                ", multiTimeZone='" + multiTimeZone + '\'' +
                ", multiLevelIvr='" + multiLevelIvr + '\'' +
                ", dashboard='" + dashboard + '\'' +
                '}';
    }

    //    private Channels channels;
//
//    private DialingMode dialingMode;

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
}
