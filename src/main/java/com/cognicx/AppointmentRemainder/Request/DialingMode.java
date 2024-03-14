package com.cognicx.AppointmentRemainder.Request;

public class DialingMode {
    private boolean manual;
    private boolean preview;
    private boolean predictive;
    private boolean progressive;
    private boolean robo;
    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public boolean isPredictive() {
        return predictive;
    }

    public void setPredictive(boolean predictive) {
        this.predictive = predictive;
    }

    public boolean isProgressive() {
        return progressive;
    }

    public void setProgressive(boolean progressive) {
        this.progressive = progressive;
    }

    public boolean isRobo() {
        return robo;
    }

    public void setRobo(boolean robo) {
        this.robo = robo;
    }
}
