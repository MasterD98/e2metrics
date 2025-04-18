package org.rapid.userservice.controller.rqrs.alert;

import jakarta.validation.constraints.NotNull;

public record SaveAlertLimitsHttpRq(
        @NotNull float wontFixIssueRatio,

        @NotNull long weeklyCommitCount,

        @NotNull long meanPullResponseTime,

        @NotNull float meanLeadTimePull,

        @NotNull float responseTimeIssue,

        @NotNull String username
) {
}
