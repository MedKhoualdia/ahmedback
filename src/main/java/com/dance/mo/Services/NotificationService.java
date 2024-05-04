package com.dance.mo.Services;

import com.dance.mo.Entities.Notification;
import com.dance.mo.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(Notification notification) {
        notificationRepository.save(notification);

    }
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getLatestNotifications() {

        return notificationRepository.findBySeenFalse();
    }

    public Notification getNotification(Long notificationId) {
        return notificationRepository.getById(notificationId);
    }
}
