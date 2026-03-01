package org.example.sciencecalc.math;

import org.apache.commons.math.complex.Complex;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.example.sciencecalc.math.Arithmetic.ONE_EIGHTH;
import static org.example.sciencecalc.math.Arithmetic.ONE_FOURTH;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AlgebraTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;
    private static final double DELTA8 = 0.00000001;
    private static final double DELTA9 = 0.000000001;

    static List<Arguments> gammaFunctionArgs() {
        return List.of(
            Arguments.of(10, 362880.0000000015),
            Arguments.of(10.1, 454760.751441586)
        );
    }

    @ParameterizedTest
    @MethodSource("gammaFunctionArgs")
    void testGammaFunction(double x, double expectedResult) {
        // when
        final double result = Algebra.gammaFunction(x);
        // then
        assertEquals(expectedResult, result, DELTA9);
    }

    @Test
    void testSquareRootMultiply() {
        // given
        final byte x = 3;
        final byte y = 4;
        // when
        final double result = Algebra.squareRootMultiply(x, y);
        // then
        assertEquals(3.4641, result, DELTA4);
    }

    @Test
    void testSquareRootDivide() {
        // given
        final byte x = 8;
        final byte y = 4;
        // when
        final double result = Algebra.squareRootDivide(x, y);
        // then
        assertEquals(1.414214, result, DELTA6);
    }

    static List<Arguments> squareRootWithExponentArgs() {
        return List.of(
            Arguments.of(2, 4, 4),
            Arguments.of(5, 3, 11.18033989),
            Arguments.of(4, 5, 32)
        );
    }

    @ParameterizedTest
    @MethodSource("squareRootWithExponentArgs")
    void testSquareRootWithExponent(double x, double exponent, double expectedResult) {
        // when
        final double result = Algebra.squareRootWithExponent(x, exponent);
        // then
        assertEquals(expectedResult, result, DELTA8);
    }

    static List<Arguments> squareRootWithComplexNumberArgs() {
        return List.of(
            Arguments.of(-9, 3),
            Arguments.of(-13, Math.sqrt(13)),
            Arguments.of(-49, 7)
        );
    }

    @ParameterizedTest
    @MethodSource("squareRootWithComplexNumberArgs")
    void testSquareRootWithComplexNumber(double x, double expectedResult) {
        // when
        final double result = Algebra.squareRootWithComplexNumber(x);
        // then
        assertEquals(expectedResult, result, DELTA8);
    }

    @ParameterizedTest
    @CsvSource({
        "1,1",
        "2,1.26",
        "3,1.44",
        "4,1.59",
        "5,1.71",
        "8,2",
        "10,2.15",
        "27,3",
        "64,4",
        "125,5",
        "216,6",
        "343,7",
        "512,8",
        "729,9",
        "1000,10",
    })
    void testCubeRoot(double number, double expectedResult) {
        // when
        final double result = Algebra.cubeRoot(number);
        // then
        assertEquals(expectedResult, result, DELTA2);
    }

    @ParameterizedTest
    @CsvSource({
        "1296,4,6",
        "450,3.14,6.99797",
    })
    void testNthRoot(double number, double degree, double expectedResult) {
        // when
        final double result = Algebra.nthRoot(number, degree);
        // then
        assertEquals(expectedResult, result, DELTA5);
    }

    static List<Arguments> addRadicalsArgs() {
        return List.of(
            Arguments.of(new double[]{4, 7}, new double[]{2, 4, 7}, new double[]{3, 4, 7}), // ∜7 + 2∜7 = 3∜7
            Arguments.of(new double[]{5, 3, 5}, new double[]{3, 3, 5}, new double[]{8, 3, 5}) // 5∛5 + 3∛5 = 8∛5
        );
    }

    @ParameterizedTest
    @MethodSource("addRadicalsArgs")
    void testAddRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
        // when
        final double[] result = Algebra.addRadicals(radical1, radical2);
        // then
        assertArrayEquals(expectedResult, result, DELTA5);
    }

    static List<Arguments> subtractRadicalsArgs() {
        return List.of(
            // 5*⁶√9 - 2*⁶√9 = 3*⁶√9
            Arguments.of(new double[]{5, 6, 9}, new double[]{2, 6, 9}, new double[]{3, 6, 9})
        );
    }

    @ParameterizedTest
    @MethodSource("subtractRadicalsArgs")
    void testSubtractRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
        // when
        final double[] result = Algebra.subtractRadicals(radical1, radical2);
        // then
        assertArrayEquals(expectedResult, result, DELTA5);
    }

    static List<Arguments> multiplyRadicalsArgs() {
        return List.of(
            Arguments.of(new double[]{4, 7}, new double[]{4, 4}, new double[]{1, 4, 28}), // ∜7 * ∜4 = ∜28
            Arguments.of(new double[]{4, 3, 3}, new double[]{2, 3, 5}, new double[]{8, 3, 15}), // 4∛3 * 2∛5 = 8∛15
            Arguments.of(new double[]{5, 3, 2}, new double[]{3, 12}, new double[]{5, 3, 24}) // 5∛2 * ∛12 = 5∛24
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyRadicalsArgs")
    void testMultiplyRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
        // when
        final double[] result = Algebra.multiplyRadicals(radical1, radical2);
        // then
        assertArrayEquals(expectedResult, result, DELTA5);
    }

    static List<Arguments> divideRadicalsArgs() {
        return List.of(
            Arguments.of(new double[]{5, 8}, new double[]{5, 4}, new double[]{1, 5, 2}), // ⁵√8 / ⁵√4 = ⁵√2
            // 5∛15 / 2∛5 = 2.5∛3
            Arguments.of(new double[]{5, 3, 15}, new double[]{2, 3, 5}, new double[]{2.5, 3, 3})
        );
    }

    @ParameterizedTest
    @MethodSource("divideRadicalsArgs")
    void testDivideRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
        // when
        final double[] result = Algebra.divideRadicals(radical1, radical2);
        // then
        assertArrayEquals(expectedResult, result, DELTA1);
    }

    static List<Arguments> simplifyRadicalArgs() {
        return List.of(
            Arguments.of(new double[]{3, 192}, new double[]{4, 3, 3}), // ∛192 = 4∛3
            Arguments.of(new double[]{2, 288}, new double[]{12, 2, 2}) // √288 = 12√2
        );
    }

    @ParameterizedTest
    @MethodSource("simplifyRadicalArgs")
    void testSimplifyRadical(double[] radical, double[] expectedResult) {
        // when
        final double[] result = Algebra.simplifyRadical(radical);
        // then
        assertArrayEquals(expectedResult, result, DELTA1);
    }

    static List<Arguments> simplifyRadicalsSumArgs() {
        return List.of(
            // 2√6 + ∜64 = 2√6 + 2∜4
            Arguments.of(new double[]{2, 6}, new double[]{4, 64}, new double[][]{{1, 2, 6}, {2, 4, 4}})
        );
    }

    @ParameterizedTest
    @MethodSource("simplifyRadicalsSumArgs")
    void testSimplifyRadicalsSum(double[] radical1, double[] radical2, double[][] expectedResult) {
        // when
        final double[][] result = Algebra.simplifyRadicalsSum(radical1, radical2);
        // then
        assertNotNull(result);
        assertEquals(2, result.length);
        assertArrayEquals(expectedResult[Constants.ARR_1ST_INDEX], result[Constants.ARR_1ST_INDEX], DELTA1);
        assertArrayEquals(expectedResult[Constants.ARR_2ND_INDEX], result[Constants.ARR_2ND_INDEX], DELTA1);
    }

    static List<Arguments> simplifyRadicalsProductArgs() {
        return List.of(
            Arguments.of(new double[]{2, 2, 6}, new double[]{4, 64}, new double[]{8, 4, 9}) // 2√6 * ∜64 = 8∜9
        );
    }

    @ParameterizedTest
    @MethodSource("simplifyRadicalsProductArgs")
    void testSimplifyRadicalsProduct(double[] radical1, double[] radical2, double[] expectedResult) {
        // when
        final double[] result = Algebra.simplifyRadicalsProduct(radical1, radical2);
        // then
        assertArrayEquals(expectedResult, result, DELTA1);
    }

    static List<Arguments> simplifyRadicalsQuotientArgs() {
        return List.of(
            // 2√6 / ∜64 = 0.03125∜9,437,184
            Arguments.of(new double[]{2, 2, 6}, new double[]{4, 64}, new double[]{0.03125, 4, 9_437_184})
        );
    }

    @ParameterizedTest
    @MethodSource("simplifyRadicalsQuotientArgs")
    void testSimplifyRadicalsQuotient(double[] radical1, double[] radical2, double[] expectedResult) {
        // when
        final double[] result = Algebra.simplifyRadicalsQuotient(radical1, radical2);
        // then
        assertArrayEquals(expectedResult, result, DELTA1);
    }

    @Test
    void testAddExponentsLaw() {
        // given
        final byte base = 5;
        final double[] exponents = new double[]{3, 2};
        // when
        final double result = Algebra.addExponentsLaw(base, exponents);
        // then
        assertEquals(3_125, result, DELTA1);
    }

    @Test
    void testSubtractExponentsLaw() {
        // given
        final byte base = 5;
        final double[] exponents = new double[]{3, 2};
        // when
        final double result = Algebra.subtractExponentsLaw(base, exponents);
        // then
        assertEquals(5, result, DELTA1);
    }

    @Test
    void testNegativeExponent() {
        // given
        final byte base = 5;
        final double exponent = -4;
        // when
        final double result = Algebra.negativeExponent(base, exponent);
        // then
        assertEquals(0.0016, result, DELTA4);
    }

    @Test
    void testLog() {
        // given
        final byte number = 4;
        // when
        final double logarithm = Algebra.log(number);
        // then
        assertEquals(0.602, logarithm, DELTA3);
    }

    @Test
    void testLog10ProductRule() {
        // given
        final double a = 5.89;
        final double b = 4.73;
        // when
        final double logarithm = Algebra.logProductRule(a, b);
        // then
        assertEquals(1.444976, logarithm, DELTA6);
    }

    static List<Arguments> logProductRuleWithBaseArgs() {
        return List.of(
            // log₄(500) = log₄(4 × 125) = log₄(4) + log₄(125) = 1 + 3.483 = 4.483
            Arguments.of(4, 125, 4, 4.483, DELTA3),
            // log₅(100) = log₅(10 × 10) = log₅(10) + log₅(10) = 1.4307 + 1.4307 = 2.8614
            Arguments.of(10, 10, 5, 2.8614, DELTA3)
        );
    }

    @ParameterizedTest
    @MethodSource("logProductRuleWithBaseArgs")
    void testLogProductRuleWithBase(double a, double b, double base, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.logProductRule(a, b, base);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    static List<Arguments> logQuotientRuleArgs() {
        return List.of(
            // log₃(10) = log₃(20 / 2) = log₃(20) - log₃(2) = 2.727 - 0.6309 = 2.096
            Arguments.of(20, 2, 3, 2.096, DELTA3),
            // log₄(64) = log₄(128 / 2) = log₄(128) - log₄(2) = 3.5 - 0.5 = 3
            Arguments.of(128, 2, 4, 3, DELTA3)
        );
    }

    @ParameterizedTest
    @MethodSource("logQuotientRuleArgs")
    void testLogQuotientRule(double a, double b, double base, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.logQuotientRule(a, b, base);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    static List<Arguments> logPowerRuleArgs() {
        return List.of(
            // log₇(64) = log₇(8²) = 2 × log₇(8) = 2 × 1.0686 = 2.1372
            Arguments.of(8, 2, 7, 2.1372, DELTA4),
            // log₇(8) = log₇(2³) = 3 × log₇(2) = 3 × 0.3562 = 1.0686
            Arguments.of(2, 3, 7, 1.0686, DELTA4),
            // log₇(4) = log₇(2²) = 2 × log₇(2) = 2 × 0.3562 = 0.7124
            Arguments.of(2, 2, 7, 0.7124, DELTA4),
            // log₃(729) = log₃(9³) = 3 × log₃(9) = 3 × 2 = 6
            Arguments.of(9, 3, 3, 6, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("logPowerRuleArgs")
    void testLogPowerRule(double number, double exponent, double base, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.logPowerRule(number, exponent, base);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    static List<Arguments> antilogArgs() {
        return List.of(
            Arguments.of(3, Math.E, 20.085537, DELTA6),
            Arguments.of(3, 10, 1000, DELTA1),
            Arguments.of(3, 2, 8, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("antilogArgs")
    void testAntilog(double logarithm, double base, double expectedResult, double delta) {
        // when
        final double antilog = Algebra.antilog(logarithm, base);
        // then
        assertEquals(expectedResult, antilog, delta);
    }

    static List<Arguments> logAddArgs() {
        return List.of(
            // 3 log₆ 4 + log₆ 9 = log₆(4³ × 9) = log₆ 576 = 3.5474
            Arguments.of(3, 4, 1, 9, 6, 3.5474, DELTA4)
        );
    }

    @ParameterizedTest
    @MethodSource("logAddArgs")
    void testLogAdd(double x, double a, double y, double b, double base, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.logAdd(a, x, b, y, base);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    static List<Arguments> logSubtractArgs() {
        return List.of(
            // 3 log₆ 4 - log₆ 9 = log₆(4³ / 9) = log₆ 7.111 = 1.09482
            Arguments.of(3, 4, 1, 9, 6, 1.09482, DELTA5)
        );
    }

    @ParameterizedTest
    @MethodSource("logSubtractArgs")
    void testLogSubtract(double x, double a, double y, double b, double base, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.logSubtract(a, x, b, y, base);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    static List<Arguments> logMultiplyNumberArgs() {
        return List.of(
            // 3 log₆ 4 = log₆(4³) = log₆ 64 = 2.3211
            Arguments.of(3, 4, 6, 2.3211, DELTA4)
        );
    }

    @ParameterizedTest
    @MethodSource("logMultiplyNumberArgs")
    void testLogMultiplyNumber(double x, double a, double base, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.logMultiplyNumber(a, x, base);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    @Test
    void testLogChangeOfBase() {
        // given
        final byte number = 100;
        final byte base = 2;
        // when
        final double logarithm = Algebra.logChangeOfBase(number, base);
        // then
        assertEquals(6.644, logarithm, DELTA3);
    }

    @Test
    void testNegativeLog() {
        // given
        final byte number = 8;
        final byte base = 2;
        // when
        final double logarithm = Algebra.negativeLog(number, base);
        // then
        assertEquals(-3, logarithm, DELTA1);
    }

    static List<Arguments> logChangeOfBaseArgs() {
        return List.of(
            Arguments.of(9, 27, 3, 0.667, DELTA3), // log₂₇(9) = log₃(9)/log₃(27) ≈ 2/3 ≈ 0.667
            // log₅(1000) = log₁₀(1000)/log₁₀(5) ≈ 3/0.699 ≈ 4.292
            Arguments.of(1000, 5, 10, 4.292, DELTA3),
            // log₂(20) = log₁₀(20)/log₁₀(2) ≈ 1.301/0.301 ≈ 4.322
            Arguments.of(20, 2, 10, 4.322, DELTA3),
            // log₁₀(20) = logₑ(20)/logₑ(10) ≈ 2.996/2.303 ≈ 1.301
            Arguments.of(20, 10, Math.E, 1.301, DELTA3)
        );
    }

    @ParameterizedTest
    @MethodSource("logChangeOfBaseArgs")
    void testLogChangeOfBase(double x, double base, double newBase, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.logChangeOfBase(x, base, newBase);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    static List<Arguments> lnArgs() {
        return List.of(
            Arguments.of(1, 0, DELTA1),
            Arguments.of(Math.E, 1, DELTA1),
            Arguments.of(2, 0.6931, DELTA4),
            Arguments.of(4, 1.3863, DELTA4)
        );
    }

    @ParameterizedTest
    @MethodSource("lnArgs")
    void testLn(double number, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.ln(number);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    @Test
    void testLnChangeOfBase() {
        // given
        final byte number = 100;
        final byte base = 2;
        // when
        final double logarithm = Algebra.lnChangeOfBase(number, base);
        // then
        assertEquals(6.644, logarithm, DELTA3);
    }

    static List<Arguments> log2Args() {
        return List.of(
            Arguments.of(ONE_EIGHTH, -3, DELTA1),
            Arguments.of(ONE_FOURTH, -2, DELTA1),
            Arguments.of(0.5, -1, DELTA1),
            Arguments.of(1, 0, DELTA1),
            Arguments.of(2, 1, DELTA1),
            Arguments.of(4, 2, DELTA1),
            Arguments.of(8, 3, DELTA1),
            Arguments.of(16, 4, DELTA1),
            Arguments.of(32, 5, DELTA1),
            Arguments.of(64, 6, DELTA1),
            Arguments.of(128, 7, DELTA1),
            Arguments.of(256, 8, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("log2Args")
    void testLog2(double number, double expectedResult, double delta) {
        // when
        final double logarithm = Algebra.log2(number);
        // then
        assertEquals(expectedResult, logarithm, delta);
    }

    static List<Arguments> doublingTimeArgs() {
        return List.of(
            Arguments.of(2, 15, 4.95948, DELTA5),
            Arguments.of(2, 430, 0.415629, DELTA6),
            Arguments.of(2, 2, 35.0028, DELTA4)
        );
    }

    @ParameterizedTest
    @MethodSource("doublingTimeArgs")
    void testDoublingTime(double initialAmount, double increase, double expectedResult, double delta) {
        // when
        final double doublingTime = Algebra.doublingTime(initialAmount, increase);
        // then
        assertEquals(expectedResult, doublingTime, delta);
    }

    static List<Arguments> conjugateArgs() {
        return List.of(
            // 2+2i => 2-2i
            Arguments.of(new Complex(2, 2), new Complex(2, -2), DELTA1),
            // 5-i => 5+i
            Arguments.of(new Complex(5, -1), new Complex(5, 1), DELTA1),
            Arguments.of(new Complex(-7, 0), new Complex(-7, 0), DELTA1), // -7 => -7
            // 7+32i => 7-32i
            Arguments.of(new Complex(7, 32), new Complex(7, -32), DELTA1),
            // 1.2-2.5i => 1.2+2.5i
            Arguments.of(new Complex(1.2, -2.5), new Complex(1.2, 2.5), DELTA1),
            // 6+0.7i => 6-0.7i
            Arguments.of(new Complex(6, 0.7), new Complex(6, -0.7), DELTA1),
            Arguments.of(new Complex(0, 1), new Complex(0, -1), DELTA1), // i => -i
            Arguments.of(new Complex(0, -3), new Complex(0, 3), DELTA1), // -3i => 3i
            Arguments.of(new Complex(18, 0), new Complex(18, 0), DELTA1), // 18 => 18
            // -9-0.03i => -9+0.03i
            Arguments.of(new Complex(-9, -0.03), new Complex(-9, 0.03), DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("conjugateArgs")
    void testConjugate(Complex complex, Complex expectedResult, double delta) {
        // when
        final var conjugate = complex.conjugate(); // conjugate(a+bi) = a-bi
        // then
        assertEquals(expectedResult.getReal(), conjugate.getReal(), delta);
        assertEquals(expectedResult.getImaginary(), conjugate.getImaginary(), delta);
    }

    static List<Arguments> binomialCoefficientArgs() {
        return List.of(
            Arguments.of(4, 2, 6),
            Arguments.of(6, 2, 15)
        );
    }

    @ParameterizedTest
    @MethodSource("binomialCoefficientArgs")
    void testBinomialCoefficient(long totalItems, long numberOfItemsChosen, long expectedResult) {
        // when
        final long result = Algebra.binomialCoefficient(totalItems, numberOfItemsChosen);
        // then
        assertEquals(expectedResult, result);
    }

    static List<Arguments> multiplyBinomialsArgs() {
        return List.of(
            // (3x−2)(x+5) = (3 × 1)x2² + ((3 × 5) + (-2 × 1))x + (-2 × 5)
            // = 3x² + (15 + -2)x + -10 = 3x² + 13x + -10
            Arguments.of(
                new double[]{-2, 3},
                new double[]{5, 1},
                new double[]{-10, 13, 3},
                DELTA1
            )
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyBinomialsArgs")
    void testMultiplyBinomials(double[] binomial1, double[] binomial2, double[] expectedResult, double delta) {
        // when
        final double[] result = Algebra.multiplyBinomials(binomial1, binomial2);
        // then
        assertArrayEquals(expectedResult, result, delta);
    }

    static List<Arguments> discriminantArgs() {
        return List.of(
            // quadratic
            Arguments.of(new double[]{-18, 21, -8}, -135, DELTA1), // -8x² + 21x - 18
            Arguments.of(new double[]{-7, 2, 9}, 256, DELTA1), // 0 = -7x² + 2x + 9; D = ±√256=±16
            Arguments.of(new double[]{6, 10, -1}, 124, DELTA1), // 6x² + 10x - 1 = 0
            // cubic
            Arguments.of(new double[]{2, -3, 0, 1}, 0, DELTA1), // x³ - 3x + 2 = 0
            // quartic
            Arguments.of(new double[]{1, 5, 0, -1, 4}, -238_383, DELTA1) // 4x⁴ - x³ + 5x + 1
        );
    }

    @ParameterizedTest
    @MethodSource("discriminantArgs")
    void testDiscriminant(double[] polynomialTerms, double expectedResult, double delta) {
        // when
        final double discriminant = Algebra.discriminant(polynomialTerms);
        // then
        assertEquals(expectedResult, discriminant, delta);
    }

    static List<Arguments> addPolynomialsArgs() {
        return List.of(
            // P(x) = 4x⁴ - x³ + 5x + 1
            // Q(x) = x⁵ + 4x⁴ - 7x³ - 3x² + x + 12
            // x⁵ + 8x⁴ - 8x³ - 3x² + 6x + 13
            Arguments.of(
                new double[]{1, 5, 0, -1, 4},
                new double[]{12, 1, -3, -7, 4, 1},
                new double[]{13, 6, -3, -8, 8, 1},
                DELTA1
            )
        );
    }

    @ParameterizedTest
    @MethodSource("addPolynomialsArgs")
    void testAddPolynomials(double[] polynomial1, double[] polynomial2, double[] expectedResult, double delta) {
        // when
        final double[] added = Algebra.addPolynomials(polynomial1, polynomial2);
        // then
        assertArrayEquals(expectedResult, added, delta);
    }

    static List<Arguments> subtractPolynomialsArgs() {
        return List.of(
            // P(x) = 4x⁴ - x³ + 5x + 1
            // Q(x) = x⁵ + 4x⁴ - 7x³ - 3x² + x + 12
            // -x⁵ + 6x³ + 3x² + 4x - 11
            Arguments.of(
                new double[]{1, 5, 0, -1, 4},
                new double[]{12, 1, -3, -7, 4, 1},
                new double[]{-11, 4, 3, 6, 0, -1},
                DELTA1
            )
        );
    }

    @ParameterizedTest
    @MethodSource("subtractPolynomialsArgs")
    void testSubtractPolynomials(double[] polynomial1, double[] polynomial2,
                                 double[] expectedResult, double delta) {
        // when
        final double[] subtracted = Algebra.subtractPolynomials(polynomial1, polynomial2);
        // then
        assertArrayEquals(expectedResult, subtracted, delta);
    }

    static List<Arguments> multiplyPolynomialsArgs() {
        return List.of(
            // x⁴ - 3x² + 2x + 4
            // -0.5x² + x - 2
            // -0.5x⁶ + x⁵ - 0.5x⁴ - 4x³ + 6x² - 8
            Arguments.of(
                new double[]{4, 2, -3, 0, 1},
                new double[]{-2, 1, -0.5},
                new double[]{-8, 0, 6, -4, -0.5, 1, -0.5},
                DELTA1
            )
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyPolynomialsArgs")
    void testMultiplyPolynomials(double[] polynomial1, double[] polynomial2,
                                 double[] expectedResult, double delta) {
        // when
        final double[] multiplied = Algebra.multiplyPolynomials(polynomial1, polynomial2);
        // then
        assertArrayEquals(expectedResult, multiplied, delta);
    }

    static List<Arguments> dividePolynomialsArgs() {
        return List.of(
            // (x⁴ - 27x³ + 239x² - 753x + 540) / (x - 1) = x³ - 26x² + 213x - 540
            Arguments.of(
                new double[]{540, -753, 239, -27, 1},
                new double[]{-1, 1},
                new double[]{-540, 213, -26, 1},
                DELTA1
            )
        );
    }

    @ParameterizedTest
    @MethodSource("dividePolynomialsArgs")
    void testDividePolynomials(double[] polynomial1, double[] polynomial2,
                               double[] expectedResult, double delta) {
        // when
        final double[] divided = Algebra.dividePolynomials(polynomial1, polynomial2);
        // then
        assertArrayEquals(expectedResult, divided, delta);
    }

    static List<Arguments> quadraticStdFormulaToVertexArgs() {
        return List.of(
            // Standard form: f(x) = 4x² + 4x - 3
            // Vertex form: f(x) = 4(x + 0.5)² - 4
            Arguments.of(new double[]{-3, 4, 4}, new double[]{-4, -0.5, 4}, DELTA1),
            // Standard form: f(x) = x² + 1
            // Vertex form: f(x) = x² + 1
            Arguments.of(new double[]{1, 0, 1}, new double[]{1, 0, 1}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("quadraticStdFormulaToVertexArgs")
    void testQuadraticStdFormulaToVertex(double[] quadratic, double[] expectedResult, double delta) {
        // when
        final double[] terms = Algebra.quadraticStdFormulaToVertex(quadratic);
        // then
        assertArrayEquals(expectedResult, terms, delta);
    }

    static List<Arguments> quadraticStdFormulaToFactoredArgs() {
        return List.of(
            // Standard form: f(x) = 4x² + 4x - 3
            // Vertex form: f(x) = 4(x - 0.5)(x + 1.5)
            Arguments.of(new double[]{-3, 4, 4}, new double[]{4, 0.5, -1.5}, DELTA1),
            // Standard form: f(x) = x² + 1
            // Factored form: f(x) = (x + i)(x - i)
            Arguments.of(new double[]{1, 0, 1}, new double[]{1, 0, 1}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("quadraticStdFormulaToFactoredArgs")
    void testQuadraticStdFormulaToFactored(double[] quadratic, double[] expectedResult, double delta) {
        // when
        final double[] terms = Algebra.quadraticStdFormulaToFactored(quadratic);
        // then
        assertArrayEquals(expectedResult, terms, delta);
    }

    static List<Arguments> quadraticRootsArgs() {
        return List.of(
            // 4x² + 3x – 7 = -4 – x
            // 4x² + (3 + 1)x + (-7 + 4) = 0
            // 4x² + 4x - 3 = 0
            Arguments.of(new double[]{-3, 4, 4}, new double[]{0.5, -1.5}, DELTA1),
            // x² + 1 = 0
            // Δ = b² – 4ac = 0² - 4×1×1 = -4
            // (0 + 2i) / (2×1) = i
            // (0 - 2i) / (2×1) = -i
            Arguments.of(new double[]{1, 0, 1}, new double[]{0, 1}, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("quadraticRootsArgs")
    void testQuadraticRoots(double[] quadratic, double[] expectedResult, double delta) {
        // when
        final double[] roots = Algebra.quadraticRoots(quadratic);
        // then
        assertArrayEquals(expectedResult, roots, delta);
    }

    @Test
    void testRMS() {
        // given
        final double[] dataset = new double[]{2, 6, 3, -4, 2, 4, -1, 3, 2, -1};
        // when
        final double rms = Algebra.rms(dataset);
        // then
        assertEquals(3.1623, rms, DELTA4);
    }
}
