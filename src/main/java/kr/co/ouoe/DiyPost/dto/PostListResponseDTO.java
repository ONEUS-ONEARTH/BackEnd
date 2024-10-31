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
public class PostListResponseDTO {

    private String error;
    private List<PostResponseDTO> boards;

}
