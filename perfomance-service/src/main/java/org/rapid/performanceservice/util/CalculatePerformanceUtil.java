package org.rapid.performanceservice.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rapid.performanceservice.dto.PerformanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CalculatePerformanceUtil {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Map<String, Integer> WEIGHTS = new HashMap<>() {{
        put("bug", 10);
        put("documentation", 2);
        put("duplicate", 0);
        put("enhancement", 8);
        put("good first issue", 6);
        put("help wanted", 5);
        put("invalid", 4);
        put("question", 7);
        put("wontfix", 0);
    }};
    
    public PerformanceDTO getPerformances(String owner, String repo, String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("X-GitHub-Api-Version", "2022-11-28");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        float issuesFixingFrequency = getIssuesFixingFrequency(owner,repo,entity);
        float bugFixRatio = getBugFixRatio(owner,repo,entity);
        int commitCount = getCommitCount(owner,repo,entity);
        float meanLeadFixTime = getMeanLeadFixTime(owner,repo,entity);
        int pullRequestFrequency = getPullRequestFrequency(owner,repo,entity);
        int weeklyCommitCount = getWeeklyCommitCount(owner,repo,entity);
        int openedIssuesCount = getOpenedIssuesCount(owner,repo,entity);
        int allIssuesCount = getAllIssuesCount(owner,repo,entity);
        float wontFixIssuesRatio = getWontFixIssuesRatio(owner,repo,entity);
        int meanPullRequestResponseTime = getMeanPullRequestResponseTime(owner,repo,entity);
        int pullRequestCount = getPullRequestCount(owner,repo,entity);
        float meanLeadTimeForPulls = getMeanLeadTimeForPulls(owner,repo,entity);
        float responseTimeForIssue = getResponseTimeForIssue(owner,repo,entity);
        
        return PerformanceDTO.builder()
                .allIssuesCount(allIssuesCount)
                .bugFixRatio(bugFixRatio)
                .commitCount(commitCount)
                .issuesFixingFrequency(issuesFixingFrequency)
                .meanLeadFixTime(meanLeadFixTime)
                .meanLeadTimeForPulls(meanLeadTimeForPulls)
                .meanPullRequestResponseTime(meanPullRequestResponseTime)
                .openedIssuesCount(openedIssuesCount)
                .pullRequestCount(pullRequestCount)
                .pullRequestFrequency(pullRequestFrequency)
                .responseTimeForIssue(responseTimeForIssue)
                .weeklyCommitCount(weeklyCommitCount)
                .wontFixIssuesRatio(wontFixIssuesRatio)
                .build();
    }

    private int getCommitCount(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {

        String url = String.format("https://api.github.com/repos/%s/%s/commits", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.size(); // commit count from the response array
        } else {
            throw new RuntimeException("Failed to fetch commits: " + response.getStatusCode());
        }
    }

    private float getIssuesFixingFrequency(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {

        // Get all issues (open + closed)
        String allIssuesUrl = String.format("https://api.github.com/repos/%s/%s/issues?state=all&per_page=100", owner, repo);
        ResponseEntity<String> allIssuesResponse = restTemplate.exchange(allIssuesUrl, HttpMethod.GET, httpEntity, String.class);
        JsonNode allIssues = objectMapper.readTree(allIssuesResponse.getBody());
        int totalIssuesCount = allIssues.size();

        // Get only closed issues

        String closedIssuesUrl = String.format("https://api.github.com/repos/%s/%s/issues?state=closed&per_page=100", owner, repo);
        ResponseEntity<String> closedIssuesResponse = restTemplate.exchange(closedIssuesUrl, HttpMethod.GET, httpEntity, String.class);
        JsonNode closedIssues = objectMapper.readTree(closedIssuesResponse.getBody());

        int fixedIssuesCount = 0;
        for (JsonNode issue : closedIssues) {
            // Ignore if it's a Pull Request (they contain the "pull_request" field)
            if (!issue.has("pull_request")) {
                fixedIssuesCount++;
            }
        }

        return totalIssuesCount == 0 ? 0 : (float) fixedIssuesCount / totalIssuesCount;
    }

    private float getBugFixRatio(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {

        float fixedIssues = 0f;
        int totalWeightedIssues = 0;

        // All issues (state=all)
        String allIssuesUrl = String.format("https://api.github.com/repos/%s/%s/issues?state=all&per_page=100", owner, repo);
        ResponseEntity<String> allResponse = restTemplate.exchange(allIssuesUrl, HttpMethod.GET, httpEntity, String.class);
        JsonNode allIssues = objectMapper.readTree(allResponse.getBody());

        for (JsonNode issue : allIssues) {
            if (!issue.has("labels")) continue;

            for (JsonNode label : issue.get("labels")) {
                String labelName = label.get("name").asText();
                if (WEIGHTS.containsKey(labelName)) {
                    totalWeightedIssues += WEIGHTS.get(labelName);
                }
            }
        }

        // Closed issues
        String closedIssuesUrl = String.format("https://api.github.com/repos/%s/%s/issues?state=closed&per_page=100", owner, repo);
        ResponseEntity<String> closedResponse = restTemplate.exchange(closedIssuesUrl, HttpMethod.GET, httpEntity, String.class);
        JsonNode closedIssues = objectMapper.readTree(closedResponse.getBody());

        for (JsonNode issue : closedIssues) {
            // Skip PRs
            if (issue.has("pull_request")) continue;

            if (!issue.has("labels")) continue;

            for (JsonNode label : issue.get("labels")) {
                String labelName = label.get("name").asText();
                if (WEIGHTS.containsKey(labelName)) {
                    fixedIssues += WEIGHTS.get(labelName);
                }
            }
        }

        return totalWeightedIssues == 0 ? 0f : fixedIssues / totalWeightedIssues;
    }
    private float getMeanLeadFixTime(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {

        // Fetch closed issues
        String url = String.format("https://api.github.com/repos/%s/%s/issues?state=closed&per_page=100", owner, repo);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        JsonNode issues = objectMapper.readTree(response.getBody());

        if (!issues.isArray() || issues.isEmpty()) return 0;

        long totalFixTimeHours = 0;
        int count = 0;

        for (JsonNode issue : issues) {
            try {
                if (!issue.has("created_at") || !issue.has("closed_at")) continue;

                String createdAt = issue.get("created_at").asText();
                String closedAt = issue.get("closed_at").asText();

                OffsetDateTime createdTime = OffsetDateTime.parse(createdAt);
                OffsetDateTime closedTime = OffsetDateTime.parse(closedAt);

                long hoursBetween = ChronoUnit.HOURS.between(createdTime, closedTime);
                totalFixTimeHours += hoursBetween;
                count++;
            } catch (Exception e) {
                // Skip faulty records
            }
        }

        return count == 0 ? 0 : (float) totalFixTimeHours / count;
    }
    private int getPullRequestFrequency(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls?state=all&per_page=100", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        JsonNode pullRequests = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        if (!pullRequests.isArray() || pullRequests.isEmpty()) return 0;

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        int currentMonth = now.getMonthValue();
        int frequency = 0;

        for (JsonNode pr : pullRequests) {
            try {
                if (!pr.has("created_at")) continue;

                String createdAt = pr.get("created_at").asText();
                OffsetDateTime createdTime = OffsetDateTime.parse(createdAt);

                if (createdTime.getMonthValue() == currentMonth && createdTime.getYear() == now.getYear()) {
                    frequency++;
                }
            } catch (Exception e) {
                // Skip bad data
            }
        }

        return frequency;
    }
    private int getWeeklyCommitCount(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/stats/participation", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        JsonNode all = root.get("all");
        if (all == null || !all.isArray() || all.isEmpty()) {
            return 0;
        }

        // Get the last element (current week's commits)
        return all.get(all.size() - 1).asInt();
    }

    private int getOpenedIssuesCount(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/issues?state=open", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        if (!root.isArray()) {
            throw new IllegalStateException("Unexpected response format from GitHub API");
        }

        return root.size(); // Number of open issues
    }
    private int getAllIssuesCount(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/issues?state=all", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        if (!root.isArray()) {
            throw new IllegalStateException("Unexpected response format from GitHub API");
        }

        return root.size(); // Total number of issues
    }
    private float getWontFixIssuesRatio(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {

        // Get all issues
        String allIssuesUrl = String.format("https://api.github.com/repos/%s/%s/issues?state=all", owner, repo);
        ResponseEntity<String> allIssuesResponse = restTemplate.exchange(allIssuesUrl, HttpMethod.GET, httpEntity, String.class);
        JsonNode allIssues = objectMapper.readTree(Objects.requireNonNull(allIssuesResponse.getBody()));
        int totalIssues = allIssues.size();

        // Get wontfix issues
        String wontFixUrl = String.format("https://api.github.com/repos/%s/%s/issues?state=all&labels=wontfix", owner, repo);
        ResponseEntity<String> wontFixResponse = restTemplate.exchange(wontFixUrl, HttpMethod.GET, httpEntity, String.class);
        JsonNode wontFixIssues = objectMapper.readTree(Objects.requireNonNull(wontFixResponse.getBody()));
        int wontFixCount = wontFixIssues.size();

        // Calculate ratio
        return totalIssues == 0 ? 0 : (float) wontFixCount / totalIssues;
    }

    private int getMeanPullRequestResponseTime(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls?state=all", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JsonNode pullRequests = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        if (!pullRequests.isArray() || pullRequests.isEmpty()) {
            return 0;
        }

        long totalResponseTimeSeconds = 0;

        for (JsonNode pr : pullRequests) {
            try {
                Instant createdAt = Instant.parse(pr.get("created_at").asText());
                Instant updatedAt = Instant.parse(pr.get("updated_at").asText());
                long diffInSeconds = Duration.between(createdAt, updatedAt).getSeconds();
                totalResponseTimeSeconds += diffInSeconds;
            } catch (Exception e) {
                // skip any PRs with missing or malformed timestamps
                continue;
            }
        }

        return (int) (totalResponseTimeSeconds / pullRequests.size());
    }

    private int getPullRequestCount(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JsonNode pullRequests = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        // Return the size of the pull request list (count of pull requests)
        return pullRequests.size();
    }

    private float getMeanLeadTimeForPulls(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JsonNode pullRequests = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        // Extract the creation times of pull requests
        long totalLeadTime = 0;
        int pullCount = pullRequests.size();
        Instant[] createdTimes = new Instant[pullCount];

        for (int i = 0; i < pullCount; i++) {
            String createdAt = pullRequests.get(i).get("created_at").asText();
            createdTimes[i] = Instant.parse(createdAt);
        }

        // Calculate the total lead time between consecutive pull request creation times
        for (int i = 0; i < pullCount - 1; i++) {
            long leadTime = ChronoUnit.SECONDS.between(createdTimes[i], createdTimes[i + 1]);
            totalLeadTime += leadTime;
        }

        return (pullCount > 0) ? (float) totalLeadTime / pullCount : 0;
    }
    private float getResponseTimeForIssue(String owner, String repo, HttpEntity<String> httpEntity) throws Exception {
        String url = String.format("https://api.github.com/repos/%s/%s/issues?state=all", owner, repo);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JsonNode issuesData = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        float totalTime = 0;
        int issueCount = issuesData.size();

        for (int i = 0; i < issueCount; i++) {
            String createdAt = issuesData.get(i).get("created_at").asText();
            Instant createdAtTime = Instant.parse(createdAt);
            String eventsUrl = issuesData.get(i).get("events_url").asText().substring(22);

            // Fetching events for each issue
            String eventUrl = "https://api.github.com" + eventsUrl;
            ResponseEntity<String> eventResponse = restTemplate.exchange(eventUrl, HttpMethod.GET, httpEntity, String.class);
            JsonNode eventData = objectMapper.readTree(Objects.requireNonNull(eventResponse.getBody()));

            if (!eventData.isEmpty()) {
                String eventCreatedAt = eventData.get(0).get("created_at").asText();
                Instant firstEventTime = Instant.parse(eventCreatedAt);

                long responseTimeInSeconds = createdAtTime.until(firstEventTime, java.time.temporal.ChronoUnit.SECONDS);
                totalTime += responseTimeInSeconds;
            }
        }

        return (issueCount > 0) ? totalTime / issueCount : 0;
    }
}
