package org.rapid.performanceservice.controller;

import jakarta.validation.Valid;
import org.rapid.performanceservice.controller.rqrs.AddRepositoryHttpRq;
import org.rapid.performanceservice.controller.rqrs.AddRepositoryHttpRs;
import org.rapid.performanceservice.controller.rqrs.DeleteRepositoryHttpRq;
import org.rapid.performanceservice.controller.rqrs.DeleteRepositoryHttpRs;
import org.rapid.performanceservice.service.RepoServiceIf;
import org.rapid.performanceservice.service.rqrs.AddRepositoryRq;
import org.rapid.performanceservice.service.rqrs.AddRepositoryRs;
import org.rapid.performanceservice.service.rqrs.DeleteRepositoryRq;
import org.rapid.performanceservice.service.rqrs.DeleteRepositoryRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("performance/repository")
public class RepoController {
    private final RepoServiceIf repoService;

    public RepoController(RepoServiceIf repoService) {
        this.repoService = repoService;
    }

    @PostMapping("")
    ResponseEntity<AddRepositoryHttpRs> addRepository(@Valid @RequestBody AddRepositoryHttpRq rq){
        AddRepositoryRs rs = repoService.addRepository(new AddRepositoryRq(rq.owner(), rq.repo(), rq.username()));
        return ResponseEntity.ok(new AddRepositoryHttpRs(rs.repository()));
    }

    @DeleteMapping("")
    ResponseEntity<DeleteRepositoryHttpRs> deleteRepository(@Valid DeleteRepositoryHttpRq rq){
        DeleteRepositoryRs rs = repoService.deleteRepository(new DeleteRepositoryRq(rq.owner(),rq.repo(),rq.username()));
        return ResponseEntity.ok(new DeleteRepositoryHttpRs(rs.success()));
    }
}
