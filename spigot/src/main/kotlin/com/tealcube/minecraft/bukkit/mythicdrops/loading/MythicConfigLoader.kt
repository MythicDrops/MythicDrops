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
    private val startupYamlConfiguration: VersionedFileAwareYamlConfiguration
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

        TODO("Not yet implemented")
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
