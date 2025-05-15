package com.klinux.scratch;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.klinux.scratch.factory.GameServiceFactory;
import com.klinux.scratch.factory.MatrixGeneratorServiceFactory;
import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Result;
import com.klinux.scratch.service.GameService;
import com.klinux.scratch.service.MatrixGeneratorService;
import com.klinux.scratch.util.GameConfigLoader;

@ExtendWith(MockitoExtension.class)
class ScratchGameAppTest {

    @Mock private GameConfigLoader mockConfigLoader;
    @Mock private MatrixGeneratorServiceFactory mockMatrixFactory;
    @Mock private GameServiceFactory mockGameFactory;
    @Mock private MatrixGeneratorService mockMatrixService;
    @Mock private GameService mockGameService;

    private ByteArrayOutputStream output;
    private PrintStream printStream;

    @Captor private ArgumentCaptor<String[][]> matrixCaptor;

    @BeforeEach
    void setup() {
    	MockitoAnnotations.openMocks(this);
        output = new ByteArrayOutputStream();
        printStream = new PrintStream(output);
       
    }

    @Test
    void testRunMethod_withMockedDependencies() {
        // Arrange
        GameConfiguration config = new GameConfiguration();
        String[][] mockMatrix = {
            {"A", "B", "C"},
            {"D", "F", "B"},
            {"E", "A", "10x"}
        };

        Result result = new Result();
        result.setReward(500.0);
        result.setMatrix(mockMatrix);

        try (MockedStatic<GameConfigLoader> mockedStatic = Mockito.mockStatic(GameConfigLoader.class)) {
            mockedStatic.when(() -> GameConfigLoader.loadConfig("config/config.json")).thenReturn(config);

            when(mockMatrixFactory.create(config)).thenReturn(mockMatrixService);
            when(mockGameFactory.create(config)).thenReturn(mockGameService);
            when(mockMatrixService.generateMatrix()).thenReturn(mockMatrix);
            when(mockGameService.startGame(mockMatrix, 100)).thenReturn(result);

            ScratchGameApp app = new ScratchGameApp(mockMatrixFactory, mockGameFactory, printStream);

            // Act
            app.run("config/config.json", 100.0);

            // Assert
            String outputStr = output.toString();
            assertTrue(outputStr.contains("\"matrix\": "));
            assertTrue(outputStr.contains("[\"A\", \"B\", \"C\"]"));
            assertTrue(outputStr.contains("\"reward\": 500.0"));
        }
    }
    
    @Test
    void testRunMethod_withValidArgs() {
        // Arrange
        GameConfiguration config = new GameConfiguration();
        Result result = new Result();
        result.setReward(500.0);
        result.setMatrix(new String[][]{
            {"A", "B", "C"},
            {"D", "F", "B"},
            {"E", "A", "10x"}
        });

        try (MockedStatic<GameConfigLoader> mockedStatic = Mockito.mockStatic(GameConfigLoader.class)) {
            mockedStatic.when(() -> GameConfigLoader.loadConfig("config/config.json")).thenReturn(config);           
            
            mockMatrixService = mock(MatrixGeneratorService.class);
            mockMatrixFactory = mock(MatrixGeneratorServiceFactory.class);
            mockGameService = mock(GameService.class);
            mockGameFactory = mock(GameServiceFactory.class);

            when(mockMatrixFactory.create(config)).thenReturn(mockMatrixService);
            when(mockGameFactory.create(config)).thenReturn(mockGameService);

            when(mockMatrixService.generateMatrix()).thenReturn(new String[][]{
                {"A", "B", "C"},
                {"D", "F", "B"},
                {"E", "A", "10x"}
            });
            when(mockGameService.startGame(any(), eq(200.0))).thenReturn(result);

            ScratchGameApp app = new ScratchGameApp(mockMatrixFactory, mockGameFactory, printStream);

            // Act
            app.run("config/config.json", 200.0);

            // Assert
            String outputStr = output.toString();
            assertTrue(outputStr.contains("\"matrix\": "));
            assertTrue(outputStr.contains("[\"A\", \"B\", \"C\"]"));
            assertTrue(outputStr.contains("\"reward\": 500.0"));
        }
    }


    @Test
    void testMain_withValidArguments_shouldNotThrowException() {
        String[] args = {"-config", "config/config.json", "-bettingAmount", "200"};

        // No need assert — just check that it does not throw an exception.
        assertDoesNotThrow(() -> ScratchGameApp.main(args));
    }

}
