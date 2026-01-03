package org.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("user-request",true,true,false);
    }

    @Bean public Exchange exchange()
    {
        return new DirectExchange("user-exchange");
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange)
    {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("routing-key")
                .noargs();
    }
}
