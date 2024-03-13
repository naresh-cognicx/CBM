package com.cognicx.AppointmentRemainder.model;

import javax.persistence.*;

@Entity
@Table(name = "partner_det", schema = "appointment_remainder")
@NamedQuery(name = "PartnerDet.findAll", query = "SELECT u FROM PartnerDet u")
public class PartnerDet {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partnerId")
    private String partnerId;
    private String partnerName;

    private String partnerEmail;

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
        return "PartnerDet{" +
                "partnerId='" + partnerId + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", partnerEmail='" + partnerEmail + '\'' +
                '}';
    }
}
