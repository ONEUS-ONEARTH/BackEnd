package kr.co.ouoe.MeetingPost.dto.reply;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingReplyResponseList {

    private List<MeetingReplyResponseDTO> replyList;
    private int totalReplys; // 혹시 페이징 처리를 위한 총 댓글 개수

}
