package org.rapid.userservice.service.rqrs.alert;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SaveAlertRq(
        LocalDateTime timestamp,
        String message,
        boolean isShowed,
        String username
) {
}
