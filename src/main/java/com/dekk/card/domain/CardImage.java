package com.dekk.card.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "card_images")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "origin_url", nullable = false)
    @Comment("원본 이미지 경로")
    private String originUrl;

    @Column(name = "image_url")
    @Comment("이미지 경로")
    private String imageUrl;

    @Column(name = "is_uploaded", nullable = false)
    @Comment("이미지 업로드 완료 여부")
    private Boolean isUploaded;
}
