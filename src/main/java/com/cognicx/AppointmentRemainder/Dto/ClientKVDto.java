package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;
import java.util.List;

public class ClientKVDto {

	private BigInteger inventoryClientId;
	private String inventoryClientName;
	List<ProcessKVDto> processes;

	public ClientKVDto() {
	}

	public ClientKVDto(BigInteger inventoryClientId, String inventoryClientName, List<ProcessKVDto> processes) {
		this.inventoryClientId = inventoryClientId;
		this.inventoryClientName = inventoryClientName;
		this.processes = processes;
	}

	public BigInteger getInventoryClientId() {
		return inventoryClientId;
	}

	public void setInventoryClientId(BigInteger inventoryClientId) {
		this.inventoryClientId = inventoryClientId;
	}

	public String getInventoryClientName() {
		return inventoryClientName;
	}

	public void setInventoryClientName(String inventoryClientName) {
		this.inventoryClientName = inventoryClientName;
	}

	public List<ProcessKVDto> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessKVDto> processes) {
		this.processes = processes;
	}

}
