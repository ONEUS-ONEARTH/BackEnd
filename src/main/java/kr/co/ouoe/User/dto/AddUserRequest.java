package kr.co.ouoe.User.dto;

import kr.co.ouoe.User.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

//회원가입 요청할때 포장되어 오는 패키지
@Getter
@Setter
public class AddUserRequest {

    private  String name;
    private  String nickname;
    private String email;
    private String password;
    private String adress;
    private String phoneNumber;
    private MultipartFile imageFile;


    /*
    public User to() {
        return User.of(
                nickname,
                email,
                password,
                adress,
                phoneNumber
        );
    }
    */
    public User toEntity(BCryptPasswordEncoder encoder, String imgPath){

        String password = encoder.encode(this.password);
        return
                User.builder()
                        .email(email)
                        .nickname(nickname)
                        .name(name)
                        .password(password)
                        .loginMethod("일반")
                        .phoneNumber(phoneNumber)
                        .imagePath(imgPath)
                        .adress(adress)
                        .build();
    }



}
