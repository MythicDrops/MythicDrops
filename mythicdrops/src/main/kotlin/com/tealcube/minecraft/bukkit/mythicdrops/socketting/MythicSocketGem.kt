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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting

import com.google.common.base.Joiner
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketParticleEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketPotionEffect
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EntityUtil
import org.apache.commons.text.WordUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType

data class MythicSocketGem(
    override val name: String,
    override val weight: Double = 0.0,
    override val prefix: String = "",
    override val suffix: String = "",
    override val lore: List<String> = emptyList(),
    override val socketEffects: Set<SocketEffect> = emptySet(),
    override val itemGroups: List<ItemGroup> = emptyList(),
    override val gemTriggerType: GemTriggerType = GemTriggerType.ON_HIT_AND_WHEN_HIT,
    override val enchantments: Set<MythicEnchantment> = emptySet(),
    override val commands: List<SocketCommand> = emptyList(),
    override val entityTypesCanDropFrom: Set<EntityType> = emptySet(),
    override val family: String = "",
    override val level: Int = 0
) : SocketGem {
    companion object {
        private const val potionEffectsString = "potionEffects"
        private const val particleEffectsString = "particleEffects"

        @JvmStatic
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            itemGroupManager: ItemGroupManager
        ): MythicSocketGem {
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
            val family = configurationSection.getString("family") ?: ""
            val level = configurationSection.getInt("level")
            return MythicSocketGem(
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
                entityTypesCanDropFrom,
                family,
                level
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

    override fun canDropFrom(entityType: EntityType): Boolean {
        return entityTypesCanDropFrom.isEmpty() || entityTypesCanDropFrom.contains(entityType)
    }

    override fun getPresentableType(): String = if (itemGroups.isNotEmpty()) {
        WordUtils.capitalizeFully(Joiner.on(" ").skipNulls().join(itemGroups.map { it.name }))
    } else {
        "Any"
    }
}
