package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.model.DTO.AuthenticationRequest;
import org.example.userservice.model.DTO.AuthenticationResponse;
import org.example.userservice.model.DTO.RegisterRequest;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserServiceController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ){
        //Registers a user
        userService.register(request);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ){
        //Returns the jwt token of the user if username and password are correct
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
