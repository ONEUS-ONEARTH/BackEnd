package kr.co.ouoe.MeetingPost.repository;

import kr.co.ouoe.MeetingPost.entity.MeetingPost;
import kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingPostRepository extends JpaRepository<MeetingPost,Long> {

    @Query("Select new kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO(" +
            "m )from MeetingPost m")
    List<MeetingResponseDTO> findAllMeetingResponseDTO();
    MeetingPost findByIdAndUserId(Long postId, Long userId);


    @Query("Select new kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO(" +
            "m )from MeetingPost m where m.userId= :userId ")
    List<MeetingResponseDTO> findAllMeetingResponseDTOByUserId(Long userId);

}
