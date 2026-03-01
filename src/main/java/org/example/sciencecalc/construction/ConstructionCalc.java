package org.example.sciencecalc.construction;

import org.example.sciencecalc.physics.MassUnit;

public class ConstructionCalc {
    private ConstructionCalc() {
    }

    public static class Materials {
        private Materials() {
        }

        /**
         * @param volume  in m³
         * @param density in kg/m³
         * @return total weight = volume × density × number of pieces
         */
        public static double aluminumWeight(double volume, double density, long numOfMetalPieces) {
            return MassUnit.weight(volume, density) * numOfMetalPieces;
        }
    }
}
