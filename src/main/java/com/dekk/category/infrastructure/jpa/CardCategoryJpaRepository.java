package com.dekk.category.infrastructure.jpa;

import com.dekk.category.domain.model.CardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardCategoryJpaRepository extends JpaRepository<CardCategory, Long> {
    boolean existsByCardIdAndCategoryId(Long cardId, Long categoryId);
    List<CardCategory> findAllByCategoryId(Long categoryId);
    List<CardCategory> findAllByCardId(Long cardId);
    void deleteAllByCategoryId(Long categoryId);
    void deleteAllByCardId(Long cardId);
}
