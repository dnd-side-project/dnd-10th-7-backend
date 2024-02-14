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
    private String socialname;

    private String nickname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String birthDay;
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private Career career;

    private Boolean isDeleted = Boolean.FALSE;

    @Builder
    public User(SocialType socialType, String socialId, String email, Level level, String socialname, Gender gender, String birthday, String profileImageUrl, Career career, String nickname) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.email = email;
        this.level = level;
        this.socialname = socialname;
        this.gender = gender;
        this.birthDay = birthday;
        this.profileImageUrl = profileImageUrl;
        this.career = career;
        this.nickname = nickname;
    }

    public static User of(SocialType socialType, String socialId, String email, String socialName, String profileImageUrl){
        return User.builder()
                .socialType(socialType)
                .socialId(socialId)
                .email(email)
                .socialname(socialName)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public static User of(SigningAccount signingAccount, SignUpRequestDto signUpRequestDto) {
        return User.builder()
                .socialType(SocialType.toEnum(signingAccount.socialType()))
                .socialId(signingAccount.socialId())
                .email(signingAccount.email())
                .level(Level.ONE)
                .socialname(signingAccount.socialname())
                .gender(Gender.toEnum(signUpRequestDto.gender()))
                .birthday(signUpRequestDto.birthday())
                .profileImageUrl(signingAccount.profileImageUrl())
                .career(Career.toEnum(signUpRequestDto.career()))
                .nickname(signUpRequestDto.nickname())
                .build();
    }
    public void levelUp(Level level) {
        this.level = level;
    }
}
