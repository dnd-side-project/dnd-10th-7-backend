package com.sendback.domain.users.repository;

import com.sendback.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    User findByNickname(String nickname);
    Optional<User> findBySocialId(String socialId);
}
