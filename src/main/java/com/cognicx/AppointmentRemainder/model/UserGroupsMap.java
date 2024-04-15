package com.cognicx.AppointmentRemainder.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the user_groups_map database table.
 * 
 */
@Entity
@Table(name="user_groups_map" , schema = "user_rule")
@NamedQuery(name="UserGroupsMap.findAll", query="SELECT u FROM UserGroupsMap u")
public class UserGroupsMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="AUTOGEN_USER_GROUPS_MAP_ID")
	private String autogenUserGroupsMapId;

	@Column(name="AUTOGEN_USER_GROUPS_ID")
	private BigInteger autogenUserGroupsId;

	@Column(name="AUTOGEN_USERS_ID")
	private BigInteger autogenUsersId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	private String status;

	public UserGroupsMap() {
	}

	public String getAutogenUserGroupsMapId() {
		return this.autogenUserGroupsMapId;
	}

	public void setAutogenUserGroupsMapId(String autogenUserGroupsMapId) {
		this.autogenUserGroupsMapId = autogenUserGroupsMapId;
	}

	public BigInteger getAutogenUserGroupsId() {
		return this.autogenUserGroupsId;
	}

	public void setAutogenUserGroupsId(BigInteger autogenUserGroupsId) {
		this.autogenUserGroupsId = autogenUserGroupsId;
	}

	public BigInteger getAutogenUsersId() {
		return this.autogenUsersId;
	}

	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
	}

	public Date getRecAddDt() {
		return this.recAddDt;
	}

	public void setRecAddDt(Date recAddDt) {
		this.recAddDt = recAddDt;
	}

	public Date getRecUpdateDt() {
		return this.recUpdateDt;
	}

	public void setRecUpdateDt(Date recUpdateDt) {
		this.recUpdateDt = recUpdateDt;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}