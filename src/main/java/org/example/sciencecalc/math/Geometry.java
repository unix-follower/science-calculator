package org.example.sciencecalc.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.example.sciencecalc.math.Algebra.squareRoot;
import static org.example.sciencecalc.math.Arithmetic.ONE_FOURTH;
import static org.example.sciencecalc.math.Arithmetic.ONE_HALF;
import static org.example.sciencecalc.math.LinAlgUtils.check2dSize;
import static org.example.sciencecalc.math.NumberUtils.checkGreater;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;

public final class Geometry {
    private static final String TRIANGLE_SSS_ERR_MSG = "Side length (%s) must be less than the sum " +
        "of the other two sides to form a triangle";

    private Geometry() {
    }

    /**
     * @return π = circumference / diameter
     */
    public static double pi(double circumference, double diameter) {
        return circumference / diameter;
    }

    /**
     * @return a = πr² = π × (d / 2)²
     */
    public static double circleAreaOfDiameter(double diameter) {
        final double radius = diameter / 2;
        return Math.PI * radius * radius;
    }

    /**
     * @return a = c² / 4π
     */
    public static double circleAreaOfCircumference(double circumference) {
        return circumference * circumference / Trigonometry.PI4;
    }

    /**
     * @return r² * cos⁻¹((r-h)/r) - (r-h) * √(2rh-h²)
     */
    public static double circularSegmentArea(double radius, double height) {
        final double diff = radius - height;
        return radius * radius * Trigonometry.cosInverse(diff / radius) - diff
            * squareRoot(2 * radius * height - (height * height));
    }

    /**
     * @return c = πr
     */
    public static double circleCircumference(double radius) {
        return 2 * Math.PI * radius;
    }

    /**
     * @return c = πd
     */
    public static double circleCircumferenceOfDiameter(double diameter) {
        return Math.PI * diameter;
    }

    /**
     * @return c = 2√(πa)
     */
    public static double circleCircumferenceOfArea(double area) {
        return 2 * Math.sqrt(Math.PI * area);
    }

    /**
     * @return d = 2r
     */
    public static double circleDiameter(double radius) {
        return 2 * radius;
    }

    /**
     * @return d = c / π
     */
    public static double circleDiameterOfCircumference(double circumference) {
        return circumference / Math.PI;
    }

    /**
     * @return d = 2√(a / π)
     */
    public static double circleDiameterOfArea(double area) {
        return 2 * Math.sqrt(area / Math.PI);
    }

    /**
     * @return r = d / 2
     */
    public static double circleRadius(double diameter) {
        return diameter / 2;
    }

    /**
     * @return r = c / 2π
     */
    public static double circleRadiusOfCircumference(double circumference) {
        return circumference / (Math.PI * 2);
    }

    /**
     * @return r = √(a / π)
     */
    public static double circleRadiusOfArea(double area) {
        return Math.sqrt(area / Math.PI);
    }

    /**
     * @return A = (y₁ − y₀)/(x₁ − x₀)
     */
    public static double averageRateOfChange(double startPointX, double startPointY,
                                             double endpointX, double endpointY) {
        if (startPointX == endpointX) {
            throw new IllegalArgumentException("The x-values must not be the same to avoid division by zero.");
        }
        return (endpointY - startPointY) / (endpointX - startPointX);
    }

    // Triangle calculators

    /**
     * If the shorter leg length 'a' is known:
     * b = a√3
     * c = 2a
     *
     * @return [a, b, c]
     */
    public static double[] triangle306090SolveWithA(double sideA) {
        return new double[]{sideA, sideA * Math.sqrt(3), 2 * sideA};
    }

    /**
     * If the longer leg length b is known:
     * a = b√3/3
     * c = 2b√3/3
     *
     * @return [a, b, c]
     */
    public static double[] triangle306090SolveWithB(double sideB) {
        final double sqrtResult = Math.sqrt(3) / 3;
        return new double[]{sideB * sqrtResult, sideB, 2 * sideB * sqrtResult};
    }

    /**
     * If the hypotenuse c is known:
     * a = c/2
     * b = c√3/2
     *
     * @return [a, b, c]
     */
    public static double[] triangle306090SolveWithC(double sideC) {
        final double sideA = sideC / 2;
        return new double[]{sideA, sideA * Math.sqrt(3), sideC};
    }

    /**
     * Given three sides (SSS). Heron's formula.
     *
     * @return h = 0.5/b * √((a + b + c) * (-a + b + c) * (a - b + c) * (a + b - c)). The units are cm²
     */
    public static double heightOfTriangleWithSSS(
        double targetSide, double sideLengthA, double sideLengthB, double sideLengthC) {
        final double abSum = sideLengthA + sideLengthB;

        return ONE_HALF / targetSide * Math.sqrt(
            (abSum + sideLengthC)
                * (-sideLengthA + sideLengthB + sideLengthC)
                * (sideLengthA - sideLengthB + sideLengthC)
                * (abSum - sideLengthC)
        );
    }

    /**
     * The basic formulation of the Heron's formula.
     *
     * @return A = √(s(s-a)(s-b)(s-c)). The units are cm²
     */
    public static double heronFormulaUsingSemiperimeter(double sideA, double sideB, double sideC) {
        final double semiperimeter = semiperimeter(sideA, sideB, sideC);
        return Math.sqrt(
            semiperimeter * (semiperimeter - sideA) * (semiperimeter - sideB) * (semiperimeter - sideC)
        );
    }

    /**
     * The Heron's formula w/o semiperimeter
     *
     * @return A = ¼ * √(4a²b² - (a² + b² − c²)²). The units are cm²
     */
    public static double heronFormulaUsingQuadProduct(double sideA, double sideB, double sideC) {
        final double aSquared = sideA * sideA;
        final double bSquared = sideB * sideB;
        final double cSquared = sideC * sideC;
        return ONE_FOURTH * Math.sqrt(4 * aSquared * bSquared - Math.pow(aSquared + bSquared - cSquared, 2));
    }

    /**
     * @return A = (ch)/2. The units are cm²
     */
    public static double areaWithBaseAndHeight(double base, double height) {
        return base * height / 2;
    }

    public static double[] scaleneTriangleHeight(double sideA, double sideB, double sideC) {
        final double sideAHeight = heightOfTriangleWithSSS(sideA, sideA, sideB, sideC);
        final double sideBHeight = heightOfTriangleWithSSS(sideB, sideA, sideB, sideC);
        final double sideCHeight = heightOfTriangleWithSSS(sideC, sideA, sideB, sideC);
        return new double[]{sideAHeight, sideBHeight, sideCHeight};
    }

    /**
     * @return hΔ = a * √3/2. The units are cm²
     */
    public static double equilateralTriangleHeight(double sides) {
        return sides * Math.sqrt(3) / 2;
    }

    /**
     * @return area = √3/4 * a². The units are cm²
     */
    public static double equilateralTriangleArea(double sides) {
        return Math.sqrt(3) / 4 * sides * sides;
    }

    /**
     * hₐ = 2 * area/a = √(a²−(0.5*b)²) * b/a
     * hₐ = b * sin(β)
     * h_b = √(a²−(0.5*b)²)
     * The units are cm²
     */
    public static double[] isoscelesTriangleHeight(double sideA, double sideB) {
        final double heightB = Math.sqrt(sideA * sideA - Math.pow(0.5 * sideB, 2));
        return new double[]{heightB * sideB / sideA, heightB};
    }

    /**
     * @return area = (bh_b)/2. The units are cm²
     */
    public static double isoscelesTriangleArea(double base, double heightB) {
        return base * heightB / 2;
    }

    /**
     * The units are cm²
     */
    public static double[] rightTriangleHeight(double sideA, double sideB, double hypotenuse) {
        final double sideAHeight = heightOfTriangleWithSSS(sideA, sideA, sideB, hypotenuse);
        final double sideBHeight = heightOfTriangleWithSSS(sideB, sideA, sideB, hypotenuse);
        return new double[]{sideAHeight, sideBHeight, sideA * sideB / hypotenuse};
    }

    // 2D geometry

    /**
     * @return area = ½ * a * b. The units are cm²
     */
    public static double area(double sideA, double sideB) {
        return 0.5 * sideA * sideB;
    }

    /**
     * For isosceles triangle: 2a + b.
     *
     * @return perimeter = a + b + c. The units are cm
     */
    public static double perimeter(double sideA, double sideB, double hypotenuse) {
        return sideA + sideB + hypotenuse;
    }

    /**
     * @return P = b + 2y
     */
    public static double rectangularChannelPerimeter(double width, double height) {
        return width + 2 * height;
    }

    /**
     * @return 2πr
     */
    public static double circlePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }

    public static double semiperimeter(double sideA, double sideB, double hypotenuse) {
        return perimeter(sideA, sideB, hypotenuse) / 2;
    }

    /**
     * a² + b² = c²
     * <br/> Find a leg (a or b).
     *
     * @return a = √(c² - b²). The units are cm
     */
    public static double[] pythagoreanTheoremForRightTriangleWithLegAndHypotenuse(double side, double hypotenuse) {
        final double squaredSide = side * side;
        final double squaredHypotenuse = hypotenuse * hypotenuse;
        final double squaredSide2 = squaredHypotenuse - squaredSide;
        return new double[]{side, Math.sqrt(squaredSide2), hypotenuse};
    }

    /**
     * a² + b² = c²
     * For a non right-angled triangle:
     * <br/> Find a leg (a or b).
     *
     * @return √c² = a² + b² - 2ab * cos(γ). The units are cm
     */
    public static double[] pythagoreanTheoremWithLegsAndAngle(double sideA, double sideB, double angleGammaRad) {
        final double squaredSideA = sideA * sideA;
        final double squaredSideB = sideB * sideB;
        final double squaredUnkownSide = squaredSideA + squaredSideB - 2 * sideA * sideB * Math.cos(angleGammaRad);
        return new double[]{sideA, sideB, Math.sqrt(squaredUnkownSide)};
    }

    /**
     * a² + b² = c²
     * <br/> Find the hypotenuse (c).
     *
     * @return c = √(a² + b²). The units are cm
     */
    public static double[] pythagoreanTheoremForRightTriangleWithLegs(double sideA, double sideB) {
        final double squaredSideA = sideA * sideA;
        final double squaredSideB = sideB * sideB;
        final double squaredHypotenuse = squaredSideA + squaredSideB;
        return new double[]{sideA, sideB, Math.sqrt(squaredHypotenuse)};
    }

    /**
     * @return area = a². The units are cm²
     */
    public static double areaOfSquare(double sideLength) {
        checkGreater0(sideLength);
        return sideLength * sideLength;
    }

    /**
     * @return area = width * height. The units are cm² or m²
     */
    public static double rectangleArea(double width, double height) {
        checkGreater0(width);
        checkGreater0(height);
        return width * height;
    }

    /**
     * @return area = b * h / 2. The units are cm²
     */
    public static double areaOfTriangleWithBaseAndHeight(double base, double height) {
        checkGreater0(base);
        checkGreater0(height);
        return base * height / 2;
    }

    /**
     * Given three sides (SSS). Heron's formula.
     *
     * @return area = ¼ * √((a + b + c) * (-a + b + c) * (a - b + c) * (a + b - c)). The units are cm²
     */
    public static double areaOfTriangleWithSSS(double sideLengthA, double sideLengthB, double sideLengthC) {
        checkGreater0(sideLengthA);
        checkGreater0(sideLengthB);
        checkGreater0(sideLengthC);

        if (sideLengthA > sideLengthB + sideLengthC) {
            throw new IllegalArgumentException(String.format(TRIANGLE_SSS_ERR_MSG, "a"));
        }

        final double legsSum = sideLengthA + sideLengthC;
        if (legsSum < sideLengthB) {
            throw new IllegalArgumentException(String.format(TRIANGLE_SSS_ERR_MSG, "b"));
        }

        final double aAndBSum = sideLengthA + sideLengthB;
        if (aAndBSum == sideLengthC) {
            throw new IllegalArgumentException(String.format(TRIANGLE_SSS_ERR_MSG, "c"));
        }

        return ONE_FOURTH * Math.sqrt(
            (aAndBSum + sideLengthC)
                * (-sideLengthA + sideLengthB + sideLengthC)
                * (legsSum - sideLengthB)
                * (aAndBSum - sideLengthC)
        );
    }

    /**
     * Given two sides and the angle between them (SAS).
     *
     * @return area = ½ * a * b * sin(γ). The units are cm²
     */
    public static double areaOfTriangleWithSAS(double sideLengthA, double sideLengthB, double angleGammaRadians) {
        checkGreater0(sideLengthA);
        checkGreater0(sideLengthB);
        checkGreater0(angleGammaRadians);

        return ONE_HALF * sideLengthA * sideLengthB * Math.sin(angleGammaRadians);
    }

    /**
     * Given two angles and the side between them (ASA).
     *
     * @return area = a² * sin(β) * sin(γ) / (2 * sin(β + γ)). The units are cm²
     */
    public static double areaOfTriangleWithASA(
        double sideLengthA, double angleBetaRadians, double angleGammaRadians) {
        return sideLengthA * sideLengthA * Math.sin(angleBetaRadians) * Math.sin(angleGammaRadians)
            / (2 * Math.sin(angleBetaRadians + angleGammaRadians));
    }

    /**
     * @return area = πr². The units are cm²
     */
    public static double circleArea(double radius) {
        checkGreater0(radius);
        return Math.PI * radius * radius;
    }

    /**
     * @return A = π(R²−r²)
     */
    public static double hollowCircleArea(double outerRadius, double innerRadius) {
        checkGreater0(outerRadius);
        checkGreater(outerRadius, innerRadius);
        return Math.PI * (outerRadius * outerRadius - innerRadius * innerRadius);
    }

    /**
     * @return area = ½ * πr². The units are cm²
     */
    public static double semicircleArea(double radius) {
        return ONE_HALF * circleArea(radius);
    }

    /**
     * @return area = r² * α / 2. The units are cm²
     */
    public static double sectorArea(double radius, double angleAlphaRadians) {
        checkGreater0(radius);
        return radius * radius * angleAlphaRadians / 2;
    }

    /**
     * @return area = π * a * b. The units are cm²
     */
    public static double ellipseArea(double radiusA, double radiusB) {
        checkGreater0(radiusA);
        checkGreater0(radiusB);
        return Math.PI * radiusA * radiusB;
    }

    /**
     * @return area = (a + b) * h / 2. The units are cm²
     */
    public static double trapezoidArea(double sideA, double sideB, double height) {
        checkGreater0(sideA);
        checkGreater0(sideB);
        checkGreater0(height);
        return (sideA + sideB) * height / 2;
    }

    /**
     * z = (B−b)/(2y)
     *
     * @return A = by + y²z
     */
    public static double trapezoidalChannelArea(double bottomWidth, double height, double slope) {
        return bottomWidth * height + height * height * slope;
    }

    /**
     * z = (B−b)/(2y)
     *
     * @return b + 2 × y × √(1 + z²)
     */
    public static double trapezoidalChannelPerimeter(double bottomWidth, double height, double slope) {
        return bottomWidth + 2 * height * squareRoot(1 + slope * slope);
    }

    /**
     * z = B/(2y)
     *
     * @return y²z
     */
    public static double triangularChannelArea(double height, double slope) {
        return height * height * slope;
    }

    /**
     * z = B/(2y)
     *
     * @return 2 × y × √(1 + z²)
     */
    public static double triangularChannelPerimeter(double height, double slope) {
        return 2 * height * squareRoot(1 + slope * slope);
    }

    /**
     * @return area = b * h. The units are cm²
     */
    public static double parallelogramAreaWithBaseAndHeight(double base, double height) {
        checkGreater0(base);
        checkGreater0(height);
        return base * height;
    }

    private static double areaWithSidesAndAngle(double sideA, double sideB, double angleAlphaRadians) {
        checkGreater0(sideA);
        checkGreater0(sideB);
        return sideA * sideB * Math.sin(angleAlphaRadians);
    }

    /**
     * @return area = a * b * sin(α). The units are cm²
     */
    public static double parallelogramAreaWithSidesAndAngle(double sideA, double sideB, double angleAlphaRadians) {
        return areaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
    }

    /**
     * @return area = e * f * sin(θ). The units are cm²
     */
    public static double parallelogramAreaWithDiagonalsAndAngle(
        double diagonal1, double diagonal2, double angleThetaRadians) {
        checkGreater0(diagonal1);
        checkGreater0(diagonal2);
        return diagonal1 * diagonal2 * Math.sin(angleThetaRadians);
    }

    /**
     * @return area = a * h. The units are cm²
     */
    public static double rhombusAreaWithSideAndHeight(double side, double height) {
        checkGreater0(side);
        checkGreater0(height);
        return side * height;
    }

    private static double areaWithDiagonals(double diagonal1, double diagonal2) {
        checkGreater0(diagonal1);
        checkGreater0(diagonal2);
        return diagonal1 * diagonal2 / 2;
    }

    /**
     * @return area = (e * f) / 2. The units are cm²
     */
    public static double rhombusAreaWithDiagonals(double diagonal1, double diagonal2) {
        return areaWithDiagonals(diagonal1, diagonal2);
    }

    /**
     * @return area = a² * sin(α). The units are cm²
     */
    public static double rhombusAreaWithSideAndAngle(double side, double angleAlphaRadians) {
        checkGreater0(side);
        return side * side * Math.sin(angleAlphaRadians);
    }

    /**
     * @return area = (e * f) / 2. The units are cm²
     */
    public static double kiteAreaWithDiagonals(double diagonal1, double diagonal2) {
        return areaWithDiagonals(diagonal1, diagonal2);
    }

    /**
     * @return area = a * b * sin(α). The units are cm²
     */
    public static double kiteAreaWithSidesAndAngle(double sideA, double sideB, double angleAlphaRadians) {
        return areaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
    }

    /**
     * @return area = a² * √(25 + 10√5) / 4. The units are cm²
     */
    public static double pentagonArea(double sideLength) {
        checkGreater0(sideLength);
        return sideLength * sideLength * Math.sqrt(25 + 10 * Math.sqrt(5)) / 4;
    }

    /**
     * @return area = 3/2 * √3 * a². The units are cm²
     */
    public static double hexagonArea(double sideLength) {
        checkGreater0(sideLength);
        return 3. / 2 * Math.sqrt(3) * sideLength * sideLength;
    }

    /**
     * Alternative: area = perimeter * apothem / 2.
     *
     * @return area = 2 * (1 + √2) * a². The units are cm²
     */
    public static double octagonArea(double sideLength) {
        checkGreater0(sideLength);
        return 2 * (1 + Math.sqrt(2)) * sideLength * sideLength;
    }

    /**
     * @return area = πR² - πr² = π(R² - r²). The units are cm²
     */
    public static double annulusArea(double radius, double innerRadius) {
        checkGreater0(radius);
        checkGreater0(innerRadius);

        if (radius < innerRadius) {
            throw new IllegalArgumentException("Radius R should be greater than radius r");
        }

        final double radiusSquared = radius * radius;
        final double innerRadiusSquared = innerRadius * innerRadius;
        return Math.PI * (radiusSquared - innerRadiusSquared);
    }

    /**
     * @return area = e * f * sin(α). The units are cm²
     */
    public static double irregularQuadrilateralArea(double diagonal1, double diagonal2, double angleAlphaRadians) {
        checkGreater0(diagonal1);
        checkGreater0(diagonal2);
        return diagonal1 * diagonal2 * Math.sin(angleAlphaRadians);
    }

    /**
     * @return area = n * a² * cot(π/n) / 4. The units are cm²
     */
    public static double polygonArea(int numberOfSides, double sideLength) {
        checkGreater0(numberOfSides);
        checkGreater0(sideLength);
        return numberOfSides * sideLength * sideLength * Trigonometry.cot(Math.PI / numberOfSides) / 4;
    }

    /**
     * The properties of equilateral triangle:
     * <br/> - all sides are equal
     * <br/> - all angles are equal to 60°
     */
    public static boolean isEquilateralTriangle(double sideA, double sideB, double sideC) {
        return sideA == sideB && sideA == sideC;
    }

    /**
     * The properties of scalene triangle:
     * <br/> - all sides are different
     * <br/> - depending on the angles might be acute, obtuse or right
     */
    public static boolean isScaleneTriangle(double sideA, double sideB, double sideC) {
        return sideA != sideB && sideA != sideC && sideB != sideC;
    }

    /**
     * All angles are less than 90°
     */
    public static boolean isAcuteTriangle(double angleAlphaRad, double angleBetaRad, double angleGammaRad) {
        return angleAlphaRad < Trigonometry.PI_OVER_2 && angleBetaRad < Trigonometry.PI_OVER_2
            && angleGammaRad < Trigonometry.PI_OVER_2;
    }

    public static boolean isAcuteTriangleWithSSA(double angleRad, double sideA, double sideB) {
        final double[] sides = pythagoreanTheoremWithLegsAndAngle(sideA, sideB, angleRad);
        final double sideC = sides[Constants.ARR_3RD_INDEX];
        final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
        return isAcuteTriangle(
            angles[Constants.ALPHA_INDEX], angles[Constants.BETA_INDEX], angles[Constants.GAMMA_INDEX]);
    }

    /**
     * One of the angles is exactly 90°
     */
    public static boolean isRightTriangle(double angleAlphaRad, double angleBetaRad, double angleGammaRad) {
        return angleAlphaRad == Trigonometry.PI_OVER_2 || angleBetaRad == Trigonometry.PI_OVER_2
            || angleGammaRad == Trigonometry.PI_OVER_2;
    }

    public static boolean isRightTriangleWithSSA(double angleRad, double sideA, double sideB) {
        final double[] sides = pythagoreanTheoremWithLegsAndAngle(sideA, sideB, angleRad);
        final double sideC = sides[Constants.ARR_3RD_INDEX];
        final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
        return isRightTriangle(
            angles[Constants.ALPHA_INDEX], angles[Constants.BETA_INDEX], angles[Constants.GAMMA_INDEX]);
    }

    /**
     * One of the angles measures more than 90°
     */
    public static boolean isObtuseTriangle(double angleAlphaRad, double angleBetaRad, double angleGammaRad) {
        return angleAlphaRad > Trigonometry.PI_OVER_2 || angleBetaRad > Trigonometry.PI_OVER_2
            || angleGammaRad > Trigonometry.PI_OVER_2;
    }

    public static boolean isObtuseTriangleWithSSA(double angleRad, double sideA, double sideB) {
        final double[] sides = pythagoreanTheoremWithLegsAndAngle(sideA, sideB, angleRad);
        final double sideC = sides[Constants.ARR_3RD_INDEX];
        final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
        return isObtuseTriangle(
            angles[Constants.ALPHA_INDEX], angles[Constants.BETA_INDEX], angles[Constants.GAMMA_INDEX]);
    }

    // Angle calculators

    /**
     * complementary angle = 90° - angle
     * complementary angle = π/2 - angle
     */
    public static double complementaryAngle(double angleRadians) {
        return Trigonometry.PI_OVER_2 - angleRadians;
    }

    public static boolean isComplementaryAngle(double angleAlphaRad, double angleBetaRad) {
        final double angleSum = angleAlphaRad + angleBetaRad;
        final double epsilon = 1e-9; // tolerance of the difference
        return Math.abs(Trigonometry.PI_OVER_2 - angleSum) <= epsilon;
    }

    /**
     * supplementary angle = 180° - angle
     *
     * @return supplementary angle = π - angle
     */
    public static double supplementaryAngle(double angleRadians) {
        return Math.PI - angleRadians;
    }

    /**
     * α + β = 180° (π) -> true
     * α + β ≠ 180° (π) -> false
     * If true:
     * sin(α) = sin(β)
     * cos(α) = -cos(β)
     * tan(α) = -tan(β)
     */
    public static boolean isSupplementaryAngles(double angleAlphaRad, double angleBetaRad) {
        final double angleSum = angleAlphaRad + angleBetaRad;
        final double epsilon = 1e-9; // tolerance of the difference
        return Math.abs(Math.PI - angleSum) <= epsilon;
    }

    public static double coterminalAngle(double angleRadians) {
        final double quotient = Math.floor(angleRadians / Trigonometry.PI2);
        final double mulResult = Trigonometry.PI2 * quotient;
        return angleRadians - mulResult;
    }

    /**
     * β = α ± (360° * k)
     * β = α ± (2π * k)
     * sin(α) = sin(α ± (360° * k))
     */
    public static double[] coterminalAngles(double angleRadians, int min, int max) {
        final var angles = new ArrayList<Double>();
        for (int k = min; k <= max; k++) {
            final double rotations = Trigonometry.PI2 * k;
            if (rotations != 0) { // skip adding itself
                angles.add(angleRadians + rotations);
            }
        }
        return angles.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * β - α = 2π * k
     */
    public static boolean areCoterminalAngles(double angleAlphaRad, double angleBetaRad, int rotations) {
        final double angleDiff = angleBetaRad - angleAlphaRad;
        return angleDiff == Trigonometry.PI2 * rotations;
    }

    public static double subtractFullAngleIfNeeded(double angleRadians) {
        if (angleRadians > Trigonometry.PI2) {
            return subtractFullAngleIfNeeded(angleRadians - Trigonometry.PI2);
        }
        return angleRadians;
    }

    /**
     * 0° to 90°: reference angle = angle
     * 90° to 180°: reference angle = 180° − angle
     * 180° to 270°: reference angle = angle − 180°
     * 270° to 360°: reference angle = 360° − angle
     * 0 to π/2: reference angle = angle
     * π/2 to π: reference angle = π − angle
     * π to 3π/2: reference angle = angle − π
     * 3π/2 to 2π: reference angle = 2π − angle
     */
    public static double referenceAngle(double angleRadians) {
        final double normalizedAngleRad = subtractFullAngleIfNeeded(angleRadians);
        final int quadrant = Trigonometry.quadrant(normalizedAngleRad);
        switch (quadrant) {
            case 1 -> {
                return normalizedAngleRad;
            }
            case 2 -> {
                return Math.PI - normalizedAngleRad;
            }
            case 3 -> {
                return normalizedAngleRad - Math.PI;
            }
            case 4 -> {
                return Trigonometry.PI2 - normalizedAngleRad;
            }
            default -> throw new IllegalStateException();
        }
    }

    /**
     * @return θ = L / r. The units are radians
     */
    public static double centralAngleGivenArcLengthRadius(double arcLength, double radius) {
        return arcLength / radius;
    }

    /**
     * @return L = θ * r. The units are meters
     */
    public static double arcLength(double centralAngleRad, double radius) {
        return centralAngleRad * radius;
    }

    /**
     * Minute angle = 6° * number of minutes
     * Hour angle = 30° * number of hours + 0.5° * number of minutes
     * Minute to hour hand angle = |Hour angle − Minute angle|
     * Hour to minute hand angle = 360° − Minute to hour hand angle
     */
    public static double[] clockAngle(short hours, short minutes) {
        final double minuteAngle = Math.toRadians(6) * minutes;
        final double hourAngle = Trigonometry.PI_OVER_6 * hours + Math.toRadians(0.5) * minutes;
        final double minuteToHourAngle = Math.abs(hourAngle - minuteAngle);
        final double hourToMinuteAngle = Math.abs(Trigonometry.PI2 - minuteToHourAngle);
        return new double[]{hourToMinuteAngle, minuteToHourAngle};
    }

    // CoordinateGeometry

    private static void check3dSize(double[] vector) {
        if (vector == null || vector.length != 3) {
            throw new IllegalArgumentException("The 3d vector is required");
        }
    }

    private static void check2dOr3dSize(double[] vector) {
        if (vector == null || vector.length < 2 || vector.length > 3) {
            throw new IllegalArgumentException("The 2d or 3d vector is required");
        }
    }

    /**
     * @return d = ∣a₁ − b₁∣ + ... + ∣a_N − b_N∣
     */
    public static double manhattanDistance(double[] vectorA, double[] vectorB) {
        LinAlgUtils.checkSameDimensions(vectorA, vectorB);

        double result = 0;
        for (int i = 0; i < vectorA.length; i++) {
            result += Math.abs(vectorA[i] - vectorB[i]);
        }
        return result;
    }

    /**
     * The theta is in radians.
     * r = √(x² + y²)
     * θ = tan⁻¹(y/x)
     * z = z
     */
    public static double[] cartesianToCylindricalCoordinates(double[] coordinates) {
        check2dOr3dSize(coordinates);

        final double x = coordinates[Constants.X_INDEX];
        final double y = coordinates[Constants.Y_INDEX];
        final double radius = Math.sqrt(x * x + y * y);
        final double theta = Math.atan(y / x);

        if (coordinates.length == 3) {
            return new double[]{radius, theta, coordinates[Constants.Z_INDEX]};
        }
        return new double[]{radius, theta};
    }

    /**
     * m = √(x² + y²)
     * θ = arccos(x / m)
     * x = ρ * cos(θ)
     * y = ρ * sin(θ)
     * z = z
     */
    public static double[] cylindricalToCartesianCoordinates(double[] coordinates) {
        check2dOr3dSize(coordinates);

        final double radius = coordinates[Constants.R_INDEX];
        final double theta = coordinates[Constants.THETA_INDEX];
        final double x = radius * Math.cos(theta);
        final double y = radius * Math.sin(theta);

        if (coordinates.length == 3) {
            return new double[]{x, y, coordinates[Constants.Z_INDEX]};
        }
        return new double[]{x, y};
    }

    /**
     * Same as {@link #cartesianToCylindricalCoordinates}, but supports 2d only.
     */
    public static double[] cartesianToPolarCoordinates(double[] coordinates) {
        check2dSize(coordinates);
        return cartesianToCylindricalCoordinates(coordinates);
    }

    /**
     * Same as {@link #cylindricalToCartesianCoordinates}, but supports 2d only.
     */
    public static double[] polarToCartesianCoordinates(double[] coordinates) {
        check2dSize(coordinates);
        return cylindricalToCartesianCoordinates(coordinates);
    }

    /**
     * r = √(x² + y² + z²)
     * θ = cos⁻¹(z/r)
     * φ = tan⁻¹(y/x)
     */
    public static double[] cartesianToSphericalCoordinates(double[] coordinates) {
        check3dSize(coordinates);

        final double x = coordinates[Constants.X_INDEX];
        final double y = coordinates[Constants.Y_INDEX];
        final double z = coordinates[Constants.Z_INDEX];

        final double radius = Math.sqrt(x * x + y * y + z * z);
        final double theta = Math.acos(z / radius);
        final double phi = Math.atan(y / x);
        return new double[]{radius, theta, phi};
    }

    /**
     * x = r * sin θ * cos φ
     * y = r * sin θ * sin φ
     * z = r * cos θ
     */
    public static double[] sphericalToCartesianCoordinates(double[] coordinates) {
        check3dSize(coordinates);

        final double radius = coordinates[Constants.X_INDEX];
        final double theta = coordinates[Constants.Y_INDEX];
        final double phi = coordinates[Constants.Z_INDEX];

        final double x = radius * Math.sin(theta) * Math.cos(phi);
        final double y = radius * Math.sin(theta) * Math.sin(phi);
        final double z = radius * Math.cos(theta);
        return new double[]{x, y, z};
    }

    /**
     * @return Δx = x₂-x₁ or Δy = y₂-y₁
     */
    public static double deltaDistance(double point2, double point1) {
        return point2 - point1;
    }

    /**
     * For 1d, d = √(x₂-x₁)²)
     * For 2d, d = √((x₂-x₁)² + (y₂-y₁)²)
     * For 3d, d = √((x₂-x₁)² + (y₂-y₁)² + (z₂-z₁)²)
     * For 4d, d = √((x₂-x₁)² + (y₂-y₁)² + (z₂-z₁)² + (k₂-k₁)²)
     */
    public static double distance(double[] pointACoords, double[] pointBCoords) {
        LinAlgUtils.checkSameDimensions(pointACoords, pointBCoords);

        final double x1 = pointACoords[Constants.X_INDEX];
        final double x2 = pointBCoords[Constants.X_INDEX];
        final double deltaX = deltaDistance(x2, x1);

        final int dimension = pointACoords.length;
        switch (dimension) {
            case 1 -> {
                return Math.sqrt(Math.pow(Math.abs(x2 - x1), 2));
            }
            case 2 -> {
                final double y1 = pointACoords[Constants.Y_INDEX];
                final double y2 = pointBCoords[Constants.Y_INDEX];

                return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaDistance(y2, y1), 2));
            }
            case 3 -> {
                final double y1 = pointACoords[Constants.Y_INDEX];
                final double y2 = pointBCoords[Constants.Y_INDEX];
                final double z1 = pointACoords[Constants.Z_INDEX];
                final double z2 = pointBCoords[Constants.Z_INDEX];

                return Math.sqrt(
                    Math.pow(deltaX, 2) + Math.pow(deltaDistance(y2, y1), 2) + Math.pow(deltaDistance(z2, z1), 2)
                );
            }
            case 4 -> {
                final double y1 = pointACoords[Constants.Y_INDEX];
                final double y2 = pointBCoords[Constants.Y_INDEX];
                final double z1 = pointACoords[Constants.Z_INDEX];
                final double z2 = pointBCoords[Constants.Z_INDEX];
                final double k1 = pointACoords[Constants.K_INDEX];
                final double k2 = pointBCoords[Constants.K_INDEX];

                return Math.sqrt(
                    Math.pow(deltaX, 2) + Math.pow(deltaDistance(y2, y1), 2) +
                        Math.pow(deltaDistance(z2, z1), 2) + Math.pow(deltaDistance(k2, k1), 2)
                );
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    /**
     * @return d = |mx₁ − y₁ + b| / √(m² + 1)
     */
    public static double distanceBetweenPointsAndStraightLine(
        double[] pointCoords, double slope, double yIntercept) {
        check2dSize(pointCoords);

        final double x1 = pointCoords[Constants.X_INDEX];
        final double y1 = pointCoords[Constants.Y_INDEX];

        return Math.abs(slope * x1 - y1 + yIntercept) / Math.sqrt(slope * slope + 1);
    }

    /**
     * The slope has to be the same for both lines.
     *
     * @return d = |b₂ − b₁| / √(m² + 1)
     */
    public static double distanceBetweenParallelLines(
        double slope, double line1YIntercept, double line2YIntercept) {
        return Math.abs(line2YIntercept - line1YIntercept) / Math.sqrt(slope * slope + 1);
    }

    /**
     * @return y = (x - x₁) * (y₂ - y₁) / (x₂ - x₁) + y₁
     */
    public static double linearInterpolation(double[] pointACoords, double[] pointBCoords, double midpointX) {
        check2dSize(pointACoords);
        LinAlgUtils.checkSameDimensions(pointACoords, pointBCoords);

        final double x1 = pointACoords[Constants.X_INDEX];
        final double y1 = pointACoords[Constants.Y_INDEX];
        final double x2 = pointBCoords[Constants.X_INDEX];
        final double y2 = pointBCoords[Constants.Y_INDEX];

        final double deltaY = deltaDistance(y2, y1);
        final double deltaX = deltaDistance(x2, x1);
        return (midpointX - x1) * deltaY / deltaX + y1;
    }

    /**
     * x_f = xᵢ*cos(θ) − yᵢ*sin(θ)
     * y_f = xᵢ*sin(θ) + yᵢ*cos(θ)
     */
    public static double[] rotation(double[] pointCoords, double angleThetaRadians) {
        final double[] originCoords = {0, 0};
        return rotationAroundPoint(pointCoords, originCoords, angleThetaRadians);
    }

    /**
     * x_f = xₒ + (xᵢ-xₒ) cos(θ) − (yᵢ-yₒ) sin(θ)
     * y_f = yₒ + (xᵢ-xₒ) sin(θ) + (yᵢ-yₒ) cos(θ)
     */
    public static double[] rotationAroundPoint(
        double[] pointCoords, double[] pivotCoords, double angleThetaRadians) {
        LinAlgUtils.checkSameDimensions(pointCoords, pivotCoords);
        check2dSize(pointCoords);

        final double xi = pointCoords[Constants.X_INDEX];
        final double yi = pointCoords[Constants.Y_INDEX];
        final double xo = pivotCoords[Constants.X_INDEX];
        final double yo = pivotCoords[Constants.Y_INDEX];

        final double sine = Math.sin(angleThetaRadians);
        final double cosine = Math.cos(angleThetaRadians);
        return new double[]{
            xo + (xi - xo) * cosine - (yi - yo) * sine,
            yo + (xi - xo) * sine + (yi - yo) * cosine
        };
    }

    /**
     * @return m = (y₂ - y₁) / (x₂ - x₁)
     */
    public static double slope(double[] pointACoords, double[] pointBCoords) {
        LinAlgUtils.checkSameDimensions(pointACoords, pointBCoords);
        check2dSize(pointACoords);

        final double x1 = pointACoords[Constants.X_INDEX];
        final double y1 = pointACoords[Constants.Y_INDEX];
        final double x2 = pointBCoords[Constants.X_INDEX];
        final double y2 = pointBCoords[Constants.Y_INDEX];

        return (y2 - y1) / (x2 - x1);
    }

    /**
     * @return m = −a/b
     */
    public static double slope(double coefficientOfX, double coefficientOfY) {
        return -coefficientOfX / coefficientOfY;
    }

    public static double slopeFromKnownIntercepts(double xIntercept, double yIntercept) {
        return -yIntercept / xIntercept;
    }

    public static double areaUnderSlope(double x1, double x2, double slope) {
        final double deltaX = deltaDistance(x2, x1);
        final double deltaY = slope * deltaX;
        return deltaX * deltaY / 2;
    }

    /**
     * x꜀ = −c/a
     * y꜀ = −c/b
     */
    public static double[] intercept(double coefficientOfX, double coefficientOfY, double constantTerm) {
        final double xIntercept = -constantTerm / coefficientOfX;
        final double yIntercept = -constantTerm / coefficientOfY;
        return new double[]{xIntercept, yIntercept};
    }

    /**
     * y = mx + b
     */
    public static double[] intercept(double slopeTerm, double constantTerm) {
        return intercept(slopeTerm, -1, constantTerm);
    }

    /**
     * @return b = y₁ - m * x₁
     */
    public static double slopeInterceptConstantTerm(double x1, double y1, double slope) {
        return y1 - slope * x1;
    }

    /**
     * @return x = (x₁ + x₂)/2
     */
    public static double midpoint(double pointA, double pointB) {
        return (pointA + pointB) / 2;
    }

    /**
     * x = (x₁ + x₂)/2
     * y = (y₁ + y₂)/2
     */
    public static double[] midpoint(double[] pointACoords, double[] pointBCoords) {
        final double x1 = pointACoords[Constants.X_INDEX];
        final double y1 = pointACoords[Constants.Y_INDEX];
        final double x2 = pointBCoords[Constants.X_INDEX];
        final double y2 = pointBCoords[Constants.Y_INDEX];
        return new double[]{midpoint(x1, x2), midpoint(y1, y2)};
    }

    public static double endpointWithGivenMidpoint(double point, double midpoint) {
        return 2 * midpoint - point;
    }

    /**
     * @return (H × W) - ((H - 2t) × (W - 2t))
     */
    public static double crossSectionalAreaOfHollowRectangle(double width, double height, double thickness) {
        return (height * width) - ((height - 2 * thickness) * (width - 2 * thickness));
    }

    /**
     * @return W × H
     */
    public static double crossSectionalAreaOfRectangle(double width, double height) {
        return rectangleArea(width, height);
    }

    /**
     * @return 2 × W × t₁ + (H - 2 × t₁) × t₂
     */
    public static double crossSectionalAreaOfISection(
        double width, double height, double thickness1, double thickness2) {
        return 2 * width * thickness1 + (height - 2 * thickness1) * thickness2;
    }

    /**
     *
     * The same formula as {@link #crossSectionalAreaOfISection}
     */
    public static double crossSectionalAreaOfCSection(
        double width, double height, double thickness1, double thickness2) {
        return crossSectionalAreaOfISection(width, height, thickness1, thickness2);
    }

    /**
     * @return W × t₁ + (H - t₁) × t₂
     */
    public static double crossSectionalAreaOfTSection(
        double width, double height, double thickness1, double thickness2) {
        return width * thickness1 + (height - thickness1) * thickness2;
    }

    /**
     * @return W × t + (H - t) × t
     */
    public static double crossSectionalAreaOfLSection(double width, double height, double thickness) {
        return width * thickness + (height - thickness) * thickness;
    }

    /**
     * @return 0.5 × B × H
     */
    public static double crossSectionalAreaOfIsoscelesTriangle(double base, double height) {
        return ONE_HALF * base * height;
    }

    /**
     * @return 0.4330 × L²
     */
    public static double crossSectionalAreaOfEquilateralTriangle(double side) {
        return 0.433 * side * side;
    }

    /**
     * @return 0.25 × π × D²
     */
    public static double crossSectionalAreaOfCircle(double diameter) {
        return ONE_FOURTH * Math.PI * diameter * diameter;
    }

    /**
     * A_C = π×(D²−d²)/4
     * d = D−2t
     * A_C = π×(D²−(D−2t)²)/4
     *
     * @return 0.25 × π × (D² - (D - 2 × t)²)
     */
    public static double crossSectionalAreaOfTube(double diameter, double thickness) {
        return ONE_FOURTH * Math.PI * (diameter * diameter - Math.pow(diameter - 2 * thickness, 2));
    }

    /**
     * @return A = πd²/4
     */
    public static double crossSectionalAreaOfCircularWire(double diameter) {
        return Math.PI * diameter * diameter / 4;
    }

    /**
     * @return A = 4πr². The units are m²
     */
    public static double sphereArea(double radius) {
        return Trigonometry.PI4 * radius * radius;
    }

    /**
     * Plate/box
     *
     * @return V = l*w*h
     */
    public static double rectangularPrismVolume(double length, double width, double height) {
        return length * width * height;
    }

    /**
     * @return 1/2*b*h
     */
    public static double triangleArea(double base, double height) {
        return ONE_HALF * base * height;
    }

    /**
     * Linear model: y = b + ax.
     * Sₓ = ∑xᵢ = x₁ + x₂ + x₃ + … ;
     * Sᵧ = ∑yᵢ = y₁ + y₂ + y₃ + … ;
     * Sₓₓ = ∑xᵢ² = x₁² + x₂² + x₃² + … ;
     * Sᵧᵧ = ∑yᵢ² = y₁² + y₂² + y₃² + … ;
     * Sₓᵧ = ∑xᵢyᵢ = x₁y₁ + x₂y₂ + x₃y₃ + … ;
     * Δ = n·Sₓₓ - Sₓ².
     * a = (n·Sₓᵧ - Sₓ·Sᵧ) / Δ;
     * b = (Sₓₓ·Sᵧ - Sₓ·Sₓᵧ) / Δ.
     *
     * @param independentVariables x
     * @param dependentVariables   y
     */
    public static double[] leastSquaresRegressionLine(double[] independentVariables, double[] dependentVariables) {
        Objects.requireNonNull(independentVariables);
        Objects.requireNonNull(dependentVariables);
        LinAlgUtils.checkSameDimensions(independentVariables, dependentVariables);
        final double sx = Arrays.stream(independentVariables).sum();
        final double sy = Arrays.stream(dependentVariables).sum();
        final double sxx = Arrays.stream(independentVariables).map(x -> x * x).sum();
        double sxy = 0;
        for (int i = 0; i < independentVariables.length; i++) {
            final double x = independentVariables[i];
            final double y = dependentVariables[i];
            sxy += x * y;
        }
        final int n = independentVariables.length;
        final double delta = n * sxx - sx * sx;
        final double slope = (n * sxy - sx * sy) / delta; // a
        final double intercept = (sxx * sy - sx * sxy) / delta; // b
        return new double[]{slope, intercept};
    }
}
