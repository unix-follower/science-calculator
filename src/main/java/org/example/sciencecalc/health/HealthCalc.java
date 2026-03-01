package org.example.sciencecalc.health;

public final class HealthCalc {
    private HealthCalc() {
    }

    public static final class BodyMeasurements {
        private BodyMeasurements() {
        }

        /**
         * @return BMI = weight/height². The units are kg/m²
         */
        public static double bmi(double heightMeters, double weightKg) {
            return weightKg / (heightMeters * heightMeters);
        }

        /**
         * @return ABSI = WC/(BMI^(2/3) * height^½). The units are kg/m²
         */
        public static double absi(double heightInM, double weightInKg, double waistCircumferenceInM) {
            final double bodyMassIdx = bmi(heightInM, weightInKg);
            return waistCircumferenceInM / (Math.pow(bodyMassIdx, 2. / 3) * Math.pow(heightInM, 0.5));
        }

        /**
         * @return ABSI_(z score) = (ABSI - ABSI_mean)/ABSI_sd
         */
        public static double absiZScore(double absi, double absiMean, double absiStandardDeviation) {
            return (absi - absiMean) / absiStandardDeviation;
        }

        /**
         * The calculation method of American Diabetes Association (most common).
         *
         * @param age in years
         * @param sex male = 0, female = 1
         * @return Body fat % = –44.988 + (0.503 × age) + (10.689 × sex) + (3.172 × BMI) – (0.026 × BMI²)
         * + (0.181 × BMI × sex) – (0.02 × BMI × age) – (0.005 × BMI² × sex) + (0.00021 × BMI² × age)
         */
        public static double bodyFat(double age, double sex, double heightMeters, double weightKg) {
            final double bmi = bmi(heightMeters, weightKg);
            final double sqBmi = bmi * bmi;
            return -44.988 + (0.503 * age) + (10.689 * sex) + (3.172 * bmi) - (0.026 * sqBmi)
                + (0.181 * bmi * sex) - (0.02 * bmi * age) - (0.005 * sqBmi * sex) + (0.00021 * sqBmi * age);
        }
    }
}
