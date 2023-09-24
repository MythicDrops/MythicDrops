/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package io.pixeloutlaw.minecraft.spigot

/**
 * Simple typealias for a function that returns a boolean.
 */
internal fun interface Prerequisite {
    fun invoke(): Boolean
}

internal class OrPrerequisite internal constructor() : Prerequisite {
    private val prerequisites: MutableList<Prerequisite> = mutableListOf()

    override fun invoke(): Boolean =
        prerequisites.fold(true) { accumulator: Boolean, prerequisite: Prerequisite ->
            accumulator || prerequisite.invoke()
        }

    fun prerequisite(prerequisite: Prerequisite) {
        prerequisites.add(prerequisite)
    }
}

/**
 * Holds a collection of functions that return a boolean.
 */
internal class Prerequisites internal constructor() {
    internal val prereqs: MutableList<Prerequisite> = mutableListOf()

    fun invoke(): Boolean =
        prereqs.fold(true) { accumulator: Boolean, prerequisite: Prerequisite ->
            accumulator && prerequisite.invoke()
        }

    fun prerequisite(prerequisite: Prerequisite) {
        prereqs.add(prerequisite)
    }
}

/**
 * Constructs a [Prerequisites] instance and invokes it after configuring it via [block].
 */
internal fun prerequisites(block: Prerequisites.() -> Unit): Boolean = Prerequisites().apply { block() }.invoke()

/**
 * Constructs an [OrPrerequisite] and configures it ready to be passed to [Prerequisites.prerequisite].
 */
internal fun orPrerequisite(block: OrPrerequisite.() -> Unit): Prerequisite = OrPrerequisite().apply(block)
