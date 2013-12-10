package net.nunnerycode.bukkit.mythicdrops.api.enchantments;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.enchantments.Enchantment;

/**
 * A class containing an {@link Enchantment} and a minimum and maximum level.
 */
public final class MythicEnchantment {

	private final Enchantment enchantment;
	private final int minimumLevel;
	private final int maximumLevel;

	/**
	 * Instantiate a new MythicEnchantment with an {@link Enchantment} and a minimum and maximum level.
	 *
	 * @param enchantment  Enchantment to use
	 * @param minimumLevel minimum level of Enchantment
	 * @param maximumLevel maximum level of Enchantment
	 */
	public MythicEnchantment(Enchantment enchantment, int minimumLevel, int maximumLevel) {
		this.enchantment = enchantment;
		this.minimumLevel = Math.min(minimumLevel, maximumLevel);
		this.maximumLevel = Math.max(minimumLevel, maximumLevel);
	}

	public static MythicEnchantment fromString(String string) {
		Enchantment ench = null;
		int value1 = 0;
		int value2 = 0;
		String[] split = string.split(":");
		switch (split.length) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				ench = Enchantment.getByName(split[0]);
				if (ench == null) {
					break;
				}
				value1 = value2 = NumberUtils.toInt(split[1], 1);
				break;
			default:
				ench = Enchantment.getByName(split[0]);
				if (ench == null) {
					break;
				}
				value1 = NumberUtils.toInt(split[1], 1);
				value2 = NumberUtils.toInt(split[2], 1);
				break;
		}
		if (ench == null) {
			return null;
		}
		return new MythicEnchantment(ench, value1, value2);
	}

	/**
	 * Gets the {@link Enchantment}.
	 *
	 * @return Enchantment
	 */
	public Enchantment getEnchantment() {
		return enchantment;
	}

	/**
	 * Returns the range between the minimum and maximum levels of the Enchantment.
	 * <br>
	 * Equivalent of {@code {@link #getMaximumLevel()} - {@link #getMinimumLevel()} }
	 *
	 * @return range between the minimum and maximum levels
	 */
	public double getRange() {
		return getMaximumLevel() - getMinimumLevel();
	}

	/**
	 * Returns the maximum level of the Enchantment.
	 *
	 * @return maximum level
	 */
	public int getMaximumLevel() {
		return maximumLevel;
	}

	/**
	 * Returns the minimum level of the Enchantment.
	 *
	 * @return minimum level
	 */
	public int getMinimumLevel() {
		return minimumLevel;
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
}
