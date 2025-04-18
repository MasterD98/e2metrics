package org.rapid.performanceservice.util.rqrs;

import lombok.Builder;

@Builder
public record GetGithubTokenHttpRs(String accessToken){
}
