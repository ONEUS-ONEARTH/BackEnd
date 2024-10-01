package kr.co.oneusonearth.User.repository;

import kr.co.oneusonearth.User.domain.BookMark;
import kr.co.oneusonearth.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

}
