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
package com.tealcube.minecraft.bukkit.mythicdrops.items

import com.tealcube.minecraft.bukkit.mythicdrops.DEFAULT_REPAIR_COST
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi.mythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsRepairable
import com.tealcube.minecraft.bukkit.mythicdrops.getMaterial
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.hdb.HeadDatabaseAdapter
import com.tealcube.minecraft.bukkit.mythicdrops.unChatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.customModelData
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getAttributeModifiers
import io.pixeloutlaw.minecraft.spigot.mythicdrops.hasCustomModelData
import io.pixeloutlaw.minecraft.spigot.mythicdrops.isUnbreakable
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

internal data class MythicCustomItem(
    override val name: String = "",
    override val displayName: String = "",
    override val chanceToDropOnDeath: Double = 0.0,
    override val enchantments: Set<MythicEnchantment> = emptySet(),
    override val lore: List<String> = emptyList(),
    override val material: Material = Material.AIR,
    override val isBroadcastOnFind: Boolean = false,
    override val hasDurability: Boolean = false,
    override val durability: Int = 0,
    override val hasCustomModelData: Boolean = false,
    override val customModelData: Int = 0,
    override val isUnbreakable: Boolean = false,
    override val weight: Double = 0.0,
    override val attributes: Set<MythicAttribute> = emptySet(),
    override val isGlow: Boolean = false,
    override val itemFlags: Set<ItemFlag> = emptySet(),
    override val repairCost: Int = DEFAULT_REPAIR_COST,
    override val isEnchantmentsRemovableByGrindstone: Boolean = true,
    override val isAddDefaultAttributes: Boolean = false,
    override val hdbId: String = "",
    override val rgb: CustomItem.Rgb = CustomItem.Rgb(-1, -1, -1)
) : CustomItem {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection, key: String): MythicCustomItem {
            val enchantmentsConfigurationSection = configurationSection.getOrCreateSection("enchantments")
            val mythicEnchantments = enchantmentsConfigurationSection.getKeys(false).mapNotNull { enchKey ->
                EnchantmentUtil.getByKeyOrName(enchKey)?.let { enchantment ->
                    if (enchantmentsConfigurationSection.isConfigurationSection(enchKey)) {
                        val enchantmentConfigurationSection =
                            enchantmentsConfigurationSection.getOrCreateSection(enchKey)
                        val minimumLevel = enchantmentConfigurationSection.getInt("minimum-level")
                        val maximumLevel = enchantmentConfigurationSection.getInt("maximum-level")
                        MythicEnchantment(enchantment, minimumLevel, maximumLevel)
                    } else {
                        MythicEnchantment(enchantment, enchantmentsConfigurationSection.getInt(enchKey))
                    }
                }
            }.toSet()
            val attributesConfigurationSection = configurationSection.getOrCreateSection("attributes")
            val attributes = attributesConfigurationSection.getKeys(false).mapNotNull { attrKey ->
                val attrCS = attributesConfigurationSection.getOrCreateSection(attrKey)
                MythicAttribute.fromConfigurationSection(attrCS, attrKey)
            }.toSet()
            val itemFlags =
                configurationSection.getStringList("item-flags").mapNotNull {
                    enumValueOrNull<ItemFlag>(
                        it
                    )
                }.toSet()
            val isEnchantmentsRemovableByGrindstone =
                configurationSection.getBoolean("enchantments-removable-by-grindstone", true)
            val isAddDefaultAttributes =
                configurationSection.getBoolean("add-default-attributes", false)
            return MythicCustomItem(
                name = key,
                displayName = configurationSection.getNonNullString("display-name"),
                chanceToDropOnDeath = configurationSection.getDouble("chance-to-drop-on-monster-death"),
                enchantments = mythicEnchantments,
                lore = configurationSection.getStringList("lore"),
                material = configurationSection.getMaterial("material"),
                isBroadcastOnFind = configurationSection.getBoolean("broadcast-on-find"),
                hasDurability = configurationSection.contains("durability"),
                durability = configurationSection.getInt("durability"),
                hasCustomModelData = configurationSection.contains("custom-model-data"),
                customModelData = configurationSection.getInt("custom-model-data"),
                isUnbreakable = configurationSection.getBoolean("unbreakable"),
                weight = configurationSection.getDouble("weight"),
                attributes = attributes,
                isGlow = configurationSection.getBoolean("glow"),
                itemFlags = itemFlags,
                repairCost = configurationSection.getInt("repair-cost", DEFAULT_REPAIR_COST),
                isEnchantmentsRemovableByGrindstone = isEnchantmentsRemovableByGrindstone,
                isAddDefaultAttributes = isAddDefaultAttributes,
                hdbId = configurationSection.getNonNullString("hdb-id"),
                rgb = CustomItem.Rgb(
                    red = configurationSection.getInt("rgb.red"),
                    green = configurationSection.getInt("rgb.green"),
                    blue = configurationSection.getInt("rgb.blue")
                )
            )
        }

        fun fromItemStack(
            itemStack: ItemStack,
            name: String,
            chanceToDropOnDeath: Double,
            weight: Double,
            headDatabaseAdapter: HeadDatabaseAdapter
        ): MythicCustomItem {
            val hasCustomModelData = itemStack.hasCustomModelData()
            val customModelData = if (hasCustomModelData) {
                itemStack.customModelData ?: 0
            } else {
                0
            }
            val attributeModifiersFromItems = itemStack.getAttributeModifiers().asMap() ?: emptyMap()
            val attributes =
                attributeModifiersFromItems.flatMap { entry ->
                    entry.value.map {
                        MythicAttribute(
                            attribute = entry.key,
                            minimumAmount = it.amount,
                            maximumAmount = it.amount,
                            name = it.name,
                            operation = it.operation,
                            equipmentSlot = it.slot
                        )
                    }
                }.toSet()
            val itemFlags = itemStack.itemMeta?.itemFlags ?: emptySet()
            val hdbId = headDatabaseAdapter.getIdFromItem(itemStack)
            return MythicCustomItem(
                name,
                (itemStack.displayName ?: "").unChatColorize(),
                chanceToDropOnDeath,
                itemStack.enchantments.mapNotNull { MythicEnchantment(it.key, it.value) }.toSet(),
                itemStack.lore.map(String::unChatColorize),
                itemStack.type,
                isBroadcastOnFind = false,
                hasDurability = true,
                durability = itemStack.getFromItemMetaAsDamageable { damage } ?: 0,
                hasCustomModelData = hasCustomModelData,
                customModelData = customModelData,
                isUnbreakable = itemStack.isUnbreakable,
                weight = weight,
                attributes = attributes,
                itemFlags = itemFlags,
                repairCost = itemStack.getFromItemMetaAsRepairable { repairCost } ?: DEFAULT_REPAIR_COST,
                hdbId = hdbId ?: ""
            )
        }
    }

    override fun toItemStack(customEnchantmentRegistry: CustomEnchantmentRegistry): ItemStack {
        return mythicDrops.productionLine.customItemFactory.toItemStack(this)
    }
}
