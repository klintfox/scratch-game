package com.klinux.scratch.model;

import java.util.List;

public class WinResult {
    private double multiplier;
    private List<String> appliedCombinations;

    public WinResult(double multiplier, List<String> appliedCombinations) {
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