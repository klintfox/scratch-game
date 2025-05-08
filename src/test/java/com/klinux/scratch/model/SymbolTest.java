package com.klinux.scratch.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class SymbolTest {

    @Test
    public void testAllConstructorsAndGetters() {
        // Constructor vacío
        Symbol emptySymbol = new Symbol();
        assertNull(emptySymbol.getName());
        assertNull(emptySymbol.getType());
        assertEquals(0.0, emptySymbol.getRewardMultiplier());
        assertEquals(Optional.empty(), emptySymbol.getExtra());
        assertEquals(Optional.empty(), emptySymbol.getImpact());

        // Constructor con name y rewardMultiplier
        Symbol basicSymbol = new Symbol("A", 2.0);
        assertEquals("A", basicSymbol.getName());
        assertEquals(2.0, basicSymbol.getRewardMultiplier());

        // Constructor con name, type y rewardMultiplier
        Symbol partialSymbol = new Symbol("B", SymbolType.STANDARD, 1.5);
        assertEquals("B", partialSymbol.getName());
        assertEquals(SymbolType.STANDARD, partialSymbol.getType());
        assertEquals(1.5, partialSymbol.getRewardMultiplier());

        // Constructor completo
        Symbol fullSymbol = new Symbol("C", SymbolType.STANDARD, 3.0, 1000, BonusImpact.EXTRA_BONUS);
        assertEquals("C", fullSymbol.getName());
        assertEquals(SymbolType.STANDARD, fullSymbol.getType());
        assertEquals(3.0, fullSymbol.getRewardMultiplier());
        assertEquals(Optional.of(1000), fullSymbol.getExtra());
        assertEquals(Optional.of(BonusImpact.EXTRA_BONUS), fullSymbol.getImpact());
    }
}
