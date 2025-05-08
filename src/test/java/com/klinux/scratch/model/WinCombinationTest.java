package com.klinux.scratch.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class WinCombinationTest {

    @Test
    public void testSettersAndToString() {
        WinCombination combo = new WinCombination(1.5, "horizontal", 3, "group1");

        combo.setRewardMultiplier(2.0);
        combo.setWhen("vertical");
        combo.setCount(5);
        combo.setGroup("group2");
        combo.setCoveredAreas(List.of(List.of("0,0", "0,1")));

        assertEquals(2.0, combo.getRewardMultiplier());
        assertEquals("vertical", combo.getWhen());
        assertEquals(5, combo.getCount());
        assertEquals("group2", combo.getGroup());
        assertEquals(List.of(List.of("0,0", "0,1")), combo.getCoveredAreas());

        String toStringResult = combo.toString();
        // Solo valida que toString no sea null y contenga algo esperado
        assertEquals(true, toStringResult.contains("rewardMultiplier=2.0"));
    }
}

