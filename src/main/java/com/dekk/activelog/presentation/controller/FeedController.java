package com.dekk.activelog.presentation.controller;

import com.dekk.activelog.application.service.FeedService;
import com.dekk.activelog.presentation.response.UnseenCardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedController implements FeedApi {

    private final FeedService feedService;

    @Override
    public ResponseEntity<List<UnseenCardResponse>> getUnseenCards(Long userId, int size) {

        List<UnseenCardResponse> responses = feedService.getUnseenCards(userId, size);

        return ResponseEntity.ok(responses);
    }
}
