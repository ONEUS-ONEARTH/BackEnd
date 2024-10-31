package kr.co.ouoe.MeetingPost.domain;

import jakarta.persistence.*;
import kr.co.ouoe.User.entity.User;
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

    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_post_id")
    private MeetingPost meetingPost;


}
