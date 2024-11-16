package kr.co.ouoe.MeetingPost.dto.reply;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingReplyModifyRequestDTO {
    private Long replyId;
    private Long postId;
    private String email;
    private String content;
}
