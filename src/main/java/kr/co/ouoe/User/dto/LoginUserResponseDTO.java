package kr.co.ouoe.User.dto;


import kr.co.ouoe.common.jwt.dto.TokenDto;
import kr.co.ouoe.User.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserResponseDTO {
    private  String nickname;
    private String name;
    private String password;
    private String phoneNumber;
    private String adress;
    private  String email;
    private  String profileImg;
    private TokenDto token;

    public LoginUserResponseDTO(User user, TokenDto tokenDto) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImg=user.getImagePath();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.adress = user.getAdress();
        this.password = user.getPassword();
        this.token = tokenDto;
    }
}