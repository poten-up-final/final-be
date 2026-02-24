package com.dekk.card.infrastructure.jpa;

import com.dekk.card.domain.model.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardJpaRepository extends JpaRepository<Card, Long> {

    @Query("""
            SELECT c FROM Card c
            WHERE c.isActive = true
            AND NOT EXISTS (
                SELECT 1 FROM ActiveLog al
                WHERE al.cardId = c.id AND al.userId = :userId
            )
            ORDER BY c.createdAt DESC
            """)
    Slice<Card> findUnseenCardsByUserId(@Param("userId") Long userId, Pageable pageable);
}
