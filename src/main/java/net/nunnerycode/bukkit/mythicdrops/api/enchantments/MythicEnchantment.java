package net.nunnerycode.bukkit.mythicdrops.api.enchantments;

import org.bukkit.enchantments.Enchantment;

public class MythicEnchantment {

	private final Enchantment enchantment;
	private final double minimumLevel;
	private final double maximumLevel;


	public MythicEnchantment(Enchantment enchantment, double minimumLevel, double maximumLevel) {
		this.enchantment = enchantment;
		this.minimumLevel = Math.min(minimumLevel, maximumLevel);
		this.maximumLevel = Math.max(minimumLevel, maximumLevel);
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public double getMinimumLevel() {
		return minimumLevel;
	}

	public double getMaximumLevel() {
		return maximumLevel;
	}

	public double getRange() {
		return getMaximumLevel() - getMinimumLevel();
	}
}
