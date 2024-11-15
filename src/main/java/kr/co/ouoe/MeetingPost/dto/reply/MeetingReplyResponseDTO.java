package kr.co.ouoe.MeetingPost.dto.reply;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingReplyResponseDTO {

    private String author;
    private String content;
    private LocalDateTime createDate;
}
