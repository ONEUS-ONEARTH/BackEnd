package kr.co.ouoe.DiyPost.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile thumbnailUrl;
    private int likeScore;


}
