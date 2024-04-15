package com.cognicx.AppointmentRemainder.ami;

import org.asteriskjava.manager.event.UserEvent;

public class callhangupEvent extends UserEvent{

	private String calluid;



	private String productid;
	private String phone;
	private String appdata;
	private String hangupcause;
	private String campaingnname;

	public String getHangupcause() {
		return hangupcause;
	}

	public void setHangupcause(String hangupcause) {
		this.hangupcause = hangupcause;
	}


	public String getPhone() {
		System.out.println("Getting Phone number in Call Hang UP "+phone);
		return phone;
	}

	public void setPhone(String phone) {
		System.out.println("setting Phone Number in Call Hang UP"+phone);
		this.phone = phone;
	}

	public callhangupEvent(Object source) {
		super(source);
	}

	public String getCalluid() {
		System.out.println("Getting Call ID"+calluid);
		return calluid;
	}

	public void setCalluid(String calluid) {
		System.out.println("Setting Call ID");
		this.calluid = calluid;
	}

	public String getAppdata() {
		System.out.println("getting App Data in Call Hang UP"+appdata);
		return appdata;
	}

	public void setAppdata(String appdata) {
		System.out.println("Setting App Data in Call Hang UP "+appdata);
		this.appdata = appdata;
	}


	public String getCampaingnname() {
		return campaingnname;
	}

	public void setCampaingnname(String campaingnname) {
		this.campaingnname = campaingnname;
	}


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}


}
