package com.cognicx.AppointmentRemainder.service;

import org.asteriskjava.manager.event.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognicx.AppointmentRemainder.ami.callconnecedEvent;
import com.cognicx.AppointmentRemainder.ami.calldialEvent;
import com.cognicx.AppointmentRemainder.ami.callhangupEvent;
import com.cognicx.AppointmentRemainder.dao.CampaignDao;

@Service
public class AsteriskService {


	
	@Autowired
	CampaignDao campaignDao;
	
	private Logger logger = LoggerFactory.getLogger(AsteriskService.class);
	
	public void insertDialContDetails(UserEvent userEvent) {
		try {
			calldialEvent callconnEvent=(calldialEvent) userEvent;
			String calluid=callconnEvent.getCalluid();
			String productID=callconnEvent.getProductid();
			String phone=callconnEvent.getPhone();
			logger.info("AsteriskService  insertDialContDetails Method Invoked");
			campaignDao.insertActiveContDetails(calluid, "Dial", "C_01", phone);
		}catch(Exception e) {
			logger.error("Error in AsteriskService::update Act Cont " + e);
		}
	}
	
	public void insertActiveContDetails(UserEvent userEvent) {
		try {
			callconnecedEvent callconnEvent=(callconnecedEvent) userEvent;
			String calluid=callconnEvent.getCalluid();
			String productID=callconnEvent.getProductid();
			String phone=callconnEvent.getPhone();
			logger.info("AsteriskService  insertActiveContDetails Method Invoked");
			campaignDao.insertActiveContDetails(calluid, "Connected", "C_01", phone);
		}catch(Exception e) {
			logger.error("Error in AsteriskService::update Act Cont " + e);
		}
	}

	public void updateActiveContDetails(UserEvent userEvent) {
		try {
			callhangupEvent callhangEvent=(callhangupEvent) userEvent;
			String calluid=callhangEvent.getCalluid();
			String productID=callhangEvent.getProductid();
			String errocode=callhangEvent.getHangupcause();
			String phone=callhangEvent.getPhone();
			logger.info("Hang UP Cause :"+errocode);
			logger.info("AsteriskService  updateActiveContDetails Method Invoked");
			campaignDao.updateActiveContDetails(calluid, "HangUp", "C_01", phone,errocode);
		}catch(Exception e) {
			logger.error("Error in AsteriskService::update Act Cont " + e);
		}
	}


}
