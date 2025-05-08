package com.klinux.scratch.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Symbol {
    private String name;
    private SymbolType type;
    
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;
    
    private Integer extra;
    private BonusImpact impact;

    public Symbol() {
    }
    
    public Symbol(String name, double rewardMultiplier) {
        this.name = name;
        this.rewardMultiplier = rewardMultiplier;
    }
    
    public Symbol(String name, SymbolType type, double rewardMultiplier) {
        this.name = name;
        this.type = type;
        this.rewardMultiplier = rewardMultiplier;
    }

    public Symbol(String name, SymbolType type, double rewardMultiplier, Integer extra, BonusImpact impact) {
        this.name = name;
        this.type = type;
        this.rewardMultiplier = rewardMultiplier;
        this.extra = extra;
        this.impact = impact;
    }

    public String getName() {
        return name;
    }

    public SymbolType getType() {
        return type;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public Optional<Integer> getExtra() {
        return Optional.ofNullable(extra);
    }

    public Optional<BonusImpact> getImpact() {
        return Optional.ofNullable(impact);
    }
}