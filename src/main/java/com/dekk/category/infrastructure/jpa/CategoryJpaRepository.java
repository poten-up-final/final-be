package com.dekk.category.infrastructure.jpa;

import com.dekk.category.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDepth(int depth);
    List<Category> findAllByParentId(Long parentId);
    void deleteAllByParentId(Long parentId);
}
