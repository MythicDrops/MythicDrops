package net.nunnerycode.bukkit.mythicdrops.utils;

import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.LongRange;
import org.apache.commons.lang.math.RandomUtils;

public final class RandomRangeUtils {

	private RandomRangeUtils(){
	}

	/**
	 * Returns a value between value1 and value2 that can include value1, but not value2
	 *
	 * @param value1 First value
	 * @param value2 Second value
	 * @return a value between value1 and value2 that can include value1, but not value2
	 */
	public static long randomRangeLongExclusive(long value1, long value2) {
		long max = Math.max(value1, value2);
		long min = Math.min(value1, value2);
		long value = min + (long) (RandomUtils.nextDouble() * (max - min));
		return Math.min(Math.max(value, min), max - 1);
	}

	/**
	 * Returns a value between value1 and value2 that can include value1 and value2
	 *
	 * @param value1 First value
	 * @param value2 Second value
	 * @return a value between value1 and value2 that can include value1 and value2
	 */
	public static long randomRangeLongInclusive(long value1, long value2) {
		long max = Math.max(value1, value2);
		long min = Math.min(value1, value2);
		long value = min + (long) (RandomUtils.nextDouble() * (max - min + 1));
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * Returns a value based on a LongRange, including its lower value but not the upper
	 *
	 * @param rangeContainer LongRange to check against
	 * @return a value based on a LongRange, including its lower value but not the upper
	 */
	public static long randomLongFromLongRangeExclusive(LongRange rangeContainer) {
		long min = rangeContainer.getMinimumLong();
		long max = rangeContainer.getMaximumLong();
		long value = min + (long) (RandomUtils.nextDouble() * (rangeContainer.getMaximumLong() - rangeContainer
				.getMinimumLong()));
		return Math.min(Math.max(value, min), max - 1);
	}

	/**
	 * Returns a value based on a LongRange, including both its lower and upper value
	 *
	 * @param rangeContainer LongRange to check against
	 * @return value
	 */
	public static long randomLongFromLongRangeInclusive(LongRange rangeContainer) {
		long min = rangeContainer.getMinimumLong();
		long max = rangeContainer.getMaximumLong();
		long value = min + (long) (RandomUtils.nextDouble() * (rangeContainer.getMaximumLong() - rangeContainer
				.getMinimumLong()) + 1);
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * Returns a value between value1 and value2 that can include value1, but not value2
	 *
	 * @param value1 First value
	 * @param value2 Second value
	 * @return a value between value1 and value2 that can include value1, but not value2
	 */
	public static double randomRangeDoubleExclusive(double value1, double value2) {
		double min = Math.min(value1, value2);
		double max = Math.max(value1, value2);
		double value = min + RandomUtils.nextDouble() * (max - min);
		return Math.min(Math.max(value, min), max - 1);
	}

	/**
	 * Returns a value between value1 and value2 that can include value1 and value2
	 *
	 * @param value1 First value
	 * @param value2 Second value
	 * @return a value between value1 and value2 that can include value1 and value2
	 */
	public static double randomRangeDoubleInclusive(double value1, double value2) {
		double min = Math.min(value1, value2);
		double max = Math.max(value1, value2);
		double value = min + RandomUtils.nextDouble() * (max - min + 1.0);
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * Returns a value based on a DoubleRange, including its lower value but not the upper
	 *
	 * @param rangeContainer DoubleRange to check against
	 * @return value
	 */
	public static double randomDoubleFromDoubleRangeExclusive(DoubleRange rangeContainer) {
		double min = rangeContainer.getMinimumDouble();
		double max = rangeContainer.getMaximumDouble();
		double value = min + RandomUtils.nextDouble() * (rangeContainer.getMaximumLong() - rangeContainer
				.getMinimumLong());
		return Math.min(Math.max(value, min), max - 1);
	}

	/**
	 * Returns a value based on a DoubleRange, including both its lower and upper values
	 *
	 * @param rangeContainer DoubleRange to check against
	 * @return value
	 */
	public static double randomDoubleFromDoubleRangeInclusive(DoubleRange rangeContainer) {
		double min = rangeContainer.getMinimumDouble();
		double max = rangeContainer.getMaximumDouble();
		double value = min + RandomUtils.nextDouble() * (rangeContainer.getMaximumLong() - rangeContainer
				.getMinimumLong() + 1.0);
		return Math.min(Math.max(value, min), max);
	}
}
