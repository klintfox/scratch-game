package com.klinux.scratch.factory;

import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.service.MatrixGeneratorService;

public class MatrixGeneratorServiceFactory {
    public MatrixGeneratorService create(GameConfiguration config) {
        return new MatrixGeneratorService(config);
    }
}