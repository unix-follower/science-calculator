package org.example.sciencecalc.sport;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SportCalcTest {
    private static final double DELTA2 = 0.01;

    @Nested
    class CaloriesBurned {
        @Test
        void testStairsCalorie() {
            // given the Empire State Building
            final double met = 8.8;
            final byte bodyWeight = 75;
            final double timeElapsed = 9.3;
            // when
            final double totalBurned = SportCalc.CaloriesBurned.stairsCalorie(met, bodyWeight, timeElapsed);
            // then
            assertEquals(107.42, totalBurned, DELTA2);
        }
    }
}
