package org.rapid.userservice.controller.rqrs.payment;

import jakarta.validation.constraints.NotNull;
import org.rapid.userservice.model.Role;

import java.time.LocalDateTime;

public record SavePaymentHttpRq (
        @NotNull LocalDateTime timestamp,

        @NotNull long paymentAmount,

        @NotNull String currencyCode,

        @NotNull Role role,

        @NotNull String username) {
}
