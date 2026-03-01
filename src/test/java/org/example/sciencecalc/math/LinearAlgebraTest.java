package org.example.sciencecalc.math;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinearAlgebraTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;

    @Test
    void testVectorMagnitude2d() {
        // given
        final int x = -3;
        final int y = 8;
        final double[] vector = {x, y};
        // when
        final double magnitude = LinearAlgebra.vectorMagnitude(vector);
        // then
        assertEquals(8.544, magnitude, DELTA3);
    }

    @Test
    void testVectorMagnitude3d() {
        // given
        final int x = -3;
        final int y = 8;
        final int z = 2;
        final double[] vector = {x, y, z};
        // when
        final double magnitude = LinearAlgebra.vectorMagnitude(vector);
        // then
        assertEquals(8.775, magnitude, DELTA3);
    }

    @Test
    void testVectorMagnitude5d() {
        // given
        final int x = 3;
        final int y = 1;
        final int z = 2;
        final int t = -3;
        final int w = 4;
        final double[] vector = {x, y, z, t, w};
        // when
        final double magnitude = LinearAlgebra.vectorMagnitude(vector);
        // then
        assertEquals(6.245, magnitude, DELTA3);
    }

    @Test
    void testUnitVector() {
        // given
        final int x = 8;
        final int y = -3;
        final int z = 5;
        final double[] vector = {x, y, z};
        // when
        final var unitVectorResultData = LinearAlgebra.unitVector(vector);
        // then
        final double vectorMagnitude = unitVectorResultData.getLeft();
        final double[] unitVectorResult = unitVectorResultData.getMiddle();
        final double unitVectorResultMagnitude = unitVectorResultData.getRight();
        assertEquals(9.9, vectorMagnitude, DELTA3);

        assertNotNull(unitVectorResult);
        assertEquals(3, unitVectorResult.length);
        assertEquals(0.80812, unitVectorResult[Constants.X_INDEX], DELTA5);
        assertEquals(-0.303046, unitVectorResult[Constants.Y_INDEX], DELTA6);
        assertEquals(0.50508, unitVectorResult[Constants.Z_INDEX], DELTA5);

        assertEquals(1, unitVectorResultMagnitude, DELTA1);
    }

    @Test
    void testVectorProjection2d() {
        // given
        final double[] vectorA = {3, 4};
        final double[] vectorB = {2, 6};
        // when
        final var projectionResult = LinearAlgebra.vectorProjection(vectorA, vectorB);
        // then
        assertNotNull(projectionResult);

        final double[] projection = projectionResult.getLeft();
        assertNotNull(projection);
        assertEquals(2, projection.length);
        assertEquals(1.5, projection[Constants.X_INDEX], DELTA1);
        assertEquals(4.5, projection[Constants.Y_INDEX], DELTA1);

        final double projectionFactor = projectionResult.getRight();
        assertEquals(0.75, projectionFactor, DELTA1);
    }

    @Test
    void testVectorProjection3d() {
        // given
        final double[] vectorA = {2, -3, 5};
        final double[] vectorB = {3, 6, -4};
        // when
        final var projectionResult = LinearAlgebra.vectorProjection(vectorA, vectorB);
        // then
        assertNotNull(projectionResult);

        final double[] projection = projectionResult.getLeft();
        assertNotNull(projection);
        assertEquals(3, projection.length);
        assertEquals(-1.5738, projection[Constants.X_INDEX], DELTA4);
        assertEquals(-3.1475, projection[Constants.Y_INDEX], DELTA4);
        assertEquals(2.0984, projection[Constants.Z_INDEX], DELTA4);

        final double projectionFactor = projectionResult.getRight();
        assertEquals(-0.5246, projectionFactor, DELTA4);
    }

    @Test
    void testFindMissingUnitVectorComponent() {
        // given
        final double x = 0.80812;
        final double y = -0.303046;
        final double[] unitVector = {x, y};
        // when
        final var unitVectorResultData = LinearAlgebra
            .findMissingUnitVectorComponent(unitVector);
        // then
        final double[] unitVectorResult = unitVectorResultData.getLeft();
        final double magnitude = unitVectorResultData.getRight();
        assertEquals(1, magnitude, DELTA1);

        assertNotNull(unitVectorResult);
        assertEquals(3, unitVectorResult.length);
        assertEquals(x, unitVectorResult[Constants.X_INDEX], DELTA5);
        assertEquals(y, unitVectorResult[Constants.Y_INDEX], DELTA6);
        assertEquals(0.50508, unitVectorResult[Constants.Z_INDEX], DELTA5);
    }

    @Test
    void testCrossProduct() {
        // given
        final double[] vectorA = {2, 3, 7};
        final double[] vectorB = {1, 2, 4};
        // when
        final double[] resultVector = LinearAlgebra.crossProduct(vectorA, vectorB);
        // then
        assertNotNull(resultVector);
        assertEquals(3, resultVector.length);
        assertEquals(-2, resultVector[Constants.X_INDEX], DELTA1);
        assertEquals(-1, resultVector[Constants.Y_INDEX], DELTA1);
        assertEquals(1, resultVector[Constants.Z_INDEX], DELTA1);
    }

    @Test
    void testUnsupportedDimsForCrossProduct() {
        // given
        final double[] vectorA = {2, 3};
        final double[] vectorB = {1, 2};
        // when
        final var exception = assertThrows(IllegalArgumentException.class,
            () -> LinearAlgebra.crossProduct(vectorA, vectorB));
        // then
        assertEquals("The cross product can only be applied to 3D vectors", exception.getMessage());
    }

    @Test
    void testDotProduct2d() {
        // given
        final double[] vectorA = {5, 4};
        final double[] vectorB = {2, 3};
        // when
        final double result = LinearAlgebra.dotProduct(vectorA, vectorB);
        // then
        assertEquals(22, result, DELTA1);
    }

    @Test
    void testDotProduct3d() {
        // given
        final double[] vectorA = {4, 5, -3};
        final double[] vectorB = {1, -2, -2};
        // when
        final double result = LinearAlgebra.dotProduct(vectorA, vectorB);
        // then
        assertEquals(0, result, DELTA1);
    }

    @Test
    void testDotProductOfMatrix() {
        // given
        final double[][] matrixA = {
            {4, 5, -3},
            {2, -1, 4}
        };
        final double[][] matrixB = {
            {1, -2, -2},
            {5, 1, 3}
        };
        // when
        final double result = LinearAlgebra.dotProduct(matrixA, matrixB);
        // then
        // 4*1 + 5*-2 + -3*-2 = [4, -10, 6] = 0
        // 2*5 + -1*1 + 4*3   = [10, -1, 12] = 21
        assertEquals(21, result, DELTA1);
    }

    @Test
    void testDotProduct2dAndAngleBetween() {
        // given
        final double[] vectorA = {5, 4};
        final double[] vectorB = {2, 3};
        // when
        final double[] resultData = LinearAlgebra.dotProductAndAngleBetween(vectorA, vectorB);
        // then
        assertNotNull(resultData);
        assertEquals(4, resultData.length);
        final double dot = resultData[Constants.ARR_1ST_INDEX];
        final double magnitudeA = resultData[Constants.ARR_2ND_INDEX];
        final double magnitudeB = resultData[Constants.ARR_3RD_INDEX];
        final double angleRadians = resultData[Constants.ARR_4TH_INDEX];
        assertEquals(22, dot, DELTA1);
        assertEquals(6.403, magnitudeA, DELTA3);
        assertEquals(3.6056, magnitudeB, DELTA4);
        assertEquals(0.30814, angleRadians, DELTA5);
    }

    @Test
    void testDotProduct3dAndAngleBetween() {
        // given
        final double[] vectorA = {4, 5, 3};
        final double[] vectorB = {1, -2, -2};
        // when
        final double[] resultData = LinearAlgebra.dotProductAndAngleBetween(vectorA, vectorB);
        // then
        assertNotNull(resultData);
        assertEquals(4, resultData.length);
        final double dot = resultData[Constants.ARR_1ST_INDEX];
        final double magnitudeA = resultData[Constants.ARR_2ND_INDEX];
        final double magnitudeB = resultData[Constants.ARR_3RD_INDEX];
        final double angleRadians = resultData[Constants.ARR_4TH_INDEX];
        assertEquals(-12, dot, DELTA1);
        assertEquals(7.071, magnitudeA, DELTA3);
        assertEquals(3, magnitudeB, DELTA4);
        assertEquals(2.172, angleRadians, DELTA3);
    }

    static List<Arguments> vectorAddWithMultiplesArgs() {
        return List.of(
            Arguments.of(new double[]{-3, 2, 8}, 1,
                new double[]{2, 2, -4}, 3, new double[]{3, 8, -4}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("vectorAddWithMultiplesArgs")
    void testVectorAddWithMultiples(double[] vectorA, double multipleAlpha,
                                    double[] vectorB, double multipleBeta, double[] expectedResult, double delta) {
        // when
        final double[] result = LinearAlgebra
            .vectorAddWithMultiples(vectorA, multipleAlpha, vectorB, multipleBeta);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> vectorSubtractWithMultiplesArgs() {
        return List.of(
            Arguments.of(new double[]{-3, 2, 8}, 1,
                new double[]{2, 2, -4}, 3, new double[]{-9, -4, 20}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("vectorSubtractWithMultiplesArgs")
    void testVectorSubtractWithMultiples(
        double[] vectorA, double multipleAlpha, double[] vectorB, double multipleBeta,
        double[] expectedResult, double delta) {
        // when
        final double[] result = LinearAlgebra
            .vectorSubtractWithMultiples(vectorA, multipleAlpha, vectorB, multipleBeta);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> determinantArgs() {
        return List.of(
            // 2x2
            Arguments.of(new double[][]{
                {2, 5},
                {4, 1},
            }, -18, DELTA1),
            // 3x3
            Arguments.of(new double[][]{
                {2, 5, 1},
                {4, 1, 7},
                {6, 8, 3},
            }, 70, DELTA1),
            // 4x4
            Arguments.of(new double[][]{
                {2, 5, 1, 3}, // a₁ - a₄
                {4, 1, 7, 9}, // b₁ - b₄
                {6, 8, 3, 2}, // c₁ - c₄
                {7, 8, 1, 4}, // d₁ - d₄
            }, 630, DELTA1),
            Arguments.of(new double[][]{
                // col1 + (-2) * col3
                {0, 5, 1, 3},
                {-10, 1, 7, 9},
                {0, 8, 3, 2},
                {5, 8, 1, 4},
            }, 630, DELTA1),
            // 5x5
            Arguments.of(new double[][]{
                {2, 5, 1, 3, 1},
                {4, 1, 7, 9, 2},
                {6, 8, 3, 2, 3},
                {7, 8, 1, 4, 4},
                {1, 2, 3, 4, 5},
            }, 2940, DELTA1),
            // 6x6
            Arguments.of(new double[][]{
                {2, 5, 1, 3, 1},
                {4, 1, 7, 9, 2},
                {6, 8, 3, 2, 3},
                {7, 8, 1, 4, 4},
                {1, 2, 3, 4, 5},
            }, 2940, DELTA1),
            // 10x10
            Arguments.of(new double[][]{
                {2, 5, 1, 3, 1, 2, 3, 4, 5, 6},
                {4, 1, 7, 9, 2, 3, 4, 5, 6, 7},
                {6, 8, 3, 25, 3, 4, 15, 6, 7, 8},
                {7, 8, 1, 4, 4, -5, 6, 7, 8, 9},
                {1, 2, 33, 4, 5, 61, 7, 8, 9, 10},
                {24, 50, 42, 51, 36, 77, 8, 9, 10, 11},
                {3, 4, 5, -6, 7, 81, 99, 10, 11, 12},
                {4, 3, -6, 7, 8, 9, 10, 11, 12, 13},
                {5, 2, 7, 8, 9, 11, 11, 12, 13, 14},
                {6, 1, 8, 9, 10, 11, 12, 13, 14, 25}
            }, -51_561_264_000d, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("determinantArgs")
    void testDeterminant(double[][] matrix, double expectedResult, double delta) {
        // when
        final double result = LinearAlgebra.determinant(matrix);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> eigenvaluesEigenvectorsOf2x2Args() {
        return List.of(
            // 2x2
            Arguments.of(new double[][]{
                {2, 5},
                {4, 1},
            }, Pair.of(new double[]{6, -3}, new double[][]{{1, Arithmetic.FOUR_FIFTH}, {1, -1}}), DELTA2)
        );
    }

    @ParameterizedTest
    @MethodSource("eigenvaluesEigenvectorsOf2x2Args")
    void testEigenvaluesEigenvectorsOf2x2(
        double[][] matrix, Pair<double[], double[][]> expectedResult, double delta) {
        // when
        final var result = LinearAlgebra.eigenvaluesEigenvectorsOf2x2(matrix);
        // then
        assertNotNull(result);
        assertArrayEquals(expectedResult.getLeft(), result.getLeft(), delta);
        final double[][] expectedEigenVectors = expectedResult.getRight();
        final double[][] eigenVectors = result.getRight();
        assertMatrixEquals(expectedEigenVectors, eigenVectors, delta);
    }

    static List<Arguments> eigenvaluesEigenvectorsArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{
                {2, 5, 6},
                {3, 7, 8},
                {4, 8, 11},
            }, Pair.of(new double[]{-0.21047, 0.73175, 19.47871},
                new double[][]{{0.57, 0.78, 1}, {-3.49, 0.34, 1}, {-0.34, -1.11, 1}}), DELTA5),
            Arguments.of(new double[][]{
                {1, 2, 1},
                {6, -1, 0},
                {-1, -2, -1},
            }, Pair.of(new double[]{-4, 0, 3}, new double[][]{{-1, 2, 1}, {-1, -6, 13}, {-2, -3, 2}}), DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("eigenvaluesEigenvectorsArgs")
    void testEigenvaluesEigenvectors(
        double[][] matrix, Pair<double[], double[][]> expectedResult, double delta) {
        // when
        final var result = LinearAlgebra.eigenvaluesEigenvectors(matrix);
        // then
        assertNotNull(result);
        final double[] expectedEigenvalues = expectedResult.getLeft();
        assertArrayEquals(expectedEigenvalues, result.getLeft(), delta);

        final double[][] eigenvectors = result.getRight();
        for (int i = 0; i < expectedEigenvalues.length; i++) {
            final double[] eigenvector = eigenvectors[i];
            final double[] scaled = LinearAlgebra.scaleEigenvector(expectedEigenvalues[i], eigenvector);
            final double[] matrixVectorProd = LinearAlgebra.multiplyMatrixVector(matrix, eigenvector);
            assertArrayEquals(matrixVectorProd, scaled, delta);
        }
    }

    static List<Arguments> matrixTraceArgs() {
        return List.of(
            // 2x2
            Arguments.of(new double[][]{{1, 2}, {3, 4}}, 5, DELTA1),
            // 3x3
            Arguments.of(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 15, DELTA1),
            // 4x4
            Arguments.of(new double[][]{
                {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 34, DELTA1),
            // 5x5
            Arguments.of(new double[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
            }, 65, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixTraceArgs")
    void testMatrixTrace(double[][] matrix, double expectedResult, double delta) {
        // when
        final double result = LinearAlgebra.matrixTrace(matrix);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixMultiplyScalarArgs() {
        return List.of(
            // 2x2
            Arguments.of(new double[][]{{1, 2}, {3, 4}}, 0, new double[][]{{0, 0}, {0, 0}}, DELTA1),
            Arguments.of(new double[][]{{1, 2}, {3, 4}}, 5, new double[][]{{5, 10}, {15, 20}}, DELTA1),
            // 3x3
            Arguments.of(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 2,
                new double[][]{{2, 4, 6}, {8, 10, 12}, {14, 16, 18}}, DELTA1),
            // 4x4
            Arguments.of(new double[][]{
                {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 2, new double[][]{
                {2, 4, 6, 8}, {10, 12, 14, 16}, {18, 20, 22, 24}, {26, 28, 30, 32}}, DELTA1),
            // 5x5
            Arguments.of(new double[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
            }, 2, new double[][]{
                {2, 4, 6, 8, 10},
                {12, 14, 16, 18, 20},
                {22, 24, 26, 28, 30},
                {32, 34, 36, 38, 40},
                {42, 44, 46, 48, 50}
            }, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixMultiplyScalarArgs")
    void testMatrixMultiplyScalar(double[][] matrix, double multiplier, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixMultiplyScalar(matrix, multiplier);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    private static void assertMatrixEquals(double[][] expectedResult, double[][] result, double delta) {
        assertNotNull(result);
        assertEquals(expectedResult.length, result.length);
        for (int i = 0; i < expectedResult.length; i++) {
            assertArrayEquals(expectedResult[i], result[i], delta);
        }
    }

    static List<Arguments> matrixDivideScalarArgs() {
        return List.of(
            // 2x2
            Arguments.of(new double[][]{{5, 10}, {15, 20}}, 5, new double[][]{{1, 2}, {3, 4}}, DELTA1),
            // 3x3
            Arguments.of(new double[][]{{2, 4, 6}, {8, 10, 12}, {14, 16, 18}}, 2,
                new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, DELTA1),
            // 4x4
            Arguments.of(new double[][]{
                {2, 4, 6, 8}, {10, 12, 14, 16}, {18, 20, 22, 24}, {26, 28, 30, 32}}, 2, new double[][]{
                {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, DELTA1),
            // 5x5
            Arguments.of(new double[][]{
                {2, 4, 6, 8, 10},
                {12, 14, 16, 18, 20},
                {22, 24, 26, 28, 30},
                {32, 34, 36, 38, 40},
                {42, 44, 46, 48, 50}
            }, 2, new double[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
            }, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixDivideScalarArgs")
    void testMatrixDivideScalar(double[][] matrix, double divisor, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixDivideScalar(matrix, divisor);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixAddArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{3000, 3000, 3000}, {3000, 3000, 3000}, {3000, 3000, 3000}},
                new double[][]{{250, 0, 0}, {-400, -400, 300}, {300, 300, 550}},
                new double[][]{{3250, 3000, 3000}, {2600, 2600, 3300}, {3300, 3300, 3550}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixAddArgs")
    void testMatrixAdd(double[][] matrix, double[][] matrixChange, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixAdd(matrix, matrixChange);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixSubtractArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{3250, 3000, 3000}, {2600, 2600, 3300}, {3300, 3300, 3550}},
                new double[][]{{250, 0, 0}, {-400, -400, 300}, {300, 300, 550}},
                new double[][]{{3000, 3000, 3000}, {3000, 3000, 3000}, {3000, 3000, 3000}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixSubtractArgs")
    void testMatrixSubtract(double[][] matrix, double[][] matrixChange, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixSubtract(matrix, matrixChange);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> cofactorMatrixArgs() {
        return List.of(
            // 2x2
            Arguments.of(new double[][]{{1, 2}, {3, 4}}, new double[][]{{4, -3}, {-2, 1}}, DELTA1),
            // 3x3
            Arguments.of(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}},
                new double[][]{{-3, 6, -3}, {6, -12, 6}, {-3, 6, -3}}, DELTA1),
            // 4x4
            Arguments.of(new double[][]{
                    {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 21, 12}, {33, 14, 15, 1}}, new double[][]{
                    {-1000, 2550, -100, -1200}, {420, -1230, 200, 360}, {60, -40, -100, 80}, {-80, 120, 0, -40}},
                DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("cofactorMatrixArgs")
    void testCofactorMatrix(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.cofactorMatrix(matrix);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixNormArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{2, 2, 6}, {1, 3, 9}, {6, 1, 0}},
                new double[]{15, 13, 11.67, 13.115, 9}, DELTA3)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixNormArgs")
    void testMatrixNorm(double[][] matrix, double[] expectedResult, double delta) {
        // when
        final double[] result = LinearAlgebra.matrixNorm(matrix);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> transposeMatrixArgs() {
        return List.of(
            // 3x2
            Arguments.of(new double[][]{{3, -1}, {0, 2}, {1, -1}}, new double[][]{{3, 0, 1}, {-1, 2, -1}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("transposeMatrixArgs")
    void testTransposeMatrix(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.transposeMatrix(matrix);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixMultiplyArgs() {
        return List.of(
            // 3x2 * 2x2
            Arguments.of(new double[][]{{3, -1}, {0, 2}, {1, -1}}, new double[][]{{1, 0}, {-1, 4}},
                new double[][]{{4, -4}, {-2, 8}, {2, -4}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixMultiplyArgs")
    void testMatrixMultiply(double[][] matrix, double[][] matrix2, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixMultiply(matrix, matrix2);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixInverseArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{1, 0, 5}, {2, 1, 6}, {3, 4, 0}},
                new double[][]{{-24, 20, -5}, {18, -15, 4}, {5, -4, 1}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixInverseArgs")
    void testMatrixInverse(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixInverse(matrix);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> gaussJordanEliminationSolverArgs() {
        return List.of(
            // 3x3
            // x+y+z=32
            // -x+2y=25
            // -y+2z=16
            Arguments.of(new double[][]{{1, 1, 1}, {-1, 2, 0}, {0, -1, 2}}, new double[]{32, 25, 16},
                new double[]{3, 14, 15}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("gaussJordanEliminationSolverArgs")
    void testGaussJordanEliminationSolver(
        double[][] coeffMatrix, double[] constantVector, double[] expectedResult, double delta) {
        // when
        final double[] result = LinearAlgebra.gaussJordanEliminationSolver(coeffMatrix, constantVector);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> cramersRuleArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{1, 1, 1}, {0, 1, -2}, {2, 0, -1}}, new double[]{26, 6, 12},
                new double[]{8, 14, 4}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("cramersRuleArgs")
    void testCramersRule(
        double[][] coeffMatrix, double[] constantVector, double[] expectedResult, double delta) {
        // when
        final double[] result = LinearAlgebra.cramersRule(coeffMatrix, constantVector);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixLUDecompositionArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{24, 20, -5}, {18, -15, 4}, {5, -4, 1}},
                Pair.of(new double[][]{{1, 0, 0}, {0.75, 1, 0}, {0.20833, 0.2722, 1}},
                    new double[][]{{24, 20, -5}, {0, -30, 7.75}, {0, 0, -0.06806}}), DELTA5)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixLUDecompositionArgs")
    void testMatrixLUDecomposition(double[][] matrix, Pair<double[][], double[][]> expectedResult, double delta) {
        // when
        final var result = LinearAlgebra.matrixLUDecomposition(matrix);
        // then
        assertNotNull(result);

        final double[][] expectedLowerMatrix = expectedResult.getRight();
        final double[][] lowerMatrix = result.getRight();
        assertMatrixEquals(expectedLowerMatrix, lowerMatrix, delta);

        final double[][] expectedUpperMatrix = expectedResult.getRight();
        final double[][] upperMatrix = result.getRight();
        assertMatrixEquals(expectedUpperMatrix, upperMatrix, delta);
    }

    static List<Arguments> matrixCholeskyDecompositionArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{25, 15, 5}, {15, 13, 11}, {5, 11, 21}},
                new double[][]{{5, 0, 0}, {3, 2, 0}, {1, 4, 2}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixCholeskyDecompositionArgs")
    void testMatrixCholeskyDecomposition(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixCholeskyDecomposition(matrix);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> matrixNullSpaceArgs() {
        return List.of(
            // 2x4
            Arguments.of(new double[][]{{2, -4, 8, 2}, {6, -12, 3, 13}},
                new double[][]{{2, 1, 0, 0}, {-2.333, 0, 0.333, 1}}, DELTA3)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixNullSpaceArgs")
    void testMatrixNullSpace(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] vectors = LinearAlgebra.matrixNullSpace(matrix);
        // then
        assertMatrixEquals(expectedResult, vectors, delta);
    }

    static List<Arguments> matrixColumnSpaceArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{1, 4, 3}, {3, 7, -1}, {-2, 1, 12}},
                new double[][]{{1, 3, -2}, {4, 7, 1}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixColumnSpaceArgs")
    @Disabled
    void testMatrixColumnSpace(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] vectors = LinearAlgebra.matrixColumnSpace(matrix);
        // then
        assertMatrixEquals(expectedResult, vectors, delta);
    }

    static List<Arguments> isLinearlyIndependentArgs() {
        return List.of(
            Arguments.of(new double[][]{{1, 3, -2}, {4, 7, 1}, {3, -1, -12}}, true),
            Arguments.of(new double[][]{{1, 3, -2}, {4, 7, 1}, {3, -1, 12}}, false)
        );
    }

    @ParameterizedTest
    @MethodSource("isLinearlyIndependentArgs")
    void testIsLinearlyIndependent(double[][] vectors, boolean expectedResult) {
        // when
        final boolean independent = LinearAlgebra.isLinearlyIndependent(vectors);
        // then
        assertEquals(expectedResult, independent);
    }

    static List<Arguments> linearCombinationLCMArgs() {
        return List.of(
            // x - 4y = 1
            // -2x + 4y = 2
            Arguments.of(new double[][]{{1, -4, 1}, {-2, 4, 2}}, new double[]{-3, -1}, DELTA1),
            // 2x + 3y = 3
            // 2x - y = -3
            Arguments.of(new double[][]{{2, 3, 3}, {2, -1, -3}}, new double[]{-0.75, 1.5}, DELTA2),
            // 3x - 7y = 1
            // 4x + 4y = -2
            Arguments.of(new double[][]{{3, -7, 1}, {4, 4, -2}}, new double[]{-0.25, -0.25}, DELTA2)
        );
    }

    @ParameterizedTest
    @MethodSource("linearCombinationLCMArgs")
    void testLinearCombinationLCM(double[][] equations, double[] expectedResult, double delta) {
        // when
        final double[] solution = LinearAlgebra.linearCombinationLCM(equations);
        // then
        assertArrayEquals(expectedResult, solution, delta);
    }

    static List<Arguments> matrixRankArgs() {
        return List.of(
            // 4x3
            Arguments.of(new double[][]{{0, 2, -1}, {1, 0, 1}, {2, -1, 3}, {1, 1, 4}}, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixRankArgs")
    void testMatrixRank(double[][] matrix, int expectedResult) {
        // when
        final int rank = LinearAlgebra.matrixRank(matrix);
        // then
        assertEquals(expectedResult, rank);
    }

    static List<Arguments> gramSchmidtArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{1, 3, -2}, {4, 7, 1}, {3, -1, 12}},
                new double[][]{{0.2673, 0.8018, -0.5345}, {0.4438, 0.39, 0.8068}}, DELTA4)
        );
    }

    @ParameterizedTest
    @MethodSource("gramSchmidtArgs")
    void testGramSchmidt(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.gramSchmidt(matrix);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> svdArgs() {
        // 2x2
        return List.of(
            Arguments.of(new double[][]{{1, 2}, {3, 4}}, Triple.of(
                new double[][]{{0.4047, 0.9125}, {0.9144, -0.4091}}, new double[][]{{5.4653, 0}, {0, 0.3606}},
                new double[][]{{0.5735, 0.8192}, {-0.8176, 0.5758}}), DELTA2)
        );
    }

    @ParameterizedTest
    @MethodSource("svdArgs")
    @Disabled
    void testSvd(double[][] matrix, Triple<double[][], double[][], double[][]> expectedResult, double delta) {
        // when
        final var result = LinearAlgebra.svd(matrix);
        // then
        assertNotNull(result);
        final double[][] reconstructed = LinearAlgebra
            .reconstructFromSVD(result.getLeft(), result.getMiddle(), result.getRight());
        assertMatrixApproxEquals(matrix, reconstructed, delta);
        assertMatrixEquals(expectedResult.getLeft(), result.getLeft(), delta);
        assertMatrixEquals(expectedResult.getMiddle(), result.getMiddle(), delta);
        assertMatrixEquals(expectedResult.getRight(), result.getRight(), delta);
    }

    private static void assertMatrixApproxEquals(double[][] expected, double[][] actual, double delta) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].length, actual[i].length);
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], actual[i][j], delta);
            }
        }
    }

    static List<Arguments> matrixPowerArgs() {
        // 3x3
        return List.of(
            Arguments.of(new double[][]{{1, 0, 0}, {2, 1, -1}, {0, -1, 1}}, 13,
                new double[][]{{1, 0, 0}, {8192, 4096, -4096}, {-8190, -4096, 4096}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("matrixPowerArgs")
    void testMatrixPower(double[][] matrix, int exponent, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.matrixPower(matrix, exponent);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> hadamardProductArgs() {
        // 3x3
        return List.of(
            Arguments.of(new double[][]{{1, 2, 3}, {11, 22, 33}, {111, 222, 333}},
                new double[][]{{2, 2, 2}, {3, 3, 3}, {1, 2, 3}},
                new double[][]{{2, 4, 6}, {33, 66, 99}, {111, 444, 999}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("hadamardProductArgs")
    void testHadamardProduct(double[][] matrix, double[][] matrix2, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.hadamardProduct(matrix, matrix2);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> tensorProductArgs() {
        // 3x3
        return List.of(
            Arguments.of(new double[][]{{1, 2}, {3, 4}}, new double[][]{{2, 4}, {6, 8}},
                new double[][]{{2, 4, 4, 8}, {6, 8, 12, 16}, {6, 12, 8, 16}, {18, 24, 24, 32}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("tensorProductArgs")
    void testTensorProduct(double[][] matrix, double[][] matrix2, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.tensorProduct(matrix, matrix2);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> diagonalizeMatrixArgs() {
        // 3x3
        return List.of(
            Arguments.of(new double[][]{{1, 0, 0}, {2, 1, -1}, {0, -1, 1}}, new double[]{2, 0, 1},
                new double[][]{{2, 0, 0}, {0, 0, 0}, {0, 0, 1}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("diagonalizeMatrixArgs")
    void testDiagonalizeMatrix(double[][] matrix, double[] eigenvalues, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.diagonalizeMatrix(matrix, eigenvalues);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> characteristicPolynomialArgs() {
        return List.of(
            // 2x2
            Arguments.of(new double[][]{{2, 3}, {4, 3}}, new double[]{1, -5, -6}, DELTA1),
            // 3x3
            Arguments.of(new double[][]{{0, 2, 1}, {1, 3, -1}, {2, 0, 2}},
                new double[]{1, -5, 2, 14}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("characteristicPolynomialArgs")
    void testCharacteristicPolynomial(double[][] matrix, double[] expectedResult, double delta) {
        // when
        final double[] result = LinearAlgebra.characteristicPolynomial(matrix);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> adjointMatrixArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}},
                new double[][]{{-3, 6, -3}, {6, -12, 6}, {-3, 6, -3}}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("adjointMatrixArgs")
    void testAdjointMatrix(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.adjointMatrix(matrix);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }

    static List<Arguments> singularValuesArgs() {
        return List.of(
            // 3x3
            Arguments.of(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, new double[]{16.848, 1.0677, 0}, DELTA3)
        );
    }

    @ParameterizedTest
    @MethodSource("singularValuesArgs")
    void testSingularValues(double[][] matrix, double[] expectedResult, double delta) {
        // when
        final double[] result = LinearAlgebra.singularValues(matrix);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> pseudoinverseArgs() {
        return List.of(
            // 3x2
            Arguments.of(new double[][]{{1, 3}, {2, 4}, {3, 3}},
                new double[][]{{-0.3421, -0.1579, 0.5526}, {0.2895, 0.21053, -0.23684}}, DELTA5),
            Arguments.of(new double[][]{{1, 2}, {2, 4}, {3, 6}},
                new double[][]{{0.014286, 0.02857, 0.04286}, {0.02857, 0.05714, 0.08571}}, DELTA5)
        );
    }

    @ParameterizedTest
    @MethodSource("pseudoinverseArgs")
    @Disabled
    void testPseudoinverse(double[][] matrix, double[][] expectedResult, double delta) {
        // when
        final double[][] result = LinearAlgebra.pseudoinverse(matrix);
        // then
        assertMatrixEquals(expectedResult, result, delta);
    }
}
