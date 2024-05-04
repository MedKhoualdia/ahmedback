package com.dance.mo.Controller;

import com.dance.mo.Entities.Notification;
import com.dance.mo.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @MessageMapping("/subscribe")
    @SendTo("/topic/notifications")
    public List<Notification> subscribe() {
        return notificationService.getLatestNotifications();
    }

}

