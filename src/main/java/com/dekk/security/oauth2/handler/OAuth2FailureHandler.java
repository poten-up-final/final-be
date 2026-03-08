package com.dekk.security.oauth2.handler;

import com.dekk.auth.domain.exception.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String redirectUri;

    public OAuth2FailureHandler(@Value("${app.oauth2.redirect-uri}") String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 authentication failed: {}", exception.getMessage());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUri);

        if (exception instanceof OAuth2AuthenticationException oAuthException) {
            OAuth2Error error = oAuthException.getError();

            if (AuthErrorCode.DUPLICATE_EMAIL.code().equals(error.getErrorCode())) {
                String provider = error.getDescription() != null ? error.getDescription() : "unknown";
                builder.queryParam("error", AuthErrorCode.DUPLICATE_EMAIL.name())
                        .queryParam("provider", provider);
            } else {
                builder.queryParam("error", error.getErrorCode());
            }
        } else {
            builder.queryParam("error", exception.getLocalizedMessage());
        }

        String targetUrl = builder.build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
