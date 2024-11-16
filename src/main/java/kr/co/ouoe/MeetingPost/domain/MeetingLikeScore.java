package kr.co.ouoe.MeetingPost.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "meeting_like_score")
@Getter
@Setter
public class MeetingLikeScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "post_id")
    private long postId;

    @Column(name = "user_id")
    private long userId;

}
