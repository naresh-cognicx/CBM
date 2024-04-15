package com.cognicx.AppointmentRemainder.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;


/**
 * The persistent class for the user_report_map database table.
 * 
 */
@Entity
@Table(name = "user_inventory_map", schema = "user_rule")
@NamedQuery(name="UserInventoryMap.findAll", query="SELECT ui FROM UserInventoryMap ui")
public class UserInventoryMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_USER_INVENTORY_MAP_ID")
	private BigInteger autogenUserInventoryMapId;

	@Column(name="AUTOGEN_USERS_DETAILS_ID")
	private BigInteger autogenUsersDetailsId;

	@Column(name="INVENTORY_REGION_ID")
	private BigInteger inventoryRegionId;

	@Column(name="INVENTORY_REGION_NAME")
	private String inventoryRegionName;
	
	@Column(name="INVENTORY_CENTER_ID")
	private BigInteger inventoryCenterId;

	@Column(name="INVENTORY_CENTER_NAME")
	private String inventoryCenterName;
	
	@Column(name="INVENTORY_CLIENT_ID")
	private BigInteger inventoryClientId;
	
	@Column(name="INVENTORY_CLIENT_NAME")
	private String inventoryClientName;
	
	@Column(name="INVENTORY_PROCESS_ID")
	private BigInteger inventoryProcessId;

	@Column(name="INVENTORY_PROCESS_NAME")
	private String inventoryProcessName;
	
	@Column(name="INVENTORY_CATEGORY_ID")
	private BigInteger inventoryCategoryId;

	@Column(name="INVENTORY_CATEGORY_NAME")
	private String inventoryCategoryName;
		
	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	private String status;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;

	public UserInventoryMap() {
		
	}
	
	public BigInteger getAutogenUserInventoryMapId() {
		return autogenUserInventoryMapId;
	}

	public void setAutogenUserInventoryMapId(BigInteger autogenUserInventoryMapId) {
		this.autogenUserInventoryMapId = autogenUserInventoryMapId;
	}

	public BigInteger getAutogenUsersDetailsId() {
		return autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(BigInteger autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public BigInteger getInventoryRegionId() {
		return inventoryRegionId;
	}

	public void setInventoryRegionId(BigInteger inventoryRegionId) {
		this.inventoryRegionId = inventoryRegionId;
	}

	public String getInventoryRegionName() {
		return inventoryRegionName;
	}

	public void setInventoryRegionName(String inventoryRegionName) {
		this.inventoryRegionName = inventoryRegionName;
	}

	public BigInteger getInventoryCenterId() {
		return inventoryCenterId;
	}

	public void setInventoryCenterId(BigInteger inventoryCenterId) {
		this.inventoryCenterId = inventoryCenterId;
	}

	public String getInventoryCenterName() {
		return inventoryCenterName;
	}

	public void setInventoryCenterName(String inventoryCenterName) {
		this.inventoryCenterName = inventoryCenterName;
	}

	public BigInteger getInventoryClientId() {
		return inventoryClientId;
	}

	public void setInventoryClientId(BigInteger inventoryClientId) {
		this.inventoryClientId = inventoryClientId;
	}

	public String getInventoryClientName() {
		return inventoryClientName;
	}

	public void setInventoryClientName(String inventoryClientName) {
		this.inventoryClientName = inventoryClientName;
	}

	public BigInteger getInventoryProcessId() {
		return inventoryProcessId;
	}

	public void setInventoryProcessId(BigInteger inventoryProcessId) {
		this.inventoryProcessId = inventoryProcessId;
	}

	public String getInventoryProcessName() {
		return inventoryProcessName;
	}

	public void setInventoryProcessName(String inventoryProcessName) {
		this.inventoryProcessName = inventoryProcessName;
	}

	public Date getRecAddDt() {
		return recAddDt;
	}

	public void setRecAddDt(Date recAddDt) {
		this.recAddDt = recAddDt;
	}

	public Date getRecUpdateDt() {
		return recUpdateDt;
	}

	public void setRecUpdateDt(Date recUpdateDt) {
		this.recUpdateDt = recUpdateDt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
	}
			
}

