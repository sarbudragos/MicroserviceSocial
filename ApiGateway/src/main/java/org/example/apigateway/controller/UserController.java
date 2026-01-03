package org.example.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.model.DTO.AuthenticationRequest;
import org.example.apigateway.model.DTO.AuthenticationResponse;
import org.example.apigateway.model.DTO.RegisterRequest;
import org.example.apigateway.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ){
        String result =  userService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(userService.authenticate(request));
    }
}