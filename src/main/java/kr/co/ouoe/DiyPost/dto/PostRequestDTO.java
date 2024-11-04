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

    private String titile;
    private String content;
    private String tag;
    private String thumbnailUrl;


}
