package kr.co.ouoe.MeetingPost.dto;

import kr.co.ouoe.DiyPost.entity.DiyPost;
import kr.co.ouoe.MeetingPost.domain.MeetingPost;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private String author;
    private  LocalDateTime createDate;
    private long userId;
    private long meetingId;

    public MeetingResponseDTO(MeetingPost meetingPost) {
        this.id = meetingPost.getId();
        this.title = meetingPost.getTitle();
        this.content = meetingPost.getContent();
        this.thumbnailUrl=meetingPost.getThumbNail();
        this.createDate=meetingPost.getCreatedAt();

    }

}
