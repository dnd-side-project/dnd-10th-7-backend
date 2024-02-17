package com.sendback.domain.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sendback.domain.project.entity.Progress;
import com.sendback.domain.user.dto.response.QRegisteredProjectResponseDto;
import com.sendback.domain.user.dto.response.QScrappedProjectResponseDto;
import com.sendback.domain.user.dto.response.RegisteredProjectResponseDto;
import com.sendback.domain.user.dto.response.ScrappedProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static com.sendback.domain.project.entity.QProject.project;
import static com.sendback.domain.scrap.entity.QScrap.scrap;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RegisteredProjectResponseDto> findAllRegisteredProjectsByMe(Pageable pageable, Long userId, Boolean check) {

        List<RegisteredProjectResponseDto> content = queryFactory
                .select(new QRegisteredProjectResponseDto(
                       project.id.as("projectId"),
                       project.title,
                       project.progress.stringValue(),
                       project.summary,
                       project.fieldName.stringValue(),
                       project.createdAt,
                       project.projectPull.pullUpCnt
                ))
                .from(project)
                .where(project.user.id.eq(userId),
                        project.isFinished.eq(check),
                        project.isDeleted.eq(false))
                .orderBy(project.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory
                .selectFrom(project)
                .where(project.user.id.eq(userId),
                        project.isFinished.eq(check),
                        project.isDeleted.eq(false))
                .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ScrappedProjectResponseDto> findAllScrappedProjectsByMe(Pageable pageable, Long userId, Boolean check) {
        List<ScrappedProjectResponseDto> content = queryFactory
                .select(new QScrappedProjectResponseDto(
                        project.id.as("projectId"),
                        project.title,
                        project.progress.stringValue(),
                        project.summary,
                        project.fieldName.stringValue(),
                        project.createdAt,
                        project.projectPull.pullUpCnt
                ))
                .from(scrap)
                .join(scrap.project, project)
                .where(scrap.user.id.eq(userId),
                        project.isFinished.eq(check),
                        project.isDeleted.eq(false))
                .orderBy(project.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory
                .selectFrom(scrap)
                .join(scrap.project, project)
                .where(scrap.user.id.eq(userId),
                        project.isFinished.eq(check),
                        project.isDeleted.eq(false))
                .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }
}
