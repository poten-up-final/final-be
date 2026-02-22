package com.dekk.card.domain;

import com.dekk.card.domain.enums.Platform;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "cards")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CardImage cardImage;

    @ElementCollection
    @CollectionTable(
        name = "card_tags",
        joinColumns = @JoinColumn(name = "card_id")
    )
    @Column(name = "tag_id")
    private List<Long> tagIds = new ArrayList<>();

    @Column(name = "origin_id", nullable = false, updatable = false)
    @Comment("플랫폼별 카드 고유 번호")
    private String originId;

    @Column(name = "is_active", nullable = false)
    @Comment("활성화 여부")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Comment("플랫폼명")
    private Platform platform;

    @Comment("추천 키")
    private Integer height;

    @Comment("추천 몸무게")
    private Integer weight;
}
