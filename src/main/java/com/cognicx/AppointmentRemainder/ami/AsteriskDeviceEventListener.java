package com.cognicx.AppointmentRemainder.ami;

import javax.transaction.Transactional;
import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.DeviceStateChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cognicx.AppointmentRemainder.service.impl.CampaignServiceImpl;

@Component
public class AsteriskDeviceEventListener extends AbstractManagerEventListener{
	private static Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

	@Transactional
	public void handleEvent(DeviceStateChangeEvent event)
	{
		 if(event instanceof DeviceStateChangeEvent) {
			logger.info("received asterisk Device state change event:::"+ event);
		}
		else
		{
			System.out.println("received asterisk event");
			logger.info("received asterisk event:::"+ event);
			
		}
		
	}
}
