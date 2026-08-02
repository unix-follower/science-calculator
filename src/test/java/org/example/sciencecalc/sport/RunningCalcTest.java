package org.example.sciencecalc.sport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunningCalcTest {
    private static final double DELTA4 = 0.0001;

    @Test
    void testRunningPace() {
        // given
        final short distanceRun = 5000; // 5 km
        final short timeSpent = 960; // 16 minutes
        // when
        final double runningPace = RunningCalc.runningPace(distanceRun, timeSpent);
        // then
        assertEquals(5.20833333, runningPace, DELTA4);
    }
}
