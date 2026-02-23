package com.dekk.card.domain;

import com.dekk.card.domain.enums.ProductGender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(name = "external_product_id")
    private String externalProductId;

    private String option;

    @Column(name = "is_similar", nullable = false)
    private Boolean isSimilar;

    @Column(name = "product_url")
    private String productUrl;

    @Enumerated(EnumType.STRING)
    private ProductGender gender;

}
