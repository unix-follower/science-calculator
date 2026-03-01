package org.example.sciencecalc.finance;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinanceCalcTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA4 = 0.0001;

    @Nested
    class BusinessPlanning {
        @Test
        void testManHours() {
            // given
            final int people = 2;
            final int hoursPerPerson = 16;
            // when
            final double manHours = FinanceCalc.BusinessPlanning.manHours(people, hoursPerPerson);
            // then
            assertEquals(32, manHours, DELTA1);
        }

        @Test
        void testNumOfPeopleNeeded() {
            // given
            final int manHours = 35;
            final int hoursPerPerson = 16;
            // when
            final double people = FinanceCalc.BusinessPlanning.numOfPeopleNeeded(manHours, hoursPerPerson);
            // then
            assertEquals(2, people, DELTA1);
        }

        @Test
        void testManHoursTotalCostExact() {
            // given
            final int manHours = 35;
            final double hourlyPay = 5.71;
            // when
            final double totalCost = FinanceCalc.BusinessPlanning.manHoursTotalCostExact(manHours, hourlyPay);
            // then
            assertEquals(199.85, totalCost, DELTA2);
        }

        @Test
        void testCostPerPerson() {
            // given
            final double hourlyPay = 5.71;
            final int hoursPerPerson = 16;
            // when
            final double cost = FinanceCalc.BusinessPlanning.costPerPerson(hoursPerPerson, hourlyPay);
            // then
            assertEquals(91.36, cost, DELTA2);
        }

        @Test
        void testHourlyPay() {
            // given
            final double manHours = 35;
            final double totalCost = 199.85;
            // when
            final double pay = FinanceCalc.BusinessPlanning.hourlyPay(totalCost, manHours);
            // then
            assertEquals(5.71, pay, DELTA2);
        }

        @Test
        void testYoutubeGrossRevenuePerDay() {
            // given
            final double rpm = 6.5;
            final int numberOfViews = 13500;
            // when
            final double grossRevenue = FinanceCalc.BusinessPlanning
                .youtubeGrossRevenuePerDay(rpm, numberOfViews);
            // then
            assertEquals(87.75, grossRevenue, DELTA2);
        }

        @Test
        void testYoutubeNetRevenuePerDay() {
            // given
            final double rpm = 6.5;
            final int numberOfViews = 13500;
            // when
            final double netRevenue = FinanceCalc.BusinessPlanning.youtubeNetRevenuePerDay(rpm, numberOfViews);
            // then
            assertEquals(48.26, netRevenue, DELTA2);
            assertEquals(337.84, netRevenue * 7, DELTA2);
        }

        @Test
        void testAverageFixedCost() {
            // given
            final short totalFixedCost = 1000;
            final byte numberOfUnits = 20;
            // when
            final double avgFixedCost = FinanceCalc.BusinessPlanning.averageFixedCost(totalFixedCost, numberOfUnits);
            // then
            assertEquals(50, avgFixedCost, DELTA1);
        }
    }

    @Nested
    class EquityInvestment {
        @Test
        void testDividendYield() {
            // given
            final double koSharePrice = 66.65;
            final double annualDividendPerShare = 2.04;
            // when
            final double result = FinanceCalc.EquityInvestment
                .dividendYield(koSharePrice, annualDividendPerShare);
            // then
            assertEquals(3.06, result, DELTA2);
        }

        static List<Arguments> dividendsPerPeriodArgs() {
            return List.of(
                Arguments.of(FinanceCalc.Frequency.MONTHLY, 0.17),
                Arguments.of(FinanceCalc.Frequency.QUARTERLY, 0.51),
                Arguments.of(FinanceCalc.Frequency.SEMI_ANNUAL, 1.02),
                Arguments.of(FinanceCalc.Frequency.ANNUAL, 2.04)
            );
        }

        @ParameterizedTest
        @MethodSource("dividendsPerPeriodArgs")
        void testDividendsPerPeriod(FinanceCalc.Frequency frequency, double expectedResult) {
            // given
            final double koAnnualDividendPerShare = 2.04;
            // when
            final double result = FinanceCalc.EquityInvestment
                .dividendsPerPeriod(koAnnualDividendPerShare, frequency);
            // then
            assertEquals(expectedResult, result, DELTA2);
        }

        static List<Arguments> dripArgs() {
            return List.of(
                Arguments.of(FinanceCalc.Frequency.DAILY, 101.646),
                Arguments.of(FinanceCalc.Frequency.WEEKLY, 101.6449),
                Arguments.of(FinanceCalc.Frequency.MONTHLY, 101.6403),
                Arguments.of(FinanceCalc.Frequency.QUARTERLY, 101.6284),
                Arguments.of(FinanceCalc.Frequency.SEMI_ANNUAL, 101.6106),
                Arguments.of(FinanceCalc.Frequency.ANNUAL, 101.5756)
            );
        }

        @ParameterizedTest
        @MethodSource("dripArgs")
        void testDRIP(FinanceCalc.Frequency compoundFrequency, double expectedResult) {
            // given
            final double moneyInvested = 96.38;
            final double dividendYield = 0.0266;
            final byte numberOfYears = 2;
            // when
            final double result = FinanceCalc.EquityInvestment.drip(
                moneyInvested, dividendYield, compoundFrequency, numberOfYears);
            // then
            assertEquals(expectedResult, result, DELTA4);
        }
    }
}
