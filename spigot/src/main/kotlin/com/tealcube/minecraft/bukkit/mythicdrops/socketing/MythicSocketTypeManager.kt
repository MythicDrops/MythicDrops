package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.choices.Choice
import com.tealcube.minecraft.bukkit.mythicdrops.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import io.pixeloutlaw.kindling.Log
import org.bukkit.configuration.Configuration
import java.util.Locale

internal class MythicSocketTypeManager : SocketTypeManager {
    private val managedSocketTypes = mutableMapOf<String, SocketType>()

    override fun loadFromConfiguration(configuration: Configuration): Set<SocketType> {
        Log.debug("Loading socket types")
        val socketTypeConfigurationSection = configuration.getOrCreateSection("socket-types")
        val socketTypes =
            socketTypeConfigurationSection.getKeys(false).mapNotNull {
                MythicSocketType.fromConfigurationSection(
                    socketTypeConfigurationSection.getOrCreateSection(it),
                    it
                )
            }
        Log.info("Loaded socket types (${socketTypes.size}): ${socketTypes.joinToString { it.name }}")
        return socketTypes.toSet()
    }

    override fun get(): Set<SocketType> = managedSocketTypes.values.toSet()

    override fun contains(id: String): Boolean = managedSocketTypes.containsKey(id.lowercase(Locale.getDefault()))

    override fun getById(id: String): SocketType? = managedSocketTypes[id.lowercase(Locale.getDefault())]

    override fun add(toAdd: SocketType) {
        managedSocketTypes[toAdd.name.lowercase(Locale.getDefault())] = toAdd
    }

    override fun addAll(toAdd: Collection<SocketType>) {
        toAdd.forEach { add(it) }
    }

    override fun remove(id: String) {
        managedSocketTypes.remove(id.lowercase(Locale.getDefault()))
    }

    override fun random(): SocketType? = Choice.between(get()).choose()

    override fun randomByWeight(block: (SocketType) -> Boolean): SocketType? = WeightedChoice.between(get()).choose(block)

    override fun clear() {
        managedSocketTypes.clear()
    }
}
