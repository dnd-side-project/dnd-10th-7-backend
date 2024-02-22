package com.sendback.domain.project.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.dto.response.*;
import com.sendback.global.common.constants.FieldName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import java.util.List;
import static com.sendback.domain.feedback.entity.QFeedback.feedback;
import static com.sendback.domain.feedback.entity.QFeedbackSubmit.feedbackSubmit;
import static com.sendback.domain.like.entity.QLike.like;
import static com.sendback.domain.project.entity.QProject.project;
import static com.sendback.domain.scrap.entity.QScrap.scrap;
import static com.sendback.domain.user.entity.QUser.user;

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

    @Override
    public Page<SubmittedFeedbackResponseDto> findAllSubmittedProjectsByMe(Pageable pageable, Long userId, Boolean isFinished) {
        List<SubmittedFeedbackResponseDto> content = queryFactory
                .select(new QSubmittedFeedbackResponseDto(
                        feedback.project.progress.stringValue(),
                        feedback.project.fieldName.stringValue(),
                        feedback.title,
                        feedback.content.as("summary"),
                        feedback.id.as("feedbackId"),
                        feedback.createdAt
                ))
                .distinct()
                .from(feedbackSubmit)
                .join(feedbackSubmit.feedback, feedback)
                .where(feedbackSubmit.user.id.eq(userId),
                        feedbackSubmit.isDeleted.eq(false),
                        feedback.isDeleted.eq(false),
                        feedback.isFinished.eq(isFinished)
                )
                .orderBy(feedback.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory
                .select(feedbackSubmit.feedback)
                .from(feedbackSubmit)
                .distinct()
                .join(feedbackSubmit.feedback, feedback)
                .where(feedbackSubmit.user.id.eq(userId),
                        feedbackSubmit.isDeleted.eq(false),
                        feedback.isDeleted.eq(false),
                        feedback.isFinished.eq(isFinished)
                )
                .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Project> findRecommendedProjects(List<FieldName> filedNameList, int size) {
        List<Project> content = queryFactory
                .selectFrom(project)
                .join(project.likes, like)
                .groupBy(project.id)
                .where(
                        like.isDeleted.eq(false),
                        project.isDeleted.eq(false),
                        project.fieldName.in(filedNameList)
                )
                .orderBy(project.id.count().desc())
                .limit(size)
                .fetch();
        return content;
    }

    @Override
    public List<Project> findRecommendedProjects(int size) {
        List<Project> content = queryFactory
                .selectFrom(project)
                .join(project.likes, like)
                .groupBy(project.id)
                .where(
                        like.isDeleted.eq(false),
                        project.isDeleted.eq(false)
                )
                .orderBy(project.id.count().desc())
                .limit(size)
                .fetch();
        return content;
    }

    @Override
    public Page<Project> findAllByPageableAndFieldAndIsFinishedAndSort(Pageable pageable, String keyword, String field, Boolean isFinished, Long sort) {

        JPAQuery<Project> query = queryFactory.selectFrom(project)
                .join(project.user, user).fetchJoin()
                .where(containsKeyword(keyword),
                        specifyField(field),
                        specifyIsFinished(isFinished),
                        project.isDeleted.isFalse())
                .orderBy(specifySort(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                () -> queryFactory.selectFrom(project)
                        .join(project.user, user).fetchJoin()
                        .where(containsKeyword(keyword),
                                specifyField(field),
                                specifyIsFinished(isFinished),
                                project.isDeleted.isFalse())
                        .fetch().size());

    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) return null;

        return project.title.contains(keyword);
    }

    private BooleanExpression specifyField(String field) {
        if (field == null || field.equals("분야 없음")) return null;

        return project.fieldName.eq(FieldName.toEnum(field));
    }

    private BooleanExpression specifyIsFinished(Boolean isFinished) {
        if (isFinished == null || !isFinished) {
            return project.feedbackCount.gt(0);
        }
        return project.feedbackCount.eq(0);
    }

    private OrderSpecifier<?> specifySort(Long sort) {
        if (sort == null || sort == 0)
            return project.projectPull.pulledAt.desc();
        return project.likeCount.desc();
    }

}
