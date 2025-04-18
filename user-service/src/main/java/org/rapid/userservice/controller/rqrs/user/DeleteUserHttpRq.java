package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;

public record DeleteUserHttpRq(@NotNull @PathVariable String username) {
}
