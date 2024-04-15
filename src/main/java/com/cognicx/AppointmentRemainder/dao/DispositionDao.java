package com.cognicx.AppointmentRemainder.dao;

import java.util.List;

import com.cognicx.AppointmentRemainder.Request.DispositionDetRequest;


public interface DispositionDao {
	List<Object[]> getDispositionDetail();
	List<Object[]> getDispositionCodeDetail(String DispName);
	String creatDispositionDetail(DispositionDetRequest dispositionDetRequest);
	String updateDispositionDetail(DispositionDetRequest dispositionDetRequest);
}
