package org.rapid.performanceservice.controller.rqrs;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

public record DeleteRepositoryHttpRq(@NotNull @RequestParam String owner,
                                     @NotNull @RequestParam String repo,
                                     @NotNull @RequestParam String username) {
}
