package com.tracker.app.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tracker.app.entities.Notification;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;



@Controller
public class NotificationController {
	
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public Notification send(final Notification notification) throws Exception {
        return notification;
    }
 // 
    @MessageMapping("/private/{username}")
    @SendToUser("/all/private/{username}")
    public void sendToSpecificUser(@Payload Notification notification) {
        simpMessagingTemplate.convertAndSendToUser("manager", "/specific/manager", "i am a notifihshsh");
    }
    
    @GetMapping(value = "/myfile.js",produces = "application/javascript")

    @ResponseBody
    
    public byte[] getMyFile() throws IOException {
        Resource resource = new ClassPathResource("static/notif.js");
        Path filePath = resource.getFile().toPath();

        return Files.readAllBytes(filePath);
    }
}
