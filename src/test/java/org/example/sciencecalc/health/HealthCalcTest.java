package org.example.sciencecalc.health;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthCalcTest {
    private static final double DELTA1 = 0.1;

    @Nested
    class BodyMeasurements {
        @Test
        void testAbsi() {
            // given
            final double height = 1.7;
            final double weight = 59;
            final double waistCircumference = 0.73;
            // when
            final double result = HealthCalc.BodyMeasurements.absi(height, weight, waistCircumference);
            // then
            assertEquals(0.07495, result, 0.00001);
        }

        @Test
        void testBodyFat() {
            // given
            final byte age = 32;
            final byte sex = 0;
            final double height = 1.7;
            final byte weight = 57;
            final double bodyFatWeight = 7.7;
            // when
            final double result = HealthCalc.BodyMeasurements.bodyFat(age, sex, height, weight);
            // then
            assertEquals(13.5, result, DELTA1);
            assertEquals(bodyFatWeight, weight * (result / 100), DELTA1);
        }
    }
}
