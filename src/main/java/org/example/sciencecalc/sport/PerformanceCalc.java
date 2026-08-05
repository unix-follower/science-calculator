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
}
