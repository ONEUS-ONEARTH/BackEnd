package kr.co.ouoe.DiyPost.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostTopListResponseDTO {

    private String error;
    private List<PostTopResponseDTO> boards;
    private int totalPost;

}
