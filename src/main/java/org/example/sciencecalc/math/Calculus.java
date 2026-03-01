package org.example.sciencecalc.math;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static org.example.sciencecalc.math.Algebra.squareRoot;
import static org.example.sciencecalc.math.Arithmetic.ONE_HALF;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;
import static org.example.sciencecalc.math.NumberUtils.checkNotEq0;
import static org.example.sciencecalc.math.NumberUtils.checkNotEqTo;

/**
 * Derivative notations:
 * <ul>
 *     <li>Lagrange's: f'</li>
 *     <li>Leibniz's: dy/dx; y = f(x) → d/dx f(x)</li>
 *     <li>Newton's: ẏ</li>
 * </ul>
 */
public final class Calculus {
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
