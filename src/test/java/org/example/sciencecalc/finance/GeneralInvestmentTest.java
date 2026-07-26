package org.example.sciencecalc.finance;

import org.example.sciencecalc.finance.GeneralInvestmentCalc.CompoundingFrequency;
import org.example.sciencecalc.math.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeneralInvestmentTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA5 = 0.00001;

    @Test
    void testCompoundInterestRate() {
        // given
        final double initialBalance = 30.04;
        final double finalBalance = 50.47;
        final double term = 10.0; // 10 years (120 months)
        final CompoundingFrequency frequency = CompoundingFrequency.MONTHLY;
        // when
        final double rate = GeneralInvestmentCalc.compoundInterestRate(initialBalance, finalBalance, term, frequency);
        // then
        assertEquals(0.051995, rate, DELTA5);
    }

    @Test
    void testCompoundGrowth() {
        // given
        final short initialDeposit = 1000;
        final double interestRate = 0.08; // 8%
        final byte term = 20; // 20 years (240.164 months)
        final CompoundingFrequency frequency = CompoundingFrequency.MONTHLY;
        // when
        final double[] results = GeneralInvestmentCalc.compoundGrowth(initialDeposit, interestRate, term, frequency);
        // then
        assertNotNull(results);
        assertEquals(2, results.length);
        final double finalBalance = results[Constants.ARR_1ST_INDEX];
        assertEquals(4926.8, finalBalance, DELTA1);
        final double totalCompoundGrowth = results[Constants.ARR_2ND_INDEX];
        assertEquals(3926.8, totalCompoundGrowth, DELTA1);
    }

    @Test
    void testHoldingPeriodReturn() {
        // given
        final byte boughtPrice = 100;
        final byte currentPrice = 120;
        final double dividendIncomePerShare = 7.5;
        // when
        final double[] results = GeneralInvestmentCalc
            .holdingPeriodReturn(boughtPrice, currentPrice, dividendIncomePerShare);
        // then
        assertNotNull(results);
        assertEquals(3, results.length);
        final double capitalGainsYield = results[Constants.ARR_1ST_INDEX];
        assertEquals(0.2, capitalGainsYield, DELTA1);
        final double dividendYield = results[Constants.ARR_2ND_INDEX];
        assertEquals(0.075, dividendYield, DELTA3);
        final double holdingPeriodReturn = results[Constants.ARR_3RD_INDEX];
        assertEquals(0.275, holdingPeriodReturn, DELTA3);
    }

    @Test
    void testDiscountRate() {
        // given
        final short presentValue = 1000;
        final short futureValue = 2000;
        final byte term = 10;
        // when
        final double[] results = GeneralInvestmentCalc
            .discountRate(presentValue, futureValue, term, CompoundingFrequency.MONTHLY);
        // then
        assertNotNull(results);
        assertEquals(2, results.length);
        final double annualDiscountRate = results[Constants.ARR_1ST_INDEX];
        assertEquals(0.06952, annualDiscountRate, DELTA5);
        final double periodicDiscountRate = results[Constants.ARR_2ND_INDEX];
        assertEquals(0.00579, periodicDiscountRate, DELTA3);
    }

    @Test
    void testInvestmentFees() {
        // given
        final short initialInvestmentAmount = 10_000;
        final double salesLoad = 0.02; // 2%
        final double annualReturn = 0.1;
        final double annualOperatingFees = 0.02; // 2%
        final byte investmentDuration = 10;
        final double turnoverCost = 0.03; // 3%
        final double redemptionFees = 0.02; // 2%
        // when
        final double[] results = GeneralInvestmentCalc.investmentFees(
            initialInvestmentAmount, salesLoad, annualReturn, annualOperatingFees, investmentDuration, turnoverCost,
            redemptionFees);
        // then
        assertNotNull(results);
        assertEquals(6, results.length);
        final double investedAmount = results[Constants.ARR_1ST_INDEX];
        assertEquals(9800, investedAmount, DELTA1);
        final double effectiveReturn = results[Constants.ARR_2ND_INDEX];
        assertEquals(0.08, effectiveReturn, DELTA2);
        final double fundValueBeforeRedemption = results[Constants.ARR_3RD_INDEX];
        assertEquals(20_863.46, fundValueBeforeRedemption, DELTA2);
        final double finalFundValue = results[Constants.ARR_4TH_INDEX];
        assertEquals(20_446.2, finalFundValue, DELTA1);
        final double fundValueWithoutFees = results[Constants.ARR_5TH_INDEX];
        assertEquals(25_937.42, fundValueWithoutFees, DELTA2);
        final double totalFees = results[Constants.ARR_6TH_INDEX];
        assertEquals(5491.23, totalFees, DELTA2);
    }
}
