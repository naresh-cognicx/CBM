package com.cognicx.AppointmentRemainder.ami;

import javax.transaction.Transactional;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognicx.AppointmentRemainder.service.impl.CampaignServiceImpl;
@Component
public class AsteriskEventListener extends AbstractManagerEventListener {
	private static Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);


	@Transactional
	protected void handleEvent(ManagerEvent event)
	{
		if(event instanceof NewChannelEvent)
		{
			NewChannelEvent newChannelEvent=(NewChannelEvent) event;
			String calluniqueId=newChannelEvent.getUniqueId();
			String callerNum=newChannelEvent.getConnectedLineNum();
			System.out.println("received asterisk New Channel event");
			logger.info("received asterisk event:::"+ calluniqueId+ "callerNum::  "+callerNum);
			AsteriskEvent asteriskevent=new AsteriskEvent();
			asteriskevent.setCalluniqueId(calluniqueId);
			asteriskevent.setCallerNum(callerNum);
	
		}else if(event instanceof UserEvent) {
			UserEvent userEvent=(UserEvent) event;
			logger.info("received asterisk User event:::"+ event);
		}
		else
		{
			System.out.println("received asterisk event");
			logger.info("received asterisk event:::"+ event);
			
		}
		
	}

}
