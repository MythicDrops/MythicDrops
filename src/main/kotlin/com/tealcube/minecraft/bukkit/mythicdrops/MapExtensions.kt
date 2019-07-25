package com.tealcube.minecraft.bukkit.mythicdrops

fun <T, U> Map<U, Set<T>>.additivePlus(pair: Pair<U, Set<T>>): Map<U, Set<T>> {
    val originalValue = this[pair.first] ?: emptySet()
    val additiveValue = originalValue + pair.second
    return this.plus(pair.first to additiveValue)
}
