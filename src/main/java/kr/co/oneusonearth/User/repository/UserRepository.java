package kr.co.oneusonearth.User.repository;

import kr.co.oneusonearth.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
