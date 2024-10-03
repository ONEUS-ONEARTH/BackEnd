package kr.co.oneusonearth.MeetingPost.repository;

import kr.co.oneusonearth.MeetingPost.domain.MeetingPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPostRepository extends JpaRepository<MeetingPost,Long> {
}
