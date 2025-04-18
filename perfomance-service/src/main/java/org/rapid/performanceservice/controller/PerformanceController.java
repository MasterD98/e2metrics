package org.rapid.performanceservice.controller;

import jakarta.validation.Valid;
import org.rapid.performanceservice.controller.rqrs.GetPerformanceHttpRq;
import org.rapid.performanceservice.controller.rqrs.GetPerformanceHttpRs;
import org.rapid.performanceservice.service.PerformanceServiceIf;
import org.rapid.performanceservice.service.rqrs.GetPerformanceRq;
import org.rapid.performanceservice.service.rqrs.GetPerformanceRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("performance")
public class PerformanceController {

    private final PerformanceServiceIf performanceService;

    public PerformanceController(PerformanceServiceIf performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("")
    ResponseEntity<GetPerformanceHttpRs> getPerformance(@Valid GetPerformanceHttpRq rq){
        GetPerformanceRs rs = performanceService.getPerformance(new GetPerformanceRq(rq.owner(),rq.repo(),rq.username(),rq.limit()));
        return ResponseEntity.ok(new GetPerformanceHttpRs(rs.performances()));
    }
}
