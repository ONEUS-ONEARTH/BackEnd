package kr.co.ouoe.MeetingPost.dto;


import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingPostModifyRequestDTO {

    private Long meetingPostId;
    private String title;
    private String content;
   // private String tag;
    private String thumbnailUrl;
    private long x;//위도
    private long y;//경도
}
