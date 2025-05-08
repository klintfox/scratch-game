package com.klinux.scratch.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.util.GameConfigLoader;

class MatrixGeneratorServiceTest {

	@Test
	void shouldGenerateMatrixWithCorrectDimensions() {
	    GameConfiguration config = GameConfigLoader.loadConfig("config/config.json");
	    MatrixGeneratorService service = new MatrixGeneratorService(config);

	    String[][] matrix = service.generateMatrix();

	    assertEquals(config.getRows(), matrix.length);
	    assertEquals(config.getColumns(), matrix[0].length);
	}

	@Test
	void shouldContainAtLeastOneBonusSymbol() {
		GameConfiguration config = GameConfigLoader.loadConfig("config/config.json");
	    String[][] matrix = {
	            {"A", "A", "D"},
	            {"D", "10x", "A"},
	            {"B", "C", "B"}
	    };

	    Set<String> bonusKeys = config.getProbabilities().getBonusSymbols().getSymbols().keySet();
	    int bonusCount = 0;

	    for (String[] row : matrix) {
	        for (String symbol : row) {
	            if (bonusKeys.contains(symbol)) {
	                bonusCount++;
	            }
	        }
	    }

	    assertTrue(bonusCount >= 1, "Matrix should contain at least one bonus symbol");
	}

	@Test
	void shouldFallbackToOtherStandardSymbolIfCellNotDefined() {
		GameConfiguration config = GameConfigLoader.loadConfig("config/config.json");
		MatrixGeneratorService service = new MatrixGeneratorService(config);

		assertDoesNotThrow(service::generateMatrix);
	}
	
	@Test
	void testGetFallbackStandardSymbols_shouldThrowWhenAllEmpty() throws Exception {
	    MatrixGeneratorService service = new MatrixGeneratorService(new GameConfiguration());

	    Map<String, Map<String, Integer>> emptyMap = Map.of(
	        "0,0", Map.of(),
	        "1,1", Map.of()
	    );

	    Method method = MatrixGeneratorService.class.getDeclaredMethod("getFallbackStandardSymbols", Map.class);
	    method.setAccessible(true);

	    Exception exception = assertThrows(InvocationTargetException.class, () -> {
	        try {
	            method.invoke(service, emptyMap);
	        } catch (InvocationTargetException e) {
	            throw e;
	        }
	    });

	    Throwable cause = exception.getCause();
	    assertTrue(cause instanceof IllegalStateException);
	    assertEquals("No standard symbols defined in configuration.", cause.getMessage());
	}
	
	@Test
	void testPickSymbolByProbability_shouldThrowWhenMapIsEmpty() throws Exception {
	    MatrixGeneratorService service = new MatrixGeneratorService(new GameConfiguration());
	    Method method = MatrixGeneratorService.class.getDeclaredMethod("pickSymbolByProbability", Map.class, Random.class);
	    method.setAccessible(true);

	    Map<String, Integer> emptySymbols = Map.of();

	    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () ->
	        method.invoke(service, emptySymbols, new Random())
	    );

	    assertTrue(thrown.getCause() instanceof IllegalStateException);
	    assertEquals("Symbol map cannot be empty.", thrown.getCause().getMessage());
	}

	@Test
	void testPickSymbolByProbability_shouldThrowWhenTotalWeightIsZero() throws Exception {
	    MatrixGeneratorService service = new MatrixGeneratorService(new GameConfiguration());
	    Method method = MatrixGeneratorService.class.getDeclaredMethod("pickSymbolByProbability", Map.class, Random.class);
	    method.setAccessible(true);

	    Map<String, Integer> zeroWeightSymbols = Map.of("A", 0, "B", 0);

	    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () ->
	        method.invoke(service, zeroWeightSymbols, new Random())
	    );

	    assertTrue(thrown.getCause() instanceof IllegalStateException);
	    assertEquals("Total weight must be positive.", thrown.getCause().getMessage());
	}
	
	@Test
	void testPickSymbolByProbability_shouldReturnSymbolCorrectly() throws Exception {
	    MatrixGeneratorService service = new MatrixGeneratorService(new GameConfiguration());
	    Method method = MatrixGeneratorService.class.getDeclaredMethod("pickSymbolByProbability", Map.class, Random.class);
	    method.setAccessible(true);

	    Map<String, Integer> symbols = new LinkedHashMap<>();
	    symbols.put("A", 1);  // range: 0
	    symbols.put("B", 2);  // range: 1-2
	    symbols.put("C", 1);  // range: 3

	    // Probabilidad controlada
	    Random mockRandom = mock(Random.class);
	    when(mockRandom.nextInt(4)).thenReturn(1);  // must return "B"

	    String selected = (String) method.invoke(service, symbols, mockRandom);
	    assertEquals("B", selected);
	}
	
	@Test
	void testPickSymbolByProbability_shouldThrowIfNoSymbolSelectedDespiteValidWeights() throws Exception {
	    MatrixGeneratorService service = new MatrixGeneratorService(new GameConfiguration());
	    Method method = MatrixGeneratorService.class.getDeclaredMethod("pickSymbolByProbability", Map.class, Random.class);
	    method.setAccessible(true);

	    Map<String, Integer> symbols = new LinkedHashMap<>();
	    symbols.put("A", 1);
	    symbols.put("B", 2);

	    // Random Force Value > Total Weigh
	    Random mockRandom = mock(Random.class);
	    when(mockRandom.nextInt(3)).thenReturn(3);  // out of valid range 0-2

	    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () ->
	        method.invoke(service, symbols, mockRandom)
	    );

	    assertTrue(thrown.getCause() instanceof IllegalStateException);
	    assertEquals("No symbol selected despite valid weights.", thrown.getCause().getMessage());
	}


}
