package com.dekk.card.recommend.application;

import org.springframework.stereotype.Service;

@Service
public class RecommendScoringService {

    private static final double HEIGHT_RANGE = 5.0;
    private static final double WEIGHT_RANGE = 7.0;
    private static final double MAX_SCORE = 1.0;
    private static final double MIN_SCORE = 0.0;
    private static final double SCORE_DIMENSION_COUNT = 2.0;

    public double calculateBodyScore(int userHeight, int userWeight, Integer cardHeight, Integer cardWeight) {
        double heightScore = calculateDimensionScore(userHeight, cardHeight, HEIGHT_RANGE);
        double weightScore = calculateDimensionScore(userWeight, cardWeight, WEIGHT_RANGE);
        return (heightScore + weightScore) / SCORE_DIMENSION_COUNT;
    }

    private double calculateDimensionScore(int userValue, Integer cardValue, double range) {
        if (cardValue == null) {
            return MAX_SCORE;
        }
        double diff = Math.abs(userValue - cardValue);
        return Math.max(MIN_SCORE, MAX_SCORE - diff / range);
    }
}
