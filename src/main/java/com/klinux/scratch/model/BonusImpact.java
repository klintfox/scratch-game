package com.klinux.scratch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BonusImpact {
    EXTRA_BONUS, MULTIPLY_REWARD, MISS;

    @JsonCreator
    public static BonusImpact forValue(String value) {
        return BonusImpact.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}