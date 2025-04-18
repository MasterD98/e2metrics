package org.rapid.authservice.controller;

import jakarta.validation.Valid;
import org.rapid.authservice.controller.rqrs.*;
import org.rapid.authservice.dto.UserDto;
import org.rapid.authservice.service.AuthServiceIf;
import org.rapid.authservice.service.rqrs.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceIf authService;

    public AuthController(AuthServiceIf authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    ResponseEntity<LoginHttpRs> login(@RequestBody @Valid LoginHttpRq rq){
        LoginRs rs = authService.login(new LoginRq(rq.username(),rq.password()));
        return ResponseEntity.ok(new LoginHttpRs(rs.token()));
    }
    @PostMapping("register")
    ResponseEntity<RegisterHttpRs> register(@RequestBody @Valid RegisterHttpRq rq){
        RegisterRs rs = authService.register(
                new RegisterRq(
                        rq.username(),
                        rq.email(),
                        rq.firstName(),
                        rq.lastName(),
                        rq.password(),
                        rq.role()
                )
        );
        return ResponseEntity.ok(new RegisterHttpRs(rs.token()));
    }
    @PostMapping("role")
    ResponseEntity<ChangeRoleHttpRs> changeRole(@Valid @RequestBody ChangeRoleHttpRq rq){
        ChangeRoleRs rs = authService.changeRole(new ChangeRoleRq(rq.username(),rq.role()));
        return ResponseEntity.ok(new ChangeRoleHttpRs(UserDto.builder()
                .createdAt(rs.user().getCreatedAt())
                .email(rs.user().getEmail())
                .firstName(rs.user().getFirstName())
                .lastName(rs.user().getLastName())
                .role(rs.user().getRole())
                .username(rs.user().getUsername())
                .build()
        )
        );
    }
}
