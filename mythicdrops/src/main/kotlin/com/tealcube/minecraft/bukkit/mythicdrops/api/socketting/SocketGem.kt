/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketting

import com.google.common.base.Joiner
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EntityUtil
import org.apache.commons.text.WordUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType

/**
 * Holds information about a Socket Gem.
 *
 * @property name Name of Socket Gem (appears in item lore)
 * @property weight Weight of Socket Gem (defaults to 0.0)
 * @property prefix Prefix of Socket Gem (defaults to empty string)
 * @property suffix Suffix of Socket Gem (defaults to empty string)
 * @property lore Lore of Socket Gem (defaults to empty list)
 * @property socketEffects Socket Effects of Socket Gem (defaults to empty set)
 * @property itemGroups Item Groups of Socket Gem (defaults to empty list)
 * @property gemTriggerType Gem Type of Socket Gem (defaults to [GemTriggerType.ON_HIT_AND_WHEN_HIT])
 * @property enchantments Enchantments of Socket Gem (defaults to empty set)
 * @property commands Commands of Socket Gem (defaults to empty list)
 * @property entityTypesCanDropFrom Entity Types that the Socket Gem can drop from (defaults to empty list)
 */
data class SocketGem(
    val name: String,
    override val weight: Double = 0.0,
    val prefix: String = "",
    val suffix: String = "",
    val lore: List<String> = emptyList(),
    val socketEffects: Set<SocketEffect> = emptySet(),
    val itemGroups: List<ItemGroup> = emptyList(),
    val gemTriggerType: GemTriggerType = GemTriggerType.ON_HIT_AND_WHEN_HIT,
    val enchantments: Set<MythicEnchantment> = emptySet(),
    val commands: List<SocketCommand> = emptyList(),
    val entityTypesCanDropFrom: Set<EntityType> = emptySet()
) : Weighted {
    companion object {
        private const val potionEffectsString = "potionEffects"
        private const val particleEffectsString = "particleEffects"

        /**
         * Constructs a Socket Gem from a given [ConfigurationSection] and its associated [key].
         *
         * @param configurationSection ConfigurationSection of a YAML file
         * @param key Key for the given [configurationSection]
         * @param itemGroupManager ItemGroupManager for getting available [ItemGroup]s
         *
         * @return constructed Socket Gem
         */
        @JvmStatic
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            itemGroupManager: ItemGroupManager
        ): SocketGem {
            val weight = configurationSection.getDouble("weight")
            val prefix = configurationSection.getString("prefix") ?: ""
            val suffix = configurationSection.getString("suffix") ?: ""
            val lore = configurationSection.getStringList("lore")
            val socketParticleEffects = buildSocketParticleEffects(configurationSection)
            val socketPotionEffects = buildSocketPotionEffects(configurationSection)
            val socketEffects: Set<SocketEffect> = (socketParticleEffects + socketPotionEffects).toSet()
            val itemGroups = configurationSection.getStringList("itemGroups").mapNotNull {
                itemGroupManager.getItemGroup(it)
            }
            val gemTriggerType = GemTriggerType.fromName(configurationSection.getString("triggerType"))
            val enchantments = configurationSection.getConfigurationSection("enchantments")?.let { enchantmentsCs ->
                // for each item in the enchantments configuration section, we need to support both the standard
                // setup and the range setup.
                enchantmentsCs.getKeys(false).mapNotNull { enchantmentKey ->
                    if (enchantmentsCs.isConfigurationSection(enchantmentKey)) {
                        val enchantmentCs =
                            enchantmentsCs.getConfigurationSection(enchantmentKey) ?: return@mapNotNull null
                        MythicEnchantment.fromConfigurationSection(enchantmentCs, enchantmentKey)
                    } else {
                        val enchantment = Enchantment.getByName(enchantmentKey) ?: return@mapNotNull null
                        MythicEnchantment(enchantment, enchantmentsCs.getInt(enchantmentKey))
                    }
                }.toSet()
            } ?: emptySet()
            val commands = configurationSection.getStringList("commands").map { SocketCommand(it) }
            val entityTypesCanDropFrom =
                configurationSection.getStringList("entityTypesCanDropFrom").mapNotNull { EntityUtil.getEntityType(it) }
                    .toSet()
            return SocketGem(
                key,
                weight,
                prefix,
                suffix,
                lore,
                socketEffects,
                itemGroups,
                gemTriggerType,
                enchantments,
                commands,
                entityTypesCanDropFrom
            )
        }

        private fun buildSocketParticleEffects(configurationSection: ConfigurationSection): List<SocketParticleEffect> {
            if (!configurationSection.isConfigurationSection("particleEffects")) {
                return emptyList()
            }
            return configurationSection.getConfigurationSection("particleEffects")?.let {
                return it.getKeys(false).mapNotNull { key -> SocketParticleEffect.fromConfigurationSection(it, key) }
            } ?: emptyList()
        }

        private fun buildSocketPotionEffects(configurationSection: ConfigurationSection): List<SocketPotionEffect> {
            if (!configurationSection.isConfigurationSection("potionEffects")) {
                return emptyList()
            }
            return configurationSection.getConfigurationSection("potionEffects")?.let {
                return it.getKeys(false).mapNotNull { key -> SocketPotionEffect.fromConfigurationSection(it, key) }
            } ?: emptyList()
        }
    }

    /**
     * Determines if this can drop from a given [EntityType].
     *
     * @return true if it can drop, false otherwise
     */
    fun canDropFrom(entityType: EntityType): Boolean {
        return entityTypesCanDropFrom.isEmpty() || entityTypesCanDropFrom.contains(entityType)
    }

    /**
     * Gets the presentable type for the gem. Combines all item groups into one phrase.
     *
     * For instance, if the gem has the item groups "diamond" and "melee", this will return "Diamond Melee".
     *
     * If the item has no item groups, it will return "Any".
     *
     * @return joined item groups or "Any" if no item groups
     */
    fun getPresentableType(): String = if (itemGroups.isNotEmpty()) {
        WordUtils.capitalizeFully(Joiner.on(" ").skipNulls().join(itemGroups.map { it.name }))
    } else {
        "Any"
    }
}