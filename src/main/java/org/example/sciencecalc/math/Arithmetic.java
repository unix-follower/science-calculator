package org.example.sciencecalc.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongPredicate;

import static org.example.sciencecalc.math.Algebra.squareRoot;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;

public final class Arithmetic {
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

    /**
     * ϕ=½(1+√5)
     */
    public static final double GOLDEN_RATIO = 1.618033988749895;

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
    public static Map<Long, Integer> primeFactorMap(long number) {
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
