package kr.co.ouoe.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GoogleInfoDTO {
    private String name;
    private String email;
    private String imgPath;
    private String id;
}
