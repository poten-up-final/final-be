package com.dekk.card.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "products")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductImage productImage;

    @Column(nullable = false)
    @Comment("상품명")
    private String name;

    @Column(nullable = false)
    @Comment("수집 시점 할인가")
    private Integer price;

    @Column(name = "external_product_id")
    @Comment("플랫폼 별 상품 고유 id")
    private String externalProductId;

    @Comment("상품 옵션 명")
    private String option;

    @Column(name = "is_similar", nullable = false)
    @Comment("유사 상품 여부")
    private Boolean isSimilar;

    @Column(name = "product_url")
    @Comment("상품 상세 화면 url")
    private String productUrl;

    @Comment("삭제일시")
    private LocalDateTime deletedAt;

}
