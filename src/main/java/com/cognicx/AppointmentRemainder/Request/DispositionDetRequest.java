package com.cognicx.AppointmentRemainder.Request;

import java.util.List;

public class DispositionDetRequest {
	private String dispId;
	private String dispositionName;
	private String description;
	private List<DispositionCodeDet> dispCodeDetailsList;
	
	
	public String getDispositionName() {
		return dispositionName;
	}
	public void setDispositionName(String dispositionName) {
		this.dispositionName = dispositionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<DispositionCodeDet> getDispCodeDetailsList() {
		return dispCodeDetailsList;
	}
	public void setDispCodeDetailsList(List<DispositionCodeDet> dispCodeDetailsList) {
		this.dispCodeDetailsList = dispCodeDetailsList;
	}
	public String getDispId() {
		return dispId;
	}
	public void setDispId(String dispId) {
		this.dispId = dispId;
	}

	
	
}
