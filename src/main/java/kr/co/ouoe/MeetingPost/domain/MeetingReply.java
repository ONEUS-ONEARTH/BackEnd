package kr.co.ouoe.MeetingPost.domain;

import jakarta.persistence.*;
import kr.co.ouoe.User.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "meeting_reply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class MeetingReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "createDate")
    private LocalDateTime createDate;


}
