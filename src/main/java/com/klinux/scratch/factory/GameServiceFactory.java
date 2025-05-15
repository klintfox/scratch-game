package com.klinux.scratch.factory;

import com.klinux.scratch.model.GameConfiguration;
import com.klinux.scratch.service.GameService;

public class GameServiceFactory {
	public GameService create(GameConfiguration config) {
		return new GameService(config);
	}
}