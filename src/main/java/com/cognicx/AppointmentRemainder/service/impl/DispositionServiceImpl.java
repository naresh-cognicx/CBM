package com.cognicx.AppointmentRemainder.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.Request.DispositionCodeDet;
import com.cognicx.AppointmentRemainder.Request.DispositionDetRequest;
import com.cognicx.AppointmentRemainder.dao.DispositionDao;
import com.cognicx.AppointmentRemainder.response.GenericResponse;
import com.cognicx.AppointmentRemainder.service.DispositionService;

@Service
public class DispositionServiceImpl implements DispositionService{
	@Autowired
	DispositionDao dispositionDao;
	
	private Logger logger = LoggerFactory.getLogger(DispositionServiceImpl.class);

	@Override
	public ResponseEntity<GenericResponse> updateDisposition(DispositionDetRequest dispostionDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			String isUpdated = dispositionDao.updateDispositionDetail(dispostionDetRequest);
			if (isUpdated!=null && isUpdated.equalsIgnoreCase("updated")) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Disposition updated successfully");
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while updating Disposition");
			}
		} catch (Exception e) {
			logger.error("Error in DispositionServiceImpl::updateDisposition " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while updating Disposition");
		}
		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}


	
	@Override
	public ResponseEntity<GenericResponse> getDispositionDetail() {
		GenericResponse genericResponse = new GenericResponse();
		List<DispositionDetRequest> dispositionDetList = null;
		try {
			dispositionDetList = getDispostionDetList();
			genericResponse.setStatus(200);
			genericResponse.setValue(dispositionDetList);
			genericResponse.setMessage("Success");
		} catch (Exception e) {
			logger.error("Error in DispositionServiceImpl::getDispositionDetail " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("No data Found");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}
	
	//@Override
	public List<DispositionDetRequest> getDispostionDetList() {
		List<DispositionDetRequest> dispositionDetList;
		dispositionDetList = new ArrayList<>();
		List<Object[]> dispositionDetObjList = dispositionDao.getDispositionDetail();
		if (dispositionDetObjList != null && !dispositionDetObjList.isEmpty()) {
			for (Object[] obj : dispositionDetObjList) {
				DispositionDetRequest dispositionDetRequest = new DispositionDetRequest();
				String dispId=String.valueOf(obj[0]);
				dispositionDetRequest.setDispId(dispId);
				 dispositionDetRequest.setDispositionName(String.valueOf(obj[1]));
				 dispositionDetRequest.setDescription(String.valueOf(obj[2]));
				
				dispositionDetRequest.setDispCodeDetailsList(getDispositionCodeDetails(dispId));
				System.out.println("Disposition Details :"+dispositionDetRequest.toString());
				logger.info("Disposition Details :"+dispositionDetRequest.toString());
				dispositionDetList.add(dispositionDetRequest);
			}
		}
		return dispositionDetList;
	}
	
	public List<DispositionCodeDet> getDispositionCodeDetails(String dispId){
		List<DispositionCodeDet> dispCodeDetailsList=new ArrayList<>();
		List<Object[]> dispositionCodeDetObjList = dispositionDao.getDispositionCodeDetail(dispId);
		if (dispositionCodeDetObjList != null && !dispositionCodeDetObjList.isEmpty()) {
			for (Object[] obj : dispositionCodeDetObjList) {
				DispositionCodeDet codeRequest = new DispositionCodeDet();
				codeRequest.setDispId(dispId);
				codeRequest.setCode(String.valueOf(obj[0]));
				codeRequest.setItemName(String.valueOf(obj[1]));
				codeRequest.setDispCodeId(String.valueOf(obj[2]));
				dispCodeDetailsList.add(codeRequest);
			}
		}
		return dispCodeDetailsList;
	}

	@Override
	public ResponseEntity<GenericResponse> createDisposition(DispositionDetRequest dispostionDetRequest) {
		GenericResponse genericResponse = new GenericResponse();
		try {
			String DispositionId =  dispositionDao.creatDispositionDetail(dispostionDetRequest);
			if (DispositionId != null) {
				genericResponse.setStatus(200);
				genericResponse.setValue("Success");
				genericResponse.setMessage("Disposition created successfully, Disposition Id: " + DispositionId);
			} else {
				genericResponse.setStatus(400);
				genericResponse.setValue("Failure");
				genericResponse.setMessage("Error occured while creating DispositionId");
			}
		} catch (Exception e) {
			logger.error("Error in DispositionServiceImpl::DispositionId " + e);
			genericResponse.setStatus(400);
			genericResponse.setValue("Failure");
			genericResponse.setMessage("Error occured while creating DispositionId");
		}

		return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
	}

}
