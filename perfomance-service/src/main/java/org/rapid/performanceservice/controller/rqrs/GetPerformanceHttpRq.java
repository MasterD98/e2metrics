package org.rapid.performanceservice.controller.rqrs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.RequestParam;

public record GetPerformanceHttpRq(@NotNull @RequestParam String owner,
                                   @NotNull @RequestParam  String repo,
                                   @RequestParam @NotNull String username,
                                   @RequestParam @Positive int limit) {
}
