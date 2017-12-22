/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils;

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
        long value = min + Math.round(RANDOM.nextDouble() * (max - min));
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
        long value = min + Math.round(RANDOM.nextDouble() * (max - min + 1));
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
        double value = min + RANDOM.nextDouble() * (max - min);
        return Math.min(Math.max(value, min), max);
    }

    public static int randomRange(int value1, int value2) {
        int max = Math.max(value1, value2);
        int min = Math.min(value1, value2);
        int value = min + RANDOM.nextInt(max - min + 1);
        return Math.min(Math.max(value, min), max);
    }

}
