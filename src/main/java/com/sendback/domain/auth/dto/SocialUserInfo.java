package com.sendback.domain.auth.dto;

public record SocialUserInfo(
        String id, String nickname, String email, String profileImageUrl
) {

}