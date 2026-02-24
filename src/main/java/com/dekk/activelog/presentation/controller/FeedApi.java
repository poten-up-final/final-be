package com.dekk.activelog.presentation.controller;

import com.dekk.activelog.presentation.response.UnseenCardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Feed API", description = "메인 피드 및 카드 추천 관련 API")
@Validated
public interface FeedApi {

    @Operation(summary = "미평가 카드 추출 API", description = "유저가 아직 스와이프(평가)하지 않은 새로운 카드를 최상단부터 size만큼 조회합니다.")
    @GetMapping("/unseen")
    ResponseEntity<List<UnseenCardResponse>> getUnseenCards(
        @Parameter(description = "임시 유저 ID (추후 토큰으로 대체)", example = "1")
        @RequestParam Long userId,

        @Parameter(description = "한 번에 가져올 카드 개수 (1~30)", example = "10")
        @RequestParam(defaultValue = "10")
        @Min(1) @Max(30) int size
    );
}
