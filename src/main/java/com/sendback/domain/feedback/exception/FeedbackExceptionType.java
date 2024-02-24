package com.sendback.domain.feedback.exception;

import com.sendback.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackExceptionType implements ExceptionType {

    NOT_FOUND_FEEDBACK(3000, "요청한 피드백을 찾을 수 없습니다."),
    DUPLICATE_FEEDBACK_SUBMIT(3010, "중복된 유저가 피드백을 제출했습니다."),
    REJECT_SUBMIT_FEEDBACK_BY_AUTHOR(3020, "작성자 본인은 본인 피드백에 제출할 수 없습니다.");
    private final int statusCode;
    private final String message;
}
