package kr.co.ouoe.DiyPost.dto;



import kr.co.ouoe.DiyPost.entity.DiyPost;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {

    private  long id;
    private  String title;
    private  String content;
    private  String author;
    private String tag;
    private long userId;
    private LocalDateTime createdDate;

    public PostResponseDTO(DiyPost diyPost) {
        this.id = diyPost.getId();
        this.title = diyPost.getTitle();
        this.tag= diyPost.getTag();
        this.content = diyPost.getContent();
        this.userId = diyPost.getUserId();
        //this.author=diyPost.getUser().getNickname();
        this.createdDate = diyPost.getCreatedAt();
    }
}
