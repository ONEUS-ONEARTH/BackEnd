package kr.co.ouoe.User.entity;

import jakarta.persistence.*;
import kr.co.ouoe.DiyPost.entity.DiyPost;
import kr.co.ouoe.User.account.BookMarkCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "bookmarks")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookMark   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private Long id;

    @Column(name = "user_Id")
    private Long userId;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)//업사이클포스트인지 미팅 포스트인지 구분ㅇ
    private BookMarkCategory bookMarkCategory;

    @Column(name = "post_id")
    private Long postId;





}
