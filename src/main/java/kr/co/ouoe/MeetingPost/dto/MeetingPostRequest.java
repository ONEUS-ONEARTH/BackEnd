package kr.co.ouoe.MeetingPost.dto;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingPostRequest {
    private String title;
    private String content;
    private String host;//주최자 개인인지 기업인지
    private String thumbnailUrl;
    private String option; //개인또는 회사주최
    // 주후에 시작일 기입 예정
    private long x; //위도
    private long y; //경도
    
}
