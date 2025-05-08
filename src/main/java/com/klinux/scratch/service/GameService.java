package com.klinux.scratch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.klinux.scratch.model.BonusEffect;
import com.klinux.scratch.model.BonusImpact;
import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Symbol;
import com.klinux.scratch.model.SymbolType;

public class GameService {

	private final GameConfiguration config;
	private final WinCheckerService winCheckerService;
	private double betAmount;

	public GameService(GameConfiguration config, double betAmount) {
		this.config = config;
		this.winCheckerService = new WinCheckerService(config);
		this.betAmount = betAmount;
	}

	public double startGame(String[][] matrix) {

		double reward = 0;
		reward = (winCheckerService.checkWins(matrix, betAmount));
		if (reward > 0) {
			// Get bonus symbols from matrix and apply to reward
			Map<String, Symbol> allSymbols = config.getSymbols();
			List<BonusEffect> bonusEffects = extractBonusEffects(matrix, allSymbols);

			// If any symbol is "MISS", the bonuses are voided
			boolean hasMiss = bonusEffects.stream()
				.anyMatch(effect -> "MISS".equals(effect.getSymbolName()));

			if (!hasMiss) {
				for (BonusEffect effect : bonusEffects) {
					
					System.out.println("- Applied Bonus Symbol: " + effect.getSymbolName());
					
					BonusImpact impact = effect.getImpact();
					if (impact == BonusImpact.MULTIPLY_REWARD) {
						reward *= effect.getRewardMultiplier();
					} else if (impact == BonusImpact.EXTRA_BONUS) {
						reward += effect.getExtra();
					}
				}
			}else {
				System.out.println("- Applied Bonus Symbol: MISS");
			}
		}
		return reward;
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