package org.rapid.performanceservice.service.rqrs;

public record GetPerformanceRq(String owner,String repo,String username,int limit) {
}
