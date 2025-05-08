package com.klinux.scratch.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.klinux.scratch.model.GameConfiguration;

class GameConfigLoaderTest {

	@Test
    void testLoadConfig_Success() {
        // Suponemos que tenemos un archivo JSON en src/test/resources llamado "game_config.json"
        GameConfiguration config = GameConfigLoader.loadConfig("config/config.json");

        assertNotNull(config);
        // Aquí podrías agregar más comprobaciones sobre las propiedades de `config`
        // por ejemplo, verificar que las combinaciones de ganancias estén correctamente cargadas
        assertNotNull(config.getWinCombinations());
        assertTrue(config.getSymbols().containsKey("A"));
    }

    @Test
    void testLoadConfig_FileNotFound() {
        // Probamos con un archivo que no existe
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            GameConfigLoader.loadConfig("non_existent_file.json");
        });

        assertEquals("Configuration file not found: non_existent_file.json", thrown.getMessage());
    }

    @Test
    void testLoadConfig_InvalidJson() {
        // Intentar cargar el archivo inválido
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            GameConfigLoader.loadConfig("config/invalid_game_config.json");
        });

        // Imprimir el mensaje de error para entender qué está ocurriendo
        System.out.println("Error message: " + thrown.getMessage());

        // Comprobar que el mensaje de error contiene la información esperada
        //assertTrue(thrown.getMessage().contains("Error al cargar la configuración del juego"));
        assertTrue(thrown.getMessage().contains("Error loading game configuration"));
    }
    
    @Test
    void testLoadConfig_EmptyFile() {
        // Crear un archivo vacío en src/test/resources llamado "empty_game_config.json"
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            GameConfigLoader.loadConfig("config/empty_game_config.json");
        });

        assertTrue(thrown.getMessage().contains("Error loading game configuration"));
    }

}
