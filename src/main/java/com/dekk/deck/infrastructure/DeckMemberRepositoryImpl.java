package com.dekk.deck.infrastructure;

import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.infrastructure.jpa.DeckMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeckMemberRepositoryImpl implements DeckMemberRepository {

    private final DeckMemberJpaRepository jpaRepository;

    @Override
    public DeckMember save(DeckMember deckMember) {
        return jpaRepository.save(deckMember);
    }

    @Override
    public void deleteAllByDeckId(Long deckId) {
        jpaRepository.deleteAllByDeckId(deckId);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }
}
