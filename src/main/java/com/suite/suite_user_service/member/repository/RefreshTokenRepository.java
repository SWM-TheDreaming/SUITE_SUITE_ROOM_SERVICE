package com.suite.suite_user_service.member.repository;

import com.suite.suite_user_service.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKeyId(String keyId);
}
