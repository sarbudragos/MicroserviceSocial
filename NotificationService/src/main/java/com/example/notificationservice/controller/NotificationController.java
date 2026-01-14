package com.example.notificationservice.controller;

import com.example.notificationservice.model.DTO.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @KafkaListener(id = "notification-group", topics = "follow-post-created")
    public void handleFollowPostCreated(NotificationMessage message) {
        logger.info("Follow Post Created for {}", message.getUserEmail());
    }

    @KafkaListener(id = "notification-group", topics = "user-created")
    public void handleUserCreated(NotificationMessage message) {
        logger.info("New User Created for {}", message.getUserEmail());
    }
}
