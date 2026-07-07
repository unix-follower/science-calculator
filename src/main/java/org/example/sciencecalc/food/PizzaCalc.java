package org.example.sciencecalc.food;

import org.example.sciencecalc.math.Algebra;
import org.example.sciencecalc.physics.LengthUnit;

public class PizzaCalc {
    // Effusivity (e) = √(k * rho * c)
    private static final double EFFUSIVITY_STEEL = 14000.0; // J / (m² · K · s^0.5)

    // Thermal diffusivity of pizza dough (alpha = k / (rho * c))
    private static final double ALPHA_DOUGH = 0.14e-6; // m² / s
    private static final double DOUGH_THERMAL_CONDUCTIVITY = 0.5; // W/(m·K)

    private PizzaCalc() {
    }

    /**
     * a₁ × τ + a₂ × √τ = a₃ × d
     *
     * @param ovenTemp                  Preheated oven temperature in °C
     * @param doughTemp                 Initial dough temperature in °C
     * @param thickness                 Thickness of the pizza in cm
     * @param doughDensity              in kg/m³
     * @param doughSpecificHeatCapacity in J/(kg·K)
     */
    public static double[] perfectPizza(
        double ovenTemp,
        double doughTemp,
        double thickness,
        double doughDensity,
        double doughSpecificHeatCapacity
    ) {
        final double doughEffusivity = Algebra.squareRoot(DOUGH_THERMAL_CONDUCTIVITY * doughDensity
            * doughSpecificHeatCapacity); // J / (m² · K · s^0.5)
        // T_interface = (e_steel * T_steel + e_dough * T_dough) / (e_steel + e_dough)
        final double interfaceTemp = (EFFUSIVITY_STEEL * ovenTemp + doughEffusivity * doughTemp)
            / (EFFUSIVITY_STEEL + doughEffusivity); // pizza bottom temp

        final double thicknessMeters = LengthUnit.centimetersToMeters(thickness);
        final double structuralFactor = 0.251;
        // t = (thickness²) / (4 * alpha * factor)
        final double heatingTimeSeconds = Math.round(
            thicknessMeters * thicknessMeters / (4 * ALPHA_DOUGH * structuralFactor));

        return new double[]{heatingTimeSeconds, interfaceTemp};
    }
}
