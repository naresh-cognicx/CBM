package com.cognicx.AppointmentRemainder.response;

public class SupervisorsResponse {
	
	  private String supervisorUsersId;  
	  private String supervisorUsersName;  
	  
	  public SupervisorsResponse() {}
	  
	  public SupervisorsResponse(String supervisorUsersId, String supervisorUsersName) {
		  this.supervisorUsersId = supervisorUsersId;
		  this.supervisorUsersName = supervisorUsersName;
	  }

	public String getSupervisorUsersId() {
		return supervisorUsersId;
	}

	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}

	public String getSupervisorUsersName() {
		return supervisorUsersName;
	}

	public void setSupervisorUsersName(String supervisorUsersName) {
		this.supervisorUsersName = supervisorUsersName;
	}

}
