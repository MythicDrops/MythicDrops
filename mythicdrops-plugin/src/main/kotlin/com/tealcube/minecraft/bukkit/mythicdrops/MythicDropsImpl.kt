package com.tealcube.minecraft.bukkit.mythicdrops

import com.github.shyiko.klob.Glob
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.config.ConfigManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ProductionLine
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.strategies.DropStrategyManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.repair.RepairItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGuiFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.aura.AuraRunnable
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicCustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicDropsLogger
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap
import com.tealcube.minecraft.bukkit.mythicdrops.relations.MythicRelation
import com.tealcube.minecraft.bukkit.mythicdrops.repair.MythicRepairItem
import com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners.MythicSocketGemCombiner
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import io.pixeloutlaw.kindling.Log
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Single
import java.util.Locale

@Single
internal class MythicDropsImpl(
    override val itemGroupManager: ItemGroupManager,
    override val socketGemCacheManager: SocketGemCacheManager,
    override val socketGemManager: SocketGemManager,
    override val socketGemCombinerManager: SocketGemCombinerManager,
    override val socketGemCombinerGuiFactory: SocketGemCombinerGuiFactory,
    override val settingsManager: SettingsManager,
    override val repairItemManager: RepairItemManager,
    override val customItemManager: CustomItemManager,
    override val relationManager: RelationManager,
    override val tierManager: TierManager,
    override val loadingErrorManager: LoadingErrorManager,
    override val customEnchantmentRegistry: CustomEnchantmentRegistry,
    override val dropStrategyManager: DropStrategyManager,
    override val productionLine: ProductionLine,
    override val configManager: ConfigManager,
    private val plugin: Plugin,
    private val auraRunnable: AuraRunnable
) : MythicDrops {
    override fun reloadSettings() {
        reloadStartupSettings()

        Log.debug("Clearing loading errors...")
        loadingErrorManager.clear()

        Log.debug("Loading settings from armor.yml...")
        configManager.armorYAML.load()
        settingsManager.loadArmorSettingsFromConfiguration(configManager.armorYAML)

        Log.debug("Loading settings from config.yml...")
        configManager.configYAML.load()
        settingsManager.loadConfigSettingsFromConfiguration(configManager.configYAML)

        Log.debug("Loading settings from language.yml...")
        configManager.languageYAML.load()
        settingsManager.loadLanguageSettingsFromConfiguration(configManager.languageYAML)

        Log.debug("Loading settings from creatureSpawning.yml...")
        configManager.creatureSpawningYAML.load()
        settingsManager.loadCreatureSpawningSettingsFromConfiguration(configManager.creatureSpawningYAML)

        Log.debug("Loading settings from repairing.yml...")
        configManager.repairingYAML.load()
        settingsManager.loadRepairingSettingsFromConfiguration(configManager.repairingYAML)

        Log.debug("Loading settings from socketing.yml...")
        configManager.socketingYAML.load()
        settingsManager.loadSocketingSettingsFromConfiguration(configManager.socketingYAML)

        Log.debug("Loading settings from identifying.yml...")
        configManager.identifyingYAML.load()
        settingsManager.loadIdentifyingSettingsFromConfiguration(configManager.identifyingYAML)
    }

    override fun reloadTiers() {
        Log.debug("Loading tiers...")
        tierManager.clear()

        configManager.tierYAMLs.forEach { tierYaml ->
            tierYaml.load()
            Log.debug("Loading tier from ${tierYaml.fileName}...")
            val key = tierYaml.fileName.replace(".yml", "")

            // check if tier with same name already exists
            if (tierManager.contains(key)) {
                val message = "Not loading $key as there is already a tier with that name loaded"
                Log.info(message)
                loadingErrorManager.add(message)
                return@forEach
            }

            val tier = MythicTier.fromConfigurationSection(tierYaml, key, itemGroupManager)

            tierManager.add(tier)
        }

        val tiers = tierManager.get().joinToString(prefix = "[", postfix = "]") { it.name }
        Log.info("Loaded tiers: $tiers")
    }

    override fun reloadCustomItems() {
        Log.debug("Loading custom items...")
        customItemManager.clear()
        val customItemYAML = configManager.customItemYAML
        customItemYAML.load()
        customItemYAML.getKeys(false).forEach {
            if (!customItemYAML.isConfigurationSection(it)) {
                return@forEach
            }
            val customItemCs = customItemYAML.getOrCreateSection(it)
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

    override fun reloadNames() {
        NameMap.clear()

        Log.debug("Loading prefixes...")
        val prefixes = loadPrefixes()
        NameMap.putAll(prefixes)
        Log.info("Loaded prefixes: ${prefixes.values.flatten().size}")

        Log.debug("Loading suffixes...")
        val suffixes = loadSuffixes()
        NameMap.putAll(suffixes)
        Log.info("Loaded suffixes: ${suffixes.values.flatten().size}")

        Log.debug("Loading lore...")
        val lore = loadLore()
        NameMap.putAll(lore)
        Log.info("Loaded lore: ${lore.values.flatten().size}")

        Log.debug("Loading mob names...")
        val mobNames = loadMobNames()
        NameMap.putAll(mobNames)
        Log.info("Loaded mob names: ${mobNames.values.flatten().size}")
    }

    override fun reloadRepairCosts() {
        Log.debug("Loading repair costs...")
        repairItemManager.clear()
        val repairCostsYAML = configManager.repairCostsYAML
        repairCostsYAML.load()
        repairCostsYAML.getKeys(false).mapNotNull { key ->
            if (!repairCostsYAML.isConfigurationSection(key)) {
                return@mapNotNull null
            }

            val repairItemConfigurationSection = repairCostsYAML.getOrCreateSection(key)
            MythicRepairItem.fromConfigurationSection(
                repairItemConfigurationSection,
                key,
                loadingErrorManager
            )
        }.forEach { repairItemManager.add(it) }
        Log.info("Loaded repair items: ${repairItemManager.get().size}")
    }

    override fun reloadItemGroups() {
        Log.debug("Loading item groups...")
        itemGroupManager.clear()
        val itemGroupYAML = configManager.itemGroupYAML
        itemGroupYAML.load()
        itemGroupYAML.getKeys(false).forEach { key ->
            if (!itemGroupYAML.isConfigurationSection(key)) {
                return@forEach
            }
            val itemGroupCs = itemGroupYAML.getOrCreateSection(key)
            itemGroupManager.add(MythicItemGroup.fromConfigurationSection(itemGroupCs, key))
        }
        Log.info("Loaded item groups: ${itemGroupManager.get().size}")
    }

    override fun reloadSocketGemCombiners() {
        Log.debug("Loading socket gem combiners...")
        socketGemCombinerManager.clear()
        val socketGemCombinersYAML = configManager.socketGemCombinersYAML
        socketGemCombinersYAML.load()
        socketGemCombinersYAML.getKeys(false).forEach {
            if (!socketGemCombinersYAML.isConfigurationSection(it)) return@forEach
            try {
                socketGemCombinerManager.add(
                    MythicSocketGemCombiner.fromConfigurationSection(
                        socketGemCombinersYAML.getOrCreateSection(
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
        val socketGemCombinersYAML = configManager.socketGemCombinersYAML
        socketGemCombinersYAML.getKeys(false).forEach { socketGemCombinersYAML[it] = null }
        socketGemCombinerManager.get().forEach {
            val key = it.uuid.toString()
            socketGemCombinersYAML["$key.world"] = it.location.world.name
            socketGemCombinersYAML["$key.x"] = it.location.x
            socketGemCombinersYAML["$key.y"] = it.location.y
            socketGemCombinersYAML["$key.z"] = it.location.z
        }
        socketGemCombinersYAML.save()
    }

    override fun reloadSocketGems() {
        Log.debug("Loading socket gems...")
        socketGemManager.clear()
        val socketGemsYAML = configManager.socketGemsYAML
        socketGemsYAML.load()
        socketGemManager.addAll(socketGemManager.loadFromConfiguration(socketGemsYAML))

        try {
            auraRunnable.cancel()
            auraRunnable.runTaskTimer(
                plugin,
                20,
                20 * settingsManager.socketingSettings.options.auraRefreshInSeconds.toLong()
            )
        } catch (ex: IllegalStateException) {
            Log.warn("Tried to cancel AuraRunnable that wasn't running yet")
        }
    }

    override fun reloadRelations() {
        Log.debug("Loading relations...")
        relationManager.clear()
        val relationYAML = configManager.relationYAML
        relationYAML.load()
        relationYAML.getKeys(false).forEach {
            if (!relationYAML.isConfigurationSection(it)) return@forEach
            relationManager.add(
                MythicRelation.fromConfigurationSection(
                    relationYAML.getOrCreateSection(
                        it
                    ),
                    it
                )
            )
        }
        Log.info("Loaded relations: ${relationManager.get().size}")
    }

    override fun reloadStartupSettings() {
        val startupYAML = configManager.startupYAML
        startupYAML.load()
        settingsManager.loadStartupSettingsFromConfiguration(startupYAML)

        Log.clearLoggers()
        val logLevel = if (settingsManager.startupSettings.debug) {
            Log.Level.DEBUG
        } else {
            Log.Level.INFO
        }
        Log.addLogger(MythicDropsLogger(logLevel))
    }

    override fun reloadAll() {
        reloadSettings()
        reloadItemGroups()
        reloadTiers()
        reloadNames()
        reloadCustomItems()
        reloadRepairCosts()
        reloadSocketGems()
        reloadRelations()
    }

    private fun loadPrefixes(): Map<out String, List<String>> {
        val prefixes = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/prefixes/general.txt").iterate(dataFolderAsPath).forEach {
            prefixes[NameType.GENERAL_PREFIX.format] = it.toFile().readLines()
        }

        Glob.from("resources/prefixes/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.MATERIAL_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ITEMTYPE_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        return prefixes
    }

    private fun loadSuffixes(): Map<out String, List<String>> {
        val suffixes = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/suffixes/general.txt").iterate(dataFolderAsPath).forEach {
            suffixes[NameType.GENERAL_SUFFIX.format] = it.toFile().readLines()
        }

        Glob.from("resources/suffixes/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.MATERIAL_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ITEMTYPE_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        return suffixes
    }

    private fun loadLore(): Map<out String, List<String>> {
        val lore = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/lore/general.txt").iterate(dataFolderAsPath).forEach {
            lore[NameType.GENERAL_LORE.format] = it.toFile().readLines()
        }

        Glob.from("resources/lore/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.MATERIAL_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.ITEMTYPE_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        return lore
    }

    private fun loadMobNames(): Map<out String, List<String>> {
        val mobNames = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/mobnames/general.txt").iterate(dataFolderAsPath).forEach {
            mobNames[NameType.GENERAL_MOB_NAME.format] = it.toFile().readLines()
        }

        Glob.from("resources/mobnames/*.txt", "!resources/mobnames/general.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.SPECIFIC_MOB_NAME.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            mobNames[key] = file.readLines()
        }

        return mobNames
    }
}
