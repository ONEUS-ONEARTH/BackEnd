package kr.co.ouoe.MeetingPost.dto;


import kr.co.ouoe.MeetingPost.entity.MeetingLocate;
import kr.co.ouoe.MeetingPost.entity.MeetingPost;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTopResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private String author;
    private  LocalDateTime createDate;
    private boolean isEditable;
    private boolean isCilcked;
    private long userId;
    private long meetingId;
    private String option; //개인인지 회사인지
    private String adress; //주소
    private int likeScore;
   // private MeetingLocate meetingLocate;
    private double x;
    private double y;

    public MeetingTopResponseDTO(MeetingPost meetingPost) {
        this.id = meetingPost.getId();
        this.title = meetingPost.getTitle();
        this.content = meetingPost.getContent();
        this.thumbnailUrl=meetingPost.getThumbNail();
        this.createDate=meetingPost.getCreatedAt();
        this.userId=meetingPost.getUserId();
        this.option=meetingPost.getOption().toString();
        this.likeScore=meetingPost.getLikeScore();
        this.x=meetingPost.getMeetingLocate().getX();
        this.y=meetingPost.getMeetingLocate().getY();
        this.adress=meetingPost.getMeetingLocate().getAddress();

    }

}
