package com.tealcube.minecraft.bukkit.mythicdrops.config

import org.koin.core.qualifier.named

internal object ConfigQualifiers {
    val armor by lazy { named(armorName) }
    val config by lazy { named(configName) }
    val creatureSpawning by lazy { named(creatureSpawningName) }
    val customItems by lazy { named(customItemsName) }
    val identifying by lazy { named(identifyingName) }
    val itemGroups by lazy { named(itemGroupsName) }
    val language by lazy { named(languageName) }
    val relation by lazy { named(relationName) }
    val repairCosts by lazy { named(repairCostsName) }
    val repairing by lazy { named(repairingName) }
    val socketGemCombiners by lazy { named(socketGemCombinersName) }
    val socketGems by lazy { named(socketGemsName) }
    val socketing by lazy { named(socketingName) }
    val startup by lazy { named(startupName) }
    val tiers by lazy { named(tiersName) }

    private const val armorName = "armor"
    private const val configName = "config"
    private const val creatureSpawningName = "creatureSpawning"
    private const val customItemsName = "customItems"
    private const val identifyingName = "identifying"
    private const val itemGroupsName = "itemGroups"
    private const val languageName = "language"
    private const val relationName = "relation"
    private const val repairCostsName = "repairCosts"
    private const val repairingName = "repairing"
    private const val socketGemCombinersName = "socketGemCombiners"
    private const val socketGemsName = "socketGems"
    private const val socketingName = "socketing"
    private const val startupName = "startup"
    private const val tiersName = "tiers"
}
