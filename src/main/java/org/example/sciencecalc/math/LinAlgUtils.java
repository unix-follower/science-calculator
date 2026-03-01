package org.example.sciencecalc.math;

import static org.example.sciencecalc.math.Constants.MISMATCHED_DIMENSIONS;

public final class LinAlgUtils {
    private LinAlgUtils() {
    }

    public static void check2dSize(double[] vector) {
        if (vector == null || vector.length != 2) {
            throw new IllegalArgumentException("The 2d vector is required");
        }
    }

    public static void checkSameDimensions(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
        }
    }

    public static void checkSameDimensions(double[][] matrixA, double[][] matrixB) {
        if (matrixA.length != matrixB.length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
        }

        if (matrixA[Constants.ARR_1ST_INDEX].length != matrixB[Constants.ARR_1ST_INDEX].length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
        }
    }

    public static void checkSameDimensions(double[][] matrix, double[] vector) {
        if (matrix[Constants.ARR_1ST_INDEX].length != vector.length) {
            throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
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
}
