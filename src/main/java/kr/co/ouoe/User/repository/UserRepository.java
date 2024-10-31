package kr.co.ouoe.User.repository;


import kr.co.ouoe.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //이메일로 로그인시
    User findByEmail(String email);
    //이메일 중복체크
    Boolean existsByEmail(String email);
    //
    //Boolean existsPhoneNumber(String phone);


}
