package io.github.zemelua.bumperdance.config;

import io.github.zemelua.bumperdance.BumperDance;
import org.bukkit.configuration.Configuration;

public class BumperDanceConfigManager {
	private final BumperDance bumperDance;
	private Configuration config;

	private static final String SNEAK_GROW_ENABLE = "sneakGrow.enable";
	private static final String SNEAK_GROW_RANGE_H = "sneakGrow.rangeH";
	private static final String SNEAK_GROW_RANGE_V = "sneakGrow.rangeV";
	private static final String SNEAK_GROW_DISTANCE_DECAY = "sneakGrow.distanceDecay";
	private static final String HARVEST_EXP_ENABLE = "harvestEXP.enable";
	private static final String HARVEST_EXP_COEFFICIENT = "harvestEXP.coefficient";
	private static final String EASY_REAP_ENABLE = "easyReap.enable";
	private static final String EASY_REAP_UNNECESSARY_HOE = "easyReap.unnecessaryHoe";

	public BumperDanceConfigManager(BumperDance bumperDance) {
		this.bumperDance = bumperDance;
		this.init();
	}

	public boolean isSneakGrowEnable() {
		return this.config.getBoolean(SNEAK_GROW_ENABLE);
	}

	public int getSneakGrowRangeH() {
		return this.config.getInt(SNEAK_GROW_RANGE_H);
	}

	public int getSneakGrowRangeV() {
		return this.config.getInt(SNEAK_GROW_RANGE_V);
	}

	public boolean isSneakGrowDistanceDecay() {
		return this.config.getBoolean(SNEAK_GROW_DISTANCE_DECAY);
	}

	public boolean isHarvestEXPEnable() {
		return this.config.getBoolean(HARVEST_EXP_ENABLE);
	}

	public int getHarvestEXPCoefficient() {
		return this.config.getInt(HARVEST_EXP_COEFFICIENT);
	}

	public boolean isEasyReapEnable() {
		return this.config.getBoolean(EASY_REAP_ENABLE);
	}

	public boolean isUnnecessaryHoe() {
		return this.config.getBoolean(EASY_REAP_UNNECESSARY_HOE);
	}

	private void init() {
		this.bumperDance.saveDefaultConfig();
		this.config = this.bumperDance.getConfig();
	}
}
