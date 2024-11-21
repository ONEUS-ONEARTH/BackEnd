package kr.co.ouoe.MeetingPost.dto.map;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapResponseDTO {
    private Double x;//위도
    private Double y; //경도
}
