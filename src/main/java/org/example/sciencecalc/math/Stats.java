package org.example.sciencecalc.math;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.example.sciencecalc.math.Algebra.squareRoot;
import static org.example.sciencecalc.math.LinAlgUtils.checkSameDimensions;
import static org.example.sciencecalc.math.NumberUtils.checkGreater0;

public final class Stats {
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
         * x̄ is X-bar.
         *
         * @return Sample Mean = x̄ = 1/n * ∑ⁿᵢ₌₁(xᵢ) = ∑xᵢ / n = (x₁ + x₂ + ... + xₙ) / n
         */
        public static double mean(double[] dataset) {
            Objects.requireNonNull(dataset);
            Arrays.sort(dataset);
            return Arrays.stream(dataset).sum() / dataset.length;
        }

        /**
         * @return Sample Mean = x̄² = 1/n * ∑ⁿᵢ₌₁(xᵢ²) = ∑xᵢ / n = (x₁ + x₂ + ... + xₙ) / n
         */
        public static double meanOfSquares(double[] dataset) {
            Objects.requireNonNull(dataset);
            Arrays.sort(dataset);
            return Arrays.stream(dataset).map(x -> x * x).sum() / dataset.length;
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
         * @return Sₓᵧ = ∑ⁿᵢ₌₁(xᵢ−x̄)(yᵢ−ȳ)
         */
        public static double sumOfProducts(double[] independentVariables, double[] dependentVariables,
                                           double meanX, double meanY) {
            Objects.requireNonNull(independentVariables);
            Objects.requireNonNull(dependentVariables);
            checkSameDimensions(independentVariables, dependentVariables);
            double sum = 0;
            for (int i = 0; i < independentVariables.length; i++) {
                final double x = independentVariables[i];
                final double y = dependentVariables[i];
                final double prod = (x - meanX) * (y - meanY);
                sum += prod;
            }
            return sum;
        }

        /**
         * @return Sₓ²ₓ² = ∑ⁿᵢ₌₁(xᵢ²−x̄²)²
         */
        public static double sumOfSquaredDeviationsOfSquares(double[] independentVariables, double mean) {
            Objects.requireNonNull(independentVariables);
            double sum = 0;
            for (double x : independentVariables) {
                sum += Math.pow(x * x - mean * mean, 2);
            }
            return sum;
        }

        /**
         * @return Sₓ²ᵧ = ∑ⁿᵢ₌₁(xᵢ²−x̄²)(yᵢ−ȳ)
         */
        public static double sumOfProductsOfDeviations(double[] independentVariables, double meanX,
                                                       double[] dependentVariables, double meanY) {
            Objects.requireNonNull(independentVariables);
            Objects.requireNonNull(dependentVariables);
            checkSameDimensions(independentVariables, dependentVariables);
            double sum = 0;
            for (int i = 0; i < independentVariables.length; i++) {
                final double x = independentVariables[i];
                final double y = dependentVariables[i];
                sum += (x * x - meanX * meanX) * (y - meanY);
            }
            return sum;
        }

        /**
         * Between a variable and its own square.
         *
         * @return Sₓₓ² = ∑ⁿᵢ₌₁(xᵢ−x̄)²(xᵢ²−x̄²)
         */
        public static double sumOfProductsOfDeviations(double[] independentVariables, double mean) {
            Objects.requireNonNull(independentVariables);
            double sum = 0;
            for (double x : independentVariables) {
                sum += Math.pow(x - mean, 2) * (x * x - mean * mean);
            }
            return sum;
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
         * SSE = ∑(yᵢ - ŷᵢ)²
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
         * @return SST = ∑(yᵢ - ȳ)²
         */
        public static double totalSumOfSquares(double[] dependentVariables, double dependentVarsSampleMean) {
            return sumOfSquaredErrors(dependentVariables, dependentVarsSampleMean);
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
            assert denominator > 0;
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

        /**
         * The constant of proportionality is the same as a slope. It is defined as the m within the y = mx + c.
         * It can be negative.
         *
         * @param independentVariable x
         * @param dependentVariable   y
         * @return constant of proportionality = Y / X
         */
        public static double constantOfProportionality(double independentVariable, double dependentVariable) {
            return dependentVariable / independentVariable;
        }

        /**
         * Exactly r successes: P(X = r)
         * r or more successes: P(X ≥ r)
         * r or fewer successes: P(X ≤ r)
         * Between r₀ and r₁ successes P(r₀ ≤ X ≤ r₁)
         *
         * @param numberOfEvents          n
         * @param numberOfSuccesses       r
         * @param probabilityOfOneSuccess p
         * @return P(X=r) = nCr * pʳ * (1 - p)ⁿ⁻ʳ
         */
        public static double binomialDistribution(
            long numberOfEvents, long numberOfSuccesses, double probabilityOfOneSuccess) {
            NumberUtils.checkGreater(numberOfEvents, -1);
            NumberUtils.checkGreater(numberOfSuccesses, -1);
            if (probabilityOfOneSuccess < 0 || probabilityOfOneSuccess > 1) {
                throw new IllegalArgumentException("Probability of one success must be between 0 and 1.");
            }
            if (numberOfSuccesses > numberOfEvents) {
                throw new IllegalArgumentException(
                    "Number of required successes cannot exceed the number of events.");
            }

            final long numberOfCombinations = Combinatorics.combinations(numberOfEvents, numberOfSuccesses);

            return numberOfCombinations * Math.pow(probabilityOfOneSuccess, numberOfSuccesses) *
                Math.pow(1 - probabilityOfOneSuccess, (double) numberOfEvents - numberOfSuccesses);
        }

        /**
         * @param probabilityOfSuccessPerEvent range 0-1
         * @return μ = np
         */
        public static double binomialDistributionMean(int numOfEvents, double probabilityOfSuccessPerEvent) {
            return numOfEvents * probabilityOfSuccessPerEvent;
        }

        /**
         * @param probabilityOfSuccessPerEvent range 0-1
         * @return σ² = np(1-p)
         */
        public static double binomialDistributionVariance(int numOfEvents, double probabilityOfSuccessPerEvent) {
            return numOfEvents * probabilityOfSuccessPerEvent * (1 - probabilityOfSuccessPerEvent);
        }

        /**
         * @param numOfEvents             n
         * @param numOfSuccesses          r
         * @param probabilityOfOneSuccess p. Range 0-1
         * @return P(Y=n) = (n−1, r−1)×pᶦ×(1−p)ⁿ⁻ᶦ
         */
        public static double negativeBinomialDistribution(int numOfEvents, int numOfSuccesses,
                                                          double probabilityOfOneSuccess) {
            final long combinations = Combinatorics.combinations(numOfEvents - 1L, numOfSuccesses - 1L);
            return combinations * Math.pow(probabilityOfOneSuccess, numOfSuccesses)
                * Math.pow(1 - probabilityOfOneSuccess, (double) numOfEvents - numOfSuccesses);
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
         * @return P(x) = 1 / √(2π) * e^(-(x - μ)² / (2σ²)) / σ
         */
        public static double normalDistribution(double mean, double standardDeviation, double rawScoreValue) {
            checkGreater0(standardDeviation);
            final double multiplier = Arithmetic.reciprocal(squareRoot(Trigonometry.PI2));
            final double exponent = -Math.pow(rawScoreValue - mean, 2) / (2 * Math.pow(standardDeviation, 2));
            return multiplier * Math.exp(exponent) / standardDeviation;
        }

        /**
         * @param numOfFailures        x. Before success
         * @param probabilityOfSuccess of one trial. Range 0-1
         * @return P = (1-p)ˣ * p
         */
        public static double geometricDistribution(int numOfFailures, double probabilityOfSuccess) {
            return Math.pow(1 - probabilityOfSuccess, numOfFailures) * probabilityOfSuccess;
        }

        /**
         * @param probabilityOfSuccess of one trial. Range 0-1
         * @return μ = (1-p)/p
         */
        public static double geometricDistributionMean(double probabilityOfSuccess) {
            return (1 - probabilityOfSuccess) / probabilityOfSuccess;
        }

        /**
         * @param probabilityOfSuccess of one trial. Range 0-1
         * @return σ² = (1-p)/p²
         */
        public static double geometricDistributionVariance(double probabilityOfSuccess) {
            return (1 - probabilityOfSuccess) / (probabilityOfSuccess * probabilityOfSuccess);
        }

        /**
         * @return χ² = (observed value - expected value)² / expected value
         */
        public static double chiSquare(double observedValue, double expectedValue) {
            return Math.pow(observedValue - expectedValue, 2) / expectedValue;
        }

        /**
         * mean: μ = 1/a
         * median: m = ln(2)/a
         * variance: σ² = 1/a²
         * standard deviation: σ = √(1/a²)
         *
         * @param rateParameter     a
         * @param timeBetweenEvents X
         * @return P(x > X) = exp(-ax). The probability of x being higher than the indicated value X
         */
        public static double exponentialDistributionHigher(double rateParameter, double timeBetweenEvents) {
            return Math.exp(-rateParameter * timeBetweenEvents);
        }

        /**
         * @param rateParameter     a
         * @param timeBetweenEvents X
         * @return P(x ≤ X) = 1 − exp(-ax). The probability of x being lower than the indicated value X
         */
        public static double exponentialDistributionLower(double rateParameter, double timeBetweenEvents) {
            return 1 - Math.exp(-rateParameter * timeBetweenEvents);
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

        /**
         * @param independentVariables x
         * @param dependentVariables   y
         * @return SSR = ∑(ŷᵢ - ȳ)²
         */
        public static double regressionSumOfSquares(double[] independentVariables, double[] dependentVariables,
                                                    double sampleMeanY) {
            final double[] line = Geometry.leastSquaresRegressionLine(independentVariables, dependentVariables);
            final double slope = line[Constants.ARR_1ST_INDEX];
            final double intercept = line[Constants.ARR_2ND_INDEX];
            double ssr = 0;
            for (double x : independentVariables) {
                final double yHat = slope * x + intercept;
                ssr += Math.pow(yHat - sampleMeanY, 2);
            }
            return ssr;
        }

        /**
         * Y ~ aX + b
         * SST = SSR + SSE
         * R² = 1 - SSE / SST
         * R² = SSR / (SSR + SSE)
         * The range is between 0 and 1.
         * If R² = 1, then we have a perfect fit, which means that the values of Y are fully determined
         * (i.e., without any error) by the values of X, and all data points lie precisely
         * at the estimated line of best fit.
         * If R² = 0, then the model is no better at predicting the values of Y
         * than the model which always returns the average value of Y as a prediction.
         *
         * @param independentVariables x
         * @param dependentVariables   y
         * @return R² = SSR / SST
         */
        public static double coefficientOfDetermination(double[] independentVariables, double[] dependentVariables) {
            final double meanY = Descriptive.mean(dependentVariables);
            final double sst = Descriptive.totalSumOfSquares(dependentVariables, meanY);
            final double ssr = regressionSumOfSquares(independentVariables, dependentVariables, meanY);
            assert sst > 0;
            return ssr / sst;
        }

        /**
         * y = a × bˣ, where a ≠ 0 and b > 0, b ≠ 1.
         * a is the value predicted by the exponential regression model for x = 0;
         * If b > 1, the exponential fit describes an exponential growth;
         * If 0 < b < 1, the exponential fit describes an exponential decay.
         * <br/>
         * ln(y) = ln(a × bˣ)
         * ln(y) = ln(a) + ln(bˣ)
         * ln(y) = ln(a) + x × ln(b)
         *
         * @param independentVariables x
         * @param dependentVariables   y
         * @return b = exp(m) a = exp(c)
         */
        public static double[] exponentialRegression(double[] independentVariables, double[] dependentVariables) {
            Objects.requireNonNull(independentVariables);
            Objects.requireNonNull(dependentVariables);
            checkSameDimensions(independentVariables, dependentVariables);
            // Transform dependent variables to ln(y)
            final double[] lnY = new double[dependentVariables.length];
            for (int i = 0; i < dependentVariables.length; i++) {
                lnY[i] = Algebra.ln(dependentVariables[i]);
            }
            // Fit linear regression: ln(y) = intercept + slope * x
            final double[] coeffs = Geometry.leastSquaresRegressionLine(independentVariables, lnY);
            final double slope = coeffs[Constants.ARR_1ST_INDEX];
            final double intercept = coeffs[Constants.ARR_2ND_INDEX];
            // a = exp(intercept), b = exp(slope)
            return new double[]{Math.exp(intercept), Math.exp(slope)};
        }

        /**
         * Cubic model: y = a + bx + cx² + dx³
         * β = (XᵀX)⁻¹Xᵀy
         * X = |1 x₁ x₁² x₁³|
         * |1 x₂ x₂² x₂³|
         * |... ...|
         * |1 xₙ xₙ² xₙ³|
         *
         * @param independentVariables x
         * @param dependentVariables   y
         * @return Fitted coefficients: [a, b, c, d]
         */
        public static double[] cubicRegression(double[] independentVariables, double[] dependentVariables) {
            Objects.requireNonNull(independentVariables);
            Objects.requireNonNull(dependentVariables);
            final double[][] matrix = new double[independentVariables.length][];
            for (int i = 0; i < matrix.length; i++) {
                final double x = independentVariables[i];
                matrix[i] = new double[]{1, x, x * x, x * x * x};
            }
            final double[][] xTransposed = LinearAlgebra.transposeMatrix(matrix);
            final double[][] xMultiplied = LinearAlgebra.matrixMultiply(xTransposed, matrix);
            final double[][] xInversed = LinearAlgebra.matrixInverse(xMultiplied);
            final double[][] xInversedProdTransposed = LinearAlgebra.matrixMultiply(xInversed, xTransposed);
            return LinearAlgebra.matrixMultiply(xInversedProdTransposed, dependentVariables);
        }

        /**
         * Polynomial model: y = a₀ + a₁x + a₂x² + ... + aₙxⁿ
         * β = (XᵀX)⁻¹Xᵀy
         * For degree n, you need at least n+1 data points. If you have exactly n+1 points,
         * then the fit will be perfect, i.e., the curve will go through every point.
         * It may happen that the polynomial regression cannot be fitted.
         *
         * @param independentVariables x
         * @param dependentVariables   y
         */
        public static double[] polynomialRegression(double[] independentVariables, double[] dependentVariables) {
            Objects.requireNonNull(independentVariables);
            Objects.requireNonNull(dependentVariables);
            final int size = independentVariables.length;
            final int degree = size - 2; // Fit degree n-2 for n data points
            final double[][] matrix = new double[size][degree + 1];
            for (int i = 0; i < size; i++) {
                final double x = independentVariables[i];
                final double[] row = new double[degree + 1]; // NOPMD
                for (int j = 0; j <= degree; j++) {
                    row[j] = Math.pow(x, j);
                }
                matrix[i] = row;
            }
            final double[][] xTransposed = LinearAlgebra.transposeMatrix(matrix);
            final double[][] xMultiplied = LinearAlgebra.matrixMultiply(xTransposed, matrix);
            final double[][] xInversed = LinearAlgebra.matrixInverse(xMultiplied);
            final double[][] xInversedProdTransposed = LinearAlgebra.matrixMultiply(xInversed, xTransposed);
            return LinearAlgebra.matrixMultiply(xInversedProdTransposed, dependentVariables);
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
}
