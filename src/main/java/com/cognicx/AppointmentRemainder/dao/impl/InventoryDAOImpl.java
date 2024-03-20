package com.cognicx.AppointmentRemainder.dao.impl;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cognicx.AppointmentRemainder.dao.InventoryDAO;
import com.cognicx.AppointmentRemainder.model.InventoryMapping;
import com.cognicx.AppointmentRemainder.model.InventoryMaster;
import com.cognicx.AppointmentRemainder.util.CommonUtil;
import com.cognicx.AppointmentRemainder.Dto.CenterKVDto;
import com.cognicx.AppointmentRemainder.Dto.ClientKVDto;
import com.cognicx.AppointmentRemainder.Dto.InventoryDto;
import com.cognicx.AppointmentRemainder.Dto.ProcessKVDto;
import com.cognicx.AppointmentRemainder.Dto.RegionKVDto;
import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;
/*import com.ison.app.dao.InventoryDAO;
import com.ison.app.model.InventoryMapping;
import com.ison.app.model.InventoryMaster;
import com.ison.app.shared.dto.CenterKVDto;
import com.ison.app.shared.dto.ClientKVDto;
import com.ison.app.shared.dto.InventoryDto;
import com.ison.app.shared.dto.ProcessKVDto;
import com.ison.app.shared.dto.RegionKVDto;
import com.ison.app.util.CommonUtil;*/

@Repository("InventoryDAO")
public class InventoryDAOImpl implements InventoryDAO {

    @PersistenceContext(unitName = ApplicationConstant.FIRST_PERSISTENCE_UNIT_NAME)
    public EntityManager firstEntityManager;

    private Logger logger = LoggerFactory.getLogger(InventoryDAOImpl.class);

    @Override
    @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public InventoryDto cretae(InventoryDto inventoryDto) throws Exception {
	try {
	    if (inventoryDto != null && !inventoryDto.getNames().isEmpty()) {
		List<Object[]> inventoryList = inventoryDropdown(inventoryDto);
		for (String name : inventoryDto.names) {
		    boolean isPresent = inventoryList.stream()
			    .anyMatch(p -> name.toLowerCase().equalsIgnoreCase(p[1].toString().toLowerCase()));
		    if (!isPresent) {
			InventoryMaster inventoryMaster = new InventoryMaster();
			CommonUtil.copyProperties(inventoryDto, inventoryMaster);
			inventoryMaster.setName(name);
			firstEntityManager.persist(inventoryMaster);
		    }
		}
		inventoryDto.setFlag(true);
	    }

	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: create() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return inventoryDto;
    }

	public InventoryDto checkInvenMasterAlreadyExists(InventoryDto inventoryDto) throws Exception {
		List<Object[]> result = null;
		Query qryObj = null;
		try {
			if (inventoryDto != null) {
				inventoryDto.setFlag(false);
				qryObj = firstEntityManager.createNativeQuery(
						"SELECT 1 FROM  INVENTORY_MASTER B  WHERE B.INVENTORY_TYPE =:TYPE AND B.NAME =:NAME");
				qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
				qryObj.setParameter("NAME", inventoryDto.getName());
				result = qryObj.getResultList();
				if (!result.isEmpty()) {
					inventoryDto.setFlag(true);
				}
			}

		} catch (Exception e) {
			logger.error("Exception :: InventoryDAOImpl :: checkInvenMasterAlreadyExists() : " + e);
		} finally {
			firstEntityManager.close();
		}

		return inventoryDto;
	}

    @Override
    @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public InventoryDto update(InventoryDto inventoryDto) throws Exception {
	try {
	    inventoryDto.setFlag(false);
	    InventoryMaster inventoryMaster = firstEntityManager.find(InventoryMaster.class,
		    inventoryDto.getAutogenInventoryMasterId());
	    if (inventoryMaster != null) {
		if (!CommonUtil.nullRemove(inventoryDto.getInventoryType()).isEmpty()) {
		    inventoryMaster.setInventoryType(inventoryDto.getInventoryType());
		}
		if (!CommonUtil.nullRemove(inventoryDto.getName()).isEmpty()) {
		    inventoryMaster.setName(inventoryDto.getName());
		}
		if (!CommonUtil.nullRemove(inventoryDto.getStatus()).isEmpty()) {
		    inventoryMaster.setStatus(inventoryDto.getStatus());
		}
		firstEntityManager.merge(inventoryMaster);
		CommonUtil.copyProperties(inventoryMaster, inventoryDto);
		inventoryDto.setFlag(true);
		List<Integer> ss = new ArrayList<>();
	    }

	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: create() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return inventoryDto;
    }

    public List<Object[]> inventoryDropdown(InventoryDto inventoryDto) throws Exception {
	List<Object[]> result = null;
	Query qryObj = null;
		try {
			if (inventoryDto != null) {
				qryObj = firstEntityManager.createNativeQuery(
						"SELECT AUTOGEN_INVENTORY_MASTER_ID, NAME, STATUS FROM INVENTORY_MASTER WHERE INVENTORY_TYPE=:TYPE");
				if ("REGION".equalsIgnoreCase(inventoryDto.getInventoryType())) {
					qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
				} else if ("CENTER".equalsIgnoreCase(inventoryDto.getInventoryType())) {
					qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
				} else if ("PROCESS".equalsIgnoreCase(inventoryDto.getInventoryType())) {
					qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
				} else if ("CLIENT".equalsIgnoreCase(inventoryDto.getInventoryType())) {
					qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
				} else if ("CATEGORY".equalsIgnoreCase(inventoryDto.getInventoryType())) {
					qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
				}
				result = qryObj.getResultList();
			}

		} catch (Exception e) {
	    logger.error("Exception :: InventoryDAOImpl :: inventoryDropdown() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return result;
    }

    public InventoryDto getAllinventoryTypeList(InventoryDto inventoryDto) throws Exception {
	List<Object[]> result = null;
	Query qryObj = null;
	try {
	    if (inventoryDto != null) {
		qryObj = firstEntityManager.createNativeQuery(
			"SELECT INVENTORY_TYPE, AUTOGEN_INVENTORY_MASTER_ID, NAME, STATUS FROM INVENTORY_MASTER");
		/*
		 * if("REGION".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		 * qryObj.setParameter("TYPE", inventoryDto.getInventoryType()); } else
		 * if("CENTER".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		 * qryObj.setParameter("TYPE", inventoryDto.getInventoryType()); } else
		 * if("PROCESS".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		 * qryObj.setParameter("TYPE", inventoryDto.getInventoryType()); } else
		 * if("CLIENT".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		 * qryObj.setParameter("TYPE", inventoryDto.getInventoryType()); }else
		 * if("CATEGORY".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		 * qryObj.setParameter("TYPE", inventoryDto.getInventoryType()); }
		 */
		 result = qryObj.getResultList();
	    }
	   
	    // result.stream().filter(p -> p[0].toString().equalsIgnoreCase("CENTER"))
	    inventoryDto.setResultObjList(result);
	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: inventoryDropdown() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return inventoryDto;
    }

    @Override
    public List<Object[]> inventoryDropdownMappingList(InventoryDto inventoryDto) throws Exception {
	List<Object[]> result = null;
	Query qryObj = null;
	try {
	    if (inventoryDto != null) {
		if ("REGION".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj = firstEntityManager.createNativeQuery(
			    "SELECT DISTINCT A.AUTOGEN_INVENTORY_MASTER_ID, A.NAME, A.STATUS FROM INVENTORY_MASTER A, inventory_mapping B  WHERE A.STATUS='ACTIVE' AND A.AUTOGEN_INVENTORY_MASTER_ID =B.INVENTORY_REGION_ID ");
		    result = qryObj.getResultList();
		} else if ("CLIENTREGION".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj = firstEntityManager.createNativeQuery(
			    "SELECT DISTINCT A.AUTOGEN_INVENTORY_MASTER_ID, A.NAME, A.STATUS FROM INVENTORY_MASTER A, inventory_mapping B  WHERE A.STATUS='ACTIVE' AND A.AUTOGEN_INVENTORY_MASTER_ID =B.INVENTORY_REGION_ID AND B.INVENTORY_CLIENT_ID =:CLIENTID ");
		    qryObj.setParameter("CLIENTID", inventoryDto.getInventoryClientId());
		    result = qryObj.getResultList();
		} else if ("CLIENTCENTER".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj = firstEntityManager.createNativeQuery(
			    "SELECT DISTINCT A.AUTOGEN_INVENTORY_MASTER_ID, A.NAME, A.STATUS FROM INVENTORY_MASTER A, inventory_mapping B  WHERE A.STATUS='ACTIVE' AND A.AUTOGEN_INVENTORY_MASTER_ID =B.INVENTORY_CENTER_ID AND B.INVENTORY_CLIENT_ID =:CLIENTID AND B.INVENTORY_REGION_ID =:REGIONID");
		    qryObj.setParameter("CLIENTID", inventoryDto.getInventoryClientId());
		    qryObj.setParameter("REGIONID", inventoryDto.getInventoryRegionId());
		    result = qryObj.getResultList();
		} else if ("CENTER".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj = firstEntityManager.createNativeQuery(
			    "SELECT DISTINCT A.AUTOGEN_INVENTORY_MASTER_ID, A.NAME, A.STATUS  FROM INVENTORY_MASTER A, inventory_mapping B  WHERE A.STATUS='ACTIVE' AND A.AUTOGEN_INVENTORY_MASTER_ID =B.INVENTORY_CENTER_ID AND B.INVENTORY_REGION_ID =:REGIONID");
		    qryObj.setParameter("REGIONID", inventoryDto.getInventoryRegionId());
		    result = qryObj.getResultList();
		} else if ("CLIENT".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj = firstEntityManager.createNativeQuery(
			    "SELECT DISTINCT A.AUTOGEN_INVENTORY_MASTER_ID, A.NAME, A.STATUS  FROM INVENTORY_MASTER A, inventory_mapping B  WHERE A.STATUS='ACTIVE' AND A.AUTOGEN_INVENTORY_MASTER_ID =B.INVENTORY_CLIENT_ID AND B.INVENTORY_CENTER_ID =:CENTERID AND B.INVENTORY_REGION_ID =:REGIONID");
		    qryObj.setParameter("REGIONID", inventoryDto.getInventoryRegionId());
		    qryObj.setParameter("CENTERID", inventoryDto.getInventoryCenterId());
		    result = qryObj.getResultList();
		} else if ("PROCESS".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj = firstEntityManager.createNativeQuery(
			    "SELECT DISTINCT A.AUTOGEN_INVENTORY_MASTER_ID, A.NAME, A.STATUS  FROM INVENTORY_MASTER A, inventory_mapping B  WHERE A.STATUS='ACTIVE' AND A.AUTOGEN_INVENTORY_MASTER_ID =B.INVENTORY_PROCESS_ID AND B.INVENTORY_CLIENT_ID =:CLIENTID AND B.INVENTORY_CENTER_ID =:CENTERID AND B.INVENTORY_REGION_ID =:REGIONID");
		    qryObj.setParameter("REGIONID", inventoryDto.getInventoryRegionId());
		    qryObj.setParameter("CENTERID", inventoryDto.getInventoryCenterId());
		    qryObj.setParameter("CLIENTID", inventoryDto.getInventoryClientId());
		    result = qryObj.getResultList();
		}
	    }

	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: inventoryDropdownMappingList() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return result;
    }

    public InventoryDto getInventoryDetList(InventoryDto inventoryDto) throws Exception {
	try {
	    inventoryDto.setInventoryType("REGION");
	    inventoryDropdownMappingList(inventoryDto);
	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: getInventoryDetList() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return inventoryDto;

    }

	public InventoryDto checkInvenMapAlreadyExists(InventoryDto inventoryDto) throws Exception {
		List<Object[]> result = null;
		Query qryObj = null;
		try {
			if (inventoryDto != null) {
				inventoryDto.setFlag(false);
				qryObj = firstEntityManager.createNativeQuery(
						"SELECT 1 FROM  INVENTORY_MAPPING B  WHERE B.INVENTORY_CLIENT_ID =:CLIENTID AND B.INVENTORY_CENTER_ID =:CENTERID AND B.INVENTORY_REGION_ID =:REGIONID AND B.INVENTORY_PROCESS_ID =:PROCESSID");
				qryObj.setParameter("REGIONID", inventoryDto.getInventoryRegionId());
				qryObj.setParameter("CENTERID", inventoryDto.getInventoryCenterId());
				qryObj.setParameter("CLIENTID", inventoryDto.getInventoryClientId());
				qryObj.setParameter("PROCESSID", inventoryDto.getInventoryProcessId());
				result = qryObj.getResultList();
				if (result != null && !result.isEmpty()) {
					inventoryDto.setFlag(true);
				}
			}

		} catch (Exception e) {
			logger.info("Exception :: InventoryDAOImpl :: inventoryDropdown() : " + e);
		} finally {
			firstEntityManager.close();
		}

		return inventoryDto;
	}

    /**
     * public boolean checkInvenMapAlreadyExists(BigInteger regionId, BigInteger
     * centerId, BigInteger clientId, BigInteger processId) throws Exception {
     * List<Object[]> result = null; Query qryObj = null; boolean status = false;
     * try { if(regionId != null) { qryObj = firstEntityManager.
     * createNativeQuery("SELECT 1 FROM  INVENTORY_MAPPING B  WHERE B.INVENTORY_CLIENT_ID =:CLIENTID AND B.INVENTORY_CENTER_ID =:CENTERID AND B.INVENTORY_REGION_ID =:REGIONID AND B.INVENTORY_PROCESS_ID =:PROCESSID"
     * ); qryObj.setParameter("REGIONID", regionId); qryObj.setParameter("CENTERID",
     * centerId); qryObj.setParameter("CLIENTID", clientId);
     * qryObj.setParameter("PROCESSID", processId); result = qryObj.getResultList();
     * if(result.size() > 0) { status = true; } }
     * 
     * } catch (Exception e) {
     * logger.info("Exception :: InventoryDAOImpl :: inventoryDropdown() : " + e); }
     * finally { firstEntityManager.close(); }
     * 
     * return status; }
     */

    @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public InventoryDto cretaeInventoryMapping(InventoryDto inventoryDto) throws Exception {
	try {
	    if (inventoryDto != null) {
		Query qryObj = null;
		qryObj = firstEntityManager.createQuery(
			"SELECT i.inventoryRegionId, i.inventoryCenterId, i.inventoryClientId, i.inventoryProcessId FROM InventoryMapping i");
		List<Object[]> result = qryObj.getResultList();
		inventoryDto.getCenters().stream().forEach(centerObj -> {
		    inventoryDto.getClients().stream().forEach(clientObj -> {
			inventoryDto.getProcesses().stream().forEach(processObj -> {
			    try {
				boolean isPresent = result.stream()
					.anyMatch(p -> p[0] == inventoryDto.getInventoryRegionId()
						&& p[1] == centerObj.getInventoryCenterId()
						&& p[2] == clientObj.getInventoryClientId()
						&& p[3] == processObj.getInventoryProcessId());
				if (!isPresent) {
				    InventoryMapping inventoryMapping = new InventoryMapping();
				    inventoryMapping.setInventoryRegionId(inventoryDto.getInventoryRegionId());
				    inventoryMapping.setInventoryRegionName(inventoryDto.getInventoryRegionName());
				    inventoryMapping.setInventoryCenterId(centerObj.getInventoryCenterId());
				    inventoryMapping.setInventoryCenterName(centerObj.getInventoryCenterName());
				    inventoryMapping.setInventoryClientId(clientObj.getInventoryClientId());
				    inventoryMapping.setInventoryClientName(clientObj.getInventoryClientName());
				    inventoryMapping.setInventoryProcessId(processObj.getInventoryProcessId());
				    inventoryMapping.setInventoryProcessName(processObj.getInventoryProcessName());
				    inventoryMapping.setStatus("ACTIVE");
				    inventoryMapping.setCreatedBy(inventoryDto.getCreatedBy());
				    firstEntityManager.persist(inventoryMapping);
				}
				inventoryDto.setFlag(true);
			    } catch (Exception e) {
				logger.info(
					"Exception :: InventoryDAOImpl :: cretaeInventoryMapping() Nested Try Exception : "
						+ e);
			    }

			});
		    });
		});

	    }
	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: cretaeInventoryMapping() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return inventoryDto;
    }

    public InventoryMaster findInventory(InventoryDto inventoryDto) throws Exception {
	List<Object[]> result = null;
	InventoryMaster inventoryMaster = null;
	Query qryObj = null;
	try {
	    if (inventoryDto != null) {
		qryObj = firstEntityManager.createNativeQuery(
			"SELECT DISTINCT AUTOGEN_INVENTORY_MASTER_ID, NAME, STATUS FROM INVENTORY_MASTER WHERE STATUS='ACTIVE' AND INVENTORY_TYPE=:TYPE AND AUTOGEN_INVENTORY_MASTER_ID=:MASTERID");
		if ("REGION".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
		} else if ("CENTER".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
		} else if ("PROCESS".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
		} else if ("CLIENT".equalsIgnoreCase(inventoryDto.getInventoryType())) {
		    qryObj.setParameter("TYPE", inventoryDto.getInventoryType());
		}
		qryObj.setParameter("MASTERID", inventoryDto.getAutogenInventoryMasterId());
		 result = qryObj.getResultList();
	    }

	    if (result != null && !result.isEmpty()) {
		Object[] obj = result.get(0);
		inventoryMaster = new InventoryMaster();
		if (!CommonUtil.nullRemove(obj[0]).isEmpty()) {
		    inventoryMaster.setAutogenInventoryMasterId(new BigInteger(CommonUtil.nullRemove(obj[0])));
		}
		inventoryMaster.setName(CommonUtil.nullRemove(obj[1]));
		inventoryMaster.setStatus(CommonUtil.nullRemove(obj[2]));
	    }

	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: findInventory() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	return inventoryMaster;
    }

    public InventoryDto getMappedInventoryList() throws Exception {
	List<Object[]> clientResult = null;
	List<Object[]> regionResult = null;
	List<Object[]> centerResult = null;
	List<Object[]> processResult = null;
	List<RegionKVDto> regionKVDtoList = new ArrayList<>();
	InventoryDto inventoryDto = new InventoryDto();
	Query qryObj = null;

	try {
	    qryObj = firstEntityManager.createNativeQuery(
		    "SELECT DISTINCT INVENTORY_REGION_ID, INVENTORY_REGION_NAME FROM inventory_mapping ORDER BY INVENTORY_REGION_NAME");
	    regionResult = qryObj.getResultList();
	    for (Object[] regionObj : regionResult) {
		qryObj = null;
		qryObj = firstEntityManager.createNativeQuery(
			"SELECT DISTINCT INVENTORY_CENTER_ID, INVENTORY_CENTER_NAME FROM inventory_mapping WHERE INVENTORY_REGION_ID=:REGIONID ORDER BY INVENTORY_CENTER_NAME");
		qryObj.setParameter("REGIONID", (BigInteger) regionObj[0]);
		centerResult = qryObj.getResultList();
		List<CenterKVDto> centerKVDtoList = new ArrayList<>();

		for (Object[] centerObj : centerResult) {

		    qryObj = firstEntityManager.createNativeQuery(
			    "SELECT DISTINCT INVENTORY_CLIENT_ID, INVENTORY_CLIENT_NAME FROM inventory_mapping WHERE  INVENTORY_REGION_ID=:REGIONID AND  INVENTORY_CENTER_ID=:CENTERID  ORDER BY INVENTORY_CLIENT_NAME");
		    qryObj.setParameter("REGIONID", (BigInteger) regionObj[0]);
		    qryObj.setParameter("CENTERID", (BigInteger) centerObj[0]);

		    clientResult = qryObj.getResultList();
		    List<ClientKVDto> clientKVDtoList = new ArrayList<>();

		    for (Object[] clientObj : clientResult) {
			qryObj = firstEntityManager.createNativeQuery(
				"SELECT DISTINCT INVENTORY_PROCESS_ID, INVENTORY_PROCESS_NAME FROM inventory_mapping WHERE INVENTORY_CLIENT_ID=:CLIENTID AND INVENTORY_REGION_ID=:REGIONID AND INVENTORY_CENTER_ID=:CENTERID ORDER BY INVENTORY_PROCESS_NAME");
			qryObj.setParameter("CENTERID", (BigInteger) centerObj[0]);
			qryObj.setParameter("REGIONID", (BigInteger) regionObj[0]);
			qryObj.setParameter("CLIENTID", (BigInteger) clientObj[0]);
			processResult = qryObj.getResultList();
			List<ProcessKVDto> processKVDtoList = new ArrayList<>();
			for (Object[] processObj : processResult) {
			    ProcessKVDto processKVDto = new ProcessKVDto((BigInteger) processObj[0],
				    (String) processObj[1]);
			    processKVDtoList.add(processKVDto);
			}

			ClientKVDto clientKVDto = new ClientKVDto((BigInteger) clientObj[0], (String) clientObj[1],
				processKVDtoList);
			clientKVDtoList.add(clientKVDto);

		    }
		    CenterKVDto centerKVDto = new CenterKVDto((BigInteger) centerObj[0], (String) centerObj[1],
			    clientKVDtoList);
		    centerKVDtoList.add(centerKVDto);

		}
		RegionKVDto regionKVDto = new RegionKVDto((BigInteger) regionObj[0], (String) regionObj[1],
			centerKVDtoList);
		regionKVDtoList.add(regionKVDto);

	    }

	} catch (Exception e) {
	    logger.info("Exception :: InventoryDAOImpl :: getMappedInventoryList() : " + e);
	} finally {
	    firstEntityManager.close();
	}

	inventoryDto.setResultList(regionKVDtoList);
	return inventoryDto;

    }


}
