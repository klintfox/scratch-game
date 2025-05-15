package com.klinux.scratch.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Result;
import com.klinux.scratch.model.Symbol;
import com.klinux.scratch.model.SymbolType;
import com.klinux.scratch.model.WinCombination;
import com.klinux.scratch.model.WinCombinationOptionalResult;

public class WinCheckerService {

	private final GameConfiguration config;

	public WinCheckerService(GameConfiguration config) {
		this.config = config;
	}

	public Result checkWins(String[][] matrix, double betAmount) {

		Result result = new Result();
		result.setMatrix(matrix);

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
				double winCombination = getWinCombinationMultiplierByCount(nTimesSymbolRepeated,
						config.getWinCombinations());

				double simpleWinCombination = BigDecimal.valueOf(symbol.getRewardMultiplier() * winCombination)
						.setScale(2, RoundingMode.HALF_UP).doubleValue();

				WinCombinationOptionalResult winCombinarionOptionalResult = getWinCombinationOptionalMultiplier(
						symbolName, matrix, config.getWinCombinations());
				double winCombinationOptional = winCombinarionOptionalResult.getMultiplier();

				double totalReward = (winCombinationOptional > 0)
						? betAmount * simpleWinCombination * winCombinationOptional
						: betAmount * simpleWinCombination;

				rewards.add(totalReward);

				List<String> reasons = new ArrayList<>();
				reasons.add("same_symbol_" + nTimesSymbolRepeated + "_times");
				reasons.addAll(winCombinarionOptionalResult.getAppliedCombinations());

				appliedWinningCombinations.put(symbolName, reasons);
			}
		}
		result.setReward(rewards.stream().mapToDouble(Double::doubleValue).sum());
		result.setAppliedWinningCombinations(appliedWinningCombinations);
		return result;
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

	private WinCombinationOptionalResult getWinCombinationOptionalMultiplier(String symbolName, String[][] matrix,
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
				}
			}
		}

		return applied.isEmpty() ? new WinCombinationOptionalResult(0.0, List.of())
				: new WinCombinationOptionalResult(totalMultiplier, applied);
	}

}