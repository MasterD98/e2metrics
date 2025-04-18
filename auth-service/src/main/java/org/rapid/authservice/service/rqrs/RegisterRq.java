package org.rapid.authservice.service.rqrs;

import org.rapid.authservice.model.Role;

public record RegisterRq(String username,
                         String email,
                         String firstName,
                         String lastName,
                         String password,
                         Role role) {
}
