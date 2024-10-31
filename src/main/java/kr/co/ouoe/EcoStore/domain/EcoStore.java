package kr.co.ouoe.EcoStore.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "eco_store")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EcoStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "info")//가게에 대한 간략한 설명
    private String info;

    @Column(name = "longitude")
    private Long longitude;

    @Column(name = "langtitude")
    private Long langtitude;

}
