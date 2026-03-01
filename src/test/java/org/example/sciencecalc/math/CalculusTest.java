package org.example.sciencecalc.math;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CalculusTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;

    static List<Arguments> limitArgs() {
        final DoubleUnaryOperator f = x -> x * x + 5; // x²+5
        final DoubleUnaryOperator f2 = x -> 3 * x * x * x + 4 * x + 5; // 3x³+4x+5
        final DoubleUnaryOperator f3 = x -> 6 * x * x + 10 * x + 15; // 6x²+10x+15

        double exponent = 2;
        double maxLimit = 4;
        final DoubleUnaryOperator f4 = x -> (Math.pow(x, exponent) + Math.pow(maxLimit, exponent))
            / (x - maxLimit); // (xⁿ+aⁿ)/(x-a)

        final DoubleUnaryOperator f5 = Math::exp; // limₓ→₀ eˣ = 1
        final DoubleUnaryOperator f6 = x -> (Math.exp(x) - 1) / x; // limₓ→₀ (eˣ-1) / x = 1
        final DoubleUnaryOperator f7 = x -> (Math.pow(2, x) - 1) / x; // limₓ→₀ (aˣ-1) / x = logₑa
        final DoubleUnaryOperator f8 = x -> Math.pow(1 + x, 1 / x); // limₓ→₀ (1 + x)¹/ˣ = e
        final DoubleUnaryOperator f9 = x -> Math.sqrt(7 * x + 22); // limₓ→₋₃ √(7x + 22)
        // limₓ→₁ 5x³-6x²+2x-1
        final DoubleUnaryOperator f10 = x -> 5 * Math.pow(x, 3) - 6 * x * x + 2 * x - 1;
        // limₓ→₋₅ (5x+4) / (x+8)
        final DoubleUnaryOperator f11 = x -> (5 * x + 4) / (x + 8);
        // limₓ→₁ √(51-2x)
        final DoubleUnaryOperator f12 = x -> Math.sqrt(51 - 2 * x);
        final DoubleUnaryOperator f13 = x -> -4. / (2 * x - 5);
        final DoubleUnaryOperator f14 = x -> 2 * x / (x * x - 7 * x + 6);
        final DoubleUnaryOperator f15 = x -> 3 * x / ((x - 2) * (x + 2));
        final DoubleUnaryOperator f16 = x -> Math.pow(x - 5, 2) / Math.pow(x - 3, 2);
        // (12x³+12x²) / (x⁴-x²)
        final DoubleUnaryOperator f17 = x -> (12. * Math.pow(x, 3) + 12. * x * x) / (Math.pow(x, 4) - x * x);
        // limₓ→₂ (x⁴+3x³-10x²) / (x²-2x) = limₓ→₂ (x²-5x)
        final DoubleUnaryOperator f18 = x -> (Math.pow(x, 4) + 3 * Math.pow(x, 3) - 10 * x * x) / (x * x - 2 * x);
        // limₓ→₅ (12x-60) / (x²-6x+5) = limₓ→₅ 12 / (x - 1)
        final DoubleUnaryOperator f19 = x -> (12. * x - 60) / (x * x - 6 * x + 5);
        // limₓ→₃ (x³-9x) / (x²-3x) = limₓ→₅ (x+3)
        final DoubleUnaryOperator f20 = x -> (Math.pow(x, 3) - 9 * x) / (x * x - 3 * x);
        // {x² for x≤0
        // {ln(x) for x>0
        final DoubleUnaryOperator piecewiseFn1 = x -> x <= 0 ? x * x : BigDecimal.valueOf(Algebra.ln(x))
            .setScale(8, RoundingMode.HALF_EVEN)
            .doubleValue();
        // {sin(x) for 0≤x≤π
        // {x/π - 1 for π<x≤10
        final DoubleUnaryOperator piecewiseFn2 = x -> {
            if (x <= Math.PI) {
                return Trigonometry.sin(x);
            } else if (x <= 10) {
                return x / Math.PI - 1;
            } else {
                throw new IllegalArgumentException();
            }
        };
        // {1/(2x) for x≤-2
        // {2ˣ for -2<x≤0
        final DoubleUnaryOperator piecewiseFn3 = x -> {
            if (x <= -2) {
                return 1 / (2 * x);
            } else if (x <= 0) {
                return Math.pow(2, x);
            } else {
                throw new IllegalArgumentException();
            }
        };
        // {2ˣ-1 for -8≤x<1
        // {√x for x≥1
        final DoubleUnaryOperator piecewiseFn4 = x ->
            x <= -2 ? Math.pow(2, x) - 1 : Algebra.squareRoot(x);

        final DoubleUnaryOperator cot = Trigonometry::cot;
        final DoubleUnaryOperator sin = Trigonometry::sin;
        final DoubleUnaryOperator tan = Trigonometry::tan;

        // limₓ→₃ (x-3) / (√(4x+4)-4) = limₓ→₃ (√(4x+4)+4) / 4
        final DoubleUnaryOperator conjugateFn1 = x ->
            (x - 3) / (Algebra.squareRoot(4 * x + 4) - 4);

        // limₓ→₋₂ (√(3x+10)-2) / (x+2) = limₓ→₋₂ 3 / (√(3x+10)+2)
        final DoubleUnaryOperator conjugateFn2 = x ->
            (Algebra.squareRoot(3 * x + 10) - 2) / (x + 2);

        // limₓ→₃ (x-3) / (2-√(x+1)) = limₓ→₃ -2 - √(x+1), for x ≠ 3
        final DoubleUnaryOperator conjugateFn3 = x ->
            (x - 3) / (2 - Algebra.squareRoot(x + 1));

        // limₓ→₂ (3-√(5x-1)) / (x-2) = limₓ→₂ -5 / (3+√(5x-1)), for x ≠ 2
        final DoubleUnaryOperator conjugateFn4 = x ->
            (3 - Algebra.squareRoot(5 * x - 1)) / (x - 2);

        // limₓ→₋₃ (√(4x+28)-4) / (x+3) = limₓ→₋₃ 4 / (√(4x+28)-4), for x ≠ -3
        final DoubleUnaryOperator conjugateFn5 = x ->
            (Algebra.squareRoot(4 * x + 28) - 4) / (x + 3);

        // limₓ→₋₂ (3-√(6x+21)) / (x+2) = limₓ→₋₂ -6 / (3+√(6x+21)), for x ≠ -2
        final DoubleUnaryOperator conjugateFn6 = x ->
            (3 - Algebra.squareRoot(6 * x + 21)) / (x + 2);

        // limₓ→₁ (x-1) / (√(5x-1)-2) = limₓ→₁ (√(5x-1)+2) / 5, for x ≠ 1
        final DoubleUnaryOperator conjugateFn7 = x ->
            (x - 1) / (Algebra.squareRoot(5 * x - 1) - 2);

        final DoubleUnaryOperator removableDiscontinuityFn = x ->
            (x == 1) ? Double.NaN : (x * x - 1) / (x - 1);

        return List.of(
            Arguments.of("#1", f, 2., 9., DELTA1), // 2²+5=9
            Arguments.of("#2", f2, 0., 5., DELTA1), // 3(0)³+4(0)+5=5
            Arguments.of("#3", f3, 3., 99., DELTA1), // 6(3)²+10(3)+15=99
            Arguments.of("#4", f4, 3., -25., DELTA1), // (3²+4²)/(3-4)=-25
            Arguments.of("#5", f5, 0, 1., DELTA1),
            Arguments.of("#6", f6, 0, 1., DELTA1),
            Arguments.of("#7", f7, 0, Algebra.ln(2), DELTA1),
            Arguments.of("#8", f8, 0, Math.E, DELTA1),
            Arguments.of("#9", f9, -3., 1., DELTA1),
            Arguments.of("#10", f10, 1., 0, DELTA1),
            Arguments.of("#11", f11, -5., -7., DELTA1), // -21/3 = -7
            Arguments.of("#12", f12, 1, 7., DELTA1), // √49 = 7
            Arguments.of("#13", f13, 3, -4., DELTA1),
            Arguments.of("#14", f14, 1, Double.NaN, DELTA1),
            Arguments.of("#15", f15, 4, 1, DELTA1),
            Arguments.of("#16", f16, 3, Double.NaN, DELTA1),
            Arguments.of("#17", f17, -1, -6, DELTA1),
            Arguments.of("#18", f18, 2, 14, DELTA1),
            Arguments.of("#19", f19, 5, 3., DELTA1),
            Arguments.of("#20", f20, 3, 6., DELTA1),
            Arguments.of("piecewise#1", piecewiseFn1, 1, 0, DELTA1),
            Arguments.of("piecewise#2", piecewiseFn2, Math.PI, 0, DELTA1),
            Arguments.of("piecewise#3", piecewiseFn3, -2, Double.NaN, DELTA1), // -1/4 ≠ 1/4
            Arguments.of("piecewise#4", piecewiseFn4, 4, 2, DELTA1), // √4 = 2
            Arguments.of("trig#1", cot, Trigonometry.PI_OVER_4, 1, DELTA1),
            Arguments.of("trig#2", sin, Trigonometry.PI_OVER_6, Arithmetic.ONE_HALF, DELTA1),
            Arguments.of("trig#3", cot, 0, Double.NaN, DELTA1),
            Arguments.of("trig#4", tan, 0, 0, DELTA1),
            Arguments.of("conjugate#1", conjugateFn1, 3, 2, DELTA1),
            Arguments.of("conjugate#2", conjugateFn2, -2, Arithmetic.THREE_FOURTH, DELTA2),
            Arguments.of("conjugate#3", conjugateFn3, 3, -4, DELTA1),
            Arguments.of("conjugate#4", conjugateFn4, 2, -Arithmetic.FIVE_SIXTH, DELTA6),
            Arguments.of("conjugate#5", conjugateFn5, -3, Arithmetic.ONE_HALF, DELTA1),
            Arguments.of("conjugate#6", conjugateFn6, -2, -1, DELTA1),
            Arguments.of("conjugate#7", conjugateFn7, 1, Arithmetic.FOUR_FIFTH, DELTA1),
            Arguments.of("removableDiscontinuity#1", removableDiscontinuityFn, 1., 2., DELTA1)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("limitArgs")
    void testLimit(String testName, DoubleUnaryOperator f, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.limit(f, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> limitSumRuleArgs() {
        final DoubleUnaryOperator f = _ -> 3;
        final DoubleUnaryOperator g = _ -> 2;

        return List.of(
            Arguments.of(f, g, -3., 5., DELTA1) // 3+2=5
        );
    }

    @ParameterizedTest
    @MethodSource("limitSumRuleArgs")
    void testLimitSumRule(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.limitSumRule(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> oneSidedLimitRHSArgs() {
        final DoubleUnaryOperator f = x -> x <= -4 ? 1 / (x + 1) : Math.pow(2, x);

        return List.of(
            // rhs = limₓ→₋₄⁺ 2ˣ = 2⁻⁴ = (1/2)⁴ = 1/16
            Arguments.of(f, -4., Arithmetic.ONE_SIXTEENTH, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("oneSidedLimitRHSArgs")
    void testOneSidedLimitRHS(
        DoubleUnaryOperator f, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.oneSidedLimitRHS(f, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> oneSidedLimitSumArgs() {
        final DoubleUnaryOperator f = x -> x < -3 ? 3 : 0;
        final DoubleUnaryOperator g = _ -> 2;
        final DoubleUnaryOperator f1 = x -> x < -2 ? 1 : 4;
        final DoubleUnaryOperator g1 = x -> x < -2 ? 4 : 1;

        return List.of(
            // lhs = limₓ→₃⁻ f(x) + limₓ→₃⁻ g(x) = 3+2=5
            // rhs = limₓ→₃⁺ f(x) + limₓ→₃⁺ g(x) = 0+2=5
            Arguments.of(f, g, -3., Double.NaN, DELTA1),
            // lhs = limₓ→₋₂⁻ f(x) + limₓ→₋₂⁻ g(x) = 1+4=5
            // rhs = limₓ→₋₂⁺ f(x) + limₓ→₋₂⁺ g(x) = 4+1=5
            Arguments.of(f1, g1, -2., 5., DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("oneSidedLimitSumArgs")
    void testOneSidedLimitSum(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.oneSidedLimitSum(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> limitDifferenceRuleArgs() {
        final DoubleUnaryOperator f = x -> x < -1 ? 2 : 1;
        final DoubleUnaryOperator g = x -> x < -1 ? 1 : 0;

        return List.of(
            Arguments.of(f, g, -1., Double.NaN, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("limitDifferenceRuleArgs")
    void testLimitDifferenceRule(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.limitDifferenceRule(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> oneSidedLimitDifferenceArgs() {
        final DoubleUnaryOperator f = x -> x < -1 ? 2 : 1;
        final DoubleUnaryOperator g = x -> x < -1 ? 1 : 0;

        return List.of(
            Arguments.of(f, g, -1., 1., DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("oneSidedLimitDifferenceArgs")
    void testOneSidedLimitDifference(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.oneSidedLimitDifference(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    @Test
    void testLimitConstantMultipleRuleWithEquationTerms() {
        // given f(x) = x² + 3x - 4
        final DoubleUnaryOperator xSquaredFn = x -> x * x;
        final DoubleUnaryOperator plus3xFn = x -> 3 * x;
        final DoubleUnaryOperator minus4Fn = _ -> -4;
        final DoubleUnaryOperator[] equationTerms = {xSquaredFn, plus3xFn, minus4Fn};
        final BiFunction<DoubleUnaryOperator[], Double, Double> f = (terms, x) ->
            Arrays.stream(terms).mapToDouble(fn -> fn.applyAsDouble(x)).sum();
        final byte constant = 5;
        final double x = 3;
        // when
        final Pair<double[], Double> result = Calculus
            .limitConstantMultipleRule(f, equationTerms, constant, x);
        // then
        assertNotNull(result.getLeft());
        assertNotNull(result.getRight());
        assertArrayEquals(new double[]{45, 45, -20}, result.getLeft(), DELTA1);
        assertEquals(70, result.getRight(), DELTA1);
    }

    static List<Arguments> limitConstantMultipleRuleArgs() {
        final DoubleUnaryOperator f = _ -> 2;

        return List.of(
            Arguments.of(f, 5., 3., 10., DELTA1) // 5*2=10
        );
    }

    @ParameterizedTest
    @MethodSource("limitConstantMultipleRuleArgs")
    void testLimitConstantMultipleRule(
        DoubleUnaryOperator f, double constant, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.limitConstantMultipleRule(f, constant, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> limitProductRuleArgs() {
        final DoubleUnaryOperator f = _ -> 2;
        final DoubleUnaryOperator g = _ -> 4;

        return List.of(
            Arguments.of(f, g, -1., 8., DELTA1) // limₓ→₋₁ f(x) * limₓ→₋₁ g(x) = 2*4=8
        );
    }

    @ParameterizedTest
    @MethodSource("limitProductRuleArgs")
    void testLimitProductRule(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.limitProductRule(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> oneSidedLimitProductRuleArgs() {
        final DoubleUnaryOperator f = x -> x < 3 ? 1 : 5;
        final DoubleUnaryOperator g = x -> x < 3 ? 5 : 1;
        final DoubleUnaryOperator f1 = x -> x < -2 ? 4 : 2;
        final DoubleUnaryOperator g1 = x -> 2;

        return List.of(
            // lhs = limₓ→₃⁻ f(x) * limₓ→₃⁻ g(x) = 1*5=5
            // rhs = limₓ→₃⁺ f(x) * limₓ→₃⁺ g(x) = 5*1=5
            Arguments.of(f, g, 3., 5., DELTA1),
            // lhs = limₓ→₋₂⁻ f(x) * limₓ→₋₂⁻ g(x) = 4*2=8
            // rhs = limₓ→₋₂⁺ f(x) * limₓ→₋₂⁺ g(x) = 2*2=4
            Arguments.of(f1, g1, -2., Double.NaN, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("oneSidedLimitProductRuleArgs")
    void testOneSidedLimitProductRule(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.oneSidedLimitProductRule(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> limitQuotientRuleArgs() {
        final DoubleUnaryOperator f = _ -> 1;
        final DoubleUnaryOperator g = _ -> 0;

        return List.of(
            Arguments.of(f, g, -1., Double.NaN, DELTA1) // limₓ→₋₁ f(x) / limₓ→₋₁ g(x) = 1/0
        );
    }

    @ParameterizedTest
    @MethodSource("limitQuotientRuleArgs")
    void testLimitQuotientRule(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.limitQuotientRule(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    static List<Arguments> limitOfCompositeFunctionsArgs() {
        final DoubleUnaryOperator f = _ -> -1;
        final DoubleUnaryOperator g = x -> x < -1 ? 3 : -1;
        final DoubleUnaryOperator g1 = _ -> 3;
        final DoubleUnaryOperator h = _ -> 1;
        final DoubleUnaryOperator g2 = x -> x < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        final DoubleUnaryOperator h1 = x -> x * 0;
        final DoubleUnaryOperator g3 = _ -> 0;
        final DoubleUnaryOperator h2 = x -> x < 0 ? 1 : -1;

        return List.of(
            // limₓ→₃ g(f(x)) = limₓ→₃ g(limₓ→₃ f(3)) = limₓ→₋₁ g(-1)
            Arguments.of(f, g, 3., Double.NaN, DELTA1),
            // limₓ→₋₁ h(g(x)) = limₓ→₋₁ h(limₓ→₋₁ g(-1)) = limₓ→₃ h(3)
            Arguments.of(g1, h, 3., 1., DELTA1),
            // limₓ→₋₁ h(g(x)) = limₓ→₋₁ h(limₓ→₋₁ g(-1)) = limₓ→∞ h(±∞)
            Arguments.of(g2, h1, -1., Double.NaN, DELTA1),
            // limₓ→₋₂ h(g(x)) = limₓ→₋₂ h(limₓ→₋₂ g(-2)) = limₓ→₀ h(0)
            Arguments.of(g3, h2, -2., Double.NaN, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("limitOfCompositeFunctionsArgs")
    void testLimitOfCompositeFunctions(
        DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
        // when
        final double limit = Calculus.limitOfCompositeFunctions(f, g, x);
        // then
        assertEquals(expectedResult, limit, delta);
    }

    @Test
    void testDerivativeConstantRule() {
        // given f(x) = 3
        final DoubleUnaryOperator constantFn = _ -> 3;
        // when
        final double result = Calculus.derivativeConstantRule(constantFn);
        // then
        assertEquals(0, result, DELTA1);
    }

    @Test
    void testDerivativeConstantMultipleRule() {
        // given f(x) = x² + 3x - 4
        final DoubleUnaryOperator f = x -> x * x + 3 * x - 4;
        final byte constant = 5;
        final double x = 1;
        // when
        final double result = Calculus.derivativeConstantMultipleRule(f, constant, x);
        // then
        assertEquals(25, result, DELTA1); // 10x + 15
    }

    @Test
    void testDerivativeConstantMultipleRuleWithEquationTerms() {
        // given f(x) = x² + 3x - 4
        final DoubleUnaryOperator xSquaredFn = x -> x * x;
        final DoubleUnaryOperator plus3xFn = x -> 3 * x;
        final DoubleUnaryOperator minus4Fn = _ -> -4;
        final DoubleUnaryOperator[] equationTerms = {xSquaredFn, plus3xFn, minus4Fn};
        final BiFunction<DoubleUnaryOperator[], Double, Double> f = (terms, x) ->
            Arrays.stream(terms).mapToDouble(fn -> fn.applyAsDouble(x)).sum();
        final byte constant = 5;
        final double x = 1;
        // when
        final Pair<double[], Double> result = Calculus
            .derivativeConstantMultipleRule(f, equationTerms, constant, x);
        // then
        assertNotNull(result.getLeft());
        assertNotNull(result.getRight());
        assertArrayEquals(new double[]{10, 15, 0}, result.getLeft(), DELTA1);
        assertEquals(25, result.getRight(), DELTA1); // 10x + 15
    }

    @Test
    void testIntegralConstantMultipleRule() {
        // given f(x) = x² + 3x - 4
        final DoubleUnaryOperator f = x -> x * x + 3 * x - 4;
        final double lowerLimit = 2;
        final double upperLimit = 3;
        final int numberOfIntervals = 1000;
        final byte constant = 5;
        // when
        final double result = Calculus
            .integralConstantMultipleRule(f, lowerLimit, upperLimit, numberOfIntervals, constant);
        // then
        assertEquals(49.16, result, DELTA1); // 5x³/3 + 15x²/2 - 20x
    }

    static List<Arguments> partialDerivativeForwardDifferenceWrtXArgs() {
        // f(x,y) = 3x²y + 2y² + 7x
        final DoubleBinaryOperator f = (x, y) -> 3 * x * x * y + 2 * y * y + 7 * x;
        final DoubleBinaryOperator f2 = (x, _) -> 8 * x * x; // f(x,y) = f(x,2) = 8x²
        // f(x,y) = 1/5(x² - 2xy) + 3
        final DoubleBinaryOperator f3 = (x, y) -> Arithmetic.ONE_FIFTH * (x * x - 2 * x * y) + 3;

        return List.of(
            Arguments.of(f, new double[]{2, 3}, 43, DELTA1), // 6xy + 7 = 6 * 2 * 3 + 7 = 43
            Arguments.of(f2, new double[]{3, 2}, 48, DELTA1), // 16 * 3 = 48
            Arguments.of(f3, new double[]{0, 2}, -4. / 5, DELTA5) // ∂/∂x(x²)=2x; 1/5(2(0)-2(2)*1)+0 = -4/5
        );
    }

    @ParameterizedTest
    @MethodSource("partialDerivativeForwardDifferenceWrtXArgs")
    void testPartialDerivativeForwardDifferenceWrtX(
        DoubleBinaryOperator f, double[] args, double expectedResult, double delta) {
        // given
        final double h = Calculus.NUMERICAL_APPROXIMATION;
        // when
        final double result = Calculus.partialDerivativeForwardDifferenceWrtX(
            f, args[Constants.X_INDEX], args[Constants.Y_INDEX], h);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> partialDerivativeDifferenceRuleWrtYArgs() {
        // f(x,y) = 3x²y + 2y² + 7x
        final DoubleBinaryOperator f = (x, y) -> 3 * x * x * y + 2 * y * y + 7 * x;
        // f(x,y) = 1/5(x² - 2xy) + 3
        final DoubleBinaryOperator f2 = (x, y) -> Arithmetic.ONE_FIFTH * (x * x - 2 * x * y) + 3;
        return List.of(
            Arguments.of(f, new double[]{2, 3}, 24., DELTA1), // 3x² + 4y = 24
            Arguments.of(f2, new double[]{2, 0}, -4. / 5, DELTA3) // 1/5(0-2(2)*1)+0 = -2/5*2 = -4/5
        );
    }

    @ParameterizedTest
    @MethodSource("partialDerivativeDifferenceRuleWrtYArgs")
    void testPartialDerivativeForwardDifferenceWrtY(
        DoubleBinaryOperator f, double[] args, double expectedResult, double delta) {
        // given
        final double h = Calculus.NUMERICAL_APPROXIMATION;
        // when
        final double result = Calculus.partialDerivativeForwardDifferenceWrtY(
            f, args[Constants.X_INDEX], args[Constants.Y_INDEX], h);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> partialDerivativeSumRuleWrtXArgs() {
        final DoubleBinaryOperator f = (x, y) -> x * x + 3 * x * y + y * y; // f(x,y) = x² + 3xy + y²
        final DoubleBinaryOperator g = (x, y) -> 2 * x + y; // g(x,y) = 2xy
        return List.of(
            // ∂/∂x(f+g) = (2*2 + 3*3) + 2 = 13 + 2 = 15
            Arguments.of(f, g, new double[]{2, 3}, 15, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("partialDerivativeSumRuleWrtXArgs")
    void testPartialDerivativeSumRuleWrtX(
        DoubleBinaryOperator f, DoubleBinaryOperator g, double[] args, double expectedResult, double delta) {
        // when
        final double result = Calculus.partialDerivativeSumRuleWrtX(
            f, g, args[Constants.X_INDEX], args[Constants.Y_INDEX]);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> partialDerivativeSumRuleWrtYArgs() {
        final DoubleBinaryOperator f = (x, y) -> x * x + 3 * x * y + y * y; // f(x,y) = x² + 3xy + y²
        final DoubleBinaryOperator g = (x, y) -> 2 * x + y; // g(x,y) = 2xy
        return List.of(
            // ∂/∂y(f+g) = (3*2 + 2*3) + 1 = 12 + 1 = 13
            Arguments.of(f, g, new double[]{2, 3}, 13, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("partialDerivativeSumRuleWrtYArgs")
    void testPartialDerivativeSumRuleWrtY(
        DoubleBinaryOperator f, DoubleBinaryOperator g, double[] args, double expectedResult, double delta) {
        // when
        final double result = Calculus.partialDerivativeSumRuleWrtY(
            f, g, args[Constants.X_INDEX], args[Constants.Y_INDEX]);
        // then
        assertEquals(expectedResult, result, delta);
    }

    @Test
    void testDefiniteIntegralTrainDisplacement() {
        // given x = [0, 10] hr
        // The train travels: Δ₁+R₁+T+R₂+Δ₂ = 20+80+45+250+25 = 420
        final var fns = new DoubleUnaryOperator[]{
            x -> 40 * x, // Δ₁ = 1/2 * 1 * 40 = 20, for x ∈ [0,1], triangle
            _ -> 40, // R₁ = 2 * 40 = 80, for x ∈ [1,3], rectangle, constant velocity
            x -> 40 + 10 * (x - 3), // T = 1/2 * (40 + 50) * 1 = 45, trapezoid from 40 to 50, for x ∈ [3,4]
            _ -> 50, // R₂ = 5 * 50 = 250, for x ∈ [4,9], rectangle, constant velocity
            x -> 50 * (10 - x), // Δ₂ = 1/2 * 1 * 50 = 25, for x ∈ [9,10], triangle
        };
        final byte lowerLimit = 0;
        final byte upperLimit = 10;
        final byte numberOfIntervals = 20;
        final double[] boundaries = new double[]{1, 3, 4, 9};
        // when
        final double totalDisplacement = Calculus.definiteIntegralPiecewise(
            fns, lowerLimit, upperLimit, numberOfIntervals, boundaries);
        // then
        assertEquals(420, totalDisplacement, DELTA1);
    }

    @Test
    void testDefiniteIntegralSnowstorm() {
        // given x = [0, 18]
        // Δ₁+R₁+T+R₂+Δ₂ = 1+10+1.5+1+0.5 = 14
        final var fns = new DoubleUnaryOperator[]{
            // Δ₁ = triangle, for x ∈ [0,2], height=1, base=2
            // Δ₁ = 0.5 * 2 * 1 = 1
            x -> x <= 2 ? Geometry.triangleArea(x, 1) : 0,
            // R₁ = rectangle, for x ∈ [2,12], height=1
            // R₁ = 10 * 1 = 10
            x -> (x >= 2 && x <= 12) ? 1 : 0,
            // T = trapezoid, for x ∈ [12,14], heights 1 and 0.5
            // T = 0.5 * (1 + 0.5) * 2 = 1.5
            x -> (x >= 12 && x <= 14) ? (1 - 0.25 * (x - 12)) : 0,
            // R₂ = rectangle, for x ∈ [14,16], height=0.5
            // R₂ = 2 * 0.5 = 1
            x -> (x >= 14 && x <= 16) ? 0.5 : 0,
            // Δ₂ = triangle, for x ∈ [16,18], height=0.5
            // Δ₂ = 0.5 * 2 * 0.5 = 0.5
            x -> (x >= 16 && x <= 18) ? 0.5 * (1 - (x - 16) / 2.) : 0
        };
        final byte lowerLimit = 0;
        final byte upperLimit = 18;
        final byte numberOfIntervals = 40;
        final double[] boundaries = new double[]{2, 12, 14, 16};
        // when
        final double totalSnowfall = Calculus.definiteIntegralPiecewise(
            fns, lowerLimit, upperLimit, numberOfIntervals, boundaries);
        // then
        assertEquals(14, totalSnowfall, DELTA1);
    }

    static List<Arguments> riemannSumIntegrateLeftArgs() {
        final DoubleUnaryOperator f = x -> {
            double result = 0;
            if (x <= 0) {
                result = 8;
            } else if (x <= 2) {
                result = 6;
            } else if (x <= 5) {
                result = 3;
            } else if (x <= 7) {
                result = 4;
            }
            return result;
        };
        return List.of(
            Arguments.of(f, 0, 7, 3, 40, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("riemannSumIntegrateLeftArgs")
    @Disabled
    void testRiemannSumIntegrateLeft(DoubleUnaryOperator f, double lowerLimit, double upperLimit,
                                     int numOfIntervals, double expectedResult, double delta) {
        // when
        final double result = Calculus.riemannSumIntegrateLeft(f, lowerLimit, upperLimit, numOfIntervals);
        // then
        assertEquals(expectedResult, result, delta);
    }
}
