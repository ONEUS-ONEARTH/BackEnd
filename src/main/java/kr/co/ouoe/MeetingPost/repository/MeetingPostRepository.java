package kr.co.ouoe.MeetingPost.repository;

import kr.co.ouoe.MeetingPost.domain.MeetingPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPostRepository extends JpaRepository<MeetingPost,Long> {
}
