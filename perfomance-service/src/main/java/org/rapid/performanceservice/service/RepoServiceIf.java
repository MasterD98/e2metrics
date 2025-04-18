package org.rapid.performanceservice.service;

import org.rapid.performanceservice.service.rqrs.AddRepositoryRq;
import org.rapid.performanceservice.service.rqrs.AddRepositoryRs;
import org.rapid.performanceservice.service.rqrs.DeleteRepositoryRq;
import org.rapid.performanceservice.service.rqrs.DeleteRepositoryRs;

public interface RepoServiceIf {
    AddRepositoryRs addRepository(AddRepositoryRq rq);
    DeleteRepositoryRs deleteRepository(DeleteRepositoryRq rq);
}
