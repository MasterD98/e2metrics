package org.rapid.performanceservice.service.rqrs;

public record DeleteRepositoryRq(String owner,String repo,String username) {
}
