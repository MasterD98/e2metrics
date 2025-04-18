package org.rapid.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="alert_limit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertLimits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "wont_fix_issue_ratio")
    private float wontFixIssueRatio;

    @Column(name = "weekly_commit_count")
    private long weeklyCommitCount;

    @Column(name = "mean_pull_response_time")
    private long meanPullResponseTime;

    @Column(name = "mean_lead_time_pull")
    private float meanLeadTimePull;

    @Column(name = "response_time_issue")
    private float responseTimeIssue;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
