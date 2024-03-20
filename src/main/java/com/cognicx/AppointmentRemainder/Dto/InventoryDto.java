package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cognicx.AppointmentRemainder.*;
import com.cognicx.AppointmentRemainder.model.KeyValueObject;

public class InventoryDto {

    public BigInteger autogenInventoryMasterId;

    public String createdBy;

    public String inventoryType;

    public String name;

    public List<String> names;

    public Date recAddDt;

    public Date recUpdateDt;

    public String status;

    public String updatedBy;

    public boolean flag;

    public List<KeyValueObject> keyValueObjectMap = new ArrayList<>();

    public BigInteger inventoryCategoryId;

    public BigInteger inventoryCenterId;

    public BigInteger inventoryProcessId;

    public BigInteger inventoryRegionId;

    public BigInteger inventoryClientId;

    public String inventoryRegionName;

    public String inventoryCenterName;

    public String inventoryClientName;

    public String inventoryProcessName;

    public BigInteger autogenInventoryMappingId;

    public Object result;

    public List<Object[]> resultObjList;

    public List<RegionKVDto> resultList;

    public List<InventoryCenterDto> centers;

    public List<InventoryProcessDto> processes;

    public List<InventoryClientDto> clients;

    public InventoryDto() {
    }

    public InventoryDto(InventoryDto inventoryDto) {

    }

    public BigInteger getAutogenInventoryMasterId() {
	return autogenInventoryMasterId;
    }

    public void setAutogenInventoryMasterId(BigInteger autogenInventoryMasterId) {
	this.autogenInventoryMasterId = autogenInventoryMasterId;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public String getInventoryType() {
	return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
	this.inventoryType = inventoryType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
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

    public String getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
	this.updatedBy = updatedBy;
    }

    public boolean isFlag() {
	return flag;
    }

    public void setFlag(boolean flag) {
	this.flag = flag;
    }

    public List<KeyValueObject> getKeyValueObjectMap() {
	return keyValueObjectMap;
    }

    public void setKeyValueObjectMap(List<KeyValueObject> keyValueObjectMap) {
	this.keyValueObjectMap = keyValueObjectMap;
    }

    public BigInteger getAutogenInventoryMappingId() {
	return autogenInventoryMappingId;
    }

    public void setAutogenInventoryMappingId(BigInteger autogenInventoryMappingId) {
	this.autogenInventoryMappingId = autogenInventoryMappingId;
    }

    public BigInteger getInventoryCategoryId() {
	return inventoryCategoryId;
    }

    public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
	this.inventoryCategoryId = inventoryCategoryId;
    }

    public BigInteger getInventoryCenterId() {
	return inventoryCenterId;
    }

    public void setInventoryCenterId(BigInteger inventoryCenterId) {
	this.inventoryCenterId = inventoryCenterId;
    }

    public BigInteger getInventoryProcessId() {
	return inventoryProcessId;
    }

    public void setInventoryProcessId(BigInteger inventoryProcessId) {
	this.inventoryProcessId = inventoryProcessId;
    }

    public BigInteger getInventoryRegionId() {
	return inventoryRegionId;
    }

    public void setInventoryRegionId(BigInteger inventoryRegionId) {
	this.inventoryRegionId = inventoryRegionId;
    }

    public BigInteger getInventoryClientId() {
	return inventoryClientId;
    }

    public void setInventoryClientId(BigInteger inventoryClientId) {
	this.inventoryClientId = inventoryClientId;
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

    public Object getResult() {
	return result;
    }

    public void setResult(Object result) {
	this.result = result;
    }

    public List<RegionKVDto> getResultList() {
	return resultList;
    }

    public void setResultList(List<RegionKVDto> resultList) {
	this.resultList = resultList;
    }

    public List<InventoryCenterDto> getCenters() {
	return centers;
    }

    public void setCenters(List<InventoryCenterDto> centers) {
	this.centers = centers;
    }

    public List<InventoryProcessDto> getProcesses() {
	return processes;
    }

    public void setProcesses(List<InventoryProcessDto> processes) {
	this.processes = processes;
    }

    public List<InventoryClientDto> getClients() {
	return clients;
    }

    public void setClients(List<InventoryClientDto> clients) {
	this.clients = clients;
    }

    public List<Object[]> getResultObjList() {
	return resultObjList;
    }

    public void setResultObjList(List<Object[]> resultObjList) {
	this.resultObjList = resultObjList;
    }

    public List<String> getNames() {
	return names;
    }

    public void setNames(List<String> names) {
	this.names = names;
    }

}
