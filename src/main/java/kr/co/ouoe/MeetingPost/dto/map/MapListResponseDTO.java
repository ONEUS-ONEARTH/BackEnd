package kr.co.ouoe.MeetingPost.dto.map;


import kr.co.ouoe.MeetingPost.dto.MeetingResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapListResponseDTO {

    private String error;
    private List<MapResponseDTO> maps;
}
