package com.cognicx.AppointmentRemainder.model;

import java.math.BigInteger;

public class KeyValueObject {
	
	private BigInteger id;
	private String label;
	private String status;
	
	public KeyValueObject(BigInteger id, String label, String status) {
		this.id = id;
		this.label = label;
		this.status = status;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
