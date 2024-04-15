package com.cognicx.AppointmentRemainder.dao;

import java.util.List;

import com.cognicx.AppointmentRemainder.Dto.InventoryDto;
import com.cognicx.AppointmentRemainder.model.InventoryMaster;


/*import com.cognicx.AppointmentRemainder.model.InventoryMaster;
import com.cognicx.AppointmentRemainder.Dto.InventoryDto;*/

public interface InventoryDAO {
	
	public InventoryDto cretae(InventoryDto inventoryDto) throws Exception;
	
	public InventoryDto update(InventoryDto inventoryDto) throws Exception;
	
	public List<Object[]> inventoryDropdown(InventoryDto inventoryDto) throws Exception;
	
	public List<Object[]>  inventoryDropdownMappingList(InventoryDto inventoryDto) throws Exception; 
	
	public InventoryDto cretaeInventoryMapping(InventoryDto inventoryDto) throws Exception;
	
	public InventoryDto checkInvenMapAlreadyExists(InventoryDto inventoryDto) throws Exception;
	
	public InventoryDto checkInvenMasterAlreadyExists(InventoryDto inventoryDto) throws Exception; 
	
	public InventoryMaster findInventory(InventoryDto inventoryDto) throws Exception;
	
	public InventoryDto getMappedInventoryList() throws Exception; 
	
	public InventoryDto getAllinventoryTypeList(InventoryDto inventoryDto) throws Exception;

}
