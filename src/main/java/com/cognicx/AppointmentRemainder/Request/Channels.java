package com.cognicx.AppointmentRemainder.Request;

public class Channels {
    private boolean voice;
    private boolean whatsapp;

    private boolean sms;

    private boolean email;

    private boolean webChat;

    private boolean socialMedia;

    public boolean isVoice() {
        return voice;
    }

    public void setVoice(boolean voice) {
        this.voice = voice;
    }

    public boolean isWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(boolean whatsapp) {
        this.whatsapp = whatsapp;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isWebChat() {
        return webChat;
    }

    public void setWebChat(boolean webChat) {
        this.webChat = webChat;
    }

    public boolean isSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(boolean socialMedia) {
        this.socialMedia = socialMedia;
    }
}
