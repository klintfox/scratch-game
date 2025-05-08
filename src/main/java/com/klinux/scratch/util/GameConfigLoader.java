package com.klinux.scratch.util;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klinux.scratch.model.GameConfiguration;

public class GameConfigLoader {

	public static GameConfiguration loadConfig(String configFilePath) {
		 ObjectMapper objectMapper = new ObjectMapper();
	        
	        // Load the config fiel from settings
	        try (InputStream inputStream = GameConfigLoader.class.getClassLoader().getResourceAsStream(configFilePath)) {
	            if (inputStream == null) {
	                throw new RuntimeException("Configuration file not found: " + configFilePath);
	            }
	            // Deserialize the JSON
	            return objectMapper.readValue(inputStream, GameConfiguration.class);
	        } catch (IOException e) {
	            throw new RuntimeException("Error loading game configuration", e);
	        }
	}
}
