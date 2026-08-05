package org.example.sciencecalc.health;

public final class BodyMeasurementsCalc {
    private BodyMeasurementsCalc() {
    }

    /**
     * <a href="https://www.omnicalculator.com/health/karvonen-formula">Calculator</a>
     */
    public static double maximumHeartRate(int age) {
        return 220 - age;
    }
}
