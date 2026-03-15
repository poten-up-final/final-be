package com.dekk.deck.infrastructure.jpa;

import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeckMemberJpaRepository extends JpaRepository<DeckMember, Long> {

    void deleteAllByDeckId(Long deckId);

    long countByUserId(Long userId);

    Optional<DeckMember> findByDeckIdAndUserId(Long deckId, Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE DeckMember dm SET dm.deletedAt = CURRENT_TIMESTAMP " +
        "WHERE dm.deckId = :deckId AND dm.role = :role")
    void deleteAllGuestsByDeckId(@Param("deckId") Long deckId, @Param("role") DeckRole role);
}
