package com.example.notificationservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic followPostCreatedTopic() {
        return new NewTopic("follow-post-created", 1, (short) 1);
    }

    @Bean
    public NewTopic newuserCreatedTopic() {
        return new NewTopic("user-created", 1, (short) 1);
    }
}
