package kr.co.oneusonearth.User.repository;

import kr.co.oneusonearth.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.OptionalInt;

public interface UserRepository extends JpaRepository<User, Long> {

    //이메일로 로그인시
    Optional<User> findByEmail(String email);

}
