package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.model.Notification;
import com.bgrfacile.bgrsignapi.model.User;
import com.bgrfacile.bgrsignapi.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification sendNotification(User user, String title, String message, String relatedEntityType, Long relatedEntityId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedEntityType(relatedEntityType);
        notification.setRelatedEntityId(relatedEntityId);
        notification.setIsRead(false);
        return notificationRepository.save(notification);
    }

//    public List<Notification> getUserNotifications(Long userId) {
//        return notificationRepository.findByUserId(userId);
//    }
}
