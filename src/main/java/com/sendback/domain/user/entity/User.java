package com.sendback.domain.user.entity;

import com.sendback.domain.user.dto.SigningAccount;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Users SET isDeleted = true WHERE id = ?")
@Table(name="users")
public class User extends BaseEntity {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String socialId;
    private String email;
    @Enumerated(EnumType.STRING)
    private Level level;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String birthDay;
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private Career career;

    private Boolean isDeleted = Boolean.FALSE;

    @Builder
    public User(SocialType socialType, String socialId, String email, Level level, String nickname, Gender gender, String birthday, String profileImageUrl, Career career) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.email = email;
        this.level = level;
        this.nickname = nickname;
        this.gender = gender;
        this.birthDay = birthday;
        this.profileImageUrl = profileImageUrl;
        this.career = career;
    }

    public static User of(SocialType socialType, String socialId, String email, String nickname, String profileImageUrl){
        return User.builder()
                .socialType(socialType)
                .socialId(socialId)
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public static User of(SigningAccount signingAccount, SignUpRequestDto signUpRequestDto){
        return User.builder()
                .socialType(SocialType.toEnum(signingAccount.socialType()))
                .socialId(signingAccount.socialId())
                .email(signingAccount.email())
                .level(Level.ONE)
                .nickname(signingAccount.nickname())
                .gender(Gender.toEnum(signUpRequestDto.gender()))
                .birthday(signUpRequestDto.birthday())
                .profileImageUrl(signingAccount.profileImageUrl())
                .career(Career.toEnum(signUpRequestDto.career()))
                .build();
    }
}
