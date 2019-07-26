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
package com.tealcube.minecraft.bukkit.mythicdrops

fun List<String>.replaceArgs(vararg args: Pair<String, String>): List<String> = map { it.replaceArgs(*args) }
fun List<String>.replaceArgs(args: Collection<Pair<String, String>>): List<String> = map { it.replaceArgs(args) }

fun List<String>.replaceWithCollection(element: String, collection: Collection<String>): List<String> {
    val index = indexOf(element)
    if (index < 0) {
        return this
    }
    val mutableThis = this.toMutableList()
    mutableThis.removeAt(index)
    mutableThis.addAll(index, collection)
    return mutableThis.toList()
}

fun List<String>.replaceWithCollections(vararg elementAndCollectionPairs: Pair<String, Collection<String>>): List<String> =
    elementAndCollectionPairs.fold(this) { acc, pair -> acc.replaceWithCollection(pair.first, pair.second) }
fun List<String>.replaceWithCollections(elementAndCollectionPairs: Collection<Pair<String, Collection<String>>>): List<String> =
    elementAndCollectionPairs.fold(this) { acc, pair -> acc.replaceWithCollection(pair.first, pair.second) }

fun List<String>.chatColorize(): List<String> = map { it.chatColorize() }
