/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.api.choices

import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted
import kotlin.random.Random

/**
 * Simple utility for making weighted choices.
 */
class WeightedChoice<T : Weighted> : Choice<T>() {
    companion object {
        /**
         * Constructs a [WeightedChoice] for the given [option].
         *
         * @param option Option(s) for choice.
         * @return constructed choice
         */
        @JvmStatic
        fun <T : Weighted> between(vararg option: T): WeightedChoice<T> =
            between(option.asIterable())

        /**
         * Constructs a [WeightedChoice] for the given [option].
         *
         * @param option Option(s) for choice.
         * @return constructed choice
         */
        @JvmStatic
        fun <T : Weighted> between(options: Iterable<T>): WeightedChoice<T> =
            WeightedChoice<T>().also { it.addOptions(options) }
    }

    override fun choose(): T? {
        return choose { true }
    }

    /**
     * Chooses one of the available options and returns it based on weight.
     *
     * @param block Extra block to execute to determine if option is selectable
     * @return chosen option or null if one cannot be chosen
     */
    fun choose(block: (T) -> Boolean): T? {
        val selectableOptions = options.filter(block)
        val totalWeight: Double = selectableOptions.fold(0.0) { sum, element -> sum + element.weight }
        val chosenWeight = Random.Default.nextDouble(0.0, totalWeight)
        val shuffledOptions = selectableOptions.shuffled()

        var currentWeight = 0.0
        for (option in shuffledOptions) {
            currentWeight += option.weight

            if (currentWeight >= chosenWeight) {
                return option
            }
        }
        return null
    }
}
