package org.example.sciencecalc.finance;

import org.example.sciencecalc.math.Algebra;
import org.example.sciencecalc.math.Arithmetic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeneralInvestmentCalc {
    public enum CompoundingFrequency {
        YEARLY(1.0), // (1/Yr)
        SEMI_ANNUALLY(2.0), // (2/Yr)
        QUARTERLY(4.0), // (4/Yr)
        BI_MONTHLY(6.0), // (6/Yr)
        MONTHLY(12.0), // (12/Yr)
        BI_WEEKLY_AVG(26.0), // average
        BI_WEEKLY(365.25 / 14.0), // (26/Yr)
        WEEKLY_AVG(52.0),
        WEEKLY(365.25 / 7.0), // (52/Yr)
        DAILY_AVG(360.0),
        DAILY_360(365.0), // (360/Yr)
        DAILY_365(365.25), // (365/Yr)
        CONTINUOUS(Double.POSITIVE_INFINITY);

        private final double frequency;

        CompoundingFrequency(double frequency) {
            this.frequency = frequency;
        }

        public double getFrequency() {
            return frequency;
        }
    }

    /**
     * <a href="https://www.omnicalculator.com/finance/compound-interest-rate">Calculator</a>
     * where:
     * PV - Present Value;
     * FV - Future Value;
     * t - term;
     * m - compound frequency in a given term;
     * r - interest rate.
     * <p/>
     * FV = PV × (1 + r/m)^(mt)
     * FV = PV × e^(rt)
     *
     * @param initialBalance Present Value (PV).
     * @param finalBalance   Future Value (FV), equal to the sum of the initial balance and the surplus.
     * @param term           in years
     */
    public static double compoundInterestRate(
        double initialBalance, double finalBalance, double term, CompoundingFrequency compoundingFrequency) {
        if (compoundingFrequency == CompoundingFrequency.CONTINUOUS) {
            // r = ln(FV/PV) × 1/t
            return Algebra.ln(finalBalance / initialBalance) * Arithmetic.reciprocal(term);
        }

        final double m = compoundingFrequency.getFrequency();
        // r = m × ((FV/PV)^(1/(mt)) − 1)
        final double rawRate = m * (Math.pow(finalBalance / initialBalance, Arithmetic.reciprocal(m * term)) - 1.0);
        return BigDecimal.valueOf(rawRate)
            .setScale(6, RoundingMode.HALF_UP)
            .doubleValue();
    }

    /**
     * <a href="https://www.omnicalculator.com/finance/compound-growth">Calculator</a>
     * where:
     * PV - Initial balance or present value;
     * FV - Future value of the initial balance;
     * t - Number of years;
     * m - Number of times the interest is compounded per year;
     * r - Annual interest rate;
     * CG – Compound growth.
     * <p/>
     * FV = PV × (1 + r/m)^(mt)
     * CG = FV − PV
     *
     * @param interestRate Annual rate of interest, in % on the scale [0,1].
     * @param term         in years
     */
    public static double[] compoundGrowth(
        double initialDeposit, double interestRate, double term, CompoundingFrequency frequency) {
        final double m = frequency.getFrequency();
        final double finalBalance = initialDeposit * Math.pow(1 + interestRate / m, m * term);
        final double totalCompoundGrowth = finalBalance - initialDeposit;
        return new double[]{finalBalance, totalCompoundGrowth};
    }

    /**
     * <a href="https://www.omnicalculator.com/finance/holding-period-return">Calculator</a>
     *
     * @return all results in percents on the scale [0, 1]
     */
    public static double[] holdingPeriodReturn(double boughtPrice, double currentPrice, double dividendIncomePerShare) {
        final double capitalGains = currentPrice - boughtPrice;
        final double capitalGainsYield = capitalGains / boughtPrice;
        final double dividendYield = dividendIncomePerShare / boughtPrice;
        final double holdingPeriodReturn = capitalGainsYield + dividendYield;
        return new double[]{capitalGainsYield, dividendYield, holdingPeriodReturn};
    }
}
