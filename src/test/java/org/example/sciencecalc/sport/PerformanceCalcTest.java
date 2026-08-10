package org.example.sciencecalc.sport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PerformanceCalcTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;

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

    @Test
    void testMaxHeartRateHaskellFox() {
        // given
        final byte age = 32;
        // when
        final double maxHeartRate = PerformanceCalc.maxHeartRateHaskellFox(age);
        // then
        assertEquals(188, maxHeartRate, DELTA1);
    }

    @Test
    void testMaxHeartRateInbar() {
        // given
        final byte age = 32;
        // when
        final double maxHeartRate = PerformanceCalc.maxHeartRateInbar(age);
        // then
        assertEquals(183.88, maxHeartRate, DELTA2);
    }

    @Test
    void testMaxHeartRateNes() {
        // given
        final byte age = 32;
        // when
        final double maxHeartRate = PerformanceCalc.maxHeartRateNes(age);
        // then
        assertEquals(190.52, maxHeartRate, DELTA2);
    }

    @Test
    void testMaxHeartRateOaklandNonlinear() {
        // given
        final byte age = 32;
        // when
        final double maxHeartRate = PerformanceCalc.maxHeartRateOaklandNonlinear(age);
        // then
        assertEquals(184.83, maxHeartRate, DELTA2);
    }

    @Test
    void testMaxHeartRateTanakaMonahanSeals() {
        // given
        final byte age = 32;
        // when
        final double maxHeartRate = PerformanceCalc.maxHeartRateTanakaMonahanSeals(age);
        // then
        assertEquals(185.6, maxHeartRate, DELTA2);
    }
}
