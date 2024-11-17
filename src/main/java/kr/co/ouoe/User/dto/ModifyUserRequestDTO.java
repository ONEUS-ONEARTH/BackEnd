package kr.co.ouoe.User.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyUserRequestDTO {

    private String nickname;
    private String adress;
    private String phone;

    private MultipartFile image;

}
