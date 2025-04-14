package dev.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import org.bukkit.NamespacedKey
import org.koin.core.annotation.Single

@Single
internal class NamespacedKeys(
    private val mythicDropsPlugin: MythicDropsPlugin
) {
    /**
     * The [NamespacedKey] used for storing which custom item is used.
     */
    internal val customItem = mythicDrops("custom-item")

    /**
     * The [NamespacedKey] used for storing which socket gem is used.
     */
    internal val socketGem = mythicDrops("socket-gem")

    /**
     * The [NamespacedKey] used for storing which tier is used.
     */
    internal val tier = mythicDrops("tier")

    /**
     * The [NamespacedKey] used for storing if the current item is a socket extender.
     */
    internal val socketExtender = mythicDrops("socket-extender")

    /**
     * The [NamespacedKey] used for storing if the current item is a socket extender.
     */
    internal val socketExtendersUsed = mythicDrops("socket-extenders-used")

    internal val hackHideAttributes = mythicDrops("hack-hide-attributes")

    /**
     * Creates a [NamespacedKey] in the "MythicDrops" namespace in a "Spigot Approved"^TM way.
     *
     * @param key
     */
    private fun mythicDrops(key: String): NamespacedKey = NamespacedKey(mythicDropsPlugin, key)
}
