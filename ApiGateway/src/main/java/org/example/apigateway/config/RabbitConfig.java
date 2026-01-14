package org.example.apigateway.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue userQueue() {
        return new Queue("user-request",true,false,false);
    }

    @Bean
    public Exchange userExchange()
    {
        return new DirectExchange("user-exchange");
    }

    @Bean
    public Binding userBinding()
    {
        return BindingBuilder.bind(userQueue())
                .to(userExchange())
                .with("routing-key")
                .noargs();
    }

    @Bean
    public Queue postQueue() {
        return new Queue("post-request",true,false,false);
    }

    @Bean
    public Exchange postExchange()
    {
        return new DirectExchange("post-exchange");
    }

    @Bean
    public Binding postBinding()
    {
        return BindingBuilder.bind(postQueue())
                .to(postExchange())
                .with("routing-key")
                .noargs();
    }
}
