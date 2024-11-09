package kr.co.ouoe.DiyPost.repository;


import kr.co.ouoe.DiyPost.entity.LikeScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeScoreRepository extends JpaRepository<LikeScore, Long> {


    boolean existsLikeScoreByPostIdAndUserId(long postId, long userId);
    LikeScore findLikeScoreByPostIdAndUserId(long postId, long userId);
}
