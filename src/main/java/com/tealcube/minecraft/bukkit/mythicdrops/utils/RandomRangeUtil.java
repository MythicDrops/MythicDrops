package com.tealcube.minecraft.bukkit.mythicdrops.utils;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;

import java.util.Random;

public final class RandomRangeUtil {

    private static final Random RANDOM = new Random();

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

    public static int randomRange(int value1, int value2) {
        int max = Math.max(value1, value2);
        int min = Math.min(value1, value2);
        int value = min + RANDOM.nextInt(max - min + 1);
        return Math.min(Math.max(value, min), max);
    }

}
