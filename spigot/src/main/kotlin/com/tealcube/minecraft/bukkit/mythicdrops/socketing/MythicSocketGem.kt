/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.attributes.MythicMythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.enchantments.MythicMythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.orIfEmpty
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import io.pixeloutlaw.minecraft.spigot.mythicdrops.toTitleCase
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemFlag

internal data class MythicSocketGem(
    override val name: String,
    override val weight: Double = 0.0,
    override val prefix: String = "",
    override val suffix: String = "",
    override val lore: List<String> = emptyList(),
    override val socketEffects: Set<SocketEffect> = emptySet(),
    override val anyOfItemGroups: List<ItemGroup> = emptyList(),
    override val allOfItemGroups: List<ItemGroup> = emptyList(),
    override val noneOfItemGroups: List<ItemGroup> = emptyList(),
    override val gemTriggerType: GemTriggerType = GemTriggerType.ON_HIT_AND_WHEN_HIT,
    override val enchantments: Set<MythicEnchantment> = emptySet(),
    override val commands: List<SocketCommand> = emptyList(),
    override val entityTypesCanDropFrom: Set<EntityType> = emptySet(),
    override val family: String = "",
    override val level: Int = 0,
    override val attributes: Set<MythicAttribute> = emptySet(),
    override val isBroadcastOnFind: Boolean = false,
    override val hasCustomModelData: Boolean = false,
    override val customModelData: Int = 0,
    override val socketType: SocketType,
    override val itemFlags: Set<ItemFlag> = emptySet()
) : SocketGem {
    companion object {
        private const val POTION_EFFECTS_STRING = "potion-effects"
        private const val PARTICLE_EFFECTS_STRING = "particle-effects"

        @JvmStatic
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            itemGroupManager: ItemGroupManager,
            loadingErrorManager: LoadingErrorManager,
            socketTypeManager: SocketTypeManager
        ): MythicSocketGem? {
            val weight = configurationSection.getDouble("weight")
            val prefix = configurationSection.getString("prefix") ?: ""
            val suffix = configurationSection.getString("suffix") ?: ""
            val lore = configurationSection.getStringList("lore")
            val socketParticleEffects = buildSocketParticleEffects(configurationSection)
            val socketPotionEffects = buildSocketPotionEffects(configurationSection)
            val socketEffects: Set<SocketEffect> = (socketParticleEffects + socketPotionEffects).toSet()
            val itemGroups =
                if (configurationSection.isList("item-groups")) {
                    configurationSection.getStringList("item-groups").mapNotNull {
                        itemGroupManager.getById(it)
                    }
                } else {
                    emptyList()
                }
            val allOfItemGroups =
                if (configurationSection.isList("all-of-item-groups")) {
                    configurationSection.getStringList("all-of-item-groups").mapNotNull {
                        itemGroupManager.getById(it)
                    }
                } else {
                    emptyList()
                }
            val anyOfItemGroups =
                if (configurationSection.isList("any-of-item-groups")) {
                    configurationSection.getStringList("any-of-item-groups").mapNotNull {
                        itemGroupManager.getById(it)
                    }
                } else {
                    emptyList()
                }
            val noneOfItemGroups =
                if (configurationSection.isList("none-of-item-groups")) {
                    configurationSection.getStringList("none-of-item-groups").mapNotNull {
                        itemGroupManager.getById(it)
                    }
                } else {
                    emptyList()
                }
            val gemTriggerType = GemTriggerType.fromName(configurationSection.getString("trigger-type"))
            val enchantments =
                configurationSection.getConfigurationSection("enchantments")?.let { enchantmentsCs ->
                    // for each item in the enchantments configuration section, we need to support both the standard
                    // setup and the range setup.
                    enchantmentsCs.getKeys(false).mapNotNull { enchantmentKey ->
                        if (enchantmentsCs.isConfigurationSection(enchantmentKey)) {
                            val enchantmentCs =
                                enchantmentsCs.getConfigurationSection(enchantmentKey) ?: return@mapNotNull null
                            MythicMythicEnchantment.fromConfigurationSection(enchantmentCs, enchantmentKey)
                        } else {
                            val enchantment = EnchantmentUtil.getByKeyOrName(enchantmentKey) ?: return@mapNotNull null
                            MythicMythicEnchantment(enchantment, enchantmentsCs.getInt(enchantmentKey))
                        }
                    }.toSet()
                } ?: emptySet()
            val commands = loadSocketCommands(configurationSection)
            val entityTypesCanDropFrom =
                configurationSection.getStringList("entity-types-can-drop-from")
                    .mapNotNull {
                        enumValueOrNull<EntityType>(
                            it
                        )
                    }
                    .toSet()
            val family = configurationSection.getString("family") ?: ""
            val level = configurationSection.getInt("level")
            val attributesConfigurationSection = configurationSection.getOrCreateSection("attributes")
            val attributes =
                attributesConfigurationSection.getKeys(false).mapNotNull { attrKey ->
                    val attrCS = attributesConfigurationSection.getOrCreateSection(attrKey)
                    MythicMythicAttribute.fromConfigurationSection(attrCS, attrKey)
                }.toSet()
            val isBroadcastOnFind = configurationSection.getBoolean("broadcast-on-find")
            val hasCustomModelData = configurationSection.isInt("custom-model-data")
            val customModelData = configurationSection.getInt("custom-model-data", 0)
            val itemFlags =
                configurationSection.getStringList("item-flags").mapNotNull { enumValueOrNull<ItemFlag>(it) }.toSet()
            val rawSocketType = configurationSection.getNonNullString("socket-type")
            val socketType: SocketType? = socketTypeManager.getById(rawSocketType)
            if (socketType == null) {
                Log.debug("socketType == null: key=$key rawSocketType=$rawSocketType")
                loadingErrorManager.add(
                    "Not loading socket gem $key as there isn't a socket type by name: '$rawSocketType'"
                )
                return null
            }
            return MythicSocketGem(
                name = key,
                weight = weight,
                prefix = prefix,
                suffix = suffix,
                lore = lore,
                socketEffects = socketEffects,
                anyOfItemGroups = anyOfItemGroups,
                allOfItemGroups = allOfItemGroups.orIfEmpty(itemGroups),
                noneOfItemGroups = noneOfItemGroups,
                gemTriggerType = gemTriggerType,
                enchantments = enchantments,
                commands = commands,
                entityTypesCanDropFrom = entityTypesCanDropFrom,
                family = family,
                level = level,
                attributes = attributes,
                isBroadcastOnFind = isBroadcastOnFind,
                hasCustomModelData = hasCustomModelData,
                customModelData = customModelData,
                socketType = socketType,
                itemFlags = itemFlags
            )
        }

        private fun loadSocketCommands(configurationSection: ConfigurationSection): List<SocketCommand> {
            return when {
                configurationSection.isConfigurationSection("commands") -> {
                    val commandsConfigurationSection = configurationSection.getOrCreateSection("commands")
                    val commandKeys = commandsConfigurationSection.getKeys(false)
                    commandKeys.mapNotNull { commandsConfigurationSection.getConfigurationSection(it) }
                        .map(MythicSocketCommand.Companion::fromConfigurationSection)
                }

                configurationSection.isList("commands") -> {
                    configurationSection.getStringList("commands").map(MythicSocketCommand.Companion::fromString)
                }

                else -> {
                    emptyList()
                }
            }
        }

        private fun buildSocketParticleEffects(configurationSection: ConfigurationSection): List<SocketParticleEffect> {
            if (!configurationSection.isConfigurationSection(PARTICLE_EFFECTS_STRING)) {
                return emptyList()
            }
            return configurationSection.getConfigurationSection(PARTICLE_EFFECTS_STRING)?.let {
                it.getKeys(false).mapNotNull { key -> SocketParticleEffect.fromConfigurationSection(it, key) }
            } ?: emptyList()
        }

        private fun buildSocketPotionEffects(configurationSection: ConfigurationSection): List<SocketPotionEffect> {
            if (!configurationSection.isConfigurationSection(POTION_EFFECTS_STRING)) {
                return emptyList()
            }
            return configurationSection.getConfigurationSection(POTION_EFFECTS_STRING)?.let {
                it.getKeys(false).mapNotNull { key -> SocketPotionEffect.fromConfigurationSection(it, key) }
            } ?: emptyList()
        }
    }

    override fun canDropFrom(entityType: EntityType): Boolean {
        return entityTypesCanDropFrom.isEmpty() || entityTypesCanDropFrom.contains(entityType)
    }

    @Deprecated(
        "Use getPresentableItemGroupType instead.",
        replaceWith = ReplaceWith("getPresentableItemGroupType(allOfLore, anyOfLore, noneOfLore)")
    )
    override fun getPresentableType(
        allOfLore: List<String>,
        anyOfLore: List<String>,
        noneOfLore: List<String>
    ): List<String> = getPresentableItemGroupType(allOfLore, anyOfLore, noneOfLore)

    override fun getPresentableItemGroupType(
        allOfLore: List<String>,
        anyOfLore: List<String>,
        noneOfLore: List<String>
    ): List<String> {
        val replacedAllOfLore = determineReplacedLore(allOfLore, allOfItemGroups)
        val replacedAnyOfLore = determineReplacedLore(anyOfLore, anyOfItemGroups)
        val replacedNoneOfLore = determineReplacedLore(noneOfLore, noneOfItemGroups)
        return replacedAllOfLore + replacedAnyOfLore + replacedNoneOfLore
    }

    private fun determineReplacedLore(
        lore: List<String>,
        itemGroups: List<ItemGroup>
    ): List<String> {
        return if (itemGroups.isNotEmpty()) {
            lore.map { loreLine ->
                loreLine.replaceArgs(
                    "%itemgroup%" to itemGroups.joinToString(" ") { it.name }.toTitleCase()
                )
            }
        } else {
            emptyList()
        }
    }
}
