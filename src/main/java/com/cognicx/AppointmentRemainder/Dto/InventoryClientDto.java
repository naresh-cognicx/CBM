package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;
import java.util.List;

public class InventoryClientDto{
	
	private BigInteger inventoryClientId;
	
	public String inventoryClientName;
	

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

}
