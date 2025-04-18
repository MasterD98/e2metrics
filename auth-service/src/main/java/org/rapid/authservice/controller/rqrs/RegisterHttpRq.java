package org.rapid.authservice.controller.rqrs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.rapid.authservice.model.Role;

public record RegisterHttpRq(@NotNull(message = "username cannot be null") String username,
                             @NotNull(message = "email cannot be null") @Email(message = "must be valid email") String email,
                             @NotNull(message = "first name cannot be null") String firstName,
                             @NotNull(message = "last name cannot be null") String lastName,
                             @NotNull(message = "password cannot be null") String password,
                             @NotNull(message = "role cannot be null") Role role) {
}
