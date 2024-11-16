package kr.co.ouoe.User.repository;

import kr.co.ouoe.User.entity.BookMark;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
   boolean existsBookMarkByUserIdAndAndPostId(long userId, long postId);


   List<BookMark> findAllByUserIdAndBookMarkCategory(long userId,String category);
}
