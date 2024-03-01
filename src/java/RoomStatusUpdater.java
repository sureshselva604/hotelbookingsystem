package com.hotel.booking.Scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.hotel.booking.Service.ServiceInterface;

@Configuration
@EnableScheduling
public class RoomStatusUpdater {

	
	  @Autowired
	    private ServiceInterface serviceInterface;

	    @Scheduled(cron = "0 0 */1 * * *") 
	    public void updateRoomStatuses() {
	    	serviceInterface.updateRoomStatuses();
	    }
}
