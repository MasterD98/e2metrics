package org.rapid.userservice.controller.rqrs.payment;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

public record GetPaymentsHttpRq(
        @NotNull @RequestParam String username
) {
}
