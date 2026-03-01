package org.example.sciencecalc.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;

    @Nested
    class Descriptive {
        @Test
        void testQuartiles() {
            // given
            final double[] dataset = new double[]{9, 12, 17, 19, 21, 21, 22, 22, 25, 27, 29, 30, 30, 32, 32, 36, 37,
                38, 40, 42, 44, 45, 48};
            // when
            final double[] result = Stats.Descriptive.quartiles(dataset);
            // then
            assertArrayEquals(new double[]{21.5, 30, 39}, result, DELTA1);
        }

        static List<Arguments> modeArgs() {
            return List.of(
                Arguments.of(new double[]{12, 15, 15, 17, 17, 22, 23, 23, 24, 26, 26, 26}, new double[]{26},
                    DELTA1), // 1 mode
                Arguments.of(new double[]{1, 1, 4, 6, 6, 7, 7, 8, 9}, new double[]{1, 6, 7}, DELTA1), // multimodal
                Arguments.of(new double[]{6, 2, 4, 4, 7, 6}, new double[]{4, 6}, DELTA1), // bimodal
                Arguments.of(new double[]{1, 2, 3, 4, 5}, new double[]{}, DELTA1) // no mode
            );
        }

        @ParameterizedTest
        @MethodSource("modeArgs")
        void testMode(double[] dataset, double[] expectedResult, double delta) {
            // when
            final double[] result = Stats.Descriptive.mode(dataset);
            // then
            assertArrayEquals(expectedResult, result, delta);
        }

        @Test
        void testRange() {
            // given
            final double[] dataset = new double[]{45, 789, 0.5, 0.0000005, 0, 25, 1};
            // when
            final double result = Stats.Descriptive.range(dataset);
            // then 789-0
            assertEquals(789, result, DELTA1);
        }

        @Test
        void testMidrange() {
            // given
            final double[] dataset = new double[]{5, 45, 789, 0.5, 0.0000005, 0, 25, 1, 12456};
            // when
            final double result = Stats.Descriptive.midrange(dataset);
            // then
            assertEquals(6228, result, DELTA1);
        }

        static List<Arguments> kthPercentileArgs() {
            return List.of(
                Arguments.of(new double[]{1.7, 1.8, 1.9, 2, 2, 2.1, 2.3, 2.4, 2.4}, 60, 2.1, DELTA1),
                Arguments.of(new double[]{0.9, 1, 1.2, 1.3, 1.3, 1.7, 1.7, 1.8, 1.9, 2, 2, 2.1, 2.3,
                    2.4, 2.4}, 60, 1.96, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("kthPercentileArgs")
        void testKthPercentile(double[] dataset, double percentile, double expectedResult, double delta) {
            // when
            final double result = Stats.Descriptive.kthPercentile(dataset, percentile);
            // then
            assertEquals(expectedResult, result, delta);
        }

        @Test
        void testIqr() {
            // given
            final double[] dataset = new double[]{5, 7, 8, 9};
            // when
            final double result = Stats.Descriptive.iqr(dataset);
            // then
            assertEquals(3.25, result, DELTA2);
        }

        @Test
        void testQuartileDeviation() {
            // given
            final double iqr = 64;
            // when
            final double deviation = Stats.Descriptive.quartileDeviation(iqr);
            // then
            assertEquals(32, deviation, DELTA1);
        }

        static List<Arguments> medianArgs() {
            return List.of(
                Arguments.of(new double[]{58, 47, 55, 6, 5, 14, 60, 3, 39, 6, 28, 15, 87, 31, 19}, 28,
                    DELTA1), // n = 15, an odd
                Arguments.of(new double[]{71, 71, 5, 18, 98, 23, 53, 92, 74, 82, 65, 74, 97, 75, 87, 13},
                    72.5, DELTA1) // n = 16, an even
            );
        }

        @ParameterizedTest
        @MethodSource("medianArgs")
        void testMedian(double[] dataset, double expectedResult, double delta) {
            // when
            final double result = Stats.Descriptive.median(dataset);
            // then
            assertEquals(expectedResult, result, delta);
        }

        @Test
        void testRelativeStd() {
            // given
            final byte mean = 120;
            final byte std = 30;
            // when
            final double rsd = Stats.Descriptive.relativeStd(mean, std);
            // then (30 g / 120 g) × 100% = 25%
            assertEquals(25, rsd, DELTA1);
        }

        @Test
        void testSumOfSquares() {
            // given
            final double[] dataset = new double[]{20, 22, 18};
            // when
            final double result = Stats.Descriptive.sumOfSquares(dataset);
            // then
            assertEquals(8, result, DELTA1);
        }

        @Test
        void testSampleVariance() {
            // given
            final double[] dataset = new double[]{5, 5, 5, 7, 8, 8, 9, 9};
            // when
            final double result = Stats.Descriptive.sampleVariance(dataset);
            // then
            assertEquals(3.143, result, DELTA3);
        }

        @Test
        void testPopulationVariance() {
            // given
            final double[] dataset = new double[]{5, 5, 5, 7, 8, 8, 9, 9};
            // when
            final double result = Stats.Descriptive.populationVariance(dataset);
            // then
            assertEquals(2.75, result, DELTA2);
        }

        @Test
        void testStdOfSampleVariance() {
            // given
            final double[] dataset = new double[]{2, 4, 5, 6, 6, 9, 10};
            // when
            final double result = Stats.Descriptive.stdOfSampleVariance(dataset);
            // then
            assertEquals(2.7689, result, DELTA4);
        }

        @Test
        void testStdOfPopulationVariance() {
            // given
            final double[] dataset = new double[]{2, 4, 5, 6, 6, 9, 10};
            // when
            final double result = Stats.Descriptive.stdOfPopulationVariance(dataset);
            // then
            assertEquals(2.5635, result, DELTA4);
        }

        @Test
        void testSkewness() {
            // given
            final double[] dataset = new double[]{140, 146, 146, 148, 152, 153, 154, 156, 156, 160, 162, 162, 163,
                165, 166, 166, 168, 172};
            // when
            final double result = Stats.Descriptive.skewness(dataset);
            // then
            assertEquals(-0.3212, result, DELTA4);
        }

        @Test
        void testKurtosis() {
            // given
            final double[] dataset = new double[]{140, 146, 146, 148, 152, 153, 154, 156, 156, 160, 162, 162, 163,
                165, 166, 166, 168, 172};
            // when
            final double result = Stats.Descriptive.kurtosis(dataset);
            // then
            assertEquals(-0.7194, result, DELTA4);
        }

        @Test
        void testPearsonCorrelation() {
            // given
            final double[] independentVariables = new double[]{1, 3, 3, 5};
            final double[] dependentVariables = new double[]{1, 2, 3, 4};
            // when
            final double result = Stats.Descriptive.pearsonCorrelation(independentVariables, dependentVariables);
            // then
            assertEquals(0.9487, result, DELTA4);
        }

        @Test
        void testMatthewsCorrelationCoefficient() {
            // given
            final byte truePositives = 10;
            final byte falsePositives = 5;
            final byte trueNegatives = 70;
            final byte falseNegatives = 15;
            // when
            final double correlationCoeff = Stats.Descriptive
                .matthewsCorrelationCoefficient(truePositives, falsePositives, trueNegatives, falseNegatives);
            // then
            assertEquals(0.4042, correlationCoeff, DELTA4);
        }

        @Test
        void testSpearmansRankCorrelation() {
            // given
            final double[] independentVariables = new double[]{1, 2, 3, 4, 5};
            final double[] dependentVariables = new double[]{0, 8, 3, 2, 4};
            // when
            final double correlationCoeff = Stats.Descriptive
                .spearmansRankCorrelation(independentVariables, dependentVariables);
            // then
            assertEquals(0.3, correlationCoeff, DELTA1);
        }

        @Test
        void testMseSampleMean() {
            // given
            final double[] dataset = new double[]{3, 15, 6, 3, 44, 8, 15, 9, 7, 25, 24, 5, 88, 44, 3, 21};
            // when
            final double mse = Stats.Descriptive.mseSampleMean(dataset);
            final double rmse = Stats.Descriptive.rmse(mse);
            // then
            assertEquals(474.38, mse, DELTA2);
            assertEquals(21.78, rmse, DELTA2);
        }

        @Test
        void testMse() {
            // given
            final double[] predictedValues = new double[]{3, 15, 6, 3, 44, 8, 15, 9, 7, 25, 24, 5, 88, 44, 3, 21};
            final double[] actualValues = new double[]{3.1, 14.9, 6, 3.14, 44.0015, 8, 12, 10.0321, 7, 22, 29, 3.9,
                88.5, 43.99, 3, 21};
            // when
            final double mse = Stats.Descriptive.mse(predictedValues, actualValues);
            final double rmse = Stats.Descriptive.rmse(mse);
            // then
            assertEquals(2.85, mse, DELTA2);
            assertEquals(1.69, rmse, DELTA2);
        }

        @Test
        void testSumOfSquaredErrorsWithSampleMean() {
            // given
            final double[] dataset = new double[]{3, 15, 6, 3, 44, 8, 15, 9, 7, 25, 24, 5, 88, 44, 3, 21};
            final double mean = Stats.Descriptive.mean(dataset);
            // when
            final double sse = Stats.Descriptive.sumOfSquaredErrors(dataset, mean);
            // then
            assertEquals(7590, sse, DELTA1);
        }

        @Test
        void testMad() {
            // given
            final double[] dataset = new double[]{3, 17, 9, 7, 13, 11};
            final double mean = Stats.Descriptive.mean(dataset);
            // when
            final double mad = Stats.Descriptive.mad(dataset, mean);
            // then
            assertEquals(3.6667, mad, DELTA4);
        }

        @Test
        void testStdError() {
            // given
            final double[] dataset = new double[]{5.5, 5.8, 6.1, 5.4, 5.5, 5.4, 5.9, 5.6, 5.9, 5.5};
            // when
            final double stdError = Stats.Descriptive.stdError(dataset);
            // then
            assertEquals(0.07775, stdError, DELTA5);
        }

        @Test
        void testRse() {
            // given
            final double sampleMean = 50;
            final double stdError = 5;
            // when
            final double rse = Stats.Descriptive.rse(sampleMean, stdError);
            // then
            assertEquals(10, rse, DELTA1);
        }

        @Test
        void testErrorPropagationViaAddition() {
            // given two rods
            final double rodLengthError = 0.03; // (X ± ΔX) as 2.00 ± 0.03 m
            final double rod2LengthError = 0.04; // (Y ± ΔY) is 0.88 ± 0.04 m
            // when
            final double lengthError = Stats.Descriptive.errorPropagationViaAddition(rodLengthError, rod2LengthError);
            // then
            assertEquals(0.05, lengthError, DELTA2);
        }

        @Test
        void testErrorPropagationViaMultiplication() {
            // given a flying bird
            final byte distance = 120; // (X ± ΔX) = 120 ± 3 m
            final byte time = 20; // (Y ± ΔY) = 20 ± 1.2 s
            final byte deltaX = 3;
            final double deltaY = 1.2;
            // when
            final double velocity = Stats.Descriptive.errorPropagationViaMultiplication(distance, time, deltaX, deltaY);
            // then
            assertEquals(156, velocity, DELTA1);
        }

        @Test
        void testSimpsonIndex() {
            // given
            final double[] population = new double[]{300, 335, 365};
            // when
            final double index = Stats.Descriptive.simpsonIndex(population);
            final double diversityIndex = Stats.Descriptive.simpsonDiversityIndex(index);
            final double reciprocalIndex = Stats.Descriptive.simpsonReciprocalIndex(index);
            // then
            assertEquals(0.33, index, DELTA2);
            assertEquals(0.67, diversityIndex, DELTA2);
            assertEquals(2.99, reciprocalIndex, DELTA2);
        }

        @Test
        void testSdi() {
            // given
            final byte laboratoryMean = 9;
            final byte consensusGroupMean = 8;
            final byte consensusGroupStd = 2;
            // when
            final double sdi = Stats.Descriptive.sdi(laboratoryMean, consensusGroupMean, consensusGroupStd);
            // then
            assertEquals(0.5, sdi, DELTA1);
        }

        @Test
        void testPercentileRank() {
            // given
            final double[] dataset = new double[]{6, 12, 24, 33, 23, 17, 30, 18, 27, 17, 25, 23, 27, 20, 13, 32, 26};
            final byte lowNumber = 25;
            // when
            final double rank = Stats.Descriptive.percentileRank(dataset, lowNumber);
            // then
            assertEquals(64.7, rank, DELTA1);
        }

        @Test
        void testGroupedDataStd() {
            // given Kcal[min, max], frequency
            final double[][] dataset = new double[][]{{100, 129, 5}, {130, 159, 4}, {160, 189, 12}, {190, 219, 6},
                {220, 249, 3}};
            // when
            final double std = Stats.Descriptive.groupedDataStd(dataset);
            // then
            assertEquals(36.0459, std, DELTA4);
        }
    }

    @Nested
    class Regression {
        @Test
        void testLinearRegression() {
            // given
            final double[] independentVariables = new double[]{1, 2, 3};
            final double[] dependentVariables = new double[]{3, 6, 6};
            // when
            final double[] regressionCoefficients = Stats.Regression
                .linearRegression(independentVariables, dependentVariables);
            // then
            assertArrayEquals(new double[]{2, 1.5}, regressionCoefficients, DELTA1);
        }
    }

    @Nested
    class Inferential {
        @Test
        void testZScore() {
            // given
            final byte experimentalResult = 62;
            final double mean = 58.75;
            final double standardDeviation = 7.854;
            // when
            final double zScore = Stats.Inferential.zScore(experimentalResult, mean, standardDeviation);
            // then
            assertEquals(0.4138, zScore, DELTA4);
        }
    }
}
