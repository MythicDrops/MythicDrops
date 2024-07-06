package com.tealcube.minecraft.bukkit.mythicdrops.loading

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.loading.ConfigLoader
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.aura.AuraRunnable
import com.tealcube.minecraft.bukkit.mythicdrops.config.ConfigQualifiers
import com.tealcube.minecraft.bukkit.mythicdrops.config.Resources
import com.tealcube.minecraft.bukkit.mythicdrops.config.TierYamlConfigurations
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLogger
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap
import com.tealcube.minecraft.bukkit.mythicdrops.relations.MythicRelation
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItem
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombiner
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import org.bukkit.scheduler.BukkitTask
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
internal class MythicConfigLoader(
    private val auraRunnable: AuraRunnable,
    private val customItemManager: CustomItemManager,
    private val itemGroupManager: ItemGroupManager,
    private val loadingErrorManager: LoadingErrorManager,
    private val relationManager: RelationManager,
    private val repairItemManager: RepairItemManager,
    private val resources: Resources,
    private val settingsManager: SettingsManager,
    private val socketGemCombinerManager: SocketGemCombinerManager,
    private val socketGemManager: SocketGemManager,
    private val socketExtenderTypeManager: SocketExtenderTypeManager,
    private val socketTypeManager: SocketTypeManager,
    private val tierManager: TierManager,
    private val tierYamlConfigurations: TierYamlConfigurations,
    @Named(ConfigQualifiers.STARTUP)
    private val startupYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.ARMOR)
    private val armorYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.CONFIG)
    private val configYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.CUSTOM_ITEMS)
    private val customItemsYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.LANGUAGE)
    private val languageYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.CREATURE_SPAWNING)
    private val creatureSpawningYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.RELATION)
    private val relationYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.REPAIR_COSTS)
    private val repairCostsYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.REPAIRING)
    private val repairingYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.SOCKET_GEM_COMBINERS)
    private val socketGemCombinersYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.SOCKET_GEMS)
    private val socketGemsYamlConfiguration: VersionedFileAwareYamlConfiguration,
    @Named(ConfigQualifiers.SOCKET_TYPES)
    private val socketTypesYamlConfiguration: VersionedFileAwareYamlConfiguration,
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

    private var auraTask: BukkitTask? = null

    override fun reloadStartupSettings() {
        startupYamlConfiguration.load()
        settingsManager.loadStartupSettingsFromConfiguration(startupYamlConfiguration)

        Log.clearLoggers()
        val logLevel =
            if (settingsManager.startupSettings.isDebug) {
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
        Log.debug("Loading custom items...")
        customItemManager.clear()
        customItemsYamlConfiguration.load()
        customItemsYamlConfiguration.getKeys(false).forEach {
            if (!customItemsYamlConfiguration.isConfigurationSection(it)) {
                return@forEach
            }
            val customItemCs = customItemsYamlConfiguration.getOrCreateSection(it)
            val customItem = MythicCustomItem.fromConfigurationSection(customItemCs, it)
            if (customItem.material.isAir) {
                val message =
                    "Error when loading custom item ($it): material is equivalent to AIR: ${customItem.material}"
                Log.debug(message)
                loadingErrorManager.add(message)
                return@forEach
            }
            customItemManager.add(customItem)
        }
        Log.info("Loaded custom items: ${customItemManager.get().size}")
    }

    override fun saveCustomItems() {
        Log.debug("Saving custom items...")

        val version = customItemsYamlConfiguration.getNonNullString("version")
        customItemsYamlConfiguration.getKeys(false).forEach {
            customItemsYamlConfiguration[it] = null
        }
        customItemsYamlConfiguration["version"] = version

        customItemManager.get().forEach { customItem ->
            val name = customItem.name
            customItemsYamlConfiguration.set("$name.display-name", customItem.displayName)
            customItemsYamlConfiguration.set("$name.material", customItem.material.name)
            customItemsYamlConfiguration.set("$name.lore", customItem.lore)
            customItemsYamlConfiguration.set("$name.weight", customItem.weight)
            customItemsYamlConfiguration.set("$name.durability", customItem.durability)
            customItemsYamlConfiguration.set("$name.chance-to-drop-on-monster-death", customItem.chanceToDropOnDeath)
            customItemsYamlConfiguration.set("$name.broadcast-on-find", customItem.isBroadcastOnFind)
            customItemsYamlConfiguration.set("$name.custom-model-data", customItem.customModelData)
            customItem.enchantments.forEach {
                val enchKey = it.enchantment.key
                customItemsYamlConfiguration.set("$name.enchantments.$enchKey.minimum-level", it.minimumLevel)
                customItemsYamlConfiguration.set("$name.enchantments.$enchKey.maximum-level", it.maximumLevel)
            }
            customItem.attributes.forEach {
                customItemsYamlConfiguration.set("$name.attributes.${it.name}.attribute", it.attribute.name)
                customItemsYamlConfiguration.set("$name.attributes.${it.name}.minimum-amount", it.minimumAmount)
                customItemsYamlConfiguration.set("$name.attributes.${it.name}.maximum-amount", it.maximumAmount)
                customItemsYamlConfiguration.set("$name.attributes.${it.name}.operation", it.operation)
                customItemsYamlConfiguration.set("$name.attributes.${it.name}.slot", it.equipmentSlot)
            }

            val color = customItem.rgb
            customItemsYamlConfiguration.set("$name.rgb.red", color.red)
            customItemsYamlConfiguration.set("$name.rgb.green", color.green)
            customItemsYamlConfiguration.set("$name.rgb.blue", color.blue)
        }

        customItemsYamlConfiguration.save()
        Log.info("Saved custom items")
    }

    override fun reloadNames() {
        NameMap.clear()

        Log.debug("Loading prefixes...")
        val prefixes = resources.loadPrefixes()
        NameMap.putAll(prefixes)
        Log.info("Loaded prefixes: ${prefixes.values.flatten().size}")

        Log.debug("Loading suffixes...")
        val suffixes = resources.loadSuffixes()
        NameMap.putAll(suffixes)
        Log.info("Loaded suffixes: ${suffixes.values.flatten().size}")

        Log.debug("Loading lore...")
        val lore = resources.loadLore()
        NameMap.putAll(lore)
        Log.info("Loaded lore: ${lore.values.flatten().size}")

        Log.debug("Loading mob names...")
        val mobNames = resources.loadMobNames()
        NameMap.putAll(mobNames)
        Log.info("Loaded mob names: ${mobNames.values.flatten().size}")
    }

    override fun reloadRepairCosts() {
        Log.debug("Loading repair costs...")
        repairItemManager.clear()
        repairCostsYamlConfiguration.load()
        repairCostsYamlConfiguration
            .getKeys(false)
            .mapNotNull { key ->
                if (!repairCostsYamlConfiguration.isConfigurationSection(key)) {
                    return@mapNotNull null
                }

                val repairItemConfigurationSection = repairingYamlConfiguration.getOrCreateSection(key)
                MythicRepairItem.fromConfigurationSection(repairItemConfigurationSection, key, loadingErrorManager)
            }.forEach {
                repairItemManager.add(it)
            }
        Log.info("Loaded repair items: ${repairItemManager.get().size}")
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
        Log.debug("Loading socket gem combiners...")
        socketGemCombinerManager.clear()
        socketGemCombinersYamlConfiguration.load()
        socketGemCombinersYamlConfiguration.getKeys(false).forEach {
            if (!socketGemCombinersYamlConfiguration.isConfigurationSection(it)) return@forEach
            try {
                socketGemCombinerManager.add(
                    MythicSocketGemCombiner.fromConfigurationSection(
                        socketGemCombinersYamlConfiguration.getOrCreateSection(
                            it
                        ),
                        it
                    )
                )
            } catch (iae: IllegalArgumentException) {
                Log.error("Unable to load socket gem combiner with id=$it", iae)
                loadingErrorManager.add("Unable to load socket gem combiner with id=$it")
            }
        }
    }

    override fun saveSocketGemCombiners() {
        Log.debug("Saving socket gem combiners...")
        socketGemCombinersYamlConfiguration
            .getKeys(false)
            .forEach { socketGemCombinersYamlConfiguration[it] = null }
        socketGemCombinerManager.get().forEach {
            val key = it.uuid.toString()
            socketGemCombinersYamlConfiguration["$key.world"] = it.location.world.name
            socketGemCombinersYamlConfiguration["$key.x"] = it.location.x
            socketGemCombinersYamlConfiguration["$key.y"] = it.location.y
            socketGemCombinersYamlConfiguration["$key.z"] = it.location.z
        }
        socketGemCombinersYamlConfiguration.save()
        Log.info("Saved socket gem combiners")
    }

    override fun reloadSocketGems() {
        Log.debug("Loading socket types and socket extender types...")
        socketTypeManager.clear()
        socketExtenderTypeManager.clear()
        socketTypesYamlConfiguration.load()
        socketTypeManager.addAll(
            socketTypeManager.loadFromConfiguration(
                socketTypesYamlConfiguration
            )
        )
        socketExtenderTypeManager.addAll(
            socketExtenderTypeManager.loadFromConfiguration(
                socketTypesYamlConfiguration
            )
        )

        Log.debug("Loading socket gems...")
        socketGemManager.clear()
        socketGemsYamlConfiguration.load()
        socketGemManager.addAll(
            socketGemManager.loadFromConfiguration(socketGemsYamlConfiguration)
        )

        auraTask?.cancel()
        val isStartAuraRunnable =
            MythicDropsApi.mythicDrops.socketGemManager
                .get()
                .any { it.gemTriggerType == GemTriggerType.AURA }
        if (isStartAuraRunnable) {
            auraTask = auraRunnable.runTaskTimer()
            Log.info("Auras enabled")
        }
    }

    override fun reloadRelations() {
        Log.debug("Loading relations...")
        relationManager.clear()
        relationYamlConfiguration.load()
        relationYamlConfiguration.getKeys(false).forEach {
            if (!relationYamlConfiguration.isConfigurationSection(it)) return@forEach
            relationManager.add(MythicRelation.fromConfigurationSection(relationYamlConfiguration.getOrCreateSection(it), it))
        }
        Log.info("Loaded relations: ${relationManager.get().size}")
    }
}
