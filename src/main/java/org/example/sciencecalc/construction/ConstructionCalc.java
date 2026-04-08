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

        /**
         * @return number of balusters needed = (railing length - (number of posts × single post's width))
         * / (baluster width + baluster spacing)
         */
        public static double balusters(double railingLengthM, int numOfPosts, double postWidthM,
                                       double balusterWidthM, double balusterSpacingM) {
            return (railingLengthM - (numOfPosts * postWidthM)) / (balusterWidthM + balusterSpacingM);
        }
    }
}
