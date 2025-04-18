package org.rapid.userservice.service.rqrs.user.http;

import lombok.Builder;

@Builder
public record GetAccessTokenHttpRq(
        String client_id,
        String client_secret,
        String code

) {
}
