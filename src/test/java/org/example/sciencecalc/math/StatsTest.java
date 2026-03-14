package org.example.sciencecalc.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
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
    private static final double DELTA6 = 0.000001;
    private static final double DELTA7 = 0.0000001;

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

        @Test
        void testConstantOfProportionality() {
            // given
            final byte x = 10;
            final byte y = 20;
            // when
            final double constant = Stats.Descriptive.constantOfProportionality(x, y);
            // then
            assertEquals(2, constant, DELTA1);
        }

        @Test
        void testNormalDistribution() {
            // given
            final short mean = 251;
            final byte standardDeviation = 5;
            final short rawScoreValue = 253;
            // when
            final double probability = Stats.Descriptive.normalDistribution(mean, standardDeviation, rawScoreValue);
            final double probLower = Stats.Descriptive.normalDistributionXLt(mean, standardDeviation, rawScoreValue);
            final double probGreater = Stats.Descriptive.normalDistributionXGt(mean, standardDeviation, rawScoreValue);
            // then
            assertEquals(0.0737, probability, DELTA4);
            assertEquals(0.655422, probLower, DELTA6);
            assertEquals(0.344578, probGreater, DELTA6);
        }

        @Test
        void testBinomialDistribution() {
            // given
            final byte numOfEvents = 5;
            final byte numOfSuccesses = 3;
            final double probabilityOfSuccessPerEvent = 0.667;
            // when
            final double probability = Stats.Descriptive
                .binomialDistribution(numOfEvents, numOfSuccesses, probabilityOfSuccessPerEvent);
            final double meanNumberOfSuccesses = Stats.Descriptive
                .binomialDistributionMean(numOfEvents, probabilityOfSuccessPerEvent);
            final double variance = Stats.Descriptive
                .binomialDistributionVariance(numOfEvents, probabilityOfSuccessPerEvent);
            // then
            assertEquals(0.32905, probability, DELTA5); // 32.905%
            assertEquals(3.335, meanNumberOfSuccesses, DELTA3);
            assertEquals(1.1106, variance, DELTA4);
        }

        @Test
        void testNegativeBinomialDistribution() {
            // given
            final byte numOfEvents = 25;
            final byte numOfSuccesses = 15;
            final double probabilityOfOneSuccess = 0.4;
            // when
            final double probability = Stats.Descriptive
                .negativeBinomialDistribution(numOfEvents, numOfSuccesses, probabilityOfOneSuccess);
            // then
            assertEquals(0.012733, probability, DELTA6);
        }

        @Test
        void testGeometricDistribution() {
            // given
            final byte numOfFailures = 1;
            final double probabilityOfSuccess = 1. / 6;
            // when
            final double probability = Stats.Descriptive.geometricDistribution(numOfFailures, probabilityOfSuccess);
            final double mean = Stats.Descriptive.geometricDistributionMean(probabilityOfSuccess);
            final double variance = Stats.Descriptive.geometricDistributionVariance(probabilityOfSuccess);
            // then
            assertEquals(0.138889, probability, DELTA6);
            assertEquals(5, mean, DELTA1);
            assertEquals(30, variance, DELTA1);
        }

        @Test
        void testChiSquare() {
            // given
            final byte observedValue = 5;
            final byte expectedValue = 9;
            // when
            final double result = Stats.Descriptive.chiSquare(observedValue, expectedValue);
            // then
            assertEquals(1.7778, result, DELTA4);
        }

        @Test
        void testExponentialDistribution() {
            // given
            final double rateParameter = 0.25; // 1/4
            final byte timeBetweenEvents = 3;
            // when
            final double higher = Stats.Descriptive.exponentialDistributionHigher(rateParameter, timeBetweenEvents);
            final double lower = Stats.Descriptive.exponentialDistributionLower(rateParameter, timeBetweenEvents);
            // then
            assertEquals(0.472367, higher, DELTA6);
            assertEquals(0.527633, lower, DELTA6);
            assertEquals(4, Arithmetic.reciprocal(rateParameter), DELTA1); // mean
            assertEquals(2.77259, Algebra.ln(2) / rateParameter, DELTA5); // median
            assertEquals(16, Arithmetic.reciprocal(rateParameter * rateParameter), DELTA1); // variance
        }

        static List<Arguments> poissonDistributionArgs() {
            return List.of(
                Arguments.of(1, 5, 0.0337, DELTA4), // Probability P(X = x)
                Arguments.of(0, 5, 0.0067, DELTA4) // Cumulative prob. P(X < x)
            );
        }

        @ParameterizedTest
        @MethodSource("poissonDistributionArgs")
        void testPoissonDistribution(long numOfOccurrences, long successRate, double expectedResult, double delta) {
            // when
            final double probability = Stats.Descriptive.poissonDistribution(numOfOccurrences, successRate);
            // then
            assertEquals(expectedResult, probability, delta);
        }

        @Test
        void testPoissonDistributionCumulativeProbabilityAtMostEqToX() {
            // given
            final double probabilityLtX = 0.00674;
            final double probabilityEqX = 0.03371;
            // when
            final double cumulativeProbability = Stats.Descriptive
                .poissonDistributionCumulativeProbabilityAtMostEqToX(probabilityLtX, probabilityEqX);
            // then
            assertEquals(0.0404, cumulativeProbability, DELTA4);
        }

        @Test
        void testPoissonDistributionCumulativeProbabilityGtX() {
            // given
            final double cumulativeProbabilityAtMostEqToX = 0.0404;
            // when
            final double probability = Stats.Descriptive
                .poissonDistributionCumulativeProbabilityGtX(cumulativeProbabilityAtMostEqToX);
            // then
            assertEquals(0.9596, probability, DELTA4);
        }

        @Test
        void testPoissonDistributionCumulativeProbabilityGtEqX() {
            // given
            final double probabilityLtX = 0.00674;
            // when
            final double probability = Stats.Descriptive.poissonDistributionCumulativeProbabilityGtEqX(probabilityLtX);
            // then
            assertEquals(0.9933, probability, DELTA4);
        }

        @Test
        void testTStatistic() {
            // given
            final byte sampleMean = 15;
            final byte populationMean = 10;
            final byte sampleSize = 36;
            final byte sampleStd = 6;
            // when
            final double tStatistic = Stats.Descriptive.tStatistic(sampleMean, populationMean, sampleSize, sampleStd);
            // then
            assertEquals(5, tStatistic, DELTA1);
        }

        @Test
        void testCentralLimitTheorem() {
            // given
            final byte populationStd = 35;
            final byte sampleSize = 49;
            // when
            final double sampleStd = Stats.Descriptive.centralLimitTheorem(populationStd, sampleSize);
            // then
            assertEquals(5, sampleStd, DELTA1);
        }

        @Test
        void testUniformDistributionXLtOrEq() {
            // given
            final double minOutcome = 0.2;
            final double outcome = 0.5;
            final byte maxOutcome = 1;
            // when
            final double probability = Stats.Descriptive.uniformDistributionXLtOrEq(minOutcome, outcome, maxOutcome);
            // then
            assertEquals(0.375, probability, DELTA3);
        }

        @Test
        void testUniformDistributionXGtOrEq() {
            // given
            final double minOutcome = 0.2;
            final double outcome = 0.5;
            final byte maxOutcome = 1;
            // when
            final double probability = Stats.Descriptive.uniformDistributionXGtOrEq(minOutcome, outcome, maxOutcome);
            // then
            assertEquals(0.625, probability, DELTA3);
        }

        @Test
        void testUniformDistributionXWithinInterval() {
            // given
            final double minOutcome = 0.2;
            final double lowerBound = 0.5;
            final double upperBound = 0.7;
            final byte maxOutcome = 1;
            // when
            final double probability = Stats.Descriptive
                .uniformDistributionXWithinInterval(minOutcome, lowerBound, upperBound, maxOutcome);
            // then
            assertEquals(0.25, probability, DELTA2);
        }

        @Test
        void testUniformDistributionPDF() {
            // given
            final double minOutcome = 0.2;
            final byte maxOutcome = 1;
            // when
            final double probability = Stats.Descriptive.uniformDistributionPDF(minOutcome, maxOutcome);
            // then
            assertEquals(1.25, probability, DELTA2);
        }

        @Test
        void testUniformDistributionQuantileFunction() {
            // given
            final double minOutcome = 0.2;
            final byte maxOutcome = 1;
            final double quantile = 0.15;
            // when
            final double result = Stats.Descriptive
                .uniformDistributionQuantileFunction(minOutcome, maxOutcome, quantile);
            // then
            assertEquals(0.32, result, DELTA2);
        }

        @Test
        void testLognormalDistributionXLtOrEq() {
            // given
            final double mean = 0.3;
            final double std = 0.7;
            final double outcome = 0.5;
            // when
            final double probability = Stats.Descriptive.lognormalDistributionXLtOrEq(mean, std, outcome);
            // then
            assertEquals(0.07798, probability, DELTA5);
        }

        @Test
        void testLognormalDistributionXGtOrEq() {
            // given
            final double mean = 0.3;
            final double std = 0.7;
            final double outcome = 0.5;
            // when
            final double probability = Stats.Descriptive.lognormalDistributionXGtOrEq(mean, std, outcome);
            // then
            assertEquals(0.922, probability, DELTA3);
        }

        @Test
        void testLognormalDistributionPDF() {
            // given
            final double mean = 0.3;
            final double std = 0.7;
            final double outcome = 0.5;
            // when
            final double probability = Stats.Descriptive.lognormalDistributionPDF(mean, std, outcome);
            // then
            assertEquals(0.4166173, probability, DELTA7);
        }

        @Test
        void testLognormalDistributionQuantileFunction() {
            // given
            final double mean = 0.3;
            final double std = 0.7;
            final double quantile = 0.4;
            // when
            final double result = Stats.Descriptive
                .lognormalDistributionQuantileFunction(mean, std, quantile);
            // then
            assertEquals(1.1304919934222937, result, DELTA7);
        }

        @ParameterizedTest
        @CsvSource({
            "104.9018,0.01913", // Daily 1.913%
            "14.98596,0.1418", // Weekly 14.18%
            "3.446464,0.7801", // Monthly 78%
            "0.2872053,1011.9627" // Annual 101 196%
        })
        void testExponentialGrowthPrediction(double time, double expectedGrowthRate) {
            // given
            final int initialValue = 137_018;
            final int finalValue = 1_000_000;
            // when
            final double growthRate = Stats.Descriptive.exponentialGrowthPrediction(time, initialValue, finalValue);
            // then
            assertEquals(expectedGrowthRate, growthRate, DELTA4);
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

        @Test
        void testCoefficientOfDetermination() {
            // given
            final double[] independentVariables = new double[]{0, 2, 4};
            final double[] dependentVariables = new double[]{1, 4, 4};
            // when
            final double coeff = Stats.Inferential.coefficientOfDetermination(independentVariables, dependentVariables);
            // then
            assertEquals(0.75, coeff, DELTA2);
        }

        @Test
        void testExponentialRegression() {
            // given
            final double[] independentVariables = new double[]{1, 2, 3, 4, 5};
            final double[] dependentVariables = new double[]{1, 3, 9, 27, 81};
            // when
            final double[] fittedCoeffs = Stats.Inferential
                .exponentialRegression(independentVariables, dependentVariables);
            // then
            assertArrayEquals(new double[]{0.3333, 3}, fittedCoeffs, DELTA4);
        }

        @Test
        void testCubicRegression() {
            // given
            final double[] independentVariables = new double[]{0, 2, 3, 4, 5};
            final double[] dependentVariables = new double[]{1, 0, 3, 5, 4};
            // when
            final double[] fittedCoeffs = Stats.Inferential
                .cubicRegression(independentVariables, dependentVariables);
            // then y = 0.9973 - 5.0755x + 3.0687x² - 0.3868x³
            assertArrayEquals(new double[]{0.9973, -5.0755, 3.0687, -0.3868}, fittedCoeffs, DELTA4);
        }

        @Test
        void testPolynomialRegression() {
            // given
            final double[] independentVariables = new double[]{0, 2, 3, 4, 5, 6};
            final double[] dependentVariables = new double[]{1, 0, 3, 5, 4, 2};
            // when
            final double[] fittedCoeffs = Stats.Inferential
                .polynomialRegression(independentVariables, dependentVariables);
            // then y = 0.0747x⁴ - 1.1808x³ + 5.7366x² - 7.8798x + 1.004
            assertArrayEquals(new double[]{1.004, -7.8798, 5.7366, -1.1808, 0.0747}, fittedCoeffs, DELTA4);
        }

        @Test
        void testDegreesOfFreedom1Sample() {
            // given dataset: 15, 46, 67, 23, 45. N=5
            final byte sampleSize = 5;
            // when
            final int df = Stats.Inferential.degreesOfFreedom1Sample(sampleSize);
            // then
            assertEquals(4, df);
        }

        @Test
        void testDegreesOfFreedom2SampleWithEqVariances() {
            // given
            // N1: 1, 7, 5, 12, 17. N1=5
            // N2: 14, 15, 21, 29. N2=4
            final byte sampleSize1 = 5;
            final byte sampleSize2 = 4;
            // when
            final int df = Stats.Inferential.degreesOfFreedom2SampleWithEqVariances(sampleSize1, sampleSize2);
            // then
            assertEquals(7, df);
        }

        @Test
        void testDegreesOfFreedom2SampleWithUneqVariances() {
            // given
            final byte sampleSize1 = 100;
            final byte variance1 = 8;
            final byte sampleSize2 = 50;
            final byte variance2 = 5;
            // when
            final double df = Stats.Inferential
                .degreesOfFreedom2SampleWithUneqVariances(sampleSize1, variance1, sampleSize2, variance2);
            // then
            assertEquals(120.57, df, DELTA2);
        }

        @Test
        void testDegreesOfFreedomChiSquare() {
            // given
            final short rows = 1000;
            final byte columns = 4;
            // when
            final long df = Stats.Inferential.degreesOfFreedomChiSquare(rows, columns);
            // then
            assertEquals(2997, df);
        }

        @Test
        void testDegreesOfFreedomANOVA() {
            // given
            final byte sampleSize = 100;
            final byte numOfGroups = 10;
            // when
            final long dfBetweenGroups = Stats.Inferential.degreesOfFreedomBetweenGroupsANOVA(numOfGroups);
            final long dfWithinGroups = Stats.Inferential.degreesOfFreedomWithinGroupsANOVA(sampleSize, numOfGroups);
            final long totalDf = Stats.Inferential.totalDegreesOfFreedomANOVA(sampleSize);
            // then
            assertEquals(9, dfBetweenGroups);
            assertEquals(90, dfWithinGroups);
            assertEquals(99, totalDf);
        }

        @Test
        void testFStatistic() {
            // given
            final byte sampleVariance = 10;
            final byte sampleVariance2 = 5;
            // when
            final double fStatistic = Stats.Inferential.fStatistic(sampleVariance, sampleVariance2);
            // then
            assertEquals(2, fStatistic, DELTA1);
        }

        @Test
        void testFStatisticMultipleRegression() {
            // given
            final byte sumSquareOfResidualsFullModel = 12;
            final byte sumSquareOfResidualsRestricted = 24;
            final byte numOfExcludedCoeffs = 2;
            final byte numOfCoeffs = 3;
            final byte sampleSize = 37;
            // when
            final double fStatistic = Stats.Inferential.fStatistic(sumSquareOfResidualsFullModel,
                sumSquareOfResidualsRestricted, numOfExcludedCoeffs, numOfCoeffs, sampleSize);
            // then
            assertEquals(17, fStatistic, DELTA1);
        }

        @Test
        void testSampleProportionError() {
            // given
            final double zScore = 1.96; // confidence level = 95%
            final byte sampleSize = 30;
            final double sampleProportion = 0.8; // p̂ = 400/500
            // when
            final double error = Stats.Inferential.sampleProportionError(zScore, sampleSize, sampleProportion);
            // then ±14.314%
            assertEquals(0.14314, error, DELTA5);
        }

        @Test
        void testSampleMeanErrorGivenStd() {
            // given
            final byte sampleSize = 30;
            final byte sampleStd = 70;
            // Degrees of freedom (d) = 29 (row).
            // a = 1 - 95% confidence level = 1 - 0.95 = 0.05
            // α/2 = 0.05/2 = 0.025
            // Column: 0.025 (for one-tail) or 0.005 (for two-tails).
            final double criticalTValue = 2.04523; // The intersection of row 29 and column 0.025
            // when
            final double error = Stats.Inferential.sampleMeanErrorGivenStd(criticalTValue, sampleSize, sampleStd);
            // then ±26.1384
            assertEquals(26.1384, error, DELTA4);
        }

        @Test
        void testSampleMeanErrorGivenPopulationStd() {
            // given
            final double zScore = 1.96; // confidence level = 95%
            final byte sampleSize = 30;
            final byte populationStd = 70;
            // when
            final double error = Stats.Inferential.sampleMeanErrorGivenPopulationStd(zScore, sampleSize, populationStd);
            // then ±26.1384
            assertEquals(25.05, error, DELTA2);
        }
    }
}
