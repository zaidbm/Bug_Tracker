package com.tracker.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.tracker.app.entities.Employe;
import com.tracker.app.entities.Notification;
import com.tracker.app.entities.Team;

@Service
public class NotificationService {
	
	SimpMessagingTemplate messagingTemplate;
	@Autowired
	public NotificationService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate=messagingTemplate;
	}
	@MessageMapping("/private")
	public void sendToUser(Employe employe,Notification notification) {
		String username=employe.getUser().getUsername();
    messagingTemplate.convertAndSendToUser(username, "/specific",notification);
	}
	
	public void notifyTeamMembers(Team team,Notification notification) {
		for (Employe employee : team.getAllMembers()) {
			sendToUser(employee,notification);
		}
	}
	
	
	
	
	
	
	
	
	
	
    }



