package com.klinux.scratch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SymbolType {
    STANDARD, BONUS;

    @JsonCreator
    public static SymbolType forValue(String value) {
        return SymbolType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}