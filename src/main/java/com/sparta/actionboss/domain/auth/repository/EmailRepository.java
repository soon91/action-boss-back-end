package com.sparta.actionboss.domain.auth.repository;

import com.sparta.actionboss.domain.auth.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByEmail(String email);
}
