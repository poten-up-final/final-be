package com.dekk.deck.infrastructure;

import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.infrastructure.jpa.DeckMemberJpaRepository;
import java.util.Optional;
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

    @Override
    public Optional<DeckMember> findByDeckIdAndUserId(Long deckId, Long userId) {
        return jpaRepository.findByDeckIdAndUserId(deckId, userId);
    }

    @Override
    public void deleteAllGuestsByDeckId(Long deckId, DeckRole role) {
        jpaRepository.deleteAllGuestsByDeckId(deckId, role);
    }
}
