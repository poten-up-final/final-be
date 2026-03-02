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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/cards")
@RequiredArgsConstructor
public class CardQueryController implements CardQueryApi {

    private final CardQueryService cardQueryService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<?>>> getCards(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {
        if (userDetails != null) {
            return ResponseEntity.ok(ApiResponse.of(CardResultCode.MEMBER_CARD_LIST_SUCCESS, getMemberCards(pageable)));
        }
        return ResponseEntity.ok(ApiResponse.of(CardResultCode.GUEST_CARD_LIST_SUCCESS, getGuestCards(pageable)));
    }

    private PageResponse<MemberCardResponse> getMemberCards(Pageable pageable) {
        return PageResponse.from(
                cardQueryService.getCardsForMember(pageable)
                        .map(MemberCardResponse::from)
        );
    }

    private PageResponse<GuestCardResponse> getGuestCards(Pageable pageable) {
        return PageResponse.from(
                cardQueryService.getCardsForGuest(pageable)
                        .map(GuestCardResponse::from)
        );
    }
}
