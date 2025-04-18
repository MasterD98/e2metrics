package org.rapid.performanceservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PerformanceDTO {
    private float issuesFixingFrequency;

    private float bugFixRatio;

    private int commitCount;

    private float meanLeadFixTime;

    private int pullRequestFrequency;

    private int weeklyCommitCount;

    private int openedIssuesCount;

    private int allIssuesCount;

    private float wontFixIssuesRatio;

    private int meanPullRequestResponseTime;

    private int pullRequestCount;

    private float meanLeadTimeForPulls;

    private float responseTimeForIssue;
}
