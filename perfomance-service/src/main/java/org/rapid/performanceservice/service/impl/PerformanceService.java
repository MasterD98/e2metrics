package org.rapid.performanceservice.service.impl;

import org.rapid.performanceservice.entity.Performance;
import org.rapid.performanceservice.entity.Repository;
import org.rapid.performanceservice.repository.PerformanceRepository;
import org.rapid.performanceservice.repository.RepoRepository;
import org.rapid.performanceservice.service.PerformanceServiceIf;
import org.rapid.performanceservice.service.rqrs.GetPerformanceRq;
import org.rapid.performanceservice.service.rqrs.GetPerformanceRs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService implements PerformanceServiceIf {
    private final PerformanceRepository performanceRepository;
    private final RepoRepository repoRepository;

    @Autowired
    public PerformanceService(PerformanceRepository performanceRepository, RepoRepository repoRepository) {
        this.performanceRepository = performanceRepository;
        this.repoRepository = repoRepository;
    }

    @Override
    public GetPerformanceRs getPerformance(GetPerformanceRq rq) {
        Optional<Repository> optionalRepository = repoRepository.getRepositoriesByUsernameAndOwnerAndName(rq.username(),rq.owner(),rq.repo());
        if(optionalRepository.isEmpty()){
            throw new IllegalArgumentException("repository not found");
        }
        List<Performance> performances = performanceRepository.getPerformance(optionalRepository.get());

        return new GetPerformanceRs(performances);
    }
}
