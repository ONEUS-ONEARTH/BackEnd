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
public class MeetingListResponseDTO {

    private String error;
    private List<MeetingResponseDTO> boards;
}
