package kr.co.ouoe.MeetingPost.dto.map;


import lombok.Builder;

@Builder
public class MapResponseDTO {
    private long x;//위도
    private long y; //경도
}
