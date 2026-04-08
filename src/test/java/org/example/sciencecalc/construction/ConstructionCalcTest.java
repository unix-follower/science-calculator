package org.example.sciencecalc.construction;

import org.example.sciencecalc.math.Arithmetic;
import org.example.sciencecalc.math.Geometry;
import org.example.sciencecalc.physics.LengthUnit;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructionCalcTest {
    private static final double DELTA1 = 0.1;

    @Nested
    class Materials {
        @Test
        void testAluminumWeight() {
            // given aluminum
            final double length = LengthUnit.feetToMeters(8);
            final double width = LengthUnit.feetToMeters(4);
            final double thickness = LengthUnit.inchesToMeters(Arithmetic.ONE_FOURTH);
            final byte pieces = 1;
            final double volume = Geometry.rectangularPrismVolume(length, width, thickness);
            final short density = 2700; // average
            // when
            final double totalWeight = ConstructionCalc.Materials.aluminumWeight(volume, density, pieces);
            // then
            assertEquals(50.97, totalWeight, DELTA1);
        }

        @Test
        void testBalusters() {
            // given
            final double railingLength = 6.096;
            final byte numOfPosts = 5;
            final double postWidth = LengthUnit.centimetersToMeters(5.08);
            final double balusterWidth = LengthUnit.centimetersToMeters(5.08);
            final double balusterSpacing = LengthUnit.centimetersToMeters(7.62);
            // when
            final double balustersNeeded = ConstructionCalc.Materials
                .
                balusters(railingLength, numOfPosts, postWidth, balusterWidth, balusterSpacing);
            // then
            assertEquals(46, balustersNeeded, DELTA1);
        }
    }
}
