package kr.co.ouoe.MeetingPost.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "meeting_locate")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED )
public class MeetingLocate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    //@Column(name = "meeting_post_id")
    //private Long meetingPostId;

    @Column(name = "lantitude",nullable = false)
    private double x;

    @Column(name = "longitude",nullable = false)
    private double y;


    @Builder
    public MeetingLocate( Long x, Long y) {
        this.x =x;
        this.y = y;
    }
}
