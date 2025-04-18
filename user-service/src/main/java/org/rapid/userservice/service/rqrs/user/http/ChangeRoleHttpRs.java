package org.rapid.userservice.service.rqrs.user.http;

import lombok.Getter;
import lombok.Setter;
import org.rapid.userservice.model.Role;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChangeRoleHttpRs {
    private User user;
}

@Setter
@Getter
class User{
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private LocalDateTime createdAt;
}