package kr.co.ouoe.MeetingPost.domain;

import jakarta.persistence.*;
import kr.co.ouoe.User.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "meeting_post")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingPost  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private Long id;

    @Column(name = "title" ,nullable = false)
    private  String title;

    @Column(name = "content",nullable = false)
    private  String content;

    @Column(name = "thumb_nail")
    private String ThumbNail;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private MeetingLocate meetingLocate;

    @Builder
    public MeetingPost(String title, String content, String ThumbNail) {
        this.title = title;
        this.content = content;
        this.ThumbNail = ThumbNail;
    }




}
