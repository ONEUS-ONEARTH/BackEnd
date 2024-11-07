package kr.co.ouoe.DiyPost.dto;


import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {

    private String title;
    private String content;
    private String tag;
    private String thumbnailUrl;
    private int likeScore;


}
