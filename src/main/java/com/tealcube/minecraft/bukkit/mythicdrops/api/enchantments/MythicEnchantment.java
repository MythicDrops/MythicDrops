/**
 * The MIT License
 * Copyright (c) 2013 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


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
     * @param enchantment
     *         Enchantment to use
     * @param minimumLevel
     *         minimum level of Enchantment
     * @param maximumLevel
     *         maximum level of Enchantment
     */
    public MythicEnchantment(Enchantment enchantment, int minimumLevel, int maximumLevel) {
        this.enchantment = enchantment;
        this.minimumLevel = Math.max(1, Math.min(minimumLevel, maximumLevel));
        this.maximumLevel = Math.min(Math.max(minimumLevel, maximumLevel), 127);
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
     * Returns the range between the minimum and maximum levels of the Enchantment. <br> Equivalent of {@code {@link
     * #getMaximumLevel()} - {@link #getMinimumLevel()} }
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
        return Math.min(maximumLevel, 127);
    }

    /**
     * Returns the minimum level of the Enchantment.
     *
     * @return minimum level
     */
    public int getMinimumLevel() {
        return Math.max(minimumLevel, 1);
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof MythicEnchantment)) {
            return false;
        }

        MythicEnchantment that = (MythicEnchantment) o;

        return Double.compare(that.maximumLevel, maximumLevel) == 0
                && Double.compare(that.minimumLevel, minimumLevel) == 0 && !(enchantment != null
                                                                             ? !enchantment
                .equals(that.enchantment) : that.enchantment != null);
    }

    @Override
    public String toString() {
        if (enchantment != null) {
            return String.format("%s:%s:%s", enchantment.getName(), minimumLevel, maximumLevel);
        } else {
            return null;
        }
    }
}
