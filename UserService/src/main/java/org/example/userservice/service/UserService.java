package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.model.DTO.AuthenticationRequest;
import org.example.userservice.model.DTO.NotificationMessage;
import org.example.userservice.model.DTO.RegisterRequest;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final KafkaTemplate<Object, Object> kafkaTemplate;


    public void register(RegisterRequest request) {
        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .eMail(request.getEmail())
                .build();

        userRepository.saveAndFlush(user);

        kafkaTemplate.send("user-created", new NotificationMessage(
                user.getId(),
                user.getEMail()
        ));
    }

    public String authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByUserName(request.getUserName()).orElseThrow();
        return jwtService.generateToken(user);
    }
}
