package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;

public record UpdateUserHttpRq(@NotNull String username,
                               String email,
                               String firstName,
                               String lastName,
                               String overviewLayout,
                               String comparisonLayout,
                               String forecastLayout,
                               byte[] profilePicture) {
}
