package org.rapid.userservice.service.rqrs.user;

import lombok.Builder;

@Builder
public record UpdateUserRq(String username,
                           String email,
                           String firstName,
                           String lastName,
                           String overviewLayout,
                           String comparisonLayout,
                           String forecastLayout,
                           byte[] profilePicture) {
}
