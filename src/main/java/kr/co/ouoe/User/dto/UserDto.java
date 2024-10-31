package kr.co.ouoe.User.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDto implements Serializable {
    private final String nickname;
    private final String email;
    private final String password;
    private final String profileImg;
    private final String loginMethod;
    private final int marketingAgree;
    private final int subscribe;
    private final LocalDateTime lastAdTime;
    private final String provider;

    public UserDto(String nickname, String email, String password, String profileImg, String loginMethod) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.loginMethod = loginMethod;
        this.marketingAgree = 0;
        this.subscribe = 0;
        this.lastAdTime = LocalDateTime.now();
        this.provider = loginMethod;
    }

    public static UserDto of(String nickname, String email, String password, String profileImg,String loginMethod) {
        return new UserDto(nickname,email,password,profileImg,loginMethod);
    }

    /*public static UserDto from(User entity) {
        return new UserDto(
                entity.getNickname(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getLoginMethod(),
                entity.getImagePath(),
        );
    }*/

}