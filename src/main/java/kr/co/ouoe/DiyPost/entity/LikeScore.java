package kr.co.ouoe.DiyPost.entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "like_score")
@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LikeScore {
    // 라이트 없어도 될듯
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "post_id")
    private long postId;

    @Column(name = "user_id")
    private long userId;





}
