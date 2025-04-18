package org.rapid.performanceservice.repository;

import org.rapid.performanceservice.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface RepoRepository extends JpaRepository<Repository,Long> {
    Optional<Repository> getRepositoriesByUsernameAndOwnerAndName(String username,String owner,String name);

    @Modifying
    @Query("DELETE FROM Repository r WHERE r.username=:username AND r.owner=:owner AND r.name=:name")
    int deleteRepository(String username,String owner,String name);
}
