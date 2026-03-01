package org.example.sciencecalc.math;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static org.example.sciencecalc.math.Algebra.squareRoot;
import static org.example.sciencecalc.math.Constants.DIVISION_BY_ZERO;
import static org.example.sciencecalc.math.LinAlgUtils.checkSameDimensions;
import static org.example.sciencecalc.math.LinAlgUtils.round4InPlace;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;

public final class LinearAlgebra {
    private static final double EPSILON_NEGATIVE10 = 1e-10;

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
        LinAlgUtils.check2dSize(vectorA);
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
