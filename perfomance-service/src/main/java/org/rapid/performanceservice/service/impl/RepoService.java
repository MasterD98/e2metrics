package org.rapid.performanceservice.service.impl;

import org.rapid.performanceservice.entity.Repository;
import org.rapid.performanceservice.repository.RepoRepository;
import org.rapid.performanceservice.service.RepoServiceIf;
import org.rapid.performanceservice.service.rqrs.AddRepositoryRq;
import org.rapid.performanceservice.service.rqrs.AddRepositoryRs;
import org.rapid.performanceservice.service.rqrs.DeleteRepositoryRq;
import org.rapid.performanceservice.service.rqrs.DeleteRepositoryRs;
import org.springframework.stereotype.Service;

@Service
public class RepoService implements RepoServiceIf {
    private final RepoRepository repoRepository;

    public RepoService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }


    @Override
    public AddRepositoryRs addRepository(AddRepositoryRq rq) {
        Repository repository = repoRepository.save(Repository.builder()
                        .owner(rq.owner())
                        .username(rq.username())
                        .name(rq.repo())
                .build());
        return new AddRepositoryRs(repository);
    }

    @Override
    public DeleteRepositoryRs deleteRepository(DeleteRepositoryRq rq) {
        int rowCount = repoRepository.deleteRepository(rq.username(),rq.owner(),rq.repo());
        return new DeleteRepositoryRs(rowCount>0);
    }
}
