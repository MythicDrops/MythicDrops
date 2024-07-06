package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import dev.mythicdrops.spigot.choices.Choice
import dev.mythicdrops.spigot.choices.WeightedChoice
import io.pixeloutlaw.kindling.Log
import org.bukkit.configuration.Configuration
import java.util.Locale

internal class MythicSocketExtenderTypeManager(
    private val loadingErrorManager: LoadingErrorManager,
    private val socketTypeManager: SocketTypeManager
) : SocketExtenderTypeManager {
    private val managedSocketExtenderTypes = mutableMapOf<String, SocketExtenderType>()

    override fun loadFromConfiguration(configuration: Configuration): Set<SocketExtenderType> {
        Log.debug("Loading socket extender types")
        val socketExtenderTypeConfigurationSection = configuration.getOrCreateSection("socket-extender-types")
        val socketExtenderTypes =
            socketExtenderTypeConfigurationSection.getKeys(false).mapNotNull {
                MythicSocketExtenderType.fromConfigurationSection(
                    socketExtenderTypeConfigurationSection.getOrCreateSection(it),
                    it,
                    socketTypeManager,
                    loadingErrorManager
                )
            }
        val amountOfSocketExtenderTypes = "(${socketExtenderTypes.size})"
        val joinedSocketExtenderTypeNames = socketExtenderTypes.joinToString { it.name }
        Log.info("Loaded socket extender types $amountOfSocketExtenderTypes: $joinedSocketExtenderTypeNames")
        return socketExtenderTypes.toSet()
    }

    override fun get(): Set<SocketExtenderType> = managedSocketExtenderTypes.values.toSet()

    override fun contains(id: String): Boolean = managedSocketExtenderTypes.containsKey(id.lowercase(Locale.getDefault()))

    override fun getById(id: String): SocketExtenderType? = managedSocketExtenderTypes[id.lowercase(Locale.getDefault())]

    override fun add(toAdd: SocketExtenderType) {
        managedSocketExtenderTypes[toAdd.name.lowercase(Locale.getDefault())] = toAdd
    }

    override fun addAll(toAdd: Collection<SocketExtenderType>) {
        toAdd.forEach { add(it) }
    }

    override fun remove(id: String) {
        managedSocketExtenderTypes.remove(id.lowercase(Locale.getDefault()))
    }

    override fun random(): SocketExtenderType? = Choice.between(get()).choose()

    override fun randomByWeight(block: (SocketExtenderType) -> Boolean): SocketExtenderType? = WeightedChoice.between(get()).choose(block)

    override fun clear() {
        managedSocketExtenderTypes.clear()
    }
}
