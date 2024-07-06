package com.tealcube.minecraft.bukkit.mythicdrops.loading

import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.loading.ConfigLoader
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.config.ConfigQualifiers
import com.tealcube.minecraft.bukkit.mythicdrops.config.TierYamlConfigurations
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLogger
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
internal class MythicConfigLoader(
    private val itemGroupManager: ItemGroupManager,
    private val loadingErrorManager: LoadingErrorManager,
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager,
    private val tierYamlConfigurations: TierYamlConfigurations,
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
    private val identifyingYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.ITEM_GROUPS)
    private val itemGroupsYamlConfiguration: VersionedFileAwareYamlConfiguration
) : ConfigLoader {
    companion object {
        private const val ALREADY_LOADED_TIER_MSG =
            "Not loading %s as there is already a tier with that display color and identifier color loaded: %s"
    }

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
        Log.debug("Loading tiers...")
        tierManager.clear()

        tierYamlConfigurations.load()
        tierYamlConfigurations.getTierYamlConfigurations().forEach { tierYaml ->
            tierYaml.load()
            Log.debug("Loading tier from ${tierYaml.fileName}...")
            val key = tierYaml.fileName.replace(".yml", "")

            if (tierManager.contains(key)) {
                val message = "Not loading $key as there is already a tier with that name loaded"
                Log.info(message)
                loadingErrorManager.add(message)
                return@forEach
            }

            val tier =
                MythicTier.fromConfigurationSection(tierYaml, key, itemGroupManager, loadingErrorManager)
                    ?: return@forEach

            // check if tier already exists with same color combination
            val preExistingTierWithColors = tierManager.getWithColors(tier.displayColor, tier.identifierColor)
            val disableLegacyItemChecks =
                settingsManager.configSettings.options.isDisableLegacyItemChecks
            if (preExistingTierWithColors != null && !disableLegacyItemChecks) {
                val message =
                    ALREADY_LOADED_TIER_MSG.format(
                        key,
                        preExistingTierWithColors.name
                    )
                Log.info(message)
                loadingErrorManager.add(message)
                return@forEach
            }

            tierManager.add(tier)
        }

        Log.info("Loaded tiers: ${tierManager.get().joinToString(prefix = "[", postfix = "]") { it.name }}")
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
        Log.debug("Loading item groups...")
        itemGroupManager.clear()

        itemGroupsYamlConfiguration.load()
        itemGroupsYamlConfiguration.getKeys(false).forEach { key ->
            if (!itemGroupsYamlConfiguration.isConfigurationSection(key)) {
                return@forEach
            }
            val itemGroupCs = itemGroupsYamlConfiguration.getOrCreateSection(key)
            itemGroupManager.add(MythicItemGroup.fromConfigurationSection(itemGroupCs, key))
        }
        Log.info("Loaded item groups: ${itemGroupManager.get().size}")
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
