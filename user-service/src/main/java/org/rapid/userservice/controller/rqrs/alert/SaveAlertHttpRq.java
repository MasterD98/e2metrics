package org.rapid.userservice.controller.rqrs.alert;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SaveAlertHttpRq(
        @NotNull LocalDateTime timestamp,
        @NotNull String message,
        boolean isShowed,
        @NotNull String username) {
}
