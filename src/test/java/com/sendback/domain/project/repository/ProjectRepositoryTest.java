package com.sendback.domain.project.repository;

import com.sendback.domain.feedback.persister.FeedbackTestPersister;
import com.sendback.domain.like.persister.LikeTestPersister;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.persister.ProjectTestPersister;
import com.sendback.global.RepositoryTest;

import com.sendback.global.common.constants.FieldName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectRepositoryTest extends RepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    @DisplayName("특정 userId를 외래키로 가지는 프로젝트의 개수를 반환한다.")
    public void countByUserId() {
        //given
        Project project = projectTestPersister.builder().save();

        //when
        Long projectCount = projectRepository.countByUserId(project.getUser().getId());

        //then
        assertThat(projectCount).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 uerId를 가지는 프로젝트들을 반환한다.")
    public void findByUserId() {
        //given
        Project project = projectTestPersister.builder().save();

        //when
        List<Project> findProject = projectRepository.findByUserId(project.getUser().getId());

        //then
        assertThat(findProject.size()).isEqualTo(1);
    }

    @Nested
    @DisplayName("프로젝트 검색 및 조회")
    class findAllByPageableAndFieldAndIsFinishedAndSort {

        @Test
        @DisplayName("예술/대중문화 - 최신순 - 끝난 1page 조회")
        public void success_artField() throws Exception {
            //given
            ProjectTestPersister.ProjectBuilder projectBuilder = projectTestPersister.builder();
            projectBuilder.save();
            projectBuilder.save();
            projectBuilder.save();  //dummy
            Project project_seventh = projectBuilder.save(FieldName.ART);
            Project project_sixth = projectBuilder.save(FieldName.ART);
            Project project_fifth = projectBuilder.save(FieldName.ART);
            Project project_fourth = projectBuilder.save(FieldName.ART);
            Project project_third = projectBuilder.save(FieldName.ART);
            Project project_second = projectBuilder.save(FieldName.ART);
            Project project_first = projectBuilder.save(FieldName.ART);
            Pageable pageable = PageRequest.of(0, 5);

            //when
            Page<Project> response = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    pageable, null, FieldName.ART.getName(), true, 0L);

            //then
            assertThat(response.getTotalElements()).isEqualTo(7);
            assertThat(response.getTotalPages()).isEqualTo(2);
            assertThat(response.getContent().get(0)).usingRecursiveComparison().isEqualTo(project_first);
            assertThat(response.getContent().get(1)).usingRecursiveComparison().isEqualTo(project_second);
            assertThat(response.getContent().get(2)).usingRecursiveComparison().isEqualTo(project_third);
            assertThat(response.getContent().get(3)).usingRecursiveComparison().isEqualTo(project_fourth);
            assertThat(response.getContent().get(4)).usingRecursiveComparison().isEqualTo(project_fifth);
        }

        @Test
        @DisplayName("분야 전체 - 최신순 - 끝나지 않은 1page 조회")
        public void success_allField_notFinished() throws Exception {
            //given
            ProjectTestPersister.ProjectBuilder projectBuilder = projectTestPersister.builder();
            FeedbackTestPersister.FeedbackBuilder feedbackBuilder = feedbackTestPersister.builder();
            Project project_ninth = projectBuilder.save();
            feedbackBuilder.project(project_ninth).save();

            Project project_eighth = projectBuilder.save();
            feedbackBuilder.project(project_eighth).save();

            Project project_seventh = projectBuilder.save();
            feedbackBuilder.project(project_seventh).save();

            Project project_sixth = projectBuilder.save(FieldName.ART);
            feedbackBuilder.project(project_sixth).save();

            Project project_fifth = projectBuilder.save(FieldName.ART);
            feedbackBuilder.project(project_fifth).save();

            Project project_fourth = projectBuilder.save(FieldName.ART);
            feedbackBuilder.project(project_fourth).save();

            Project project_third = projectBuilder.save(FieldName.ART);
            feedbackBuilder.project(project_third).save();

            Project project_second = projectBuilder.save(FieldName.ART);
            feedbackBuilder.project(project_second).save();

            Project project_first = projectBuilder.save(FieldName.ART);
            feedbackBuilder.project(project_first).save();

            Pageable pageable = PageRequest.of(0, 5);
            Pageable secondPageable = PageRequest.of(1, 5);

            //when
            Page<Project> response = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    pageable, null, null, false, 0L);
            Page<Project> secondResponse = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    secondPageable, null, null, false, 0L);

            //then
            assertThat(response.getTotalElements()).isEqualTo(9);
            assertThat(response.getTotalPages()).isEqualTo(2);
            assertThat(response.getContent().get(0)).usingRecursiveComparison().isEqualTo(project_first);
            assertThat(response.getContent().get(1)).usingRecursiveComparison().isEqualTo(project_second);
            assertThat(response.getContent().get(2)).usingRecursiveComparison().isEqualTo(project_third);
            assertThat(response.getContent().get(3)).usingRecursiveComparison().isEqualTo(project_fourth);
            assertThat(response.getContent().get(4)).usingRecursiveComparison().isEqualTo(project_fifth);
            assertThat(secondResponse.getContent().get(0)).usingRecursiveComparison().isEqualTo(project_sixth);
            assertThat(secondResponse.getContent().get(1)).usingRecursiveComparison().isEqualTo(project_seventh);
            assertThat(secondResponse.getContent().get(2)).usingRecursiveComparison().isEqualTo(project_eighth);
            assertThat(secondResponse.getContent().get(3)).usingRecursiveComparison().isEqualTo(project_ninth);
        }

        @Test
        @DisplayName("분야 전체 - 최신순 - 끝난 1page 조회")
        public void success_allField_finished() throws Exception {
            //given
            ProjectTestPersister.ProjectBuilder projectBuilder = projectTestPersister.builder();
            Project project_ninth = projectBuilder.save();
            Project project_eighth = projectBuilder.save();
            Project project_seventh = projectBuilder.save();
            Project project_sixth = projectBuilder.save(FieldName.ART);
            Project project_fifth = projectBuilder.save(FieldName.ART);
            Project project_fourth = projectBuilder.save(FieldName.ART);
            Project project_third = projectBuilder.save(FieldName.ART);
            Project project_second = projectBuilder.save(FieldName.ART);
            Project project_first = projectBuilder.save(FieldName.ART);
            Pageable pageable = PageRequest.of(0, 5);
            Pageable secondPageable = PageRequest.of(1, 5);

            //when
            Page<Project> response = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    pageable, null, null, true, 0L);
            Page<Project> secondResponse = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    secondPageable, null, null, true, 0L);

            //then
            assertThat(response.getTotalElements()).isEqualTo(9);
            assertThat(response.getTotalPages()).isEqualTo(2);
            assertThat(response.getContent().get(0)).usingRecursiveComparison().isEqualTo(project_first);
            assertThat(response.getContent().get(1)).usingRecursiveComparison().isEqualTo(project_second);
            assertThat(response.getContent().get(2)).usingRecursiveComparison().isEqualTo(project_third);
            assertThat(response.getContent().get(3)).usingRecursiveComparison().isEqualTo(project_fourth);
            assertThat(response.getContent().get(4)).usingRecursiveComparison().isEqualTo(project_fifth);
            assertThat(secondResponse.getContent().get(0)).usingRecursiveComparison().isEqualTo(project_sixth);
        }

        @Test
        @DisplayName("분야 전체 - 인기순 - 끝난 1page 조회")
        public void success_allField_orderByLikeCnt_finished() throws Exception {
            //given
            ProjectTestPersister.ProjectBuilder projectBuilder = projectTestPersister.builder();
            Project project_ninth = projectBuilder.save();
            Project project_eighth = projectBuilder.save();
            Project project_seventh = projectBuilder.save();
            Project project_sixth = projectBuilder.save(FieldName.ART);
            Project project_fifth = projectBuilder.save(FieldName.ART);
            Project project_fourth = projectBuilder.save(FieldName.ART);
            Project project_third = projectBuilder.save(FieldName.ART);
            Project project_second = projectBuilder.save(FieldName.ART);
            Project project_first = projectBuilder.save(FieldName.ART);
            LikeTestPersister.LikeBuilder likeBuilder = likeTestPersister.builder();
            for (int cnt = 0 ; cnt < 5 ; cnt ++) {
                likeBuilder.project(project_fifth).save();
            }
            for (int cnt = 0 ; cnt < 4 ; cnt ++) {
                likeBuilder.project(project_fourth).save();
            }
            for (int cnt = 0 ; cnt < 3 ; cnt ++) {
                likeBuilder.project(project_third).save();
            }
            for (int cnt = 0 ; cnt < 2 ; cnt ++) {
                likeBuilder.project(project_second).save();
            }
            likeBuilder.project(project_first).save();

            Pageable pageable = PageRequest.of(0, 5);
            Pageable secondPageable = PageRequest.of(1, 5);

            //when
            Page<Project> response = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    pageable, null, null, true, 1L);
            Page<Project> secondResponse = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    secondPageable, null, null, true, 1L);

            //then
            assertThat(response.getTotalElements()).isEqualTo(9);
            assertThat(response.getTotalPages()).isEqualTo(2);
            assertThat(response.getContent().get(0)).usingRecursiveComparison().isEqualTo(project_fifth);
            assertThat(response.getContent().get(1)).usingRecursiveComparison().isEqualTo(project_fourth);
            assertThat(response.getContent().get(2)).usingRecursiveComparison().isEqualTo(project_third);
            assertThat(response.getContent().get(3)).usingRecursiveComparison().isEqualTo(project_second);
            assertThat(response.getContent().get(4)).usingRecursiveComparison().isEqualTo(project_first);
        }

        @Test
        @DisplayName("분야 전체 - 최신순 - 안 끝난 - 키워드 검색 1page 조회")
        public void success_allField_orderByLikeCnt_keyword_finished() throws Exception {
            //given
            ProjectTestPersister.ProjectBuilder projectBuilder = projectTestPersister.builder();
            FeedbackTestPersister.FeedbackBuilder feedbackBuilder = feedbackTestPersister.builder();

            Project project_ninth = projectBuilder.save();
            feedbackBuilder.project(project_ninth).save();

            Project project_eighth = projectBuilder.save();
            feedbackBuilder.project(project_eighth).save();

            Project project_seventh = projectBuilder.save();
            feedbackBuilder.project(project_seventh).save();

            Project project_sixth = projectBuilder.save("재있미다", FieldName.ART);
            feedbackBuilder.project(project_sixth).save();

            Project project_fifth = projectBuilder.save("너무 재미있다",FieldName.ART);
            feedbackBuilder.project(project_fifth).save();

            Project project_fourth = projectBuilder.save("z재미z", FieldName.ART);
            feedbackBuilder.project(project_fourth).save();

            Project project_third = projectBuilder.save("난 조회 안됐으면 해", FieldName.ART);
            feedbackBuilder.project(project_third).save();

            Project project_second = projectBuilder.save("z재미",FieldName.ART);
            feedbackBuilder.project(project_second).save();

            Project project_first = projectBuilder.save("재미z",FieldName.ART);
            feedbackBuilder.project(project_first).save();


            Pageable pageable = PageRequest.of(0, 5);

            //when
            Page<Project> response = projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(
                    pageable, "재미", null, null, 0L);

            //then
            assertThat(response.getTotalElements()).isEqualTo(4);
            assertThat(response.getTotalPages()).isEqualTo(1);
            assertThat(response.getContent().get(0)).usingRecursiveComparison().isEqualTo(project_first);
            assertThat(response.getContent().get(1)).usingRecursiveComparison().isEqualTo(project_second);
            assertThat(response.getContent().get(2)).usingRecursiveComparison().isEqualTo(project_fourth);
            assertThat(response.getContent().get(3)).usingRecursiveComparison().isEqualTo(project_fifth);
        }
    }

}
