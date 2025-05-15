package com.klinux.scratch.model;

import java.util.List;

public class WinCombinationOptionalResult {
    private double multiplier;
    private List<String> appliedCombinations;

    public WinCombinationOptionalResult(double multiplier, List<String> appliedCombinations) {
        this.multiplier = multiplier;
        this.appliedCombinations = appliedCombinations;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public List<String> getAppliedCombinations() {
        return appliedCombinations;
    }
}