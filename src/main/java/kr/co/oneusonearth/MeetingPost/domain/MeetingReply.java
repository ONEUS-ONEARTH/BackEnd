package kr.co.oneusonearth.MeetingPost.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "meeting_reply")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MeetingReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content")
    private String content;

}
