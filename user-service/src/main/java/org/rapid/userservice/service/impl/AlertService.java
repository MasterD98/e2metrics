package org.rapid.userservice.service.impl;

import jakarta.transaction.Transactional;
import org.rapid.userservice.entity.Alert;
import org.rapid.userservice.entity.AlertLimits;
import org.rapid.userservice.entity.User;
import org.rapid.userservice.repository.AlertLimitsRepository;
import org.rapid.userservice.repository.AlertRepository;
import org.rapid.userservice.repository.UserRepository;
import org.rapid.userservice.service.AlertServiceIf;
import org.rapid.userservice.service.rqrs.alert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService implements AlertServiceIf {

    private final AlertLimitsRepository alertLimitsRepository;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertLimitsRepository alertLimitsRepository, UserRepository userRepository, AlertRepository alertRepository) {
        this.alertLimitsRepository = alertLimitsRepository;
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public GetAlertLimitsRs getAlertLimits(GetAlertLimitsRq rq) {
        Optional<AlertLimits> alertLimits = alertLimitsRepository.getAlertLimitsByUserUsername(rq.username());
        if(alertLimits.isEmpty()){
            throw new IllegalArgumentException("invalid username");
        }
        return new GetAlertLimitsRs(alertLimits.get());
    }

    @Override
    public SaveAlertLimitsRs saveAlertLimits(SaveAlertLimitsRq rq) {
        Optional<User> user = userRepository.getUserByUsername(rq.username());
        if(user.isEmpty()){
            throw new IllegalArgumentException("invalid username");
        }
        AlertLimits savedAlertLimits = alertLimitsRepository.save(AlertLimits.builder()
                        .user(user.get())
                        .wontFixIssueRatio(rq.wontFixIssueRatio())
                        .meanPullResponseTime(rq.meanPullResponseTime())
                        .responseTimeIssue(rq.responseTimeIssue())
                        .weeklyCommitCount(rq.weeklyCommitCount())
                        .meanLeadTimePull(rq.meanLeadTimePull())
                .build());
        return new SaveAlertLimitsRs(savedAlertLimits);
    }

    @Override
    public GetAlertsRs getAlerts(GetAlertsRq rq) {
        List<Alert> alerts =alertRepository.getAllByUserUsername(rq.username());
        return new GetAlertsRs(alerts);
    }

    @Override
    public SaveAlertRs saveAlert(SaveAlertRq rq) {
        Optional<User> user = userRepository.getUserByUsername(rq.username());
        if(user.isEmpty()){
            throw new IllegalArgumentException("invalid username");
        }

        return new SaveAlertRs(alertRepository.save(
                Alert.builder()
                        .timestamp(rq.timestamp())
                        .isShowed(false)
                        .message(rq.message())
                        .user(user.get())
                        .build()
        ));
    }

    @Transactional
    @Override
    public UpdateAlertStatusRs updateAlertStatus(UpdateAlertStatusRq rq) {
        int updatedRowCount = alertRepository.modifyAlertStatus(rq.id(), rq.isShowed());
        return new UpdateAlertStatusRs(updatedRowCount>0);
    }
}
