package com.dekk.card.presentation.controller;

import com.dekk.card.application.CardQueryService;
import com.dekk.card.presentation.response.CardResultCode;
import com.dekk.card.presentation.response.GuestCardResponse;
import com.dekk.card.presentation.response.MemberCardResponse;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardQueryController implements CardQueryApi {

    private final CardQueryService cardQueryService;

    @Override
    @GetMapping("/w/v1/guests/cards")
    public ResponseEntity<ApiResponse<PageResponse<GuestCardResponse>>> getGuestCards(
            @ParameterObject Pageable pageable
    ) {
        PageResponse<GuestCardResponse> response = PageResponse.from(
                cardQueryService.getCardsForGuest(pageable)
                        .map(GuestCardResponse::from)
        );
        return ResponseEntity.ok(ApiResponse.of(CardResultCode.GUEST_CARD_LIST_SUCCESS, response));
    }

    @Override
    @GetMapping("/w/v1/members/cards")
    public ResponseEntity<ApiResponse<PageResponse<MemberCardResponse>>> getMemberCards(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {
        PageResponse<MemberCardResponse> response = PageResponse.from(
                cardQueryService.getCardsForMember(pageable)
                        .map(MemberCardResponse::from)
        );
        return ResponseEntity.ok(ApiResponse.of(CardResultCode.MEMBER_CARD_LIST_SUCCESS, response));
    }
}
