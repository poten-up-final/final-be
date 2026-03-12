package com.dekk.card.presentation.request;

import com.dekk.card.application.dto.command.AssignCategoriesCommand;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AssignCategoriesRequest(
        @NotNull(message = "카테고리 ID 목록은 필수값입니다") List<Long> categoryIds) {
    public AssignCategoriesCommand toCommand() {
        return new AssignCategoriesCommand(categoryIds);
    }
}
