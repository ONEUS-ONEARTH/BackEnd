package kr.co.ouoe.DiyPost.entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "diy_meterials")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DiyMeterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public DiyMeterial(String name){
        this.name=name;
    }

}
