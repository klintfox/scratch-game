package com.klinux.scratch;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.klinux.scratch.factory.GameServiceFactory;
import com.klinux.scratch.factory.MatrixGeneratorServiceFactory;
import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.model.Result;
import com.klinux.scratch.service.GameService;
import com.klinux.scratch.service.MatrixGeneratorService;
import com.klinux.scratch.util.GameConfigLoader;

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

    public void run(String configPath, double betAmount) {
        GameConfiguration config = configLoader.loadConfig(configPath);
        MatrixGeneratorService matrixGeneratorService = matrixFactory.create(config);
        String[][] matrix = matrixGeneratorService.generateMatrix();
        GameService gameService = gameFactory.create(config);
        Result result  = new Result(); 
        result = gameService.startGame(matrix, betAmount);
        if (result.getReward() <= 0.0) {
        	result.setAppliedWinningCombinations(new HashMap<>());
            result.setAppliedBonusSymbol(Collections.emptyList());
        }
        out.print(result);
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
