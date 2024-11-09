package kr.co.ouoe.MeetingPost.dto;

import kr.co.ouoe.DiyPost.dto.PostResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageMeetingPostResponseDTO {
    private List<MeetingResponseDTO> list;
    private int allPageNo;
}