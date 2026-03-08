package com.dekk.deck.application;

import com.dekk.card.application.CardQueryService;
import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.deck.application.dto.result.DeckResult;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckCard;
import com.dekk.deck.domain.model.enums.DeckType;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeckQueryService {

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;
    private final CardQueryService cardQueryService;

    public List<DeckResult> getDecks(Long userId) {

        List<Deck> allDecks = getAllDecks(userId);
        List<Long> deckIds = allDecks.stream().map(Deck::getId).toList();

        Map<Long, Long> cardCountMap = deckCardRepository.countCardsByDeckIds(deckIds);

        List<DeckCard> allDeckCards = deckCardRepository.findAllByDeckIdIn(deckIds);
        Map<Long, List<Long>> topCardIdsPerDeck = extractTopCardIdsPerDeck(allDecks, allDeckCards);

        Map<Long, String> cardImageUrlMap = getCardImageUrlMap(topCardIdsPerDeck);

        return allDecks.stream()
            .map(deck -> mapToDeckResult(deck, cardCountMap, topCardIdsPerDeck, cardImageUrlMap))
            .toList();
    }

    private List<Deck> getAllDecks(Long userId) {
        Deck defaultDeck = deckRepository.findByUserIdAndIsDefaultTrue(userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_NOT_FOUND));

        List<Deck> customDecks = deckRepository.findAllByUserIdAndIsDefaultFalseOrderByCreatedAtDesc(userId);

        List<Deck> allDecks = new ArrayList<>();
        allDecks.add(defaultDeck);
        allDecks.addAll(customDecks);

        return allDecks;
    }

    private Map<Long, List<Long>> extractTopCardIdsPerDeck(List<Deck> allDecks, List<DeckCard> allDeckCards) {
        Map<Long, List<Long>> deckTopCardIdsMap = allDecks.stream()
            .collect(Collectors.toMap(Deck::getId, deck -> new ArrayList<>()));

        allDeckCards.stream()
            .sorted(Comparator.comparing(DeckCard::getId).reversed())
            .forEach(dc -> {
                List<Long> topCards = deckTopCardIdsMap.get(dc.getDeckId());
                if (topCards != null && topCards.size() < 3) {
                    topCards.add(dc.getCardId());
                }
            });

        return deckTopCardIdsMap;
    }

    private Map<Long, String> getCardImageUrlMap(Map<Long, List<Long>> topCardIdsPerDeck) {
        List<Long> allTopCardIds = topCardIdsPerDeck.values().stream()
            .flatMap(List::stream)
            .distinct()
            .toList();

        if (allTopCardIds.isEmpty()) {
            return Map.of();
        }

        List<MemberCardResult> cardResults = cardQueryService.getCardsByIds(allTopCardIds);
        return cardResults.stream()
            .collect(Collectors.toMap(MemberCardResult::cardId, MemberCardResult::cardImageUrl));
    }

    private DeckResult mapToDeckResult(Deck deck,
                                       Map<Long, Long> cardCountMap,
                                       Map<Long, List<Long>> topCardIdsPerDeck,
                                       Map<Long, String> cardImageUrlMap) {

        List<String> previewUrls = topCardIdsPerDeck.getOrDefault(deck.getId(), List.of()).stream()
            .map(cardId -> cardImageUrlMap.getOrDefault(cardId, ""))
            .filter(url -> !url.isBlank())
            .toList();

        return new DeckResult(
            deck.getId(),
            deck.getName(),
            deck.isDefault() ? DeckType.DEFAULT : DeckType.CUSTOM,
            cardCountMap.getOrDefault(deck.getId(), 0L),
            previewUrls
        );
    }
}
