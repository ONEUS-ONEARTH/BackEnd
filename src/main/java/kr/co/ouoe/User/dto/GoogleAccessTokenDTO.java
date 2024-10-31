package kr.co.ouoe.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class GoogleAccessTokenDTO {
    String accessToken;
    String expiresIn;
    String refreshToken;
    String scope;
    String tokenType;


}
