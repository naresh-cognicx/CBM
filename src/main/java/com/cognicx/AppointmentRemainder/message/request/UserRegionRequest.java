package com.cognicx.AppointmentRemainder.message.request;

import java.math.BigInteger;

import java.util.List;

public class UserRegionRequest {
	
	private BigInteger inventoryRegionId;
	private String inventoryRegionName;
	private List<UserCenterRequest> userCenters;
	
	public BigInteger getInventoryRegionId() {
		return inventoryRegionId;
	}
	public void setInventoryRegionId(BigInteger inventoryRegionId) {
		this.inventoryRegionId = inventoryRegionId;
	}
	public String getInventoryRegionName() {
		return inventoryRegionName;
	}
	public void setInventoryRegionName(String inventoryRegionName) {
		this.inventoryRegionName = inventoryRegionName;
	}
	public List<UserCenterRequest> getUserCenters() {
		return userCenters;
	}
	public void setUserCenters(List<UserCenterRequest> userCenters) {
		this.userCenters = userCenters;
	} 
}
