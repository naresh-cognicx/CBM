package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

public class ProcessKVDto {

	private BigInteger inventoryProcessId;
	private String inventoryProcessName;

	public ProcessKVDto(BigInteger inventoryProcessId, String inventoryProcessName) {
		this.inventoryProcessId = inventoryProcessId;
		this.inventoryProcessName = inventoryProcessName;
	}

	public BigInteger getInventoryProcessId() {
		return inventoryProcessId;
	}

	public void setInventoryProcessId(BigInteger inventoryProcessId) {
		this.inventoryProcessId = inventoryProcessId;
	}

	public String getInventoryProcessName() {
		return inventoryProcessName;
	}

	public void setInventoryProcessName(String inventoryProcessName) {
		this.inventoryProcessName = inventoryProcessName;
	}

}
