package io.pixeloutlaw.minecraft.spigot.config.migration.steps.post

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import com.tealcube.minecraft.bukkit.mythicdrops.unChatColorize
import org.bukkit.configuration.ConfigurationSection

object DeprecateDisplayColorAndIdentifierColorConfigMigrationStep : PostConfigMigrationStep {
    @JvmStatic
    @Suppress("UNUSED_PARAMETER")
    fun deserialize(map: Map<String, Any>): DeprecateDisplayColorAndIdentifierColorConfigMigrationStep {
        return DeprecateDisplayColorAndIdentifierColorConfigMigrationStep
    }

    @Suppress("DEPRECATION")
    override fun migrate(configuration: ConfigurationSection, mythicDrops: MythicDrops) {
        val tierFromConfiguration = MythicTier.fromConfigurationSection(configuration)
        val existingItemDisplayNameFormat = tierFromConfiguration.itemDisplayNameFormat
        val updatedItemDisplayNameFormat = if (existingItemDisplayNameFormat.isBlank()) {
            val tierPrefix = "${tierFromConfiguration.displayColor}"
            val tierSuffix = "${tierFromConfiguration.identifierColor}"
            val body = mythicDrops.settingsManager.configSettings.display.itemDisplayNameFormat.chatColorize()
            "$tierPrefix$body$tierSuffix".unChatColorize()
        } else {
            val tierPrefix = "${tierFromConfiguration.displayColor}"
            val tierSuffix = "${tierFromConfiguration.identifierColor}"
            val body = existingItemDisplayNameFormat.chatColorize()
            "$tierPrefix$body$tierSuffix".unChatColorize()
        }
        configuration["item-display-name-format"] = updatedItemDisplayNameFormat
        configuration["display-color"] = null
        configuration["identifier-color"] = null
    }

    override fun serialize(): MutableMap<String, Any> = mutableMapOf()
}