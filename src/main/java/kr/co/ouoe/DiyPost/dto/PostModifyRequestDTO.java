package kr.co.ouoe.DiyPost.dto;


import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostModifyRequestDTO {

    private Long postId;
    private String title;
    private String content;
    private String tag;
    private String thumbnailUrl;
}
