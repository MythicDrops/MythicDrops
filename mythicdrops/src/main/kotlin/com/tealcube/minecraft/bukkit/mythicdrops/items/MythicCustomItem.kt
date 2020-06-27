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

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.addAttributeModifier
import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.CustomEnchantmentRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.getAttributeModifiers
import com.tealcube.minecraft.bukkit.mythicdrops.getFromItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.getMaterial
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setRepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.unChatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EnchantmentUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TemplatingUtil
import io.pixeloutlaw.minecraft.spigot.hilt.getCustomModelData
import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import io.pixeloutlaw.minecraft.spigot.hilt.getLore
import io.pixeloutlaw.minecraft.spigot.hilt.hasCustomModelData
import io.pixeloutlaw.minecraft.spigot.hilt.isUnbreakable
import io.pixeloutlaw.minecraft.spigot.hilt.setCustomModelData
import io.pixeloutlaw.minecraft.spigot.hilt.setUnbreakable
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

@JsonClass(generateAdapter = true)
data class MythicCustomItem(
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
    override val isGlow: Boolean = false
) : CustomItem {
    companion object {
        private val logger = JulLoggerFactory.getLogger(MythicCustomItem::class)

        @JvmStatic
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
                isGlow = configurationSection.getBoolean("glow")
            )
        }

        @JvmStatic
        fun fromItemStack(
            itemStack: ItemStack,
            name: String,
            chanceToDropOnDeath: Double,
            weight: Double
        ): MythicCustomItem {
            val (hasCustomModelData, customModelData) = try {
                itemStack.hasCustomModelData() to (itemStack.getCustomModelData() ?: 0)
            } catch (ignored: Throwable) {
                // cannot use custom model data on 1.13
                false to 0
            }
            val attributeModifiersFromItems = itemStack.getAttributeModifiers()?.asMap() ?: emptyMap()
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
            return MythicCustomItem(
                name,
                (itemStack.getDisplayName() ?: "").unChatColorize(),
                chanceToDropOnDeath,
                itemStack.enchantments.mapNotNull { MythicEnchantment(it.key, it.value) }.toSet(),
                itemStack.getLore().map(String::unChatColorize),
                itemStack.type,
                isBroadcastOnFind = false,
                hasDurability = true,
                durability = itemStack.getFromItemMetaAsDamageable({ damage }) ?: 0,
                hasCustomModelData = hasCustomModelData,
                customModelData = customModelData,
                isUnbreakable = itemStack.isUnbreakable(),
                weight = weight,
                attributes = attributes
            )
        }
    }

    override fun toItemStack(): ItemStack = toItemStack(MythicDropsPlugin.getInstance().customEnchantmentRegistry)

    override fun toItemStack(customEnchantmentRegistry: CustomEnchantmentRegistry): ItemStack {
        val itemStack = ItemStack(material, 1)
        if (hasDurability) {
            itemStack.getThenSetItemMetaAsDamageable({
                damage = durability
            }, { this.durability = this@MythicCustomItem.durability.toShort() })
        }
        if (hasCustomModelData) {
            try {
                itemStack.setCustomModelData(customModelData)
            } catch (ex: Throwable) {
                logger.severe("Attempting to set custom model data for \"$name\" while on 1.13!")
            }
        }
        if (displayName.isNotBlank()) {
            itemStack.setDisplayNameChatColorized(displayName)
        }
        itemStack.setLoreChatColorized(lore.map(TemplatingUtil::template))
        itemStack.addUnsafeEnchantments(enchantments.map { it.enchantment to it.getRandomLevel() }.toMap())
        val glowEnchantment =
            customEnchantmentRegistry.getCustomEnchantmentByKey(CustomEnchantmentRegistry.GLOW, material)
        if (isGlow && glowEnchantment != null) {
            itemStack.addUnsafeEnchantment(glowEnchantment, 1)
        }
        itemStack.setUnbreakable(isUnbreakable)
        itemStack.setRepairCost() // sets to default repair cost
        attributes.forEach {
            val (attribute, attributeModifier) = it.toAttributeModifier()
            itemStack.addAttributeModifier(attribute, attributeModifier)
        }
        return itemStack
    }
}
