package org.example.sciencecalc.physics;

public enum ElectricCurrentUnit {
    AMPERE, // A
    MILLIAMPERES, // mA
    MICROAMPERES; // μA

    public static double milliAmperesToAmperes(double milliAmperes) {
        return milliAmperes / 1000;
    }
}
