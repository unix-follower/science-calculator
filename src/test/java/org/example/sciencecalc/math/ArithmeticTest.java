package org.example.sciencecalc.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ArithmeticTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;

    @ParameterizedTest
    @CsvSource({
        "2,true",
        "3,true",
        "5,true",
        "7,true",
        "11,true",
        "13,true",
        "17,true",
        "19,true",
        "23,true",
        "29,true",
        "31,true",
        "37,true",
        "41,true",
        "43,true",
        "47,true",
        "53,true",
        "59,true",
        "61,true",
        "67,true",
        "71,true",
        "73,true",
        "79,true",
        "83,true",
        "89,true",
        "97,true",
    })
    void testIsPrime(double number, boolean expectedResult) {
        // when
        final boolean prime = Arithmetic.isPrime(number);
        // then
        assertEquals(expectedResult, prime);
    }

    @ParameterizedTest
    @CsvSource({
        "5,9,true",
        "6,11,true",
        "18,30,false",
        "18,35,true",
    })
    void testIsCoprime(long x, long x1, boolean expectedResult) {
        // when
        final boolean coprime = Arithmetic.isCoprime(x, x1);
        // then
        assertEquals(expectedResult, coprime);
    }

    @ParameterizedTest
    @CsvSource({
        "2,false",
        "3,false",
        "5,false",
        "6,true",
        "7,false",
        "8,true",
        "9,true",
        "10,true",
    })
    void testIsCompositeNumber(long number, boolean expectedResult) {
        // when
        final boolean compositeNumber = Arithmetic.isCompositeNumber(number);
        // then
        assertEquals(expectedResult, compositeNumber);
    }

    static List<Arguments> primeFactorizationArgs() {
        return List.of(
            Arguments.of(24, new long[]{2, 2, 2, 3}),
            Arguments.of(80, new long[]{2, 2, 2, 2, 5}),
            Arguments.of(121, new long[]{11, 11}),
            Arguments.of(121.0, new long[]{11, 11})
        );
    }

    @ParameterizedTest
    @MethodSource("primeFactorizationArgs")
    void testPrimeFactorization(double number, long[] expectedResult) {
        // when
        final long[] primes = Arithmetic.primeFactorization(number);
        // then
        assertArrayEquals(expectedResult, primes);
    }

    static List<Arguments> lcmArgs() {
        return List.of(
            Arguments.of(new double[]{18, 24}, 72),
            Arguments.of(new double[]{2, 4, 6, 8, 10, 12}, 120)
        );
    }

    @ParameterizedTest
    @MethodSource("lcmArgs")
    void testLcmWithPrimeFactorization(double[] numbers, long expectedResult) {
        // when
        final long lcm = Arithmetic.lcmWithPrimeFactorization(numbers);
        // then
        assertEquals(expectedResult, lcm);
    }

    @ParameterizedTest
    @MethodSource("lcmArgs")
    void testLcmWithGcf(double[] numbers, long expectedResult) {
        // when
        final long lcm = Arithmetic.lcmWithGcf(numbers);
        // then
        assertEquals(expectedResult, lcm);
    }

    @ParameterizedTest
    @CsvSource({
        "2,3,4,5,4"
    })
    void testLcmOverGcfOfFractions(long numerator1, long denominator1,
                                   long numerator2, long denominator2, long expectedResult) {
        // given
        final long[] fraction1 = new long[]{numerator1, denominator1};
        final long[] fraction2 = new long[]{numerator2, denominator2};
        // when
        final long lcm = Arithmetic.lcmOverGcfOfFractions(fraction1, fraction2);
        // then
        assertEquals(expectedResult, lcm);
    }

    static List<Arguments> gcfArgs() {
        return List.of(
            Arguments.of(new double[]{72, 40}, 8),
            Arguments.of(new double[]{72, 40, 32}, 8),
            Arguments.of(new double[]{33_264, 35_640}, 2376)
        );
    }

    @ParameterizedTest
    @MethodSource("gcfArgs")
    void testGcfWithEuclideanAlg(double[] numbers, long expectedResult) {
        // when
        final long gcf = Arithmetic.gcfWithEuclideanAlg(numbers);
        // then
        assertEquals(expectedResult, gcf);
    }

    @ParameterizedTest
    @MethodSource("gcfArgs")
    void testGcfWithBinaryAlg(double[] numbers, long expectedResult) {
        // when
        final long gcf = Arithmetic.gcfWithBinaryAlg(numbers);
        // then
        assertEquals(expectedResult, gcf);
    }

    @ParameterizedTest
    @MethodSource("gcfArgs")
    void testGcfWithCommonFactors(double[] numbers, long expectedResult) {
        // when
        final long gcf = Arithmetic.gcfWithCommonFactors(numbers);
        // then
        assertEquals(expectedResult, gcf);
    }

    @ParameterizedTest
    @MethodSource("gcfArgs")
    void testGcfWithLcm(double[] numbers, long expectedResult) {
        // when
        final long gcf = Arithmetic.gcfWithLcm(numbers);
        // then
        assertEquals(expectedResult, gcf);
    }

    static List<Arguments> factorArgs() {
        return List.of(
            Arguments.of(1, new long[]{1}),
            Arguments.of(2, new long[]{1, 2}),
            Arguments.of(3, new long[]{1, 3}),
            Arguments.of(4, new long[]{1, 2, 4}),
            Arguments.of(5, new long[]{1, 5}),
            Arguments.of(6, new long[]{1, 2, 3, 6}),
            Arguments.of(7, new long[]{1, 7}),
            Arguments.of(8, new long[]{1, 2, 4, 8}),
            Arguments.of(9, new long[]{1, 3, 9}),
            Arguments.of(10, new long[]{1, 2, 5, 10}),
            Arguments.of(11, new long[]{1, 11}),
            Arguments.of(12, new long[]{1, 2, 3, 4, 6, 12}),
            Arguments.of(13, new long[]{1, 13}),
            Arguments.of(14, new long[]{1, 2, 7, 14}),
            Arguments.of(15, new long[]{1, 3, 5, 15}),
            Arguments.of(16, new long[]{1, 2, 4, 8, 16}),
            Arguments.of(17, new long[]{1, 17}),
            Arguments.of(18, new long[]{1, 2, 3, 6, 9, 18}),
            Arguments.of(19, new long[]{1, 19}),
            Arguments.of(20, new long[]{1, 2, 4, 5, 10, 20}),
            Arguments.of(21, new long[]{1, 3, 7, 21}),
            Arguments.of(22, new long[]{1, 2, 11, 22}),
            Arguments.of(23, new long[]{1, 23}),
            Arguments.of(24, new long[]{1, 2, 3, 4, 6, 8, 12, 24}),
            Arguments.of(25, new long[]{1, 5, 25}),
            Arguments.of(26, new long[]{1, 2, 13, 26}),
            Arguments.of(27, new long[]{1, 3, 9, 27}),
            Arguments.of(28, new long[]{1, 2, 4, 7, 14, 28}),
            Arguments.of(29, new long[]{1, 29}),
            Arguments.of(30, new long[]{1, 2, 3, 5, 6, 10, 15, 30}),
            Arguments.of(31, new long[]{1, 31}),
            Arguments.of(32, new long[]{1, 2, 4, 8, 16, 32}),
            Arguments.of(33, new long[]{1, 3, 11, 33}),
            Arguments.of(34, new long[]{1, 2, 17, 34}),
            Arguments.of(35, new long[]{1, 5, 7, 35}),
            Arguments.of(36, new long[]{1, 2, 3, 4, 6, 9, 12, 18, 36}),
            Arguments.of(37, new long[]{1, 37}),
            Arguments.of(39, new long[]{1, 3, 13, 39}),
            Arguments.of(40, new long[]{1, 2, 4, 5, 8, 10, 20, 40}),
            Arguments.of(41, new long[]{1, 41}),
            Arguments.of(42, new long[]{1, 2, 3, 6, 7, 14, 21, 42}),
            Arguments.of(43, new long[]{1, 43}),
            Arguments.of(44, new long[]{1, 2, 4, 11, 22, 44}),
            Arguments.of(45, new long[]{1, 3, 5, 9, 15, 45}),
            Arguments.of(46, new long[]{1, 2, 23, 46}),
            Arguments.of(47, new long[]{1, 47}),
            Arguments.of(48, new long[]{1, 2, 3, 4, 6, 8, 12, 16, 24, 48}),
            Arguments.of(49, new long[]{1, 7, 49}),
            Arguments.of(50, new long[]{1, 2, 5, 10, 25, 50}),
            Arguments.of(51, new long[]{1, 3, 17, 51}),
            Arguments.of(52, new long[]{1, 2, 4, 13, 26, 52}),
            Arguments.of(53, new long[]{1, 53}),
            Arguments.of(54, new long[]{1, 2, 3, 6, 9, 18, 27, 54}),
            Arguments.of(55, new long[]{1, 5, 11, 55}),
            Arguments.of(56, new long[]{1, 2, 4, 7, 8, 14, 28, 56}),
            Arguments.of(57, new long[]{1, 3, 19, 57}),
            Arguments.of(58, new long[]{1, 2, 29, 58}),
            Arguments.of(59, new long[]{1, 59}),
            Arguments.of(60, new long[]{1, 2, 3, 4, 5, 6, 10, 12, 15, 20, 30, 60}),
            Arguments.of(61, new long[]{1, 61}),
            Arguments.of(62, new long[]{1, 2, 31, 62}),
            Arguments.of(63, new long[]{1, 3, 7, 9, 21, 63}),
            Arguments.of(64, new long[]{1, 2, 4, 8, 16, 32, 64}),
            Arguments.of(65, new long[]{1, 5, 13, 65}),
            Arguments.of(66, new long[]{1, 2, 3, 6, 11, 22, 33, 66}),
            Arguments.of(67, new long[]{1, 67}),
            Arguments.of(68, new long[]{1, 2, 4, 17, 34, 68}),
            Arguments.of(69, new long[]{1, 3, 23, 69}),
            Arguments.of(70, new long[]{1, 2, 5, 7, 10, 14, 35, 70}),
            Arguments.of(71, new long[]{1, 71}),
            Arguments.of(72, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 18, 24, 36, 72}),
            Arguments.of(73, new long[]{1, 73}),
            Arguments.of(74, new long[]{1, 2, 37, 74}),
            Arguments.of(75, new long[]{1, 3, 5, 15, 25, 75}),
            Arguments.of(76, new long[]{1, 2, 4, 19, 38, 76}),
            Arguments.of(77, new long[]{1, 7, 11, 77}),
            Arguments.of(78, new long[]{1, 2, 3, 6, 13, 26, 39, 78}),
            Arguments.of(79, new long[]{1, 79}),
            Arguments.of(80, new long[]{1, 2, 4, 5, 8, 10, 16, 20, 40, 80}),
            Arguments.of(81, new long[]{1, 3, 9, 27, 81}),
            Arguments.of(82, new long[]{1, 2, 41, 82}),
            Arguments.of(83, new long[]{1, 83}),
            Arguments.of(84, new long[]{1, 2, 3, 4, 6, 7, 12, 14, 21, 28, 42, 84}),
            Arguments.of(85, new long[]{1, 5, 17, 85}),
            Arguments.of(86, new long[]{1, 2, 43, 86}),
            Arguments.of(87, new long[]{1, 3, 29, 87}),
            Arguments.of(88, new long[]{1, 2, 4, 8, 11, 22, 44, 88}),
            Arguments.of(89, new long[]{1, 89}),
            Arguments.of(90, new long[]{1, 2, 3, 5, 6, 9, 10, 15, 18, 30, 45, 90}),
            Arguments.of(91, new long[]{1, 7, 13, 91}),
            Arguments.of(92, new long[]{1, 2, 4, 23, 46, 92}),
            Arguments.of(93, new long[]{1, 3, 31, 93}),
            Arguments.of(94, new long[]{1, 2, 47, 94}),
            Arguments.of(95, new long[]{1, 5, 19, 95}),
            Arguments.of(96, new long[]{1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 96}),
            Arguments.of(97, new long[]{1, 97}),
            Arguments.of(98, new long[]{1, 2, 7, 14, 49, 98}),
            Arguments.of(99, new long[]{1, 3, 9, 11, 33, 99}),
            Arguments.of(100, new long[]{1, 2, 4, 5, 10, 20, 25, 50, 100}),
            Arguments.of(104, new long[]{1, 2, 4, 8, 13, 26, 52, 104}),
            Arguments.of(105, new long[]{1, 3, 5, 7, 15, 21, 35, 105}),
            Arguments.of(108, new long[]{1, 2, 3, 4, 6, 9, 12, 18, 27, 36, 54, 108}),
            Arguments.of(110, new long[]{1, 2, 5, 10, 11, 22, 55, 110}),
            Arguments.of(112, new long[]{1, 2, 4, 7, 8, 14, 16, 28, 56, 112}),
            Arguments.of(117, new long[]{1, 3, 9, 13, 39, 117}),
            Arguments.of(120, new long[]{1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 20, 24, 30, 40, 60, 120}),
            Arguments.of(121, new long[]{1, 11, 121}),
            Arguments.of(125, new long[]{1, 5, 25, 125}),
            Arguments.of(126, new long[]{1, 2, 3, 6, 7, 9, 14, 18, 21, 42, 63, 126}),
            Arguments.of(130, new long[]{1, 2, 5, 10, 13, 26, 65, 130}),
            Arguments.of(132, new long[]{1, 2, 3, 4, 6, 11, 12, 22, 33, 44, 66, 132}),
            Arguments.of(135, new long[]{1, 3, 5, 9, 15, 27, 45, 135}),
            Arguments.of(140, new long[]{1, 2, 4, 5, 7, 10, 14, 20, 28, 35, 70, 140}),
            Arguments.of(144, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 16, 18, 24, 36, 48, 72, 144}),
            Arguments.of(147, new long[]{1, 3, 7, 21, 49, 147}),
            Arguments.of(150, new long[]{1, 2, 3, 5, 6, 10, 15, 25, 30, 50, 75, 150}),
            Arguments.of(162, new long[]{1, 2, 3, 6, 9, 18, 27, 54, 81, 162}),
            Arguments.of(169, new long[]{1, 13, 169}),
            Arguments.of(175, new long[]{1, 5, 7, 25, 35, 175}),
            Arguments.of(180, new long[]{1, 2, 3, 4, 5, 6, 9, 10, 12, 15, 18, 20, 30, 36, 45, 60, 90, 180}),
            Arguments.of(189, new long[]{1, 3, 7, 9, 21, 27, 63, 189}),
            Arguments.of(192, new long[]{1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 192}),
            Arguments.of(196, new long[]{1, 2, 4, 7, 14, 28, 49, 98, 196}),
            Arguments.of(200, new long[]{1, 2, 4, 5, 8, 10, 20, 25, 40, 50, 100, 200}),
            Arguments.of(210, new long[]{1, 2, 3, 5, 6, 7, 10, 14, 15, 21, 30, 35, 42, 70, 105, 210}),
            Arguments.of(216, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 18, 24, 27, 36, 54, 72, 108, 216}),
            Arguments.of(225, new long[]{1, 3, 5, 9, 15, 25, 45, 75, 225}),
            Arguments.of(240, new long[]{1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 16, 20, 24, 30, 40, 48, 60,
                80, 120, 240}),
            Arguments.of(245, new long[]{1, 5, 7, 35, 49, 245}),
            Arguments.of(250, new long[]{1, 2, 5, 10, 25, 50, 125, 250}),
            Arguments.of(256, new long[]{1, 2, 4, 8, 16, 32, 64, 128, 256}),
            Arguments.of(270, new long[]{1, 2, 3, 5, 6, 9, 10, 15, 18, 27, 30, 45, 54, 90, 135, 270}),
            Arguments.of(288, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 16, 18, 24, 32, 36, 48, 72, 96, 144, 288}),
            Arguments.of(294, new long[]{1, 2, 3, 6, 7, 14, 21, 42, 49, 98, 147, 294}),
            Arguments.of(300, new long[]{1, 2, 3, 4, 5, 6, 10, 12, 15, 20, 25, 30, 50, 60, 75, 100, 150, 300}),
            Arguments.of(343, new long[]{1, 7, 49, 343}),
            Arguments.of(360, new long[]{1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 18, 20, 24, 30, 36, 40,
                45, 60, 72, 90, 120, 180, 360}),
            Arguments.of(375, new long[]{1, 3, 5, 15, 25, 75, 125, 375}),
            Arguments.of(400, new long[]{1, 2, 4, 5, 8, 10, 16, 20, 25, 40, 50, 80, 100, 200, 400}),
            Arguments.of(500, new long[]{1, 2, 4, 5, 10, 20, 25, 50, 100, 125, 250, 500}),
            Arguments.of(625, new long[]{1, 5, 25, 125, 625})
        );
    }

    @ParameterizedTest
    @MethodSource("factorArgs")
    void testFactor(double number, long[] expectedResult) {
        // when
        final long[] factors = Arithmetic.factor(number);
        // then
        assertArrayEquals(expectedResult, factors);
    }

    static List<Arguments> commonFactorsArgs() {
        return List.of(
            Arguments.of(2.0, 4.0, new long[]{1, 2}),
            Arguments.of(2, 4, new long[]{1, 2}),
            Arguments.of(3, 9, new long[]{1, 3}),
            Arguments.of(4, 6, new long[]{1, 2}),
            Arguments.of(4, 8, new long[]{1, 2, 4}),
            Arguments.of(5, 10, new long[]{1, 5}),
            Arguments.of(6, 12, new long[]{1, 2, 3, 6}),
            Arguments.of(7, 9, new long[]{1})
        );
    }

    @ParameterizedTest
    @MethodSource("commonFactorsArgs")
    void testCommonFactors(double number1, double number2, long[] expectedResult) {
        // when
        final long[] factors = Arithmetic.commonFactors(number1, number2);
        // then
        assertArrayEquals(expectedResult, factors);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,true",
        "2.0,true",
        "2.1,false",
        "3,false",
        "4,true",
        "5,false",
        "6,true",
    })
    void testIsDivisibleBy2(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy2(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,false",
        "3,true",
        "3.0,true",
        "3.1,false",
        "4,false",
        "5,false",
        "6,true",
        "9,true",
    })
    void testIsDivisibleBy3(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy3(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,false",
        "3,false",
        "4,true",
        "4.0,true",
        "4.1,false",
        "5,false",
        "6,false",
        "8,true",
    })
    void testIsDivisibleBy4(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy4(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,false",
        "3,false",
        "4,false",
        "5,true",
        "5.0,true",
        "5.1,false",
        "6,false",
        "10,true",
    })
    void testIsDivisibleBy5(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy5(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,false",
        "3,false",
        "4,false",
        "5,false",
        "6,true",
        "6.0,true",
        "6.1,false",
        "7,false",
        "12,true",
    })
    void testIsDivisibleBy6(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy6(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    static List<Arguments> isDivisibleBy7Args() {
        return List.of(
            Arguments.of(1, false),
            Arguments.of(2, false),
            Arguments.of(3, false),
            Arguments.of(4, false),
            Arguments.of(5, false),
            Arguments.of(6, false),
            Arguments.of(7, true),
            Arguments.of(7.0, true),
            Arguments.of(7.1, false),
            Arguments.of(9, false),
            Arguments.of(49, true),
            Arguments.of(77, true),
            Arguments.of(777, true)
        );
    }

    @ParameterizedTest
    @MethodSource("isDivisibleBy7Args")
    void testIsDivisibleBy7(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy7(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @MethodSource("isDivisibleBy7Args")
    void testIsDivisibleBy7ViaSubtractTwiceLastDigit(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy7ViaSubtractTwiceLastDigit(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @MethodSource("isDivisibleBy7Args")
    void testIsDivisibleBy7ViaReverseOrder(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy7ViaReverseOrder(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,false",
        "3,false",
        "4,false",
        "5,false",
        "6,false",
        "8,true",
        "8.0,true",
        "8.1,false",
        "9,false",
        "16,true",
        "88,true",
        "888,true",
    })
    void testIsDivisibleBy8(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy8(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,false",
        "3,false",
        "4,false",
        "5,false",
        "6,false",
        "9,true",
        "9.0,true",
        "9.1,false",
        "18,true",
        "81,true",
        "999,true",
    })
    void testIsDivisibleBy9(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy9(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",
        "2,false",
        "3,false",
        "4,false",
        "5,false",
        "6,false",
        "10,true",
        "10.0,true",
        "10.1,false",
        "11,false",
        "20,true",
        "100,true",
    })
    void testIsDivisibleBy10(double number, boolean expectedResult) {
        // when
        final boolean divisible = Arithmetic.isDivisibleBy10(number);
        // then
        assertEquals(expectedResult, divisible);
    }

    static List<Arguments> solveProportionArgs() {
        return List.of(
            Arguments.of(new double[]{7, 12}, new double[]{Double.POSITIVE_INFINITY, 96},
                new double[]{56, 96}), // 7/12 = x/96
            Arguments.of(new double[]{7, 12}, new double[]{56, Double.POSITIVE_INFINITY},
                new double[]{56, 96}), // 7/12 = 56/x
            Arguments.of(new double[]{56, 96}, new double[]{7, Double.POSITIVE_INFINITY},
                new double[]{7, 12}), // 56/96 = 7/x
            Arguments.of(new double[]{56, 96}, new double[]{Double.POSITIVE_INFINITY, 12},
                new double[]{7, 12}) // 56/96 = x/12
        );
    }

    @ParameterizedTest
    @MethodSource("solveProportionArgs")
    void testSolveProportion(double[] proportion, double[] proportionWithUnknown, double[] expectedResult) {
        // when
        final double[] result = Arithmetic.solveProportion(proportion, proportionWithUnknown);
        // then
        assertArrayEquals(expectedResult, result, DELTA1);
    }

    @Test
    void testFindEquivalentRatio() {
        // given
        // 3/8 = x/27
        final double[] ratio = new double[]{3, 8};
        final double[] ratioWithUnknown = new double[]{Double.POSITIVE_INFINITY, 72};
        // when
        final double[] result = Arithmetic.findEquivalentRatio(ratio, ratioWithUnknown);
        // then
        assertArrayEquals(new double[]{27, 72}, result, DELTA1);
    }

    @Test
    void testScaleUpRatio() {
        // given
        final double[] ratio = new double[]{3, 8};
        final byte coefficient = 2;
        // when
        final double[] result = Arithmetic.scaleUpRatio(ratio, coefficient);
        // then
        assertArrayEquals(new double[]{6, 16}, result, DELTA1);
    }

    @Test
    void testScaleDownRatio() {
        // given
        final double[] ratio = new double[]{27, 72};
        final byte coefficient = 4;
        // when
        final double[] result = Arithmetic.scaleDownRatio(ratio, coefficient);
        // then
        assertArrayEquals(new double[]{6.75, 18}, result, DELTA2);
    }

    @Test
    void testSimplifyRatio() {
        // given
        final double[] ratio = new double[]{27, 72};
        // when
        final double[] result = Arithmetic.simplifyRatio(ratio);
        // then
        assertArrayEquals(new double[]{3, 8}, result, DELTA2);
    }

    @Test
    void testSimplifyRatio1toN() {
        // given
        final double[] ratio = new double[]{27, 72};
        // when
        final double[] result = Arithmetic.simplifyRatio1toN(ratio);
        // then
        assertArrayEquals(new double[]{1, 2.6667}, result, DELTA4);
    }

    @Test
    void testSimplifyRatioNto1() {
        // given
        final double[] ratio = new double[]{27, 72};
        // when
        final double[] result = Arithmetic.simplifyRatioNto1(ratio);
        // then
        assertArrayEquals(new double[]{0.375, 1}, result, DELTA3);
    }

    @Test
    void testGoldenRatioGivenLongerSection() {
        // given
        final double longerSection = 0.850651;
        // when
        final double[] result = Arithmetic.goldenRatioGivenLongerSection(longerSection);
        // then
        assertArrayEquals(new double[]{longerSection, 0.526, 1.376}, result, DELTA3);
    }

    @Test
    void testGoldenRatioGivenShorterSection() {
        // given
        final double shorterSection = 0.526;
        // when
        final double[] result = Arithmetic.goldenRatioGivenShorterSection(shorterSection);
        // then
        assertArrayEquals(new double[]{0.851, shorterSection, 1.377}, result, DELTA3);
    }

    @Test
    void testGoldenRatioGivenWhole() {
        // given
        final double whole = 1.377;
        // when
        final double[] result = Arithmetic.goldenRatioGivenWhole(whole);
        // then
        assertArrayEquals(new double[]{0.851, 0.526, whole}, result, DELTA3);
    }

    static List<Arguments> addFractionsArgs() {
        return List.of(
            Arguments.of(new long[]{3, 5}, new long[]{1, 5}, new long[]{4, 5}), // 3/5 + 1/5 = 4/5
            Arguments.of(new long[]{2, 5}, new long[]{3, 10}, new long[]{7, 10}), // 2/5 + 3/10 = 7/10
            // 1/2 + 3/5 = 11/10 = 1 1/10
            Arguments.of(new long[]{1, 2}, new long[]{3, 5}, new long[]{1, 1, 10}),
            // 2 3/5 + 1 1/2 = 13/5 + 3/2 = 41/10 = 4 1/10
            Arguments.of(new long[]{2, 3, 5}, new long[]{1, 1, 2}, new long[]{4, 1, 10})
        );
    }

    @ParameterizedTest
    @MethodSource("addFractionsArgs")
    void testAddFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
        // when
        final long[] result = Arithmetic.addFractions(fraction1, fraction2);
        // then
        assertArrayEquals(expectedResult, result);
    }

    static List<Arguments> subtractFractionsArgs() {
        return List.of(
            Arguments.of(new long[]{3, 5}, new long[]{1, 5}, new long[]{2, 5}), // 3/5 - 1/5 = 2/5
            // 2/5 - 3/10 = 4/10 - 3/10 = 1/10
            Arguments.of(new long[]{2, 5}, new long[]{3, 10}, new long[]{1, 10}),
            // 2 3/5 − 1 1/2 = 13/5 − 3/2 = 11/10 = 1 1/10
            Arguments.of(new long[]{2, 3, 5}, new long[]{1, 1, 2}, new long[]{1, 1, 10})
        );
    }

    @ParameterizedTest
    @MethodSource("subtractFractionsArgs")
    void testSubtractFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
        // when
        final long[] result = Arithmetic.subtractFractions(fraction1, fraction2);
        // then
        assertArrayEquals(expectedResult, result);
    }

    static List<Arguments> multiplyFractionsArgs() {
        return List.of(
            Arguments.of(new long[]{2, 3}, new long[]{5, 6}, new long[]{5, 9}), // 2/3 ⋅ 5/6 = 10/18 = 5/9
            Arguments.of(new long[]{3, 1}, new long[]{5, 7}, new long[]{2, 1, 7}), // 3/1 ⋅ 5/7 = 15/7 = 2 1/7
            // 2 1/2 ⋅ 3 1/4 = 5/2 ⋅ 13/4 = 65/8 = 8 1/8
            Arguments.of(new long[]{2, 1, 2}, new long[]{3, 1, 4}, new long[]{8, 1, 8})
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyFractionsArgs")
    void testMultiplyFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
        // when
        final long[] result = Arithmetic.multiplyFractions(fraction1, fraction2);
        // then
        assertArrayEquals(expectedResult, result);
    }

    static List<Arguments> divideFractionsArgs() {
        return List.of(
            Arguments.of(new long[]{1, 2}, new long[]{3, 5}, new long[]{5, 6}), // 1/2 ÷ 3/5 = 1/2 ⋅ 5/3 = 5/6
            // 2 1/2 ÷ 3 1/4 = 5/2 ÷ 13/4 = 5/2 ⋅ 4/13 = 10/13
            Arguments.of(new long[]{2, 1, 2}, new long[]{3, 1, 4}, new long[]{10, 13})
        );
    }

    @ParameterizedTest
    @MethodSource("divideFractionsArgs")
    void testDivideFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
        // when
        final long[] result = Arithmetic.divideFractions(fraction1, fraction2);
        // then
        assertArrayEquals(expectedResult, result);
    }

    @Test
    void testSimplifyFraction() {
        // given
        final long[] fraction = new long[]{42, 126}; // 42/126 = 1/3
        // when
        final long[] result = Arithmetic.simplifyFraction(fraction);
        // then
        assertArrayEquals(new long[]{1, 3}, result);
    }

    static List<Arguments> decimalToFractionArgs() {
        return List.of(
            Arguments.of(0.32, new long[]{8, 25}),
            Arguments.of(0.3333, new long[]{3333, 10000})
        );
    }

    @ParameterizedTest
    @MethodSource("decimalToFractionArgs")
    void testDecimalToFraction(double decimal, long[] expectedResult) {
        // when
        final long[] fraction = Arithmetic.decimalToFraction(decimal);
        // then
        assertArrayEquals(expectedResult, fraction);
    }

    static List<Arguments> fractionToDecimalArgs() {
        return List.of(
            Arguments.of(new long[]{2, 1, 2}, 2.5),
            Arguments.of(new long[]{3, 1, 4}, 3.25)
        );
    }

    @ParameterizedTest
    @MethodSource("fractionToDecimalArgs")
    void testFractionToDecimal(long[] fraction, double expectedResult) {
        // when
        final double decimal = Arithmetic.fractionToDecimal(fraction);
        // then
        assertEquals(expectedResult, decimal, DELTA3);
    }

    @Test
    void testAverage() {
        // given
        final double[] dataset = new double[]{24., 55., 17., 87., 100.};
        // when
        final double average = Arithmetic.average(dataset);
        // then
        assertEquals(56.6, average, DELTA1);
    }

    @Test
    void testWeightedAverage() {
        // given
        final double[][] dataset = new double[][]{
            {0.25, 75.}, {0.25, 90.}, {0.25, 88.}, {0.15, 70.}, {0.10, 86.}
        };
        // when
        final double weightedAverage = Arithmetic.weightedAverage(dataset);
        // then
        assertEquals(82.35, weightedAverage, DELTA2);
    }

    @Test
    void testPartPercentOfWhole() {
        // given
        final byte percent = 80;
        final byte whole = 30;
        // when
        final double part = Arithmetic.partPercentOfWhole(whole, percent);
        // then
        assertEquals(24, part, DELTA1);
    }

    @Test
    void testPercentOfWhole() {
        // given
        final byte part = 27;
        final byte whole = 30;
        // when
        final double percent = Arithmetic.percentOfWhole(part, whole);
        // then
        assertEquals(90, percent, DELTA1);
    }

    @Test
    void testPartPercentOfWhatWhole() {
        // given
        final byte percent = 25;
        final short whole = 4000;
        // when
        final double part = Arithmetic.partPercentOfWhatWhole(whole, percent);
        // then
        assertEquals(1000, part, DELTA1);
    }

    @Test
    void testWholePercentOfWhatPart() {
        // given
        final byte part = 10;
        final byte percent = 5;
        // when
        final double whole = Arithmetic.wholePercentOfWhatPart(part, percent);
        // then
        assertEquals(200, whole, DELTA1);
    }

    @Test
    void testIncreasedByPercent() {
        // given
        final byte whole = 5;
        final byte percentIncrease = 30;
        // when
        final double finalWhole = Arithmetic.increasedByPercent(whole, percentIncrease);
        // then
        assertEquals(6.5, finalWhole, DELTA1);
    }

    @Test
    void testOriginalBeforePercentIncrease() {
        // given
        final double finalWhole = 6.5;
        final byte percentIncrease = 30;
        // when
        final double whole = Arithmetic.originalBeforePercentIncrease(finalWhole, percentIncrease);
        // then
        assertEquals(5, whole, DELTA1);
    }

    @Test
    void testDecreasedByPercent() {
        // given
        final double whole = 48.89;
        final byte percentDecrease = 10;
        // when
        final double finalWhole = Arithmetic.decreasedByPercent(whole, percentDecrease);
        // then
        assertEquals(44, finalWhole, DELTA1);
    }

    @Test
    void testOriginalBeforePercentDecrease() {
        // given
        final byte finalWhole = 44;
        final byte percentDecrease = 10;
        // when
        final double whole = Arithmetic.originalBeforePercentDecrease(finalWhole, percentDecrease);
        // then
        assertEquals(48.89, whole, DELTA2);
    }

    static List<Arguments> percentageChangeArgs() {
        return List.of(
            Arguments.of(60., 72., 20., DELTA1),
            Arguments.of(50., -22., -144., DELTA1),
            Arguments.of(-10., -25., -150., DELTA1),
            Arguments.of(253_339_000., 310_384_000., 22.517, DELTA3),
            Arguments.of(5., 20., 300., DELTA1),
            Arguments.of(20., 10., -50., DELTA1),
            Arguments.of(2., 3., 50., DELTA1),
            Arguments.of(5., 4., -20., DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("percentageChangeArgs")
    void testPercentageChange(double initial, double finalValue, double expectedResult, double delta) {
        // when
        final double percentageChange = Arithmetic.percentageChange(initial, finalValue);
        // then
        assertEquals(expectedResult, percentageChange, delta);
    }

    static List<Arguments> averagePercentageArgs() {
        return List.of(
            Arguments.of(new double[]{80., 40.}, 60., DELTA1),
            Arguments.of(new double[]{80., 80., 80., 80., 40.}, 72., DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("averagePercentageArgs")
    void testAveragePercentage(double[] percents, double expectedResult, double delta) {
        // when
        final double averagePercentage = Arithmetic.averagePercentage(percents);
        // then
        assertEquals(expectedResult, averagePercentage, delta);
    }

    static List<Arguments> weightedAveragePercentageArgs() {
        return List.of(
            // (80%⋅4+40%⋅1) / (4+1) = 72%
            Arguments.of(new double[][]{{4., 80.}, {1., 40.}}, 72., DELTA1),
            // (300⋅64%+450⋅42%+250⋅36%) / (300+450+250) = 47.1%
            Arguments.of(new double[][]{{300., 64.}, {450., 42.}, {250., 36.}}, 47.1, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("weightedAveragePercentageArgs")
    void testWeightedAveragePercentage(double[][] weightedPercents, double expectedResult, double delta) {
        // when
        final double weightedAvgPercent = Arithmetic.weightedAveragePercentage(weightedPercents);
        // then
        assertEquals(expectedResult, weightedAvgPercent, delta);
    }

    @Test
    void testPercentToGoal() {
        // given
        final short progress = 8_500;
        final short goal = 15_500;
        // when
        final double percent = Arithmetic.percentToGoal(progress, goal);
        // then
        assertEquals(54.84, percent, DELTA2);
    }

    @Test
    void testPercentagePoint() {
        // given
        final byte percent1 = 5;
        final byte percent2 = 7;
        // when
        final double percentagePoint = Arithmetic.percentagePoint(percent1, percent2);
        final double percentChange = Arithmetic.percentageChange(percent1, percent2);
        // then
        assertEquals(2, percentagePoint, DELTA1);
        assertEquals(40, percentChange, DELTA1);
    }

    @Test
    void testPercentError() {
        // given
        final short trueValue = 343;
        final short observedValue = 329;
        // when
        final double percentError = Arithmetic.percentError(trueValue, observedValue);
        // then
        assertEquals(-4.082, percentError, DELTA3);
    }

    @Test
    void testPercentageDifference() {
        // given
        final short value1 = 70;
        final short value2 = 85;
        // when
        final double percentageDifference = Arithmetic.percentageDifference(value1, value2);
        final double difference = Arithmetic.percentagePoint(value1, value2);
        // then
        assertEquals(19.355, percentageDifference, DELTA3);
        assertEquals(15, difference, DELTA1);
    }

    @Test
    void testPercentOfPercent() {
        // given
        final byte percent1 = 40;
        final byte percent2 = 90;
        // when
        final double[] cumulativePercentResults = Arithmetic.percentOfPercent(percent1, percent2);
        // then
        assertNotNull(cumulativePercentResults);
        assertEquals(3, cumulativePercentResults.length);
        final double cumulativePercent = cumulativePercentResults[Constants.ARR_1ST_INDEX];
        assertEquals(36, cumulativePercent, DELTA1);
        final double firstPercent = cumulativePercentResults[Constants.ARR_2ND_INDEX];
        assertEquals(0.4, firstPercent, DELTA1);
        final double secondPercent = cumulativePercentResults[Constants.ARR_3RD_INDEX];
        assertEquals(0.9, secondPercent, DELTA1);
    }

    @Test
    void testPercentTime() {
        // given
        final byte hoursSpent = 2;
        final byte totalHours = 8;
        // when
        final double percentOfTotalTime = Arithmetic.percentTime(hoursSpent, totalHours);
        // then
        assertEquals(25, percentOfTotalTime, DELTA1);
    }

    static List<Arguments> reciprocalArgs() {
        return List.of(
            Arguments.of(5, 0.2, DELTA1),
            Arguments.of(Arithmetic.TWO_THIRDS, 1.5, DELTA1),
            Arguments.of(1, 1, DELTA1)
        );
    }

    @ParameterizedTest
    @MethodSource("reciprocalArgs")
    void testReciprocal(double x, double expectedResult, double delta) {
        // when
        final double result = Arithmetic.reciprocal(x);
        // then
        assertEquals(expectedResult, result, delta);
    }

    static List<Arguments> reciprocalOfFractionArgs() {
        return List.of(
            Arguments.of(new long[]{5, 1}, new long[]{1, 5}),
            Arguments.of(new long[]{2, 3}, new long[]{3, 2}),
            Arguments.of(new long[]{1, 2, 3}, new long[]{3, 5})
        );
    }

    @ParameterizedTest
    @MethodSource("reciprocalOfFractionArgs")
    void testReciprocal(long[] fraction, long[] expectedResult) {
        // when
        final long[] result = Arithmetic.reciprocal(fraction);
        // then
        assertArrayEquals(expectedResult, result);
    }

    @Test
    void testFactorialViaRecursion() {
        // given
        final byte num = 7;
        // when
        final double result = Arithmetic.factorialViaRecursion(num);
        // then
        assertEquals(5040, result, DELTA1);
    }
}
