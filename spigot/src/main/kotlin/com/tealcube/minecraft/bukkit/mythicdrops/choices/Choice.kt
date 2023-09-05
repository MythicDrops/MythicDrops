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
package com.tealcube.minecraft.bukkit.mythicdrops.choices

/**
 * Simple utility for making choices.
 */
internal open class Choice<T> {
    companion object {
        /**
         * Constructs a [Choice] for the given [option].
         *
         * @param option Option(s) for choice.
         * @return constructed choice
         */
        @JvmStatic
        fun <T> between(vararg option: T): Choice<T> = between(option.asIterable())

        /**
         * Constructs a [Choice] for the given [option].
         *
         * @param option Option(s) for choice.
         * @return constructed choice
         */
        @JvmStatic
        fun <T> between(options: Iterable<T>): Choice<T> = Choice<T>().also { it.addOptions(options) }
    }

    protected val options = mutableSetOf<T>()

    /**
     * Adds an option to the available options when choosing.
     *
     * @param option Option to add
     * @return if adding was successful
     */
    fun addOption(option: T) = options.add(option)

    /**
     * Adds options to the available options when choosing.
     *
     * @param option Option to add
     * @return if adding was successful
     */
    fun addOptions(vararg option: T) = options.addAll(option)

    /**
     * Adds options to the available options when choosing.
     *
     * @param pOptions Options to add
     * @return if adding was successful
     */
    fun addOptions(pOptions: Iterable<T>) = options.addAll(pOptions)

    /**
     * Chooses one of the available options and returns it.
     *
     * @param block Extra block to execute to determine if option is selectable
     * @return chosen option or null if one cannot be chosen
     */
    open fun choose(): T? = options.randomOrNull()
}
