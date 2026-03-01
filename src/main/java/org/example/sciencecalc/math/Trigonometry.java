package org.example.sciencecalc.math;

import static org.example.sciencecalc.math.Constants.DIVISION_BY_ZERO;

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
public final class Trigonometry {
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
