package com.dekk.deck.domain.repository;

import com.dekk.deck.domain.model.DeckMember;

public interface DeckMemberRepository {
    DeckMember save(DeckMember deckMember);

    void deleteAllByDeckId(Long deckId);

    long countByUserId(Long userId);
}
