package com.cognicx.AppointmentRemainder.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the roles database table.
 *
 */
@Entity
@Table(name = "roles", schema = "user_rule")
@NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r")
public class Roles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUTOGEN_ROLES_ID")
	private Long autogenRolesId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_ADD_DT")
	private Date recAddDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name = "ROLES_NAME")
	private String rolesName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ROLE_CREATE_STATUS")
	private String roleCreateStatus;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserScreenMap> userScreenMap;

	public Roles() {
	}

	public Long getAutogenRolesId() {
		return autogenRolesId;
	}

	public void setAutogenRolesId(Long autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
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

	public String getRolesName() {
		return this.rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleCreateStatus() {
		return roleCreateStatus;
	}

	public void setRoleCreateStatus(String roleCreateStatus) {
		this.roleCreateStatus = roleCreateStatus;
	}

	public List<UserScreenMap> getUserScreenMap() {
		return userScreenMap;
	}

	public void setUserScreenMap(List<UserScreenMap> userScreenMap) {
		this.userScreenMap = userScreenMap;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}