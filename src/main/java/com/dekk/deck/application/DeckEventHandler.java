package com.dekk.deck.application;

import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.user.domain.event.UserOnboardedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeckEventHandler {

    private final DeckCommandService deckCommandService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUserOnboarded(UserOnboardedEvent event) {
        try {
            deckCommandService.createDefaultDeck(event.userId());
        } catch (Exception e) {
            log.error("기본 보관함 생성 실패: userId={}", event.userId(), e);
            throw new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_CREATE_FAILED);
        }
    }
}
