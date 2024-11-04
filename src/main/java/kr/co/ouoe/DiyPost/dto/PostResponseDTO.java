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
    private LocalDateTime createdDate;

    public PostResponseDTO(DiyPost diyPost) {
        this.id = diyPost.getId();
        this.title = diyPost.getTitle();
        this.content = diyPost.getContent();
        //this.author=diyPost.getUser().getNickname();
        this.createdDate = diyPost.getCreatedAt();
    }
}
