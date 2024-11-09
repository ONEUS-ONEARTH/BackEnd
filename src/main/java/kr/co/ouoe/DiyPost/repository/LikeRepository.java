package kr.co.ouoe.DiyPost.repository;


import kr.co.ouoe.DiyPost.entity.Like;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {


    boolean existsLikeByPostIdAndUserId(long postId, long userId);
}
