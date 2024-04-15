package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

import java.util.List;

public class CenterKVDto {

	private BigInteger inventoryCenterId;
	private String inventoryCenterName;
	List<ClientKVDto> clients;

	public CenterKVDto(BigInteger inventoryCenterId, String inventoryCenterName, List<ClientKVDto> clients) {
		this.inventoryCenterId = inventoryCenterId;
		this.inventoryCenterName = inventoryCenterName;
		this.clients = clients;
	}

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

	public List<ClientKVDto> getClients() {
		return clients;
	}

	public void setClients(List<ClientKVDto> clients) {
		this.clients = clients;
	}

}
