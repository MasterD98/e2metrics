package org.rapid.performanceservice.controller.rqrs;

import jakarta.validation.constraints.NotNull;

public record AddRepositoryHttpRq(@NotNull String owner,@NotNull String repo,@NotNull String username) {
}
