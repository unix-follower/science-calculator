package org.example.sciencecalc.finance;

import org.example.sciencecalc.finance.GeneralInvestmentCalc.CompoundingFrequency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneralInvestmentTest {
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
}
