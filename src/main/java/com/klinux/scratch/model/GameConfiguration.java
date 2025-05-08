package com.klinux.scratch.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameConfiguration {
	private int columns;
	private int rows;
	private Map<String, Symbol> symbols;
	private ProbabilityConfig probabilities;

	@JsonProperty("win_combinations")
	private Map<String, WinCombination> winCombinations;

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public Map<String, Symbol> getSymbols() {
		return symbols;
	}

	public ProbabilityConfig getProbabilities() {
		return probabilities;
	}

	public Map<String, WinCombination> getWinCombinations() {
		return winCombinations;
	}
	
	public void setSymbols(Map<String, Symbol> symbols) {
        this.symbols = symbols;
    }

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setProbabilities(ProbabilityConfig probabilities) {
		this.probabilities = probabilities;
	}

	public void setWinCombinations(Map<String, WinCombination> winCombinations) {
		this.winCombinations = winCombinations;
	}
	
	
	
	
}