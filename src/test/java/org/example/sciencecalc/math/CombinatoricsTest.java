package org.example.sciencecalc.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombinatoricsTest {
    private static final double DELTA1 = 0.1;

    @Test
    void testPermutations() {
        // given the deck of cards
        final byte totalCards = 9;
        final byte chooseCards = 3;
        // when
        final double permutations = Combinatorics.permutations(totalCards, chooseCards);
        // then
        assertEquals(504, permutations, DELTA1);
    }

    @Test
    void testPermutationsWithReplacement() {
        // given the deck of cards
        final byte totalCards = 9;
        final byte chooseCards = 3;
        // when
        final double permutations = Combinatorics.permutationsWithReplacement(totalCards, chooseCards);
        // then
        assertEquals(729, permutations, DELTA1);
    }

    @Test
    void testCombinations() {
        // given the deck of cards
        final byte totalCards = 9;
        final byte chooseCards = 3;
        // when
        final double combinations = Combinatorics.combinations(totalCards, chooseCards);
        // then
        assertEquals(84, combinations, DELTA1);
    }

    @Test
    void testCombinationsWithReplacement() {
        // given the deck of cards
        final byte totalCards = 9;
        final byte chooseCards = 3;
        // when
        final double combinations = Combinatorics.combinationsWithReplacement(totalCards, chooseCards);
        // then
        assertEquals(165, combinations, DELTA1);
    }
}
