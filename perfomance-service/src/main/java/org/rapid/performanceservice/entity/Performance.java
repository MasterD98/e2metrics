package org.rapid.performanceservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "performance")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    private LocalDate timestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "repository_id")
    private Repository repository;

}
