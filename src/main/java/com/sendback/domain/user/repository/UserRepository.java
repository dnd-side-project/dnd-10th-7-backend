package com.sendback.domain.user.repository;

import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickname(String nickname);
    Optional<User> findBySocialId(String socialId);
}
