package kr.co.ouoe.MeetingPost.repository;


import kr.co.ouoe.MeetingPost.entity.MeetingReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingReplyRepository extends JpaRepository<MeetingReply,Long> {

    List<MeetingReply> findByPostId(Long postId);
}
