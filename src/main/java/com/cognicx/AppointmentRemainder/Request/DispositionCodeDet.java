package com.cognicx.AppointmentRemainder.Request;

public class DispositionCodeDet {
	private String dispId;
	private String code;
	private String itemName;
	private String dispCodeId;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getDispId() {
		return dispId;
	}
	public void setDispId(String dispId) {
		this.dispId = dispId;
	}
	
	public String getDispCodeId() {
		return dispCodeId;
	}
	
	public void setDispCodeId(String dispCodeId) {
		this.dispCodeId = dispCodeId;
	}
	
	
	
}
