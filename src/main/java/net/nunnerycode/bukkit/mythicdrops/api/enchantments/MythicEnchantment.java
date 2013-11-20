package net.nunnerycode.bukkit.mythicdrops.api.enchantments;

import org.bukkit.enchantments.Enchantment;

public class MythicEnchantment {

	private final Enchantment enchantment;
	private final int minimumLevel;
	private final int maximumLevel;

	public MythicEnchantment(Enchantment enchantment, int minimumLevel, int maximumLevel) {
		this.enchantment = enchantment;
		this.minimumLevel = Math.min(minimumLevel, maximumLevel);
		this.maximumLevel = Math.max(minimumLevel, maximumLevel);
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public int getMinimumLevel() {
		return minimumLevel;
	}

	public int getMaximumLevel() {
		return maximumLevel;
	}

	public double getRange() {
		return getMaximumLevel() - getMinimumLevel();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MythicEnchantment)) return false;

		MythicEnchantment that = (MythicEnchantment) o;

		if (Double.compare(that.maximumLevel, maximumLevel) != 0) return false;
		if (Double.compare(that.minimumLevel, minimumLevel) != 0) return false;
		if (enchantment != null ? !enchantment.equals(that.enchantment) : that.enchantment != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = enchantment != null ? enchantment.hashCode() : 0;
		temp = Double.doubleToLongBits(minimumLevel);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maximumLevel);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
