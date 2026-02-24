package com.dekk.card.domain.repository;

import com.dekk.card.domain.model.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CardRepository {
    Slice<Card> findUnseenCardsByUserId(Long userId, Pageable pageable);
}
