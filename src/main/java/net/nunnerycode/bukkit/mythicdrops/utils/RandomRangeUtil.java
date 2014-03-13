package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;

public final class RandomRangeUtil {

  private RandomRangeUtil() {
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
    long value = min + Math.round(MythicDropsPlugin.getInstance().getRandom().nextDouble() * (max - min));
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
    long value = min + Math.round(MythicDropsPlugin.getInstance().getRandom().nextDouble() * (max - min + 1));
    return Math.min(Math.max(value, min), max);
  }

  /**
   * Returns a value between value1 and value2 that can include value1 and value2
   *
   * @param value1 First value
   * @param value2 Second value
   * @return a value between value1 and value2 that can include value1 and value2
   */
  public static double randomRangeDouble(double value1, double value2) {
    double min = Math.min(value1, value2);
    double max = Math.max(value1, value2);
    double value = min + MythicDropsPlugin.getInstance().getRandom().nextDouble() * (max - min);
    return Math.min(Math.max(value, min), max);
  }

}