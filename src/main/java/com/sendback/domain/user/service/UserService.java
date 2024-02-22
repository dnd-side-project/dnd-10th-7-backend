package com.sendback.domain.user.service;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.feedback.repository.FeedbackSubmitRepository;
import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.field.service.FieldService;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.dto.SigningAccount;
import com.sendback.domain.user.dto.request.UpdateUserInfoRequestDto;
import com.sendback.domain.user.dto.response.*;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.entity.Level;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.common.CustomPage;
import com.sendback.global.common.constants.FieldName;
import com.sendback.global.config.jwt.JwtProvider;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sendback.domain.user.exception.UserExceptionType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FeedbackSubmitRepository feedbackSubmitRepository;
    private final FieldService fieldService;
    private final FieldRepository fieldRepository;
    private final ProjectRepository projectRepository;
    private final JwtProvider jwtProvider;
    private final LikeRepository likeRepository;

    @Transactional
    public Token signUpUser(@RequestBody SignUpRequestDto signUpRequestDto) {
        if(userRepository.findByNickname(signUpRequestDto.nickname()).isPresent()){
            throw new BadRequestException(DUPLICATED_NICKNAME);
        }
        jwtProvider.validateSignToken(signUpRequestDto.signToken());
        SigningAccount signingAccount = jwtProvider.getSignUserInfo(signUpRequestDto.signToken());
        if(userRepository.findBySocialId(signingAccount.socialId()).isPresent()){
            throw new BadRequestException(PREVIOUS_REGISTERED_USER);
        }
        User user = User.of(signingAccount, signUpRequestDto);
        User savedUser = userRepository.save(user);
        List<Field> fieldList = signUpRequestDto.fields().stream()
                .map(field -> Field.of(FieldName.toEnum(field), user))
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

    public UserInfoResponseDto getUserInfo(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER)
        );
        Long projectCount = projectRepository.countByUserId(userId);
        Long feedbackCount = feedbackSubmitRepository.countByUserId(userId);
        List<Project> projectList = projectRepository.findByUserId(userId);
        Long likeCount = likeRepository.countByProjectIn(projectList);
        List<Field> fieldList = fieldRepository.findAllByUserId(userId);
        List<String> fieldNameList = fieldList.stream()
                .map(Field::getName)
                .map(FieldName::getName)
                .toList();
        Long needToFeedbackCount = Level.getRemainCountUntilNextLevel(feedbackCount);
        UserInfoResponseDto responseDto = new UserInfoResponseDto(user.getNickname(),
                user.getCareer().getValue(), user.getProfileImageUrl(), user.getBirthDay(),
                user.getEmail(), fieldNameList, Level.toNumber(user.getLevel()), feedbackCount, needToFeedbackCount,
                projectCount, likeCount);
        return responseDto;
    }

    @Transactional
    public UpdateUserInfoResponseDto updateUserInfo(Long userId, UpdateUserInfoRequestDto updateUserInfoRequestDto){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER)
        );
        if(userRepository.findByNickname(updateUserInfoRequestDto.nickname()).isPresent()){
            throw new BadRequestException(DUPLICATED_NICKNAME);
        }
        user.update(updateUserInfoRequestDto);
        fieldRepository.deleteByUserId(userId);
        List<Field> fieldList = updateUserInfoRequestDto.fields().stream()
                .map(intersts -> Field.of(FieldName.toEnum(intersts), user))
                .collect(Collectors.toList());
        fieldRepository.saveAll(fieldList);
        return new UpdateUserInfoResponseDto(updateUserInfoRequestDto.nickname(), updateUserInfoRequestDto.birthday(),
                updateUserInfoRequestDto.career(), updateUserInfoRequestDto.fields());
    }

    public CustomPage<RegisteredProjectResponseDto> getRegisteredProjects(Long userId, int page, int size, int sort){

        Pageable pageable = PageRequest.of(page-1, size);
        boolean isFinished = sort == 0 ? true : false;

        Page<RegisteredProjectResponseDto> responseDtos = projectRepository.findAllRegisteredProjectsByMe(pageable, userId, isFinished);

        return CustomPage.of(responseDtos);
    }

    public CustomPage<ScrappedProjectResponseDto> getScrappedProjects(Long userId, int page, int size, int sort){

        Pageable pageable = PageRequest.of(page-1, size);
        boolean isFinished = sort == 0 ? true : false;

        Page<ScrappedProjectResponseDto> responseDtos = projectRepository.findAllScrappedProjectsByMe(pageable, userId, isFinished);

        return CustomPage.of(responseDtos);
    }

    public CustomPage<SubmittedFeedbackResponseDto> getSubmittedFeedback(Long userId, int page, int size, int sort){
        Pageable pageable = PageRequest.of(page-1, size);
        boolean isFinished = sort == 0 ? true : false;

        Page<SubmittedFeedbackResponseDto> responseDtos = projectRepository.findAllSubmittedProjectsByMe(pageable, userId, isFinished);

        return CustomPage.of(responseDtos);
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new NotFoundException(NOT_FOUND_USER)
        );
    }
}
