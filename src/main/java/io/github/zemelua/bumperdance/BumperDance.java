package io.github.zemelua.bumperdance;

import io.github.zemelua.bumperdance.config.BumperDanceConfigManager;
import io.github.zemelua.bumperdance.event.BumperDanceEventListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class BumperDance extends JavaPlugin {
	private BumperDanceConfigManager configManager;
	private Random random;

	public Random getRandom() {
		return random;
	}

	public BumperDanceConfigManager getConfigManager() {
		return configManager;
	}

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new BumperDanceEventListener(this), this);
		this.random = new Random();
		this.configManager = new BumperDanceConfigManager(this);
	}

	@Override
	public void onDisable() {
	}
}
