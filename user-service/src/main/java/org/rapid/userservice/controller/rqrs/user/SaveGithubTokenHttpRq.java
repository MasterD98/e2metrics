package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;

public record SaveGithubTokenHttpRq(@NotNull String code , @NotNull String username) {
}
