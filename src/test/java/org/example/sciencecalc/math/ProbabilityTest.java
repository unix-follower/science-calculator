package org.example.sciencecalc.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProbabilityTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA4 = 0.0001;

    @Test
    void testAccuracy() {
        // given
        final byte truePositives = 10;
        final byte falsePositives = 5;
        final byte trueNegatives = 70;
        final byte falseNegatives = 15;
        // when
        final double accuracy = Probability
            .accuracy(truePositives, trueNegatives, falsePositives, falseNegatives);
        // then
        assertEquals(0.8, accuracy, DELTA1);
    }

    @Test
    void testPrecision() {
        // given
        final byte truePositives = 10;
        final byte falsePositives = 5;
        // when
        final double precision = Probability.precision(truePositives, falsePositives);
        // then
        assertEquals(0.6667, precision, DELTA4);
    }

    @Test
    void testRecall() {
        // given
        final byte truePositives = 10;
        final byte falseNegatives = 15;
        // when
        final double sensitivity = Probability.recall(truePositives, falseNegatives);
        // then
        assertEquals(0.4, sensitivity, DELTA1);
    }

    @Test
    void testSpecificity() {
        // given
        final byte trueNegatives = 70;
        final byte falsePositives = 5;
        // when
        final double specificity = Probability.specificity(trueNegatives, falsePositives);
        // then
        assertEquals(0.9333, specificity, DELTA4);
    }

    @Test
    void testF1Score() {
        // given
        final byte truePositives = 10;
        final byte falsePositives = 5;
        final byte falseNegatives = 15;
        // when
        final double f1Score = Probability.f1Score(truePositives, falsePositives, falseNegatives);
        // then
        assertEquals(0.5, f1Score, DELTA1);
    }

    @Test
    void testF1ScoreFromPrecisionAndRecall() {
        // given
        final double precision = 0.6667;
        final double recall = 0.4;
        // when
        final double f1Score = Probability.f1Score(precision, recall);
        // then
        assertEquals(0.5, f1Score, DELTA1);
    }

    @Test
    void testRisk() {
        // given
        final byte failureProbability = 12;
        final short loss = 1000; // USD
        // when
        final double risk = Probability.risk(failureProbability, loss);
        // then
        assertEquals(120, risk, DELTA1); // USD
    }

    @Test
    void testRelativeRisk() {
        // given
        final byte exposedGroupDisease = 80;
        final short exposedGroupNoDisease = 920;
        final byte controlGroupDisease = 10;
        final short controlGroupNoDisease = 990;
        // when
        final double risk = Probability
            .relativeRisk(exposedGroupDisease, exposedGroupNoDisease, controlGroupDisease, controlGroupNoDisease);
        // then
        assertEquals(8, risk, DELTA1);
    }

    @Test
    void testLowerConfidenceBound() {
        // given
        final byte exposedGroupDisease = 80;
        final short exposedGroupNoDisease = 920;
        final byte controlGroupDisease = 10;
        final short controlGroupNoDisease = 990;
        final double zScore = 1.959;
        // when
        final double lowerBound = Probability.lowerConfidenceBound(exposedGroupDisease,
            exposedGroupNoDisease, controlGroupDisease, controlGroupNoDisease, zScore);
        // then
        assertEquals(4.17, lowerBound, DELTA2);
    }

    @Test
    void testUpperConfidenceBound() {
        // given
        final byte exposedGroupDisease = 80;
        final short exposedGroupNoDisease = 920;
        final byte controlGroupDisease = 10;
        final short controlGroupNoDisease = 990;
        final double zScore = 1.959;
        // when
        final double upperBound = Probability.upperConfidenceBound(exposedGroupDisease,
            exposedGroupNoDisease, controlGroupDisease, controlGroupNoDisease, zScore);
        // then
        assertEquals(15.34, upperBound, DELTA2);
    }
}
