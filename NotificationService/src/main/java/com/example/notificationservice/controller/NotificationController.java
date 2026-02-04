package com.example.notificationservice.controller;

import com.example.notificationservice.model.DTO.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final JavaMailSender emailSender;
    private final Environment env;

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("spring.mail.username"));
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    @KafkaListener(id = "follow-group", topics = "follow-post-created")
    public void handleFollowPostCreated(NotificationMessage message) {
        logger.info("Follow Post Created for {}", message.getUserEmail());
        sendSimpleMessage(message.getUserEmail(), "Follow Post Created", "Follow Post Created");
    }

    @KafkaListener(id = "register-group", topics = "user-created")
    public void handleUserCreated(NotificationMessage message) {
        logger.info("New User Created for {}", message.getUserEmail());
        sendSimpleMessage(message.getUserEmail(),"New User Created", "New User Created");
    }
}
