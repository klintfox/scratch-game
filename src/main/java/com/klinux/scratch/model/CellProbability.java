package com.klinux.scratch.model;

import java.util.Map;

public class CellProbability {
    private int column;
    private int row;
    private Map<String, Integer> symbols;

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Map<String, Integer> getSymbols() {
        return symbols;
    }

	public void setColumn(int column) {
		this.column = column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setSymbols(Map<String, Integer> symbols) {
		this.symbols = symbols;
	}
    
    
}