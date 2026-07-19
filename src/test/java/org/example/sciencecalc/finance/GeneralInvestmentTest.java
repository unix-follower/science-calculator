package org.example.sciencecalc.finance;

import org.example.sciencecalc.finance.GeneralInvestmentCalc.CompoundingFrequency;
import org.example.sciencecalc.math.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeneralInvestmentTest {
    private static final double DELTA1 = 0.1;
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
}
