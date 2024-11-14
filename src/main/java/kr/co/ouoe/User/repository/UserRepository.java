package kr.co.ouoe.User.repository;



import kr.co.ouoe.User.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //이메일로 로그인시
    User findByEmail(String email);
    //이메일 중복체크
    Boolean existsByEmail(String email);

    @Query("select kr.co.ouoe.User.entity.User from users where id=:id")
     User findOne(long id);

    @Override
    Optional<User> findById(Long aLong);

    //Boolean existsPhoneNumber(String phone);


}
