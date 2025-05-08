package com.klinux.scratch.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Symbol;
import com.klinux.scratch.model.SymbolType;
import com.klinux.scratch.model.WinCombination;
import com.klinux.scratch.model.WinResult;

public class WinCheckerService {	

	private final GameConfiguration config;

	public WinCheckerService(GameConfiguration config) {
		this.config = config;
	}

	public double checkWins(String[][] matrix, double betAmount) {

	    // Step 1: Get allSymbols from settings
	    Map<String, Symbol> allSymbols = config.getSymbols();

	    // Step 2: Filter STANDARD symbols that appear at least 3 times in the array
	    Map<String, Integer> frequentStandardSymbols = getFrequentStandardSymbols(matrix, allSymbols);

	    // Step 3: For each symbol we find the reward
	    List<Double> rewards = new ArrayList<>();
	    Map<String, List<String>> appliedWinningCombinations = new HashMap<>();

	    for (Map.Entry<String, Integer> entry : frequentStandardSymbols.entrySet()) {
	        String symbolName = entry.getKey();
	        int nTimesSymbolRepeated = entry.getValue();
	        Symbol symbol = allSymbols.get(symbolName);

	        if (symbol != null) {
	            double winCombination = getWinCombinationMultiplierByCount(nTimesSymbolRepeated, config.getWinCombinations());

	            double simpleWinCombination = BigDecimal.valueOf(
	                    symbol.getRewardMultiplier() * winCombination
	            ).setScale(2, RoundingMode.HALF_UP).doubleValue();

	            WinResult winResult = getWinCombinationOptionalMultiplier(symbolName, matrix, config.getWinCombinations());
	            double winCombinationOptional = winResult.getMultiplier();

	            double totalReward = (winCombinationOptional > 0)
	                ? betAmount * simpleWinCombination * winCombinationOptional
	                : betAmount * simpleWinCombination;

	            rewards.add(totalReward);

	            List<String> reasons = new ArrayList<>();
	            reasons.add("same_symbol_" + nTimesSymbolRepeated + "_times");
	            reasons.addAll(winResult.getAppliedCombinations());

	            appliedWinningCombinations.put(symbolName, reasons);
	            System.out.println("- Symbol: " + symbolName + " - " + nTimesSymbolRepeated + " times.");
	        }
	    }

	    printAppliedWinningCombinations(appliedWinningCombinations);

	    return rewards.stream().mapToDouble(Double::doubleValue).sum();
	}
	
	private void printAppliedWinningCombinations(Map<String, List<String>> appliedWinningCombinations) {
	    if (!appliedWinningCombinations.isEmpty()) {
	        System.out.println("- Applied Winning Combinations: {");
	        int count = 0;
	        for (Map.Entry<String, List<String>> entry : appliedWinningCombinations.entrySet()) {
	            String symbol = entry.getKey();
	            List<String> combos = entry.getValue();
	            System.out.print("    \"" + symbol + "\": [");
	            for (int i = 0; i < combos.size(); i++) {
	                System.out.print("\"" + combos.get(i) + "\"");
	                if (i < combos.size() - 1) System.out.print(", ");
	            }
	            System.out.print("]");
	            if (++count < appliedWinningCombinations.size()) {
	                System.out.println(",");
	            } else {
	                System.out.println();
	            }
	        }
	        System.out.println("  }");
	    }
	}


	private Map<String, Integer> getFrequentStandardSymbols(String[][] matrix, Map<String, Symbol> allSymbols) {
		Map<String, Integer> symbolCounts = new HashMap<>();

		// Count occurrences
		for (String[] row : matrix) {
			for (String symbolName : row) {
				Symbol symbol = allSymbols.get(symbolName);
				if (symbol != null && symbol.getType() == SymbolType.STANDARD) {
					symbolCounts.put(symbolName, symbolCounts.getOrDefault(symbolName, 0) + 1);
				}
			}
		}

		// Filter symbols that appear at least 3 times
		return symbolCounts.entrySet().stream().filter(entry -> entry.getValue() >= 3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private double getWinCombinationMultiplierByCount(int ntimesSymbolRepeated,
			Map<String, WinCombination> winCombinations) {

		if (winCombinations == null || winCombinations.isEmpty()) {
			throw new IllegalArgumentException("Win combinations are empty or not set.");
		}

		for (WinCombination wc : winCombinations.values()) {
			if ("same_symbols".equalsIgnoreCase(wc.getGroup()) && wc.getCount() == ntimesSymbolRepeated) {
				return wc.getRewardMultiplier();
			}
		}
		return 0; // or throw an exception if you prefer
	}

	//Mejorar esto 
	private WinResult getWinCombinationOptionalMultiplier(String symbolName, String[][] matrix,
	        Map<String, WinCombination> winCombinations) {

	    double totalMultiplier = 1.0;
	    List<String> applied = new ArrayList<>();

	    for (Map.Entry<String, WinCombination> entry : winCombinations.entrySet()) {
	        WinCombination winCombination = entry.getValue();
	        String name = entry.getKey();

	        if (!"linear_symbols".equalsIgnoreCase(winCombination.getWhen()))
	            continue;

	        List<List<String>> coveredAreas = winCombination.getCoveredAreas();
	        if (coveredAreas == null)
	            continue;

	        for (List<String> area : coveredAreas) {
	            boolean matches = true;

	            for (String coord : area) {
	                String[] parts = coord.split(":");
	                int row = Integer.parseInt(parts[0]);
	                int col = Integer.parseInt(parts[1]);

	                if (!matrix[row][col].equals(symbolName)) {
	                    matches = false;
	                    break;
	                }
	            }

	            if (matches) {
	                totalMultiplier *= winCombination.getRewardMultiplier();
	                applied.add(name);
	                break; // Solo una coincidencia por combinación, pero se pueden aplicar varias combinaciones distintas
	            }
	        }
	    }

	    return applied.isEmpty() ? new WinResult(0.0, List.of()) : new WinResult(totalMultiplier, applied);
	}



}