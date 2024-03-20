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
			String campaignName = callconnEvent.getCampaingnname();

			logger.info("AsteriskService  insertDialContDetails Method Invoked");
			logger.info("Call ID :"+calluid);
			logger.info("productID :"+productID);
			logger.info("phone :"+phone);
			logger.info("campaign : "+campaignName);
			campaignDao.insertActiveContDetails(calluid, "Dial", productID, phone,campaignName);
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
			String campaignName = callconnEvent.getCampaingnname();
			logger.info("AsteriskService  insertActiveContDetails Method Invoked");
			logger.info("Call ID :"+calluid);
			logger.info("productID :"+productID);
			logger.info("phone :"+phone);
			logger.info("campaign : "+campaignName);
			campaignDao.insertActiveContDetails(calluid, "Connected", productID, phone,campaignName);
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
			String campaignName = callhangEvent.getCampaingnname();
			logger.info("Hang UP Cause :"+errocode);
			logger.info("AsteriskService  updateActiveContDetails Method Invoked");
			logger.info("AsteriskService  insertActiveContDetails Method Invoked");
			logger.info("Call ID :"+calluid);
			logger.info("productID :"+productID);
			logger.info("phone :"+phone);
			logger.info("campaign : "+campaignName);
			campaignDao.updateActiveContDetails(calluid, "HangUp",productID, phone,errocode,campaignName);
		}catch(Exception e) {
			logger.error("Error in AsteriskService::update Act Cont " + e);
		}
	}
}
