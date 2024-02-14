package com.sendback.domain.user.repository;

import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByNickname(String nickname);
}
