package org.rapid.userservice.repository;

import org.rapid.userservice.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert,Long> {
    List<Alert> getAllByUserUsername(String username);
    @Modifying
    @Query("UPDATE Alert a SET a.isShowed=:isShowed WHERE a.id=:id")
    int modifyAlertStatus(long id,boolean isShowed);
}
