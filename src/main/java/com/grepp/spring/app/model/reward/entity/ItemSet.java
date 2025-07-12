package com.grepp.spring.app.model.reward.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_set",
uniqueConstraints = @UniqueConstraint(
    columnNames = {"hat", "hair", "face", "top", "bottom"}

)
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setId;

    private Long hat;
    private Long hair;
    private Long face;
    private Long top;
    private Long bottom;

    @Column(nullable = false)
    private String image;


    @Builder
    public ItemSet(Long setId, Long hat, Long hair, Long face, Long top, Long bottom, String image) {
        this.setId = setId;
        this.hat = hat;
        this.hair = hair;
        this.face = face;
        this.top = top;
        this.bottom = bottom;
        this.image = image;
    }
}






