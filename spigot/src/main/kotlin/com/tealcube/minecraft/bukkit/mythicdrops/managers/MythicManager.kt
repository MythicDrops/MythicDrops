package com.tealcube.minecraft.bukkit.mythicdrops.managers

import com.tealcube.minecraft.bukkit.mythicdrops.api.managers.Manager
import com.tealcube.minecraft.bukkit.mythicdrops.choices.Choice
import io.pixeloutlaw.minecraft.spigot.resettableLazy

internal abstract class MythicManager<T, ID> : Manager<T, ID> {
    protected val managed = mutableMapOf<ID, T>()
    private val managedValuesAsSet = resettableLazy { managed.values.toSet() }

    abstract fun getId(item: T): ID

    override fun get(): Set<T> = managedValuesAsSet.value

    override fun add(toAdd: T) {
        managed[getId(toAdd)] = toAdd
        managedValuesAsSet.reset()
    }

    override fun addAll(toAdd: Collection<T>) {
        toAdd.forEach { add(it) }
    }

    override fun clear() {
        managed.clear()
        managedValuesAsSet.reset()
    }

    override fun random(): T? = Choice.between(get()).choose()

    override fun toString(): String {
        return managed.toString()
    }
}
