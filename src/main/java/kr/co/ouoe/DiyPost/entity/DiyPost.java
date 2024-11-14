package kr.co.ouoe.DiyPost.entity;


import jakarta.persistence.*;

import kr.co.ouoe.User.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "diy_post")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class DiyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    @Lob
    private String content;

    @Column(name = "thumbnailurl")
    private String thumbnailurl;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "tag")
    private String tag;

    @Column(name = "diy_keywords")
    @ElementCollection
    private List<String> diyKeywords;

    @Column(name = "user_id",nullable = false)
    Long userId;

    @Column(name = "like_score",nullable = false)
    private int likeScore = 0;

    //첫 포스팅할때 쓰이는 빌더
    @Builder
    public DiyPost(String title, String content, LocalDateTime createdAt,String thumbnailurl,String tag,Long userId) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.thumbnailurl = thumbnailurl;
        this.userId=userId;
        this.tag=tag;

    }

    @Builder
    public void update(String title, String content, LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }


}
