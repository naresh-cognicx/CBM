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
 * The persistent class for the inventory_mapping database table.
 * 
 */
@Entity
@Table(name="inventory_mapping")
@NamedQuery(name="InventoryMapping.findAll", query="SELECT i FROM InventoryMapping i")
public class InventoryMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_INVENTORY_MAPPING_ID")
	private String autogenInventoryMappingId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="INVENTORY_CENTER_ID")
	private BigInteger inventoryCenterId;

	@Column(name="INVENTORY_CLIENT_ID")
	private BigInteger inventoryClientId;

	@Column(name="INVENTORY_PROCESS_ID")
	private BigInteger inventoryProcessId;

	@Column(name="INVENTORY_REGION_ID")
	private BigInteger inventoryRegionId;
	
	@Column(name="INVENTORY_REGION_NAME")
	private String inventoryRegionName;
	
	@Column(name="INVENTORY_CENTER_NAME")
	private String inventoryCenterName;
	
	@Column(name="INVENTORY_CLIENT_NAME")
	private String inventoryClientName;
	
	@Column(name="INVENTORY_PROCESS_NAME")
	private String inventoryProcessName;

	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	private String status;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	public InventoryMapping() {
	}

	public String getAutogenInventoryMappingId() {
		return this.autogenInventoryMappingId;
	}

	public void setAutogenInventoryMappingId(String autogenInventoryMappingId) {
		this.autogenInventoryMappingId = autogenInventoryMappingId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public BigInteger getInventoryCenterId() {
		return this.inventoryCenterId;
	}

	public void setInventoryCenterId(BigInteger inventoryCenterId) {
		this.inventoryCenterId = inventoryCenterId;
	}

	public BigInteger getInventoryClientId() {
		return this.inventoryClientId;
	}

	public void setInventoryClientId(BigInteger inventoryClientId) {
		this.inventoryClientId = inventoryClientId;
	}

	public BigInteger getInventoryProcessId() {
		return this.inventoryProcessId;
	}

	public void setInventoryProcessId(BigInteger inventoryProcessId) {
		this.inventoryProcessId = inventoryProcessId;
	}

	public BigInteger getInventoryRegionId() {
		return this.inventoryRegionId;
	}

	public void setInventoryRegionId(BigInteger inventoryRegionId) {
		this.inventoryRegionId = inventoryRegionId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getInventoryRegionName() {
		return inventoryRegionName;
	}

	public void setInventoryRegionName(String inventoryRegionName) {
		this.inventoryRegionName = inventoryRegionName;
	}

	public String getInventoryCenterName() {
		return inventoryCenterName;
	}

	public void setInventoryCenterName(String inventoryCenterName) {
		this.inventoryCenterName = inventoryCenterName;
	}

	public String getInventoryClientName() {
		return inventoryClientName;
	}

	public void setInventoryClientName(String inventoryClientName) {
		this.inventoryClientName = inventoryClientName;
	}

	public String getInventoryProcessName() {
		return inventoryProcessName;
	}

	public void setInventoryProcessName(String inventoryProcessName) {
		this.inventoryProcessName = inventoryProcessName;
	}



}