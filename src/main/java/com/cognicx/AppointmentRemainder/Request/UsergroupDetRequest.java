package com.cognicx.AppointmentRemainder.Request;

public class UsergroupDetRequest {
	private String usergroupId;
	private String usergroupName;
	private String usergroupDesc;
	private String usergroupType;
	public String getUsergroupName() {
		return usergroupName;
	}
	public void setUsergroupName(String usergroupName) {
		this.usergroupName = usergroupName;
	}
	public String getUsergroupDesc() {
		return usergroupDesc;
	}
	public void setUsergroupDesc(String usergroupDesc) {
		this.usergroupDesc = usergroupDesc;
	}
	public String getUsergroupType() {
		return usergroupType;
	}
	public void setUsergroupType(String usergroupType) {
		this.usergroupType = usergroupType;
	}
	public String getUsergroupId() {
		return usergroupId;
	}
	public void setUsergroupId(String usergroupId) {
		this.usergroupId = usergroupId;
	}
}
