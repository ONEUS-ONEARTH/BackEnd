package kr.co.ouoe.MeetingPost.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTopListResponseDTO {

    private String error;
    private List<MeetingTopResponseDTO> boards;
    private int totalPost;
}
