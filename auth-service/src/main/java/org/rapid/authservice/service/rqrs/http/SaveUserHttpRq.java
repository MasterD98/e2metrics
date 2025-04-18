package org.rapid.authservice.service.rqrs.http;

import lombok.Builder;

@Builder
public record SaveUserHttpRq(String username, String email,
                             String firstName,
                             String lastName,
                             String subscription) {
}
