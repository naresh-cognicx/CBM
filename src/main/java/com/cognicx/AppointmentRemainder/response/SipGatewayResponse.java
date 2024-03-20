package com.cognicx.AppointmentRemainder.response;

public class SipGatewayResponse {
    private String sipGatewayId;

    private String sipGatewayName;

    private String sipUserName;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public String getSipUserName() {
        return sipUserName;
    }

    public void setSipUserName(String sipUserName) {
        this.sipUserName = sipUserName;
    }
}
