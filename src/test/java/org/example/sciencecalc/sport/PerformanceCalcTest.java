package org.example.sciencecalc.sport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PerformanceCalcTest {
    private static final double DELTA1 = 0.1;

    @Test
    void testTargetHeartRate() {
        // given
        final byte age = 32;
        final byte restingHeartRate = 70;
        final double intensityPercent = 55; // Moderate (50-60%)
        // when
        final double totalBurned = PerformanceCalc.targetHeartRate(age, restingHeartRate, intensityPercent);
        // then
        assertEquals(134.9, totalBurned, DELTA1);
    }
}
