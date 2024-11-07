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
public class PagePostResponseDTO {
    private List<PostResponseDTO> list;
    private int allPageNo;
}