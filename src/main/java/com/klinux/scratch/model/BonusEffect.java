package com.klinux.scratch.model;

public class BonusEffect {
    private final String symbolName;
    private final double rewardMultiplier;
    private final double extra;
    private final BonusImpact impact;

    public BonusEffect(String symbolName, double rewardMultiplier, double extra, BonusImpact impact) {
        this.symbolName = symbolName;
        this.rewardMultiplier = rewardMultiplier;
        this.extra = extra;
        this.impact = impact;
    }

    // Getters
    public String getSymbolName() { return symbolName; }
    public double getRewardMultiplier() { return rewardMultiplier; }
    public double getExtra() { return extra; }
    public BonusImpact getImpact() { return impact; }
}
