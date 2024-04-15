package com.cognicx.AppointmentRemainder.message.request;

import java.math.BigInteger;
import java.util.List;

public class UserCenterRequest {
	
	private BigInteger inventoryCenterId;
	private String inventoryCenterName;
	private List<UserClientRequest> userClients;
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
	public List<UserClientRequest> getUserClients() {
		return userClients;
	}
	public void setUserClients(List<UserClientRequest> userClients) {
		this.userClients = userClients;
	}
	
}