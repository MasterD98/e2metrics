package org.rapid.performanceservice.service.rqrs;

import org.rapid.performanceservice.entity.Performance;

import java.util.List;

public record GetPerformanceRs(List<Performance> performances) {
}
