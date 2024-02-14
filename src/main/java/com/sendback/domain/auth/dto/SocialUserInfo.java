package com.sendback.domain.auth.dto;

public record SocialUserInfo(
        String id, String socialname, String email, String profileImageUrl
) {

}