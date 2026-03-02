package com.dekk.card.presentation.controller;

import com.dekk.card.presentation.response.GuestCardResponse;
import com.dekk.card.presentation.response.MemberCardResponse;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "카드 조회 API", description = "카드 조회 관련 API")
public interface CardQueryApi {

    @Operation(summary = "비회원 카드 목록 조회", description = "비회원용 카드 목록을 페이징하여 조회합니다.")
    ResponseEntity<ApiResponse<PageResponse<GuestCardResponse>>> getGuestCards(
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "회원 카드 목록 조회", description = "회원용 카드 목록을 페이징하여 조회합니다. 상품 정보가 포함됩니다.")
    ResponseEntity<ApiResponse<PageResponse<MemberCardResponse>>> getMemberCards(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @ParameterObject Pageable pageable
    );
}
