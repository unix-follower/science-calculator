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

class MathCalcTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA3 = 0.001;

    @Nested
    class Seq {
        static List<Arguments> geometricSequenceArgs() {
            return List.of(
                Arguments.of(2, 4, new double[]{2, 8, 32, 128, 512})
            );
        }

        @ParameterizedTest
        @MethodSource("geometricSequenceArgs")
        void testGeometricSequence(double firstTerm, double commonRatio, double[] expectedResult) {
            // given
            final byte limit = 5;
            // when
            final double[] geometricSequence = MathCalc.Seq.geometricSequence(firstTerm, commonRatio, limit);
            // then
            assertArrayEquals(expectedResult, geometricSequence);
        }

        static List<Arguments> geometricSequenceFiniteSumArgs() {
            return List.of(
                Arguments.of(new double[]{2, 8, 32, 128, 512}, 1, 3, 168),
                Arguments.of(new double[]{2, 8, 32, 128, 512}, 2, 3, 160),
                Arguments.of(new double[]{2, 8, 32, 128, 512}, 0, 4, 682)
            );
        }

        @ParameterizedTest
        @MethodSource("geometricSequenceFiniteSumArgs")
        void testGeometricSequenceFiniteSum(double[] sequence, int startIndex, int endIndex, double expectedResult) {
            // when
            final double finiteSum = MathCalc.Seq.geometricSequenceFiniteSum(sequence, startIndex, endIndex);
            // then
            assertEquals(expectedResult, finiteSum, DELTA1);
        }

        @Test
        void testGeometricSequenceCommonRatioForNonConsecutiveTerms() {
            // given
            final double mTermPosition = 3;
            final double mTerm = 32;
            final double nTermPosition = 5;
            final double nTerm = 512;
            // when
            final double commonRatio = MathCalc.Seq
                .geometricSequenceCommonRatioForNonConsecutiveTerms(mTermPosition, mTerm, nTermPosition, nTerm);
            // then
            assertEquals(4, commonRatio, DELTA1);
        }

        @Test
        void testGeometricSequenceCommonRatio() {
            // given
            final double previousTerm = 8;
            final double nTerm = 32;
            // when
            final double commonRatio = MathCalc.Seq.geometricSequenceCommonRatio(previousTerm, nTerm);
            // then
            assertEquals(4, commonRatio, DELTA1);
        }

        static List<Arguments> arithmeticSequenceArgs() {
            return List.of(
                Arguments.of(0, 8, new double[]{0, 8, 16, 24, 32})
            );
        }

        @ParameterizedTest
        @MethodSource("arithmeticSequenceArgs")
        void testArithmeticSequence(double firstTerm, double commonDiff, double[] expectedResult) {
            // given
            final byte limit = 5;
            // when
            final double[] arithmeticSequence = MathCalc.Seq.arithmeticSequence(firstTerm, commonDiff, limit);
            // then
            assertArrayEquals(expectedResult, arithmeticSequence);
        }

        @Test
        void testArithmeticSequenceNthTerm() {
            // given
            final double firstTerm = 0;
            final double commonDiff = 8;
            final byte nthTermPosition = 64;
            // when
            final double nthTerm = MathCalc.Seq.arithmeticSequenceNthTerm(firstTerm, commonDiff, nthTermPosition);
            // then
            assertEquals(504, nthTerm, DELTA1);
        }

        @Test
        void testArithmeticSequenceSumUpToNthTerm() {
            // given
            final double firstTerm = 0;
            final double commonDiff = 8;
            final byte nthTermPosition = 64;
            // when
            final double nthTerm = MathCalc.Seq
                .arithmeticSequenceSum(firstTerm, commonDiff, nthTermPosition);
            // then
            assertEquals(16128, nthTerm, DELTA1);
        }

        @Test
        void testArithmeticSequenceSum() {
            // given
            final double firstTerm = 0;
            final double commonDiff = 8;
            final byte firstTermPosition = 3;
            final byte nthTermPosition = 64;
            // when
            final double nthTerm = MathCalc.Seq
                .arithmeticSequenceSum(firstTerm, commonDiff, firstTermPosition, nthTermPosition);
            // then
            assertEquals(16120, nthTerm, DELTA1);
        }

        static List<Arguments> convolutionArgs() {
            return List.of(
                Arguments.of(new double[]{1, 2, 3}, new double[]{4, 5, 6}, new double[]{4, 13, 28, 27, 18}),
                Arguments.of(new double[]{1, 2, 3, 4, 5}, new double[]{4, 5, 6},
                    new double[]{4, 13, 28, 43, 58, 49, 30})
            );
        }

        @ParameterizedTest
        @MethodSource("convolutionArgs")
        void testConvolution(double[] sequence1, double[] sequence2, double[] expectedResult) {
            // when
            final double[] convolvedSequence = MathCalc.Seq.convolution(sequence1, sequence2);
            // then
            assertArrayEquals(expectedResult, convolvedSequence);
        }

        @Test
        void testPascalTriangle() {
            // given
            final byte rows = 10;
            final int[][] expectedMatrix = new int[][]{
                {1},
                {1, 1},
                {1, 2, 1},
                {1, 3, 3, 1},
                {1, 4, 6, 4, 1},
                {1, 5, 10, 10, 5, 1},
                {1, 6, 15, 20, 15, 6, 1},
                {1, 7, 21, 35, 35, 21, 7, 1},
                {1, 8, 28, 56, 70, 56, 28, 8, 1},
                {1, 9, 36, 84, 126, 126, 84, 36, 9, 1},
                {1, 10, 45, 120, 210, 252, 210, 120, 45, 10, 1},
            };
            // when
            final int[][] matrix = MathCalc.Seq.pascalTriangle(rows);
            // then
            assertArrayEquals(expectedMatrix, matrix);
        }

        @ParameterizedTest
        @CsvSource({
            "0,1",
            "1,2",
            "2,4",
            "3,8",
            "4,16",
            "5,32",
            "6,64",
            "7,128",
            "8,256",
            "9,512",
            "10,1024",
        })
        void testPascalTriangleRowSum(int rowNumber, long expectedResult) {
            // when
            final long rowSum = MathCalc.Seq.pascalTriangleRowSum(rowNumber);
            // then
            assertEquals(expectedResult, rowSum);
        }

        static List<Arguments> harmonicNumberArgs() {
            return List.of(
                Arguments.of(10, 2.929),
                Arguments.of(2.1, 1.5)
            );
        }

        @ParameterizedTest
        @MethodSource("harmonicNumberArgs")
        void testHarmonicNumber(double end, double expectedResult) {
            // when
            final double number = MathCalc.Seq.harmonicNumber(end);
            // then
            assertEquals(expectedResult, number, DELTA3);
        }

        @Test
        void testFibonacci() {
            // given
            final byte numberIdx = 11;
            // when
            final long nthTerm = MathCalc.Seq.fibonacci(numberIdx);
            // then
            assertEquals(89, nthTerm);
        }

        @Test
        void testFibonacciNumbers() {
            // given
            final byte numberIdx = 11;
            // when
            final long[] sequence = MathCalc.Seq.fibonacciNumbers(numberIdx);
            // then
            assertArrayEquals(new long[]{0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89}, sequence);
        }

        @Test
        void testFibonacciViaRecursion() {
            // given
            final byte numberIdx = 7;
            // when
            final long nthTerm = MathCalc.Seq.fibonacciViaRecursion(numberIdx);
            // then
            assertEquals(13, nthTerm);
        }

        @Test
        void testFibonacciViaGoldenRatio() {
            // given
            final byte numberIdx = 10;
            // when
            final long nthTerm = MathCalc.Seq.fibonacciViaGoldenRatio(numberIdx);
            // then
            assertEquals(55, nthTerm);
        }

        @Test
        void testFibonacciViaGoldenRatioWithInitialTerms() {
            // given
            final byte firstTerm = 1;
            final byte secondTerm = 2;
            final byte numberIdx = 10;
            // when
            final long nthTerm = MathCalc.Seq.fibonacciViaGoldenRatio(firstTerm, secondTerm, numberIdx);
            // then
            assertEquals(144, nthTerm);
        }

        @Test
        void testFibonacciForNegativeTerm() {
            // given
            final byte numberIdx = -8;
            // when
            final long nthTerm = MathCalc.Seq.fibonacciForNegativeTerm(numberIdx);
            // then
            assertEquals(-21, nthTerm);
        }
    }
}
