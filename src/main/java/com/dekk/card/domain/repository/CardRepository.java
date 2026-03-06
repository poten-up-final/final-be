package com.dekk.card.domain.repository;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.model.enums.Platform;
import com.dekk.card.domain.model.enums.ProductGender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardRepository {
    Card save(Card card);
    List<Card> saveAll(List<Card> cards);
    boolean existsByPlatformAndOriginId(Platform platform, String originId);
    Page<Card> findCardsWithImageByStatus(CardStatus status, Pageable pageable);
    Page<Card> findCardsWithProductsByStatus(CardStatus status, Pageable pageable);
    List<Card> findAllByIdInWithProducts(List<Long> ids);
    List<Card> findRecommendCandidates(
            List<Long> excludedCardIds,
            List<ProductGender> genders,
            int minHeight,
            int maxHeight,
            int minWeight,
            int maxWeight
    );
}
