package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

public class InventoryCenterDto {
	
	private BigInteger inventoryCenterId;
	
	private String inventoryCenterName;
	

	public BigInteger getInventoryCenterId() {
		return inventoryCenterId;
	}

	public void setInventoryCenterId(BigInteger inventoryCenterId) {
		this.inventoryCenterId = inventoryCenterId;
	}

	public String getInventoryCenterName() {
		return inventoryCenterName;
	}

	public void setInventoryCenterName(String inventoryCenterName) {
		this.inventoryCenterName = inventoryCenterName;
	}
}
