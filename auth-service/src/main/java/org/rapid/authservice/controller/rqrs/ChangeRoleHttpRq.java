package org.rapid.authservice.controller.rqrs;

import jakarta.validation.constraints.NotNull;
import org.rapid.authservice.model.Role;

public record ChangeRoleHttpRq(@NotNull String username,@NotNull Role role) {
}
