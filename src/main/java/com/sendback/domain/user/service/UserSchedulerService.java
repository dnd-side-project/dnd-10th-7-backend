package com.sendback.domain.user.service;

import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSchedulerService {

    private final UserRepository userRepository;

    @Scheduled(cron = "30 0 0 * * 0")
    public void updatePullUpCnt() {

        List<User> users = userRepository.findAllByPullUpCntIsGreaterThan(0L);

        users.forEach(
                User::resetPullUpCnt
        );

    }
}
