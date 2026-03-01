package org.example.sciencecalc.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrigonometryTest {
    static List<Arguments> sinParams() {
        return List.of(
            Arguments.of(Trigonometry.PI_OVER_12, 0.2588190451),
            Arguments.of(Trigonometry.PI_OVER_6, 0.5),
            Arguments.of(Trigonometry.PI_OVER_4, 0.7071067812),
            Arguments.of(Trigonometry.PI_OVER_3, 0.8660254038),
            Arguments.of(Trigonometry.PI5_OVER_12, 0.9659258263),
            Arguments.of(Trigonometry.PI_OVER_2, 1),
            Arguments.of(Trigonometry.PI7_OVER_12, 0.9659258263),
            Arguments.of(Trigonometry.PI2_OVER_3, 0.8660254038),
            Arguments.of(Trigonometry.PI3_OVER_4, 0.7071067812),
            Arguments.of(Trigonometry.PI5_OVER_6, 0.5),
            Arguments.of(Trigonometry.PI11_OVER_12, 0.2588190451)
        );
    }

    @ParameterizedTest
    @MethodSource("sinParams")
    void testSin(double angleAlphaRadians, double expectedResult) {
        // when
        final double sine = Trigonometry.sin(angleAlphaRadians);
        // then
        assertEquals(expectedResult, sine, 0.0000000001);
    }

    @Test
    void testSinusoid() {
        // given
        final double anglePhiRadians = Math.toRadians(40);
        final byte oscillationFrequency = 10;
        final double amplitude = 0.8;
        final byte timeSeconds = 10;
        // when
        final double sinusoid = Trigonometry.sinusoid(
            amplitude, anglePhiRadians, oscillationFrequency, timeSeconds);
        // then
        assertEquals(0.51423008, sinusoid, 0.00000001);
    }

    static List<Arguments> quadrantParams() {
        return List.of(
            Arguments.of(Trigonometry.PI_OVER_12, 1),
            Arguments.of(Trigonometry.PI_OVER_6, 1),
            Arguments.of(Trigonometry.PI_OVER_4, 1),
            Arguments.of(Trigonometry.PI_OVER_3, 1),
            Arguments.of(Trigonometry.PI5_OVER_12, 1),
            Arguments.of(Trigonometry.PI_OVER_2, 1),
            Arguments.of(Trigonometry.PI7_OVER_12, 2),
            Arguments.of(Trigonometry.PI2_OVER_3, 2),
            Arguments.of(Trigonometry.PI3_OVER_4, 2),
            Arguments.of(Trigonometry.PI5_OVER_6, 2),
            Arguments.of(Trigonometry.PI11_OVER_12, 2),
            Arguments.of(Math.PI, 2),
            Arguments.of(Trigonometry.PI7_OVER_6, 3),
            Arguments.of(Trigonometry.PI5_OVER_4, 3),
            Arguments.of(Trigonometry.PI4_OVER_3, 3),
            Arguments.of(Trigonometry.PI3_OVER_2, 3),
            Arguments.of(Trigonometry.PI5_OVER_3, 4),
            Arguments.of(Trigonometry.PI7_OVER_4, 4),
            Arguments.of(Trigonometry.PI11_OVER_6, 4),
            Arguments.of(Trigonometry.PI2, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("quadrantParams")
    void testQuadrant(double angleAlphaRadians, int expectedResult) {
        // when
        final int quadrant = Trigonometry.quadrant(angleAlphaRadians);
        // then
        assertEquals(expectedResult, quadrant);
    }

    @Test
    void testLawOfTangents() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double result = Trigonometry.lawOfTangents(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(-0.171572, result, 0.000001);
    }

    static List<Arguments> cotangentOfAngleParams() {
        return List.of(
            Arguments.of(Math.toRadians(30), 1.73205081, 0.00000001),
            Arguments.of(Math.toRadians(45), 1, 0.1),
            Arguments.of(Math.toRadians(60), 0.57735027, 0.00000001),
            Arguments.of(Math.toRadians(75), 0.26794919, 0.00000001)
        );
    }

    @ParameterizedTest
    @MethodSource("cotangentOfAngleParams")
    void testCotangentOfAngle(double angleAlphaRadians, double expectedResult, double delta) {
        // when
        final double result = Trigonometry.cot(angleAlphaRadians);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> cotangentParams() {
        return List.of(
            Arguments.of(2 * Math.sqrt(3), 2, 1.73205081, 0.00000001),
            Arguments.of(3 * Math.sqrt(2) / 2, 3 * Math.sqrt(2) / 2, 1, 0.1),
            Arguments.of(4, 4 * Math.sqrt(3), 0.57735027, 0.00000001)
        );
    }

    @ParameterizedTest
    @MethodSource("cotangentParams")
    void testCotangent(double adjacent, double opposite, double expectedResult, double delta) {
        // when
        final double result = Trigonometry.cot(adjacent, opposite);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> secantOfAngleParams() {
        return List.of(
            Arguments.of(Math.toRadians(30), 1.15470054, 0.00000001),
            Arguments.of(Math.toRadians(45), 1.41421356, 0.00000001),
            Arguments.of(Math.toRadians(60), 2, 0.1),
            Arguments.of(Math.toRadians(75), 3.86370331, 0.00000001)
        );
    }

    @ParameterizedTest
    @MethodSource("secantOfAngleParams")
    void testSecantOfAngle(double angleAlphaRadians, double expectedResult, double delta) {
        // when
        final double result = Trigonometry.sec(angleAlphaRadians);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> secantParams() {
        return List.of(
            Arguments.of(2 * 3, 3 * Math.sqrt(3), 1.15470054, 0.00000001),
            Arguments.of(5 * Math.sqrt(2), 5, 1.41421356, 0.00000001),
            Arguments.of(2 * 4, 4, 2, 0.1)
        );
    }

    @ParameterizedTest
    @MethodSource("secantParams")
    void testSecant(double hypotenuse, double adjacent, double expectedResult, double delta) {
        // when
        final double result = Trigonometry.sec(hypotenuse, adjacent);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> cosecantOfAngleParams() {
        return List.of(
            Arguments.of(Math.toRadians(30), 2, 0.1),
            Arguments.of(Math.toRadians(45), 1.41421356, 0.00000001),
            Arguments.of(Math.toRadians(60), 1.15470054, 0.00000001),
            Arguments.of(Math.toRadians(75), 1.03527618, 0.00000001)
        );
    }

    @ParameterizedTest
    @MethodSource("cosecantOfAngleParams")
    void testCosecantOfAngle(double angleAlphaRadians, double expectedResult, double delta) {
        // when
        final double result = Trigonometry.csc(angleAlphaRadians);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> cosecantParams() {
        return List.of(
            Arguments.of(2 * 3, 3, 2, 0.1),
            Arguments.of(5 * Math.sqrt(2), 5, 1.41421356, 0.00000001),
            Arguments.of(2 * 4, 4 * Math.sqrt(3), 1.15470054, 0.00000001)
        );
    }

    @ParameterizedTest
    @MethodSource("cosecantParams")
    void testCosecant(double hypotenuse, double opposite, double expectedResult, double delta) {
        // when
        final double result = Trigonometry.csc(hypotenuse, opposite);
        // then
        assertEquals(expectedResult, result, delta);
    }

    @Test
    void testCosHalfAngle() {
        // given
        final double angleRadians = Math.toRadians(30);
        // when
        final double result = Trigonometry.cosHalfAngle(angleRadians);
        // then
        assertEquals(0.96592583, result, 0.00000001);
    }

    @Test
    void testSinHalfAngle() {
        // given
        final double angleRadians = Math.toRadians(30);
        // when
        final double result = Trigonometry.sinHalfAngle(angleRadians);
        // then
        assertEquals(0.25881905, result, 0.00000001);
    }

    @Test
    void testTanHalfAngle() {
        // given
        final double angleRadians = Math.toRadians(30);
        // when
        final double result = Trigonometry.tanHalfAngle(angleRadians);
        // then
        assertEquals(0.26794919, result, 0.00000001);
    }

    @Test
    void testSinDoubleAngle() {
        // given
        final double angleThetaRadians = Math.toRadians(15);
        // when
        final double result = Trigonometry.sinDoubleAngle(angleThetaRadians);
        // then
        assertEquals(0.5, result, 0.1);
    }

    @Test
    void testCosDoubleAngle() {
        // given
        final double angleThetaRadians = Math.toRadians(15);
        // when
        final double result = Trigonometry.cosDoubleAngle(angleThetaRadians);
        // then
        assertEquals(0.86603, result, 0.00001);
    }

    @Test
    void testTanDoubleAngle() {
        // given
        final double angleThetaRadians = Math.toRadians(15);
        // when
        final double result = Trigonometry.tanDoubleAngle(angleThetaRadians);
        // then
        assertEquals(0.57735, result, 0.00001);
    }

    @Test
    void testSinPhaseShift() {
        // given
        final double amplitude = 0.5; // A
        final byte x = 1;
        final double periodRadians = 2; // B
        final byte phaseShift = 3; // C
        final byte verticalShift = 4; // D
        // when
        final double[] results = Trigonometry
            .sinPhaseShift(x, amplitude, periodRadians, phaseShift, verticalShift);
        // then
        assertNotNull(results);
        assertEquals(3, results.length);
        assertEquals(1.5, results[Constants.ARR_1ST_INDEX], 0.1);
        assertEquals(3.1416, results[Constants.ARR_2ND_INDEX], 0.001);
        assertEquals(3.5792, results[Constants.ARR_3RD_INDEX], 0.001);
    }

    @Test
    void testCosPhaseShift() {
        // given
        final double amplitude = 0.5; // A
        final byte x = 1;
        final double periodRadians = 2; // B
        final byte phaseShift = 3; // C
        final byte verticalShift = 4; // D
        // when
        final double[] results = Trigonometry
            .cosPhaseShift(x, amplitude, periodRadians, phaseShift, verticalShift);
        // then
        assertNotNull(results);
        assertEquals(3, results.length);
        assertEquals(1.5, results[Constants.ARR_1ST_INDEX], 0.1);
        assertEquals(3.1416, results[Constants.ARR_2ND_INDEX], 0.001);
        assertEquals(4.27, results[Constants.ARR_3RD_INDEX], 0.01);
    }

    @Test
    void testSinPowerReducing() {
        // given
        final double angleRadians = Math.toRadians(15);
        // when
        final double squaredResult = Trigonometry.sinPowerReducing(angleRadians);
        // then
        assertEquals(0.0669873, squaredResult, 0.0000001);
        final double inverseDegrees = Math.toDegrees(BigDecimal.valueOf(Math.sqrt(squaredResult))
            .setScale(4, RoundingMode.HALF_UP).doubleValue());
        assertEquals(14.828, inverseDegrees, 0.001);
    }

    @Test
    void testCosPowerReducing() {
        // given
        final double angleRadians = Math.toRadians(15);
        // when
        final double squaredResult = Trigonometry.cosPowerReducing(angleRadians);
        // then
        assertEquals(0.9330127, squaredResult, 0.0000001);
    }

    @Test
    void testTanPowerReducing() {
        // given
        final double angleRadians = Math.toRadians(15);
        // when
        final double squaredResult = Trigonometry.tanPowerReducing(angleRadians);
        // then
        assertEquals(0.0718, squaredResult, 0.0001);
    }

    @Test
    void testLawOfCosSAS() {
        // given
        final byte sideA = 5;
        final byte sideB = 6;
        final double angleRadians = Math.toRadians(30);
        // when
        final double sideC = Trigonometry.lawOfCosSAS(sideA, sideB, angleRadians);
        // then
        assertEquals(3.006406, sideC, 0.000001);
    }

    @Test
    void testLawOfCosSSS() {
        // given
        final byte sideA = 4;
        final byte sideB = 5;
        final byte sideC = 6;
        // when
        final double[] results = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
        // then
        assertNotNull(results);
        assertEquals(3, results.length);
        assertEquals(0.7227, results[Constants.ALPHA_INDEX], 0.0001);
        assertEquals(0.9734, results[Constants.BETA_INDEX], 0.0001);
        assertEquals(1.4455, results[Constants.GAMMA_INDEX], 0.0001);
    }

    @Test
    void testLawOfSinGivenSideAAndAnglesAlphaBeta() {
        // given
        final double sideA = 2.3094;
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(60);
        // when
        final double sideB = Trigonometry
            .lawOfSinGivenSideAAndAnglesAlphaBeta(sideA, angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(4, sideB, 0.0001);
    }

    @Test
    void testLawOfSinGivenSideBAndAnglesAlphaBeta() {
        // given
        final byte sideB = 4;
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(60);
        // when
        final double sideA = Trigonometry
            .lawOfSinGivenSideBAndAnglesAlphaBeta(sideB, angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(2.3094, sideA, 0.0001);
    }

    @Test
    void testLawOfSinGivenSidesABAndAngleAlpha() {
        // given
        final double sideA = 2.3094;
        final byte sideB = 4;
        final double angleAlphaRadians = Math.toRadians(30);
        // when
        final double angleBetaRad = Trigonometry
            .lawOfSinGivenSidesABAndAngleAlpha(sideA, sideB, angleAlphaRadians);
        // then
        assertEquals(Math.toRadians(60), angleBetaRad, 0.0001);
    }

    @Test
    void testLawOfSinGivenSidesABAndAngleBeta() {
        // given
        final double sideA = 2.3094;
        final byte sideB = 4;
        final double angleBetaRadians = Math.toRadians(60);
        // when
        final double angleAlphaRad = Trigonometry
            .lawOfSinGivenSidesABAndAngleBeta(sideA, sideB, angleBetaRadians);
        // then
        assertEquals(Math.toRadians(30), angleAlphaRad, 0.0001);
    }

    @Test
    void testLawOfSinGivenSidesBCAndAngleBeta() {
        // given
        final byte sideB = 4;
        final byte sideC = 8;
        final double angleBetaRadians = Math.toRadians(30);
        // when
        final double angleGammaRad = Trigonometry
            .lawOfSinGivenSidesBCAndAngleBeta(sideC, sideB, angleBetaRadians);
        // then
        assertEquals(Math.toRadians(90), angleGammaRad, 0.1);
    }

    @Test
    void testLawOfSinGivenSidesACAndAngleAlpha() {
        // given
        final double sideA = 2;
        final byte sideC = 4;
        final double angleAlphaRadians = Math.toRadians(30);
        // when
        final double angleGammaRad = Trigonometry
            .lawOfSinGivenSidesACAndAngleAlpha(sideA, sideC, angleAlphaRadians);
        // then
        assertEquals(Math.toRadians(90), angleGammaRad, 0.1);
    }

    @Test
    void testSinAngleSum() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(90);
        // when
        final double angleSumRad = Trigonometry.sinAngleSum(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(0.866, angleSumRad, 0.001);
    }

    @Test
    void testSinAngleDifference() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(90);
        // when
        final double angleDiffRad = Trigonometry
            .sinAngleDifference(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(-0.866, angleDiffRad, 0.001);
    }

    @Test
    void testCosAngleSum() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleSumRad = Trigonometry.cosAngleSum(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(0.259, angleSumRad, 0.001);
    }

    @Test
    void testCosAngleDifference() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleDiffRad = Trigonometry
            .cosAngleDifference(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(0.966, angleDiffRad, 0.001);
    }

    @Test
    void testTanAngleSum() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleSumRad = Trigonometry.tanAngleSum(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(3.732, angleSumRad, 0.001);
    }

    @Test
    void testTanAngleDifference() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleDiffRad = Trigonometry
            .tanAngleDifference(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(-0.268, angleDiffRad, 0.001);
    }

    @Test
    void testCotAngleSum() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleSumRad = Trigonometry.cotAngleSum(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(0.268, angleSumRad, 0.001);
    }

    @Test
    void testCotAngleDifference() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleDiffRad = Trigonometry
            .cotAngleDifference(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(-3.732, angleDiffRad, 0.001);
    }

    @Test
    void testSecAngleSum() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleSumRad = Trigonometry.secAngleSum(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(3.864, angleSumRad, 0.001);
    }

    @Test
    void testSecAngleDifference() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleDiffRad = Trigonometry
            .secAngleDifference(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(1.035, angleDiffRad, 0.001);
    }

    @Test
    void testCscAngleSum() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleSumRad = Trigonometry.cscAngleSum(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(1.035, angleSumRad, 0.001);
    }

    @Test
    void testCscAngleDifference() {
        // given
        final double angleAlphaRadians = Math.toRadians(30);
        final double angleBetaRadians = Math.toRadians(45);
        // when
        final double angleDiffRad = Trigonometry
            .cscAngleDifference(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(-3.864, angleDiffRad, 0.001);
    }

    @Test
    void testFindSinWithCosAndTan() {
        // given
        final double angleAlphaRadians = Math.toRadians(60);
        // when
        final double sine = Trigonometry.findSinWithCosAndTan(angleAlphaRadians);
        // then
        assertEquals(0.8660254, sine, 0.0000001);
    }
}
