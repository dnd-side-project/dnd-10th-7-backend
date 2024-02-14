package com.sendback.domain.user.service;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.feedback.entity.QFeedbackSubmit;
import com.sendback.domain.feedback.repository.FeedbackRepository;
import com.sendback.domain.feedback.repository.FeedbackSubmitRepository;
import com.sendback.domain.feedback.service.FeedbackService;
import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.field.service.FieldService;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.like.service.LikeService;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.user.dto.SigningAccount;
import com.sendback.domain.user.dto.response.CheckUserNicknameResponseDto;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.dto.response.UserInfoResponseDto;
import com.sendback.domain.user.entity.Career;
import com.sendback.domain.user.entity.Level;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.config.jwt.JwtProvider;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import com.sendback.global.exception.type.SignInException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sendback.domain.user.exception.UserExceptionType.INVALID_NICKNAME;
import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FieldService fieldService;
    private final JwtProvider jwtProvider;

    @Transactional
    public Token signUpUser(@RequestBody SignUpRequestDto signUpRequestDto) {
        jwtProvider.validateSignToken(signUpRequestDto.signToken());
        SigningAccount signingAccount = jwtProvider.getSignUserInfo(signUpRequestDto.signToken());
        User user = User.of(signingAccount, signUpRequestDto);
        User savedUser = userRepository.save(user);
        List<Field> fieldList = signUpRequestDto.interests().stream()
                .map(intersts -> Field.of(intersts, user))
                .collect(Collectors.toList());
        fieldService.saveAll(fieldList);
        return jwtProvider.issueToken(savedUser.getId());
    }

    public CheckUserNicknameResponseDto checkUserNickname(String nickname) {
        if(!nickname.matches("^[가-힣a-zA-Z]{2,8}$")){
            throw new BadRequestException(INVALID_NICKNAME);
        }
        Optional<User> user =  userRepository.findByNickname(nickname);
        return new CheckUserNicknameResponseDto(user.isPresent());
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new NotFoundException(NOT_FOUND_USER)
        );
    }
}
