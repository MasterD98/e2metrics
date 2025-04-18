package org.rapid.userservice.service.rqrs.payment;

import lombok.Builder;
import org.rapid.userservice.model.Role;

import java.time.LocalDateTime;

@Builder
public record SavePaymentRq(
        LocalDateTime timestamp,
        long paymentAmount,
        String currencyCode,
        Role role,
        String username) {
}
