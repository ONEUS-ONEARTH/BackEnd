package kr.co.oneusonearth.MeetingPost.domain;

import jakarta.persistence.*;
import kr.co.oneusonearth.User.domain.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
