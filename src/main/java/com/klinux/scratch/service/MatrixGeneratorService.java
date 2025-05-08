package com.klinux.scratch.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.klinux.scratch.model.CellProbability;
import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.ProbabilityConfig;

public class MatrixGeneratorService {

	private final GameConfiguration config;

	public MatrixGeneratorService(GameConfiguration config) {
		this.config = config;
	}

	public String[][] generateMatrix() {
		int rows = config.getRows();
		int cols = config.getColumns();
		String[][] matrix = new String[rows][cols];

		ProbabilityConfig probabilityConfig = config.getProbabilities();
		Map<String, Map<String, Integer>> standardSymbolMap = new HashMap<>();
		for (CellProbability cp : probabilityConfig.getStandardSymbols()) {
			String key = cp.getRow() + "," + cp.getColumn();
			standardSymbolMap.put(key, cp.getSymbols());
		}

		Map<String, Integer> bonusSymbols = probabilityConfig.getBonusSymbols().getSymbols();
//		Set<String> bonusSymbolKeys = bonusSymbols.keySet();

		Random random = new Random();
		// 1. Choose random position for bonus symbol
//        int bonusRow = random.nextInt(rows);
//        int bonusCol = random.nextInt(cols);
        
        double bonusAppearanceChance = 0.1;

        // 2. Insert symbols
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String symbol;

                boolean generateBonusHere = random.nextDouble() < bonusAppearanceChance;

                if (generateBonusHere) {
                    symbol = pickSymbolByProbability(bonusSymbols, random);
                } else {
                    Map<String, Integer> stdSymbols = standardSymbolMap.get(row + "," + col);
                    if (stdSymbols == null || stdSymbols.isEmpty()) {
                        stdSymbols = getFallbackStandardSymbols(standardSymbolMap);
                    }
                    symbol = pickSymbolByProbability(stdSymbols, random);
                }

                matrix[row][col] = symbol;
            }
        }

		return matrix;
	}

	private String pickSymbolByProbability(Map<String, Integer> symbols, Random random) {
	    if (symbols.isEmpty()) {
	        throw new IllegalStateException("Symbol map cannot be empty.");
	    }

	    int totalWeight = symbols.values().stream().mapToInt(Integer::intValue).sum();
	    if (totalWeight <= 0) {
	        throw new IllegalStateException("Total weight must be positive.");
	    }

	    int randomValue = random.nextInt(totalWeight);
	    int current = 0;

	    for (Map.Entry<String, Integer> entry : symbols.entrySet()) {
	        current += entry.getValue();
	        if (randomValue < current) {
	            return entry.getKey();
	        }
	    }

	    throw new IllegalStateException("No symbol selected despite valid weights.");
	}
	
	// Fallback if a cell has no standard symbols defined
    private Map<String, Integer> getFallbackStandardSymbols(Map<String, Map<String, Integer>> symbolMap) {
        return symbolMap.values().stream()
                .filter(map -> map != null && !map.isEmpty())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No standard symbols defined in configuration."));
    }

}
