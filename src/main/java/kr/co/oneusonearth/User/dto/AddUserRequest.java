package kr.co.oneusonearth.User.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

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

}
