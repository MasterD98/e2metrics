package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;
import org.rapid.userservice.model.Role;

public record SaveUserHttpRq (
        @NotNull String username,
        @NotNull String email,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull Role role,
        String overviewLayout,
        String comparisonLayout,
        String forecastLayout,
        String githubToken,
        byte[] profilePicture,
        boolean isReportsEnable){
}
