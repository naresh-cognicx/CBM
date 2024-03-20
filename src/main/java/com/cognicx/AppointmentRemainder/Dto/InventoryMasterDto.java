package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

public class InventoryMasterDto {
	
	private BigInteger id;
	private String name;
	private BigInteger autogenReportMasterId;
	private BigInteger autogenClientManagementId;
	private BigInteger autogenFrequencyMasterId;
	private String status;
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigInteger getAutogenReportMasterId() {
		return autogenReportMasterId;
	}
	public void setAutogenReportMasterId(BigInteger autogenReportMasterId) {
		this.autogenReportMasterId = autogenReportMasterId;
	}

	public BigInteger getAutogenClientManagementId() {
		return autogenClientManagementId;
	}
	public void setAutogenClientManagementId(BigInteger autogenClientManagementId) {
		this.autogenClientManagementId = autogenClientManagementId;
	}
	public BigInteger getAutogenFrequencyMasterId() {
		return autogenFrequencyMasterId;
	}
	public void setAutogenFrequencyMasterId(BigInteger autogenFrequencyMasterId) {
		this.autogenFrequencyMasterId = autogenFrequencyMasterId;
	}

}
