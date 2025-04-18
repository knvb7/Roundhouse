package com.SportNexus.Athletiverse.repository;

import com.SportNexus.Athletiverse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmailId(String emailId);

    Optional<User> findByEmailIdAndPassword(String emailId, String password);

    Optional<User> findByPhoneNumberAndPassword(String phoneNumber, String password);

    Optional<User> findByEmailIdOrPhoneNumber(String emailId, String phoneNumber);
}
