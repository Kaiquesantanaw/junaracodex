package com.portfoliosass.repository;

import com.portfoliosass.model.Profile;
import com.portfoliosass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);

    Optional<Profile> findByUserUsername(String username);
}
