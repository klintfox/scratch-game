package com.klinux.scratch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Result;
import com.klinux.scratch.util.GameConfigLoader;

class WinCheckerServiceTest {

	private GameConfiguration config;
    private WinCheckerService winCheckerService;
    
    double betAmount = 100;

    @BeforeEach
    public void setUp() {        
        config = GameConfigLoader.loadConfig("config/config.json");       
        winCheckerService = new WinCheckerService(config);
    }
    
    
    @Test
    public void testChecWins_kNoWin() {    	
        String[][] matrix = {
            {"D", "E", "C"},
            {"F", "E", "A"},
            {"A", "B", "D"}
        };        
        Result result = winCheckerService.checkWins(matrix, betAmount);
        double expectedReward = 0.0;

        assertEquals(expectedReward, result.getReward(), 0.01); //0.01 delta 
    }

    @Test
    public void testCheckWins_simpleWin() {    	
        String[][] matrix = {
            {"D", "E", "C"},
            {"F", "D", "D"},
            {"A", "B", "A"}
        };        
        Result result = winCheckerService.checkWins(matrix, betAmount);
        double expectedReward = 200.0;

        assertEquals(expectedReward, result.getReward(), 0.01); //0.01 delta 
    }
    
    @Test
    public void testCheckWins_winCombination() {    	
        String[][] matrix = {
            {"D", "E", "C"},
            {"F", "D", "D"},
            {"E", "B", "A"}
        };        
        Result result = winCheckerService.checkWins(matrix, betAmount);
        double expectedReward = 200.0;

        assertEquals(expectedReward, result.getReward(), 0.01); //0.01 delta 
    }
    
    @Test
    public void testCheckWins_fourSymbols_winCombination() {
        String[][] matrix = {
            {"A", "A", "F"},
            {"D", "E", "A"},  
            {"B", "A", "C"}
        };                
        Result result = winCheckerService.checkWins(matrix,betAmount);
        double expectedReward = 750.0;

        assertEquals(expectedReward, result.getReward(), 0.01);
    }
    
    @Test
    public void testCheckWins_fiveSymbols_winCombinationoptional() {
        String[][] matrix = {
            {"A", "A", "A"},
            {"D", "E", "F"},  
            {"A", "A", "D"}
        };                
        Result result = winCheckerService.checkWins(matrix,betAmount);
        double expectedReward = 2000.0;

        assertEquals(expectedReward, result.getReward(), 0.01);
    }
    
    
    @Test
    public void testCheckWins_twoWinCombination() {
        String[][] matrix = {
            {"A", "A", "B"},
            {"A", "E", "B"},  
            {"A", "A", "B"}
        };                
        Result result = winCheckerService.checkWins(matrix,betAmount);
        double expectedReward = 2600.0;

        assertEquals(expectedReward, result.getReward(), 0.01);
    }
    
    @Test
    public void testCheckWins_sixTimes_twoWinCombinationB() {
        String[][] matrix = {
            {"F", "E", "D"},
            {"E", "E", "C"},  
            {"E", "E", "E"}
        };                
        Result result = winCheckerService.checkWins(matrix,betAmount);
        double expectedReward = 1440.0;

        assertEquals(expectedReward, result.getReward(), 0.01);
    }    
    
}
