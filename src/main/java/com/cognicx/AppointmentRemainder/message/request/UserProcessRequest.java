package com.cognicx.AppointmentRemainder.message.request;

import java.math.BigInteger;
import java.util.List;

public class UserProcessRequest {
	
	private BigInteger inventoryProcessId;
	private String inventoryProcessName;
	private BigInteger inventoryCategoryId;
	private String inventoryCategoryName;

	
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
	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}
	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}
	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}
	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
	}
	
}
