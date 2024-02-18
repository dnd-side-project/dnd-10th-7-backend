package com.sendback.domain.project.fixture;

import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.GetProjectsResponseDto;
import com.sendback.domain.project.dto.response.ProjectDetailResponseDto;
import com.sendback.domain.project.entity.Progress;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.common.constants.FieldName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sendback.domain.user.fixture.UserFixture.mock_user;

public class ProjectFixture {

    private static final String TITLE = "title";
    private static final String UPDATE_TITLE = "update_title";
    private static final FieldName FIELD_NAME = FieldName.GAME;
    private static final FieldName UPDATE_FIELD_NAME = FieldName.ART;
    private static final String CONTENT = "content";
    private static final String UPDATE_CONTENT = "update content";
    private static final String SUMMARY = "summary";
    private static final String UPDATE_SUMMARY = "update summary";
    private static final String DEMO_SITE_URL = "demoUrl";
    private static final String UPDATE_DEMO_SITE_URL = "update demoUrl";
    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate UPDATE_START_DATE = LocalDate.of(2024, 2, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 1, 3);
    private static final LocalDate UPDATE_END_DATE = LocalDate.of(2024, 2, 3);
    private static final Progress PLANNING_PROGRESS = Progress.PLANNING;
    private static final Progress DEVELOPING_PROGRESS = Progress.DEVELOPING;

    public static final SaveProjectRequestDto MOCK_SAVE_PROJECT_REQUEST_DTO = new SaveProjectRequestDto(TITLE, FIELD_NAME.getName(), CONTENT, SUMMARY, DEMO_SITE_URL, START_DATE,
            END_DATE, PLANNING_PROGRESS.getValue(), 1L,2L,3L,4L);

    public static final UpdateProjectRequestDto MOCK_UPDATE_PROJECT_REQUEST_DTO = new UpdateProjectRequestDto(
            UPDATE_TITLE, UPDATE_FIELD_NAME.getName(), UPDATE_CONTENT, UPDATE_SUMMARY, UPDATE_DEMO_SITE_URL, UPDATE_START_DATE, UPDATE_END_DATE, DEVELOPING_PROGRESS.getValue(),
            4L, 3L, 2L, 1L, List.of("deleteUrl")
    );

    public static final ProjectDetailResponseDto MOCK_PROJECT_DETAIL_RESPONSE_DTO = new ProjectDetailResponseDto(
            1L, mock_user.getNickname(), mock_user.getLevel().getName(), mock_user.getProfileImageUrl(), 1L, TITLE, FIELD_NAME.getName(),
            CONTENT, DEMO_SITE_URL, PLANNING_PROGRESS.getValue(), List.of("이미지 1", "이미지 2"), 1L, 2L, 3L, 4L, 5L, 6L, 7L,
            LocalDateTime.now(), LocalDate.of(2024, 1, 12), LocalDate.of(2024, 1, 15),
            false, false, false);

    public static final GetProjectsResponseDto MOCK_GET_PROJECTS_RESPONSE_DTO = new GetProjectsResponseDto(
            "닉네임", "프로필 이미지", 1L, "제목", "한 줄 요약", Progress.REFACTORING.getValue(),
            FieldName.ART.getName(), LocalDateTime.now(), 1L, 2L, 3L, true);

    public static Project createDummyProject(User user) {
        return Project.of(user, MOCK_SAVE_PROJECT_REQUEST_DTO);
    }

}
