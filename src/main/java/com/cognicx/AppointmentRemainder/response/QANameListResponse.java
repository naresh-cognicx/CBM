package com.cognicx.AppointmentRemainder.response;

public class QANameListResponse {
	
	private String qaName;
	private String qaId;
	
	public QANameListResponse() {}
	
	public QANameListResponse(String qaName, String qaId) {
		this.qaName = qaName;
		this.qaId = qaId;
	}

	public String getQaName() {
		return qaName;
	}

	public void setQaName(String qaName) {
		this.qaName = qaName;
	}

	public String getQaId() {
		return qaId;
	}

	public void setQaId(String qaId) {
		this.qaId = qaId;
	}
}
