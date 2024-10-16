package kr.co.oneusonearth.user.entity;

import jakarta.persistence.*;
import kr.co.oneusonearth.DiyPost.domain.DiyPost;
import lombok.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark   {//UserDetails를 상속잗아 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diypost_id")
    private DiyPost diyPost;


}
