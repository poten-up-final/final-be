package com.dekk.auth.presentation.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
