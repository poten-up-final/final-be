package com.dekk.deck.infrastructure.jpa;

import com.dekk.deck.domain.model.DeckMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckMemberJpaRepository extends JpaRepository<DeckMember, Long> {

    void deleteAllByDeckId(Long deckId);

    long countByUserId(Long userId);
}
