package kr.co.ouoe.MeetingPost.repository;


import kr.co.ouoe.MeetingPost.entity.MeetingLikeScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingLikeScoreRepository extends JpaRepository<MeetingLikeScore,Long> {

    boolean existsMeetingLikeScoreByPostIdAndUserId(long postId, long userId);
    MeetingLikeScore findMeetingLikeScoreByPostIdAndUserId(long postId, long userId);
}
