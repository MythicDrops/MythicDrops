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

import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.IdentityWeighted
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import io.pixeloutlaw.minecraft.spigot.mythicdrops.isZero

/**
 * Simple utility for making weighted choices.
 */
class IdentityWeightedChoice<T : IdentityWeighted> : Choice<T>() {
    companion object {
        /**
         * Constructs a [IdentityWeightedChoice] for the given [option].
         *
         * @param option Option(s) for choice.
         * @return constructed choice
         */
        @JvmStatic
        fun <T : IdentityWeighted> between(vararg option: T): IdentityWeightedChoice<T> =
            between(option.asIterable())

        /**
         * Constructs a [IdentityWeightedChoice] for the given [options].
         *
         * @param option Option(s) for choice.
         * @return constructed choice
         */
        @JvmStatic
        fun <T : IdentityWeighted> between(options: Iterable<T>): IdentityWeightedChoice<T> =
            IdentityWeightedChoice<T>().also { it.addOptions(options) }
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
        val selectableOptions = options.filter(block).filter { !it.identityWeight.isZero() }
        val totalWeight: Double = selectableOptions.fold(0.0) { sum, element -> sum + element.identityWeight }
        val chosenWeight = (0.0..totalWeight).safeRandom()
        val shuffledOptions = selectableOptions.shuffled()

        var currentWeight = 0.0
        for (option in shuffledOptions) {
            currentWeight += option.identityWeight

            if (currentWeight >= chosenWeight) {
                return option
            }
        }
        return null
    }
}
