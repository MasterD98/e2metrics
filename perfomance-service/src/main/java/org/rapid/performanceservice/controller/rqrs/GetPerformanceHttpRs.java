package org.rapid.performanceservice.controller.rqrs;

import org.rapid.performanceservice.entity.Performance;

import java.util.List;

public record GetPerformanceHttpRs(List<Performance> performances) {
}
