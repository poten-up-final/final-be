package com.dekk.card.recommend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RecommendScoringServiceTest {

    private final RecommendScoringService scoringService = new RecommendScoringService();

    @ParameterizedTest(name = "{5}")
    @CsvSource({
            "175, 70, 175, 70, 1.0,    키와 몸무게가 완전히 일치하면 1.0을 반환한다",
            "175, 70, 180, 77, 0.0,    키와 몸무게 차이가 범위 한계와 정확히 같으면 0.0을 반환한다",
            "175, 70, 178, 74, 0.4143, 체형 차이에 따라 정확한 점수를 반환한다",
    })
    void shouldReturnExpectedScore(int uh, int uw, int ch, int cw, double expected, String description) {
        assertThat(scoringService.calculateBodyScore(uh, uw, ch, cw))
                .isCloseTo(expected, within(0.001));
    }

    @Test
    @DisplayName("범위를 초과한 차이는 음수가 되지 않고 0.0으로 보정된다")
    void shouldClampTo0_whenDiffExceedsRange() {
        // height diff = 15 → 1 - 15/5 = -2.0  →  Math.max(0.0, -2.0) = 0.0
        // weight diff = 20 → 1 - 20/7 = -1.857.. →  Math.max(0.0, -1.857) = 0.0
        assertThat(scoringService.calculateBodyScore(175, 70, 190, 90))
                .isGreaterThanOrEqualTo(0.0);
    }


    @Test
    @DisplayName("카드 체형 정보가 없으면 체형 제한 없는 카드로 간주해 1.0을 반환한다")
    void shouldReturn1_whenCardBodyInfoIsNull() {
        assertThat(scoringService.calculateBodyScore(175, 70, null, null))
                .isCloseTo(1.0, within(0.001));
    }
}
