package org.example.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.model.DTO.AuthenticationRequest;
import org.example.apigateway.model.DTO.AuthenticationResponse;
import org.example.apigateway.model.DTO.RegisterRequest;
import org.example.apigateway.model.DTO.RpcRequest;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RabbitTemplate template;

    private final String routingKey = "routing-key";
    private final Exchange exchange;


    public String register(RegisterRequest request){
        RpcRequest rpcRequest = new RpcRequest("register", request);

        return template.convertSendAndReceiveAsType(
                exchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        RpcRequest rpcRequest = new RpcRequest("authenticate", request);

        return template.convertSendAndReceiveAsType(
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );
    }

    public Boolean validate(String token) {
        RpcRequest rpcRequest = new RpcRequest("verify", token);

        return template.convertSendAndReceiveAsType(
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );
    }
}