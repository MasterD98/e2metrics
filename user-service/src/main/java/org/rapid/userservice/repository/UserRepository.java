package org.rapid.userservice.repository;

import org.rapid.userservice.entity.User;
import org.rapid.userservice.repository.view.AccessTokenView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByUsername(String username);
    @Modifying
    @Query("DELETE FROM User u WHERE u.username=:username")
    int deleteByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username=:username")
    Optional<AccessTokenView> getGithubToken(String username);
}
