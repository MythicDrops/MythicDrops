package com.tealcube.minecraft.bukkit.mythicdrops.loading

import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.loading.ConfigLoader
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.config.ConfigQualifiers
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLogger
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
internal class MythicConfigLoader(
    private val loadingErrorManager: LoadingErrorManager,
    private val settingsManager: SettingsManager,
    @Named(ConfigQualifiers.STARTUP)
    private val startupYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.ARMOR)
    private val armorYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.CONFIG)
    private val configYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.LANGUAGE)
    private val languageYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.CREATURE_SPAWNING)
    private val creatureSpawningYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.REPAIRING)
    private val repairingYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.SOCKETING)
    private val socketingYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.IDENTIFYING)
    private val identifyingYamlConfiguration: VersionedFileAwareYamlConfiguration
) : ConfigLoader {
    override fun reloadStartupSettings() {
        startupYamlConfiguration.load()
        settingsManager.loadStartupSettingsFromConfiguration(startupYamlConfiguration)

        Log.clearLoggers()
        val logLevel = if (settingsManager.startupSettings.isDebug) {
            Log.Level.DEBUG
        } else {
            Log.Level.INFO
        }
        Log.addLogger(MythicDropsLogger(logLevel))
    }

    override fun reloadSettings() {
        reloadStartupSettings()

        Log.debug("Clearing loading errors...")
        loadingErrorManager.clear()

        Log.debug("Loading settings from armor.yml...")
        armorYamlConfiguration.load()
        settingsManager.loadArmorSettingsFromConfiguration(armorYamlConfiguration)

        Log.debug("Loading settings from config.yml...")
        configYamlConfiguration.load()
        settingsManager.loadConfigSettingsFromConfiguration(configYamlConfiguration)

        if (!settingsManager.configSettings.options.isDisableLegacyItemChecks) {
            val disableLegacyItemChecksWarning =
                """
                Legacy item checks (checking the lore of items) are not disabled! This feature is deprecated and will be removed in MythicDrops 9.x.
                """.trimIndent()
            Log.warn(disableLegacyItemChecksWarning)
        }

        Log.debug("Loading settings from language.yml...")
        languageYamlConfiguration.load()
        settingsManager.loadLanguageSettingsFromConfiguration(languageYamlConfiguration)

        Log.debug("Loading settings from creatureSpawning.yml...")
        creatureSpawningYamlConfiguration.load()
        settingsManager.loadCreatureSpawningSettingsFromConfiguration(creatureSpawningYamlConfiguration)

        Log.debug("Loading settings from repairing.yml...")
        repairingYamlConfiguration.load()
        settingsManager.loadRepairingSettingsFromConfiguration(repairingYamlConfiguration)

        Log.debug("Loading settings from socketing.yml...")
        socketingYamlConfiguration.load()
        settingsManager.loadSocketingSettingsFromConfiguration(socketingYamlConfiguration)

        Log.debug("Loading settings from identifying.yml...")
        identifyingYamlConfiguration.load()
        settingsManager.loadIdentifyingSettingsFromConfiguration(identifyingYamlConfiguration)
    }

    override fun reloadTiers() {
        TODO("Not yet implemented")
    }

    override fun reloadCustomItems() {
        TODO("Not yet implemented")
    }

    override fun reloadNames() {
        TODO("Not yet implemented")
    }

    override fun reloadRepairCosts() {
        TODO("Not yet implemented")
    }

    override fun reloadItemGroups() {
        TODO("Not yet implemented")
    }

    override fun reloadSocketGemCombiners() {
        TODO("Not yet implemented")
    }

    override fun saveSocketGemCombiners() {
        TODO("Not yet implemented")
    }

    override fun reloadSocketGems() {
        TODO("Not yet implemented")
    }

    override fun reloadRelations() {
        TODO("Not yet implemented")
    }
}
