package com.sendback.global.dummy;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.project.repository.ProjectImageRepository;
import com.sendback.domain.project.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("projectImageDummy")
@DependsOn("projectDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProjectImageDummy {

    private final ProjectImageRepository projectImageRepository;
    private final ProjectRepository projectRepository;

    @PostConstruct
    public void init() {
        if (projectImageRepository.count() > 0) {
            log.info("[projectImageDummy] 프로젝트 이미지 데이터가 이미 존재");
        } else {
            createProjectImages();
            log.info("[projectImageDummy] 프로젝트 이미지 더미 생성 완료");
        }
    }

    private void createProjectImages() {

        List<Project> projects = projectRepository.findAll();

        List<String> imageUrls = List.of("https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/sad_bbdf9fb8-a6c4-4215-8901-bff7dc773824.png",
                "https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/read_90478a3a-11cf-439d-809a-efbe8226e078.png",
                "https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/movie_3caafd63-4ac6-4f73-9e57-fbead2f24d52.png",
                "https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/meal_a68f61d3-8aef-49fd-8cc6-53390f26d793.png");

        for (Project project : projects) {
            ProjectImage projectImage = ProjectImage.of(
                    project,
                    imageUrls.get((int) (Math.random() * 100) % imageUrls.size())
            );

            projectImageRepository.save(projectImage);
        }
    }
}
