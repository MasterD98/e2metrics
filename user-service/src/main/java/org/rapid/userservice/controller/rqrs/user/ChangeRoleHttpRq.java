package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;
import org.rapid.userservice.model.Role;

public record ChangeRoleHttpRq(@NotNull String username,@NotNull Role role) {
}
