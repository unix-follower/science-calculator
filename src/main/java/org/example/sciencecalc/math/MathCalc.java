package org.example.sciencecalc.math;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.LongPredicate;

import static org.example.sciencecalc.math.MathCalc.Algebra.squareRoot;
import static org.example.sciencecalc.math.NumberUtils.checkGreater;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;

public final class MathCalc {
    public static final double ONE_TWENTIETH = 0.05;  // 1/20
    public static final double ONE_SIXTEENTH = 0.0625;  // 1/16
    public static final double ONE_TWELFTH = 0.08333333;  // 1/12
    public static final double ONE_EIGHTH = 0.125; // 1/8
    public static final double ONE_SIXTH = 0.16666667; // 1/6
    public static final double ONE_FIFTH = 0.2; // 1/5
    public static final double ONE_FOURTH = 0.25; // 1/4
    public static final double ONE_THIRD = 0.33333333; // 1/3
    public static final double TWO_FIFTH = 0.4;  // 2/5
    public static final double ONE_HALF = 0.5; // 1/2
    public static final double TWO_THIRDS = 0.66666667;  // 2/3
    public static final double THREE_FOURTH = 0.75;  // 3/4
    public static final double FOUR_FIFTH = 0.8;  // 4/5
    public static final double FIVE_SIXTH = 0.83333333;  // 5/6
    public static final byte ONE = 1;
    public static final double THREE_HALF = 1.5;  // 3/2

    private static final double EPSILON_NEGATIVE10 = 1e-10;

    /**
     * ϕ=½(1+√5)
     */
    public static final double GOLDEN_RATIO = 1.618033988749895;

    private static final String DIVISION_BY_ZERO = "Division by zero";
    private static final String MISMATCHED_DIMENSIONS = "Mismatched dimensions";

    private MathCalc() {
    }

    private static void checkSameDimensions(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
        }
    }

    private static void checkSameDimensions(double[][] matrixA, double[][] matrixB) {
        if (matrixA.length != matrixB.length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
        }

        if (matrixA[Constants.ARR_1ST_INDEX].length != matrixB[Constants.ARR_1ST_INDEX].length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
        }
    }

    private static void checkSameDimensions(double[][] matrix, double[] vector) {
        if (matrix[Constants.ARR_1ST_INDEX].length != vector.length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
        }
    }

    private static void checkNotEqTo(double value, double[] invalidValues) {
        for (var invalidValue : invalidValues) {
            if (value == invalidValue) {
                throw new IllegalArgumentException("The value must not be equal to " + invalidValue);
            }
        }
    }

    private static void checkNotEq0(double value) {
        checkNotEqTo(value, new double[]{0});
    }

    private static void check2dSize(double[] vector) {
        if (vector == null || vector.length != 2) {
            throw new IllegalArgumentException("The 2d vector is required");
        }
    }

    /**
     * Round to 4 decimal places
     */
    public static void round4InPlace(double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] = Math.round(vector[i] * 10000.0) / 10000.0;
        }
    }

    public static double truncate(double value, int decimals) {
        final double factor = Math.pow(10, decimals);
        return Math.floor(value * factor) / factor;
    }

    public static final class Arithmetic {
        private Arithmetic() {
        }

        /**
         * Difference between factorial and power:
         * Factorial: multiplies decreasing integers.
         * Exponentiation: multiplies the same base repeatedly.
         *
         * @return n!
         */
        public static long factorial(long number) {
            if (number < 0) {
                throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
            }
            long result = 1;
            for (long i = 1; i <= number; i++) {
                if (result > Long.MAX_VALUE / i) {
                    throw new ArithmeticException("Factorial result exceeds the range of long.");
                }
                result *= i;
            }
            return result;
        }

        public static long factorialViaRecursion(long number) {
            if (number < 0) {
                throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
            }
            if (number <= 1) {
                return number;
            }

            return number * factorialViaRecursion(number - 1);
        }

        public static BigInteger factorial(BigInteger number) {
            var result = BigInteger.ONE;
            for (var i = BigInteger.ONE; i.compareTo(number) <= 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(i);
            }
            return result;
        }

        public static long sumOfDigits(double number) {
            failIfNotWholeNumber(number);

            long num = Math.abs((long) number);
            long sum = 0;
            while (num > 0) {
                sum += num % 10;
                num /= 10;
            }
            return sum;
        }

        private static void failIfNotWholeNumber(double number) {
            if (!isWholeNumber(number)) {
                throw new IllegalArgumentException("Only whole numbers are supported");
            }
        }

        public static long sumOfLastDigits(double number, long numOfDigits) {
            failIfNotWholeNumber(number);

            long num = Math.abs((long) number);
            long sum = 0;
            for (long i = 0; i < numOfDigits && num > 0; i++) {
                sum += num % 10;
                num /= 10;
            }
            return sum;
        }

        public static boolean isDivisibleBy2(double number) {
            return isWholeNumber(number) && ((long) number) % 2 == 0;
        }

        public static boolean isDivisibleBy3(double number) {
            return isWholeNumber(number) && sumOfDigits(number) % 3 == 0;
        }

        public static boolean isDivisibleBy4(double number) {
            return isWholeNumber(number) && sumOfLastDigits(number, 2) % 4 == 0;
        }

        public static boolean isDivisibleBy5(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            final long lastDigit = Math.abs((long) number) % 10;
            return lastDigit == 0 || lastDigit == 5;
        }

        public static boolean isDivisibleBy6(double number) {
            return isDivisibleBy2(number) && isDivisibleBy3(number);
        }

        public static boolean isDivisibleBy7(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            final long num = Math.abs((long) number);
            return num % 7 == 0;
        }

        public static boolean isDivisibleBy7ViaSubtractTwiceLastDigit(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            long num = Math.abs((long) number);
            while (num > 99) { // Reduce the number for large values
                final long lastDigit = num % 10;
                num = (num / 10) - 2 * lastDigit;
                num = Math.abs(num);
            }
            return num % 7 == 0;
        }

        public static boolean isDivisibleBy7ViaReverseOrder(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            long num = Math.abs((long) number);
            final int[] weights = {1, 3, 2, 6, 4, 5};
            long productSum = 0;
            int i = 0;
            while (num > 0) {
                final long lastDigit = num % 10;
                productSum += lastDigit * weights[i % weights.length];
                num /= 10;
                i++;
            }
            return productSum % 7 == 0;
        }

        public static boolean isDivisibleBy8(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            final long num = Math.abs((long) number);
            final long lastThreeDigits = num % 1000;
            return lastThreeDigits % 8 == 0;
        }

        public static boolean isDivisibleBy9(double number) {
            return isWholeNumber(number) && sumOfDigits(number) % 9 == 0;
        }

        public static boolean isDivisibleBy10(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            final long lastDigit = Math.abs((long) number) % 10;
            return lastDigit == 0;
        }

        public static boolean isEven(double number) {
            return isDivisibleBy2(number);
        }

        public static boolean isOdd(double number) {
            return !isDivisibleBy2(number);
        }

        public static boolean isWholeNumber(double number) {
            return (number % 1) == 0;
        }

        /**
         * <ul>
         *     <li>A natural number greater than 1 is called prime if it has exactly two factors,
         * i.e., if the number is divisible only by 1 and itself.</li>
         *     <li>1 is neither prime nor composite as it has only one factor, itself.</li>
         *     <li>Every prime number, apart from 2 and 3, can be written in the form of 6n + 1 or 6n - 1</li>
         * </ul>
         */
        public static boolean isPrime(double number) {
            if (!isWholeNumber(number) || number < 2) {
                return false;
            }
            final long n = (long) number;
            if (n == 2) { // 2 is the only even prime. All other primes are odd
                return true;
            }
            if (n % 2 == 0) {
                return false;
            }
            for (long i = 3; i <= Math.sqrt(n); i += 2) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        }

        /**
         * <ul>
         *     <li>Two natural numbers are called relatively prime (or coprime) if there is no integer
         * other than 1 that divides both these numbers. In other words, their greatest common factor
         * (GCF) is equal to 1.</li>
         *     <li>Two primes are always relatively prime.</li>
         *     <li>But numbers don't need to be prime in order to be relatively prime.</li>
         * </ul>
         */
        public static boolean isCoprime(double number, double number1) {
            final long[] numberPrimes = primeFactorization(number);
            final long[] number1Primes = primeFactorization(number1);
            // All numbers are divisible by 1
            final LongPredicate divisibleBy1Predicate = value -> value != 1;

            final long[] number1PrimesOmitDiv1 = Arrays.stream(number1Primes)
                .filter(divisibleBy1Predicate)
                .toArray();

            return Arrays.stream(numberPrimes)
                .filter(divisibleBy1Predicate)
                .noneMatch(value -> Arrays.binarySearch(number1PrimesOmitDiv1, value) >= 0);
        }

        /**
         * <ul>
         *     <li>Two natural numbers are called relatively prime (or coprime) if there is no integer
         * other than 1 that divides both these numbers. In other words, their greatest common factor
         * (GCF) is equal to 1.</li>
         *     <li>4 is the smallest composite number.</li>
         *     <li>Two primes are always relatively prime.</li>
         *     <li>But numbers don't need to be prime in order to be relatively prime.</li>
         * </ul>
         */
        public static boolean isCompositeNumber(double number) {
            final long[] numberPrimes = primeFactorization(number);
            return numberPrimes.length > 1;
        }

        public static long[] factor(double number) {
            if (!isWholeNumber(number) || number < 1) {
                return new long[]{1};
            }

            final var factors = new ArrayList<Long>();

            long num = (long) number;
            for (long i = 1; i <= num; i++) {
                if (num % i == 0) {
                    factors.add(i);
                }
            }
            return factors.stream().mapToLong(Long::longValue).toArray();
        }

        public static long[] commonFactors(double number1, double number2) {
            final long[] num1Factors = factor(number1);
            final long[] num2Factors = factor(number2);

            final var commonFactors = new ArrayList<Long>();
            for (final long factor : num1Factors) {
                final int index = Arrays.binarySearch(num2Factors, factor);
                if (index >= 0) {
                    commonFactors.add(factor);
                }
            }
            return commonFactors.stream().mapToLong(Long::longValue).toArray();
        }

        public static long[] primeFactorization(double number) {
            if (!isWholeNumber(number) || number < 2) {
                return new long[0];
            }
            long num = (long) number;
            final var factors = new ArrayList<Long>();
            for (long i = 2; i <= num / i; i++) {
                while (num % i == 0) {
                    factors.add(i);
                    num /= i;
                }
            }
            if (num > 1) {
                factors.add(num);
            }
            return factors.stream().mapToLong(Long::longValue).toArray();
        }

        /**
         * @return prime number -> exponent
         */
        private static Map<Long, Integer> primeFactorMap(long number) {
            final var map = new HashMap<Long, Integer>();
            long num = number;
            for (long i = 2; i * i <= num; i++) {
                while (num % i == 0) {
                    map.put(i, map.getOrDefault(i, 0) + 1);
                    num /= i;
                }
            }
            if (num > 1) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            }
            return map;
        }

        /**
         * 1. Find all numbers as a product of their prime factors.
         * 2. Find the highest power of each prime number.
         * 3. Multiply these values together.
         */
        public static long lcmWithPrimeFactorization(double[] numbers) {
            final long[] nums = Arrays.stream(numbers).mapToLong(value -> (long) value).toArray();

            final var primePowers = new HashMap<Long, Integer>();
            for (long number : nums) {
                final var factors = primeFactorMap(number);
                for (final var entry : factors.entrySet()) {
                    final long prime = entry.getKey();
                    final int power = entry.getValue();
                    primePowers.put(prime, Math.max(primePowers.getOrDefault(prime, 0), power));
                }
            }
            long lcm = 1;
            for (final var entry : primePowers.entrySet()) {
                lcm *= (long) Math.pow(entry.getKey(), entry.getValue());
            }
            return lcm;
        }

        /**
         * @return LCM(a,b) = |a·b| / GCF(a,b)
         */
        public static long lcmWithGcf(double[] numbers) {
            atLeast2NumRequired(numbers);

            long result = (long) numbers[Constants.ARR_1ST_INDEX];
            for (int i = 1; i < numbers.length; i++) {
                final long a = Math.abs(result);
                final long b = Math.abs((long) numbers[i]);
                result = a * b / gcd(a, b);
            }
            return result;
        }

        /**
         * @return LCM = LCM of numerator / GCF of denominator
         */
        public static long lcmOverGcfOfFractions(long[] fraction1, long[] fraction2) {
            final long numerator1 = fraction1[Constants.ARR_1ST_INDEX];
            final long denominator1 = fraction1[Constants.ARR_2ND_INDEX];
            final long numerator2 = fraction2[Constants.ARR_1ST_INDEX];
            final long denominator2 = fraction2[Constants.ARR_2ND_INDEX];
            return lcmWithPrimeFactorization(new double[]{numerator1, numerator2})
                / gcfWithCommonFactors(new double[]{denominator1, denominator2});
        }

        public static long lcmOfFractions(long denominator1, long denominator2) {
            return lcmWithPrimeFactorization(new double[]{denominator1, denominator2});
        }

        public static long lcmOverGcfOfFractions(double[] fraction1, double[] fraction2) {
            return lcmWithPrimeFactorization(fraction1) / gcfWithCommonFactors(fraction2);
        }

        public static long gcfWithEuclideanAlg(double[] numbers) {
            atLeast2NumRequired(numbers);

            long result = (long) numbers[Constants.ARR_1ST_INDEX];
            for (int i = 1; i < numbers.length; i++) {
                result = gcd(result, (long) numbers[i]);
            }
            return result;
        }

        private static void atLeast2NumRequired(double[] numbers) {
            if (numbers == null || numbers.length < 2) {
                throw new IllegalArgumentException("At least 2 numbers are required");
            }
        }

        private static void atLeast2NumRequired(long[] numbers) {
            if (numbers == null || numbers.length < 2) {
                throw new IllegalArgumentException("At least 2 numbers are required");
            }
        }

        private static void atMost3NumRequired(long[] numbers) {
            if (numbers == null || numbers.length > 3) {
                throw new IllegalArgumentException("At most 3 numbers are required");
            }
        }

        private static void exact2NumRequired(double[] numbers) {
            if (numbers == null || numbers.length != 2) {
                throw new IllegalArgumentException("Exactly 2 numbers are required");
            }
        }

        /**
         * The greatest common factor (GCF) is the largest integer factor that is present between a set of numbers.
         * aka Greatest Common Divisor, Greatest Common Denominator (GCD),
         * Highest Common Factor (HCF), or Highest Common Divisor (HCD).
         * <ul>
         *     <li>If the ratio of two numbers a and b (a > b) is an integer then gcf(a, b) = b.</li>
         *     <li>gcf(a, 0) = a, used in Euclidean algorithm.</li>
         *     <li>gcf(a, 1) = 1.</li>
         *     <li>If a and b don't have common factors (they are coprime), then gcf(a, b) = 1.</li>
         *     <li>All common factors of a and b are also divisors of gcf(a,b).</li>
         *     <li>If b * c / a is an integer and gcf(a, b) = d, then a * c / d is also an integer.</li>
         *     <li>For any integer k: gcf(k×a, k×b) = k × gcf(a, b), used in binary algorithm.</li>
         *     <li>For any positive integer k: gcf(a/k, b/k) = gcf(a, b) / k.</li>
         *     <li>gcf(a, b) × lcm(a, b) = |a×b|.</li>
         *     <li>gcf(a, b) = |a × b| / lcm(a, b).</li>
         *     <li>gcf(a, lcm(b, c)) = lcm(gcf(a, b), gcf(a, c)).</li>
         *     <li>lcm(a, gcf(b, c)) = gcf(lcm(a, b), lcm(a, c)).</li>
         *     <li>gcf(a, b, c) = gcf(gcf(a, b), c) = gcf(gcf(a, c), b) = gcf(gcf(b, c), a).</li>
         * </ul>
         */
        public static long gcd(long number1, long number2) {
            long a = Math.abs(number1);
            long b = Math.abs(number2);
            while (b != 0) {
                long temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }

        public static long gcfWithBinaryAlg(double[] numbers) {
            atLeast2NumRequired(numbers);

            long result = (long) numbers[Constants.ARR_1ST_INDEX];
            for (int i = 1; i < numbers.length; i++) {
                result = binaryGcd(result, (long) numbers[i]);
            }
            return result;
        }

        public static long gcfWithCommonFactors(double[] numbers) {
            atLeast2NumRequired(numbers);

            long result = (long) numbers[Constants.ARR_1ST_INDEX];
            for (int i = 1; i < numbers.length; i++) {
                final long[] commonFactors = commonFactors(result, (long) numbers[i]);
                result = Arrays.stream(commonFactors).max().orElse(0);
            }
            return result;
        }

        /**
         * @return gcf(a, b) = |a × b| / lcm(a, b)
         */
        public static long gcfWithLcm(double[] numbers) {
            atLeast2NumRequired(numbers);

            long result = (long) numbers[Constants.ARR_1ST_INDEX];
            final double[] absNumbers = new double[2];
            for (int i = 1; i < numbers.length; i++) {
                final long a = Math.abs(result);
                final long b = Math.abs((long) numbers[i]);
                absNumbers[Constants.ARR_1ST_INDEX] = a;
                absNumbers[Constants.ARR_2ND_INDEX] = b;
                result = a * b / lcmWithPrimeFactorization(absNumbers);
            }
            return result;
        }

        public static long binaryGcd(long number1, long number2) {
            long a = Math.abs(number1);
            long b = Math.abs(number2);
            if (a == 0) {
                return b;
            }
            if (b == 0) {
                return a;
            }

            // Find common factors of 2
            int shift = 0;
            while (((a | b) & 1) == 0) {
                a >>= 1;
                b >>= 1;
                shift++;
            }
            while ((a & 1) == 0) {
                a >>= 1;
            }
            do {
                while ((b & 1) == 0) {
                    b >>= 1;
                }
                if (a > b) {
                    long temp = a;
                    a = b;
                    b = temp;
                }
                b = b - a;
            } while (b != 0);
            return a << shift;
        }

        /**
         * a/b = c/x => x = (b * c) / a
         * a/b = x/d => x = (a * n) / b
         */
        public static double[] solveProportion(double[] proportion, double[] proportionWithUnknown) {
            exact2NumRequired(proportion);
            exact2NumRequired(proportionWithUnknown);
            final double a = proportion[Constants.ARR_1ST_INDEX];
            final double b = proportion[Constants.ARR_2ND_INDEX];
            checkGreater0(b);

            final double c = proportionWithUnknown[Constants.ARR_1ST_INDEX];
            final double d = proportionWithUnknown[Constants.ARR_2ND_INDEX];

            if (Double.isInfinite(c)) {
                final double numeratorX = a * d / b;
                return new double[]{numeratorX, d};
            } else {
                final double denominatorX = b * c / a;
                return new double[]{c, denominatorX};
            }
        }

        /**
         * @return A : B = C : D
         */
        public static double[] findEquivalentRatio(double[] ratio, double[] ratioWithUnknown) {
            return solveProportion(ratio, ratioWithUnknown);
        }

        /**
         * @return A : B = x × A : x × B
         */
        public static double[] scaleUpRatio(double[] ratio, long coefficient) {
            exact2NumRequired(ratio);
            final double numerator = ratio[Constants.ARR_1ST_INDEX];
            final double denominator = ratio[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator);
            return new double[]{numerator * coefficient, denominator * coefficient};
        }

        /**
         * @return A : B = A/x : B/x
         */
        public static double[] scaleDownRatio(double[] ratio, double coefficient) {
            exact2NumRequired(ratio);
            final double numerator = ratio[Constants.ARR_1ST_INDEX];
            final double denominator = ratio[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator);
            return new double[]{numerator / coefficient, denominator / coefficient};
        }

        /**
         * <ul>
         *     <li>(a+b)/a = a/b</li>
         *     <li>1 + 1/(a/b) = a/b</li>
         *     <li>b = √(1 - a²)</li>
         *     <li>a/b = φ</li>
         *     <li>a/√(1 - a²) = φ</li>
         *     <li>a = √(φ²/(1 + φ²))</li>
         * </ul>
         */
        public static double[] simplifyRatio(double[] ratio) {
            final long commonFactor = gcfWithCommonFactors(ratio);
            return scaleDownRatio(ratio, commonFactor);
        }

        public static double[] simplifyRatio1toN(double[] ratio) {
            final double numerator = ratio[Constants.ARR_1ST_INDEX];
            return scaleDownRatio(ratio, numerator);
        }

        public static double[] simplifyRatioNto1(double[] ratio) {
            final double denominator = ratio[Constants.ARR_2ND_INDEX];
            return scaleDownRatio(ratio, denominator);
        }

        public static double[] goldenRatioGivenLongerSection(double longerSection) {
            final double shorterSection = longerSection / GOLDEN_RATIO;
            final double whole = longerSection + shorterSection;
            return new double[]{longerSection, shorterSection, whole};
        }

        public static double[] goldenRatioGivenShorterSection(double shorterSection) {
            final double longerSection = shorterSection * GOLDEN_RATIO;
            final double whole = longerSection + shorterSection;
            return new double[]{longerSection, shorterSection, whole};
        }

        public static double[] goldenRatioGivenWhole(double whole) {
            final double phiSquared = GOLDEN_RATIO * GOLDEN_RATIO;
            final double longerSection = squareRoot(phiSquared / (1 + phiSquared));
            final double shorterSection = whole - longerSection;
            return new double[]{longerSection, shorterSection, whole};
        }

        /**
         * @return w₁(n₁/d₁) + w₂(n₂/d₂)
         */
        public static long[] addFractions(long[] fraction1, long[] fraction2) {
            atMost3NumRequired(fraction1);
            atMost3NumRequired(fraction2);

            long[] frac1 = fraction1;
            if (frac1.length == 3) {
                frac1 = mixedNumberToImproperFraction(frac1);
            }
            final long numerator1 = frac1[Constants.ARR_1ST_INDEX];
            final long denominator1 = frac1[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator1);

            long[] frac2 = fraction2;
            if (frac2.length == 3) {
                frac2 = mixedNumberToImproperFraction(frac2);
            }

            final long numerator2 = frac2[Constants.ARR_1ST_INDEX];
            final long denominator2 = frac2[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator2);

            final long[] result;
            if (denominator1 == denominator2) {
                result = new long[]{numerator1 + numerator2, denominator1};
            } else {
                final long lcm = lcmOfFractions(denominator1, denominator2);
                final long normalizedNumerator1 = lcm / denominator1 * numerator1;
                final long normalizedNumerator2 = lcm / denominator2 * numerator2;
                result = new long[]{normalizedNumerator1 + normalizedNumerator2, lcm};
            }
            return simplifyFraction(result);
        }

        /**
         * @return w₁(n₁/d₁) - w₂(n₂/d₂)
         */
        public static long[] subtractFractions(long[] fraction1, long[] fraction2) {
            atMost3NumRequired(fraction1);
            atMost3NumRequired(fraction2);

            long[] frac1 = fraction1;
            if (frac1.length == 3) {
                frac1 = mixedNumberToImproperFraction(frac1);
            }
            final long numerator1 = frac1[Constants.ARR_1ST_INDEX];
            final long denominator1 = frac1[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator1);

            long[] frac2 = fraction2;
            if (frac2.length == 3) {
                frac2 = mixedNumberToImproperFraction(frac2);
            }

            final long numerator2 = frac2[Constants.ARR_1ST_INDEX];
            final long denominator2 = frac2[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator2);

            final long[] result;
            if (denominator1 == denominator2) {
                result = new long[]{numerator1 - numerator2, denominator1};
            } else {
                final long lcm = lcmOfFractions(denominator1, denominator2);
                final long normalizedNumerator1 = lcm / denominator1 * numerator1;
                final long normalizedNumerator2 = lcm / denominator2 * numerator2;
                result = new long[]{normalizedNumerator1 - normalizedNumerator2, lcm};
            }
            return simplifyFraction(result);
        }

        /**
         * @return w₁(n₁/d₁) * w₂(n₂/d₂)
         */
        public static long[] multiplyFractions(long[] fraction1, long[] fraction2) {
            atMost3NumRequired(fraction1);
            atMost3NumRequired(fraction2);

            long[] frac1 = fraction1;
            if (frac1.length == 3) {
                frac1 = mixedNumberToImproperFraction(frac1);
            }

            long[] frac2 = fraction2;
            if (frac2.length == 3) {
                frac2 = mixedNumberToImproperFraction(frac2);
            }

            final long numerator1 = frac1[Constants.ARR_1ST_INDEX];
            final long denominator1 = frac1[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator1);

            final long numerator2 = frac2[Constants.ARR_1ST_INDEX];
            final long denominator2 = frac2[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator2);

            final long[] result = {numerator1 * numerator2, denominator1 * denominator2};
            return simplifyFraction(result);
        }

        /**
         * @return w₁(n₁/d₁) / w₂(n₂/d₂)
         */
        public static long[] divideFractions(long[] fraction1, long[] fraction2) {
            atMost3NumRequired(fraction1);
            atMost3NumRequired(fraction2);

            long[] frac1 = fraction1;
            if (frac1.length == 3) {
                frac1 = mixedNumberToImproperFraction(frac1);
            }

            long[] frac2 = fraction2;
            if (frac2.length == 3) {
                frac2 = mixedNumberToImproperFraction(frac2);
            }

            final long numerator1 = frac1[Constants.ARR_1ST_INDEX];
            final long denominator1 = frac1[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator1);

            final long numerator2 = frac2[Constants.ARR_1ST_INDEX];
            final long denominator2 = frac2[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator2);

            final long[] result = {numerator1 * denominator2, denominator1 * numerator2};
            return simplifyFraction(result);
        }

        public static long[] mixedNumberToImproperFraction(long[] fraction) {
            if (fraction.length < 3) {
                return fraction;
            }

            final long whole = fraction[Constants.ARR_1ST_INDEX];
            final long numerator = fraction[Constants.ARR_2ND_INDEX];
            final long denominator = fraction[Constants.ARR_3RD_INDEX];
            return new long[]{whole * denominator + numerator, denominator};
        }

        public static long[] improperFractionToMixedNumber(long[] fraction) {
            atLeast2NumRequired(fraction);

            final long numerator = fraction[Constants.ARR_1ST_INDEX];
            final long denominator = fraction[Constants.ARR_2ND_INDEX];
            final long whole = numerator / denominator;
            final long remainder = numerator - whole * denominator;

            if (remainder == 0) {
                return new long[]{whole, 0, denominator}; // Represent as whole number
            } else if (whole == 0) {
                return new long[]{remainder, denominator}; // Proper fraction
            } else {
                return new long[]{whole, remainder, denominator}; // Mixed number
            }
        }

        public static long[] simplifyFraction(long[] fraction) {
            atMost3NumRequired(fraction);

            final long[] frac = mixedNumberToImproperFraction(fraction);
            final long numerator = frac[Constants.ARR_1ST_INDEX];
            final long denominator = frac[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator);

            final long commonFactor = gcfWithCommonFactors(new double[]{numerator, denominator});
            final long[] simplified = {numerator / commonFactor, denominator / commonFactor};
            return improperFractionToMixedNumber(simplified);
        }

        public static long[] decimalToFraction(double decimal) {
            final String decimalStr = Double.toString(decimal);
            final int index = decimalStr.indexOf('.');
            if (index < 0) {
                // No decimal point, it's an integer
                return new long[]{(long) decimal, 1};
            }
            final int decimalPlaces = decimalStr.length() - index - 1;
            final long denominator = (long) Math.pow(10, decimalPlaces);
            final long numerator = Math.round(decimal * denominator);
            return simplifyFraction(new long[]{numerator, denominator});
        }

        public static double fractionToDecimal(long[] fraction) {
            atMost3NumRequired(fraction);

            long[] frac = fraction;
            if (frac.length == 3) {
                frac = mixedNumberToImproperFraction(frac);
            }

            final long numerator = frac[Constants.ARR_1ST_INDEX];
            final long denominator = frac[Constants.ARR_2ND_INDEX];
            checkGreater0(denominator);
            return (double) numerator / denominator;
        }

        /**
         * @return (a₁ + a₂ + … + aₙ) / n = 1/n ∑ⁿᵢ₌₁ aᵢ
         */
        public static double average(double[] dataset) {
            final double sum = Arrays.stream(dataset).sum();
            return sum / dataset.length;
        }

        /**
         * @return x = (w₁x₁ + w₂x₂ + … + wₙxₙ) / (w₁ + w₂ + … + wₙ) = (∑ⁿᵢ₌₁ wᵢxᵢ) / (∑ⁿᵢ₌₁ wᵢ)
         */
        public static double weightedAverage(double[][] dataset) {
            final double weightedSum = Arrays.stream(dataset)
                .mapToDouble(weightedDataPoint ->
                    weightedDataPoint[Constants.ARR_1ST_INDEX] * weightedDataPoint[Constants.ARR_2ND_INDEX]
                )
                .sum();
            final double sumOfWeights = Arrays.stream(dataset)
                .mapToDouble(weightedPercent -> weightedPercent[Constants.ARR_1ST_INDEX])
                .sum();
            return weightedSum / sumOfWeights;
        }

        /**
         * @return G = ⁿ√(x₁ * x₂ * … * xₙ) = (∏ⁿᵢ₌₁ xᵢ)¹/ⁿ
         */
        public static double geometricAverage() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return H = n / (1/x₁ + 1/x₂ + … + 1/xₙ) = n / (∑ⁿᵢ₌₁ 1/xᵢ)
         */
        public static double harmonicAverage() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return y = (x ⋅ p%) / 100%
         */
        public static double partPercentOfWhole(double whole, double percent) {
            return whole * percent / 100;
        }

        /**
         * @return p% = (y/x) ⋅ 100%
         */
        public static double percentOfWhole(double part, double whole) {
            return part / whole * 100;
        }

        /**
         * @return x = (p% / 100%) ⋅ y
         */
        public static double partPercentOfWhatWhole(double part, double percent) {
            return percent / 100 * part;
        }

        /**
         * @return y = (x ⋅ 100%) / p%
         */
        public static double wholePercentOfWhatPart(double whole, double percent) {
            return whole * 100 / percent;
        }

        /**
         * @return y = ±x (100% + p%) / 100%
         */
        public static double increasedByPercent(double whole, double percentIncrease) {
            return whole * (100 + percentIncrease) / 100;
        }

        /**
         * @return x = ± y / (100% + p%) ⋅ 100%
         */
        public static double originalBeforePercentIncrease(double finalWhole, double percentIncrease) {
            return finalWhole / (100 + percentIncrease) * 100;
        }

        /**
         * @return y = ± x (100% - p%) / 100%
         */
        public static double decreasedByPercent(double whole, double percentDecrease) {
            return whole * (100 - percentDecrease) / 100;
        }

        /**
         * @return x = ± y / (100% - p%) ⋅ 100%
         */
        public static double originalBeforePercentDecrease(double finalWhole, double percentDecrease) {
            return finalWhole / (100 - percentDecrease) * 100;
        }

        /**
         * @return % change = 100% × ((final−initial) / ∣initial∣)
         */
        public static double percentageChange(double initial, double finalValue) {
            return 100 * ((finalValue - initial) / Math.abs(initial));
        }

        public static double averagePercentage(double[] percents) {
            return average(percents);
        }

        public static double weightedAveragePercentage(double[][] weightedPercents) {
            return weightedAverage(weightedPercents);
        }

        /**
         * @return percentage = (progress / goal) × 100
         */
        public static double percentToGoal(double progress, double goal) {
            return progress / goal * 100;
        }

        /**
         * @return Percentage points = Percentage #2 - Percentage #1
         */
        public static double percentagePoint(double percent1, double percent2) {
            return percent2 - percent1;
        }

        /**
         * @return percent error = (OV - TV) / TV × 100%
         */
        public static double percentError(double trueValue, double observedValue) {
            return (observedValue - trueValue) / trueValue * 100;
        }

        /**
         * @return % difference = 100 × |V₁ - V₂| / ((V₁ + V₂) / 2)
         */
        public static double percentageDifference(double value1, double value2) {
            return 100 * Math.abs(value1 - value2) / ((value1 + value2) / 2);
        }

        /**
         * aka cumulative percentage
         *
         * @return cumulative % = (percent1 / 100) * (percent2 / 100) * 100
         */
        public static double[] percentOfPercent(double percent1, double percent2) {
            final double firstPercent = percent1 / 100;
            final double secondPercent = percent2 / 100;
            return new double[]{firstPercent * secondPercent * 100, firstPercent, secondPercent};
        }

        /**
         * @return % of the total time = (First time duration / Second time duration) × 100
         */
        public static double percentTime(double hoursSpent, double totalHours) {
            return hoursSpent / totalHours * 100;
        }

        /**
         * <ul>
         *     <li>The reciprocal of x = 1/x</li>
         *     <li>x * 1/x = 1</li>
         * </ul>
         */
        public static double reciprocal(double x) {
            return ONE / x;
        }

        public static long[] reciprocal(long[] fraction) {
            Objects.requireNonNull(fraction);

            if (fraction.length == 3) {
                final long[] improperFraction = mixedNumberToImproperFraction(fraction);
                return new long[]{improperFraction[Constants.ARR_2ND_INDEX], improperFraction[Constants.ARR_1ST_INDEX]};
            }

            return new long[]{fraction[Constants.ARR_2ND_INDEX], fraction[Constants.ARR_1ST_INDEX]};
        }
    }

    public static final class Algebra {
        private Algebra() {
        }

        // Algebraic Identities

        /**
         * @return (a + b)² = a² + 2ab + b²
         */
        public static double binomialSquareOfSum() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return (a - b)² = a² - 2ab + b²
         */
        public static double binomialSquareOfDifference() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return a² - b² = (a + b)(a - b)
         */
        public static double binomialDifferenceOfSquares() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return a³ + b³ = (a + b)(a² - ab + b²)
         */
        public static double binomialSumOfCubes() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return a³ - b³ = (a - b)(a² + ab + b²)
         */
        public static double binomialDifferenceOfCubes() {
            throw new UnsupportedOperationException();
        }

        /**
         * Alternative: (a + b)³ = a³ + b³ + 3ab(a + b)
         *
         * @return (a + b)³ = a³ + 3a²b + 3ab² + b³
         */
        public static double cubeOfBinomialSum() {
            throw new UnsupportedOperationException();
        }

        /**
         * Alternative: (a - b)³ = a³ - b³ - 3ab(a - b)
         *
         * @return (a - b)³ = a³ - 3a²b + 3ab² - b³
         */
        public static double cubeOfBinomialDifference() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return (x + a)(x + b) = x² + (a + b)x + ab
         */
        public static double binomialFactoring() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return (a + b)(b + c)(c + a) = (a + b + c)(ab + ac + bc) - 2abc
         */
        public static double trinomialFactoring() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return (a + b + c)² = a² + b² + c² + 2ab + 2bc + 2ca
         */
        public static double trinomialSumSquared() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return a² + b² + c² = (a + b + c)² - 2(ab + bc + ca)
         */
        public static double trinomialSumOfSquares() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return a³ + b³ + c³ - 3abc = (a + b + c)(a² + b² + c² - ab - ca - bc)
         */
        public static double trinomialSumOfCubes() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return 𝚪(n) = (n - 1)!
         */
        public static double gammaFunction(double x) {
            if (x == 0) {
                throw new IllegalArgumentException("Gamma function is undefined for 0");
            }

            // Lanczos approximation coefficients
            final double[] p = {
                676.5203681218851,
                -1259.1392167224028,
                771.32342877765313,
                -176.61502916214059,
                12.507343278686905,
                -0.13857109526572012,
                9.9843695780195716e-6,
                1.5056327351493116e-7
            };
            final int g = 7;
            if (x < 0.5) {
                // Reflection formula
                return Math.PI / (Math.sin(Math.PI * x) * gammaFunction(1 - x));
            }
            final double subtractedX = x - 1;
            double a = 0.99999999999980993;
            for (int i = 0; i < p.length; i++) {
                a += p[i] / (subtractedX + i + 1);
            }
            final double t = subtractedX + g + 0.5;
            return Math.sqrt(2 * Math.PI) * Math.pow(t, subtractedX + 0.5) * Math.exp(-t) * a;
        }

        // Roots: square, cube and nth

        /**
         * <table>
         *     <tr>
         *         <th>Square root</th><th>Is perfect square?</th>
         *     </tr>
         *     <tr><td>√1 = 1</td><td>✅</td></tr>
         *     <tr><td>√2 ≈ 1.41</td><td>❌</td></tr>
         *     <tr><td>√3 ≈ 1.73</td><td>❌</td></tr>
         *     <tr><td>√4 = 2</td><td>✅</td></tr>
         *     <tr><td>√5 ≈ 2.24</td><td>❌</td></tr>
         *     <tr><td>√7 ≈ 2.65</td><td>❌</td></tr>
         *     <tr><td>√9 = 3</td><td>✅</td></tr>
         *     <tr><td>√11 ≈ 3.32</td><td>❌</td></tr>
         *     <tr><td>√13 ≈ 3.61</td><td>❌</td></tr>
         *     <tr><td>√16 = 4</td><td>✅</td></tr>
         *     <tr><td>√17 ≈ 4.12</td><td>❌</td></tr>
         *     <tr><td>√19 ≈ 4.34</td><td>❌</td></tr>
         *     <tr><td>√25 = 5</td><td>✅</td></tr>
         *     <tr><td>√27 = √(9 × 3) = √9 × √3 = 3√3</td><td>❌</td></tr>
         *     <tr><td>√36 = 6</td><td>✅</td></tr>
         *     <tr><td>√45 = √(9 × 5) = √9 × √5 = 3√5</td><td>❌</td></tr>
         *     <tr><td>√49 = 7</td><td>✅</td></tr>
         *     <tr><td>√52 ≈ 2√13 = 7.22</td><td>❌</td></tr>
         *     <tr><td>√64 = 8</td><td>✅</td></tr>
         *     <tr><td>√81 = 9</td><td>✅</td></tr>
         *     <tr><td>√100 = 10</td><td>✅</td></tr>
         *     <tr><td>√121 = 11</td><td>✅</td></tr>
         *     <tr><td>√144 = 12</td><td>✅</td></tr>
         * </table>
         *
         * @return √x = x¹/² = x^0.5
         */
        public static double squareRoot(double x) {
            return nthRoot(x, 2);
            // or Math.sqrt(x)
        }

        /**
         * x = ⁿ√a as xⁿ = a
         * y = ±√x ⟺ y² = x
         * x¹/² * y¹/² = (x * y)¹/²
         * (x^0.5)² = x^(0.5*2) = x
         *
         * @return √(x * y) = √x * √y
         */
        public static double squareRootMultiply(double x, double y) {
            return squareRoot(x * y);
        }

        /*
         * @return √(x / y) = √x / √y
         */
        public static double squareRootDivide(double x, double y) {
            return squareRoot(x / y);
        }

        /**
         * @return √(xⁿ) = (xⁿ)¹/² = xⁿ/²
         */
        public static double squareRootWithExponent(double x, double exponent) {
            return squareRoot(Math.pow(x, exponent));
        }

        /**
         * i = √(-1)
         * x = a + bi
         */
        public static double squareRootWithComplexNumber(double x) {
            return squareRoot(Math.abs(x));
        }

        /**
         * <ul>
         *     <li>∛x = y ⟺ y³ = x</li>
         *     <li>∛(x) = x¹/³</li>
         *     <li>∛(a × b) = ∛a × ∛b</li>
         *     <li>∛(a / b) = ∛a / ∛b</li>
         * </ul>
         */
        public static double cubeRoot(double number) {
            return nthRoot(number, 3);
            // or Math.cbrt(number)
        }

        public static double nthRoot(double number, double degree) {
            return Math.pow(number, 1. / degree);
        }

        /**
         * @return f(x) = ⁿ√x
         */
        public static double radicalFn(double x, double degree) {
            return nthRoot(x, degree);
        }

        /**
         * f(x) = a × ⁿ√(b×x−h) + k
         * where:
         * <ul>
         *     <li>a scales the radical graph on the y-axis</li>
         *     <li>b scales the radical graph on the x-axis</li>
         *     <li>h offsets the radical function on the x-axis</li>
         *     <li>k offsets the radical function on the y-axis</li>
         * </ul>
         */
        public static double generalizedRadicalFn(double x, double a, double b, double h, double k, double degree) {
            return a * nthRoot(b * x - h, degree) + k;
        }

        public static double[] addRadicals(double[] radical1, double[] radical2) {
            final double[][] normalizedRadicals = ensureRadicalsWithCoef(radical1, radical2);
            final double[] normalizedRadical1 = normalizedRadicals[Constants.ARR_1ST_INDEX];
            final double[] normalizedRadical2 = normalizedRadicals[Constants.ARR_2ND_INDEX];

            final double radical1coef = normalizedRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizedRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizedRadical1[Constants.ARR_3RD_INDEX];

            final double radical2coef = normalizedRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizedRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizedRadical2[Constants.ARR_3RD_INDEX];

            if (radicand1 != radicand2 || radical1degree != radical2degree) {
                throw new IllegalArgumentException();
            }

            return new double[]{radical1coef + radical2coef, radical1degree, radicand1};
        }

        private static double[] ensureRadicalWithCoef(double[] radical) {
            final double coef;
            final double degree;
            final double radicand;
            if (radical.length > 2) {
                coef = radical[Constants.ARR_1ST_INDEX];
                degree = radical[Constants.ARR_2ND_INDEX];
                radicand = radical[Constants.ARR_3RD_INDEX];
            } else {
                coef = 1;
                degree = radical[Constants.ARR_1ST_INDEX];
                radicand = radical[Constants.ARR_2ND_INDEX];
            }

            return new double[]{coef, degree, radicand};
        }

        private static double[][] ensureRadicalsWithCoef(double[] radical1, double[] radical2) {
            final double[] normalizeRadical1 = ensureRadicalWithCoef(radical1);
            final double radical1coef = normalizeRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizeRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizeRadical1[Constants.ARR_3RD_INDEX];

            final double[] normalizeRadical2 = ensureRadicalWithCoef(radical2);
            final double radical2coef = normalizeRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizeRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizeRadical2[Constants.ARR_3RD_INDEX];

            return new double[][]{
                {radical1coef, radical1degree, radicand1},
                {radical2coef, radical2degree, radicand2}
            };
        }

        public static double[] subtractRadicals(double[] radical1, double[] radical2) {
            final double[][] normalizedRadicals = ensureRadicalsWithCoef(radical1, radical2);
            final double[] normalizedRadical1 = normalizedRadicals[Constants.ARR_1ST_INDEX];
            final double[] normalizedRadical2 = normalizedRadicals[Constants.ARR_2ND_INDEX];

            final double radical1coef = normalizedRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizedRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizedRadical1[Constants.ARR_3RD_INDEX];

            final double radical2coef = normalizedRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizedRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizedRadical2[Constants.ARR_3RD_INDEX];

            if (radicand1 != radicand2 || radical1degree != radical2degree) {
                throw new IllegalArgumentException();
            }

            return new double[]{radical1coef - radical2coef, radical1degree, radicand1};
        }

        public static double[] multiplyRadicals(double[] radical1, double[] radical2) {
            final double[][] normalizedRadicals = ensureRadicalsWithCoef(radical1, radical2);
            final double[] normalizedRadical1 = normalizedRadicals[Constants.ARR_1ST_INDEX];
            final double[] normalizedRadical2 = normalizedRadicals[Constants.ARR_2ND_INDEX];

            final double radical1coef = normalizedRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizedRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizedRadical1[Constants.ARR_3RD_INDEX];

            final double radical2coef = normalizedRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizedRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizedRadical2[Constants.ARR_3RD_INDEX];

            if (radical1degree != radical2degree) {
                throw new IllegalArgumentException();
            }

            return new double[]{radical1coef * radical2coef, radical1degree, radicand1 * radicand2};
        }

        public static double[] divideRadicals(double[] radical1, double[] radical2) {
            final double[][] normalizedRadicals = ensureRadicalsWithCoef(radical1, radical2);
            final double[] normalizedRadical1 = normalizedRadicals[Constants.ARR_1ST_INDEX];
            final double[] normalizedRadical2 = normalizedRadicals[Constants.ARR_2ND_INDEX];

            final double radical1coef = normalizedRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizedRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizedRadical1[Constants.ARR_3RD_INDEX];

            final double radical2coef = normalizedRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizedRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizedRadical2[Constants.ARR_3RD_INDEX];

            if (radical1degree != radical2degree) {
                throw new IllegalArgumentException();
            }

            return new double[]{radical1coef / radical2coef, radical1degree, radicand1 / radicand2};
        }

        /**
         * @return simplified a × ⁿ√b
         */
        public static double[] simplifyRadical(double[] radical) {
            final double[] normalizedRadical = ensureRadicalWithCoef(radical);
            final double coef = normalizedRadical[Constants.ARR_1ST_INDEX];
            final double degree = normalizedRadical[Constants.ARR_2ND_INDEX];
            final double radicand = normalizedRadical[Constants.ARR_3RD_INDEX];

            final var factorCounts = Arithmetic.primeFactorMap((long) radicand);

            // Extract groups according to degree
            long outsideCoef = 1;
            long insideRadicand = 1;
            for (Map.Entry<Long, Integer> entry : factorCounts.entrySet()) {
                final long base = entry.getKey();
                final int count = entry.getValue();
                final int outsideCount = count / (int) degree;
                final int insideCount = count % (int) degree;
                outsideCoef *= (long) Math.pow(base, outsideCount);
                insideRadicand *= (long) Math.pow(base, insideCount);
            }

            // Multiply by original coefficient
            outsideCoef *= (long) coef;

            return new double[]{outsideCoef, degree, insideRadicand};
        }

        public static long[] extractRadicalGroupsByDegree(double[] radical) {
            final double degree = radical[Constants.ARR_1ST_INDEX];
            final double radicand = radical[Constants.ARR_2ND_INDEX];

            final var factorCounts = Arithmetic.primeFactorMap((long) radicand);

            long outsideCoef = 1;
            long insideRadicand = 1;
            for (Map.Entry<Long, Integer> entry : factorCounts.entrySet()) {
                final long base = entry.getKey();
                final int count = entry.getValue();
                final int outsideCount = count / (int) degree;
                final int insideCount = count % (int) degree;
                outsideCoef *= (long) Math.pow(base, outsideCount);
                insideRadicand *= (long) Math.pow(base, insideCount);
            }
            return new long[]{outsideCoef, insideRadicand};
        }

        /**
         * @return simplified a × ⁿ√b + c × ᵐ√d
         */
        public static double[][] simplifyRadicalsSum(double[] radical1, double[] radical2) {
            final double[][] normalizedRadicals = ensureRadicalsWithCoef(radical1, radical2);
            final double[] normalizedRadical1 = normalizedRadicals[Constants.ARR_1ST_INDEX];
            final double[] normalizedRadical2 = normalizedRadicals[Constants.ARR_2ND_INDEX];

            final double radical1coef = normalizedRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizedRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizedRadical1[Constants.ARR_3RD_INDEX];

            final double radical2coef = normalizedRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizedRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizedRadical2[Constants.ARR_3RD_INDEX];

            final long[] radicalGroup1 = extractRadicalGroupsByDegree(new double[]{radical1degree, radicand1});
            final long outsideCoef1 = radicalGroup1[Constants.ARR_1ST_INDEX] * (long) radical1coef;
            final long insideRadicand1 = radicalGroup1[Constants.ARR_2ND_INDEX];

            final long[] radicalGroup2 = extractRadicalGroupsByDegree(new double[]{radical2degree, radicand2});
            final long outsideCoef2 = radicalGroup2[Constants.ARR_1ST_INDEX] * (long) radical2coef;
            final long insideRadicand2 = radicalGroup2[Constants.ARR_2ND_INDEX];

            return new double[][]{
                {outsideCoef1, radical1degree, insideRadicand1},
                {outsideCoef2, radical2degree, insideRadicand2}
            };
        }

        /**
         * a × ⁿ√b × c × ᵐ√d = (a × c) × ᵏ√(bˢ × dᵗ)
         * where
         * <ul>
         *     <li>k = lcm(n, m)</li>
         *     <li>s = k / n</li>
         *     <li>t = k / m</li>
         * </ul>
         */
        public static double[] simplifyRadicalsProduct(double[] radical1, double[] radical2) {
            final double[][] normalizedRadicals = ensureRadicalsWithCoef(radical1, radical2);
            final double[] normalizedRadical1 = normalizedRadicals[Constants.ARR_1ST_INDEX];
            final double[] normalizedRadical2 = normalizedRadicals[Constants.ARR_2ND_INDEX];

            final double radical1coef = normalizedRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizedRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizedRadical1[Constants.ARR_3RD_INDEX];

            final double radical2coef = normalizedRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizedRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizedRadical2[Constants.ARR_3RD_INDEX];

            final long k = Arithmetic.lcmWithPrimeFactorization(new double[]{radical1degree, radical2degree});
            final double s = k / radical1degree;
            final double t = k / radical2degree;
            final double radicandProduct = Math.pow(radicand1, s) * Math.pow(radicand2, t);
            final double[] unsimplified = {radical1coef * radical2coef, k, radicandProduct};
            return simplifyRadical(unsimplified);
        }

        /**
         * (a × ⁿ√b) / (c × ᵐ√d) = (a / (c × d)) × ᵏ√(bˢ × dᵗ)
         * where
         * <ul>
         *     <li>k = lcm(n, m)</li>
         *     <li>s = k / n</li>
         *     <li>t = (k × (m - 1)) / m</li>
         * </ul>
         */
        public static double[] simplifyRadicalsQuotient(double[] radical1, double[] radical2) {
            final double[][] normalizedRadicals = ensureRadicalsWithCoef(radical1, radical2);
            final double[] normalizedRadical1 = normalizedRadicals[Constants.ARR_1ST_INDEX];
            final double[] normalizedRadical2 = normalizedRadicals[Constants.ARR_2ND_INDEX];

            final double radical1coef = normalizedRadical1[Constants.ARR_1ST_INDEX];
            final double radical1degree = normalizedRadical1[Constants.ARR_2ND_INDEX];
            final double radicand1 = normalizedRadical1[Constants.ARR_3RD_INDEX];

            final double radical2coef = normalizedRadical2[Constants.ARR_1ST_INDEX];
            final double radical2degree = normalizedRadical2[Constants.ARR_2ND_INDEX];
            final double radicand2 = normalizedRadical2[Constants.ARR_3RD_INDEX];

            final long k = Arithmetic.lcmWithPrimeFactorization(new double[]{radical1degree, radical2degree});
            final double s = k / radical1degree;
            final double t = k * (radical2degree - 1) / radical2degree;
            final double radicandProduct = Math.pow(radicand1, s) * Math.pow(radicand2, t);
            return new double[]{radical1coef / (radical2coef * radicand2), k, radicandProduct};
        }

        // Exponents and logarithms

        /**
         * @return xⁿ * xᵐ = xⁿ⁺ᵐ
         */
        public static double addExponentsLaw(double base, double[] exponents) {
            final double exponent = Arrays.stream(exponents).sum();
            return Math.pow(base, exponent);
        }

        /**
         * @return xⁿ / xᵐ = xⁿ⁻ᵐ
         */
        public static double subtractExponentsLaw(double base, double[] exponents) {
            final double exponent = Arrays.stream(exponents)
                .reduce((left, right) -> left - right).orElse(0);
            return Math.pow(base, exponent);
        }

        /**
         * @return x⁻ⁿ = (1/x)ⁿ
         */
        public static double negativeExponent(double base, double exponent) {
            return Math.pow(1 / base, Math.abs(exponent));
        }

        /**
         * @return xⁿ * yⁿ = (x * y)ⁿ
         */
        public static double multiplyWithSamePower(double x, double y, double exponent) {
            return Math.pow(x * y, exponent);
        }

        /**
         * aka
         * <ol>
         *     <li>lg</li>
         *     <li>the common logarithm</li>
         *     <li>the decimal logarithm</li>
         *     <li>the decadic logarithm</li>
         *     <li>the standard logarithm</li>
         *     <li>the Briggsian logarithm</li>
         * </ol>
         * <p>The difference between the root and logarithm is:
         * <br/>ᵏ√(nᵏ) = n
         * <br/>logₙ(nᵏ) = k
         * </p>
         * <ul>
         *     <li>aʸ = x</li>
         *     <li>logₐ(x) = y</li>
         *     <li>aˡᵒᵍₐ⁽ˣ⁾ = x</li>
         *     <li>y = logₑx = ln(x)</li>
         *     <li>x = eʸ = exp(y)</li>
         * </ul>
         *
         * @return log₁₀x
         */
        public static double log(double number) {
            checkGreater0(number);
            return Math.log10(number);
        }

        /**
         * y = log_b(x)
         * x = bʸ = b^(log_bˣ)
         * y = log_b(x) = log_b(bʸ)
         *
         * @return x = log_b⁻¹(y) = bʸ
         */
        public static double antilog(double logarithm, double base) {
            return Math.pow(base, logarithm);
        }

        /**
         * @return log₁₀(a * b) = log₁₀(a) + log₁₀(b)
         */
        public static double logProductRule(double a, double b) {
            checkGreater0(a);
            checkGreater0(b);
            return log(a) + log(b);
        }

        /**
         * @return logₙ(a * b) = logₙ(a) + logₙ(b)
         */
        public static double logProductRule(double a, double b, double base) {
            checkGreater0(a);
            checkGreater0(b);
            checkGreater(base, ONE);
            return logChangeOfBase(a, base) + logChangeOfBase(b, base);
        }

        /**
         * @return logₙ(a / b) = logₙ(a) - logₙ(b)
         */
        public static double logQuotientRule(double a, double b, double base) {
            checkGreater0(a);
            checkGreater0(b);
            checkGreater(base, ONE);
            return logChangeOfBase(a, base) - logChangeOfBase(b, base);
        }

        /**
         * @return logₙ(aᵏ) = k * logₙ(a)
         */
        public static double logPowerRule(double number, double exponent, double base) {
            checkGreater0(number);
            checkGreater0(exponent);
            checkGreater(base, ONE);
            return exponent * logChangeOfBase(number, base);
        }

        /**
         * x * logₙ a + y * logₙ b = logₙ(aˣ) + logₙ(bʸ)
         *
         * @return logₙ(aˣ * bʸ)
         */
        public static double logAdd(double a, double exponentX, double b, double exponentY, double base) {
            checkGreater0(a);
            checkGreater0(b);
            checkGreater(base, ONE);
            return logChangeOfBase(Math.pow(a, exponentX) * Math.pow(b, exponentY), base);
        }

        /**
         * x * logₙ a - y * logₙ b = logₙ(aˣ) - logₙ(bʸ)
         *
         * @return logₙ(aˣ / bʸ)
         */
        public static double logSubtract(double a, double exponentX, double b, double exponentY, double base) {
            checkGreater0(a);
            checkGreater0(b);
            checkGreater(base, ONE);
            return logChangeOfBase(Math.pow(a, exponentX) / Math.pow(b, exponentY), base);
        }

        /**
         * @return x * logₙ a = logₙ(aˣ)
         */
        public static double logMultiplyNumber(double number, double exponent, double base) {
            checkGreater0(number);
            checkGreater(base, ONE);
            return logChangeOfBase(Math.pow(number, exponent), base);
        }

        /**
         * @return logₐ(x) = log(x) / log(a)
         */
        public static double logChangeOfBase(double x, double base) {
            checkGreater0(x);
            checkGreater(base, ONE);
            return log(x) / log(base);
        }

        /**
         * @return logₐ(x) = log_b(x) / log_b(a)
         */
        public static double logChangeOfBase(double x, double base, double newBase) {
            checkGreater0(x);
            checkGreater(base, ONE);
            checkGreater(newBase, ONE);
            final double numerator = log(x) / log(newBase);
            final double denominator = log(base) / log(newBase);
            return numerator / denominator;
        }

        /**
         * −logₐ(b) = n
         * 1/aⁿ = b
         *
         * @return logₐ(1/x) = n
         */
        public static double negativeLog(double x, double base) {
            checkGreater0(x);
            checkGreater(base, ONE);
            return logChangeOfBase(Arithmetic.reciprocal(x), base);
        }

        /**
         * Natural logarithm
         * <ul>
         *     <li>Product ln(x × y) = ln(x) + ln(y)</li>
         *     <li>Log of power ln(xy) = y × ln(x)</li>
         *     <li>ln(e) = 1</li>
         *     <li>ln(1) = 0</li>
         *     <li>Log reciprocal ln(1/x) = −ln(x)</li>
         * </ul>
         *
         * @return logₑx
         */
        public static double ln(double x) {
            return Math.log(x);
        }

        /**
         * @return logₐ(x) = ln(x) / ln(a)
         */
        public static double lnChangeOfBase(double x, double base) {
            checkGreater0(x);
            checkGreater(base, ONE);
            return ln(x) / ln(base);
        }

        /**
         * aka the binary logarithm
         *
         * @return log₂(x)
         */
        public static double log2(double x) {
            checkGreater0(x);
            return lnChangeOfBase(x, 2);
        }

        /**
         * Alternative: 1 / log₂(1 + increase %)
         *
         * @return log(x) / log(1 + increase %)
         */
        public static double doublingTime(double initialAmount, double increase) {
            return log(initialAmount) / log(1 + increase / 100);
        }

        /**
         * (n) = ( n )
         * (k)   (n-k)
         * C(n,k) = C(n,n-k)
         *
         * @return (a+b)ⁿ = C₀aⁿ + C₁aⁿ⁻¹b + C₂aⁿ⁻²b² + ... + Cₙbⁿ
         */
        public static long binomialCoefficient(long totalItems, long numberOfItemsChosen) {
            return Combinatorics.combinations(totalItems, numberOfItemsChosen);
        }

        /**
         * Both binomial1 and binomial2 params should be in ascending order (the lowest terms go first).
         * (a₁x + a₀) × (b₁x + b₀) = c₂x² + c₁x + c₀
         * (a + b) × (c + d) = a × (c + d) + b × (c + d) = a×c + a×d + b×c + b×d
         * (a + b) × (c + d) = (a + b) × c + (a + b) × d = a×c + b×c + a×d + b×d
         *
         * @return (a₁b₁)x² + (a₁b₀ + a₀b₁)x + (a₀b₀)
         */
        public static double[] multiplyBinomials(double[] binomial1, double[] binomial2) {
            Objects.requireNonNull(binomial1);
            Objects.requireNonNull(binomial2);
            final int idx2 = Constants.ARR_2ND_INDEX;
            Objects.checkIndex(idx2, binomial1.length);
            Objects.checkIndex(idx2, binomial2.length);

            final double[] result = new double[3];
            final int idx1 = Constants.ARR_1ST_INDEX;
            final double a0 = binomial1[idx1];
            final double b0 = binomial2[idx1];
            final double a1 = binomial1[idx2];
            final double b1 = binomial2[idx2];
            result[idx1] = a0 * b0;
            result[idx2] = a1 * b0 + a0 * b1;
            result[Constants.ARR_3RD_INDEX] = a1 * b1;
            return result;
        }

        /**
         * Polynomial's degree
         * <ul>
         *     <li>Second: a₂x² + a₁x + a₀</li>
         *     <li>Third: a₃x³ + a₂x² + a₁x + a₀</li>
         *     <li>Fourth: a₄x⁴ + a₃x³ + a₂x² + a₁x + a₀</li>
         *     <li>Fifth: a₅x⁵ + a₄x⁴ + a₃x³ + a₂x² + a₁x + a₀</li>
         * </ul>
         *
         * <ul>
         *     <li>D > 0: two distinct real number solutions.</li>
         *     <li>D = 0: repeated real number solution.</li>
         *     <li>D < 0: neither of the solutions are real numbers.</li>
         * </ul>
         *
         * @param polynomial the lowest terms go first: a₀ + a₁x + … + aₙxⁿ
         */
        public static double discriminant(double[] polynomial) {
            Objects.requireNonNull(polynomial);
            final int n = polynomial.length;
            switch (n) {
                case 3 -> {
                    // D = b² − 4ac
                    final double c = polynomial[Constants.ARR_1ST_INDEX];
                    final double b = polynomial[Constants.ARR_2ND_INDEX];
                    final double a = polynomial[Constants.ARR_3RD_INDEX];
                    return b * b - 4 * a * c;
                }
                case 4 -> {
                    // cubic: ax³ + bx² + cx + d = 0
                    // D = b²c² - 4ac³ - 4b³d - 27a²d² + 18abcd
                    final double d = polynomial[Constants.ARR_1ST_INDEX];
                    final double c = polynomial[Constants.ARR_2ND_INDEX];
                    final double b = polynomial[Constants.ARR_3RD_INDEX];
                    final double a = polynomial[Constants.ARR_4TH_INDEX];
                    return b * b * c * c - 4 * a * Math.pow(c, 3) - 4 * Math.pow(b, 3) * d
                        - 27 * a * a * d * d + 18 * a * b * c * d;
                }
                case 5 -> {
                    // quartic: ax⁴ + bx³ + cx² + dx + e = 0
                    // D = 256a³e³ - 192a²bde² - 128a²c²e²
                    //   + 144a²cd²e - 27a²d⁴ + 144ab²ce²
                    //   - 6ab²d²e - 80abc²de + 18abcd³
                    //   + 16ac⁴e - 4ac³d² - 27b²e²
                    //   + 18b³cde - 4b³d³ - 3b²c³e
                    //   + b²c²d²
                    final double e = polynomial[Constants.ARR_1ST_INDEX];
                    final double d = polynomial[Constants.ARR_2ND_INDEX];
                    final double c = polynomial[Constants.ARR_3RD_INDEX];
                    final double b = polynomial[Constants.ARR_4TH_INDEX];
                    final double a = polynomial[Constants.ARR_5TH_INDEX];
                    final double aSquare = a * a;
                    final double aCube = aSquare * a;
                    final double bSquare = b * b;
                    final double bCube = bSquare * b;
                    final double cSquare = c * c;
                    final double cCube = cSquare * c;
                    final double dSquare = d * d;
                    final double dCube = dSquare * d;
                    final double eSquare = e * e;
                    final double eCube = eSquare * e;
                    return 256 * aCube * eCube - 192 * aSquare * b * d * eSquare - 128 * aSquare * cSquare * eSquare
                        + 144 * aSquare * c * dSquare * e - 27 * aSquare * Math.pow(d, 4)
                        + 144 * a * bSquare * c * eSquare
                        - 6 * a * bSquare * dSquare * e - 80 * a * b * cSquare * d * e + 18 * a * b * c * dCube
                        + 16 * a * Math.pow(c, 4) * e - 4 * a * cCube * dSquare - 27 * bSquare * eSquare
                        + 18 * bCube * c * d * e - 4 * bCube * dCube - 3 * bCube * cCube * e
                        + bSquare * cSquare * dSquare;
                }
                default -> throw new UnsupportedOperationException();
            }
        }

        private static double sylvesterResultant(double[] f, double[] g) {
            final int m = f.length - 1;
            final int n = g.length - 1;
            final int size = m + n;
            final double[][] sylvester = new double[size][size];

            // Fill Sylvester matrix for f
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < f.length; j++) {
                    final int col = i + j;
                    if (col < size) {
                        sylvester[i][col] = f[j];
                    }
                }
            }
            // Fill Sylvester matrix for g
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < g.length; j++) {
                    final int col = i + j;
                    if (col < size) {
                        sylvester[n + i][col] = g[j];
                    }
                }
            }
            return LinearAlgebra.determinant(sylvester);
        }

        /**
         * p(x) = aₙxⁿ + … + a₁x + a₀
         * D(p) = aₙx²ⁿ⁻² ∏ⁿᵢ,ⱼ (rᵢ - rⱼ)², i<j
         */
        public static double discriminantFromRoots(double[] polynomial, double[] roots) {
            Objects.requireNonNull(polynomial);
            Objects.requireNonNull(roots);

            final int n = roots.length;
            if (polynomial.length != n + 1) {
                throw new IllegalArgumentException("Roots and coefficients mismatch");
            }

            double prod = 1;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    final double diff = roots[i] - roots[j];
                    prod *= diff * diff;
                }
            }
            final double an = polynomial[Constants.ARR_1ST_INDEX];
            return Math.pow(an, 2. * n - 2) * prod;
        }

        /**
         * P(x) = a₆x⁶ + a₅x⁵ + a₄x⁴ + a₃x³ + a₂x² + a₁x + a₀
         * Q(x) = b₆x⁶ + b₅x⁵ + b₄x⁴ + b₃x³ + b₂x² + b₁x + b₀
         * <p>
         * Both polynomials params expect the lowest terms go first: a₀ + a₁x + … + aₙxⁿ
         *
         * @return P(x) + Q(x)
         */
        public static double[] addPolynomials(double[] polynomial1, double[] polynomial2) {
            Objects.requireNonNull(polynomial1);
            Objects.requireNonNull(polynomial2);
            final int size = Math.max(polynomial1.length, polynomial2.length);
            final double[] p;
            final double[] q;
            if (size > polynomial1.length) {
                p = Arrays.copyOf(polynomial1, size);
            } else {
                p = polynomial1;
            }
            if (size > polynomial2.length) {
                q = Arrays.copyOf(polynomial2, size);
            } else {
                q = polynomial2;
            }
            final double[] result = new double[size];
            for (int i = 0; i < size; i++) {
                result[i] = p[i] + q[i];
            }
            return result;
        }

        /**
         * P(x) = a₆x⁶ + a₅x⁵ + a₄x⁴ + a₃x³ + a₂x² + a₁x + a₀
         * Q(x) = b₆x⁶ + b₅x⁵ + b₄x⁴ + b₃x³ + b₂x² + b₁x + b₀
         * <p>
         * Both polynomials params expect the lowest terms go first: a₀ + a₁x + … + aₙxⁿ
         *
         * @return P(x) - Q(x)
         */
        public static double[] subtractPolynomials(double[] polynomial1, double[] polynomial2) {
            Objects.requireNonNull(polynomial1);
            Objects.requireNonNull(polynomial2);
            final int size = Math.max(polynomial1.length, polynomial2.length);
            final double[] p;
            final double[] q;
            if (size > polynomial1.length) {
                p = Arrays.copyOf(polynomial1, size);
            } else {
                p = polynomial1;
            }
            if (size > polynomial2.length) {
                q = Arrays.copyOf(polynomial2, size);
            } else {
                q = polynomial2;
            }
            final double[] result = new double[size];
            for (int i = 0; i < size; i++) {
                result[i] = p[i] - q[i];
            }
            return result;
        }

        /**
         * (a + b + c) × (d + e) = a × (d + e) + b × (d + e) + c × (d + e) = a×d + a×e + b×d + b×e + c×d + c×e
         * (a + b + c) × (d + e) = (a + b + c) × d + (a + b + c) × e = a×d + b×d + c×d + a×e + b×e + c×e
         * Both polynomials params expect the lowest terms go first: a₀ + a₁x + … + aₙxⁿ
         *
         * @return P(x) * Q(x)
         */
        public static double[] multiplyPolynomials(double[] polynomial1, double[] polynomial2) {
            Objects.requireNonNull(polynomial1);
            Objects.requireNonNull(polynomial2);
            final int size = polynomial1.length + polynomial2.length - 1;
            final double[] result = new double[size];
            for (int i = 0; i < polynomial1.length; i++) {
                for (int j = 0; j < polynomial2.length; j++) {
                    result[i + j] += polynomial1[i] * polynomial2[j];
                }
            }
            return result;
        }

        /**
         * (aₙxⁿ + aₙ₋₁xⁿ⁻¹ + ... + a₁x + a₀) / bₖxᵏ = aₙxⁿ / bₖxᵏ + aₙ₋₁xⁿ⁻¹ / bₖxᵏ + ... + a₁x / bₖxᵏ + a₀ / bₖxᵏ
         * Both polynomials params expect the lowest terms go first: a₀ + a₁x + … + aₙxⁿ
         *
         * @return P(x) / Q(x)
         */
        public static double[] dividePolynomials(double[] dividend, double[] divisor) {
            Objects.requireNonNull(dividend);
            Objects.requireNonNull(divisor);

            final int n = dividend.length - 1;
            final int m = divisor.length - 1;
            if (m < 0) {
                throw new IllegalArgumentException("Divisor cannot be zero polynomial");
            }
            if (n < m) {
                return new double[]{0}; // Degree of dividend < divisor
            }

            final double[] quotient = new double[n - m + 1];
            final double[] remainder = Arrays.copyOf(dividend, dividend.length);

            for (int k = n; k >= m; k--) {
                final double coeff = remainder[k] / divisor[m];
                quotient[k - m] = coeff;
                for (int j = 0; j <= m; j++) {
                    remainder[k - j] -= coeff * divisor[m - j];
                }
            }
            // In ascending order
            return quotient;
        }

        public static double[] quadraticStdFormulaToVertex(double[] quadratic) {
            final double c = quadratic[Constants.ARR_1ST_INDEX];
            final double b = quadratic[Constants.ARR_2ND_INDEX];
            final double a = quadratic[Constants.ARR_3RD_INDEX];
            final double h = -b / (2 * a);
            final double k = a * h * h + b * h + c;
            return new double[]{k, h, a};
        }

        public static double[] quadraticStdFormulaToFactored(double[] quadratic) {
            final double b = quadratic[Constants.ARR_2ND_INDEX];
            final double a = quadratic[Constants.ARR_3RD_INDEX];

            final double discriminant = discriminant(quadratic);
            final double doubleA = 2 * a;
            if (discriminant >= 0) {
                final double sqrtDiscriminant = squareRoot(discriminant);
                return new double[]{a, (-b + sqrtDiscriminant) / doubleA, (-b - sqrtDiscriminant) / doubleA};
            } else {
                final double realPart = -b / doubleA;
                final double imagPart = squareRoot(-discriminant) / doubleA;
                return new double[]{a, realPart, imagPart};
            }
        }

        /**
         * Formula form:
         * <ul>
         *     <li>Standard: ax² + bx + c = 0</li>
         *     <li>Vertex: a(x - h)² + k = 0</li>
         *     <li>Factored: a(x - x₁)(x - x₂) = 0</li>
         * </ul>
         * The quadratic formula: x = (-b ± √Δ)/2a = (-b ±√(b² − 4ac)) / (2a)
         * For Δ < 0 use complex number, Real(x) = -B/2A Imaginary(x) = ±(√Δ)/2A
         *
         * @param quadratic the lowest terms go first: c + bx + ax²
         * @return [
         * x₁ = (-b + √Δ)/2a
         * x₂ = (-b – √Δ)/2a
         * ]
         */
        public static double[] quadraticRoots(double[] quadratic) {
            final double discriminant = discriminant(quadratic);
            final double b = quadratic[Constants.ARR_2ND_INDEX];
            final double a = quadratic[Constants.ARR_3RD_INDEX];
            final double doubleA = 2 * a;
            if (discriminant < 0) {
                final double realPart = -b / doubleA;
                final double imagPart = squareRoot(-discriminant) / doubleA;
                return new double[]{realPart, imagPart};
            } else {
                final double sqrtDiscriminant = squareRoot(discriminant);
                return new double[]{(-b + sqrtDiscriminant) / doubleA, (-b - sqrtDiscriminant) / doubleA};
            }
        }

        /**
         * <ul>
         *     <li>√(1/n ∑ⁿᵢ₌₁ x²ᵢ)</li>
         *     <li>Weighted: √((w₁x²₁ + w₂x²₂ + ... + wₙx²ₙ) / (w₁ + w₂ + ... + wₙ))</li>
         *     <li>Generalized (power) means: ᵖ√((xᵖ₁ + xᵖ₂ + ... + xᵖₙ); (1/n ∑ⁿᵢ₌₁ xᵖᵢ)¹/ᵖ</li>
         *     <li>p=1 - the arithmetic mean; p=2 - the quadratic mean; p=3 - the cubic mean</li>
         *     <li>(1/n ∑ⁿᵢ₌₁ x⁻¹ᵢ)⁻¹ = n/(∑ⁿᵢ₌₁ x⁻¹ⱼ) = n/(∑ⁿᵢ₌₁ 1/xⱼ)</li>
         * </ul>
         *
         * @return √((x²₁ + x²₂ + ... + x²ₙ)/n)
         */
        public static double rms(double[] dataset) {
            final int n = dataset.length;
            double sumOfSquares = 0;
            for (double value : dataset) {
                sumOfSquares += value * value;
            }
            return squareRoot(sumOfSquares / n);
        }
    }

    public static final class Geometry {
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
            checkSameDimensions(vectorA, vectorB);

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
            checkSameDimensions(pointACoords, pointBCoords);

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
            checkSameDimensions(pointACoords, pointBCoords);

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
            checkSameDimensions(pointCoords, pivotCoords);
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
            checkSameDimensions(pointACoords, pointBCoords);
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
    }


    /**
     * <table>
     *     <tr><th>Degrees</td><th>Radians</th><th>sin(α)</th><th>cos(α)</th><th>tan(α)</th><th>cot(α)</th></tr>
     *     <tr><td>0°</td><td>0</td><td>0</td><td>1</td><td>0</td><td>Undefined</td></tr>
     *     <tr><td>15°</td><td>π/12</td><td>(√6−√2)/4</td><td>(√6+√2)/4</td><td></td><td></td></tr>
     *     <tr><td>30°</td><td>π/6</td><td>0.5</td><td>√3/2</td><td>√3/3</td><td>√3</td></tr>
     *     <tr><td>45°</td><td>π/4</td><td>√2/2</td><td>√2/2</td><td>1</td><td>1</td></tr>
     *     <tr><td>60°</td><td>π/3</td><td>√3/2</td><td>0.5</td><td>√3</td><td>√3/3</td></tr>
     *     <tr><td>75°</td><td>5π/12</td><td>(√6+√2)/4</td><td>(√6-√2)/4</td><td></td><td></td></tr>
     *     <tr><td>90°</td><td>π/2</td><td>1</td><td>0</td><td>Undefined</td><td>0</td></tr>
     *     <tr><td>105°</td><td>7π/12</td><td>(√6+√2)/4</td><td>-(√6-√2)/4</td><td></td><td></td></tr>
     *     <tr><td>120°</td><td>2π/3</td><td>√3/2</td><td>-0.5</td><td>-√3</td><td>-√3/3</td></tr>
     *     <tr><td>135°</td><td>3π/4</td><td>√2/2</td><td>-√2/2</td><td>-1</td><td>-1</td></tr>
     *     <tr><td>150°</td><td>5π/6</td><td>0.5</td><td>-√3/2</td><td>-√3/3</td><td>-√3</td></tr>
     *     <tr><td>165°</td><td>11π/12</td><td>(√6−√2)/4</td><td>-(√6+√2)/4</td><td></td><td></td></tr>
     *     <tr><td>180°</td><td>π</td><td>0</td><td>-1</td><td>0</td><td>Undefined</td></tr>
     *     <tr><td>210°</td><td>7π/6</td><td>-0.5</td><td>-√3/2</td><td>√3/3</td><td>√3</td></tr>
     *     <tr><td>225°</td><td>5π/4</td><td>-√2/2</td><td>-√2/2</td><td>1</td><td>1</td></tr>
     *     <tr><td>240°</td><td>4π/3</td><td>-√3/2</td><td>-0.5</td><td>√3</td><td>√3/3</td></tr>
     *     <tr><td>270°</td><td>3π/2</td><td>-1</td><td>0</td><td>Undefined</td><td>0</td></tr>
     *     <tr><td>300°</td><td>5π/3</td><td>-√3/2</td><td>0.5</td><td>-√3</td><td>-√3/3</td></tr>
     *     <tr><td>315°</td><td>7π/4</td><td>-√2/2</td><td>√2/2</td><td>-1</td><td>-1</td></tr>
     *     <tr><td>330°</td><td>11π/6</td><td>-0.5</td><td>√3/2</td><td>-√3/3</td><td>-√3</td></tr>
     *     <tr><td>360°</td><td>2π</td><td>0</td><td>1</td><td>0</td><td>Undefined</td></tr>
     * </table>
     * <br/><strong>Inverse</strong>
     * <table>
     *     <tr><th>x</th><th colspan="2">arccos(x)</th><th colspan="2">arcsine(x)</th></tr>
     *     <tr><td></td><td>Degrees</td><td>Radians</td><td>Degrees</td><td>Radians</td></tr>
     *     <tr><td>-1</td><td>180°</td><td>π</td><td>-90°</td><td>-π/2</td></tr>
     *     <tr><td>-√3/2</td><td>150°</td><td>5π/6</td><td>-60°</td><td>-π/3</td></tr>
     *     <tr><td>-√2/2</td><td>135°</td><td>3π/4</td><td>-45°</td><td>-π/4</td></tr>
     *     <tr><td>-1/2</td><td>120°</td><td>2π/3</td><td>-30°</td><td>-π/6</td></tr>
     *     <tr><td>0</td><td>90°</td><td>π/2</td><td>0°</td><td>0</td></tr>
     *     <tr><td>1/2</td><td>60°</td><td>π/3</td><td>30°</td><td>π/6</td></tr>
     *     <tr><td>√2/2</td><td>45°</td><td>π/4</td><td>45°</td><td>π/4</td></tr>
     *     <tr><td>√3/2</td><td>30°</td><td>π/6</td><td>60°</td><td>π/3</td></tr>
     *     <tr><td>1</td><td>0°</td><td>0</td><td>90°</td><td>π/2</td></tr>
     * </table>
     * <br/><strong>Inverse Tangent</strong>
     * <table>
     *     <tr><th>x</th><th colspan="2">arctan(x)</th></tr>
     *     <tr><td></td><td>Degrees</td><td>Radians</td></tr>
     *     <tr><td>−∞</td><td>-90°</td><td>-π/2</td></tr>
     *     <tr><td>-3</td><td>−71.565°</td><td>−1.2490</td></tr>
     *     <tr><td>-2</td><td>−63.435°</td><td>−1.1071</td></tr>
     *     <tr><td>-√3</td><td>−60°</td><td>-π/3</td></tr>
     *     <tr><td>-1</td><td>-45°</td><td>-π/4</td></tr>
     *     <tr><td>-√3/3</td><td>-30°</td><td>-π/6</td></tr>
     *     <tr><td>0</td><td>0°</td><td>0</td></tr>
     *     <tr><td>√3/2</td><td>30°</td><td>π/6</td></tr>
     *     <tr><td>1</td><td>45°</td><td>π/4</td></tr>
     *     <tr><td>√3</td><td>60°</td><td>π/3</td></tr>
     *     <tr><td>2</td><td>63.435°</td><td>1.1071</td></tr>
     *     <tr><td>3</td><td>71.565°</td><td>1.2490</td></tr>
     *     <tr><td>∞</td><td>90°</td><td>π/2</td></tr>
     * </table>
     */
    public static final class Trigonometry {
        public static final double PI_OVER_32 = Math.PI / 32;
        public static final double PI_OVER_12 = Math.PI / 12;
        public static final double PI_OVER_6 = Math.PI / 6;
        public static final double PI_OVER_4 = Math.PI / 4;
        public static final double PI_OVER_3 = Math.PI / 3;
        public static final double PI_OVER_2 = Math.PI / 2;
        public static final double PI5_OVER_12 = 5 * Math.PI / 12;
        public static final double PI7_OVER_12 = 7 * Math.PI / 12;
        public static final double PI2_OVER_3 = 2 * Math.PI / 3;
        public static final double PI3_OVER_4 = 3 * Math.PI / 4;
        public static final double PI5_OVER_6 = 5 * Math.PI / 6;
        public static final double PI11_OVER_12 = 11 * Math.PI / 12;
        public static final double PI7_OVER_6 = 7 * Math.PI / 6;
        public static final double PI5_OVER_4 = 5 * Math.PI / 4;
        public static final double PI4_OVER_3 = 4 * Math.PI / 3;
        public static final double PI3_OVER_2 = 3 * Math.PI / 2;
        public static final double PI5_OVER_3 = 5 * Math.PI / 3;
        public static final double PI7_OVER_4 = 7 * Math.PI / 4;
        public static final double PI11_OVER_6 = 11 * Math.PI / 6;
        public static final double PI2 = 2 * Math.PI;
        public static final double PI3 = 3 * Math.PI;
        public static final double PI4 = 4 * Math.PI;
        public static final double PI8 = 8 * Math.PI;

        private Trigonometry() {
        }

        public static double csc(double hypotenuse, double opposite) {
            return hypotenuse / opposite;
        }

        /**
         * csc(α) = c / a
         * csc(x) = (x² + y²) / y
         * csc(x) = sin⁻¹(x)
         * D(csc) = {x : x ≠ k*180°, k ∈ ℤ}.
         * Cofunction: sec(x).
         * <p/>The cosecant function
         * <br/>- is odd: csc(x) = -csc(x)
         * <br/>- is periodic: csc(x) = csc(x + 360°)
         * <br/>- doesn't always exist.
         * <br/>- Range: -∞<y≤-1 ∪ 1≤y<∞
         *
         * @return csc(x) = 1 / sin(x)
         */
        public static double csc(double angleRadians) {
            final double sinResult = Math.sin(angleRadians);
            if (sinResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / sinResult;
        }

        public static double sec(double hypotenuse, double adjacent) {
            return hypotenuse / adjacent;
        }

        /**
         * sec(α) = c / b
         * sec(α) = (√(x² + y²)) / x
         * sec(x) = (cos(x))⁻¹
         * D(sec) = {x : x ≠ 90° + k*180°, k ∈ X}.
         * Cofunction: csc(x).
         * <p/>The secant function
         * <br/>- is even: sec(α) = sec(-α)
         * <br/>- is periodic: sec(x) = sec(x + 360°)
         * <br/>- doesn't always exist.
         * <br/>- Range: -∞<y≤-1 ∪ 1≤y<∞
         *
         * @return sec(x) = 1 / cos(x)
         */
        public static double sec(double angleRadians) {
            final double cosResult = Math.cos(angleRadians);
            if (cosResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / cosResult;
        }

        public static double cot(double adjacent, double opposite) {
            return adjacent / opposite;
        }

        /**
         * cot(α) = b / c
         * cot(α) = x / y
         * cot(x) = (tan(x))⁻¹
         * cot(x) = cos(x) / sin(x)
         * D(cot) = {x : x ≠ k*180°, k ∈ ℤ}.
         * Cofunction: tan(x).
         * <p/>The cotangent function
         * <br/>- is odd: cot(x) = -cot(-x)
         * <br/>- is periodic: cot(x) = cot(x + 360°)
         * <br/>- doesn't always exist.
         * <br/>- Range: -∞<y<∞
         *
         * @return cot(α) = 1 / tan(α)
         */
        public static double cot(double angleRadians) {
            final double tanResult = Math.tan(angleRadians);
            if (tanResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / tanResult;
        }

        /**
         * sin(2θ) = sin(θ+θ) = sin(θ)cos(θ) + cos(θ)sin(θ)
         *
         * @return sin(2θ) = 2sin(θ)cos(θ)
         */
        public static double sinDoubleAngle(double angleThetaRadians) {
            return 2 * Math.sin(angleThetaRadians) * Math.cos(angleThetaRadians);
        }

        /**
         * cos(2θ) = cos²(θ) − sin²(θ)
         * cos(2θ) = 2cos²(θ) − 1
         *
         * @return cos(2θ) = 1 − 2sin²(θ)
         */
        public static double cosDoubleAngle(double angleThetaRadians) {
            return 1 - 2 * Math.sin(angleThetaRadians) * Math.sin(angleThetaRadians);
        }

        /**
         * tan(2θ) = tan(θ+θ) = (tan(θ)+tan(θ)) / (1 − tan(θ)*tan(θ))
         *
         * @return tan(2θ) = (2tan(θ)) / (1−tan²(θ))
         */
        public static double tanDoubleAngle(double angleThetaRadians) {
            return 2 * Math.tan(angleThetaRadians) / (1 - Math.tan(angleThetaRadians) * Math.tan(angleThetaRadians));
        }

        /**
         * sin²(x/2) = (1−cos(x)) / 2
         *
         * @return sin²(x/2) = ±√((1−cos(x)) / 2)
         */
        public static double sinHalfAngle(double angleRadians) {
            return Math.sqrt((1 - Math.cos(angleRadians)) / 2);
        }

        /**
         * cos²(x/2) = (1+cos(x)) / 2
         *
         * @return cos(x/2) = ±√((1+cos(x)) / 2)
         */
        public static double cosHalfAngle(double angleRadians) {
            return Math.sqrt((1 + Math.cos(angleRadians)) / 2);
        }

        /**
         * tan²(x/2) = (1-cos(x)) / (1+cos(x))
         *
         * @return tan(x/2) = ±√((1-cos(x)) / (1+cos(x)))
         */
        public static double tanHalfAngle(double angleRadians) {
            final double cosine = Math.cos(angleRadians);
            return Math.sqrt((1 - cosine) / (1 + cosine));
        }

        public static double sinTripleAngleIdentity(double theta) {
            return 3 * Math.sin(theta) - 4 * Math.pow(Math.sin(theta), 3);
        }

        public static double cosTripleAngleIdentity(double theta) {
            return 4 * Math.pow(Math.cos(theta), 3) - 3 * Math.cos(theta);
        }

        public static double tanTripleAngleIdentity(double theta) {
            final double numerator = 3 * Math.tan(theta) - Math.pow(Math.tan(theta), 3);
            final double denominator = 1 - 3 * Math.tan(theta) * Math.tan(theta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return numerator / denominator;
        }

        /**
         * a = √(b² + c² - 2bc × cos(α))
         * b = √(a² + c² - 2ac × cos(β))
         * c = √(a² + b² - 2ab × cos(γ))
         *
         * @return √c² = a² + b² - 2ab * cos(γ)
         */
        public static double lawOfCosSAS(double sideA, double sideB, double angleGammaRadians) {
            final double sideCSquared = sideA * sideA + sideB * sideB - 2 * sideA * sideB * Math.cos(angleGammaRadians);
            return Math.sqrt(sideCSquared);
        }

        /**
         * Alternative for isosceles triangle:
         * α = arccos((a² + a² - b²) / (2a²))
         * β = arccos((a² + b² - a²) / (2ab)) = arccos(b / (2a))
         * <br/>
         * α = arccos((b² + c² - a²)/(2bc))
         * β = arccos((a² + c² - b²)/(2ac))
         * γ = arccos((a² + b² - c²)/(2ab))
         */
        public static double[] lawOfCosSSS(double sideA, double sideB, double sideC) {
            final double aSquared = sideA * sideA;
            final double bSquared = sideB * sideB;
            final double cSquared = sideC * sideC;
            final double angleAlphaRad = Math.acos((bSquared + cSquared - (-sideA * -sideA)) / (2 * sideB * sideC));
            final double angleBetaRad = Math.acos((aSquared + cSquared - (-sideB * -sideB)) / (2 * sideA * sideC));
            final double angleGammaRad = Math.acos((aSquared + bSquared - (-sideC * -sideC)) / (2 * sideA * sideB));
            return new double[]{angleAlphaRad, angleBetaRad, angleGammaRad};
        }

        /**
         * a / sin(α) = b / sin(β) = c / sin(γ)
         *
         * @return b = a / sin(α) * sin(β)
         */
        public static double lawOfSinGivenSideAAndAnglesAlphaBeta(
            double side, double angleAlphaRadians, double angleBetaRadians) {
            return side / Math.sin(angleAlphaRadians) * Math.sin(angleBetaRadians);
        }

        /**
         * @return a = b * sin(α) / sin(β)
         */
        public static double lawOfSinGivenSideBAndAnglesAlphaBeta(
            double side, double angleAlphaRadians, double angleBetaRadians) {
            return side * Math.sin(angleAlphaRadians) / Math.sin(angleBetaRadians);
        }

        /**
         * @return β = arcsin(b * sin(α) / a)
         */
        public static double lawOfSinGivenSidesABAndAngleAlpha(double sideA, double sideB, double angleAlphaRadians) {
            return Math.asin(sideB * Math.sin(angleAlphaRadians) / sideA);
        }

        /**
         * @return α = arcsin(a * sin(β) / b)
         */
        public static double lawOfSinGivenSidesABAndAngleBeta(double sideA, double sideB, double angleBetaRadians) {
            return Math.asin(sideA * Math.sin(angleBetaRadians) / sideB);
        }

        /**
         * @return γ = arcsin(b * sin(β) / c)
         */
        public static double lawOfSinGivenSidesBCAndAngleBeta(double sideB, double sideC, double angleBetaRadians) {
            return Math.asin(sideB * Math.sin(angleBetaRadians) / sideC);
        }

        /**
         * @return γ = arcsin(c * sin(β) / a)
         */
        public static double lawOfSinGivenSidesACAndAngleAlpha(double sideA, double sideC, double angleAlphaRadians) {
            return Math.asin(sideC * Math.sin(angleAlphaRadians) / sideA);
        }

        /**
         * @return sin(α+β) = sin(α)cos(β) + cos(α)sin(β)
         */
        public static double sinAngleSum(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) + Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return sin(α-β) = sin(α)cos(β) - cos(α)sin(β)
         */
        public static double sinAngleDifference(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) - Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return cos(α+β) = cos(α)cos(β) − sin(α)sin(β)
         */
        public static double cosAngleSum(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) - Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return cos(α-β) = cos(α)cos(β) + sin(α)sin(β)
         */
        public static double cosAngleDifference(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) + Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return tan(α+β) = (tan(α)+tan(β)) / (1−tan(α)tan(β))
         */
        public static double tanAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = 1 - Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.tan(angleAlpha) + Math.tan(angleBeta);
            return numerator / denominator;
        }

        /**
         * @return tan(α-β) = (tan(α)-tan(β)) / (1+tan(α)tan(β))
         */
        public static double tanAngleDifference(double angleAlpha, double angleBeta) {
            final double denominator = 1 + Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.tan(angleAlpha) - Math.tan(angleBeta);
            return numerator / denominator;
        }

        /**
         * @return cot(α+β) = (cot(α)cot(β)−1) / (cot(β)+cot(α))
         */
        public static double cotAngleSum(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotAlpha + cotBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (cotAlpha * cotBeta - 1) / denominator;
        }

        /**
         * @return cot(α-β) = (cot(α)cot(β)+1) / (cot(β)-cot(α))
         */
        public static double cotAngleDifference(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotBeta - cotAlpha;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (cotAlpha * cotBeta + 1) / denominator;
        }

        /**
         * @return sec(α+β) = (sec(α)sec(β)csc(α)csc(β)) / (csc(α)csc(β)−sec(α)sec(β))
         */
        public static double secAngleSum(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = cscAlpha * cscBeta - secAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return secAlpha * secBeta * cscAlpha * cscBeta / denominator;
        }

        /**
         * @return sec(α-β) = (sec(α)sec(β)csc(α)csc(β)) / (csc(α)csc(β)+sec(α)sec(β))
         */
        public static double secAngleDifference(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = cscAlpha * cscBeta + secAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return secAlpha * secBeta * cscAlpha * cscBeta / denominator;
        }

        /**
         * @return csc(α+β) = (sec(α)sec(β)csc(α)csc(β)) / (sec(α)csc(β)+csc(α)sec(β))
         */
        public static double cscAngleSum(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = secAlpha * cscBeta + cscAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return secAlpha * secBeta * cscAlpha * cscBeta / denominator;
        }

        /**
         * @return csc(α-β) = (sec(α)sec(β)csc(α)csc(β)) / (sec(α)csc(β)-csc(α)sec(β))
         */
        public static double cscAngleDifference(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = secAlpha * cscBeta - cscAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return secAlpha * secBeta * cscAlpha * cscBeta / denominator;
        }

        /**
         * sin(α) = y/1 = y
         * sin(α) = opposite/hypotenuse = a/c
         * Cofunction: cos(x).
         * <p/>The sine function:
         * <br/>- an odd: sin(−α) = −sin(α).
         * <br/>- period: 2π
         * <br/>- Range: −1 ≤ sin(α) ≤ 1
         */
        public static double sin(double angleRadians) {
            return Math.sin(angleRadians);
        }

        public static double sinInverse(double angleRadians) {
            return Math.asin(angleRadians);
        }

        /**
         * @return sin(α) = opposite / hypotenuse = a/c
         */
        public static double sin(double opposite, double hypotenuse) {
            return opposite / hypotenuse;
        }

        /**
         * y(t) = A * sin(2πft + φ)
         *
         * @return y(t) = A * sin(ωt+φ)
         */
        public static double sinusoid(double amplitude, double anglePhiRadians,
                                      double oscillationFrequency, long timeSeconds) {
            final double angularFrequency = 2 * Math.PI * oscillationFrequency;
            return amplitude * Math.sin(angularFrequency * timeSeconds + anglePhiRadians);
        }

        public static int quadrant(double angleRadians) {
            if (0 <= angleRadians && angleRadians <= Math.PI / 2) {
                // 0<α≤π/2
                return 1;
            } else if (Math.PI / 2 < angleRadians && angleRadians <= Math.PI) {
                // π/2<α≤π
                return 2;
            } else if (Math.PI < angleRadians && angleRadians <= 3 * Math.PI / 2) {
                // π<α≤3π/2
                return 3;
            } else {
                // 3π/2<α≤2π
                return 4;
            }
        }

        /**
         * cos(α) = x/1 = x
         * cos(α) = adjacent / hypotenuse = b / c
         * Cofunction: sin(x).
         * <p/>The cosine function:
         * <br/>- an even: cos(−α) = cos(α).
         * <br/>- period: 2π
         * <br/>- Range: −1 ≤ cos(α) ≤ 1
         */
        public static double cos(double angleRadians) {
            return Math.cos(angleRadians);
        }

        public static double cosInverse(double angleRadians) {
            return Math.acos(angleRadians);
        }

        /**
         * @return cos(α) = adjacent / hypotenuse = b / c
         */
        public static double cos(double adjacent, double hypotenuse) {
            return adjacent / hypotenuse;
        }

        /**
         * tan(α) = y/x = sin(α)/cos(α)
         * tan(α) = opposite / adjacent = a / b
         * Cofunction: cot(x).
         * <p/>The tangent function:
         * <br/>- an even: tan(−x) = -tan(x).
         * <br/>- Period: 2π.
         * <br/>- Range: -∞<y<∞
         */
        public static double tan(double angleRadians) {
            return Math.tan(angleRadians);
        }

        /**
         * @return tan⁻¹(x)
         */
        public static double tanInverse(double angle) {
            return Math.atan(angle);
        }

        /**
         * @return tan⁻¹(y/x)
         */
        public static double multivaluedTanInverse(double y, double x) {
            return Math.atan2(y, x);
        }

        /**
         * @return (a - b) / (a + b) = tan(0.5(α - β)) / tan(0.5(α + β))
         */
        public static double lawOfTangents(double angleAlphaRadians, double angleBetaRadians) {
            return tan(0.5 * (angleAlphaRadians - angleBetaRadians))
                / tan(0.5 * (angleAlphaRadians + angleBetaRadians));
        }

        /**
         * A * sin(B(x−C/B)) + D
         *
         * @return f(x) = A * sin(Bx−C) + D
         */
        public static double[] sinPhaseShift(double x, double amplitude, double period,
                                             double phase, double verticalShift) {
            final double phaseShift = phase / period;
            final double periodShift = 2 * Math.PI / period;
            return new double[]{phaseShift, periodShift, amplitude * Math.sin(period * x - phase) + verticalShift};
        }

        /**
         * @return f(x) = A * cos(Bx−C) + D
         */
        public static double[] cosPhaseShift(double x, double amplitude, double period,
                                             double phase, double verticalShift) {
            final double phaseShift = phase / period;
            final double periodShift = 2 * Math.PI / period;
            return new double[]{phaseShift, periodShift, amplitude * Math.cos(period * x - phase) + verticalShift};
        }

        /**
         * @return sin²(x) = (1−cos(2x)) / 2
         */
        public static double sinPowerReducing(double angleRadians) {
            return (1 - Math.cos(2 * angleRadians)) / 2;
        }

        /**
         * @return cos²(x) = (1+cos(2x)) / 2
         */
        public static double cosPowerReducing(double angleRadians) {
            return (1 + Math.cos(2 * angleRadians)) / 2;
        }

        /**
         * @return tan²(x) = (1-cos(2x)) / (1+cos(2x))
         */
        public static double tanPowerReducing(double angleRadians) {
            final double cosine = Math.cos(2 * angleRadians);
            return (1 - cosine) / (1 + cosine);
        }

        /**
         * @return sin(α) = tan(α) * cos(α)
         */
        public static double findSinWithCosAndTan(double angleAlphaRadians) {
            return Math.tan(angleAlphaRadians) * Math.cos(angleAlphaRadians);
        }
    }

    public static final class Seq {
        private Seq() {
        }

        /**
         * @return aₙ = a₁rⁿ⁻¹, n∈N
         */
        public static double[] geometricSequence(double firstTerm, double commonRatio, int limit) {
            final double[] sequence = new double[limit];
            for (int i = 1; i <= limit; i++) {
                final int index = i - 1;
                sequence[index] = firstTerm * Math.pow(commonRatio, index);
            }
            return sequence;
        }

        /**
         * @return aⱼ + … + aₖ
         */
        public static double geometricSequenceFiniteSum(double[] sequence, int startIndex, int endIndex) {
            double sum = 0;
            for (int i = startIndex; i <= endIndex; i++) {
                sum += sequence[i];
            }
            return sum;
        }

        /**
         * aₙ = aₘ * rⁿ⁻ᵐ
         * Alternative: r = ⁽ⁿ⁻ᵐ⁾√(aₙ / aₘ)
         *
         * @return r = (aₙ / aₘ)¹/⁽ⁿ⁻ᵐ⁾
         */
        public static double geometricSequenceCommonRatioForNonConsecutiveTerms(
            double mTermPosition, double mTerm, double nTermPosition, double nTerm) {
            return Math.pow(nTerm / mTerm, 1. / (nTermPosition - mTermPosition));
        }

        /**
         * If |r| > 1, then the series diverges.
         * If |r| < 1, then the series converges.
         * If |r| = 1, then the series is periodic, but its sum diverges.
         *
         * @return r = aₙ / aₙ₋₁
         */
        public static double geometricSequenceCommonRatio(double previousTerm, double nTerm) {
            return nTerm / previousTerm;
        }

        public static double[] arithmeticSequence(double firstTerm, double commonDifference, int limit) {
            final double[] sequence = new double[limit];
            for (int i = 1; i <= limit; i++) {
                sequence[i - 1] = arithmeticSequenceNthTerm(firstTerm, commonDifference, i);
            }
            return sequence;
        }

        /**
         * @return aₙ = a₁ + (n-1)d
         */
        public static double arithmeticSequenceNthTerm(double firstTerm, double commonDifference, int nthTermPosition) {
            return firstTerm + (nthTermPosition - 1) * commonDifference;
        }

        /**
         * @return S = n/2 * (2a₁ + (n−1)d)
         */
        public static double arithmeticSequenceSum(double firstTerm, double commonDiff, int nthTermPosition) {
            return nthTermPosition / 2. * (2 * firstTerm + (nthTermPosition - 1) * commonDiff);
        }

        /**
         * @return aⱼ + ... + aₖ
         */
        public static double arithmeticSequenceSum(
            double firstTerm, double commonDiff, int firstTermPosition, int nthTermPosition) {
            final double firstTermSum = arithmeticSequenceSum(firstTerm, commonDiff, firstTermPosition - 1);
            final double nthTermSum = arithmeticSequenceSum(firstTerm, commonDiff, nthTermPosition);
            return nthTermSum - firstTermSum;
        }

        /**
         * a = {aₙ}ₙ₌₀^∞
         * b = {bₙ}ₙ₌₀^∞
         * c = {cₙ}ₙ₌₀^∞
         *
         * @return cₙ = ∑ₖ₌₀ⁿ aₖ * bₙ₋ₖ
         */
        public static double[] convolution(double[] sequence1, double[] sequence2) {
            Objects.requireNonNull(sequence1);
            Objects.requireNonNull(sequence2);

            final int n = sequence1.length;
            final int m = sequence2.length;
            final int size = n + m - 1;
            final double[] convolvedSequence = new double[size];
            for (int i = 0; i < size; i++) {
                convolvedSequence[i] = 0;
                for (int k = 0; k < n; k++) {
                    final int j = i - k;
                    if (j >= 0 && j < m) {
                        convolvedSequence[i] += sequence1[k] * sequence2[j];
                    }
                }
            }
            return convolvedSequence;
        }

        /**
         * @param rows the first row index 0 is not included. For the size of rows = 10 -> rows = 10 + 1 = 11.
         * @return C(n, k) = C(n-1, k-1) + C(n-1, k)
         */
        public static int[][] pascalTriangle(int rows) {
            checkGreater0(rows);

            final int totalRows = rows + 1;
            final int[][] matrix = new int[totalRows][];
            for (int i = 0; i < totalRows; i++) {
                matrix[i] = new int[i + 1];
                matrix[i][0] = 1;
                matrix[i][i] = 1;
                for (int j = 1; j < i; j++) {
                    matrix[i][j] = matrix[i - 1][j - 1] + matrix[i - 1][j];
                }
            }
            return matrix;
        }

        /**
         * @return 2ⁿ
         */
        public static long pascalTriangleRowSum(int rowNumber) {
            return (long) Math.pow(2, rowNumber);
        }

        /**
         * @return Hₙ = 1/1 + 1/2 + 1/3 + ⋯ + 1/n = ∑ₖ₌₁ⁿ 1/k
         */
        public static double harmonicNumber(double end) {
            checkGreater0(end);

            double sum = 0;
            for (int k = 1; k <= end; k++) {
                sum += 1. / k;
            }
            return sum;
        }

        /**
         * F₀ = 0, F₁ = 1
         *
         * @return Fₙ = Fₙ₋₁ + Fₙ₋₂
         */
        public static long fibonacci(long numberIdx) {
            long first = 0;
            long second = 1;

            for (int i = 1; i < Math.abs(numberIdx); i++) {
                final long next = first + second;
                first = second;
                second = next;
            }
            return second;
        }

        public static long[] fibonacciNumbers(long numberIdx) {
            final long num = Math.abs(numberIdx) + 1;
            final long[] sequence = new long[Math.toIntExact(num)];
            long first = 0;
            long second = 1;
            sequence[Constants.ARR_1ST_INDEX] = first;
            sequence[Constants.ARR_2ND_INDEX] = second;

            for (int i = 2; i < num; i++) {
                final long next = first + second;
                sequence[i] = next;
                first = second;
                second = next;
            }
            return sequence;
        }

        public static long fibonacciViaRecursion(long numberIdx) {
            if (numberIdx <= 1) {
                return numberIdx;
            }
            return fibonacciViaRecursion(numberIdx - 1) + fibonacciViaRecursion(numberIdx - 2);
        }

        /**
         * Where
         * φ — Golden ratio (equal to (1 + √5)/2, or ≈1.618);
         * ψ = 1 − φ = (1 − √5)/2.
         *
         * @return Fₙ = (φⁿ − ψⁿ) / √5
         */
        public static long fibonacciViaGoldenRatio(long numberIdx) {
            return (long) ((Math.pow(GOLDEN_RATIO, numberIdx) - (1 - GOLDEN_RATIO)) / squareRoot(5));
        }

        /**
         * a = (F₁ − F₀ψ) / √5
         * b = (φF₀ − F₁) / √5
         *
         * @return Fₙ = aφⁿ + bψⁿ
         */
        public static long fibonacciViaGoldenRatio(long firstTerm, long secondTerm, long numberIdx) {
            final double psi = 1 - GOLDEN_RATIO;
            final double sq5 = squareRoot(5);
            final double a = (secondTerm - firstTerm * psi) / sq5;
            final double b = (GOLDEN_RATIO * firstTerm - secondTerm) / sq5;
            return Math.round(a * Math.pow(GOLDEN_RATIO, numberIdx) + b * Math.pow(psi, numberIdx));
        }

        /**
         * @return F₋ₙ = Fₙ × (-1)ⁿ⁺¹
         */
        public static long fibonacciForNegativeTerm(long numberIdx) {
            return Math.round(fibonacci(numberIdx) * Math.pow(-1, numberIdx + 1.));
        }
    }

    public static final class LinearAlgebra {
        private LinearAlgebra() {
        }

        /**
         * @return |v| = √(x² + y² + z²)
         */
        public static double vectorMagnitude(double[] vector) {
            return squareRoot(Arrays.stream(vector).map(m -> m * m).sum());
        }

        /**
         * û = u / |u|
         * where:
         * û — Unit vector;
         * u — Arbitrary vector in the form (x, y, z);
         * |u| — Magnitude of the vector u.
         */
        public static Triple<Double, double[], Double> unitVector(double[] vector) {
            final double magnitude = vectorMagnitude(vector);
            final double[] result = Arrays.stream(vector).map(m -> m / magnitude).toArray();
            final double resultMagnitude = vectorMagnitude(result);
            return Triple.of(magnitude, result, resultMagnitude);
        }

        public static Pair<double[], Double> vectorProjection(double[] vectorA, double[] vectorB) {
            checkSameDimensions(vectorA, vectorB);

            final double dotProduct = dotProduct(vectorA, vectorB);
            final double squaredNormOfB = dotProduct(vectorB, vectorB);
            if (squaredNormOfB == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }

            final double projectionFactor = dotProduct / squaredNormOfB;

            final double[] result = new double[vectorA.length];
            for (int i = 0; i < vectorA.length; i++) {
                result[i] = projectionFactor * vectorB[i];
            }
            return Pair.of(result, projectionFactor);
        }

        /**
         * Find one of the missing components.
         * For example, find z |v| = √(x² + y² + ?)
         */
        public static Pair<double[], Double> findMissingUnitVectorComponent(double[] unitVector) {
            final double sum = Arrays.stream(unitVector).map(m -> {
                if (m > 1) {
                    throw new IllegalArgumentException("Unit vector components must be less than or equal to 1");
                }
                return m;
            }).sum();
            final double[] result = Arrays.copyOf(unitVector, unitVector.length + 1);
            result[result.length - 1] = sum;
            final double resultMagnitude = vectorMagnitude(result);
            return Pair.of(result, resultMagnitude);
        }

        /**
         * @return v × w = (v₂w₃ - v₃w₂, v₃w₁ - v₁w₃, v₁w₂ - v₂w₁)
         */
        public static double[] crossProduct(double[] vectorA, double[] vectorB) {
            if (vectorA.length != 3 || vectorB.length != 3) {
                throw new IllegalArgumentException("The cross product can only be applied to 3D vectors");
            }

            final var a = vectorA;
            final var b = vectorB;
            final int i1 = Constants.ARR_1ST_INDEX;
            final int i2 = Constants.ARR_2ND_INDEX;
            final int i3 = Constants.ARR_3RD_INDEX;
            return new double[]{
                a[i2] * b[i3] - a[i3] * b[i2],
                a[i3] * b[i1] - a[i1] * b[i3],
                a[i1] * b[i2] - a[i2] * b[i1],
            };
        }

        /**
         * @return a⋅b = a₁b₁ + a₂b₂ + a₃b₃
         */
        public static double dotProduct(double[] vectorA, double[] vectorB) {
            checkSameDimensions(vectorA, vectorB);

            double result = 0;
            for (int i = 0; i < vectorA.length; i++) {
                result += vectorA[i] * vectorB[i];
            }
            return result;
        }

        /**
         * @return c_ij=a_i1 * b_1j + a_i2 * b_2j +...+ a_in * b_nj = ∑_k a_ik * b_kj
         */
        public static double dotProduct(double[][] matrixA, double[][] matrixB) {
            checkSameDimensions(matrixA, matrixB);

            double result = 0;
            for (int i = 0; i < matrixA.length; i++) {
                for (int j = 0; j < matrixA[i].length; j++) {
                    result += matrixA[i][j] * matrixB[i][j];
                }
            }
            return result;
        }

        public static double[] dotProductAndAngleBetween(double[] vectorA, double[] vectorB) {
            return dotProductAndAngleBetween(vectorA, vectorB, 4, RoundingMode.HALF_UP);
        }

        /**
         * a⋅b = |a| × |b| × cos α
         * cos α = a⋅b / (|a| × |b|)
         */
        public static double[] dotProductAndAngleBetween(
            double[] vectorA, double[] vectorB, int scale, RoundingMode roundingMode) {
            final var dot = BigDecimal.valueOf(dotProduct(vectorA, vectorB))
                .setScale(scale, roundingMode);
            final var magnitudeA = BigDecimal.valueOf(vectorMagnitude(vectorA))
                .setScale(scale, roundingMode);
            final var magnitudeB = BigDecimal.valueOf(vectorMagnitude(vectorB))
                .setScale(scale, roundingMode);

            final double angleRadians = dot.divide(magnitudeA.multiply(magnitudeB), roundingMode).doubleValue();
            return new double[]{
                dot.doubleValue(),
                magnitudeA.doubleValue(),
                magnitudeB.doubleValue(),
                Math.acos(angleRadians)
            };
        }

        /**
         * ∣A∣ = ∑(−1)ˢᵍⁿ⁽σ⁾ ∏aᵢ,σ₍ᵢ₎
         * <br/>where:
         * <ul>
         *     <li>∑ is the sum of all permutations of the set {1,2…,n}</li>
         *     <li>∏ is the product of i-s from 1 to n.</li>
         * </ul>
         * <p/>For 2x2 matrix:
         * A=|a₁ a₂|
         *   |b₁ b₂|
         * ∣A∣ = a₁⋅b₂−a₂⋅b₁
         * <br/>Alternative:
         * A=|a b|
         *   |c d|
         * ∣A∣ = ad−bc
         * <p/>For 3x3 matrix:
         *   |a₁ b₁ c₁|
         * A=|a₂ b₂ c₂|
         *   |a₃ b₃ c₃|
         * ∣A∣ = a₁⋅b₂⋅c₃ + a₂⋅b₃⋅c₁ + a₃⋅b₁⋅c₂ − a₃⋅b₂⋅c₁ − a₁⋅b₃⋅c₂ − a₂⋅b₁⋅c₃
         * <br/>Alternatives:
         *   |a b c|
         * A=|d e f|
         *   |g h i|
         * <br/>|A| = a(ei − fh) − b(di − fg) + c(dh − eg) = aei + bfg + cdh − ceg − bdi - afh
         * |A| = a * |e f| - b * |d f| + c * |d e|
         *           |h i|       |g i|       |g h|
         * <p/>For 4x4 matrix:
         *   |a₁ b₁ c₁ d₁|
         * A=|a₂ b₂ c₂ d₂|
         *   |a₃ b₃ c₃ d₃|
         *   |a₄ b₄ c₄ d₄|
         * ∣A∣ = a₁⋅b₂⋅c₃⋅d₄ − a₂⋅b₁⋅c₃⋅d₄ + a₃⋅b₁⋅c₂⋅d₄
         *      −a₁⋅b₃⋅c₂⋅d₄ + a₂⋅b₃⋅c₁⋅d₄ − a₃⋅b₂⋅c₁⋅d₄
         *      +a₃⋅b₂⋅c₄⋅d₁ − a₂⋅b₃⋅c₄⋅d₁ + a₄⋅b₃⋅c₂⋅d₁
         *      −a₃⋅b₄⋅c₂⋅d₁ + a₂⋅b₄⋅c₃⋅d₁ − a₄⋅b₂⋅c₃⋅d₁
         *      +a₄⋅b₁⋅c₃⋅d₂ − a₁⋅b₄⋅c₃⋅d₂ + a₃⋅b₄⋅c₁⋅d₂
         *      −a₄⋅b₃⋅c₁⋅d₂ + a₁⋅b₃⋅c₄⋅d₂ − a₃⋅b₁⋅c₄⋅d₂
         *      +a₂⋅b₁⋅c₄⋅d₃ − a₁⋅b₂⋅c₄⋅d₃ + a₄⋅b₂⋅c₁⋅d₃
         *      −a₂⋅b₄⋅c₁⋅d₃ + a₁⋅b₄⋅c₂⋅d₃ − a₄⋅b₁⋅c₂⋅d₃
         * <br/>Alternatives:
         *   |a b c d|
         * A=|e f g h|
         *   |i j k l|
         *   |m n o p|
         *           |f g h|       |e g h|       |e f h|       |e f g|
         * |A| = a * |j k l| - b * |i k l| + c * |i j l| - d * |i j k|
         *           |n o p|       |m o p|       |m n p|       |m n o|
         *
         * @return det(A) = ∣A∣
         */
        public static double determinant(double[][] matrix) {
            Objects.requireNonNull(matrix);
            final int n = matrix.length;
            if (n == 0) {
                return 1; // det([]) = 1
            }

            final byte row1 = Constants.ARR_1ST_INDEX;
            final byte col1 = Constants.ARR_1ST_INDEX;
            if (n == 1) {
                return matrix[row1][col1];
            }

            final byte row2 = Constants.ARR_2ND_INDEX;
            final byte col2 = Constants.ARR_2ND_INDEX;
            if (n == 2) {
                return matrix[row1][col1] * matrix[row2][col2] - matrix[row1][col2] * matrix[row2][col1];
            }

            final byte row3 = Constants.ARR_3RD_INDEX;
            final byte col3 = Constants.ARR_3RD_INDEX;

            final double a1 = matrix[row1][col1];
            final double a2 = matrix[row2][col1];
            final double a3 = matrix[row3][col1];
            final double b1 = matrix[row1][col2];
            final double b2 = matrix[row2][col2];
            final double b3 = matrix[row3][col2];
            final double c1 = matrix[row1][col3];
            final double c2 = matrix[row2][col3];
            final double c3 = matrix[row3][col3];
            if (n == 3) {
                return a1 * b2 * c3 + a2 * b3 * c1 + a3 * b1 * c2 - a3 * b2 * c1 - a1 * b3 * c2 - a2 * b1 * c3;
            }

            final byte row4 = Constants.ARR_4TH_INDEX;
            final byte col4 = Constants.ARR_4TH_INDEX;

            final double a4 = matrix[row4][col1];
            final double b4 = matrix[row4][col2];
            final double c4 = matrix[row4][col3];
            final double d1 = matrix[row1][col4];
            final double d2 = matrix[row2][col4];
            final double d3 = matrix[row3][col4];
            final double d4 = matrix[row4][col4];
            if (n == 4) {
                return a1 * b2 * c3 * d4 - a2 * b1 * c3 * d4 + a3 * b1 * c2 * d4
                    - a1 * b3 * c2 * d4 + a2 * b3 * c1 * d4 - a3 * b2 * c1 * d4
                    + a3 * b2 * c4 * d1 - a2 * b3 * c4 * d1 + a4 * b3 * c2 * d1
                    - a3 * b4 * c2 * d1 + a2 * b4 * c3 * d1 - a4 * b2 * c3 * d1
                    + a4 * b1 * c3 * d2 - a1 * b4 * c3 * d2 + a3 * b4 * c1 * d2
                    - a4 * b3 * c1 * d2 + a1 * b3 * c4 * d2 - a3 * b1 * c4 * d2
                    + a2 * b1 * c4 * d3 - a1 * b2 * c4 * d3 + a4 * b2 * c1 * d3
                    - a2 * b4 * c1 * d3 + a1 * b4 * c2 * d3 - a4 * b1 * c2 * d3;
            }

            final int size = n - 1;
            double det = 0;
            for (int column = 0; column < n; column++) {
                final double[][] subMatrix = new double[size][size]; //NOPMD
                for (int i = 1; i < n; i++) {
                    int subColumn = 0;
                    for (int j = 0; j < n; j++) {
                        if (j == column) {
                            continue;
                        }
                        subMatrix[i - 1][subColumn++] = matrix[i][j];
                    }
                }
                // The cofactor expansion (Laplace expansion)
                // Cᵢⱼ = (-1)ᶦ⁺ʲ × det(Aᵢⱼ)
                det += Math.pow(-1, column) * matrix[row1][column] * determinant(subMatrix);
            }
            return det;
        }

        /**
         * (a,b) + (d,e) = (a + d, b + e)
         * (a,b,c) + (d,e,f) = (a + d, b + e, c + f)
         * x + y = (x₁ + y₁, x₂ + y₂, ..., xₖ + yₖ)
         *
         * @return perform the addition coordinate-wise. α × v + β × w
         */
        public static double[] vectorAddWithMultiples(double[] vectorA, double alphaCopies,
                                                      double[] vectorB, double betaCopies) {
            Objects.requireNonNull(vectorA);
            Objects.requireNonNull(vectorB);
            checkSameDimensions(vectorA, vectorB);

            final double[] result = new double[vectorA.length];
            for (int i = 0; i < vectorA.length; i++) {
                result[i] = alphaCopies * vectorA[i] + betaCopies * vectorB[i];
            }
            return result;
        }

        /**
         * x + y = (x₁ + y₁, x₂ + y₂)
         *
         * @return θ = arctan((x₂ + y₂)/(x₁ + y₁))
         */
        public static double angleInVector2dAddition(double[] vectorA, double[] vectorB) {
            check2dSize(vectorA);
            Objects.requireNonNull(vectorB);
            checkSameDimensions(vectorA, vectorB);

            final double x1 = vectorA[Constants.ARR_1ST_INDEX];
            final double x2 = vectorA[Constants.ARR_2ND_INDEX];
            final double y1 = vectorA[Constants.ARR_1ST_INDEX];
            final double y2 = vectorA[Constants.ARR_2ND_INDEX];
            final double quotient = (x2 + y2) / (x1 + y1);
            return Trigonometry.tanInverse(quotient);
        }

        public static double[] vectorSubtractWithMultiples(double[] vectorA, double alphaCopies,
                                                           double[] vectorB, double betaCopies) {
            Objects.requireNonNull(vectorA);
            Objects.requireNonNull(vectorB);
            checkSameDimensions(vectorA, vectorB);

            final double[] result = new double[vectorA.length];
            for (int i = 0; i < vectorA.length; i++) {
                result[i] = alphaCopies * vectorA[i] - betaCopies * vectorB[i];
            }
            return result;
        }

        /**
         * A×v = λ×v
         * (A−λI)v = 0
         * p(λ) = x² − tr(A)·λ + det(A)
         * ½ tr(A) ± ½√(tr(A)² − 4·det(A))
         */
        public static Pair<double[], double[][]> eigenvaluesEigenvectorsOf2x2(double[][] matrix) {
            final byte row1 = Constants.ARR_1ST_INDEX;
            final byte col1 = Constants.ARR_1ST_INDEX;
            final byte row2 = Constants.ARR_2ND_INDEX;
            final byte col2 = Constants.ARR_2ND_INDEX;

            final double a = matrix[row1][col1];
            final double b = matrix[row1][col2];
            final double c = matrix[row2][col1];
            final double d = matrix[row2][col2];
            // only one eigenvalue
            // A=|1 k|
            //   |0 1|
            // or
            // A=|k 0|
            //   |0 k|
            final double trace = a + d;
            final double det = determinant(matrix);
            final double sqrt = squareRoot(trace * trace - 4 * det);
            final double lambda1 = (trace + sqrt) / 2;
            final double lambda2 = (trace - sqrt) / 2;

            // (q−λI)v = 0
            final double[] vector1;
            final double[] vector2;
            if (b != 0) {
                vector1 = new double[]{1, (lambda1 - a) / b};
                vector2 = new double[]{1, (lambda2 - a) / b};
            } else if (c != 0) {
                vector1 = new double[]{(lambda1 - d) / c, 1};
                vector2 = new double[]{(lambda2 - d) / c, 1};
            } else {
                vector1 = new double[]{1, 0};
                vector2 = new double[]{0, 1};
            }

            final double[] eigenvalues = {lambda1, lambda2};
            final double[][] eigenvectors = {vector1, vector2};
            return Pair.of(eigenvalues, eigenvectors);
        }

        public static Pair<double[], double[][]> eigenvaluesEigenvectors(double[][] matrix) {
            final int n = matrix.length;
            final double[][] matrixCopyA = new double[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(matrix[i], 0, matrixCopyA[i], 0, n);
            }

            // QR algorithm for eigenvalues
            double[][] multipliedMatrix = new double[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(matrixCopyA[i], 0, multipliedMatrix[i], 0, n);
            }

            int maxIter = 100;
            for (int iter = 0; iter < maxIter; iter++) {
                // QR decomposition
                final var qr = qrDecomposition(multipliedMatrix);
                final double[][] matrixQ = qr.getLeft();
                final double[][] matrixR = qr.getRight();

                multipliedMatrix = matrixMultiply(matrixR, matrixQ);
            }
            final double[] eigenvalues = new double[n];
            for (int i = 0; i < n; i++) {
                eigenvalues[i] = multipliedMatrix[i][i];
            }

            // Inverse iteration for eigenvectors
            final double[][] eigenvectors = new double[n][n];
            for (int k = 0; k < n; k++) {
                final double lambda = eigenvalues[k];
                final double[][] matrixCopyB = new double[n][n]; //NOPMD
                for (int i = 0; i < n; i++) {
                    System.arraycopy(matrix[i], 0, matrixCopyB[i], 0, n);
                    matrixCopyB[i][i] -= lambda;
                }
                double[] eigenvector = new double[n]; //NOPMD
                eigenvector[n - 1] = 1.0; // initial guess
                boolean normalized = false;
                for (int iter = 0; iter < 20; iter++) {
                    eigenvector = solveLinearSystem(matrixCopyB, eigenvector);
                    double norm = 0.0;
                    for (double vi : eigenvector) {
                        norm += vi * vi;
                    }
                    norm = squareRoot(norm);
                    if (norm <= 1e-12 || Double.isNaN(norm) || Double.isInfinite(norm)) {
                        for (int i = 0; i < n; i++) {
                            eigenvector[i] /= norm;
                        }
                        normalized = true;
                    }
                }
                if (!normalized) {
                    // Fallback: use initial guess or a standard basis vector
                    eigenvector = new double[n]; //NOPMD
                    eigenvector[n - 1] = 1.0;
                }
                eigenvectors[k] = eigenvector;
            }

            final int eigenSize = eigenvalues.length;
            final double[][] sortedEigenvectors = new double[eigenSize][eigenSize];
            final double[] sortedEigenvalues = new double[eigenSize];

            final var indices = new Integer[eigenSize];
            for (int i = 0; i < eigenSize; i++) {
                indices[i] = i;
            }

            // Sort indices by eigenvalue descending
            final Comparator<Integer> comparator = Comparator.comparingDouble(i -> eigenvalues[i]);
            Arrays.sort(indices, comparator);

            for (int i = 0; i < eigenSize; i++) {
                sortedEigenvalues[i] = eigenvalues[indices[i]];
                sortedEigenvectors[i] = eigenvectors[indices[i]];
            }

            return Pair.of(sortedEigenvalues, sortedEigenvectors);
        }

        /**
         * Av = λv
         * <ul>
         *     <li>A: An n * n square matrix.</li>
         *     <li>v: A non-zero vector (the eigenvector).</li>
         *     <li>λ: A scalar (the eigenvalue).</li>
         *     <li>Av: The result of multiplying matrix A by vector v.</li>
         *     <li>λv: The result of scaling vector v by the scalar λ.</li>
         * </ul>
         */
        public static double[] scaleEigenvector(double eigenvalue, double[] eigenvector) {
            final double[] eigenProd = new double[eigenvector.length];
            for (int i = 0; i < eigenvector.length; i++) {
                eigenProd[i] = eigenvalue * eigenvector[i];
            }
            return eigenProd;
        }

        public static double[][] identityMatrix(int size) {
            final double[][] matrix = new double[size][size];
            for (int i = 0; i < size; i++) {
                matrix[i][i] = 1.0;
            }
            return matrix;
        }

        /**
         * @return Aᵏ
         */
        public static double[][] matrixPower(double[][] matrix, int exponent) {
            final int n = matrix.length;
            double[][] result = identityMatrix(n);

            if (exponent == 0) {
                return result;
            }
            for (int k = 0; k < exponent; k++) {
                result = matrixMultiply(result, matrix);
            }
            return result;
        }

        /**
         * @return A = S⋅D⋅S⁻¹
         */
        public static double[][] matrixPowerViaEigenDecomposition(double[][] matrix, int exponent) {
            // 1. Get eigenvalues and eigenvectors
            final var pair = eigenvaluesEigenvectors(matrix);
            final double[] eigenvalues = pair.getLeft();
            final double[][] matrixS = new double[matrix.length][matrix.length];

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    matrixS[j][i] = pair.getRight()[i][j]; // columns are eigenvectors
                }
            }

            final double[][] diagonalMatrix = diagonalizeMatrix(matrix, eigenvalues);
            // 2. Build Dᵏ (diagonal matrix of eigenvalues to the k-th power)
            final double[][] diagonalMatrixPowered = matrixPower(diagonalMatrix, exponent);

            // 3. Compute S⁻¹
            final double[][] matrixSInverse = matrixInverse(matrixS);

            // 4. Compute S * Dᵏ * S⁻¹
            final double[][] matrixSDPowered = matrixMultiply(matrixS, diagonalMatrixPowered);
            return matrixMultiply(matrixSDPowered, matrixSInverse);
        }

        /**
         * QR Decomposition (Gram-Schmidt)
         */
        public static Pair<double[][], double[][]> qrDecomposition(double[][] matrix) {
            final int n = matrix.length;
            final int m = matrix[Constants.ARR_1ST_INDEX].length;
            final double[][] orthogonalMatrix = new double[n][m]; // aka Q
            final double[][] upperTriangularMatrix = new double[m][m]; // aka R
            final double[][] matrixV = new double[n][m];
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    matrixV[i][j] = matrix[i][j];
                }
                for (int k = 0; k < j; k++) {
                    double prodSum = 0;
                    for (int i = 0; i < n; i++) {
                        prodSum += orthogonalMatrix[i][k] * matrixV[i][j];
                    }
                    upperTriangularMatrix[k][j] = prodSum;
                    for (int i = 0; i < n; i++) {
                        matrixV[i][j] -= prodSum * orthogonalMatrix[i][k];
                    }
                }
                double norm = 0.0;
                for (int i = 0; i < n; i++) {
                    norm += matrixV[i][j] * matrixV[i][j];
                }
                norm = squareRoot(norm);
                upperTriangularMatrix[j][j] = norm;
                for (int i = 0; i < n; i++) {
                    orthogonalMatrix[i][j] = matrixV[i][j] / norm;
                }
            }
            return Pair.of(orthogonalMatrix, upperTriangularMatrix);
        }

        /**
         * <ul>
         *     <li>tr(A+B) = tr(A)+tr(B)</li>
         *     <li>tr(kA) = k tr(A), where k is a scalar</li>
         *     <li>tr(AB) = tr(BA)</li>
         *     <li>tr(ABC) = tr(BCA) = tr(CAB)</li>
         *     <li>tr(ABCD) = tr(BCDA) = tr(CDAB) = tr(DABC)</li>
         *     <li>tr(ABC) ≠ tr(ACB)</li>
         *     <li>tr(A₁ … Aₙ) = tr(A₂ … AₙA₁) = tr(A₃ … AₙA₁A₂) = …</li>
         *     <li>tr(Aᵀ) = tr(A)</li>
         *     <li>tr(A⊗B) = tr(A)tr(B)</li>
         *     <li>tr(Iₙ) = n</li>
         *     <li>tr(x·A + y·B) = x × tr(A) + y × tr(B), where A and B are square matrices of the same size,
         *     and x and y are scalars.</li>
         * </ul>
         */
        public static double matrixTrace(double[][] matrix) {
            Objects.requireNonNull(matrix);
            checkSquareMatrix(matrix);

            final int n = matrix.length;
            double trace = 0.0;
            for (int i = 0; i < n; i++) {
                trace += matrix[i][i];
            }
            return trace;
        }

        private static void checkSquareMatrix(double[][] matrix) {
            final int n = matrix.length;
            for (double[] row : matrix) {
                if (row.length != n) {
                    throw new IllegalArgumentException("The matrix must be square");
                }
            }
        }

        public static double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
            final double[] result = new double[matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                double sum = 0.0;
                for (int j = 0; j < vector.length; j++) {
                    sum += matrix[i][j] * vector[j];
                }
                result[i] = sum;
            }
            return result;
        }

        /**
         * <ul>
         *     <li>Associative: (xy)A = x(yA)</li>
         *     <li>Distributive over addition: x(A + B) = xA + xB</li>
         *     <li>1A = A</li>
         *     <li>det(kA) = kⁿ det(A)</li>
         * </ul>
         *
         * @return k ⋅ A
         */
        public static double[][] matrixMultiplyScalar(double[][] matrix, double multiplier) {
            Objects.requireNonNull(matrix);

            final int rows = matrix.length;
            if (rows == 0) {
                return new double[0][0];
            }

            final int columns = matrix[Constants.ARR_1ST_INDEX].length;
            final double[][] multiplied = new double[rows][columns]; // m×n
            for (int i = 0; i < rows; i++) {
                final double[] row = matrix[i];
                for (int j = 0; j < columns; j++) {
                    multiplied[i][j] = multiplier * row[j];
                }
            }
            return multiplied;
        }

        /**
         * @return A/k
         */
        public static double[][] matrixDivideScalar(double[][] matrix, double divisor) {
            Objects.requireNonNull(matrix);
            checkGreater0(divisor);

            final int rows = matrix.length;
            if (rows == 0) {
                return new double[0][0];
            }

            final int columns = matrix[Constants.ARR_1ST_INDEX].length;
            final double[][] multiplied = new double[rows][columns]; // m×n
            for (int i = 0; i < rows; i++) {
                final double[] row = matrix[i];
                for (int j = 0; j < columns; j++) {
                    multiplied[i][j] = row[j] / divisor;
                }
            }
            return multiplied;
        }

        public static double[][] matrixAdd(double[][] matrixBase, double[][] matrixChange) {
            Objects.requireNonNull(matrixBase);
            Objects.requireNonNull(matrixChange);
            checkSameDimensions(matrixBase, matrixChange);

            final int rows = matrixBase.length;
            final int columns = matrixBase[Constants.ARR_1ST_INDEX].length;

            final double[][] added = new double[rows][columns]; // m×n
            for (int i = 0; i < rows; i++) {
                final double[] row = matrixBase[i];
                for (int j = 0; j < columns; j++) {
                    added[i][j] = row[j] + matrixChange[i][j];
                }
            }
            return added;
        }

        public static double[][] matrixSubtract(double[][] matrixBase, double[][] matrixChange) {
            Objects.requireNonNull(matrixBase);
            Objects.requireNonNull(matrixChange);
            checkSameDimensions(matrixBase, matrixChange);

            final int rows = matrixBase.length;
            final int columns = matrixBase[Constants.ARR_1ST_INDEX].length;

            final double[][] subtracted = new double[rows][columns]; // m×n
            for (int i = 0; i < rows; i++) {
                final double[] row = matrixBase[i];
                for (int j = 0; j < columns; j++) {
                    subtracted[i][j] = row[j] - matrixChange[i][j];
                }
            }
            return subtracted;
        }

        /**
         * |+ -|
         * |- +|
         * <br/>
         * |+ - +|
         * |- + -|
         * |+ - +|
         * <br/>
         * |+ - + -|
         * |- + - +|
         * |+ - + -|
         * |- + - +|
         * Sign factor is (-1)ᶦ⁺ʲ
         * cofactor
         * (|a b|) = |d -c|
         * (|c d|)   |-b a|
         */
        public static double[][] cofactorMatrix(double[][] matrix) {
            Objects.requireNonNull(matrix);
            final int n = matrix.length;
            checkSquareMatrix(matrix);

            final double[][] cofactors = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    cofactors[i][j] = cofactor(matrix, i, j);
                }
            }
            return cofactors;
        }

        private static double cofactor(double[][] matrix, int row, int col) {
            final double[][] minor = minor(matrix, row, col);
            final double sign = ((row + col) % 2 == 0) ? 1 : -1;
            return sign * determinant(minor);
        }

        private static double[][] minor(double[][] matrix, int rowToRemove, int colToRemove) {
            final int n = matrix.length;
            final double[][] minor = new double[n - 1][n - 1];
            int row = 0;
            for (int i = 0; i < n; i++) {
                if (i == rowToRemove) {
                    continue;
                }
                int column = 0;
                for (int j = 0; j < n; j++) {
                    if (j == colToRemove) {
                        continue;
                    }
                    minor[row][column] = matrix[i][j];
                    column++;
                }
                row++;
            }
            return minor;
        }

        /**
         * ∥x∥₂ = √(∑ⁿₖ₌₁ |xₖ|²)
         */
        public static double vectorL2Norm(double[] vector) {
            double norm = 0.0;
            for (double v : vector) {
                norm += v * v;
            }
            return squareRoot(norm);
        }

        /**
         * ∥A∥₁ = max_₁≤ⱼ≤ₙ ∑ᵐᵢ₌₁ ∣aᵢ,ⱼ∣
         * ∥A∥∞ = max_₁≤ᵢ≤ₘ ∑ⁿⱼ₌₁ ∣aᵢ,ⱼ∣
         * ∥A∥₂ = √(λₘₐₓ(Aᵀ⋅A))
         * ∥A∥_F = √(trace(Aᵀ⋅A))
         * ∥A∥ₘₐₓ = maxᵢ,ⱼ ∣aᵢ,ⱼ∣
         */
        public static double[] matrixNorm(double[][] matrix) {
            Objects.requireNonNull(matrix);

            final double[] rowSums = new double[matrix.length];
            final double[] columnSums = new double[matrix.length];
            double maxNorm = 0;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    final double cellValue = matrix[i][j];
                    rowSums[i] += cellValue;
                    columnSums[i] += matrix[j][i];
                    if (cellValue > maxNorm) {
                        maxNorm = cellValue;
                    }
                }
            }
            final double norm1 = Arrays.stream(columnSums).max().orElseThrow();

            final double[][] transposed = transposeMatrix(matrix);
            final double[][] matrixTransposeProd = matrixMultiply(matrix, transposed);

            final var pair = eigenvaluesEigenvectors(matrixTransposeProd);
            final double maxEigenValue = Arrays.stream(pair.getLeft()).max().orElseThrow();
            final double l2norm = squareRoot(maxEigenValue);

            final double infinityNorm = Arrays.stream(rowSums).max().orElseThrow();
            final double frobeniusNorm = squareRoot(matrixTrace(matrixTransposeProd));

            return new double[]{norm1, infinityNorm, l2norm, frobeniusNorm, maxNorm};
        }

        /**
         * <ul>
         *     <li>(Aᵀ)ᵀ = A</li>
         *     <li>(A+B)ᵀ = Aᵀ + Bᵀ, where A and B are arbitrary matrices of the same size.</li>
         *     <li>(AB)ᵀ = BᵀAᵀ</li>
         *     <li>∣A∣ = ∣Aᵀ∣</li>
         * </ul>
         *
         * @return Aᵀ
         */
        public static double[][] transposeMatrix(double[][] matrix) {
            Objects.requireNonNull(matrix);

            final int rows = matrix.length;
            final int columns = matrix[Constants.ARR_1ST_INDEX].length;

            final double[][] transpose = new double[columns][rows];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    transpose[j][i] = matrix[i][j];
                }
            }
            return transpose;
        }

        /**
         * @return cₙ,ₘ = aₙ,₁ × b₁,ₘ + aₙ,₂ × b₂,ₘ + aₙ,₃ × b₃,ₘ + ...
         */
        public static double[][] matrixMultiply(double[][] matrix, double[][] matrix2) {
            Objects.requireNonNull(matrix);
            Objects.requireNonNull(matrix2);

            if (matrix.length == 0 || matrix2.length == 0) {
                return new double[0][0];
            }

            final int rowsA = matrix.length;
            final int colsA = matrix[Constants.ARR_1ST_INDEX].length;
            final int rowsB = matrix2.length;
            final int colsB = matrix2[Constants.ARR_1ST_INDEX].length;

            if (colsA != rowsB) {
                throw new IllegalArgumentException(
                    "Number of columns of first matrix must be equal to the number of rows of second matrix.");
            }

            final double[][] result = new double[rowsA][colsB];
            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    for (int k = 0; k < colsA; k++) {
                        result[i][j] += matrix[i][k] * matrix2[k][j];
                    }
                }
            }
            return result;
        }

        public static double[] matrixMultiply(double[][] matrix, double[] vector) {
            Objects.requireNonNull(matrix);
            Objects.requireNonNull(vector);

            checkSameDimensions(matrix, vector);

            final double[] result = new double[matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                result[i] = dotProduct(matrix[i], vector);
            }
            return result;
        }

        /**
         * A⋅A⁻¹ = A⁻¹⋅A = I
         * |(−1)¹⁺¹×A₁₁ (−1)¹⁺²×A₁₂ ⋯ (−1)¹⁺ⁿ×A₁ₙ|ᵀ
         * 1/∣A∣ × |(−1)²⁺¹×A₂₁ (−1)²⁺²×A₂₂ ⋯ (−1)²⁺ⁿ×A₂ₙ|
         * |     ⋮           ⋮      ⋱      ⋮     |
         * |(−1)ⁿ⁺¹×Aₙ₁ (−1)ⁿ⁺²×Aₙ₂  ⋯ (−1)ⁿ⁺ⁿ×Aₙₙ|
         * A⁻¹ = 1/(a×d-b×c) × |d -b|
         * |-c a|
         * <ul>
         *     <li>(A⁻¹)⁻¹ = A</li>
         *     <li>(A⋅B)⁻¹ = B⁻¹ ⋅ A⁻¹</li>
         *     <li>(Aᵀ)⁻¹ = (A⁻¹)ᵀ</li>
         *     <li>A singular matrix doesn't have an inverse, a nonsingular matrix does.</li>
         * </ul>
         */
        public static double[][] matrixInverse(double[][] matrix) {
            final double det = determinant(matrix);
            if (det == 0) {
                throw new IllegalStateException("The inverse doesn't exist");
            }

            final double[][] cofactors = cofactorMatrix(matrix);
            final double[][] adjugate = transposeMatrix(cofactors);
            return matrixMultiplyScalar(adjugate, 1 / det);
        }

        /**
         * {a₁x + b₁y + c₁z = d₁
         * {a₂x + b₂y + c₂z = d₂
         * {a₃x + b₃y + c₃z = d₃
         * x = ∣Wₓ∣/∣W∣
         * y = ∣Wᵧ∣/∣W∣
         * z = ∣W_z∣/∣W∣
         */
        public static double[] cramersRule(double[][] coefficientMatrix, double[] constantVector) {
            Objects.requireNonNull(coefficientMatrix);
            Objects.requireNonNull(constantVector);

            final int n = coefficientMatrix.length;
            if (coefficientMatrix[Constants.ARR_1ST_INDEX].length != n || constantVector.length != n) {
                throw new IllegalArgumentException("Matrix must be square and vector length must match.");
            }

            final double det = determinant(coefficientMatrix);
            if (Math.abs(det) < EPSILON_NEGATIVE10) {
                throw new IllegalArgumentException("System has no unique solution (determinant is zero).");
            }

            final double[] solution = new double[n];
            for (int varIdx = 0; varIdx < n; varIdx++) {
                final double[][] temp = new double[n][n]; //NOPMD
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        temp[i][j] = (j == varIdx) ? constantVector[i] : coefficientMatrix[i][j];
                    }
                }
                solution[varIdx] = determinant(temp) / det;
            }
            return solution;
        }

        /**
         * aka reduced row echelon form (RREF)
         * {a₁x + b₁y + c₁z = d₁
         * {a₂x + b₂y + c₂z = d₂
         * {a₃x + b₃y + c₃z = d₃
         */
        public static double[] gaussJordanEliminationSolver(double[][] coefficientMatrix, double[] constantVector) {
            Objects.requireNonNull(coefficientMatrix);
            Objects.requireNonNull(constantVector);

            final int rows = coefficientMatrix.length;
            final int cols = coefficientMatrix[Constants.ARR_1ST_INDEX].length;

            final double[][] augmented = new double[rows][cols + 1];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(coefficientMatrix[i], 0, augmented[i], 0, cols);
                augmented[i][cols] = constantVector[i];
            }

            int lead = 0;
            for (int row = 0; row < rows; row++) {
                if (lead >= cols) {
                    break;
                }
                int currentRow = row;
                while (Math.abs(augmented[currentRow][lead]) < EPSILON_NEGATIVE10) {
                    currentRow++;
                    if (currentRow == rows) {
                        currentRow = row;
                        lead++;
                        if (lead == cols) {
                            break;
                        }
                    }
                }
                if (lead == cols) {
                    break;
                }
                // Swap currentRow and row
                final double[] temp = augmented[row];
                augmented[row] = augmented[currentRow];
                augmented[currentRow] = temp;

                // Normalize row
                final double pivotValue = augmented[row][lead];
                for (int j = 0; j < cols + 1; j++) {
                    augmented[row][j] /= pivotValue;
                }

                // Eliminate other rows
                for (int eliminationRow = 0; eliminationRow < rows; eliminationRow++) {
                    if (eliminationRow != row) {
                        final double otherPivotValue = augmented[eliminationRow][lead];
                        for (int j = 0; j < cols + 1; j++) {
                            augmented[eliminationRow][j] -= otherPivotValue * augmented[row][j];
                        }
                    }
                }
                lead++;
            }

            // Extract solution (last column)
            final double[] solution = new double[rows];
            for (int i = 0; i < rows; i++) {
                solution[i] = augmented[i][cols];
            }
            return solution;
        }

        /**
         * Solve linear system Bx = v (Gaussian elimination)
         */
        private static double[] solveLinearSystem(double[][] coefficientMatrix, double[] constantVector) {
            final int n = coefficientMatrix.length;
            final double[][] augmented = new double[n][n + 1];
            for (int i = 0; i < n; i++) {
                System.arraycopy(coefficientMatrix[i], 0, augmented[i], 0, n);
                augmented[i][n] = constantVector[i];
            }
            // Forward elimination
            for (int i = 0; i < n; i++) {
                int maxRow = i;
                for (int k = i + 1; k < n; k++) {
                    if (Math.abs(augmented[k][i]) > Math.abs(augmented[maxRow][i])) {
                        maxRow = k;
                    }
                }
                final double[] temp = augmented[i];
                augmented[i] = augmented[maxRow];
                augmented[maxRow] = temp;
                for (int k = i + 1; k < n; k++) {
                    final double factor = augmented[k][i] / augmented[i][i];
                    for (int j = i; j <= n; j++) {
                        augmented[k][j] -= factor * augmented[i][j];
                    }
                }
            }
            // Back substitution
            final double[] solution = new double[n];
            for (int i = n - 1; i >= 0; i--) {
                solution[i] = augmented[i][n] / augmented[i][i];
                for (int k = i - 1; k >= 0; k--) {
                    augmented[k][n] -= augmented[k][i] * solution[i];
                }
            }
            return solution;
        }

        /**
         * A = LU where:
         * <ul>
         *     <li>L — lower triangular matrix (all elements above the diagonal are zero)</li>
         *     <li>U — upper triangular matrix (all the elements below the diagonal are zero)</li>
         * </ul>
         * <p>LU factorization with partial pivoting PA = LU
         * where P is a permutation matrix (it reorders the rows of A)</p>
         * |a₁₁ a₁₂ a₁₃|   |ℓ₁₁ 0   0  |   |u₁₁ u₁₂ u₁₃|
         * |a₂₁ a₂₂ a₂₃| = |ℓ₂₁ ℓ₂₂ 0  | ⋅ |0   u₂₂ u₂₃|
         * |a₃₁ a₃₂ a₃₃|   |ℓ₃₁ ℓ₃₂ ℓ₃₃|   |0   0   u₃₃|
         * <ul>
         *     <li>det(A) = det(L)⋅det(U) = (ℓ₁₁ ⋅ ... ⋅ ℓₙₙ)(u₁₁ ⋅ ... ⋅ uₙₙ)</li>
         *     <li>where:</li>
         *     <li>ℓ₁₁ ⋅ ... ⋅ ℓₙₙ are the diagonal entries of L</li>
         *     <li>u₁₁ ⋅ ... ⋅ uₙₙ are the diagonal entries of U</li>
         * </ul>
         * <br/>A⁻¹ = U⁻¹L⁻¹
         * <ol>
         *     <li>u₁ⱼ = a₁ⱼ <br/>ℓⱼ₁ = aⱼ₁ / u₁ⱼ</li>
         *     <li>uᵢᵢ = aᵢᵢ - ∑ᶦ⁻¹ₚ₌₁ ℓᵢₚuₚᵢ</li>
         *     <li>uᵢⱼ = aᵢⱼ - ∑ᶦ⁻¹ₚ₌₁ ℓᵢₚuₚⱼ</li>
         *     <li>ℓⱼᵢ = 1/uᵢᵢ (aⱼᵢ - ∑ᶦ⁻¹ₚ₌₁ ℓⱼₚuₚᵢ)</li>
         *     <li>for j = i+1, ..., n</li>
         *     <li>uₙₙ = aₙₙ - ∑ⁿ⁻¹ₚ₌₁ ℓₙₚuₚₙ</li>
         * </ol>
         */
        public static Pair<double[][], double[][]> matrixLUDecomposition(double[][] matrix) {
            Objects.requireNonNull(matrix);

            final int n = matrix.length;
            checkSquareMatrix(matrix);

            final double[][] lower = new double[n][n];
            final double[][] upper = new double[n][n];

            for (int i = 0; i < n; i++) {
                // Upper Triangular
                for (int k = i; k < n; k++) {
                    double sum = 0;
                    for (int j = 0; j < i; j++) {
                        sum += lower[i][j] * upper[j][k];
                    }
                    upper[i][k] = matrix[i][k] - sum;
                }

                // Lower Triangular
                for (int k = i; k < n; k++) {
                    if (i == k) {
                        lower[i][i] = 1; // Diagonal as 1
                    } else {
                        double sum = 0;
                        for (int j = 0; j < i; j++) {
                            sum += lower[k][j] * upper[j][i];
                        }
                        lower[k][i] = (matrix[k][i] - sum) / upper[i][i];
                    }
                }
            }
            return Pair.of(lower, upper);
        }

        /**
         * <ul>
         *     <li>A = L⋅Lᵀ</li>
         *     <li>A = Aᵀ</li>
         *     <li>ɪ · ɪᵀ = ɪ·ɪ = ɪ</li>
         *     <li>A must be symmetric.</li>
         *     <li>A must be square.</li>
         *     <li>A must be positive definite (meaning its eigenvalues must all be positive).</li>
         *     <li>For elements on L's diagonal: bⱼ,ⱼ = √(aⱼⱼ - ∑ʲ⁻¹ₖ₌₁ (bⱼ,ₖ)²</li>
         *     <li>For elements off L's diagonal: bᵢ,ⱼ = 1/bⱼ,ⱼ (aⱼⱼ - ∑ʲ⁻¹ₖ₌₁ bᵢ,ₖ ⋅ bⱼ,ₖ)</li>
         * </ul>
         */
        public static double[][] matrixCholeskyDecomposition(double[][] matrix) {
            Objects.requireNonNull(matrix);

            final int n = matrix.length;
            checkSquareMatrix(matrix);

            // Lower Triangular
            final double[][] lower = new double[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    double sum = 0.0;
                    for (int k = 0; k < j; k++) {
                        sum += lower[i][k] * lower[j][k];
                    }
                    if (i == j) {
                        lower[i][j] = squareRoot(matrix[i][i] - sum);
                    } else {
                        lower[i][j] = (matrix[i][j] - sum) / lower[j][j];
                    }
                }
            }
            return lower;
        }

        public static double[][] gaussJordanElimination(double[][] matrix) {
            Objects.requireNonNull(matrix);
            final int rows = matrix.length;
            final int cols = matrix[Constants.ARR_1ST_INDEX].length;

            final double[][] rref = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(matrix[i], 0, rref[i], 0, cols);
            }

            int lead = 0;
            for (int row = 0; row < rows; row++) {
                if (lead >= cols) {
                    break;
                }
                int currentRow = row;
                while (Math.abs(rref[currentRow][lead]) < EPSILON_NEGATIVE10) {
                    currentRow++;
                    if (currentRow == rows) {
                        currentRow = row;
                        lead++;
                        if (lead == cols) {
                            return new double[0][0];
                        }
                    }
                }
                // Swap rows currentRow and row
                final double[] temp = rref[row];
                rref[row] = rref[currentRow];
                rref[currentRow] = temp;

                // Normalize row
                final double pivotValue = rref[row][lead];
                for (int j = 0; j < cols; j++) {
                    rref[row][j] /= pivotValue;
                }

                // Eliminate other rows
                for (int eliminationRow = 0; eliminationRow < rows; eliminationRow++) {
                    if (eliminationRow != row) {
                        final double otherPivotValue = rref[eliminationRow][lead];
                        for (int j = 0; j < cols; j++) {
                            rref[eliminationRow][j] -= otherPivotValue * rref[row][j];
                        }
                    }
                }
                lead++;
            }
            return rref;
        }

        /**
         * v = (v₁, v₂, … , vₙ)
         * A⋅v = 0
         */
        public static double[][] matrixNullSpace(double[][] matrix) {
            Objects.requireNonNull(matrix);
            final int rows = matrix.length;
            final int cols = matrix[Constants.ARR_1ST_INDEX].length;
            final double[][] rref = gaussJordanElimination(matrix);

            final double epsilon = 1e-8;
            // Step 1: Identify pivot columns
            boolean[] isPivot = new boolean[cols];
            int r = 0;
            for (int c = 0; c < cols && r < rows; c++) {
                if (Math.abs(rref[r][c] - 1.0) < epsilon) {
                    isPivot[c] = true;
                    r++;
                }
            }

            // Step 2: For each free variable (non-pivot column), build a null space vector
            int numFree = 0;
            for (boolean b : isPivot) {
                if (!b) {
                    numFree++;
                }
            }
            final double[][] nullSpace = new double[numFree][cols];
            int freeIdx = 0;
            for (int freeCol = 0; freeCol < cols; freeCol++) {
                if (isPivot[freeCol]) {
                    continue;
                }
                final double[] vec = new double[cols]; //NOPMD
                vec[freeCol] = 1.0;
                // For each pivot row, set the value so that A*v = 0
                int pivotRow = 0;
                for (int c = 0; c < cols; c++) {
                    if (isPivot[c]) {
                        vec[c] = -rref[pivotRow][freeCol];
                        pivotRow++;
                    }
                }
                nullSpace[freeIdx++] = vec;
            }
            return nullSpace;
        }

        /**
         * w = α₁ ⋅ v⃗₁ + α₂ ⋅ v⃗₂ + α₃ ⋅ v⃗₃ + ... + αₙ ⋅ v⃗ₙ
         * where α₁, α₂, α₃, ... αₙ are any numbers
         */
        public static double[][] matrixColumnSpace(double[][] matrix) {
            Objects.requireNonNull(matrix);

            final int rows = matrix.length;
            final int cols = matrix[Constants.ARR_1ST_INDEX].length;
            final double[][] rref = gaussJordanElimination(matrix);

            // Identify pivot columns (columns with leading 1s in RREF)
            final boolean[] pivots = new boolean[cols];
            for (int row = 0, col = 0; col < cols && row < rows; col++) {
                if (Math.abs(rref[row][col] - 1.0) < EPSILON_NEGATIVE10) {
                    pivots[col] = true;
                    row++;
                }
            }

            // Extract the original columns corresponding to pivot columns
            int numPivots = 0;
            for (boolean pivot : pivots) {
                if (pivot) {
                    numPivots++;
                }
            }
            final double[][] basis = new double[numPivots][rows];
            int idx = 0;
            for (int c = 0; c < cols; c++) {
                if (pivots[c]) {
                    for (int i = 0; i < rows; i++) {
                        basis[idx][i] = matrix[i][c];
                    }
                    idx++;
                }
            }
            // Return as column vectors (transpose for your test format)
            final double[][] result = new double[numPivots][rows];
            for (int i = 0; i < numPivots; i++) {
                for (int j = 0; j < rows; j++) {
                    result[i][j] = basis[i][j];
                }
            }
            return result;
        }

        /**
         * v⃗₁, v⃗₂, v⃗₃, ..., v⃗ₙ are linearly independent vectors if the equation
         * α₁ ⋅ v⃗₁ + α₂ ⋅ v⃗₂ + α₃ ⋅ v⃗₃ + ... + αₙ ⋅ v⃗ₙ = 0 ⃗ holds iff α₁=α₂=α₃=...=αₙ
         */
        public static boolean isLinearlyIndependent(double[][] vectors) {
            Objects.requireNonNull(vectors);
            final int rank = matrixRank(vectors);
            final int cols = vectors[Constants.ARR_1ST_INDEX].length;
            return rank == cols;
        }

        /**
         * a₁x + b₁y = c₁
         * a₂x + b₂y = c₂
         * m₁ := LCM(a₁, a2) / a₁
         * m₂ := LCM(a1, a₂) / a₂
         * LCM(a₁, a₂)x + [LCM(a₁, a₂)b₁/a₁]y = LCM(a₁,a₂)c₁/a₁
         * -LCM(a₁, a₂)x - [LCM(a₁, a₂)b₂/a₂]y = -LCM(a₁, a₂)c₂/a₂
         * LCM(a₁, a₂) = L
         * L⋅x + L⋅b₁/a₁⋅y = L⋅c₁/a₁
         * -L⋅x - L⋅b₂/a₂⋅y = -L⋅c₂/a₂
         * (L⋅b₁/a₁ - L⋅b₂/a₂)⋅y = L⋅c₁/a₁ -L⋅c₂/a₂
         * y = (L⋅c₁/a₁ - L⋅c₂/a₂)/(L⋅b₁/a₁ - L⋅b₂/a₂)
         */
        public static double[] linearCombinationLCM(double[][] equations) {
            Objects.requireNonNull(equations);

            if (equations.length != 2 || equations[Constants.ARR_1ST_INDEX].length != 3
                || equations[Constants.ARR_2ND_INDEX].length != 3) {
                throw new IllegalArgumentException("Input must be two equations of the form [a, b, c]");
            }

            final byte row1 = Constants.ARR_1ST_INDEX;
            final byte row2 = Constants.ARR_2ND_INDEX;
            final byte col1 = Constants.ARR_1ST_INDEX;
            final byte col2 = Constants.ARR_2ND_INDEX;
            final byte col3 = Constants.ARR_3RD_INDEX;
            final double a1 = equations[row1][col1];
            final double a2 = equations[row2][col1];
            final double b1 = equations[row1][col2];
            final double b2 = equations[row2][col2];
            final double c1 = equations[row1][col3];
            final double c2 = equations[row2][col3];

            final long lcm = Arithmetic.lcmWithPrimeFactorization(new double[]{Math.abs(a1), Math.abs(a2)});

            // Scale equations to equalize x coefficients
            final double scale1 = lcm / Math.abs(a1);
            double scale2 = lcm / Math.abs(a2);

            // Adjust sign to make coefficients opposite for elimination
            if (a1 * a2 > 0) {
                scale2 = -scale2;
            }

            // Eliminate x
            final double yCoeff = b1 * scale1 + b2 * scale2;
            final double constTerm = c1 * scale1 + c2 * scale2;
            final double y = constTerm / yCoeff;

            // Substitute y back to find x
            final double x = (c1 - b1 * y) / a1;

            return new double[]{x, y};
        }

        /**
         * rank(A) ≤ min(n,m)
         */
        public static int matrixRank(double[][] matrix) {
            Objects.requireNonNull(matrix);

            final double[][] rref = gaussJordanElimination(matrix);

            int rank = 0;
            for (double[] row : rref) {
                boolean nonZero = false;
                for (double v : row) {
                    if (Math.abs(v) > EPSILON_NEGATIVE10) {
                        nonZero = true;
                        break;
                    }
                }
                if (nonZero) {
                    rank++;
                }
            }
            return rank;
        }

        /**
         * <ol>
         *     <li>Set u₁ = v₁.</li>
         *     <li>Normalize u₁: e₁ = u₁/|u₁|. This is the first element of the orthonormal basis.</li>
         *     <li>Find the second vector of the basis by choosing the vector orthogonal
         *     to u₁: u₂ = v₂ - [(v₂ ⋅ u₁)/(u₁ ⋅ u₁)] × u₁.</li>
         *     <li>Normalize u₂: e₂ = u₂/|u₂|.</li>
         *     <li>Repeat the procedure for v₃. Finding the null vector means that the original
         *     set is not linearly independent: they span a two-dimensional vector space.</li>
         * </ol>
         * v = (a₁, a₂, a₃)
         * w = (b₁, b₂, b₃)
         * u = (c₁, c₂, c₃)
         */
        public static double[][] gramSchmidt(double[][] matrix) {
            Objects.requireNonNull(matrix);

            final int rows = matrix.length;
            final int cols = matrix[Constants.ARR_1ST_INDEX].length;

            // Store orthonormal vectors as rows
            final double[][] orthonormal = new double[rows][cols];
            int orthoCount = 0;

            for (int i = 0; i < rows; i++) {
                // Copy the current row vector
                final double[] rowVector = new double[cols]; //NOPMD
                System.arraycopy(matrix[i], 0, rowVector, 0, cols);

                // Subtract projections onto previous orthonormal vectors
                for (int j = 0; j < orthoCount; j++) {
                    final double dot = dotProduct(rowVector, orthonormal[j]);
                    for (int k = 0; k < cols; k++) {
                        rowVector[k] -= dot * orthonormal[j][k];
                    }
                }

                final double norm = vectorL2Norm(rowVector);

                // If norm is not zero, normalize and add to orthonormal set
                if (norm > EPSILON_NEGATIVE10) {
                    for (int k = 0; k < cols; k++) {
                        rowVector[k] /= norm;
                    }
                    orthonormal[orthoCount++] = rowVector;
                }
            }

            // Return only the nonzero orthonormal vectors (as rows, to match expectedResult)
            final double[][] result = new double[orthoCount][cols];
            for (int i = 0; i < orthoCount; i++) {
                System.arraycopy(orthonormal[i], 0, result[i], 0, cols);
            }
            return result;
        }

        /**
         * @return A = UΣVᵀ
         */
        public static Triple<double[][], double[][], double[][]> svd(double[][] matrix) {
            final int m = matrix.length;
            final int n = matrix[Constants.ARR_1ST_INDEX].length;

            // 1. Compute A^T A
            final double[][] ATA = matrixMultiply(transposeMatrix(matrix), matrix);

            // 2. Eigen-decomposition of ATA
            final var eig = eigenvaluesEigenvectors(ATA);
            final double[] eigenvalues = eig.getLeft();
            final double[][] eigenvectors = eig.getRight();

            // 3. Singular values (sqrt of eigenvalues, sorted descending)
            final double[] singularValues = Arrays.stream(eigenvalues)
                .map(ev -> ev < 0 ? 0.0 : squareRoot(ev))
                .toArray();
            final var indices = new Integer[n];
            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }
            Arrays.sort(indices, (i, j) -> Double.compare(singularValues[j], singularValues[i]));

            final double[][] sigma = new double[m][n];
            for (int i = 0; i < n; i++) {
                sigma[i][i] = singularValues[indices[i]];
            }

            // 4. Sort V columns accordingly
            final double[][] sortedEigenvectors = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sortedEigenvectors[j][i] = eigenvectors[j][indices[i]];
                }
            }

            // 5. Compute U = A V Σ^+
            final double[][] sigmaPlus = new double[n][n];
            for (int i = 0; i < n; i++) {
                if (sigma[i][i] > EPSILON_NEGATIVE10) {
                    sigmaPlus[i][i] = 1.0 / sigma[i][i];
                }
            }
            final double[][] matrixAV = matrixMultiply(matrix, sortedEigenvectors);
            final double[][] matrixU = new double[m][n];
            for (int i = 0; i < n; i++) {
                double norm = 0.0;
                for (int j = 0; j < m; j++) {
                    norm += matrixAV[j][i] * matrixAV[j][i];
                }
                norm = squareRoot(norm);
                if (norm > EPSILON_NEGATIVE10) {
                    for (int j = 0; j < m; j++) {
                        matrixU[j][i] = matrixAV[j][i] / norm;
                    }
                }
            }

            return Triple.of(matrixU, sigma, transposeMatrix(sortedEigenvectors));
        }

        /**
         * @param leftSingularVectors            aka U
         * @param rightSingularVectorsTransposed aka Vᵀ
         */
        public static double[][] reconstructFromSVD(
            double[][] leftSingularVectors, double[][] sigma, double[][] rightSingularVectorsTransposed) {
            final double[][] usigma = matrixMultiply(leftSingularVectors, sigma);
            return matrixMultiply(usigma, rightSingularVectorsTransposed);
        }

        public static double[] getMatrixColumn(double[][] matrix, int column) {
            final double[] result = new double[matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                result[i] = matrix[i][column];
            }
            return result;
        }

        /**
         * <ul>
         *     <li>aka entrywise product, element-wise product, the Schur product.</li>
         *     <li>Commutativity (unlike the standard matrix product): A ∘ B = B ∘ A</li>
         *     <li>Associative: A ∘ (B∘C) = (A∘B) ∘ C</li>
         *     <li>Distributive over the addition: A ∘ (B+C) = (A∘B) + (A∘C)</li>
         *     <li>rank(A ∘ B) ≤ rank(A) × rank(B)</li>
         *     <li>(A⊗B) ∘ (C⊗D) = (A∘C) ⊗ (B∘D)</li>
         * </ul>
         *
         * @return A ∘ B
         */
        public static double[][] hadamardProduct(double[][] matrix, double[][] matrix2) {
            checkSameDimensions(matrix, matrix2);

            final double[][] result = new double[matrix.length][matrix[Constants.ARR_1ST_INDEX].length];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    result[i][j] = matrix[i][j] * matrix2[i][j];
                }
            }
            return result;
        }

        /**
         * aka Kronecker product
         * A=|a₁₁ a₁₂| B=|b₁₁ b₁₂|
         * |a₂₁ a₂₂|   |b₂₁ b₂₂|
         * A⊗B=|a₁₁b₁₁ a₁₁b₁₂ a₁₂b₁₁ a₁₂b₁₂|
         * |a₁₁b₂₁ a₁₁b₂₂ a₁₂b₂₁ a₁₂b₂₂|
         * |a₂₁b₁₁ a₂₁b₁₂ a₂₂b₁₁ a₂₂b₁₂|
         * |a₂₁b₂₁ a₂₁b₂₂ a₂₂b₂₁ a₂₂b₂₂|
         * <ul>
         *     <li>Associativity: (A⊗B)⊗C = A⊗(B⊗C)</li>
         *     <li>Bilinearity: (A+B)⊗C = A⊗C+B⊗C
         *     <li>Is not commutative: A ⊗ B ≠ B ⊗ A</li>
         *     <br/>(xA)⊗B = x(A⊗B)
         *     <br/>A⊗(B+C)=A⊗B+A⊗C
         *     <br/>A⊗(xB)=x(A⊗B)</li>
         *     <li>(Conjugate) transposition: (A⊗B)ᵀ = Aᵀ⊗Bᵀ</li>
         *     <li>(A⊗B)^∗ = A^∗ ⊗ B^∗</li>
         *     <li>rank(A⊗B) = rank(A)⋅rank(B)</li>
         *     <li>(A⊗B)⁻¹ = A⁻¹ ⊗ B⁻¹</li>
         *     <li>det(A⊗B) = det(A)ⁿ det(B)ᵐ</li>
         *     <li>trace(A⊗B) = trace(A)trace(B)</li>
         * </ul>
         */
        public static double[][] tensorProduct(double[][] matrix, double[][] matrix2) {
            final int rows = matrix.length;
            final int columns = matrix[Constants.ARR_1ST_INDEX].length;
            final int rows2 = matrix2.length;
            final int columns2 = matrix2[Constants.ARR_1ST_INDEX].length;

            final double[][] result = new double[rows * rows2][columns * columns2];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    for (int row = 0; row < rows2; row++) {
                        for (int col = 0; col < columns2; col++) {
                            result[i * rows2 + row][j * columns2 + col] = matrix[i][j] * matrix2[row][col];
                        }
                    }
                }
            }
            return result;
        }

        /**
         * D=|λ₁ 0 … 0|
         * |0 λ₂ … 0|
         * |⋮ ⋮ ⋱ ⋮|
         * |0 0 … λₙ|
         */
        public static double[][] diagonalizeMatrix(double[][] matrix, double[] eigenvalues) {
            Objects.requireNonNull(matrix);
            Objects.requireNonNull(eigenvalues);

            final int n = matrix.length;
            final double[][] diagonalMatrix = new double[n][n];
            final int min = Math.min(n, eigenvalues.length);
            for (int i = 0; i < min; i++) {
                diagonalMatrix[i][i] = eigenvalues[i];
            }
            return diagonalMatrix;
        }

        /**
         * Definition variants:
         * <ul>
         *     <li>det(A - λI)</li>
         *     <li>det(λI - A)</li>
         * </ul>
         * <ul>
         *     <li>det = (a - λ)(d - λ) - bc = λ2 - (a + d)λ + (ad - bc)</li>
         *     <li>p(λ) = det(A - λI)</li>
         * </ul>
         */
        public static double[] characteristicPolynomial(double[][] matrix) {
            checkSquareMatrix(matrix);
            final int n = matrix.length;

            double[][] matrixCopy = new double[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(matrix[i], 0, matrixCopy[i], 0, n);
            }

            final double[] coeffs = new double[n + 1];
            coeffs[Constants.ARR_1ST_INDEX] = 1.0; // λⁿ

            final double[][] matrixI = identityMatrix(n);
            final double[][] newB = new double[n][n];
            for (int k = 1; k <= n; k++) {
                // Compute trace of B
                double trace = 0.0;
                for (int i = 0; i < n; i++) {
                    trace += matrixCopy[i][i];
                }

                coeffs[k] = -trace / k;

                // Update B = A * (B + coeffs[k] * I)
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        newB[i][j] = matrixCopy[i][j] + coeffs[k] * matrixI[i][j];
                    }
                }
                matrixCopy = matrixMultiply(matrix, newB);
            }

            return coeffs;
        }

        /**
         * aka adjugate
         * <ul>
         *     <li>A * adj(A) = det(A) * I</li>
         *     <li>A⁻¹ = (1 / det(A)) * adj(A)</li>
         *     <li>adj(Aᵀ) = adj(A)ᵀ</li>
         *     <li>adj(AB) = adj(A)adj(B)</li>
         *     <li>adj(Aᵏ) = adj(A)ᵏ</li>
         *     <li>The adjoint of a matrix may refer to either its adjugate, i.e., the transpose of the cofactor matrix,
         *     or the conjugate transpose.</li>
         * </ul>
         */
        public static double[][] adjointMatrix(double[][] matrix) {
            final double[][] cofactors = cofactorMatrix(matrix);
            return transposeMatrix(cofactors);
        }

        /**
         * Singular values vs eigenvalues:
         * <ul>
         *     <li>Every matrix (square or rectangular) has singular values. Only square matrices have eigenvalues.</li>
         *     <li>Singular values are always real and non-negative. Eigenvalues may be negative or complex.</li>
         * </ul>
         */
        public static double[] singularValues(double[][] matrix) {
            final double[][] transposed = transposeMatrix(matrix);
            final double[][] matrixMultiplied = matrixMultiply(matrix, transposed);
            final double[] eigenvalues = eigenvaluesEigenvectors(matrixMultiplied).getLeft();
            // Clamp negatives to zero, take sqrt, and collect
            final double[] singularValues = Arrays.stream(eigenvalues)
                .map(ev -> ev < 0 ? 0. : squareRoot(ev))
                .toArray();

            // Sort in descending order
            Arrays.sort(singularValues);
            for (int i = 0, j = singularValues.length - 1; i < j; i++, j--) {
                double temp = singularValues[i];
                singularValues[i] = singularValues[j];
                singularValues[j] = temp;
            }

            round4InPlace(singularValues);
            return singularValues;
        }

        /**
         * @return A⁺
         */
        public static double[][] pseudoinverse(double[][] matrix) {
            // 1. Compute A*Aᵀ and row-reduce to RREF
            final double[][] matrixAAT = matrixMultiply(matrix, transposeMatrix(matrix));
            final double[][] matrixAATrref = gaussJordanElimination(matrixAAT);

            // 2. Take non-zero rows of matrixAATrref as columns of P
            final double[][] matrixP = extractNonZeroRowsAsColumns(matrixAATrref);

            // 3. Compute Aᵀ*A and row-reduce to RREF
            final double[][] matrixATA = matrixMultiply(transposeMatrix(matrix), matrix);
            final double[][] matrixATArref = gaussJordanElimination(matrixATA);

            // 4. Take non-zero rows of matrixATArref as columns of Q
            final double[][] matrixQ = extractNonZeroRowsAsColumns(matrixATArref);

            // 5. Compute M = Pᵀ * A * Q
            final double[][] matrixPT = transposeMatrix(matrixP);
            final double[][] matrixPQ = matrixMultiply(matrixPT, matrix);
            final double[][] matrixM = matrixMultiply(matrixPQ, matrixQ);

            // 6. Compute M⁻¹
            final double[][] matrixMinv = matrixInverse(matrixM);

            // 7. Compute pseudoinverse: Q * M⁻¹ * Pᵀ
            final double[][] matrixQMInv = matrixMultiply(matrixQ, matrixMinv);
            final double[][] matrixPTfinal = transposeMatrix(matrixP);

            return matrixMultiply(matrixQMInv, matrixPTfinal);
        }

        /**
         * Extract non-zero rows of a matrix and use them as columns of a new matrix
         */
        private static double[][] extractNonZeroRowsAsColumns(double[][] matrix) {
            final int rows = matrix.length;
            final int cols = matrix[Constants.ARR_1ST_INDEX].length;
            // Count non-zero rows
            int count = 0;
            final boolean[] isNonZero = new boolean[rows];
            for (int i = 0; i < rows; i++) {
                boolean nonZero = false;
                for (int j = 0; j < cols; j++) {
                    if (Math.abs(matrix[i][j]) > EPSILON_NEGATIVE10) {
                        nonZero = true;
                        break;
                    }
                }
                if (nonZero) {
                    isNonZero[i] = true;
                    count++;
                }
            }
            final double[][] result = new double[cols][count];
            int idx = 0;
            for (int i = 0; i < rows; i++) {
                if (isNonZero[i]) {
                    for (int j = 0; j < cols; j++) {
                        result[j][idx] = matrix[i][j];
                    }
                    idx++;
                }
            }
            return result;
        }

        public static double[] vectorSubtract(double[] vector1, double[] vector2) {
            Objects.requireNonNull(vector1);
            Objects.requireNonNull(vector2);
            checkSameDimensions(vector1, vector2);

            final double[] result = new double[vector1.length];
            for (int i = 0; i < vector1.length; i++) {
                result[i] = vector1[i] - vector2[i];
            }
            return result;
        }

        public static double[] vectorDivideScalar(double[] vector, double scalar) {
            Objects.requireNonNull(vector);
            checkGreater0(scalar);

            final double[] result = new double[vector.length];
            for (int i = 0; i < vector.length; i++) {
                result[i] = vector[i] / scalar;
            }
            return result;
        }
    }

    /**
     * Derivative notations:
     * <ul>
     *     <li>Lagrange's: f'</li>
     *     <li>Leibniz's: dy/dx; y = f(x) → d/dx f(x)</li>
     *     <li>Newton's: ẏ</li>
     * </ul>
     */
    public static final class Calculus {
        public static final double NUMERICAL_APPROXIMATION = 1e-8;

        private Calculus() {
        }

        // Limit

        /**
         * Polynomial function: limₓ→ₐ p(x) = p(a)
         * Rational function: limₓ→ₐ r(x) = r(a)
         * Square-root function: limₓ→ₐ q(x) = q(a)
         *
         * @return limₓ→ₐ f(x) = L
         */
        public static double limit(DoubleUnaryOperator f, double x) {
            final double[] limits = oneSidedLimits(f, x);
            final double lhs = limits[Constants.ARR_1ST_INDEX];
            final double rhs = limits[Constants.ARR_2ND_INDEX];
            return limitDiffTolerance(lhs, rhs);
        }

        private static double limitDiffTolerance(double lhs, double rhs) {
            // If both sides are close, return their average
            final double epsilon = 1e-6; // small value
            if (Math.abs(lhs - rhs) < epsilon) {
                return (lhs + rhs) / 2.0;
            }
            // Infinite Discontinuity: limₓ→ₐ⁺ f(x) = ∞ and/or limₓ→ₐ⁻ f(x) = -∞
            // Jump Discontinuity: limₓ→ₐ⁺ f(x) ≠ limₓ→ₐ⁻ f(x)
            // The limit does not exist (DNE) or is not well-defined numerically
            return Double.NaN;
        }

        public static double[] oneSidedLimits(DoubleUnaryOperator f, double x) {
            final double h = NUMERICAL_APPROXIMATION; // small value
            final double lhs = f.applyAsDouble(x - h); // limₓ→ₐ⁻ f(x) = L
            final double rhs = f.applyAsDouble(x + h); // limₓ→ₐ⁺ f(x) = L
            return new double[]{lhs, rhs};
        }

        public static double oneSidedLimitRHS(DoubleUnaryOperator f, double x) {
            return oneSidedLimits(f, x)[Constants.ARR_2ND_INDEX];
        }

        /**
         * @return lim₍ₓ,ᵧ₎→₍ₐ,b₎ f(x, y)
         */
        public static double limit(DoubleBinaryOperator f, double x, double y) {
            final double h = NUMERICAL_APPROXIMATION;
            final double epsilon = 1e-6;
            final double[] approaches = new double[]{
                f.applyAsDouble(x - h, y), // x approaches a⁻, y fixed
                f.applyAsDouble(x + h, y), // x approaches a⁺, y fixed
                f.applyAsDouble(x, y - h), // y approaches b⁻, x fixed
                f.applyAsDouble(x, y + h), // y approaches b⁺, x fixed
                f.applyAsDouble(x - h, y - h), // both approach from below (a,b)⁻
                f.applyAsDouble(x + h, y + h), // both approach from above (a,b)⁺
                f.applyAsDouble(x + h, y - h), // mixed (a⁺,b⁻)
                f.applyAsDouble(x - h, y + h), // mixed (a⁻,b⁺)
                f.applyAsDouble(x + h, x + h), // y = x path
                f.applyAsDouble(x + h, -x - h), // y = -x path
                f.applyAsDouble(x + h, 2 * (x + h)), // y = 2x path
                f.applyAsDouble(x + h, Math.pow(x + h, 2)) // y = x^2 path
            };
            double avg = 0.0;
            for (double value : approaches) {
                avg += value;
            }
            avg /= approaches.length;
            // Check if all approaches are close to the average
            for (double value : approaches) {
                if (Math.abs(value - avg) > epsilon) {
                    return Double.NaN; // the limit DNE
                }
            }
            return avg;
        }

        /**
         * @return limₓ→ₐ c = c
         */
        public static double limitConstantRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return limₓ→ₐ x = a
         */
        public static double limitIdentityRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return limₓ→ₐ f(x) + g(x) = limₓ→ₐ f(x) + limₓ→ₐ g(x)
         */
        public static double limitSumRule(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double fLimit = limit(f, x);
            final double gLimit = limit(g, x);
            return fLimit + gLimit;
        }

        /**
         * lhs = limₓ→ₐ⁻ f(x) + limₓ→ₐ⁻ g(x)
         * rhs = limₓ→ₐ⁺ f(x) + limₓ→ₐ⁺ g(x)
         *
         * @return lhs + rhs
         */
        public static double oneSidedLimitSum(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double[] fLimits = oneSidedLimits(f, x);
            final double[] gLimits = oneSidedLimits(g, x);
            final double lhsLimitSum = fLimits[Constants.ARR_1ST_INDEX] + gLimits[Constants.ARR_1ST_INDEX];
            final double rhsLimitSum = fLimits[Constants.ARR_2ND_INDEX] + gLimits[Constants.ARR_2ND_INDEX];
            return limitDiffTolerance(lhsLimitSum, rhsLimitSum);
        }

        /**
         * @return limₓ→ₐ f(x) - g(x) = limₓ→ₐ f(x) - limₓ→ₐ g(x)
         */
        public static double limitDifferenceRule(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double fLimit = limit(f, x);
            final double gLimit = limit(g, x);
            return fLimit - gLimit;
        }

        /**
         * lhs = limₓ→ₐ⁻ f(x) - limₓ→ₐ⁻ g(x)
         * rhs = limₓ→ₐ⁺ f(x) - limₓ→ₐ⁺ g(x)
         *
         * @return lhs - rhs
         */
        public static double oneSidedLimitDifference(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double[] fLimits = oneSidedLimits(f, x);
            final double[] gLimits = oneSidedLimits(g, x);
            final double lhsLimitDiff = fLimits[Constants.ARR_1ST_INDEX] - gLimits[Constants.ARR_1ST_INDEX];
            final double rhsLimitDiff = fLimits[Constants.ARR_2ND_INDEX] - gLimits[Constants.ARR_2ND_INDEX];
            return limitDiffTolerance(lhsLimitDiff, rhsLimitDiff);
        }

        /**
         * @return limₓ→ₐ c * f(x) = c * limₓ→ₐ f(x)
         */
        public static double limitConstantMultipleRule(DoubleUnaryOperator f, double constant, double x) {
            return constant * limit(f, x);
        }

        /**
         * @return limₓ→ₐ c * f(x) = c * limₓ→ₐ f(x)
         */
        public static Pair<double[], Double> limitConstantMultipleRule(
            BiFunction<DoubleUnaryOperator[], Double, Double> f, DoubleUnaryOperator[] equationTerms,
            double constant, double x) {
            final double[] computedTerms = new double[equationTerms.length];
            for (int i = 0; i < computedTerms.length; i++) {
                final var term = equationTerms[i];
                computedTerms[i] = constant * term.applyAsDouble(x);
            }
            final double limit = constant * f.apply(equationTerms, x);
            return Pair.of(computedTerms, limit);
        }

        /**
         * @return limₓ→ₐ f(x) * g(x) = limₓ→ₐ f(x) * limₓ→ₐ g(x)
         */
        public static double limitProductRule(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double fLimit = limit(f, x);
            final double gLimit = limit(g, x);
            return fLimit * gLimit;
        }

        /**
         * lhs = limₓ→ₐ⁻ f(x) * limₓ→ₐ⁻ g(x)
         * rhs = limₓ→ₐ⁺ f(x) * limₓ→ₐ⁺ g(x)
         *
         * @return lhs - rhs
         */
        public static double oneSidedLimitProductRule(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double[] fLimits = oneSidedLimits(f, x);
            final double[] gLimits = oneSidedLimits(g, x);
            final double lhsLimitProduct = fLimits[Constants.ARR_1ST_INDEX] * gLimits[Constants.ARR_1ST_INDEX];
            final double rhsLimitProduct = fLimits[Constants.ARR_2ND_INDEX] * gLimits[Constants.ARR_2ND_INDEX];
            return limitDiffTolerance(lhsLimitProduct, rhsLimitProduct);
        }

        /**
         * limₓ→ₐ g(x) ≠ 0
         *
         * @return limₓ→ₐ f(x)/g(x) = (limₓ→ₐ f(x)) / (limₓ→ₐ g(x))
         */
        public static double limitQuotientRule(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double fLimit = limit(f, x);
            final double gLimit = limit(g, x);
            if (gLimit == 0) {
                return Double.NaN;
            }
            return fLimit / gLimit;
        }

        /**
         * @return limₓ→ₐ f(x)ⁿ = (limₓ→ₐ f(x))ⁿ
         */
        public static double limitPowerRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return limₓ→ₐ ⁿ√f(x) = ⁿ√(limₓ→ₐ f(x))
         */
        public static double limitRootRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * limₓ→ₐ f(x) = L
         *
         * @return limₓ→ₐ g(f(x)) = limₓ→L g(L)
         */
        public static double limitOfCompositeFunctions(DoubleUnaryOperator f, DoubleUnaryOperator g, double x) {
            final double fLimit = limit(f, x);
            return limit(g, fLimit);
        }

        // Derivative
        /*
        f'(a) = limₓ→ₐ (f(x)-f(a)) / (x-a)
         */

        /**
         * @return d/dx ≈ (f(x+Δx) - f(x)) / Δx
         */
        public static double derivativeForwardDifference(DoubleUnaryOperator f, double x, double deltaX) {
            return (f.applyAsDouble(x + deltaX) - f.applyAsDouble(x)) / deltaX;
        }

        /**
         * @return d/dx ≈ (f(x) - f(x-Δx)) / Δx
         */
        public static double derivativeBackwardDifference(DoubleUnaryOperator f, double x, double deltaX) {
            return (f.applyAsDouble(x) - f.applyAsDouble(x - deltaX)) / deltaX;
        }

        /**
         * @return d/dx ≈ (f(x+Δx) - f(x-Δx)) / (2 * Δx)
         */
        public static double derivativeCenteredDifference(DoubleUnaryOperator f, double x, double deltaX) {
            return (f.applyAsDouble(x + deltaX) - f.applyAsDouble(x - deltaX)) / (2 * deltaX);
        }

        // Basic Derivative Rules

        /**
         * @return d/dx(c) = 0
         */
        public static double derivativeConstantRule(DoubleUnaryOperator constantFn) {
            final double h = NUMERICAL_APPROXIMATION;
            return (constantFn.applyAsDouble(h) - constantFn.applyAsDouble(0)) / h;
        }

        /**
         * @return d/dx(c*f(x)) = c * f'(x)
         */
        public static double derivativeConstantMultipleRule(DoubleUnaryOperator f, double constant, double x) {
            double dfDx = derivativeForwardDifference(f, x, NUMERICAL_APPROXIMATION);
            return constant * dfDx;
        }

        /**
         * @return d/dx(c*f(x)) = c * f'(x)
         */
        public static Pair<double[], Double> derivativeConstantMultipleRule(
            BiFunction<DoubleUnaryOperator[], Double, Double> f, DoubleUnaryOperator[] equationTerms,
            double constant, double x) {
            final double h = NUMERICAL_APPROXIMATION;
            final double[] differentiatedTerms = new double[equationTerms.length];
            for (int i = 0; i < differentiatedTerms.length; i++) {
                final var term = equationTerms[i];
                differentiatedTerms[i] = constant * (term.applyAsDouble(x + h) - term.applyAsDouble(x)) / h;
            }
            final double derivative = constant * (f.apply(equationTerms, x + h) - f.apply(equationTerms, x)) / h;
            return Pair.of(differentiatedTerms, derivative);
        }

        /**
         * @return d/dx(xⁿ) = n * xⁿ⁻¹
         */
        public static double derivativePowerRule(double x, double exponent) {
            return exponent * Math.pow(x, exponent - 1);
        }

        /**
         * @return d/dx f(x) + g(x) = f'(x) + g'(x)
         */
        public static double derivativeSumRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return d/dx f(x) - g(x) = f'(x) - g'(x)
         */
        public static double derivativeDifferenceRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return d/dx f(x) * g(x) = f(x) * g'(x) + g(x) * f'(x)
         */
        public static double derivativeProductRule(
            double x,
            DoubleUnaryOperator f,
            DoubleUnaryOperator fPrime,
            DoubleUnaryOperator g,
            DoubleUnaryOperator gPrime
        ) {
            return f.applyAsDouble(x) * gPrime.applyAsDouble(x) + g.applyAsDouble(x) * fPrime.applyAsDouble(x);
        }

        /**
         * @return d/dx f(x)/g(x) = (g(x) * f(x) - f(x) * g'(x)) / (g(x)²)
         */
        public static double derivativeQuotientRule(
            double x,
            DoubleUnaryOperator f,
            DoubleUnaryOperator fPrime,
            DoubleUnaryOperator g,
            DoubleUnaryOperator gPrime
        ) {
            final double numerator = gPrime.applyAsDouble(x) * f.applyAsDouble(x)
                - g.applyAsDouble(x) * fPrime.applyAsDouble(x);
            final double denominator = Math.pow(f.applyAsDouble(x), 2);
            return numerator / denominator;
        }

        /**
         * @return d/dx f(g(x)) = f'(g(x)) * g'(x)
         */
        public static double derivativeChainRule(
            double x,
            DoubleUnaryOperator fPrime,
            DoubleUnaryOperator g,
            DoubleUnaryOperator gPrime
        ) {
            return fPrime.applyAsDouble(g.applyAsDouble(x)) * gPrime.applyAsDouble(x);
        }

        // Common Derivatives

        /**
         * @return d/dx(x) = 1
         */
        public static double derivativeOfX(@SuppressWarnings("unused") double x) {
            return 1;
        }

        /**
         * @return d/dx(cx) = c
         */
        public static double derivativeOfProdConstantAndX(double constant, @SuppressWarnings("unused") double x) {
            return constant;
        }

        // Derivative Rules of Exponential Functions

        /**
         * @return d/dx(eˣ) = eˣ
         */
        public static double derivativeOfEulerNumber(double x) {
            return Math.exp(x);
        }

        /**
         * @return d/dx(aˣ) = aˣ ln a
         */
        public static double derivativeExponentialRule(double number, double exponent) {
            return Math.pow(number, exponent) * Algebra.ln(number);
        }

        // Derivative Rules of Logarithmic Functions

        /**
         * @param x x > 0
         * @return d/dx(ln(x)) = 1/x
         */
        public static double derivativeOfLn(double x) {
            checkGreater0(x);
            return 1.0 / x;
        }

        /**
         * @param x x ≠ 0
         * @return d/dx(ln(|x|)) = 1/x
         */
        public static double derivativeOfAbsLn(double x) {
            checkNotEq0(x);
            return 1.0 / x;
        }

        /**
         * @param x x > 0
         * @return d/dx(logₐ(x)) = 1/(x ln a)
         */
        public static double derivativeOfLog(double x) {
            checkGreater0(x);
            return 1 / (x * Algebra.ln(x));
        }

        // Derivative Rules of Trigonometric Functions

        /**
         * @return d/dx(sin x) = cos x
         */
        public static double derivativeOfSin(double x) {
            return Trigonometry.cos(x);
        }

        /**
         * @return d/dx(cos x) = -sin x
         */
        public static double derivativeOfCos(double x) {
            return -Trigonometry.sin(x);
        }

        /**
         * @return d/dx(tan x) = sec²x
         */
        public static double derivativeOfTan(double x) {
            return Math.pow(Trigonometry.sec(x), 2);
        }

        /**
         * @return d/dx(csc x) = -csc x cot x
         */
        public static double derivativeOfCsc(double x) {
            return -Trigonometry.csc(x) * Trigonometry.cot(x);
        }

        /**
         * @return d/dx(sec x) = sec x tan x
         */
        public static double derivativeOfSec(double x) {
            return Trigonometry.sec(x) * Trigonometry.tan(x);
        }

        /**
         * @return d/dx(cot x) = -csc²x
         */
        public static double derivativeOfCot(double x) {
            return -Math.pow(Trigonometry.csc(x), 2);
        }

        // Derivative Rules of Inverse Trigonometric Functions

        /**
         * @return d/dx(sin⁻¹x) = 1/√(1-x²)
         */
        public static double derivativeOfInverseSin(double x) {
            return -Math.pow(Trigonometry.csc(x), 2);
        }

        /**
         * @return d/dx(cos⁻¹x) = -1/√(1-x²)
         */
        public static double derivativeOfInverseCos(double x) {
            return -1 / squareRoot(1 - x * x);
        }

        /**
         * @return d/dx(tan⁻¹x) = 1/(1 + x²)
         */
        public static double derivativeOfInverseTan(double x) {
            return 1 / (1 + x * x);
        }

        /**
         * @param x x ≠ -1, 0, 1
         * @return d/dx(csc⁻¹x) = -1/(|x| √(x² - 1))
         */
        public static double derivativeOfInverseCsc(double x) {
            checkNotEqTo(x, new double[]{-1, 0, 1});
            return -1 / (Math.abs(x) * squareRoot(x * x - 1));
        }

        /**
         * @param x x ≠ -1, 0, 1
         * @return d/dx(sec⁻¹x) = 1/(|x| √(x² - 1))
         */
        public static double derivativeOfInverseSec(double x) {
            checkNotEqTo(x, new double[]{-1, 0, 1});
            return Arithmetic.reciprocal(Math.abs(x) * squareRoot(x * x - 1));
        }

        /**
         * @return d/dx(cot⁻¹x) = -1/(1 + x²)
         */
        public static double derivativeOfInverseCot(double x) {
            return -1 / (1 + x * x);
        }

        // Derivative Rules of Hyperbolic Trigonometric Functions

        /**
         * @return d/dx(sinh x) = cosh x
         */
        public static double derivativeOfSinh(@SuppressWarnings("unused") double x) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return d/dx(cosh x) = sinh x
         */
        public static double derivativeOfCosh(@SuppressWarnings("unused") double x) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return d/dx(tanh x) = sech² x
         */
        public static double derivativeOfTanh(@SuppressWarnings("unused") double x) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return d/dx(csch x) = -csch x coth x
         */
        public static double derivativeOfCsch(@SuppressWarnings("unused") double x) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return d/dx(sech x) = -sech x tanh x
         */
        public static double derivativeOfSech(@SuppressWarnings("unused") double x) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return d/dx(coth x) = - csch² x
         */
        public static double derivativeOfCoth(@SuppressWarnings("unused") double x) {
            throw new UnsupportedOperationException();
        }

        // Partial Derivative

        /**
         * @return ∂f/∂x ≈ (f(x+Δx, y) - f(x, y)) / Δx
         */
        public static double partialDerivativeForwardDifferenceWrtX(
            DoubleBinaryOperator f, double x, double y, double deltaX) {
            return (f.applyAsDouble(x + deltaX, y) - f.applyAsDouble(x, y)) / deltaX;
        }

        /**
         * @return ∂f/∂y ≈ (f(x, y+Δy) - f(x, y)) / Δy
         */
        public static double partialDerivativeForwardDifferenceWrtY(
            DoubleBinaryOperator f, double x, double y, double deltaY) {
            return (f.applyAsDouble(x, y + deltaY) - f.applyAsDouble(x, y)) / deltaY;
        }

        /**
         * @return ∂(f-g)/∂x = ∂f/∂x - ∂g/∂x
         */
        public static double partialDerivativeDifferenceRuleWrtX(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDx = partialDerivativeForwardDifferenceWrtX(f, x, y, h);
            final double dgDx = partialDerivativeForwardDifferenceWrtX(g, x, y, h);
            return dfDx - dgDx;
        }

        /**
         * @return ∂(f-g)/∂y = ∂f/∂y - ∂g/∂y
         */
        public static double partialDerivativeDifferenceRuleWrtY(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDy = partialDerivativeForwardDifferenceWrtY(f, x, y, h);
            final double dgDy = partialDerivativeForwardDifferenceWrtY(g, x, y, h);
            return dfDy - dgDy;
        }

        /**
         * @return ∂(f+g)/∂x = ∂f/∂x + ∂g/∂x
         */
        public static double partialDerivativeSumRuleWrtX(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDx = partialDerivativeForwardDifferenceWrtX(f, x, y, h);
            final double dgDx = partialDerivativeForwardDifferenceWrtX(g, x, y, h);
            return dfDx + dgDx;
        }

        /**
         * @return ∂(f+g)/∂y = ∂f/∂y + ∂g/∂y
         */
        public static double partialDerivativeSumRuleWrtY(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDy = partialDerivativeForwardDifferenceWrtY(f, x, y, h);
            final double dgDy = partialDerivativeForwardDifferenceWrtY(g, x, y, h);
            return dfDy + dgDy;
        }

        /**
         * @return ∂/∂x(f₁ + f₂ + ... + fₙ) = Σⁿᵢ₌₁ ∂fᵢ/∂x
         */
        public static double partialDerivativeSumRuleWrtX(List<DoubleBinaryOperator> functions, double x, double y) {
            double sum = 0;
            for (final var f : functions) {
                sum += partialDerivativeForwardDifferenceWrtX(f, x, y, NUMERICAL_APPROXIMATION);
            }
            return sum;
        }

        /**
         * @return ∂/∂y(f₁ + f₂ + ... + fₙ) = Σⁿᵢ₌₁ ∂fᵢ/∂y
         */
        public static double partialDerivativeSumRuleWrtY(List<DoubleBinaryOperator> functions, double x, double y) {
            double sum = 0;
            for (final var f : functions) {
                sum += partialDerivativeForwardDifferenceWrtY(f, x, y, NUMERICAL_APPROXIMATION);
            }
            return sum;
        }

        /**
         * @return ∂(f⋅g)/∂x = f⋅∂g/∂x + g⋅∂f/∂x
         */
        public static double partialDerivativeProductRuleWrtX(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDx = partialDerivativeForwardDifferenceWrtX(f, x, y, h);
            final double dgDx = partialDerivativeForwardDifferenceWrtX(g, x, y, h);
            final double fResult = f.applyAsDouble(x, y);
            final double gResult = g.applyAsDouble(x, y);
            return fResult * dgDx + gResult * dfDx;
        }

        /**
         * @return ∂(f⋅g)/∂y = f⋅∂g/∂y + g⋅∂f/∂y
         */
        public static double partialDerivativeProductRuleWrtY(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDy = partialDerivativeForwardDifferenceWrtY(f, x, y, h);
            final double dgDy = partialDerivativeForwardDifferenceWrtY(g, x, y, h);
            final double fResult = f.applyAsDouble(x, y);
            final double gResult = g.applyAsDouble(x, y);
            return fResult * dgDy + gResult * dfDy;
        }

        /**
         * @return ∂(f/g)/∂x = (∂f/∂x⋅g - f⋅∂g/∂x)/g²
         */
        public static double partialDerivativeQuotientRuleWrtX(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double gResult = g.applyAsDouble(x, y);
            checkGreater0(gResult);
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDx = partialDerivativeForwardDifferenceWrtX(f, x, y, h);
            final double dgDx = partialDerivativeForwardDifferenceWrtX(g, x, y, h);
            final double fResult = f.applyAsDouble(x, y);
            final double gResultSquared = gResult * gResult;
            return (dfDx * gResult - fResult * dgDx) / gResultSquared;
        }

        /**
         * @return ∂(f/g)/∂y = (∂f/∂y⋅g - f⋅∂g/∂y)/g²
         */
        public static double partialDerivativeQuotientRuleWrtY(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double x, double y) {
            final double gResult = g.applyAsDouble(x, y);
            checkGreater0(gResult);
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDy = partialDerivativeForwardDifferenceWrtY(f, x, y, h);
            final double dgDy = partialDerivativeForwardDifferenceWrtY(g, x, y, h);
            final double fResult = f.applyAsDouble(x, y);
            final double gResultSquared = gResult * gResult;
            return (dfDy * gResult - fResult * dgDy) / gResultSquared;
        }

        /**
         * @return ∂f/∂x = ∂f/∂y * ∂y/∂x
         */
        public static double partialDerivativeChainRule(
            DoubleBinaryOperator f,
            double x,
            double y,
            DoubleUnaryOperator dyDxFn
        ) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDx = partialDerivativeForwardDifferenceWrtX(f, x, y, h);
            final double dfDy = partialDerivativeForwardDifferenceWrtY(f, x, y, h);
            final double dyDx = dyDxFn.applyAsDouble(x);
            return dfDx + dfDy * dyDx;
        }

        /**
         * @return ∂f/∂x = ∂f/∂y * ∂y/∂x
         */
        public static double partialDerivativeChainRule(
            DoubleBinaryOperator f,
            double x,
            double y,
            double dyDx
        ) {
            final double h = NUMERICAL_APPROXIMATION;
            final double dfDx = partialDerivativeForwardDifferenceWrtX(f, x, y, h);
            final double dfDy = partialDerivativeForwardDifferenceWrtY(f, x, y, h);
            return dfDx + dfDy * dyDx;
        }

        // Integral

        /**
         * The length of plain rectangles that approximates the area of the function.
         *
         * @return Δx = h = (b-a)/n
         */
        public static double widthOfSubinterval(double lowerLimit, double upperLimit, int numberOfIntervals) {
            if (lowerLimit > upperLimit) {
                throw new IllegalArgumentException("Invalid lower limit");
            }
            return (upperLimit - lowerLimit) / numberOfIntervals;
        }

        /**
         * ∫ₐ^b f(x)dx = h * [f(a)/2 + f(x₁) + f(x₂) + ... + f(xₙ₋₁) + f(b)/2]
         *
         * @return f(a)/2 + f(b)/2 part of the formula
         */
        public static double endpointsWeightedSum(DoubleUnaryOperator f, double lowerLimit, double upperLimit) {
            return ONE_HALF * (f.applyAsDouble(lowerLimit) + f.applyAsDouble(upperLimit));
        }

        // Rules of Definite Integrals

        /**
         * @return ∫ₐᵃ f(x)dx = 0
         */
        public static double integralZeroWidthIntervalRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return ∫ₐ^b f(x)dx = -∫ᵃ_b f(x)dx
         */
        public static double integralReverseLimitsRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return ∫ₐ^b c * f(x)dx = c * ∫ₐ^b f(x)dx
         */
        public static double integralConstantMultipleRule(
            DoubleUnaryOperator f, double lowerLimit, double upperLimit, int numberOfIntervals, double constant) {
            final double h = widthOfSubinterval(lowerLimit, upperLimit, numberOfIntervals); // dx
            double sum = endpointsWeightedSum(f, lowerLimit, upperLimit);
            for (int i = 1; i < numberOfIntervals; i++) {
                final double x = lowerLimit + i * h;
                sum += f.applyAsDouble(x);
            }
            return constant * sum * h;
        }

        /**
         * @return ∫ₐ^b [f(x) + g(x)]dx = ∫ᵃ_b f(x)dx + ∫ᵃ_b g(x)dx
         */
        public static double integralSumRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return ∫ₐ^b [f(x) - g(x)]dx = ∫ᵃ_b f(x)dx - ∫ᵃ_b g(x)dx
         */
        public static double integralDifferenceRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return ∫ₐᶜ f(x)dx = ∫꜀^b f(x)dx = ∫ₐ^b f(x)dx
         */
        public static double integralAdditivityRule() {
            throw new UnsupportedOperationException();
        }

        /**
         * y = mx + b
         *
         * @return ∫(ax + b)dx = (a/2)x² + bx + C
         */
        public static double indefiniteLinearIntegral(
            double x, double slope, double constantTerm, double constantOfIntegration) {
            return slope / 2 * x * x + constantTerm * x + constantOfIntegration;
        }

        /**
         * y = mx + b
         *
         * @return ∫ₐ^b f(x)dx = F(b) - F(a)
         */
        public static double definiteLinearIntegral(double x1, double x2, double slope, double constantTerm) {
            final double fx2 = slope / 2 * x2 * x2 + constantTerm * x2;
            final double fx1 = slope / 2 * x1 * x1 + constantTerm * x1;
            return fx2 - fx1;
        }

        /**
         * Numerically integrates f(x) from a to b using the Trapezoidal Rule.
         *
         * @param numberOfIntervals (higher = more accurate).
         * @return Approximate value of the definite integral.
         */
        public static double integrateTrapezoidalRule(
            DoubleUnaryOperator f, double lowerLimit, double upperLimit, int numberOfIntervals) {
            final double h = widthOfSubinterval(lowerLimit, upperLimit, numberOfIntervals); // dx
            double sum = endpointsWeightedSum(f, lowerLimit, upperLimit);
            for (int i = 1; i < numberOfIntervals; i++) {
                // For each trapezoid except the first and the last add the relative area
                sum += f.applyAsDouble(lowerLimit + i * h);
            }
            return sum * h;
        }

        /**
         * Numerically integrates f(x) from a to b using Simpson's Rule.
         *
         * @return Approximate value of the definite integral.
         */
        public static double integrateSimpson(
            DoubleUnaryOperator f, double lowerLimit, double upperLimit, int numberOfIntervals) {
            if (numberOfIntervals % 2 != 0) {
                throw new IllegalArgumentException("numberOfIntervals must be even");
            }
            final double h = widthOfSubinterval(lowerLimit, upperLimit, numberOfIntervals); // dx
            double sum = f.applyAsDouble(lowerLimit) + f.applyAsDouble(upperLimit);
            // For each subinterval of the given function, perform a substitution to a parabolic polynomial
            for (int i = 1; i < numberOfIntervals; i++) {
                if (Arithmetic.isEven(i)) {
                    sum += 2 * f.applyAsDouble(lowerLimit + i * h);
                } else { // for odd i
                    sum += 4 * f.applyAsDouble(lowerLimit + i * h);
                }
            }
            return sum * h / 3;
        }

        public static double definiteIntegralPiecewise(DoubleUnaryOperator[] functions, double lowerLimit,
                                                       double upperLimit, int numberOfIntervals, double[] boundaries) {
            if (functions.length == 0) {
                return 0;
            }

            double totalArea = 0;
            for (int i = 0; i < functions.length; i++) {
                final double segmentStart = (i == 0) ? lowerLimit : boundaries[i - 1];
                final double segmentEnd = (i == functions.length - 1) ? upperLimit : boundaries[i];
                final double intervalsForSegment = (segmentEnd - segmentStart) / (upperLimit - lowerLimit);
                final int segmentIntervals = (int) (intervalsForSegment * numberOfIntervals);

                totalArea += integrateSimpson(functions[i], segmentStart, segmentEnd, Math.max(1, segmentIntervals));
            }

            return totalArea;
        }

        public static double riemannSumIntegrateUsingMidpoint(DoubleUnaryOperator f, double lowerLimit,
                                                              double upperLimit, int numberOfIntervals) {
            final double deltaX = widthOfSubinterval(lowerLimit, upperLimit, numberOfIntervals);
            double sum = 0;
            for (int i = 0; i < numberOfIntervals; i++) {
                // Find the midpoint of the current rectangle
                final double midpoint = lowerLimit + deltaX / 2 + i * deltaX;
                sum += f.applyAsDouble(midpoint);
            }
            return sum * deltaX;
        }

        public static double riemannSumIntegrateLeft(DoubleUnaryOperator f, double lowerLimit,
                                                     double upperLimit, int numberOfIntervals) {
            final double deltaX = widthOfSubinterval(lowerLimit, upperLimit, numberOfIntervals);
            double sum = 0;

            for (int i = 0; i < numberOfIntervals; i++) {
                // Evaluate at the LEFT endpoint of each sub-interval
                final double x = lowerLimit + i * deltaX;
                sum += f.applyAsDouble(x);
            }
            return sum * deltaX;
        }

        public static double riemannSumIntegrateRight(DoubleUnaryOperator f, double lowerLimit,
                                                      double upperLimit, int numberOfIntervals) {
            final double deltaX = widthOfSubinterval(lowerLimit, upperLimit, numberOfIntervals);
            double sum = 0;

            for (int i = 0; i < numberOfIntervals; i++) {
                // Evaluate at the RIGHT endpoint of each sub-interval
                final double x = lowerLimit + (i + 1) * deltaX;
                sum += f.applyAsDouble(x);
            }
            return sum * deltaX;
        }
    }

    public static final class Stats {
        private Stats() {
        }

        public static final class Descriptive {
            private Descriptive() {
            }

            /**
             * Lower quartile rank (LQR) = 1/4(n + 1)
             *
             * @return Q1 = ((n + 1)/4). The position (index) of the value in the dataset
             */
            public static double q1rank(int datasetSize) {
                return (datasetSize + 1) / 4.;
            }

            /**
             * @return Lower quartile = x_LQR_int + LQR_frac × (x_(LQR_int+1) - x_LQR_int)
             */
            public static double lowerQuartile(double[] sortedDataset) {
                Objects.requireNonNull(sortedDataset);
                final double q1rank = q1rank(sortedDataset.length);
                final double[] splitWhole = NumberUtils.splitWholeAndFractionViaModulo(q1rank);
                // Subtract 1 to accommodate 0-based array indexing, the starting index for dataset is 1
                final int lqrInt = Math.toIntExact((long) splitWhole[Constants.ARR_1ST_INDEX]) - 1;
                final double lqrFraction = splitWhole[Constants.ARR_2ND_INDEX];
                return sortedDataset[lqrInt] + lqrFraction * (sortedDataset[lqrInt + 1] - sortedDataset[lqrInt]);
            }

            /**
             * @return Q2 = (n + 1)/2. The position (index) of the value in the dataset
             */
            public static double q2rank(int datasetSize) {
                return (datasetSize + 1) / 2.;
            }

            /**
             * Upper quartile rank (UQR) = 3/4(n + 1)
             *
             * @return Q3 = 3(n + 1)/4. The position (index) of the value in the dataset
             */
            public static double q3rank(int datasetSize) {
                return 3 * (datasetSize + 1) / 4.;
            }

            /**
             * @return Upper quartile = x_UQR_int + UQR_frac × (x_(UQR_int+1) - x_UQR_int)
             */
            public static double upperQuartile(double[] sortedDataset) {
                Objects.requireNonNull(sortedDataset);
                final double q3rank = q3rank(sortedDataset.length);
                final double[] splitWhole = NumberUtils.splitWholeAndFractionViaModulo(q3rank);
                // Subtract 1 to accommodate 0-based array indexing, the starting index for dataset is 1
                final int uqrInt = Math.toIntExact((long) splitWhole[Constants.ARR_1ST_INDEX]) - 1;
                final double uqrFraction = splitWhole[Constants.ARR_2ND_INDEX];
                return sortedDataset[uqrInt] + uqrFraction * (sortedDataset[uqrInt + 1] - sortedDataset[uqrInt]);
            }

            /**
             * Interquartile range (IRQ). aka midspread
             *
             * @return IQR = Q3 – Q1
             */
            public static double iqr(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final double q1 = lowerQuartile(dataset);
                final double q3 = upperQuartile(dataset);
                return q3 - q1;
            }

            /**
             * @return Quartile deviation = IQR / 2
             */
            public static double quartileDeviation(double iqr) {
                return iqr / 2;
            }

            /**
             * x < Q1 - 1.5 * IQR or x > Q3 + 1.5 * IQR
             */
            public static double[] outliers(double[] data) {
                if (data == null || data.length == 0) {
                    return new double[0];
                }

                final double range = iqr(data);
                final double[] quartiles = quartiles(data);
                final double q1 = quartiles[Constants.ARR_1ST_INDEX];
                final double q3 = quartiles[Constants.ARR_3RD_INDEX];
                final double upperBoundary = q3 + (1.5 * range);
                final double lowerBoundary = q1 - (1.5 * range);

                return Arrays.stream(data)
                    .filter(value -> value < lowerBoundary || value > upperBoundary)
                    .toArray();
            }

            private static double interpolate(double[] sortedData, double position) {
                final int n = sortedData.length;
                if (position < 1) {
                    return sortedData[0];
                }
                if (position > n) {
                    return sortedData[n - 1];
                }

                final int lowerIndex = (int) Math.floor(position) - 1;
                final int upperIndex = (int) Math.ceil(position) - 1;

                final double fraction = position - Math.floor(position);

                // If position is integer, average the value at that index and the next one
                if (fraction == 0) {
                    if (lowerIndex + 1 < n) {
                        return (sortedData[lowerIndex] + sortedData[lowerIndex + 1]) / 2.;
                    } else {
                        return sortedData[lowerIndex];
                    }
                } else {
                    return sortedData[lowerIndex] + fraction * (sortedData[upperIndex] - sortedData[lowerIndex]);
                }
            }

            /**
             * Mean squared error measures how close predicted values are to observed values. MSE is the average of
             * the squared differences between the predicted values and the observed values.
             * The smaller the value of MSE, the better the model is.
             * If the predicted values coincided perfectly with observed values, then MSE would be zero.
             * This, however, nearly never happens in practice: MSE is almost always strictly positive because
             * there's almost always some noise (randomness) in the observed values.
             *
             * @return MSE = (1 / n) * Σ(yᵢ - ŷᵢ)²
             */
            public static double mse(double[] predictedValues, double[] actualValues) {
                final double sumOfSquaredErrors = sumOfSquaredErrors(predictedValues, actualValues);
                return Arithmetic.reciprocal(predictedValues.length) * sumOfSquaredErrors;
            }

            /**
             * @return MSE = (1/n) * Σᵢ(xᵢ- μ)²
             */
            public static double mseSampleMean(double[] dataset) {
                final double mean = mean(dataset);
                final double sse = sumOfSquaredErrors(dataset, mean);
                return Arithmetic.reciprocal(dataset.length) * sse;
            }

            private static void checkPredictedValuesSameLength(double[] predictedValues, double[] actualValues) {
                final int n = predictedValues.length;
                if (n != actualValues.length) {
                    throw new IllegalArgumentException(
                        "The lengths of predictedValues and actualValues must be equal.");
                }
            }

            private static void checkNonEmpty(double[] data) {
                if (data.length == 0) {
                    throw new IllegalArgumentException("The input array must not be empty.");
                }
            }

            /**
             * Root mean squared error
             *
             * @return RMSE = √MSE
             */
            public static double rmse(double mse) {
                return squareRoot(mse);
            }

            /**
             * Root mean squared error
             *
             * @return RMSE = √(SSE / n)
             */
            public static double rmseFromSSE(double sse, int datasetSize) {
                return squareRoot(sse / datasetSize);
            }

            /**
             * @return MAE = (1/n) * Σ|yᵢ - xᵢ|
             */
            public static double mae(double[] predictedValues, double[] actualValues) {
                checkPredictedValuesSameLength(predictedValues, actualValues);
                checkNonEmpty(predictedValues);

                final int n = predictedValues.length;
                double absSumErrors = 0;
                for (int i = 0; i < n; i++) {
                    absSumErrors += Math.abs(actualValues[i] - predictedValues[i]);
                }
                return Arithmetic.reciprocal(n) * absSumErrors;
            }

            /**
             * Population Mean μ = ∑Xᵢ / N
             *
             * @return Sample Mean x̄ [X-bar] = ∑xᵢ / n = (x₁ + x₂ + ... + xₙ) / n
             */
            public static double mean(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                return Arrays.stream(dataset).sum() / dataset.length;
            }

            /**
             * median (odd dataset) = x[(n+1)/2]
             * median (even dataset) = (x[n/2] + x[(n+2)/2]) / 2
             */
            public static double median(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                if (Arithmetic.isOdd(dataset.length)) {
                    // Skip adding 1 to accommodate 0-based array indexing, the starting index for dataset is 1
                    return dataset[dataset.length / 2];
                }
                // Due to 0-based array: median (even dataset) = (x[(n-1)/2] + x[(n+1)/2]) / 2
                return (dataset[(dataset.length - 1) / 2] + dataset[(dataset.length + 1) / 2]) / 2;
            }

            /**
             * If the dataset has more than one mode, the distribution is multimodal.
             * If it has two modes, bimodal.
             * If all numbers occur the same number of times, then there's no mode.
             *
             * @param data The sorting isn't necessary
             * @return Pick the number(s) that appears most often
             */
            public static double[] mode(double[] data) {
                if (data == null || data.length == 0) {
                    return new double[0]; // As if there is no mode
                }

                final var frequencyTable = new HashMap<Double, Integer>();
                for (double value : data) {
                    frequencyTable.merge(value, 1, Integer::sum);
                }

                final int maxFrequency = frequencyTable.values().stream().max(Integer::compare).orElse(0);

                return frequencyTable.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxFrequency && maxFrequency > 1)
                    .map(Map.Entry::getKey)
                    .mapToDouble(Double::doubleValue)
                    .toArray();
            }

            /**
             * Variance is a measure of the variability of the values in a dataset.
             * A high variance indicates that a dataset is more spread out.
             * A low variance indicates that the data is more tightly clustered around the mean, or less spread out.
             *
             * @return s² = (1/(n-1)) * ∑ⁿᵢ₌₁(xᵢ−x̄)²
             */
            public static double sampleVariance(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final double sumOfSquares = sumOfSquares(dataset);
                final double besselsCorrection = dataset.length - 1.;
                return Arithmetic.reciprocal(besselsCorrection) * sumOfSquares;
            }

            /**
             * @return σ² = (1/N) * ∑ᴺᵢ₌₁(xᵢ−μ)²
             */
            public static double populationVariance(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final double sumOfSquares = sumOfSquares(dataset);
                return Arithmetic.reciprocal(dataset.length) * sumOfSquares;
            }

            /**
             * @return standard deviation = √(s²) = σ
             */
            public static double stdOfSampleVariance(double[] dataset) {
                return squareRoot(sampleVariance(dataset));
            }

            /**
             * @return √(σ²)
             */
            public static double stdOfPopulationVariance(double[] dataset) {
                return squareRoot(populationVariance(dataset));
            }

            /**
             * Q1 = ((n + 1)/4)th term
             * Q2 = ((n + 1)/2)th term
             * Q3 = (3(n + 1)/4)th term
             */
            public static double[] quartiles(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final int n = dataset.length;
                final double q1 = interpolate(dataset, q1rank(n));
                final double q2 = interpolate(dataset, q2rank(n));
                final double q3 = interpolate(dataset, q3rank(n));
                return new double[]{q1, q2, q3};
            }

            /**
             * @param dataset The sorting isn't necessary
             * @return range = max value - min value
             */
            public static double range(double[] dataset) {
                Objects.requireNonNull(dataset);
                final double min = Arrays.stream(dataset).min().orElse(0);
                final double max = Arrays.stream(dataset).max().orElse(0);
                return max - min;
            }

            /**
             * @param dataset The sorting isn't necessary
             * @return midrange = (min value + max value) / 2
             */
            public static double midrange(double[] dataset) {
                Objects.requireNonNull(dataset);
                final double min = Arrays.stream(dataset).min().orElse(0);
                final double max = Arrays.stream(dataset).max().orElse(0);
                return (min + max) / 2;
            }

            /**
             * @return rank = (k / 100) × (n + 1)
             */
            public static double percentileRank(int datasetSize, double percentile) {
                return percentile / 100 * (datasetSize + 1);
            }

            /**
             * It tells you roughly whether the value is low or high in the particular dataset.
             * 100th is the highest percentile rank. It means that all other values are
             * less than or equal to this number.
             * Where:
             * PR — Percentile rank — it can take values from 0 to 100;
             * L — Number of values from the set A that are lower than or equal to the data value xₘ;
             * N — Total number of values in the set A.
             *
             * @param number Find percentile rank of
             * @return PR = L / N × 100
             */
            public static double percentileRank(double[] dataset, double number) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final int index = Arrays.binarySearch(dataset, number);
                if (index < 0) {
                    throw new NoSuchElementException("There is no such number in the dataset");
                }
                // Add 1 to accommodate 0-based array indexing
                return (index + 1.) / dataset.length * 100;
            }

            /**
             * (100 - n)%
             *
             * @return kth_percentile = aₘ + fraction_part × (aₘ₊₁ - aₘ)
             */
            public static double kthPercentile(double[] dataset, double percentile) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final double rank = percentileRank(dataset.length, percentile);
                final double[] splitWhole = NumberUtils.splitWholeAndFractionViaModulo(rank);
                // Subtract 1 to accommodate 0-based array indexing, the starting index for dataset is 1
                final int whole = Math.toIntExact((long) splitWhole[Constants.ARR_1ST_INDEX]) - 1;
                final double fractionPart = splitWhole[Constants.ARR_2ND_INDEX];
                return dataset[whole] + fractionPart * (dataset[whole + 1] - dataset[whole]);
            }

            /**
             * Relative standard deviation (RSD)
             *
             * @param mean μ
             * @param std  Standard deviation (σ)
             * @return RSD = (standard deviation / |mean|) * 100%. The result is in %
             */
            public static double relativeStd(double mean, double std) {
                return std / Math.abs(mean) * 100;
            }

            /**
             * ∑ᴺᵢ₌₁(xᵢ−μ)²; ȳ is y-bar
             *
             * @return SS = ∑ⁿᵢ₌₁(yᵢ−ȳ)²
             */
            public static double sumOfSquares(double[] dataset) {
                return sumOfPowers(dataset, 2);
            }

            /**
             * @return ∑ⁿᵢ₌₁(xᵢ−x̄)³
             */
            public static double sumOfCubes(double[] dataset) {
                return sumOfPowers(dataset, 3);
            }

            /**
             * @return ∑ⁿᵢ₌₁(xᵢ−x̄)^power
             */
            private static double sumOfPowers(double[] dataset, int power) {
                final double mean = mean(dataset);
                double sum = 0;
                for (double datapoint : dataset) {
                    final double difference = Math.pow(datapoint - mean, power);
                    sum += difference;
                }
                return sum;
            }

            /**
             * eᵢ = xᵢ- yᵢ
             * Matrix formula: SSE = |e|² = e ∙ e = eᵀe.
             * where:
             * e is the column-vector;
             * eᵀ is the transpose of e, i.e., a row-vector.
             *
             * @return SSE = Σᵢ(xᵢ- yᵢ)²
             */
            public static double sumOfSquaredErrors(double[] predictedValues, double[] actualValues) {
                Objects.requireNonNull(predictedValues);
                Objects.requireNonNull(actualValues);
                checkSameDimensions(predictedValues, actualValues);
                double sumOfSquaredErrors = 0;
                for (int i = 0; i < predictedValues.length; i++) {
                    sumOfSquaredErrors += Math.pow(actualValues[i] - predictedValues[i], 2);
                }
                return sumOfSquaredErrors;
            }

            /**
             * @return SSE = Σᵢ(xᵢ- μ)²
             */
            public static double sumOfSquaredErrors(double[] dataset, double sampleMean) {
                Objects.requireNonNull(dataset);
                double sumOfSquaredErrors = 0;
                for (double datapoint : dataset) {
                    sumOfSquaredErrors += Math.pow(datapoint - sampleMean, 2);
                }
                return sumOfSquaredErrors;
            }

            /**
             * @return SSE = MSE × n
             */
            public static double sumOfSquaredErrors(double mse, int datasetSize) {
                return mse * datasetSize;
            }

            /**
             * The coefficient of skewness measures the asymmetry of the distribution.
             * <ul>
             *     <li>A negative value of skewness implies a skew to the left. It means that the left tail of
             *     the probability density graph is longer than the right one;</li>
             *     <li>A positive value of skewness implies a skew to the right. The right tail of
             *     the probability density graph is longer than the left one;</li>
             *     <li>Skewness = 0 means that the data is symmetrical, with no bias to left or right;</li>
             *     <li>The distributions with a skewness between -0.5 and 0.5 are approximately symmetric;</li>
             *     <li>The distributions with a skewness between -1 and -0.5 and 0.5 and 1 are moderately skewed;</li>
             *     <li>The distributions with a skewness lower than -1 or higher than 1 are substantially skewed.</li>
             * </ul>
             *
             * @return skewness = Σ(xₙ − x̄)³ × N / [(N − 2) × (N − 1) × s³]
             */
            public static double skewness(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final double std = stdOfSampleVariance(dataset);
                final double sumOfCubes = sumOfCubes(dataset);
                final int n = dataset.length;
                return sumOfCubes * n / ((n - 2) * (n - 1) * Math.pow(std, 3));
            }

            /**
             * The coefficient of kurtosis measures the "tailedness" of the distribution.
             * <ul>
             *     <li>A positive value of kurtosis means that the dataset is more "peak-heavy"
             *     (more peaked) than the normal distribution;</li>
             *     <li>A negative value of kurtosis means that the dataset is less "tail-heavy"
             *     (flatter) than the normal distribution;</li>
             *     <li>The distributions with kurtosis greater than 1 are too peaked;</li>
             *     <li>The distributions with kurtosis lower than -1 are too flat.</li>
             * </ul>
             *
             * @return kurtosis = Σ(xₙ − x̄)⁴ × N × (N + 1) / [(N − 1) × (N − 2) × (N − 3)× s⁴] − 3 × (N − 1)²
             * / [(N − 2) × (N − 3)]
             */
            public static double kurtosis(double[] dataset) {
                Objects.requireNonNull(dataset);
                Arrays.sort(dataset);
                final int n = dataset.length;
                final double sumOfQuads = sumOfPowers(dataset, 4);
                final double std = stdOfSampleVariance(dataset);
                return sumOfQuads * n * (n + 1)
                    / ((n - 1) * (n - 2) * (n - 3) * Math.pow(std, 4))
                    - 3 * Math.pow(n - 1., 2)
                    / ((n - 2) * (n - 3));
            }

            /**
             * The Pearson correlation (aka Pearson's r) measures the strength and direction of the linear relation
             * between two random variables, or bivariate data. It detects only a linear relationship!
             * The sign of the Pearson correlation gives the direction of the relationship:
             * <ul>
             *     <li>If r is positive, it means that as one variable increases, the other tends to increase as well;
             *     </li>
             *     <li>If r is negative, then one variable tends to decrease as the other increases.</li>
             * </ul>
             * The absolute value gives the strength of the relationship:
             * <ul>
             *     <li>Pearson's r ranges from −1 to +1;</li>
             *     <li>The closer it is to ±1, the stronger the relationship between the variables;</li>
             *     <li>If r equals −1 or +1, then the linear fit is perfect: all data points lie on one line;</li>
             *     <li>If r equal 0, it means that no linear relationship is present in the data.</li>
             * </ul>
             * The absolute value of r:
             * <ul>
             *     <li>0.8≤∣r∣≤1.0 very strong</li>
             *     <li>0.6≤∣r∣<0.8 strong</li>
             *     <li>0.4≤∣r∣<0.6 moderate</li>
             *     <li>0.2≤∣r∣<0.4 weak</li>
             *     <li>0.0≤∣r∣<0.2 very weak</li>
             * </ul>
             * rₓᵧ = Cov(X,Y) / (sd(X)⋅sd(Y))
             * rₓᵧ = (∑ⁿᵢ₌₁(xᵢ − x̄)(yᵢ − ȳ)) / (√∑ⁿᵢ₌₁(xᵢ − x̄)² * √∑ⁿᵢ₌₁(yᵢ − ȳ)²)
             *
             * @param independentVariables x
             * @param dependentVariables   y
             * @return rₓᵧ = (∑ⁿᵢ₌₁(xᵢyᵢ) − nx̄ȳ) / (√(∑ⁿᵢ₌₁(xᵢ²) − nx̄²) * √(∑ⁿᵢ₌₁(yᵢ²) − nȳ²))
             */
            public static double pearsonCorrelation(double[] independentVariables, double[] dependentVariables) {
                Objects.requireNonNull(independentVariables);
                Objects.requireNonNull(dependentVariables);
                checkSameDimensions(independentVariables, dependentVariables);
                final double xMean = mean(independentVariables);
                final double yMean = mean(dependentVariables);
                final double numerator = LinearAlgebra.dotProduct(independentVariables, dependentVariables)
                    - (independentVariables.length * xMean * yMean);
                final double xSumOfSquares = sumOfSquares(independentVariables);
                final double ySumOfSquares = sumOfSquares(dependentVariables);
                return numerator / squareRoot(xSumOfSquares * ySumOfSquares);
            }

            /**
             * It operates on the scale from -1 to +1.
             * +1 describes a perfect prediction.
             * 0 doesn't give you any valid information.
             * -1 represents a complete inconsistency between prediction and outcome.
             *
             * @param tp true positives
             * @param fp false positives
             * @param tn true negatives
             * @param fn false negatives
             * @return MCC = ((TP × TN) - (FP × FN)) / √((TP + FP)(TP + FN)(TN + FP)(TN + FN))
             */
            public static double matthewsCorrelationCoefficient(double tp, byte fp, double tn, double fn) {
                return ((tp * tn) - (fp * fn)) / squareRoot((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn));
            }

            /**
             * ρ = Cov(r(X),r(Y)) / (sd(r(X))⋅sd(r(Y)))
             * <br/>
             * Alternative formula: ρ = 1 − 6/(n(n²−1)) * ∑ⁿᵢ₌₁ dᵢ²
             * Where:
             * dᵢ — Difference in ranks between xᵢ and yᵢ, that is dᵢ = r(xᵢ)−r(yᵢ).
             * ⚠️ If there are identical values (ties), the above formula is incorrect!
             * Meaning:
             * <ul>
             *     <li>Has positive value if the two variables tend to increase (or decrease) simultaneously.
             *     The higher the value of one variable, the higher the value of the other variable.</li>
             *     <li>Has negative value if one variable tends to increases as the other variables decreases.
             *     The higher the value of one variable, the lower the value of the other variable.</li>
             *     <li>Equals 1 if there is a perfect increasing relationship, so if one variable increases,
             *     then the other also increases (with 100% probability).</li>
             *     <li>Equals -1 if there is a perfect decreasing relationship, so if one variable increases,
             *     then the other decreases (with 100% probability).</li>
             *     <li>Is close to zero if the monotone relationship between the two variables is weak.</li>
             *     <li>Is far from zero if the monotone relationship between the two variables is strong.</li>
             *     <li>Equals zero if there is no monotonic relationship between the variables. However, this doesn't
             *     mean that there is no relationship whatsoever - there can be some other type of relationship.</li>
             * </ul>
             * <ul>
             *     <li>0.8≤∣ρ∣≤1.0 very strong;</li>
             *     <li>0.6≤∣ρ∣<0.8 strong;</li>
             *     <li>0.4≤∣ρ∣<0.6 moderate;</li>
             *     <li>0.2≤∣ρ∣<0.4 weak;</li>
             *     <li>0.0≤∣ρ∣<0.2 very weak.</li>
             * </ul>
             *
             * @param independentVariables x
             * @param dependentVariables   y
             * @return The result using an alternative formula
             */
            public static double spearmansRankCorrelation(double[] independentVariables, double[] dependentVariables) {
                Objects.requireNonNull(independentVariables);
                Objects.requireNonNull(dependentVariables);
                checkSameDimensions(independentVariables, dependentVariables);
                final double[] xRanks = getRanks(independentVariables);
                final double[] yRanks = getRanks(dependentVariables);
                double sum = 0;
                final int n = independentVariables.length;
                for (int i = 0; i < n; i++) {
                    final double diff = xRanks[i] - yRanks[i];
                    sum += diff * diff;
                }
                return 1 - 6. / (n * (n * n - 1)) * sum;
            }

            /**
             * Assigns ranks to an array of unique values.
             * Smallest value = Rank 1, Largest = Rank N.
             */
            public static double[] getRanks(double[] dataset) {
                final int n = dataset.length;
                final double[] ranks = new double[n];

                final double[] sorted = dataset.clone();
                Arrays.sort(sorted);

                // Map values to their rank (position in sorted array + 1)
                // Using a Map for O(1) lookup after sorting
                final var rankMap = new HashMap<Double, Integer>();
                for (int i = 0; i < n; i++) {
                    rankMap.put(sorted[i], i + 1);
                }

                // Populate the rank array based on original order
                for (int i = 0; i < n; i++) {
                    ranks[i] = rankMap.get(dataset[i]);
                }

                return ranks;
            }

            /**
             * Mean absolute deviation.
             *
             * @param centralPoint The central point (m) is the reference value used to measure how far each number
             *                     is from the center. You can choose the mean, median, mode.
             * @return MAD = 1/n * ∑ⁿᵢ₌₁ ∣xᵢ−m∣
             */
            public static double mad(double[] dataset, double centralPoint) {
                Objects.requireNonNull(dataset);
                double sum = 0;
                for (var datapoint : dataset) {
                    sum += Math.abs(datapoint - centralPoint);
                }
                return Arithmetic.reciprocal(dataset.length) * sum;
            }

            /**
             * The standard error of the mean (SEM); s/√N
             *
             * @return √(∑ᵢ(xᵢ−x̄)²/ (N(N−1)))
             */
            public static double stdError(double[] dataset) {
                Objects.requireNonNull(dataset);
                final double ss = sumOfSquares(dataset);
                final int n = dataset.length;
                return squareRoot(ss / (n * (n - 1)));
            }

            /**
             * Relative standard error
             *
             * @return RSE = (Standard Error/Sample Mean)×100. The result is in %
             */
            public static double rse(double sampleMean, double stdError) {
                return stdError / sampleMean * 100;
            }

            /**
             * Evaluate the uncertainty (or error) of any mathematical operation containing uncertain quantities.
             * Error propagation occurs when you measure some quantities X and Y with uncertainties ΔX and ΔY.
             * Then you want to calculate some other quantity Z using the measurements of X and Y.
             * It turns out that the uncertainties ΔX and ΔY will propagate to the uncertainty of Z.
             * Addition: Z = X+Y
             * Subtraction: Z = X-Y
             *
             * @param uncertaintyInX ΔX
             * @param uncertaintyInY ΔY
             * @return ΔZ = √(ΔX²+ΔY²). The inaccuracy in the final parameter Z
             */
            public static double errorPropagationViaAddition(double uncertaintyInX, double uncertaintyInY) {
                return squareRoot(uncertaintyInX * uncertaintyInX + uncertaintyInY * uncertaintyInY);
            }

            /**
             * Multiplication: Z = X*Y
             * Division: Z = X/Y
             *
             * @return ΔZ = Z*√((ΔX/X)²+(ΔY/Y)²). The inaccuracy in the final parameter Z
             */
            public static double errorPropagationViaMultiplication(double x, double y, double uncertaintyInX,
                                                                   double uncertaintyInY) {
                final double z = x * y;
                return z * squareRoot(Math.pow(uncertaintyInX / x, 2) + Math.pow(uncertaintyInY / y, 2));
            }

            /**
             * The value of Simpson’s index reflects how many different types of species are in a community and
             * how evenly distributed the population of each species is.
             * The Simpson’s index D is the probability that any two individuals randomly selected from an infinitely
             * large community will belong to the same species.
             * As D increases, diversity decreases.
             * where:
             * nᵢ — Number of individuals in the i-th species;
             * N — Total number of individuals in the community.
             *
             * @param population Population data for each species
             * @return D = (∑nᵢ(nᵢ−1)) / (N(N−1))
             */
            public static double simpsonIndex(double[] population) {
                Objects.requireNonNull(population);
                checkNonEmpty(population);
                double total = 0;
                double speciesAbundance = 0;
                for (double species : population) {
                    total += species;
                    speciesAbundance += species * (species - 1);
                }
                final double denominator = total * (total - 1);
                return speciesAbundance / denominator;
            }

            /**
             * aka Simpson’s index of diversity.
             * Measures the probability that two randomly selected individuals belong to different species.
             * A high score indicates high diversity, and a low score indicates low diversity.
             * When the diversity index is zero, the community contains only one species (i.e., no diversity).
             * As the number of different species increases and the population distribution of species becomes
             * more even, the diversity index increases and approaches one.
             *
             * @return Gini-Simpson index = 1-D. The index score is between 0 and 1
             */
            public static double simpsonDiversityIndex(double simpsonIndex) {
                return 1 - simpsonIndex;
            }

            /**
             * @return Inverse Simpson index = 1/D
             */
            public static double simpsonReciprocalIndex(double simpsonIndex) {
                return Arithmetic.reciprocal(simpsonIndex);
            }

            /**
             * Standard Deviation Index
             * <ol>
             *     <li>If SDI = 0, it indicates that the laboratory mean and the consensus group mean are equal,
             *     and this is a favorable condition that indicates bias is equal to zero.</li>
             *     <li>If SDI > 0 but <= 1, it indicates that the laboratory mean and the consensus group mean are
             *     closer to each other, and this is somewhat a favorable condition that indicates less bias.</li>
             *     <li>SDI value that is greater than 1 but is =< 1.25.
             *     It indicates that the laboratory mean lies within the acceptable limit.</li>
             *     <li>SDI value is greater than 1.25 but is =<1.49. It indicates that the value may be considered
             *     acceptable but an examination of the test model may be helpful.</li>
             *     <li>SDI value between 1.50 and 1.99. It indicates that the test case should be examined.
             *     This is a marginal case and indicates a bias.</li>
             *     <li>SDI >= 2 means that the performance of the test is unacceptable and remedial action is required.
             *     </li>
             * </ol>
             *
             * @param laboratoryMean     The mean of the test model
             * @param consensusGroupMean The mean of the population
             * @param consensusGroupStd  The standard deviation of the population
             * @return SDI = (Laboratory mean − Consensus group mean) / Consensus group standard deviation
             */
            public static double sdi(double laboratoryMean, double consensusGroupMean, double consensusGroupStd) {
                return (laboratoryMean - consensusGroupMean) / consensusGroupStd;
            }

            /**
             * M = (a+b)/2
             * μ = ∑(MᵢFᵢ)/n
             * σ² = (∑(FᵢMᵢ²)−(nμ²)) / (n−1)
             *
             * @param dataset [min, max, frequency]
             * @return σ = √σ². The standard deviation for given frequency distribution table
             */
            public static double groupedDataStd(double[][] dataset) {
                Objects.requireNonNull(dataset);
                final double totalSamples = Arrays.stream(dataset)
                    .mapToDouble(row -> row[Constants.ARR_3RD_INDEX])
                    .sum();
                double mean = 0; // sample
                double frequencyMidpointProdSum = 0;
                for (var row : dataset) {
                    final double min = row[Constants.ARR_1ST_INDEX];
                    final double max = row[Constants.ARR_2ND_INDEX];
                    final double frequency = row[Constants.ARR_3RD_INDEX];
                    final double midpoint = Geometry.midpoint(min, max);
                    mean += midpoint * frequency / totalSamples;
                    frequencyMidpointProdSum += frequency * midpoint * midpoint;
                }
                final double sampleVariance = (frequencyMidpointProdSum - (totalSamples * mean * mean))
                    / (totalSamples - 1);
                return squareRoot(sampleVariance);
            }
        }

        public static final class Inferential {
            private Inferential() {
            }

            /**
             * It tells you how many standard deviations a data point is above or below the mean.
             * A positive z-score means the data point is greater than the mean,
             * while a negative z-score means that it is less than the mean.
             * A z-score of 1 means that the data point is exactly 1 standard deviation above the mean.
             *
             * @param experimentalResult x
             * @return z = (x - μ) / σ
             */
            public static double zScore(double experimentalResult, double sampleMean, double standardDeviation) {
                return (experimentalResult - sampleMean) / standardDeviation;
            }
        }

        public static final class Regression {
            private Regression() {
            }

            /**
             * Linear model: y = b + ax
             * β is the column vector of the linear regression coefficients: β = (XᵀX)⁻¹Xᵀy
             * X = |1 x₁|
             * |1 x₂|
             * |... ...|
             * |1 xₙ|
             * <br/>
             * The coefficient a is the slope of the regression line. It describes how much the dependent variable
             * y changes (on average!) when the dependent variable x changes by one unit.
             * a × (x + 1) + b = (a × x + b) + a = y + a
             * If a > 0, then y increases by a units whenever x increases by 1 unit. There is a positive relationship
             * between the two variables: as one increases, the other increases as well.
             * If a < 0, then y decreases by a units whenever x increases by 1 unit. There is a negative relationship
             * between the two variables: as one increases, the other decreases.
             * If a = 0, then there is no relationship between the two variables in question:
             * the value of y is the same (constant) for all values of x.
             * The slope a can be expressed in terms of the standard deviations of x and y and
             * of their Pearson correlation: a = corr(x, y) ⋅ std(y) / std(x)
             *
             * @param independentVariables Observed values [x₁, x₂, ... xₙ]
             * @param dependentVariables   [y₁, y₂, ... yₙ]
             * @return Fitted coefficients: [a, b]
             */
            public static double[] linearRegression(double[] independentVariables, double[] dependentVariables) {
                Objects.requireNonNull(independentVariables);
                Objects.requireNonNull(dependentVariables);
                final double[][] x = new double[independentVariables.length][];
                for (int i = 0; i < x.length; i++) {
                    x[i] = new double[]{1, independentVariables[i]};
                }
                final double[][] xTransposed = LinearAlgebra.transposeMatrix(x);
                final double[][] xMultiplied = LinearAlgebra.matrixMultiply(xTransposed, x);
                final double[][] xInversed = LinearAlgebra.matrixInverse(xMultiplied);
                final double[][] xInversedProdTransposed = LinearAlgebra.matrixMultiply(xInversed, xTransposed);
                return LinearAlgebra.matrixMultiply(xInversedProdTransposed, dependentVariables);
            }
        }

        public static final class Distribution {
            private Distribution() {
            }

            /**
             * @return P(X=r) = nCr * pʳ * (1 - p)ⁿ⁻ʳ
             */
            public static double binomialDistribution(
                long numberOfEvents, long numberOfRequiredSuccesses, double probabilityOfOneSuccess) {
                if (numberOfEvents < 0 || numberOfRequiredSuccesses < 0) {
                    throw new IllegalArgumentException(
                        "Number of events and number of required successes must be non-negative."
                    );
                }
                if (probabilityOfOneSuccess < 0 || probabilityOfOneSuccess > 1) {
                    throw new IllegalArgumentException("Probability of one success must be between 0 and 1.");
                }
                if (numberOfRequiredSuccesses > numberOfEvents) {
                    throw new IllegalArgumentException(
                        "Number of required successes cannot exceed the number of events.");
                }

                final long numberOfCombinations = Combinatorics.combinations(numberOfEvents, numberOfRequiredSuccesses);

                return numberOfCombinations * Math.pow(probabilityOfOneSuccess, numberOfRequiredSuccesses) *
                    Math.pow(1 - probabilityOfOneSuccess, (double) numberOfEvents - numberOfRequiredSuccesses);
            }

            /**
             * @return P(X = x) = e^(-λ) * λˣ / x!
             */
            public static double poissonDistribution(long numberOfOccurrences, double rateOfSuccess) {
                if (numberOfOccurrences < 0) {
                    throw new IllegalArgumentException("numberOfOccurrences must be non-negative.");
                }
                if (rateOfSuccess < 0) {
                    throw new IllegalArgumentException("rateOfSuccess must be non-negative.");
                }

                return Math.pow(Math.E, -rateOfSuccess) * Math.pow(rateOfSuccess, numberOfOccurrences)
                    / Arithmetic.factorial(numberOfOccurrences);
            }

            /***
             * The probability density function (PDF)
             * @return P(x) = 1 / √(2π) * e^(-(x - μ)² / (2σ²)) / σ
             */
            public static double normalDistribution(double mean, double standardDeviation, double rawScoreValue) {
                checkGreater0(standardDeviation);
                final double multiplier = Arithmetic.reciprocal(squareRoot(Trigonometry.PI2));
                final double exponent = -Math.pow(rawScoreValue - mean, 2) / (2 * Math.pow(standardDeviation, 2));
                return multiplier * Math.exp(exponent) / standardDeviation;
            }

            /***
             * Calculates the probability mass function (PMF)
             * @return P = (1 - p)ˣ * p
             */
            public static double geometricDistributionPMF(long numberOfFailures, double probabilityOfSuccess) {
                if (numberOfFailures < 0) {
                    throw new IllegalArgumentException("numberOfFailures must be non-negative.");
                }
                if (probabilityOfSuccess <= 0 || probabilityOfSuccess > 1) {
                    throw new IllegalArgumentException("probabilityOfSuccess must be in the range (0, 1].");
                }

                return Math.pow(1 - probabilityOfSuccess, numberOfFailures) * probabilityOfSuccess;
            }
        }
    }

    public static final class Analysis {
    }

    public static final class NumberTheory {
    }

    public static final class Combinatorics {
        private Combinatorics() {
        }

        /**
         * Focus on the selection of items (where the order does not matter).
         *
         * @return ₙCᵣ = n! / (r!(n - r)!). Number of possible combinations (without repetitions).
         */
        public static long combinations(long totalObjects, long sampleSize) {
            checkCombinationInputs(totalObjects, sampleSize);
            final long numerator = Arithmetic.factorial(totalObjects);
            final long denominator = Arithmetic.factorial(sampleSize)
                * Arithmetic.factorial(totalObjects - sampleSize);
            return numerator / denominator;
        }

        private static void checkNonNegativeCombinationInputs(long totalObjects, long sampleSize) {
            if (totalObjects < 0 || sampleSize < 0) {
                throw new IllegalArgumentException("Inputs must be non-negative.");
            }
        }

        private static void checkCombinationInputs(long totalObjects, long sampleSize) {
            checkNonNegativeCombinationInputs(totalObjects, sampleSize);
            if (totalObjects == 0 && sampleSize > 0) {
                throw new IllegalArgumentException(
                    "Cannot choose combinations with replacement from zero objects.");
            }
        }

        /**
         * @return Cᴿ(n, r) = (n + r - 1)! / (r!(n - 1)!). Number of possible combinations (with repetitions).
         */
        public static long combinationsWithReplacement(long totalObjects, long sampleSize) {
            checkCombinationInputs(totalObjects, sampleSize);
            final long numerator = Arithmetic.factorial(totalObjects + sampleSize - 1);
            final long denominator = Arithmetic.factorial(sampleSize) * Arithmetic.factorial(totalObjects - 1);
            return numerator / denominator;
        }

        /**
         * Focus on the order of arrangement (where the sequence matters).
         *
         * @return ₙPᵣ = n! / ((n - r)!). Number of possible permutations (without repetitions).
         */
        public static long permutations(long totalObjects, long sampleSize) {
            checkNonNegativeCombinationInputs(totalObjects, sampleSize);
            if (sampleSize > totalObjects) {
                throw new IllegalArgumentException("sampleSize cannot be greater than totalObjects.");
            }

            final long numerator = Arithmetic.factorial(totalObjects);
            final long denominator = Arithmetic.factorial(totalObjects - sampleSize);
            return numerator / denominator;
        }

        /**
         * @return Pᴿ(n, r) = nʳ. Number of possible permutations (with repetitions).
         */
        public static double permutationsWithReplacement(long totalObjects, long sampleSize) {
            checkNonNegativeCombinationInputs(totalObjects, sampleSize);
            return Math.pow(totalObjects, sampleSize);
        }
    }

    public static final class Probability {
        private Probability() {
        }

        /**
         * @return (tp + tn) / (tp + tn + fp + fn). The result is in % on the scale 0-1
         */
        public static double accuracy(double truePositive, double trueNegative,
                                      double falsePositive, double falseNegative) {
            return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
        }

        /**
         * @param sensitivity aka recall
         * @return (Sensitivity × Prevalence) + (Specificity × (1 − Prevalence))
         */
        public static double accuracy(double sensitivity, double specificity, double prevalence) {
            return (sensitivity * prevalence) + (specificity * (1 - prevalence));
        }

        /**
         * @return (|(Vo − Va)|/Va) × 100
         */
        public static double percentError(double observedValue, double acceptedValue) {
            return Math.abs(observedValue - acceptedValue) / acceptedValue * 100;
        }

        /**
         * @return tp / (tp + fp). The result is in % on the scale 0-1
         */
        public static double precision(double truePositive, double falsePositive) {
            return truePositive / (truePositive + falsePositive);
        }

        /**
         * aka sensitivity or true positive rate.
         *
         * @return tp / (tp + fn). The result is in % on the scale 0-1
         */
        public static double recall(double truePositive, double falseNegative) {
            return truePositive / (truePositive + falseNegative);
        }

        /**
         * aka true negative rate or selectivity.
         *
         * @return TN / (TN + FP). The result is in % on the scale 0-1
         */
        public static double specificity(double trueNegative, double falsePositive) {
            return trueNegative / (trueNegative + falsePositive);
        }

        /**
         * The result is in % on the scale 0-1
         */
        public static double f1Score(double precision, double recall) {
            return 2 * precision * recall / (precision + recall);
        }

        /**
         * @return F1 score = (2 × TP) / (2 × TP + FN + FP). The result is in % on the scale 0-1
         */
        public static double f1Score(double truePositive, double falsePositive, double falseNegative) {
            return 2 * truePositive / (2 * truePositive + falseNegative + falsePositive);
        }

        /**
         * @param loss The loss if failure occurs
         * @return risk = probability × loss
         */
        public static double risk(double failureProbabilityPercent, double loss) {
            return failureProbabilityPercent / 100 * loss;
        }

        /**
         * If the relative risk is equal to 1, it means that there is no difference in the risk between the two groups.
         * If the relative risk is lower than 1, it means that the risk is lower in the exposed group.
         * If the relative risk is higher than 1, it means that the risk is higher in the exposed group.
         *
         * @param exposedGroupDisease   a — Number of members of the exposed group who developed the disease
         * @param exposedGroupNoDisease b — Number of members of the exposed group who didn't develop the disease
         * @param controlGroupDisease   c — Number of members of the control group who developed the disease
         * @param controlGroupNoDisease d — Number of members of the control group who didn't develop the disease
         * @return RR = (a / (a + b)) / (c / (c + d))
         */
        public static double relativeRisk(double exposedGroupDisease, double exposedGroupNoDisease,
                                          double controlGroupDisease, double controlGroupNoDisease) {
            return exposedGroupDisease / (exposedGroupDisease + exposedGroupNoDisease)
                / (controlGroupDisease / (controlGroupDisease + controlGroupNoDisease));
        }

        /**
         * @return lower bound = exp[ln(RR) - Zc × √(1/a + 1/c - 1/(a + b) - 1/(c + d))]
         */
        public static double lowerConfidenceBound(double exposedGroupDisease, double exposedGroupNoDisease,
                                                  double controlGroupDisease, double controlGroupNoDisease,
                                                  double zScore) {
            final double rr = relativeRisk(exposedGroupDisease, exposedGroupNoDisease,
                controlGroupDisease, controlGroupNoDisease);
            return Math.exp(Algebra.ln(rr) - zScore
                * squareRoot(Arithmetic.reciprocal(exposedGroupDisease) + Arithmetic.reciprocal(controlGroupDisease)
                - Arithmetic.reciprocal(exposedGroupDisease + exposedGroupNoDisease)
                - Arithmetic.reciprocal(controlGroupDisease + controlGroupNoDisease))
            );
        }

        /**
         * @return upper bound = exp[ln(RR) + Zc × √(1/a + 1/c - 1/(a + b) - 1/(c + d))]
         */
        public static double upperConfidenceBound(double exposedGroupDisease, double exposedGroupNoDisease,
                                                  double controlGroupDisease, double controlGroupNoDisease,
                                                  double zScore) {
            final double rr = relativeRisk(exposedGroupDisease, exposedGroupNoDisease,
                controlGroupDisease, controlGroupNoDisease);
            return Math.exp(Algebra.ln(rr) + zScore
                * squareRoot(Arithmetic.reciprocal(exposedGroupDisease) + Arithmetic.reciprocal(controlGroupDisease)
                - Arithmetic.reciprocal(exposedGroupDisease + exposedGroupNoDisease)
                - Arithmetic.reciprocal(controlGroupDisease + controlGroupNoDisease))
            );
        }
    }

    public static final class Topology {
    }

    public static final class SetTheory {
    }

    public static final class Logic {
    }

    public static final class Discrete {
    }

    public static final class Applied {
    }
}
