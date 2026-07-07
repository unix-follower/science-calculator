package org.example.sciencecalc.food;

import org.example.sciencecalc.physics.DensityUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PizzaCalcTest {
    private static final double DELTA1 = 0.1;

    @Test
    void testPerfectPizza() {
        // given
        final short preheatedSteelOvenTemp = 230;
        final byte doughTemp = 21;
        final double thickness = 0.5;

        final double doughDensity = DensityUnit.gramsPerCubicCmToKgPerCubicMeter(0.7); //  g/cm³
        final double doughSpecificHeatCapacity = 2250; // J/(kg·K)
        // when
        final double[] bakingResults = PizzaCalc.perfectPizza(
            preheatedSteelOvenTemp, doughTemp, thickness, doughDensity, doughSpecificHeatCapacity);
        // then
        assertNotNull(bakingResults);
        assertEquals(2, bakingResults.length);
        assertEquals(178, bakingResults[0], DELTA1); // seconds, 2 min 58 sec
        assertEquals(217.5, bakingResults[1], DELTA1); // ≈ Pizza bottom temperature °C
    }
}
