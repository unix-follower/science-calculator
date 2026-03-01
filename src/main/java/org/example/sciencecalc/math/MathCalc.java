package org.example.sciencecalc.math;

import java.util.Objects;

import static org.example.sciencecalc.math.Algebra.squareRoot;
import static org.example.sciencecalc.math.Arithmetic.GOLDEN_RATIO;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;

public final class MathCalc {
    private MathCalc() {
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

    public static final class Analysis {
    }

    public static final class NumberTheory {
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
