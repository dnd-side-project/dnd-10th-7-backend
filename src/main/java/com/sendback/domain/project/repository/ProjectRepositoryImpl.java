package com.sendback.domain.project.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sendback.domain.project.dto.response.QRecommendedProjectResponseDto;
import com.sendback.domain.project.dto.response.RecommendedProjectResponseDto;
import com.sendback.domain.user.dto.response.QRegisteredProjectResponseDto;
import com.sendback.domain.user.dto.response.RegisteredProjectResponseDto;
import com.sendback.global.common.constants.FieldName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static com.sendback.domain.like.entity.QLike.like;
import static com.sendback.domain.project.entity.QProject.project;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RegisteredProjectResponseDto> findAllProjectsByMe(Pageable pageable, Long userId, Boolean check) {

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
    public List<RecommendedProjectResponseDto> findRecommendedProjects(Long userId, List<FieldName> filedNameList) {
        List<RecommendedProjectResponseDto> content = queryFactory
                .select(new QRecommendedProjectResponseDto(
                        project.id.as("projectId"),
                        project.progress.stringValue(),
                        project.fieldName.stringValue(),
                        project.title,
                        project.summary,
                        project.user.nickname,
                        project.createdAt,
                        project.user.profileImageUrl
                ))
                .from(project)
                .join(project.likes, like)
                .groupBy(project)
                .where(
                        like.isDeleted.eq(false),
                        project.isDeleted.eq(false),
                        userIdIs(userId, filedNameList)
                )
                .orderBy(like.count().desc())
                .limit(10)
                .fetch();
        return content;
    }

    private BooleanExpression userIdIs(Long userId, List<FieldName> fileNameList) {
        return userId == null ?  null : project.fieldName.in(fileNameList);
    }
}
