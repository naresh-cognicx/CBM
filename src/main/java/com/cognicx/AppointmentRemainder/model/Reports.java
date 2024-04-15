package com.cognicx.AppointmentRemainder.model;

import java.math.BigInteger;

public class Reports {
	
	private BigInteger id;
	private String reportName;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
