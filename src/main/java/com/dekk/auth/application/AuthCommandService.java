package com.dekk.auth.application;

import com.dekk.auth.jwt.JwtTokenProvider;
import com.dekk.auth.presentation.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse refreshToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
