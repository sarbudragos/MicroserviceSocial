package org.example.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.model.DTO.AuthenticationRequest;
import org.example.apigateway.model.DTO.AuthenticationResponse;
import org.example.apigateway.model.DTO.RegisterRequest;
import org.example.apigateway.model.DTO.RegisterResponse;
import org.example.apigateway.model.User;
import org.example.apigateway.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ){
        String result =  userService.register(request);
        return ResponseEntity.ok(new RegisterResponse(result));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("You are authenticated!");
    }
}