package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.model.DTO.AuthenticationRequest;
import org.example.userservice.model.DTO.AuthenticationResponse;
import org.example.userservice.model.DTO.RegisterRequest;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
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


    public void register(RegisterRequest request) {
        //registers a new user
        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .eMail(request.getEmail())
                .build();

        userRepository.save(user);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByUserName(request.getUserName()).orElseThrow();
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
