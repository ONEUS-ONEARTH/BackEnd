package kr.co.ouoe.MeetingPost.dto.map;


import lombok.Builder;

@Builder
public class MapResponseDTO {
    private Double x;//위도
    private Double y; //경도
}
