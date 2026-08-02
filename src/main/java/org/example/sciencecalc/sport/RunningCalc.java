package org.example.sciencecalc.sport;

public class RunningCalc {
    private RunningCalc() {
    }

    /**
     * <a href="https://www.omnicalculator.com/sports/running-pace">Calculator</a>
     * speed = time / distance. Units: m/s, km/h
     *
     * @return pace = distance / time. Units: m/s, km/h
     */
    public static double runningPace(double distanceRunMeters, double timeSpentSeconds) {
        return distanceRunMeters / timeSpentSeconds;
    }
}
