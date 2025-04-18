package org.rapid.authservice.service.impl;

import org.rapid.authservice.entity.User;
import org.rapid.authservice.model.Role;
import org.rapid.authservice.repository.UserRepository;
import org.rapid.authservice.service.AuthServiceIf;
import org.rapid.authservice.service.rqrs.*;
import org.rapid.authservice.service.rqrs.http.SaveUserHttpRq;
import org.rapid.authservice.service.rqrs.http.SaveUserHttpRs;
import org.rapid.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements AuthServiceIf {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.restTemplate = restTemplate;
    }

    @Override
    public LoginRs login(LoginRq rq) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        rq.username(),
                        rq.password()
                )
        );
        User user = userRepository.getUserByUsername(rq.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, Object> claims = Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getEmail(),
                "roles", List.of("ROLE_" + user.getRole())
        );

        return new LoginRs(jwtUtil.generateToken(claims, user.getUsername()));
    }

    @Override
    public RegisterRs register(RegisterRq rq) {
        Optional<User> existingUser = userRepository.getUserByUsername(rq.username());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User Already Exist");
        }

        SaveUserHttpRq saveUserHttpRq = SaveUserHttpRq.builder()
                .email(rq.email())
                .firstName(rq.firstName())
                .lastName(rq.lastName())
                .username(rq.username())
                .subscription(rq.role().toString())
                .build();

        ResponseEntity<SaveUserHttpRs> rs = restTemplate.postForEntity("http://user-service/user/save", saveUserHttpRq, SaveUserHttpRs.class);

        if(rs.getStatusCode()!= HttpStatusCode.valueOf(200)){
            throw new HttpServerErrorException(rs.getStatusCode());
        }

        User createdUser = userRepository
                .save(User.builder()
                        .username(rq.username())
                        .firstName(rq.firstName())
                        .lastName(rq.lastName())
                        .email(rq.email())
                        .password(passwordEncoder.encode(rq.password()))
                        .role(rq.role())
                        .createdAt(LocalDateTime.now())
                        .build()
                );
        Map<String, Object> claims = Map.of(
                "firstName", createdUser.getFirstName(),
                "lastName", createdUser.getLastName(),
                "email", createdUser.getEmail(),
                "roles", List.of("ROLE_" + createdUser.getRole())
        );

        return new RegisterRs(jwtUtil.generateToken(claims, createdUser.getUsername()));
    }

    @Override
    public ChangeRoleRs changeRole(ChangeRoleRq rq) {
        Optional<User> optionalUser = userRepository.getUserByUsername(rq.username());
        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("Invalid username");
        }
        User user = optionalUser.get();
        user.setRole(rq.role());

        user = userRepository.save(user);

        return new ChangeRoleRs(user);
    }

}