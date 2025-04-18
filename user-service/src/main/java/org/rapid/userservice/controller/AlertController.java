package org.rapid.userservice.controller;

import jakarta.validation.Valid;
import org.rapid.userservice.controller.rqrs.alert.*;
import org.rapid.userservice.service.AlertServiceIf;
import org.rapid.userservice.service.rqrs.alert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/alert")
public class AlertController {
    private final AlertServiceIf alertService;

    @Autowired
    public AlertController(AlertServiceIf alertService) {
        this.alertService = alertService;
    }

    @GetMapping("limit")
    ResponseEntity<GetAlertLimitsHttpRs> getAlertLimits(@Valid GetUserAlertLimitsHttpRq rq){
        GetAlertLimitsRs rs = alertService.getAlertLimits(new GetAlertLimitsRq(rq.username()));
        return ResponseEntity.ok(new GetAlertLimitsHttpRs(rs.alertLimits()));
    }

    @PostMapping("limit/save")
    ResponseEntity<SaveAlertLimitsHttRs> saveAlertLimits(@Valid @RequestBody SaveAlertLimitsHttpRq rq){
        SaveAlertLimitsRs rs = alertService.saveAlertLimits(
                SaveAlertLimitsRq.builder()
                        .meanPullResponseTime(rq.meanPullResponseTime())
                        .responseTimeIssue(rq.responseTimeIssue())
                        .meanLeadTimePull(rq.meanLeadTimePull())
                        .weeklyCommitCount(rq.weeklyCommitCount())
                        .wontFixIssueRatio(rq.wontFixIssueRatio())
                        .username(rq.username())
                        .build()
        );
        return ResponseEntity.ok(new SaveAlertLimitsHttRs(rs.alertLimits()));
    }
    @GetMapping("")
    ResponseEntity<GetAlertsHttpRs> getAlerts(@Valid GetAlertsHttpRq rq){
        GetAlertsRs rs =alertService.getAlerts(new GetAlertsRq(rq.username()));
        return ResponseEntity.ok(new GetAlertsHttpRs(rs.alerts()));
    }

    @PostMapping("save")
    ResponseEntity<SaveAlertHttpRs> saveAlert(@Valid @RequestBody SaveAlertHttpRq rq){
        SaveAlertRs rs = alertService.saveAlert(SaveAlertRq.builder()
                        .message(rq.message())
                        .timestamp(rq.timestamp())
                        .username(rq.username())
                        .isShowed(rq.isShowed())
                .build());
        return ResponseEntity.ok(new SaveAlertHttpRs(rs.alert()));
    }
    @PutMapping("update")
    ResponseEntity<UpdateAlertStatusHttpRs> updateAlertStatus(@Valid @RequestBody UpdateAlertStatusHttpRq rq){
        UpdateAlertStatusRs rs= alertService.updateAlertStatus(new UpdateAlertStatusRq(rq.id(), rq.isShowed()));
        return ResponseEntity.ok(new UpdateAlertStatusHttpRs(rs.success()));
    }
}
