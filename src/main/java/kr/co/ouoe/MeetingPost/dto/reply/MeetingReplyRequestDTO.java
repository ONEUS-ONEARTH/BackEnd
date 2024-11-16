package kr.co.ouoe.MeetingPost.dto.reply;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingReplyRequestDTO {
    private long id;
    private long postId;
    private String content;
}
