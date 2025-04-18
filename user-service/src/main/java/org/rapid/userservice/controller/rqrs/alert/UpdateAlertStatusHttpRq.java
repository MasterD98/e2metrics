package org.rapid.userservice.controller.rqrs.alert;

import jakarta.validation.constraints.NotNull;

public record UpdateAlertStatusHttpRq(@NotNull long id, @NotNull boolean isShowed) {
}
