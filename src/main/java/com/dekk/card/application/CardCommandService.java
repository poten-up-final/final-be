package com.dekk.card.application;

import com.dekk.card.application.dto.command.AssignCategoriesCommand;
import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.CardCategory;
import com.dekk.card.domain.repository.CardCategoryRepository;
import com.dekk.card.domain.repository.CardRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardCommandService {

    private final CardRepository cardRepository;
    private final CardCategoryRepository cardCategoryRepository;

    public void approveCard(Long cardId) {
        Card card = findCardOrThrow(cardId);
        card.approve();
    }

    public void rejectCard(Long cardId) {
        Card card = findCardOrThrow(cardId);
        card.reject();
    }

    public void assignCategories(Long cardId, AssignCategoriesCommand command) {
        findCardOrThrow(cardId);

        List<CardCategory> originCardCategories = cardCategoryRepository.findAllByCardId(cardId);

        Set<Long> originIds =
                originCardCategories.stream().map(CardCategory::getCategoryId).collect(Collectors.toSet());

        Set<Long> requestedIds = Set.copyOf(command.categoryIds());
        List<Long> categoryIdsToRemove =
                originIds.stream().filter(id -> !requestedIds.contains(id)).toList();

        List<CardCategory> newCardCategories = requestedIds.stream()
                .filter(id -> !originIds.contains(id))
                .map(categoryId -> CardCategory.create(cardId, categoryId))
                .toList();

        cardCategoryRepository.softDeleteByCardIdAndCategoryIdIn(cardId, categoryIdsToRemove);
        cardCategoryRepository.saveAll(newCardCategories);
    }

    private Card findCardOrThrow(Long cardId) {
        return cardRepository
                .findById(cardId)
                .orElseThrow(() -> new CardBusinessException(CardErrorCode.CARD_NOT_FOUND));
    }
}
