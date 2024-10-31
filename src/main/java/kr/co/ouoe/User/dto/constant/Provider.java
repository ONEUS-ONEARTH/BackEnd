package kr.co.ouoe.User.dto.constant;

import lombok.Getter;

public enum Provider {
    EMAIL("email"),
    KAKAO("naver"),
    GOOGLE("google");

    @Getter
    private final String name;

    Provider(String name) {
        this.name = name;
    }
}
