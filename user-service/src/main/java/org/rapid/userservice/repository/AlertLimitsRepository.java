package org.rapid.userservice.repository;

import org.rapid.userservice.entity.AlertLimits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertLimitsRepository extends JpaRepository<AlertLimits,Long> {
    Optional<AlertLimits> getAlertLimitsByUserUsername(String username);
}
