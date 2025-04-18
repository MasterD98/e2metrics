package org.rapid.userservice.controller.rqrs.alert;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public record GetAlertsHttpRq(@NotNull @RequestParam String username) {
}
