package com.cognicx.AppointmentRemainder.Dto;

import java.math.BigInteger;
import java.util.List;

public class RegionKVDto {

	private BigInteger inventoryRegionId;
	private String inventoryRegionName;
	List<CenterKVDto> centers;

	public RegionKVDto() {
	}

	public RegionKVDto(BigInteger inventoryRegionId, String inventoryRegionName, List<CenterKVDto> centers) {
		this.inventoryRegionId = inventoryRegionId;
		this.inventoryRegionName = inventoryRegionName;
		this.centers = centers;
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

	public List<CenterKVDto> getCenters() {
		return centers;
	}

	public void setCenters(List<CenterKVDto> centers) {
		this.centers = centers;
	}

}
