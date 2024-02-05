package com.sendback.domain.users.repository;

import com.sendback.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByNickname(String nickname);
    Optional<Users> findBySocialId(String socialId);
}
