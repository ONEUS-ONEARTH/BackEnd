package kr.co.ouoe.MeetingPost.domain;

import jakarta.persistence.*;
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
    @Lob
    private  String content;

    @Column(name = "thumb_nail")
    private String ThumbNail;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

   @Column(name = "user_id",nullable = false)
   private Long userId;

   @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
   @JoinColumn(name = "meeting_locate_id", referencedColumnName = "id")
   private MeetingLocate meetingLocate;



   @Column
   @Enumerated(EnumType.STRING)
   private Option option;


    @Builder
    public MeetingPost(String title, String content,LocalDateTime createdAt, String ThumbNail,long userId,Option option,MeetingLocate meetingLocate) {
        this.title = title;
        this.content = content;
        this.ThumbNail = ThumbNail;
        this.createdAt = createdAt;
        this.userId=  userId;
        this.option = option;
        this.meetingLocate = meetingLocate;
    }




}
