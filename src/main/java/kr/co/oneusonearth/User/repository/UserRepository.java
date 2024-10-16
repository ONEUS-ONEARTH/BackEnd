package kr.co.oneusonearth.user.repository;

import kr.co.oneusonearth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //이메일로 로그인시
    Optional<User> findByEmail(String email);
    //이메일 중복체크
    Boolean existsUserByEmail(String email);
    Boolean existsUserByPhoneNumber(String phone);


}
