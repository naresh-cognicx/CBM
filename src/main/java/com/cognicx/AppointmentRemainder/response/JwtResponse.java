package com.cognicx.AppointmentRemainder.response;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
public class JwtResponse {
	private String userName;
    private String token;
    private String type = "Bearer";
    private String roles;
    private String expirySeconds;
    private Timestamp expiryDate;
    private List<ModuleListResponse> modulescreens;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String userGroupName;
	

	private List<Map<String, String>> domain;
	private List<Map<String, String>> businessUnit;
	private boolean ldapEnabled;

	private FeatureResponse featureResponse;

    public JwtResponse() {}
    
    public JwtResponse(JwtResponse jwtResponse) {
    	this.userName = jwtResponse.getUserName();
    	this.token = jwtResponse.getAccessToken();
        this.roles = jwtResponse.getRoles();
        this.expirySeconds = jwtResponse.getExpirySeconds();
        this.expiryDate = jwtResponse.getExpiryDate();
        this.modulescreens = jwtResponse.getModulescreens();
        this.firstName = jwtResponse.getFirstName();
        this.lastName = jwtResponse.getLastName();
        this.mobileNumber = jwtResponse.getMobileNumber();
		this.domain = jwtResponse.getDomain();
		this.businessUnit = jwtResponse.getBusinessUnit();
		this.userGroupName=jwtResponse.getUserGroupName();
		this.ldapEnabled = jwtResponse.isLdapEnabled();
		this.featureResponse = jwtResponse.getFeatureResponse();
    }

	public FeatureResponse getFeatureResponse() {
		return featureResponse;
	}

	public void setFeatureResponse(FeatureResponse featureResponse) {
		this.featureResponse = featureResponse;
	}

	public String getAccessToken() {
        return token;
    }
 
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }
 
    public String getTokenType() {
        return type;
    }
 
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getExpirySeconds() {
		return expirySeconds;
	}

	public void setExpirySeconds(String expirySeconds) {
		this.expirySeconds = expirySeconds;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ModuleListResponse> getModulescreens() {
		return modulescreens;
	}

	public void setModulescreens(List<ModuleListResponse> modulescreens) {
		this.modulescreens = modulescreens;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public List<Map<String, String>> getDomain() {
		return domain;
	}

	public void setDomain(List<Map<String, String>> domain) {
		this.domain = domain;
	}

	public List<Map<String, String>> getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(List<Map<String, String>> business) {
		this.businessUnit = business;
	}

	public boolean isLdapEnabled() {
		return ldapEnabled;
	}

	public void setLdapEnabled(boolean ldapEnabled) {
		this.ldapEnabled = ldapEnabled;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}


}