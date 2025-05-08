package com.klinux.scratch.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProbabilityConfig {

	@JsonProperty("standard_symbols")
    private List<CellProbability> standardSymbols;

    @JsonProperty("bonus_symbols")
    private BonusSymbolProbability bonusSymbols;
    
    private Map<String, WinCombination> winCombinations; 
	
	public List<CellProbability> getStandardSymbols() {
        return standardSymbols;
    }

    public BonusSymbolProbability getBonusSymbols() {
        return bonusSymbols;
    }

	public void setStandardSymbols(List<CellProbability> standardSymbols) {
		this.standardSymbols = standardSymbols;
	}

	public void setBonusSymbols(BonusSymbolProbability bonusSymbols) {
		this.bonusSymbols = bonusSymbols;
	}

	public Map<String, WinCombination> getWinCombinations() {
		return winCombinations;
	}

	public void setWinCombinations(Map<String, WinCombination> winCombinations) {
		this.winCombinations = winCombinations;
	}
    
    

}
