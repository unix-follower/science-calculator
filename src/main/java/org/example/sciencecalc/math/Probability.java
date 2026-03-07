package org.example.sciencecalc.math;

import static org.example.sciencecalc.math.Algebra.squareRoot;

public final class Probability {
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

    /**
     * Probability of single events. A and B both occurring.
     *
     * @param probabilityA range 0-1
     * @param probabilityB range 0-1
     * @return P(A∩B) = P(A|B) * P(B) or P(A∩B) = P(B|A) * P(A)
     */
    public static double intersectionProbability(double probabilityA, double probabilityB) {
        return probabilityA * probabilityB;
    }

    /**
     * Probability of single events. At least one of the event occurs
     *
     * @param probabilityA range 0-1
     * @param probabilityB range 0-1
     * @return P(A∪B) = P(A) + P(B) - P(A∩B)
     */
    public static double unionProbability(double probabilityA, double probabilityB) {
        return probabilityA + probabilityB - intersectionProbability(probabilityA, probabilityB);
    }

    /**
     * Probability of single events. Exactly one of these events occurs.
     *
     * @param probabilityA range 0-1
     * @param probabilityB range 0-1
     * @return P(A∆B) = P(A) + P(B) - 2P(A∩B)
     */
    public static double symmetricDifferenceProbability(double probabilityA, double probabilityB) {
        return probabilityA + probabilityB - 2 * intersectionProbability(probabilityA, probabilityB);
    }

    /**
     * Probability of single events. Neither A nor B occurs.
     *
     * @param probabilityA range 0-1
     * @param probabilityB range 0-1
     * @return P((A∪B)') = 1 - P(A∪B)
     */
    public static double complementProbability(double probabilityA, double probabilityB) {
        return 1 - unionProbability(probabilityA, probabilityB);
    }

    /**
     * Probability of single events. A NOT occurring. The same formula is for B NOT occurring
     *
     * @param probabilityA range 0-1
     * @return P(A') = 1 - P(A) or P(B') = 1 - P(B)
     */
    public static double complementAProbability(double probabilityA) {
        return 1 - probabilityA;
    }

    /**
     * Probability of the series of events. A always occurs
     *
     * @param probabilityA range 0-1
     * @return P(A)ⁿ
     */
    public static double probabilityOfSeriesAAlwaysOccurs(double probabilityA, int numberOfTrials) {
        return Math.pow(probabilityA, numberOfTrials);
    }

    /**
     * Probability of the series of events. A never occurs
     *
     * @param probabilityA range 0-1
     * @return (1 - P(A))ⁿ
     */
    public static double probabilityOfSeriesANeverOccurs(double probabilityA, int numberOfTrials) {
        return Math.pow(1 - probabilityA, numberOfTrials);
    }

    /**
     * Probability of the series of events. A occurs at least once
     *
     * @param probabilityA range 0-1
     * @return 1 - (1 - P(A))ⁿ
     */
    public static double probabilityOfSeriesAOccursAtLeastOnce(double probabilityA, int numberOfTrials) {
        return 1 - Math.pow(1 - probabilityA, numberOfTrials);
    }

    /**
     * Reversed: P(B|A) = (P(A|B) × P(B)) / P(A). Probability of B under the condition A.
     * Where:
     * P(A), P(B) – Probability of event A and even B occurring, respectively;
     * P(A|B) – Conditional probability of event A occurring given that B has happened;
     * P(B|A) – Conditional probability of event B occurring given that A has happened.
     *
     * @param probabilityA                range 0-1
     * @param probabilityB                range 0-1
     * @param probabilityBUnderConditionA range 0-1
     * @return P(A|B) = (P(B|A) × P(A)) / P(B). Probability of A under the condition B.
     */
    public static double bayesTheorem(double probabilityA, double probabilityB, double probabilityBUnderConditionA) {
        return probabilityBUnderConditionA * probabilityA / probabilityB;
    }

    /**
     * For the independent events
     *
     * @param probabilityA range 0-1
     * @param probabilityB range 0-1
     * @return P(A and B) = P(A) × P(B)
     */
    public static double jointProbability(double probabilityA, double probabilityB) {
        return probabilityA * probabilityB;
    }

    /**
     * @param probabilityA       range 0-1
     * @param probabilityAGivenB range 0-1
     * @return P(A and B) = P(A) × P(A|B)
     */
    public static double jointProbabilityOfDependentEvents(double probabilityA, double probabilityAGivenB) {
        return probabilityA * probabilityAGivenB;
    }
}
