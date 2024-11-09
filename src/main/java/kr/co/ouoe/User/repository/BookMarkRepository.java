package kr.co.ouoe.User.repository;

import kr.co.ouoe.User.entity.BookMark;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
   boolean existsBookMarkByUserIdAndAndPostId(long userId, long postId);
}
