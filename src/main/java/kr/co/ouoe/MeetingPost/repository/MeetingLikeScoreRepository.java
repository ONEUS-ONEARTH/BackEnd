package kr.co.ouoe.MeetingPost.repository;


import kr.co.ouoe.DiyPost.entity.LikeScore;
import kr.co.ouoe.MeetingPost.domain.MeetingLikeScore;
import kr.co.ouoe.MeetingPost.domain.MeetingReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingLikeScoreRepository extends JpaRepository<MeetingLikeScore,Long> {

    boolean existsMeetingLikeScoreByIdAndPostId(long postId, long userId);
    LikeScore findMeetingLikeScoreByPostIdAndUserId(long postId, long userId);
}
