package com.sendback.domain.user.dto.response;

import java.util.List;

public record UserInfoResponseDto(String nickname, String career, String profileImageUrl, String birthday, String email,
    List<String> field, int level, Long feedbackCount, Long needToFeedbackCount, Long projectCount, Long likeCount)
{

}
