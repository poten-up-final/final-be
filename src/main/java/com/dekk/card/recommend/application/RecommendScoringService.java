package com.dekk.card.recommend.application;

import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.user.application.dto.result.UserInfoResult;
import org.springframework.stereotype.Service;

@Service
public class RecommendScoringService {

    private static final double HEIGHT_RANGE = 5.0;
    private static final double WEIGHT_RANGE = 7.0;

    public double calculateBodyScore(UserInfoResult user, MemberCardResult card) {
        double heightScore = calculateDimensionScore(user.height(), card.height(), HEIGHT_RANGE);
        double weightScore = calculateDimensionScore(user.weight(), card.weight(), WEIGHT_RANGE);
        return (heightScore + weightScore) / 2.0;
    }

    private double calculateDimensionScore(int userValue, Integer cardValue, double range) {
        if (cardValue == null) {
            return 1.0;
        }
        double diff = Math.abs(userValue - cardValue);
        return Math.max(0.0, 1.0 - diff / range);
    }
}
