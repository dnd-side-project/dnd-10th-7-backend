package com.sendback.domain.scrap.service;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.scrap.dto.response.ClickScrapResponse;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.sendback.domain.project.fixture.ProjectFixture.createDummyProject;
import static com.sendback.domain.scrap.fixture.ScrapFixture.createDummyScrap;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

public class ScrapServiceTest extends ServiceTest {

    @InjectMocks
    ScrapService scrapService;

    @Mock
    ScrapRepository scrapRepository;

    @Mock
    UserService userService;

    @Mock
    ProjectService projectService;

    private User user;
    private Project project;
    private Scrap scrap;

    @BeforeEach
    public void setUp() {
        this.user = spy(createDummyUser());
        this.project = spy(createDummyProject(user));
        this.scrap = spy(createDummyScrap(user, project));
    }

    @Nested
    @DisplayName("스크랩 클릭을 했을 때")
    class react {

        @Test
        @DisplayName("기존의 스크랩을 하지 않았으면 새롭게 저장한다.")
        public void success() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(scrapRepository.findByUserAndProject(any(User.class), any(Project.class))).willReturn(Optional.empty());
            given(scrapRepository.save(any(Scrap.class))).willReturn(scrap);

            //when
            ClickScrapResponse clickScrapResponse = scrapService.clickScrap(1L, 1L);

            //then
            assertThat(clickScrapResponse.isClicked()).isTrue();
        }

        @Test
        @DisplayName("스크랩을 취소하는 요청이면 soft delete 한다.")
        public void softDelete() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(scrapRepository.findByUserAndProject(any(User.class), any(Project.class))).willReturn(Optional.of(scrap));

            //when
            ClickScrapResponse clickScrapResponse = scrapService.clickScrap(1L, 1L);

            //then
            assertThat(clickScrapResponse.isClicked()).isFalse();
        }

        @Test
        @DisplayName("취소했던 스크랩를 다시 클릭하는 요청이면 삭제 처리를 취소한다.")
        public void visible() throws Exception {
            //given
            scrap.click();   // 취소된 like

            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(scrapRepository.findByUserAndProject(any(User.class), any(Project.class))).willReturn(Optional.of(scrap));

            //when
            ClickScrapResponse clickScrapResponse = scrapService.clickScrap(1L, 1L);

            //then
            assertThat(clickScrapResponse.isClicked()).isTrue();
        }
    }
}
