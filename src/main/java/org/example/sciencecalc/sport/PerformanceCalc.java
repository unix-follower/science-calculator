package org.example.sciencecalc.sport;

import org.example.sciencecalc.health.BodyMeasurementsCalc;

public final class PerformanceCalc {
    private PerformanceCalc() {
    }

    /**
     * <a href="https://www.omnicalculator.com/sports/target-heart-rate">Calculator</a>
     *
     * @param age              in years.
     * @param restingHeartRate in bpm (beats per minute).
     * @param intensityPercent in %
     * @return Target heart rate in bpm.
     */
    public static double targetHeartRate(int age, double restingHeartRate, double intensityPercent) {
        final double intensity = intensityPercent / 100;
        final double reserve = BodyMeasurementsCalc.maximumHeartRate(age) - restingHeartRate;
        return restingHeartRate + intensity * reserve;
    }

    /**
     * <a href="https://www.omnicalculator.com/sports/max-heart-rate">Calculator</a>
     *
     * @param age in years.
     * @return In BPM.
     */
    public static double maxHeartRateHaskellFox(int age) {
        return 220 - age;
    }

    public static double maxHeartRateInbar(int age) {
        return 205.8 - (0.685 * age);
    }

    public static double maxHeartRateNes(int age) {
        return 211 - (0.64 * age);
    }

    public static double maxHeartRateOaklandNonlinear(int age) {
        return 192 - (0.007 * age * age);
    }

    public static double maxHeartRateTanakaMonahanSeals(int age) {
        return 208 - (0.7 * age);
    }

    /**
     * <a href="https://www.omnicalculator.com/sports/heart-rate-recovery">Calculator</a>
     *
     * @param maxHRAfterExercise Maximum heart rate after the exercise
     * @param hrAfter1MinuteRest Heart rate after 1 minute of rest
     * @return in bpm (beats per minute).
     */
    public static double heartRateRecovery(int maxHRAfterExercise, int hrAfter1MinuteRest) {
        return maxHRAfterExercise - hrAfter1MinuteRest;
    }
}
