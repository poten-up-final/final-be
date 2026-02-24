package com.dekk.card.application;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardQueryService {

    private final CardRepository cardRepository;

    public Slice<Card> getUnseenCardsByUserId(Long userId, Pageable pageable) {
        return cardRepository.findUnseenCardsByUserId(userId, pageable);
    }
}
