package org.rapid.performanceservice.schedule;

import org.rapid.performanceservice.dto.PerformanceDTO;
import org.rapid.performanceservice.entity.Performance;
import org.rapid.performanceservice.entity.Repository;
import org.rapid.performanceservice.repository.PerformanceRepository;
import org.rapid.performanceservice.repository.RepoRepository;
import org.rapid.performanceservice.util.CalculatePerformanceUtil;
import org.rapid.performanceservice.util.rqrs.GetGithubTokenHttpRs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
public class Scheduler {

    private final RepoRepository repoRepository;
    private final PerformanceRepository performanceRepository;
    private final CalculatePerformanceUtil calculatePerformanceUtil;
    private  final RestTemplate restTemplate;

    @Autowired
    public Scheduler(RepoRepository repoRepository, PerformanceRepository performanceRepository, CalculatePerformanceUtil calculatePerformanceUtil, RestTemplate restTemplate) {
        this.repoRepository = repoRepository;
        this.performanceRepository = performanceRepository;
        this.calculatePerformanceUtil = calculatePerformanceUtil;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void calculateDailyPerformance() {
        List<Repository> repositoryList = repoRepository.findAll();

        repositoryList.forEach(
                repository -> {
                    String accessToken =  getGithubAccessToken(repository.getUsername());
                    try {
                        PerformanceDTO performanceDTO = calculatePerformanceUtil.getPerformances(repository.getOwner(),repository.getName(),accessToken);
                        Performance performance = Performance.builder()
                                .allIssuesCount(performanceDTO.getAllIssuesCount())
                                .bugFixRatio(performanceDTO.getBugFixRatio())
                                .commitCount(performanceDTO.getCommitCount())
                                .issuesFixingFrequency(performanceDTO.getIssuesFixingFrequency())
                                .meanLeadFixTime(performanceDTO.getMeanLeadFixTime())
                                .meanLeadTimeForPulls(performanceDTO.getMeanLeadTimeForPulls())
                                .meanPullRequestResponseTime(performanceDTO.getMeanPullRequestResponseTime())
                                .openedIssuesCount(performanceDTO.getOpenedIssuesCount())
                                .pullRequestCount(performanceDTO.getPullRequestCount())
                                .pullRequestFrequency(performanceDTO.getPullRequestFrequency())
                                .responseTimeForIssue(performanceDTO.getResponseTimeForIssue())
                                .weeklyCommitCount(performanceDTO.getWeeklyCommitCount())
                                .wontFixIssuesRatio(performanceDTO.getWontFixIssuesRatio())
                                .timestamp(LocalDate.now())
                                .build();
                        performanceRepository.save(performance);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private String getGithubAccessToken(String username){
        String url = String.format("http://user-service/user/githubToken?username=%s",username);
        ResponseEntity<GetGithubTokenHttpRs> rs = restTemplate.getForEntity(url, GetGithubTokenHttpRs.class);

        if (!rs.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("get access token failed");
        }

        return Objects.requireNonNull(rs.getBody()).accessToken();
    }
}
