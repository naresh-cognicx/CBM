package com.cognicx.AppointmentRemainder.ami;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AsteriskEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String calluniqueId;
	private String callerNum;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCalluniqueId() {
		return calluniqueId;
	}
	public void setCalluniqueId(String calluniqueId) {
		this.calluniqueId = calluniqueId;
	}
	public String getCallerNum() {
		return callerNum;
	}
	public void setCallerNum(String callerNum) {
		this.callerNum = callerNum;
	}

}
