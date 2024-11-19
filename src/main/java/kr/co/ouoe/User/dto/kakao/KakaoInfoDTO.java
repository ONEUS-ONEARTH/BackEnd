package kr.co.ouoe.User.dto.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoInfoDTO {
    private String name;
    private String email;
    private String thumbNailUrl;
    private String phoneNumber;
    private String id;
}
