package org.rapid.performanceservice.service;

import org.rapid.performanceservice.service.rqrs.GetPerformanceRq;
import org.rapid.performanceservice.service.rqrs.GetPerformanceRs;

public interface PerformanceServiceIf {
    GetPerformanceRs getPerformance(GetPerformanceRq rq);
}
