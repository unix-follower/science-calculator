package org.example.sciencecalc.physics;

public enum InductanceUnit {
    HENRIES, // H
    MILLIHENRIES, // mH
    MICROHENRIES, // μH
    NANOJOULES; // nH

    public static final double ONE_MICROHENRY_IN_HENRIES = 1e-6;

    public static double microHenriesToHenries(double microHenries) {
        return microHenries * ONE_MICROHENRY_IN_HENRIES;
    }

    public static double milliHenriesToHenries(double milliHenries) {
        return milliHenries / 1000;
    }
}
