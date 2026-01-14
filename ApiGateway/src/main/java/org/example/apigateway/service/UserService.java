package org.example.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.model.DTO.AuthenticationRequest;
import org.example.apigateway.model.DTO.AuthenticationResponse;
import org.example.apigateway.model.DTO.RegisterRequest;
import org.example.apigateway.model.DTO.RpcRequest;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final RabbitTemplate template;

    private final String routingKey = "routing-key";

    private final Exchange userExchange;
    ObjectMapper objectMapper = new ObjectMapper();


    public String register(RegisterRequest request){
        RpcRequest rpcRequest = new RpcRequest("register", objectMapper.writeValueAsString(request));

        return template.convertSendAndReceiveAsType(
                userExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        RpcRequest rpcRequest = new RpcRequest("authenticate", objectMapper.writeValueAsString(request));

        String token = template.convertSendAndReceiveAsType(
                userExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );

        return new AuthenticationResponse(token);
    }

    public String validate(String token) {
        RpcRequest rpcRequest = new RpcRequest("validate_jwt", token);

        return template.convertSendAndReceiveAsType(
                userExchange.getName(),
                routingKey,
                rpcRequest,
                new CorrelationData(UUID.randomUUID().toString()),
                new ParameterizedTypeReference<>() {}
        );
    }
}