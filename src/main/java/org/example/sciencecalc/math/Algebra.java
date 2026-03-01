package org.example.sciencecalc.math;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.example.sciencecalc.math.NumberUtils.checkGreater;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;

public final class Algebra {
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
        checkGreater(base, 1);
        return logChangeOfBase(a, base) + logChangeOfBase(b, base);
    }

    /**
     * @return logₙ(a / b) = logₙ(a) - logₙ(b)
     */
    public static double logQuotientRule(double a, double b, double base) {
        checkGreater0(a);
        checkGreater0(b);
        checkGreater(base, 1);
        return logChangeOfBase(a, base) - logChangeOfBase(b, base);
    }

    /**
     * @return logₙ(aᵏ) = k * logₙ(a)
     */
    public static double logPowerRule(double number, double exponent, double base) {
        checkGreater0(number);
        checkGreater0(exponent);
        checkGreater(base, 1);
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
        checkGreater(base, 1);
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
        checkGreater(base, 1);
        return logChangeOfBase(Math.pow(a, exponentX) / Math.pow(b, exponentY), base);
    }

    /**
     * @return x * logₙ a = logₙ(aˣ)
     */
    public static double logMultiplyNumber(double number, double exponent, double base) {
        checkGreater0(number);
        checkGreater(base, 1);
        return logChangeOfBase(Math.pow(number, exponent), base);
    }

    /**
     * @return logₐ(x) = log(x) / log(a)
     */
    public static double logChangeOfBase(double x, double base) {
        checkGreater0(x);
        checkGreater(base, 1);
        return log(x) / log(base);
    }

    /**
     * @return logₐ(x) = log_b(x) / log_b(a)
     */
    public static double logChangeOfBase(double x, double base, double newBase) {
        checkGreater0(x);
        checkGreater(base, 1);
        checkGreater(newBase, 1);
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
        checkGreater(base, 1);
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
        checkGreater(base, 1);
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
