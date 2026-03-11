package org.example.sciencecalc.math;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProbabilityTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;

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

    @Test
    void testIntersectionProbability() {
        // given
        final double probabilityA = 0.2; // 20 %
        final double probabilityB = 0.3; // 30 %
        // when
        final double probability = Probability.intersectionProbability(probabilityA, probabilityB);
        // then 6%
        assertEquals(0.06, probability, DELTA2);
    }

    @Test
    void testUnionProbability() {
        // given
        final double probabilityA = 0.2; // 20 %
        final double probabilityB = 0.3; // 30 %
        // when
        final double probability = Probability.unionProbability(probabilityA, probabilityB);
        // then 44%
        assertEquals(0.44, probability, DELTA2);
    }

    @Test
    void testSymmetricDifferenceProbability() {
        // given
        final double probabilityA = 0.2; // 20 %
        final double probabilityB = 0.3; // 30 %
        // when
        final double probability = Probability.symmetricDifferenceProbability(probabilityA, probabilityB);
        // then 38%
        assertEquals(0.38, probability, DELTA2);
    }

    @Test
    void testComplementProbability() {
        // given
        final double probabilityA = 0.2; // 20 %
        final double probabilityB = 0.3; // 30 %
        // when
        final double probability = Probability.complementProbability(probabilityA, probabilityB);
        // then 56%
        assertEquals(0.56, probability, DELTA2);
    }

    @Test
    void testComplementAProbability() {
        // given
        final double probabilityA = 0.2; // 20 %
        // when
        final double probability = Probability.complementAProbability(probabilityA);
        // then 56%
        assertEquals(0.8, probability, DELTA2);
    }

    @Test
    void testProbabilityOfSeriesAAlwaysOccurs() {
        // given
        final double probabilityA = 0.9; // 90 %
        final byte numberOfTrials = 10;
        // when
        final double probability = Probability.probabilityOfSeriesAAlwaysOccurs(probabilityA, numberOfTrials);
        // then 34.868%
        assertEquals(0.34868, probability, DELTA5);
    }

    @Test
    void testProbabilityOfSeriesANeverOccurs() {
        // given
        final double probabilityA = 0.2; // 20 %
        final byte numberOfTrials = 10;
        // when
        final double probability = Probability.probabilityOfSeriesANeverOccurs(probabilityA, numberOfTrials);
        // then 10.7374%
        assertEquals(0.107374, probability, DELTA6);
    }

    @Test
    void testProbabilityOfSeriesAOccursAtLeastOnce() {
        // given
        final double probabilityA = 0.2; // 20 %
        final byte numberOfTrials = 10;
        // when
        final double probability = Probability
            .probabilityOfSeriesAOccursAtLeastOnce(probabilityA, numberOfTrials);
        // then 56%
        assertEquals(0.89263, probability, DELTA5);
    }

    @Test
    void testBayesTheorem() {
        // given
        final double probabilityOfRain = 0.2;
        final double probabilityOfClouds = 0.45;
        final double cloudsOnRainyDay = 0.6;
        // when
        final double probability = Probability.bayesTheorem(probabilityOfRain, probabilityOfClouds, cloudsOnRainyDay);
        // then the probability of rain occurring given the cloudy morning
        assertEquals(0.26667, probability, DELTA5);
    }

    @Test
    void testJointProbability() {
        // given
        final double probabilityA = 0.8;
        final double probabilityB = 0.6;
        // when
        final double probability = Probability.jointProbability(probabilityA, probabilityB);
        // then
        assertEquals(0.48, probability, DELTA2);
    }

    @Test
    void testJointProbabilityOfDependentEvents() {
        // given
        final double probabilityA = 0.8;
        final double probabilityB = 0.7;
        // when
        final double probability = Probability.jointProbabilityOfDependentEvents(probabilityA, probabilityB);
        // then
        assertEquals(0.56, probability, DELTA2);
    }

    @Test
    void testConditionalProbabilityNotA() {
        // given P(A)=0.05
        final double probabilityA = 0.05;
        // when
        final double probability = Probability.conditionalProbabilityNotA(probabilityA);
        // then
        assertEquals(0.95, probability, DELTA2);
    }

    @Test
    void testConditionalProbabilityNotBGivenA() {
        // given P(B∣A)=0.09
        final double probabilityBGivenA = 0.09;
        // when
        final double probability = Probability.conditionalProbabilityNotBGivenA(probabilityBGivenA);
        // then
        assertEquals(0.91, probability, DELTA2);
    }

    @Test
    void testConditionalProbabilityBGivenNotA() {
        // given P(B̄∣Ā)=0.05
        final double probabilityNotBGivenNotA = 0.05;
        // when
        final double probability = Probability.conditionalProbabilityBGivenNotA(probabilityNotBGivenNotA);
        // then
        assertEquals(0.95, probability, DELTA2);
    }

    @Test
    void testConditionalProbability() {
        // given
        final double probabilityA = 0.05; // P(A)=0.05
        final double probabilityBGivenA = 0.91; // P(B|A)
        final double probabilityBGivenNotA = 0.05; // P(B|Ā)
        // when
        final double probability = Probability
            .conditionalProbability(probabilityA, probabilityBGivenA, probabilityBGivenNotA);
        // then
        assertEquals(0.4892, probability, DELTA4);
    }

    @Test
    void testChebyshevsTheorem() {
        // given
        final double variance = Stats.Descriptive.binomialDistributionVariance(20, 0.25, 0.75);
        final byte bound = 5;
        // when
        final double probability = Probability.chebyshevsTheorem(variance, bound);
        // then
        assertEquals(0.15, probability, DELTA2);
    }

    @Test
    void testChebyshevsTheoremForm2() {
        // given
        final double bound = 2.5;
        // when
        final double probability = Probability.chebyshevsTheorem(bound);
        // then
        assertEquals(0.16, probability, DELTA2);
    }

    @Test
    void testANOVA() {
        // given
        final double[][] groups = new double[][]{{8, 12, 10, 9, 11}, {6, 7, 5, 4, 6}, {10, 15, 12, 11, 13}};
        final int numOfGroups = groups.length;
        final long numOfObservations = Arrays.stream(groups).flatMapToDouble(Arrays::stream).count();
        // when
        final double ssw = Probability.anovaSSW(groups);
        final double ssb = Probability.anovaSSB(groups);
        final double sst = Stats.Descriptive.totalSumOfSquares(ssb, ssw);
        final double msb = Stats.Descriptive.msb(ssb, numOfGroups);
        final long dfw = Stats.Inferential.degreesOfFreedomWithinGroupsANOVA(numOfObservations, numOfGroups);
        final long dfb = Stats.Inferential.degreesOfFreedomBetweenGroupsANOVA(numOfGroups);
        final double msw = Stats.Descriptive.msw(ssw, dfw);
        final double fValueWithinGroups = Stats.Inferential.fValueWithinGroups(msb, msw);
        // then
        assertEquals(29.999999999999996, ssw, DELTA2);
        assertEquals(112.93333333333334, ssb, DELTA4);
        assertEquals(142.933, sst, DELTA3);
        assertEquals(56.467, msb, DELTA3);
        assertEquals(12, dfw, DELTA1);
        assertEquals(2, dfb, DELTA1);
        assertEquals(2.5, msw, DELTA1);
        assertEquals(22.587, fValueWithinGroups, DELTA3);
    }
}
