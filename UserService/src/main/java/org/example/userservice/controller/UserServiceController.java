package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.model.DTO.AuthenticationRequest;
import org.example.userservice.model.DTO.RegisterRequest;
import org.example.userservice.model.DTO.RpcRequest;
import org.example.userservice.model.User;
import org.example.userservice.service.JwtService;
import org.example.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import tools.jackson.databind.ObjectMapper;


@Controller
@RequiredArgsConstructor
public class UserServiceController {

    private final UserService userService;
    private final JwtService jwtService;
    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(UserServiceController.class);

    @RabbitListener(queues = "user-request")
    public String userRequest(RpcRequest request) {
        String command = request.getCommand();
        return switch (command) {
            case "register" -> {
                try {
                    userService.register(objectMapper.readValue(request.getPayload(), RegisterRequest.class));
                } catch (Exception e) {
                    logger.error(e.toString());
                    yield "Registration failed.";
                }
                yield "Registered successfully!";
            }
            case "authenticate" ->{

                try {
                    yield userService.authenticate(
                            objectMapper.readValue(
                                    request.getPayload(),
                                    AuthenticationRequest.class
                            )

                    );

                } catch (Exception e) {
                    yield "login failed";
                }
            }
            case "validate_jwt" -> {
                String token = request.getPayload();
                UserDetails userDetails = jwtService.validateJwt(token);
                if(userDetails != null){
                    yield objectMapper.writeValueAsString(jwtService.validateJwt(token));
                }
                yield "invalid";
            }
            case "get_user" -> {
                Integer userId = objectMapper.readValue(request.getPayload(), Integer.class);
                try {
                    User user = userService.getUser(userId);
                    yield objectMapper.writeValueAsString(user);
                }
                catch (Exception e){
                    logger.error(e.toString());
                    yield "error";
                }
            }
            default -> "Unknown command";
        };
    }
}
