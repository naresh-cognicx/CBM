package com.cognicx.AppointmentRemainder.ami;

import org.asteriskjava.manager.event.UserEvent;

public class callconnecedEvent extends UserEvent{

	private String calluid;
	private String productid;
	private String phone;
	private String appdata;
	private String campaingnname;

	public String getProductid() {
		System.out.println("Getting Product ID in Call connect"+productid);
		return productid;
	}

	public void setProductid(String productid) {
		System.out.println("setting Product ID in Call connect "+productid);
		this.productid = productid;
	}

	public String getPhone() {
		System.out.println("Getting Phone number in Call connect "+phone);
		return phone;
	}

	public void setPhone(String phone) {
		System.out.println("setting Phone Number in Call connect"+phone);
		this.phone = phone;
	}

	public callconnecedEvent(Object source) {
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
		System.out.println("getting App Data in Call connect"+appdata);
		return appdata;
	}

	public void setAppdata(String appdata) {
		System.out.println("Setting App Data in Call connect "+appdata);
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



}
