package org.rapid.userservice.service.rqrs.user;

import lombok.Builder;
import org.rapid.userservice.model.Role;

@Builder
public record SaveUserRq(
        String username,
        String email,
        Role role,
        String firstName,
        String lastName,
        String overviewLayout,
        String comparisonLayout,
        String forecastLayout,
        String githubToken,
        byte[] profilePicture,
        boolean isReportsEnable
) {
}
