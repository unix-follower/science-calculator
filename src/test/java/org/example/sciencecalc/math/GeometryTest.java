package org.example.sciencecalc.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeometryTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;

    @Test
    void testTriangle306090SolveWithA() {
        // given
        final float sideA = 6.35f; // cm
        // when
        final double[] sides = Geometry.triangle306090SolveWithA(sideA);
        // then
        assertNotNull(sides);
        assertEquals(3, sides.length);
        assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.01); // cm
        assertEquals(11, sides[Constants.ARR_2ND_INDEX], 0.01); // cm
        assertEquals(12.7, sides[Constants.ARR_3RD_INDEX], 0.1); // cm
    }

    @Test
    void testTriangle306090SolveWithB() {
        // given
        final int sideB = 11; // cm
        // when
        final double[] sides = Geometry.triangle306090SolveWithB(sideB);
        // then
        assertNotNull(sides);
        assertEquals(3, sides.length);
        assertEquals(6.35, sides[Constants.ARR_1ST_INDEX], 0.01); // cm
        assertEquals(sideB, sides[Constants.ARR_2ND_INDEX], 0.1); // cm
        assertEquals(12.7, sides[Constants.ARR_3RD_INDEX], 0.1); // cm
    }

    @Test
    void testTriangle306090SolveWithC() {
        // given
        final float sideC = 12.7f; // cm
        // when
        final double[] sides = Geometry.triangle306090SolveWithC(sideC);
        // then
        assertNotNull(sides);
        assertEquals(3, sides.length);
        assertEquals(6.35, sides[Constants.ARR_1ST_INDEX], 0.01); // cm
        assertEquals(11, sides[Constants.ARR_2ND_INDEX], 0.1); // cm
        assertEquals(sideC, sides[Constants.ARR_3RD_INDEX], 0.1); // cm
    }

    static List<Arguments> pythagoreanTheoremForRightTriangleWithLegAndHypotenuseParams() {
        return List.of(
            Arguments.of(4, 8.94427, 8, 16, 20.94427),
            Arguments.of(7.07, 10, 7.07, 25, 24.14214)
        );
    }

    @ParameterizedTest
    @MethodSource("pythagoreanTheoremForRightTriangleWithLegAndHypotenuseParams")
    void testPythagoreanTheoremForRightTriangleWithLegAndHypotenuse(
        double sideA, double hypotenuse, double expectedResult, double expectedArea, double expectedPerimeter) {
        // when
        final double[] sides = Geometry
            .pythagoreanTheoremForRightTriangleWithLegAndHypotenuse(sideA, hypotenuse);
        // then
        assertNotNull(sides);
        assertEquals(3, sides.length);

        final double sideB = sides[Constants.ARR_2ND_INDEX];

        assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.1);
        assertEquals(expectedResult, sideB, 0.1);
        assertEquals(hypotenuse, sides[Constants.ARR_3RD_INDEX], 0.000001);

        final double area = Geometry.area(sideA, sideB);
        assertEquals(expectedArea, area, 0.1);

        final double perimeter = Geometry.perimeter(sideA, sideB, hypotenuse);
        assertEquals(expectedPerimeter, perimeter, 0.00001);
    }

    @Test
    void testPythagoreanTheoremForRightTriangleWithLegs() {
        // given
        final byte sideA = 7; // cm
        final byte sideB = 9; // cm
        // when
        final double[] sides = Geometry.pythagoreanTheoremForRightTriangleWithLegs(sideA, sideB);
        // then
        assertNotNull(sides);
        assertEquals(3, sides.length);

        assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.1);
        assertEquals(sideB, sides[Constants.ARR_2ND_INDEX], 0.1);
        final double hypotenuse = 11.40175; // cm
        assertEquals(hypotenuse, sides[Constants.ARR_3RD_INDEX], 0.00001);

        final double area = Geometry.area(sideA, sideB);
        assertEquals(31.5, area, 0.1);

        final double perimeter = Geometry.perimeter(sideA, sideB, hypotenuse);
        assertEquals(27.40175, perimeter, 0.00001);
    }

    @Test
    void testAreaOfSquare() {
        // given
        final byte sideLength = 4; // cm
        // when
        final double area = Geometry.areaOfSquare(sideLength);
        // then
        assertEquals(16, area, 0.1);
    }

    @Test
    void testRectangleArea() {
        // given
        final byte sideLengthA = 2; // cm
        final byte sideLengthB = 4; // cm
        // when
        final double area = Geometry.rectangleArea(sideLengthA, sideLengthB);
        // then
        assertEquals(8, area, DELTA1);
    }

    @Test
    void testAreaOfTriangleWithBaseAndHeight() {
        // given
        final byte base = 8; // cm
        final byte height = 4; // cm
        // when
        final double area = Geometry.areaOfTriangleWithBaseAndHeight(base, height);
        // then
        assertEquals(16, area, 0.1);
    }

    @Test
    void testAreaOfTriangleWithSSS() {
        // given
        final byte sideLengthA = 2; // cm
        final byte sideLengthB = 5; // cm
        final byte sideLengthC = 4; // cm
        // when
        final double area = Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC);
        // then
        assertEquals(3.8, area, 0.1);
    }

    @Test
    void testAreaOfTriangleWithSSSInvalidSideA() {
        // given
        final byte sideLengthA = 7; // cm
        final byte sideLengthB = 5; // cm
        final byte sideLengthC = 1; // cm
        // when
        final var exception = assertThrows(IllegalArgumentException.class,
            () -> Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
        // then
        assertEquals("Side length (a) must be less than the sum of the other two sides to form a triangle",
            exception.getMessage());
    }

    @Test
    void testAreaOfTriangleWithSSSInvalidSideB() {
        // given
        final byte sideLengthA = 2; // cm
        final byte sideLengthB = 5; // cm
        final byte sideLengthC = 2; // cm
        // when
        final var exception = assertThrows(IllegalArgumentException.class,
            () -> Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
        // then
        assertEquals("Side length (b) must be less than the sum of the other two sides to form a triangle",
            exception.getMessage());
    }

    @Test
    void testAreaOfTriangleWithSSSInvalidSideC() {
        // given
        final byte sideLengthA = 2; // cm
        final byte sideLengthB = 2; // cm
        final byte sideLengthC = 4; // cm
        // when
        final var exception = assertThrows(IllegalArgumentException.class,
            () -> Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
        // then
        assertEquals("Side length (c) must be less than the sum of the other two sides to form a triangle",
            exception.getMessage());
    }

    @Test
    void testAreaOfTriangleWithSAS() {
        // given
        final byte sideLengthA = 2; // cm
        final byte sideLengthB = 5; // cm
        final double angleGammaRadians = Math.toRadians(30);
        // when
        final double area = Geometry.areaOfTriangleWithSAS(
            sideLengthA, sideLengthB, angleGammaRadians);
        // then
        assertEquals(2.5, area, 0.1);
    }

    @Test
    void testAreaOfTriangleWithASA() {
        // given
        final byte sideLengthA = 2; // cm
        final double angleBetaRadians = Math.toRadians(30);
        final double angleGammaRadians = Math.toRadians(60);
        // when
        final double area = Geometry.areaOfTriangleWithASA(
            sideLengthA, angleBetaRadians, angleGammaRadians);
        // then
        assertEquals(0.866, area, 0.001);
    }

    @Test
    void testCircleArea() {
        // given
        final byte radius = 5; // cm
        // when
        final double area = Geometry.circleArea(radius);
        // then
        assertEquals(78.54, area, 0.01);
    }

    @Test
    void testSemicircleArea() {
        // given
        final byte radius = 5; // cm
        // when
        final double area = Geometry.semicircleArea(radius);
        // then
        assertEquals(39.27, area, 0.01);
    }

    @Test
    void testSectorArea() {
        // given
        final byte radius = 5; // cm
        final double angleAlphaRadians = Math.toRadians(30);
        // when
        final double area = Geometry.sectorArea(radius, angleAlphaRadians);
        // then
        assertEquals(6.545, area, 0.001);
    }

    @Test
    void testEllipseArea() {
        // given
        final byte radiusA = 5; // cm
        final byte radiusB = 3; // cm
        // when
        final double area = Geometry.ellipseArea(radiusA, radiusB);
        // then
        assertEquals(47.12, area, 0.01);
    }

    @Test
    void testTrapezoidArea() {
        // given
        final byte sideA = 2; // cm
        final byte sideB = 8; // cm
        final byte height = 4; // cm
        // when
        final double area = Geometry.trapezoidArea(sideA, sideB, height);
        // then
        assertEquals(20, area, 0.1);
    }

    @Test
    void testParallelogramAreaWithBaseAndHeight() {
        // given
        final byte base = 8; // cm
        final byte height = 4; // cm
        // when
        final double area = Geometry.parallelogramAreaWithBaseAndHeight(base, height);
        // then
        assertEquals(32, area, 0.1);
    }

    @Test
    void testParallelogramAreaWithSidesAndAngle() {
        // given
        final byte sideA = 2; // cm
        final byte sideB = 8; // cm
        final double angleAlphaRadians = Math.toRadians(60);
        // when
        final double area = Geometry
            .parallelogramAreaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
        // then
        assertEquals(13.856, area, 0.001);
    }

    @Test
    void testParallelogramAreaWithDiagonalsAndAngle() {
        // given
        final byte diagonal1 = 6; // cm
        final byte diagonal2 = 4; // cm
        final double angleThetaRadians = Math.toRadians(45);
        // when
        final double area = Geometry
            .parallelogramAreaWithDiagonalsAndAngle(diagonal1, diagonal2, angleThetaRadians);
        // then
        assertEquals(16.97, area, 0.01);
    }

    @Test
    void testRhombusAreaWithSideAndHeight() {
        // given
        final byte side = 4; // cm
        final byte height = 6; // cm
        // when
        final double area = Geometry.rhombusAreaWithSideAndHeight(side, height);
        // then
        assertEquals(24, area, 0.1);
    }

    @Test
    void testRhombusAreaWithDiagonals() {
        // given
        final byte diagonal1 = 6; // cm
        final byte diagonal2 = 4; // cm
        // when
        final double area = Geometry.rhombusAreaWithDiagonals(diagonal1, diagonal2);
        // then
        assertEquals(12, area, 0.1);
    }

    @Test
    void testRhombusAreaWithSideAndAngle() {
        // given
        final byte side = 4; // cm
        final double angleAlphaRadians = Math.toRadians(60);
        // when
        final double area = Geometry.rhombusAreaWithSideAndAngle(side, angleAlphaRadians);
        // then
        assertEquals(13.856, area, 0.001);
    }

    @Test
    void testKiteAreaWithDiagonals() {
        // given
        final byte diagonal1 = 4; // cm
        final byte diagonal2 = 10; // cm
        // when
        final double area = Geometry.kiteAreaWithDiagonals(diagonal1, diagonal2);
        // then
        assertEquals(20, area, 0.1);
    }

    @Test
    void testKiteAreaWithSidesAndAngle() {
        // given
        final byte sideA = 2; // cm
        final byte sideB = 7; // cm
        final double angleAlphaRadians = Math.toRadians(45);
        // when
        final double area = Geometry
            .kiteAreaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
        // then
        assertEquals(9.9, area, 0.1);
    }

    @Test
    void testPentagonArea() {
        // given
        final byte sideLength = 4; // cm
        // when
        final double area = Geometry.pentagonArea(sideLength);
        // then
        assertEquals(27.53, area, 0.01);
    }

    @Test
    void testHexagonArea() {
        // given
        final byte sideLength = 4; // cm
        // when
        final double area = Geometry.hexagonArea(sideLength);
        // then
        assertEquals(41.57, area, 0.01);
    }

    @Test
    void testOctagonArea() {
        // given
        final byte sideLength = 3; // cm
        // when
        final double area = Geometry.octagonArea(sideLength);
        // then
        assertEquals(43.46, area, 0.01);
    }

    @Test
    void testAnnulusArea() {
        // given
        final double innerRadius = 1.5; // cm
        final double radius = 4; // cm
        // when
        final double area = Geometry.annulusArea(radius, innerRadius);
        // then
        assertEquals(43.2, area, 0.1);
    }

    @Test
    void testIrregularQuadrilateralArea() {
        // given
        final byte diagonal1 = 4; // cm
        final byte diagonal2 = 5; // cm
        final double angleAlphaRadians = Math.toRadians(35);
        // when
        final double area = Geometry
            .irregularQuadrilateralArea(diagonal1, diagonal2, angleAlphaRadians);
        // then
        assertEquals(11.472, area, 0.001);
    }

    @Test
    void testPolygonArea() {
        // given
        final byte numberOfSides = 12;
        final byte sideLength = 2; // cm
        // when
        final double area = Geometry.polygonArea(numberOfSides, sideLength);
        // then
        assertEquals(44.785, area, 0.001);
    }

    @Test
    void testScaleneTriangleHeight() {
        // given
        final byte sideA = 6;
        final byte sideB = 14;
        final byte sideC = 17;
        // when
        final double[] heights = Geometry.scaleneTriangleHeight(sideA, sideB, sideC);
        // then
        assertNotNull(heights);
        assertEquals(3, heights.length);
        assertEquals(13.17, heights[Constants.ARR_1ST_INDEX], 0.01);
        final double heightB = heights[Constants.ARR_2ND_INDEX];
        assertEquals(5.644, heightB, 0.001);
        assertEquals(4.648, heights[Constants.ARR_3RD_INDEX], 0.001);

        final double areaCmSquared = Geometry.areaOfTriangleWithSSS(sideA, sideB, sideC);
        assertEquals(39.51, areaCmSquared, 0.01);

        final double perimeter = Geometry.perimeter(sideA, sideB, sideC);
        assertEquals(37, perimeter, 0.1);

        final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
        assertEquals(0.3384, angles[Constants.ALPHA_INDEX], 0.0001);
        assertEquals(0.8862, angles[Constants.BETA_INDEX], 0.0001);
        assertEquals(1.917, angles[Constants.GAMMA_INDEX], 0.001);
    }

    @Test
    void testEquilateralTriangleHeight() {
        // given
        final byte sides = 5;
        // when
        final double height = Geometry.equilateralTriangleHeight(sides);
        // then
        assertEquals(4.33, height, 0.01);

        final double areaCmSquared = Geometry.equilateralTriangleArea(sides);
        assertEquals(10.825, areaCmSquared, 0.001);

        final double perimeter = Geometry.perimeter(sides, sides, sides);
        assertEquals(15, perimeter, 0.1);
    }

    @Test
    void testIsoscelesTriangleHeight() {
        // given
        final byte sideA = 3;
        final byte sideB = 5;
        // when
        final double[] heights = Geometry.isoscelesTriangleHeight(sideA, sideB);
        // then
        assertNotNull(heights);
        assertEquals(2, heights.length);
        assertEquals(2.764, heights[Constants.ARR_1ST_INDEX], 0.001);
        final double heightB = heights[Constants.ARR_2ND_INDEX];
        assertEquals(1.6583, heightB, 0.0001);

        final double areaCmSquared = Geometry.isoscelesTriangleArea(sideB, heightB);
        assertEquals(4.146, areaCmSquared, 0.001);

        final double perimeter = Geometry.perimeter(sideA, sideB, sideA);
        assertEquals(11, perimeter, 0.1);

        final double[] angles = Trigonometry.lawOfCosSSS(sideB, sideA, sideA);
        assertEquals(1.9702, angles[Constants.ALPHA_INDEX], 0.0001);
        assertEquals(0.5857, angles[Constants.BETA_INDEX], 0.0001);
    }

    @Test
    void testRightTriangleHeight() {
        // given
        final byte sideA = 3;
        final byte sideB = 4;
        final byte sideC = 5;
        // when
        final double[] heights = Geometry.rightTriangleHeight(sideA, sideB, sideC);
        // then
        assertNotNull(heights);
        assertEquals(3, heights.length);
        assertEquals(4, heights[Constants.ARR_1ST_INDEX], 0.1);
        assertEquals(3, heights[Constants.ARR_2ND_INDEX], 0.1);
        assertEquals(2.4, heights[Constants.ARR_3RD_INDEX], 0.1);

        final double areaCmSquared = Geometry.areaOfTriangleWithSSS(sideA, sideB, sideC);
        assertEquals(6, areaCmSquared, 0.1);

        final double perimeter = Geometry.perimeter(sideA, sideB, sideC);
        assertEquals(12, perimeter, 0.1);

        final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
        assertEquals(0.6435, angles[Constants.ALPHA_INDEX], 0.0001);
        assertEquals(0.9273, angles[Constants.BETA_INDEX], 0.0001);
    }

    @Test
    void testHeronFormulaUsingSemiperimeter() {
        // given
        final byte sideA = 12;
        final byte sideB = 5;
        final byte sideC = 13;
        // when
        final double areaCmSquared = Geometry.heronFormulaUsingSemiperimeter(sideA, sideB, sideC);
        // then
        assertEquals(30, areaCmSquared, 0.1);
    }

    @Test
    void testHeronFormulaUsingQuadProduct() {
        // given
        final byte sideA = 12;
        final byte sideB = 5;
        final byte sideC = 13;
        // when
        final double areaCmSquared = Geometry.heronFormulaUsingQuadProduct(sideA, sideB, sideC);
        // then
        assertEquals(30, areaCmSquared, 0.1);
    }

    @Test
    void testAreaWithBaseAndHeight() {
        // given
        final byte sideA = 12;
        final byte sideB = 5;
        final byte sideC = 13;
        final double[] heights = Geometry.scaleneTriangleHeight(sideA, sideB, sideC);
        final double height = heights[Constants.ARR_3RD_INDEX];
        // when
        final double areaCmSquared = Geometry.areaWithBaseAndHeight(sideC, height);
        // then
        assertEquals(30, areaCmSquared, 0.1);
    }

    static List<Arguments> isEquilateralTriangleArgs() {
        return List.of(
            Arguments.of(new double[]{12, 5, 13}, false),
            Arguments.of(new double[]{5, 5, 5}, true)
        );
    }

    @ParameterizedTest
    @MethodSource("isEquilateralTriangleArgs")
    void testIsEquilateralTriangle(double[] sides, boolean expectedResult) {
        // given
        final double sideA = sides[Constants.ARR_1ST_INDEX];
        final double sideB = sides[Constants.ARR_2ND_INDEX];
        final double sideC = sides[Constants.ARR_3RD_INDEX];
        // when
        final boolean equilateral = Geometry.isEquilateralTriangle(sideA, sideB, sideC);
        // then
        assertEquals(expectedResult, equilateral);
    }

    static List<Arguments> isScaleneTriangleArgs() {
        return List.of(
            Arguments.of(new double[]{12, 5, 13}, true),
            Arguments.of(new double[]{5, 5, 5}, false)
        );
    }

    @ParameterizedTest
    @MethodSource("isScaleneTriangleArgs")
    void testIsScaleneTriangle(double[] sides, boolean expectedResult) {
        // given
        final double sideA = sides[Constants.ARR_1ST_INDEX];
        final double sideB = sides[Constants.ARR_2ND_INDEX];
        final double sideC = sides[Constants.ARR_3RD_INDEX];
        // when
        final boolean equilateral = Geometry.isScaleneTriangle(sideA, sideB, sideC);
        // then
        assertEquals(expectedResult, equilateral);
    }

    private static double[] acuteIsoscelesTriangleAngles() {
        return new double[]{60, 60, 60};
    }

    private static double[] rightScaleneTriangleAngles() {
        return new double[]{30, 60, 90};
    }

    private static double[] rightIsoscelesTriangleAngles() {
        return new double[]{45, 45, 90};
    }

    private static double[] obtuseScaleneTriangleAngles() {
        return new double[]{30, 45, 105};
    }

    static List<Arguments> isAcuteTriangleArgs() {
        return List.of(
            Arguments.of(acuteIsoscelesTriangleAngles(), true),
            Arguments.of(obtuseScaleneTriangleAngles(), false),
            Arguments.of(rightScaleneTriangleAngles(), false),
            Arguments.of(rightIsoscelesTriangleAngles(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isAcuteTriangleArgs")
    void testIsAcuteTriangle(double[] angles, boolean expectedResult) {
        // given
        final double angleAlphaRad = Math.toRadians(angles[Constants.ALPHA_INDEX]);
        final double angleBetaRad = Math.toRadians(angles[Constants.BETA_INDEX]);
        final double angleGammaRad = Math.toRadians(angles[Constants.GAMMA_INDEX]);
        // when
        final boolean acute = Geometry.isAcuteTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
        // then
        assertEquals(expectedResult, acute);
    }

    private static double[] acuteScaleneTriangleWithSSA() {
        return new double[]{45, 4, 5};
    }

    private static double[] rightScaleneTriangleWithSSA() {
        return new double[]{90, 3, 4};
    }

    private static double[] obtuseScaleneTriangleWithSSA() {
        return new double[]{30, 3, 4};
    }

    static List<Arguments> isAcuteTriangleWithSSAArgs() {
        return List.of(
            Arguments.of(acuteScaleneTriangleWithSSA(), true),
            Arguments.of(obtuseScaleneTriangleWithSSA(), false),
            Arguments.of(rightScaleneTriangleWithSSA(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isAcuteTriangleWithSSAArgs")
    void testIsAcuteTriangleWithSSAArgs(double[] data, boolean expectedResult) {
        // given
        final double angleAlphaRad = Math.toRadians(data[Constants.ALPHA_INDEX]);
        final double sideA = data[Constants.ARR_2ND_INDEX];
        final double sideB = data[Constants.ARR_3RD_INDEX];
        // when
        final boolean acute = Geometry.isAcuteTriangleWithSSA(angleAlphaRad, sideA, sideB);
        // then
        assertEquals(expectedResult, acute);
    }

    static List<Arguments> isRightTriangleArgs() {
        return List.of(
            Arguments.of(acuteIsoscelesTriangleAngles(), false),
            Arguments.of(obtuseScaleneTriangleAngles(), false),
            Arguments.of(rightScaleneTriangleAngles(), true),
            Arguments.of(rightIsoscelesTriangleAngles(), true)
        );
    }

    @ParameterizedTest
    @MethodSource("isRightTriangleArgs")
    void testIsRightTriangle(double[] angles, boolean expectedResult) {
        // given
        final double angleAlphaRad = Math.toRadians(angles[Constants.ALPHA_INDEX]);
        final double angleBetaRad = Math.toRadians(angles[Constants.BETA_INDEX]);
        final double angleGammaRad = Math.toRadians(angles[Constants.GAMMA_INDEX]);
        // when
        final boolean right = Geometry.isRightTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
        // then
        assertEquals(expectedResult, right);
    }

    static List<Arguments> isRightTriangleWithSSAArgs() {
        return List.of(
            Arguments.of(acuteScaleneTriangleWithSSA(), false),
            Arguments.of(obtuseScaleneTriangleWithSSA(), false),
            Arguments.of(rightScaleneTriangleWithSSA(), true)
        );
    }

    @ParameterizedTest
    @MethodSource("isRightTriangleWithSSAArgs")
    void testIsRightTriangleWithSSA(double[] data, boolean expectedResult) {
        // given
        final double angleAlphaRad = Math.toRadians(data[Constants.ALPHA_INDEX]);
        final double sideA = data[Constants.ARR_2ND_INDEX];
        final double sideB = data[Constants.ARR_3RD_INDEX];
        // when
        final boolean right = Geometry.isRightTriangleWithSSA(angleAlphaRad, sideA, sideB);
        // then
        assertEquals(expectedResult, right);
    }

    static List<Arguments> isObtuseTriangleArgs() {
        return List.of(
            Arguments.of(acuteIsoscelesTriangleAngles(), false),
            Arguments.of(obtuseScaleneTriangleAngles(), true),
            Arguments.of(rightScaleneTriangleAngles(), false),
            Arguments.of(rightIsoscelesTriangleAngles(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isObtuseTriangleArgs")
    void testIsObtuseTriangle(double[] angles, boolean expectedResult) {
        // given
        final double angleAlphaRad = Math.toRadians(angles[Constants.ALPHA_INDEX]);
        final double angleBetaRad = Math.toRadians(angles[Constants.BETA_INDEX]);
        final double angleGammaRad = Math.toRadians(angles[Constants.GAMMA_INDEX]);
        // when
        final boolean obtuse = Geometry.isObtuseTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
        // then
        assertEquals(expectedResult, obtuse);
    }

    static List<Arguments> isObtuseTriangleWithSSAArgs() {
        return List.of(
            Arguments.of(acuteScaleneTriangleWithSSA(), false),
            Arguments.of(obtuseScaleneTriangleWithSSA(), true),
            Arguments.of(rightScaleneTriangleWithSSA(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isObtuseTriangleWithSSAArgs")
    void testIsObtuseTriangleWithSSA(double[] data, boolean expectedResult) {
        // given
        final double angleAlphaRad = Math.toRadians(data[Constants.ALPHA_INDEX]);
        final double sideA = data[Constants.ARR_2ND_INDEX];
        final double sideB = data[Constants.ARR_3RD_INDEX];
        // when
        final boolean obtuse = Geometry.isObtuseTriangleWithSSA(angleAlphaRad, sideA, sideB);
        // then
        assertEquals(expectedResult, obtuse);
    }

    static List<Arguments> complementaryAngleArgs() {
        return List.of(
            Arguments.of(Math.toRadians(15), Math.toRadians(75)),
            Arguments.of(Math.toRadians(30), Math.toRadians(60)),
            Arguments.of(Math.toRadians(45), Math.toRadians(45)),
            Arguments.of(Math.toRadians(60), Math.toRadians(30)),
            Arguments.of(Math.toRadians(75), Math.toRadians(15)),
            Arguments.of(Math.toRadians(90), Math.toRadians(0))
        );
    }

    @ParameterizedTest
    @MethodSource("complementaryAngleArgs")
    void testComplementaryAngle(double angleRadians, double expectedResult) {
        // when
        final double complementaryAngle = Geometry.complementaryAngle(angleRadians);
        // then
        assertEquals(expectedResult, complementaryAngle, 0.000001);
    }

    static List<Arguments> isComplementaryAngleArgs() {
        return List.of(
            Arguments.of(Math.toRadians(15), Math.toRadians(75), true),
            Arguments.of(Math.toRadians(30), Math.toRadians(60), true),
            Arguments.of(Math.toRadians(45), Math.toRadians(45), true),
            Arguments.of(Math.toRadians(60), Math.toRadians(30), true),
            Arguments.of(Math.toRadians(75), Math.toRadians(15), true),
            Arguments.of(Math.toRadians(60), Math.toRadians(15), false),
            Arguments.of(Math.toRadians(60), Math.toRadians(35), false),
            Arguments.of(Math.toRadians(60), Math.toRadians(45), false),
            Arguments.of(Math.toRadians(90), Math.toRadians(10), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isComplementaryAngleArgs")
    void testIsComplementaryAngle(double angleAlphaRadians, double angleBetaRadians, boolean expectedResult) {
        // when
        final boolean complementary = Geometry
            .isComplementaryAngle(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(expectedResult, complementary);
    }

    @Test
    void testSupplementaryAngle() {
        // given
        final double angleRadians = Math.toRadians(30);
        // when
        final double supplementaryAngle = Geometry.supplementaryAngle(angleRadians);
        // then
        assertEquals(2.618, supplementaryAngle, 0.001); // 150°
    }

    static List<Arguments> isSupplementaryAngleArgs() {
        return List.of(
            Arguments.of(Math.toRadians(60), Math.toRadians(40), false),
            Arguments.of(Math.toRadians(60), Math.toRadians(60), false),
            Arguments.of(Math.toRadians(120), Math.toRadians(60), true),
            Arguments.of(Math.toRadians(45), Math.toRadians(135), true),
            Arguments.of(Math.toRadians(90), Math.toRadians(90), true)
        );
    }

    @ParameterizedTest
    @MethodSource("isSupplementaryAngleArgs")
    void testIsSupplementaryAngle(double angleAlphaRadians, double angleBetaRadians, boolean expectedResult) {
        // when
        final boolean supplementary = Geometry
            .isSupplementaryAngles(angleAlphaRadians, angleBetaRadians);
        // then
        assertEquals(expectedResult, supplementary);
    }

    static List<Arguments> coterminalAngleArgs() {
        return List.of(
            Arguments.of(Math.toRadians(420), Math.toRadians(60)),
            Arguments.of(Math.toRadians(-858), Math.toRadians(222))
        );
    }

    @ParameterizedTest
    @MethodSource("coterminalAngleArgs")
    void testCoterminalAngle(double angleRadians, double expectedResult) {
        // when
        final double coterminalAngle = Geometry.coterminalAngle(angleRadians);
        // then
        assertEquals(expectedResult, coterminalAngle, 0.000001);
    }

    @Test
    void testCoterminalAngles() {
        // given
        final double angleRadians = Math.toRadians(45);
        final byte min = -4;
        final byte max = 4;
        // when
        final double[] coterminalAngles = Geometry.coterminalAngles(angleRadians, min, max);
        // then
        assertNotNull(coterminalAngles);
        assertEquals(8, coterminalAngles.length);
        final double delta = 0.000001;
        assertEquals(Math.toRadians(-1395), coterminalAngles[Constants.ARR_1ST_INDEX], delta);
        assertEquals(Math.toRadians(-1035), coterminalAngles[Constants.ARR_2ND_INDEX], delta);
        assertEquals(Math.toRadians(-675), coterminalAngles[Constants.ARR_3RD_INDEX], delta);
        assertEquals(Math.toRadians(-315), coterminalAngles[Constants.ARR_4TH_INDEX], delta);
        assertEquals(Math.toRadians(405), coterminalAngles[Constants.ARR_5TH_INDEX], delta);
        assertEquals(Math.toRadians(765), coterminalAngles[Constants.ARR_6TH_INDEX], delta);
        assertEquals(Math.toRadians(1125), coterminalAngles[Constants.ARR_7TH_INDEX], delta);
        assertEquals(Math.toRadians(1485), coterminalAngles[Constants.ARR_8TH_INDEX], delta);
    }

    static List<Arguments> areCoterminalAngleArgs() {
        return List.of(
            Arguments.of(Math.toRadians(-170), Math.toRadians(550), true),
            Arguments.of(Math.toRadians(-170), Math.toRadians(540), false)
        );
    }

    @ParameterizedTest
    @MethodSource("areCoterminalAngleArgs")
    void testAreCoterminalAngle(double angleAlphaRad, double angleBetaRad, boolean expectedResult) {
        // given
        final byte rotations = 2;
        // when
        final boolean coterminalAngle = Geometry
            .areCoterminalAngles(angleAlphaRad, angleBetaRad, rotations);
        // then
        assertEquals(expectedResult, coterminalAngle);
    }

    static List<Arguments> referenceAngleArgs() {
        return List.of(
            Arguments.of(Math.toRadians(0), Math.toRadians(0)),
            Arguments.of(Math.toRadians(30), Math.toRadians(30)),
            Arguments.of(Math.toRadians(90), Math.toRadians(90)),
            Arguments.of(Math.toRadians(120), Math.toRadians(60)),
            Arguments.of(Math.toRadians(180), Math.toRadians(0)),
            Arguments.of(Math.toRadians(210), Math.toRadians(30)),
            Arguments.of(Math.toRadians(270), Math.toRadians(90)),
            Arguments.of(Math.toRadians(300), Math.toRadians(60)),
            Arguments.of(Trigonometry.PI2, Math.toRadians(0)),
            Arguments.of(Math.toRadians(610), Math.toRadians(70))
        );
    }

    @ParameterizedTest
    @MethodSource("referenceAngleArgs")
    void testReferenceAngle(double angleRadians, double expectedResult) {
        // when
        final double referenceAngle = Geometry.referenceAngle(angleRadians);
        // then
        assertEquals(expectedResult, referenceAngle, 0.000001);
    }

    @Test
    void testCentralAngleGivenArcLengthRadius() {
        final short arcLengthInMeters = 235;
        final double radiusInMeters = 149.6;
        // when
        final double centralAngle = Geometry
            .centralAngleGivenArcLengthRadius(arcLengthInMeters, radiusInMeters);
        // then
        assertEquals(Trigonometry.PI_OVER_2, centralAngle, 0.0001);
    }

    @Test
    void testArcLength() {
        final double centralAngleRad = Trigonometry.PI_OVER_2;
        final double radiusInMeters = 149.6;
        // when
        final double arcLengthInMeters = Geometry.arcLength(centralAngleRad, radiusInMeters);
        // then
        assertEquals(235, arcLengthInMeters, 0.1);
    }

    @Test
    void testClockAngle() {
        final byte hours = 7;
        final byte minutes = 0;
        // when
        final double[] angles = Geometry.clockAngle(hours, minutes);
        // then
        assertNotNull(angles);
        assertEquals(2, angles.length);
        assertEquals(Math.toRadians(150), angles[Constants.ARR_1ST_INDEX], DELTA6);
        assertEquals(Math.toRadians(210), angles[Constants.ARR_2ND_INDEX], DELTA6);
    }


    static List<Arguments> manhattanDistanceParams() {
        return List.of(
            Arguments.of(new double[]{2}, new double[]{3}, 1), // 1d
            Arguments.of(new double[]{2, 9}, new double[]{3, 5}, 5), // 2d
            Arguments.of(new double[]{2, 9, 4}, new double[]{3, 5, 6}, 7), // 3d
            Arguments.of(new double[]{2, 9, 4, 1}, new double[]{3, 5, 6, 7}, 13) // 4d
        );
    }

    @ParameterizedTest
    @MethodSource("manhattanDistanceParams")
    void testManhattanDistance(double[] vectorA, double[] vectorB, double expectedResult) {
        // when
        final double distance = Geometry.manhattanDistance(vectorA, vectorB);
        // then
        assertEquals(expectedResult, distance, 0.1);
    }

    @Test
    void testCartesianToCylindricalCoordinates() {
        // given
        final double[] cartesianCoords = {2, 5, 3};
        // when
        final double[] cylindricalCoords = Geometry.cartesianToCylindricalCoordinates(cartesianCoords);
        // then
        assertNotNull(cylindricalCoords);
        assertEquals(3, cylindricalCoords.length);
        assertEquals(5.385, cylindricalCoords[Constants.R_INDEX], 0.001);
        assertEquals(1.1903, cylindricalCoords[Constants.THETA_INDEX], 0.0001);
        assertEquals(3, cylindricalCoords[Constants.Z_INDEX], 0.1);
    }

    @Test
    void testCylindricalToCartesianCoordinates() {
        // given
        final double[] cylindricalCoords = {5.385, 1.1903, 3};
        // when
        final double[] cartesianCoords = Geometry.cylindricalToCartesianCoordinates(cylindricalCoords);
        // then
        assertNotNull(cartesianCoords);
        assertEquals(3, cartesianCoords.length);
        assertEquals(2, cartesianCoords[Constants.X_INDEX], 0.1);
        assertEquals(5, cartesianCoords[Constants.Y_INDEX], 0.1);
        assertEquals(3, cartesianCoords[Constants.Z_INDEX], 0.1);
    }

    @Test
    void testCartesianToPolarCoordinates() {
        // given
        final double[] cartesianCoords = {2, 3};
        // when
        final double[] polarCoords = Geometry.cartesianToPolarCoordinates(cartesianCoords);
        // then
        assertNotNull(polarCoords);
        assertEquals(2, polarCoords.length);
        assertEquals(3.6056, polarCoords[Constants.R_INDEX], 0.0001);
        assertEquals(0.9828, polarCoords[Constants.THETA_INDEX], 0.0001);
    }

    @Test
    void testPolarToCartesianCoordinates() {
        // given
        final double[] polarCoords = {3.6056, 0.9828};
        // when
        final double[] cartesianCoords = Geometry.polarToCartesianCoordinates(polarCoords);
        // then
        assertNotNull(cartesianCoords);
        assertEquals(2, cartesianCoords.length);
        assertEquals(2, cartesianCoords[Constants.X_INDEX], 0.1);
        assertEquals(3, cartesianCoords[Constants.Y_INDEX], 0.1);
    }

    @Test
    void testCartesianToSphericalCoordinates() {
        // given
        final double[] cartesianCoords = {2, 5, 3};
        // when
        final double[] sphericalCoords = Geometry.cartesianToSphericalCoordinates(cartesianCoords);
        // then
        assertNotNull(sphericalCoords);
        assertEquals(3, sphericalCoords.length);
        assertEquals(6.164, sphericalCoords[Constants.R_INDEX], 0.001);
        assertEquals(1.0625, sphericalCoords[Constants.THETA_INDEX], 0.0001);
        assertEquals(1.1903, sphericalCoords[Constants.PHI_INDEX], 0.0001);
    }

    @Test
    void testSphericalToCartesianCoordinates() {
        // given
        final double[] sphericalCoords = {6.164, 1.0625, 1.1903};
        // when
        final double[] cartesianCoords = Geometry.sphericalToCartesianCoordinates(sphericalCoords);
        // then
        assertNotNull(cartesianCoords);
        assertEquals(3, cartesianCoords.length);
        assertEquals(2, cartesianCoords[Constants.X_INDEX], 0.1);
        assertEquals(5, cartesianCoords[Constants.Y_INDEX], 0.1);
        assertEquals(3, cartesianCoords[Constants.Z_INDEX], 0.1);
    }

    static List<Arguments> distanceBetween2pointsParams() {
        return List.of(
            Arguments.of(new double[]{3}, new double[]{9}, 6, 0.1), // 1d
            Arguments.of(new double[]{3, 5}, new double[]{9, 15}, 11.6619, 0.0001), // 2d
            Arguments.of(new double[]{3, 5, 2}, new double[]{9, 15, 5}, 12.0416, 0.0001), // 3d
            Arguments.of(new double[]{3, 5, 2, 3}, new double[]{9, 15, 5, 1}, 12.20656, 0.00001) // 4d
        );
    }

    @ParameterizedTest
    @MethodSource("distanceBetween2pointsParams")
    void testDistanceBetween2points(
        double[] pointACoords, double[] pointBCoords, double expectedResult, double delta) {
        // when
        final double distance = Geometry.distance(pointACoords, pointBCoords);
        // then
        assertEquals(expectedResult, distance, delta);
    }

    @Test
    void testDistanceBetween3points() {
        // given
        final double[] pointACoords = {3, 5, 2};
        final double[] pointBCoords = {9, 15, 5};
        final double[] pointCCoords = {2, 7, 1};
        // when
        final double abDistance = Geometry.distance(pointACoords, pointBCoords);
        final double bcDistance = Geometry.distance(pointBCoords, pointCCoords);
        final double acDistance = Geometry.distance(pointACoords, pointCCoords);
        // then
        assertEquals(12.0416, abDistance, 0.0001);
        assertEquals(11.35782, bcDistance, 0.00001);
        assertEquals(2.44949, acDistance, 0.00001);
    }

    @Test
    void testDistanceBetweenPointsAndStraightLine() {
        // given
        final double[] pointCoords = {3, 5};
        final int lineSlope = 2;
        final int lineYIntercept = 6;
        // when
        final double distance = Geometry
            .distanceBetweenPointsAndStraightLine(pointCoords, lineSlope, lineYIntercept);
        // then
        assertEquals(3.130495, distance, 0.000001);
    }

    @Test
    void testDistanceBetweenParallelLines() {
        // given
        final int slope = 2;
        final int line1YIntercept = 6;
        final int line2YIntercept = 8;
        // when
        final double distance = Geometry
            .distanceBetweenParallelLines(slope, line1YIntercept, line2YIntercept);
        // then
        assertEquals(0.894427, distance, DELTA6);
    }

    @Test
    void testRotation() {
        // given
        final double[] pointCoords = {3, 4};
        final double angleTheta = Math.toRadians(60);
        // when
        final double[] resultCoords = Geometry.rotation(pointCoords, angleTheta);
        // then
        assertNotNull(resultCoords);
        assertEquals(2, resultCoords.length);
        assertEquals(-1.964, resultCoords[Constants.X_INDEX], DELTA3);
        assertEquals(4.598, resultCoords[Constants.Y_INDEX], DELTA3);
    }

    @Test
    void testRotationAroundPoint() {
        // given
        final double[] pointCoords = {3, 4};
        final double angleTheta = Math.toRadians(60);
        final double[] pivotCoords = {1, 2};
        // when
        final double[] resultCoords = Geometry.rotationAroundPoint(pointCoords, pivotCoords, angleTheta);
        // then
        assertNotNull(resultCoords);
        assertEquals(2, resultCoords.length);
        assertEquals(0.26795, resultCoords[Constants.X_INDEX], DELTA5);
        assertEquals(4.732, resultCoords[Constants.Y_INDEX], DELTA3);
    }

    @Test
    void testSlope() {
        // given
        final double[] pointACoords = {1, 5};
        final double[] pointBCoords = {7, 6};
        // when
        final double slope = Geometry.slope(pointACoords, pointBCoords);
        final double angleTheta = Math.atan(slope);
        final double distance = Geometry.distance(pointACoords, pointBCoords);
        final double deltaX = Geometry.deltaDistance(
            pointBCoords[Constants.X_INDEX], pointACoords[Constants.X_INDEX]);
        final double deltaY = Geometry.deltaDistance(
            pointBCoords[Constants.Y_INDEX], pointACoords[Constants.Y_INDEX]);
        final double constantTerm = Geometry.slopeInterceptConstantTerm(
            pointACoords[Constants.X_INDEX], pointACoords[Constants.Y_INDEX], slope);
        // then
        assertEquals(0.166667, slope, DELTA6);
        assertEquals(0.16515, angleTheta, DELTA5);
        assertEquals(6.0828, distance, DELTA4);
        assertEquals(6, deltaX, DELTA1);
        assertEquals(1, deltaY, DELTA1);
        assertEquals(4.833, constantTerm, DELTA3);
    }

    @Test
    void testSlopeFromKnownIntercepts() {
        // given
        final int xIntercept = 2;
        final int yIntercept = -3;
        // when
        final double slope = Geometry.slopeFromKnownIntercepts(xIntercept, yIntercept);
        // then
        assertEquals(1.5, slope, DELTA1);
    }

    @Test
    void testAreaUnderSlope() {
        // given
        final int x1 = 1;
        final int x2 = 7;
        final double slope = 0.166667;
        // when
        final double area = Geometry.areaUnderSlope(x1, x2, slope);
        // then
        assertEquals(3.000006, area, DELTA6);
    }

    @Test
    void testIntercept() {
        // given
        final int a = 2;
        final int b = 3;
        final int c = -2;
        // when
        final double[] intercepts = Geometry.intercept(a, b, c);
        final double slope = Geometry.slope(a, b);
        // then
        assertNotNull(intercepts);
        assertEquals(2, intercepts.length);
        assertEquals(1, intercepts[Constants.X_INDEX], DELTA1);
        assertEquals(0.6667, intercepts[Constants.Y_INDEX], DELTA4);

        assertEquals(-0.6667, slope, DELTA4);
    }

    @Test
    void testInterceptWithKnownSlopeAndConstantTerm() {
        // given
        final int slopeTerm = 2;
        final int constantTerm = -2;
        // when
        final double[] intercepts = Geometry.intercept(slopeTerm, constantTerm);
        // then
        assertNotNull(intercepts);
        assertEquals(2, intercepts.length);
        assertEquals(1, intercepts[Constants.X_INDEX], DELTA1);
        assertEquals(-2, intercepts[Constants.Y_INDEX], DELTA4);
    }

    @Test
    void testLinearInterpolation() {
        // given
        final double[] pointACoords = {200, 15};
        final double[] pointBCoords = {300, 20};
        final int midpointX = 250;
        // when
        final double midpointY = Geometry
            .linearInterpolation(pointACoords, pointBCoords, midpointX);
        final double slope = Geometry.slope(pointACoords, pointBCoords);
        final double deltaY = Geometry.deltaDistance(
            pointBCoords[Constants.Y_INDEX], pointACoords[Constants.Y_INDEX]);
        // then
        assertEquals(17.5, midpointY, DELTA1);
        assertEquals(0.05, slope, DELTA1);
        assertEquals(5, deltaY, DELTA1);
    }

    @Test
    void testMidpoint() {
        // given
        final double[] pointACoords = {0, 2};
        final double[] pointBCoords = {2, 8};
        // when
        final double[] midpointCoords = Geometry.midpoint(pointACoords, pointBCoords);
        // then
        assertNotNull(midpointCoords);
        assertEquals(2, midpointCoords.length);
        assertEquals(1, midpointCoords[Constants.X_INDEX], DELTA1);
        assertEquals(5, midpointCoords[Constants.Y_INDEX], DELTA1);
    }

    @Test
    void testEndpointWithGivenMidpoint() {
        // given
        final byte point = 2;
        final byte midpoint = 5;
        // when
        final double coordinate = Geometry.endpointWithGivenMidpoint(point, midpoint);
        // then
        assertEquals(8, coordinate, DELTA1);
    }

    @Test
    void testCrossSectionalAreaOfHollowRectangle() {
        // given
        final byte width = 10; // cm
        final byte height = 25; // cm
        final byte thickness = 3; // cm
        // when
        final double area = Geometry.crossSectionalAreaOfHollowRectangle(width, height, thickness);
        // then
        assertEquals(174, area, DELTA1); // cm²
    }

    @Test
    void testCrossSectionalAreaOfRectangle() {
        // given
        final byte width = 10; // cm
        final byte height = 25; // cm
        // when
        final double area = Geometry.crossSectionalAreaOfRectangle(width, height);
        // then
        assertEquals(250, area, DELTA1); // cm²
    }

    @Test
    void testCrossSectionalAreaOfISection() {
        // given
        final byte width = 1; // m
        final byte height = 2; // m
        final double thickness1 = 0.5; // m
        final double thickness2 = 0.6; // m
        // when
        final double area = Geometry.crossSectionalAreaOfISection(width, height, thickness1, thickness2);
        // then
        assertEquals(1.6, area, DELTA1); // m²
    }

    @Test
    void testCrossSectionalAreaOfCSection() {
        // given
        final byte width = 120; // cm
        final short height = 170; // cm
        final byte thickness1 = 14; // cm
        final byte thickness2 = 10; // cm
        // when
        final double area = Geometry.crossSectionalAreaOfCSection(width, height, thickness1, thickness2);
        // then
        assertEquals(4780, area, DELTA1); // cm²
    }

    @Test
    void testCrossSectionalAreaOfTSection() {
        // given
        final double width = 0.23; // m
        final double height = 0.4; // m
        final double thickness1 = 0.1; // m
        final double thickness2 = 0.3; // m
        // when
        final double area = Geometry.crossSectionalAreaOfTSection(width, height, thickness1, thickness2);
        // then
        assertEquals(0.113, area, DELTA3); // m²
    }

    @Test
    void testCrossSectionalAreaOfLSection() {
        // given
        final double width = 0.17; // m
        final double height = 0.2; // m
        final double thickness = 0.05; // m
        // when
        final double area = Geometry.crossSectionalAreaOfLSection(width, height, thickness);
        // then
        assertEquals(0.016, area, DELTA3); // m²
    }

    @Test
    void testCrossSectionalAreaOfIsoscelesTriangle() {
        // given
        final byte base = 10; // m
        final byte height = 26; // m
        // when
        final double area = Geometry.crossSectionalAreaOfIsoscelesTriangle(base, height);
        // then
        assertEquals(130, area, DELTA1); // m²
    }

    @Test
    void testCrossSectionalAreaOfEquilateralTriangle() {
        // given
        final byte side = 3; // m
        // when
        final double area = Geometry.crossSectionalAreaOfEquilateralTriangle(side);
        // then
        assertEquals(3.897, area, DELTA3); // m²
    }

    @Test
    void testCrossSectionalAreaOfCircle() {
        // given
        final byte diameter = 4; // m
        // when
        final double area = Geometry.crossSectionalAreaOfCircle(diameter);
        // then
        assertEquals(12.566, area, DELTA3); // m²
    }

    @Test
    void testCrossSectionalAreaOfTube() {
        // given
        final byte diameter = 10; // mm
        final byte thickness = 1; // mm
        // when
        final double area = Geometry.crossSectionalAreaOfTube(diameter, thickness);
        // then
        assertEquals(28.274, area, DELTA3); // mm²
    }

    @Test
    void testLeastSquaresRegressionLine() {
        // given
        final double[] independentVariables = new double[]{0, 2, 4};
        final double[] dependentVariables = new double[]{1, 4, 4};
        // when
        final double[] fittedModel = Geometry.leastSquaresRegressionLine(independentVariables, dependentVariables);
        // then
        assertArrayEquals(new double[]{0.75, 1.5}, fittedModel, DELTA2);
    }
}
