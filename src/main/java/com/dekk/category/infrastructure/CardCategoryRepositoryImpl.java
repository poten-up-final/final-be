package com.dekk.category.infrastructure;

import com.dekk.category.domain.model.CardCategory;
import com.dekk.category.domain.repository.CardCategoryRepository;
import com.dekk.category.infrastructure.jpa.CardCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardCategoryRepositoryImpl implements CardCategoryRepository {

    private final CardCategoryJpaRepository cardCategoryJpaRepository;

    @Override
    public CardCategory save(CardCategory cardCategory) {
        return cardCategoryJpaRepository.save(cardCategory);
    }

    @Override
    public List<CardCategory> saveAll(List<CardCategory> cardCategories) {
        return cardCategoryJpaRepository.saveAll(cardCategories);
    }

    @Override
    public boolean existsByCardIdAndCategoryId(Long cardId, Long categoryId) {
        return cardCategoryJpaRepository.existsByCardIdAndCategoryId(cardId, categoryId);
    }

    @Override
    public List<CardCategory> findAllByCategoryId(Long categoryId) {
        return cardCategoryJpaRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<CardCategory> findAllByCardId(Long cardId) {
        return cardCategoryJpaRepository.findAllByCardId(cardId);
    }

    @Override
    public void deleteAllByCategoryId(Long categoryId) {
        cardCategoryJpaRepository.deleteAllByCategoryId(categoryId);
    }

    @Override
    public void deleteAllByCardId(Long cardId) {
        cardCategoryJpaRepository.deleteAllByCardId(cardId);
    }
}
