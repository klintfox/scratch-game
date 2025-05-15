package com.klinux.scratch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.klinux.scratch.model.BonusEffect;
import com.klinux.scratch.model.BonusImpact;
import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Result;
import com.klinux.scratch.model.Symbol;
import com.klinux.scratch.model.SymbolType;

public class GameService {

	private final GameConfiguration config;
	private final WinCheckerService winCheckerService;

	public GameService(GameConfiguration config) {
		this.config = config;
		this.winCheckerService = new WinCheckerService(config);
	}

	public Result startGame(String[][] matrix, double betAmount) {

		double reward = 0;
		Result result = new Result();
		result = (winCheckerService.checkWins(matrix, betAmount));
		reward = result.getReward();
		if (reward > 0) {
			// Get bonus symbols from matrix and apply to reward
			Map<String, Symbol> allSymbols = config.getSymbols();
			List<BonusEffect> bonusEffects = extractBonusEffects(matrix, allSymbols);
			List<String> appliedBonusSymbol = new ArrayList<>();

			// If any symbol is "MISS", the bonuses are voided
			boolean hasMiss = bonusEffects.stream().anyMatch(effect -> "MISS".equals(effect.getSymbolName()));

			if (!hasMiss) {
				for (BonusEffect effect : bonusEffects) {

					appliedBonusSymbol.add(effect.getSymbolName());

					BonusImpact impact = effect.getImpact();
					if (impact == BonusImpact.MULTIPLY_REWARD) {
						reward *= effect.getRewardMultiplier();
					} else if (impact == BonusImpact.EXTRA_BONUS) {
						reward += effect.getExtra();
					}
				}
			} else {
				appliedBonusSymbol.add("Not applied - MISS");
			}
			result.setReward(reward);
			result.setAppliedBonusSymbol(appliedBonusSymbol);
		}		
		return result;
	}

	private List<BonusEffect> extractBonusEffects(String[][] matrix, Map<String, Symbol> allSymbols) {
		List<BonusEffect> bonusEffects = new ArrayList<>();

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				String symbolName = matrix[i][j];
				Symbol symbol = allSymbols.get(symbolName);

				if (symbol != null && symbol.getType() == SymbolType.BONUS) {
					double rewardMultiplier = symbol.getRewardMultiplier(); // default 0.0 si no tiene
					double extra = symbol.getExtra().map(Integer::doubleValue).orElse(0.0);
					BonusImpact impact = symbol.getImpact().orElse(null);

					bonusEffects.add(new BonusEffect(symbolName, rewardMultiplier, extra, impact));
				}
			}
		}
		return bonusEffects;
	}

}