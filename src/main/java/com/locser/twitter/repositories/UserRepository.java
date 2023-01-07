package com.locser.twitter.repositories;

import com.locser.twitter.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    @Query("select a from ApplicationUser a where a.username like concat('%', ?1, '%')")
    List<ApplicationUser> findByUsernameContains(@NonNull String username);

    Optional<ApplicationUser> findByUsername(String userName);
}