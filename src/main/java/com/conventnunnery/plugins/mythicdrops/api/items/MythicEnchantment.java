/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.api.items;

import org.bukkit.enchantments.Enchantment;

/**
 * A class holding an {@link Enchantment}, the lowest level that the Enchantment can hold, and the highest level that
 * the Enchantment can hold.
 */
public class MythicEnchantment {
    private final Enchantment enchantment;
    private final int minimumLevel;
    private final int maximumLevel;

    /**
     * Instantiates a new MythicEnchantment with an {@link Enchantment} and minimum and maximum levels.
     *
     * @param enchantment  Enchantment to hold
     * @param minimumLevel minimum enchantment level
     * @param maximumLevel maximum enchantment level
     */
    public MythicEnchantment(Enchantment enchantment, int minimumLevel, int maximumLevel) {
        this.enchantment = enchantment;
        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
    }

    /**
     * Gets the {@link Enchantment} held by this MythicEnchantment.
     *
     * @return Enchantment held by the MythicEnchantment.
     */
    public Enchantment getEnchantment() {
        return enchantment;
    }

    /**
     * Returns the lowest level able to be enchanted from this MythicEnchantment. The lowest possible is 1 and the
     * highest possible is 127.
     *
     * @return lowest level able to be enchanted
     */
    public int getMinimumLevel() {
        return Math.min(Math.max(1, Math.min(minimumLevel, maximumLevel)), 127);
    }

    /**
     * Returns the highest level able to be enchanted from this MythicEnchantment. The lowest possible is 1 and the
     * highest possible is 127.
     *
     * @return highest level able to be enchanted
     */
    public int getMaximumLevel() {
        return Math.min(Math.max(1, Math.max(minimumLevel, maximumLevel)), 127);
    }
}
