package org.example.sciencecalc.math;

public final class Combinatorics {
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
