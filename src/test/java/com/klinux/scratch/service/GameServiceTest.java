package com.klinux.scratch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Symbol;
import com.klinux.scratch.model.SymbolType;
import com.klinux.scratch.util.GameConfigLoader;

public class GameServiceTest {

	private GameConfiguration config;

    @BeforeEach
    public void setUp() {
        config = GameConfigLoader.loadConfig("config/config.json");
    }

    @Test
    public void testStartGame_noWin_withBonus() {
        String[][] matrix = {
            {"A", "B", "C"},
            {"D", "E", "F"},
            {"A", "B", "+500"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(0, reward);
    }
    
    @Test
    public void testStartGame_withBonus_andVerticalWins() {
        String[][] matrix = {
            {"A", "A", "B"},
            {"A", "+1000", "B"},
            {"A", "A", "B"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(3600.0, reward);
    }

    @Test
    public void testStartGame_withMultiplyBonus() {
        String[][] matrix = {
            {"A", "A", "A"},
            {"B", "C", "D"},
            {"E", "F", "+500"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(1500.0, reward);
    }

    @Test
    public void testStartGame_withExtraBonus() {               
        String[][] matrix = {
            {"A", "A", "A"},
            {"B", "10x", "C"},
            {"D", "E", "F"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(10000.0, reward); // 3 equals  → 3.0 * 100 = 300 + 50 extra
    }

    @Test
    public void testStartGame_bonusWithoutImpact() {
    	Symbol bonus = new Symbol("MISS", SymbolType.BONUS, 2.0, null, null);
        config.getSymbols().put("MISS", bonus);

        String[][] matrix = {
            {"A", "A", "A"},
            {"B", "MISS", "C"},
            {"D", "E", "F"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(1000.0, reward);
    }
    
    @Test
    public void testStartGame_bonusImpact() {
        String[][] matrix = {
            {"A", "A", "A"},
            {"B", "+500", "C"},
            {"D", "E", "F"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(1500.0, reward);
    }
    
    @Test
    public void testStartGame_bonusImpact10x() {
        String[][] matrix = {
            {"A", "B", "C"},
            {"E", "B", "10x"},
            {"F", "D", "B"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(3000.0, reward);
    }

    @Test
    public void testStartGame_symbolNotFoundInConfig() {
        // 'ZZZ' is not in the symbol map, it should be ignored without error
        String[][] matrix = {
            {"A", "A", "A"},
            {"ZZZ", "C", "D"},
            {"E", "F", "G"}
        };

        GameService gameService = new GameService(config, 100);
        double reward = gameService.startGame(matrix);
        assertEquals(1000.0, reward);
    }
}
