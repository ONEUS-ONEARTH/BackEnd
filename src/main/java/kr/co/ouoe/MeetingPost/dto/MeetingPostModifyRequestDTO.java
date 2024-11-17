package kr.co.ouoe.MeetingPost.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile thumbnail;
    private long x;//위도
    private long y;//경도
}
