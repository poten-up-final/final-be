package com.dekk.card.infrastructure;

import com.dekk.card.domain.repository.CardCategoryRepository;
import com.dekk.card.infrastructure.jpa.CardCategoryJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardCategoryRepositoryImpl implements CardCategoryRepository {

    private final CardCategoryJpaRepository cardCategoryJpaRepository;

    @Override
    public void softDeleteAllByCategoryId(Long categoryId) {
        cardCategoryJpaRepository.softDeleteAllByCategoryId(categoryId);
    }

    @Override
    public void softDeleteAllByCategoryIdIn(List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return;
        }
        cardCategoryJpaRepository.softDeleteAllByCategoryIdIn(categoryIds);
    }
}
