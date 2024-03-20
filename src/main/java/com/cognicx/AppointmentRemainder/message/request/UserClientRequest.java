package com.cognicx.AppointmentRemainder.message.request;

import java.math.BigInteger;
import java.util.List;

public class UserClientRequest {
	
	private BigInteger inventoryClientId;
	private String inventoryClientName;
	private List<UserProcessRequest> userProcesses;
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
	public List<UserProcessRequest> getUserProcesses() {
		return userProcesses;
	}
	public void setUserProcesses(List<UserProcessRequest> userProcesses) {
		this.userProcesses = userProcesses;
	}
	
}
