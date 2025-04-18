package org.rapid.userservice.service.rqrs.alert;

import lombok.Builder;

@Builder
public record SaveAlertLimitsRq(
        float wontFixIssueRatio,

        long weeklyCommitCount,

        long meanPullResponseTime,

        float meanLeadTimePull,

        float responseTimeIssue,

        String username
) {
}
