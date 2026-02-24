package com.dekk.card.infrastructure.jpa;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {
    private final CardJpaRepository cardJpaRepository;

    @Override
    public Slice<Card> findUnseenCardsByUserId(Long userId, Pageable pageable) {
        return cardJpaRepository.findUnseenCardsByUserId(userId, pageable);
    }
}
