package org.example.sciencecalc.sport;

public class SportCalc {
    private SportCalc() {
    }

    public static class CaloriesBurned {
        private CaloriesBurned() {
        }

        /**
         * MET (Metabolic Equivalent of Task).
         * Categories:
         * <ol>
         *     <li>Light-intensity activities (MET below 3);</li>
         *     <li>Moderate-intensity activities (MET from 3 to 6);</li>
         *     <li>Vigorous-intensity activities (MET above 6).</li>
         * </ol>
         * <table>
         *     <tr><th>Activity</th><th>MET</th></tr>
         *     <tr><td>Climbing up stairs slowly</td><td>4</td></tr>
         *     <tr><td>Climbing up stairs fast</td><td>8.8</td></tr>
         *     <tr><td>Climbing a ladder</td><td>8</td></tr>
         *     <tr><td>Descending stairs</td><td>3.5</td></tr>
         *     <tr><td>Carrying a load upstairs (1-15lb / 0.5-7kg)</td><td>5</td></tr>
         *     <tr><td>Carrying a load upstairs (15-24lb / 7-11kg)</td><td>6</td></tr>
         *     <tr><td>Carrying a load upstairs (24-49lb / 11-22kg)</td><td>8</td></tr>
         *     <tr><td>Carrying a load upstairs (49-74lb / 22-34kg)</td><td>10</td></tr>
         *     <tr><td>Carrying a load upstairs (> 74lb / 34kg)</td><td>12</td></tr>
         * </table>
         *
         * @return t × (MET × w × 3.5)/200 kcal. The units are kcal
         */
        public static double stairsCalorie(double met, double bodyWeightKg, double timeElapsedMinutes) {
            final double burningPerMinute = met * bodyWeightKg * 3.5 / 200; // kcal/min
            return timeElapsedMinutes * burningPerMinute;
        }
    }
}
