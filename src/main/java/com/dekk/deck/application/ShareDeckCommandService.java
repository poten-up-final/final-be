package com.dekk.deck.application;

import com.dekk.deck.application.dto.result.ShareTokenResult;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import com.dekk.deck.domain.model.enums.DeckType;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import com.dekk.deck.infrastructure.redis.DeckInviteRedisRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShareDeckCommandService {

    private static final Duration TOKEN_TTL = Duration.ofHours(24);
    private static final long OVERLAP_THRESHOLD_SECONDS = 600L;
    private static final int MAX_TOTAL_DECK_COUNT = 9;

    private final DeckRepository deckRepository;
    private final DeckMemberRepository deckMemberRepository;
    private final DeckInviteRedisRepository deckInviteRedisRepository;

    public ShareTokenResult turnOnShareAndGetToken(Long userId, Long deckId) {
        Deck deck = getDeckAsHost(deckId, userId);

        if (deck.getDeckType() == DeckType.CUSTOM) {
            deck.changeToShared();
        }

        return getOrCreateShareToken(deckId);
    }

    public void turnOffShare(Long userId, Long deckId) {
        Deck deck = getDeckAsHost(deckId, userId);

        if (deck.getDeckType() == DeckType.SHARED) {
            deck.changeToCustom();
        }

        deckMemberRepository.deleteAllGuestsByDeckId(deckId, DeckRole.GUEST);

        clearShareToken(deckId);
    }

    public void joinSharedDeck(Long userId, String token) {
        Long deckId = deckInviteRedisRepository
                .getDeckIdByToken(token)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.SHARE_TOKEN_EXPIRED));

        Deck deck = deckRepository
                .findById(deckId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));

        validateSharedDeck(deck);
        validateNotAlreadyJoined(deckId, userId);
        validateDeckLimit(userId);

        DeckMember guestMember = DeckMember.create(deckId, userId, DeckRole.GUEST);
        deckMemberRepository.save(guestMember);
    }

    private Deck getDeckAsHost(Long deckId, Long userId) {
        DeckMember member = deckMemberRepository
                .findByDeckIdAndUserId(deckId, userId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));

        validateHostRole(member);

        Deck deck = deckRepository
                .findById(deckId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));
        if (deck.getDeckType() == DeckType.DEFAULT) {
            throw new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_CANNOT_BE_MODIFIED);
        }

        return deck;
    }

    private ShareTokenResult getOrCreateShareToken(Long deckId) {
        Optional<String> existingToken = deckInviteRedisRepository.getTokenByDeckId(deckId);

        if (existingToken.isPresent()) {
            long remainingSeconds = deckInviteRedisRepository.getRemainingSeconds(deckId);

            if (remainingSeconds > OVERLAP_THRESHOLD_SECONDS) {
                return new ShareTokenResult(existingToken.get(), remainingSeconds);
            }
        }

        String newToken = UUID.randomUUID().toString().replace("-", "");
        deckInviteRedisRepository.saveToken(deckId, newToken, TOKEN_TTL);

        return new ShareTokenResult(newToken, TOKEN_TTL.getSeconds());
    }

    private void clearShareToken(Long deckId) {
        String currentToken = deckInviteRedisRepository.getTokenByDeckId(deckId).orElse(null);
        deckInviteRedisRepository.deleteTokens(deckId, currentToken);
    }

    private void validateSharedDeck(Deck deck) {
        if (deck.getDeckType() != DeckType.SHARED) {
            throw new DeckBusinessException(DeckErrorCode.DECK_IS_NOT_SHARED);
        }
    }

    private void validateNotAlreadyJoined(Long deckId, Long userId) {
        if (deckMemberRepository.findByDeckIdAndUserId(deckId, userId).isPresent()) {
            throw new DeckBusinessException(DeckErrorCode.ALREADY_JOINED_DECK);
        }
    }

    private void validateDeckLimit(Long userId) {
        long currentDeckCount = deckMemberRepository.countByUserId(userId);
        if (currentDeckCount >= MAX_TOTAL_DECK_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.DECK_LIMIT_EXCEEDED);
        }
    }

    private void validateHostRole(DeckMember member) {
        if (member.getRole() != DeckRole.HOST) {
            throw new DeckBusinessException(DeckErrorCode.GUEST_CANNOT_GENERATE_TOKEN);
        }
    }
}
