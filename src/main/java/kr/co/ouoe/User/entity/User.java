package kr.co.ouoe.User.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails  {//UserDetails를 상속잗아 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private Long id;

    @Column(name = "name" ,nullable = false)
    private  String name;

    @Column(name = "nickname",nullable = false)
    private  String nickname;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "login_method")
    private String loginMethod;

    @Column(name = "adress")
    private String adress;

    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "img_path")
    private String imagePath;






    @Builder
    public User(String name, String nickname, String email, String password,String adress ,String loginMethod,String phoneNumber,String imagePath) {
        this.name = name;
        this.email=email;
        this.loginMethod=loginMethod;
        this.password=password;
        this.nickname=nickname;
        this.adress=adress;
        this.phoneNumber=phoneNumber;
        this.imagePath=imagePath;
    }


    public static User of(
            String name,String nickname, String email, String password, String phoneNumber,String adress,String loginMethod,String imagePath
    ) {
        return new User(name,nickname, email, password,adress,loginMethod,phoneNumber,imagePath);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    //사용자 패스워드 반환

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }
    //계정 만료여부 반환

    @Override
    public boolean isAccountNonExpired() {
        return true; // true_> 아직 만료 되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //true-> 잠금되지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
