package com.klinux.scratch;

import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.klinux.scratch.factory.GameServiceFactory;
import com.klinux.scratch.factory.MatrixGeneratorServiceFactory;
import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.service.GameService;
import com.klinux.scratch.service.MatrixGeneratorService;
import com.klinux.scratch.util.GameConfigLoader;
import org.apache.commons.cli.ParseException;

public class ScratchGameApp {

    private final GameConfigLoader configLoader;
    private final MatrixGeneratorServiceFactory matrixFactory;
    private final GameServiceFactory gameFactory;
    private final PrintStream out;

    public ScratchGameApp(MatrixGeneratorServiceFactory matrixFactory,
                          GameServiceFactory gameFactory,
                          PrintStream out) {
        this.configLoader = new GameConfigLoader();
        this.matrixFactory = matrixFactory;
        this.gameFactory = gameFactory;
        this.out = out;
    }

    public void run(String configPath, double baseAmount) {
        GameConfiguration config = configLoader.loadConfig(configPath);
        MatrixGeneratorService matrixGeneratorService = matrixFactory.create(config);
        String[][] matrix = matrixGeneratorService.generateMatrix();
        printMatrix(matrix);
        out.println("------------------------------");
        GameService gameService = gameFactory.create(config, baseAmount);
        double reward = gameService.startGame(matrix);
        out.println("Reward: " + reward);
    }

    public void printMatrix(String[][] matrix) {
        out.println("Matrix: ");
        for (String[] row : matrix) {
            for (String cell : row) {
                out.print(cell + " ");
            }
            out.println();
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("config")
                .hasArg()
                .desc("Config file path")
                .build());
        options.addOption(Option.builder("bettingAmount")
                .hasArg()
                .desc("Betting amount")
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Error parsing command line arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ScratchGameApp", options);
            System.exit(1);
            return;
        }

        String configPath = cmd.getOptionValue("config", "config/config.json");
        double bettingAmount = Double.parseDouble(cmd.getOptionValue("bettingAmount", "100"));

        ScratchGameApp app = new ScratchGameApp(
                new MatrixGeneratorServiceFactory(),
                new GameServiceFactory(),
                System.out
        );
        app.run(configPath, bettingAmount);
    }
}

