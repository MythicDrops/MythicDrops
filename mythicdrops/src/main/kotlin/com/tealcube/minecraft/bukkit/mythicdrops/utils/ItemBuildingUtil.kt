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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import kotlin.math.max
import kotlin.math.min
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object ItemBuildingUtil {
    private val logger = JulLoggerFactory.getLogger(ItemBuildingUtil::class)
    private val mythicDrops: MythicDrops by lazy {
        MythicDropsPlugin.getInstance()
    }

    fun getSafeEnchantments(
        isSafe: Boolean,
        enchantments: Collection<MythicEnchantment>,
        itemStack: ItemStack
    ): Collection<MythicEnchantment> {
        return enchantments.filter {
            if (isSafe) {
                it.enchantment.canEnchantItem(itemStack)
            } else {
                true
            }
        }
    }

    fun getBaseEnchantments(itemStack: ItemStack, tier: Tier): Map<Enchantment, Int> {
        if (tier.baseEnchantments.isEmpty()) {
            return emptyMap()
        }
        val safeEnchantments = getSafeEnchantments(tier.isSafeBaseEnchantments, tier.baseEnchantments, itemStack)
        return safeEnchantments.map { mythicEnchantment ->
            val enchantment = mythicEnchantment.enchantment
            val isAllowHighEnchantments = tier.isAllowHighBaseEnchantments
            val levelRange = getEnchantmentLevelRange(isAllowHighEnchantments, mythicEnchantment, enchantment)

            when {
                !tier.isSafeBaseEnchantments -> enchantment to levelRange.safeRandom()
                tier.isAllowHighBaseEnchantments -> {
                    enchantment to levelRange.safeRandom()
                }
                else -> enchantment to getAcceptableEnchantmentLevel(
                    enchantment,
                    levelRange.safeRandom()
                )
            }
        }.toMap()
    }

    fun getBonusEnchantments(itemStack: ItemStack, tier: Tier): Map<Enchantment, Int> {
        if (tier.bonusEnchantments.isEmpty()) {
            return emptyMap()
        }
        val bonusEnchantmentsToAdd = (tier.minimumBonusEnchantments..tier.maximumBonusEnchantments).random()
        var tierBonusEnchantments =
            getSafeEnchantments(tier.isSafeBonusEnchantments, tier.bonusEnchantments, itemStack)
        val bonusEnchantments = mutableMapOf<Enchantment, Int>()
        repeat(bonusEnchantmentsToAdd) {
            if (tierBonusEnchantments.isEmpty()) {
                return@repeat
            }
            val mythicEnchantment = tierBonusEnchantments.random()
            if (mythicDrops.settingsManager.configSettings.options.isOnlyRollBonusEnchantmentsOnce) {
                tierBonusEnchantments -= mythicEnchantment
            }
            val enchantment = mythicEnchantment.enchantment
            val randomizedLevelOfEnchantment =
                bonusEnchantments[enchantment]?.let {
                    min(
                        mythicEnchantment.maximumLevel,
                        it + mythicEnchantment.getRandomLevel()
                    )
                }
                    ?: mythicEnchantment.getRandomLevel()
            val trimmedLevel = if (!tier.isAllowHighBonusEnchantments) {
                getAcceptableEnchantmentLevel(enchantment, randomizedLevelOfEnchantment)
            } else {
                randomizedLevelOfEnchantment
            }
            bonusEnchantments[enchantment] = trimmedLevel
        }
        return bonusEnchantments.toMap()
    }

    @Suppress("UnstableApiUsage")
    fun getBaseAttributeModifiers(tier: Tier): Multimap<Attribute, AttributeModifier> {
        val baseAttributeModifiers: Multimap<Attribute, AttributeModifier> =
            MultimapBuilder.hashKeys().arrayListValues().build()
        if (tier.baseAttributes.isEmpty()) {
            return baseAttributeModifiers
        }
        tier.baseAttributes.forEach {
            val (attribute, attributeModifier) = it.toAttributeModifier()
            baseAttributeModifiers.put(attribute, attributeModifier)
        }
        return baseAttributeModifiers
    }

    @Suppress("UnstableApiUsage")
    fun getBonusAttributeModifiers(tier: Tier): Multimap<Attribute, AttributeModifier> {
        val bonusAttributeModifiers: Multimap<Attribute, AttributeModifier> =
            MultimapBuilder.hashKeys().arrayListValues().build()
        if (tier.bonusAttributes.isEmpty()) {
            return bonusAttributeModifiers
        }
        val bonusAttributes = tier.bonusAttributes.toMutableSet()
        val bonusAttributesToAdd = (tier.minimumBonusAttributes..tier.maximumBonusAttributes).random()
        repeat(bonusAttributesToAdd) {
            if (bonusAttributes.isEmpty()) {
                return@repeat
            }
            val mythicAttribute = bonusAttributes.random()
            if (mythicDrops.settingsManager.configSettings.options.isOnlyRollBonusAttributesOnce) {
                bonusAttributes -= mythicAttribute
            }
            val (attribute, attributeModifier) = mythicAttribute.toAttributeModifier()
            bonusAttributeModifiers.put(attribute, attributeModifier)
        }
        return bonusAttributeModifiers
    }

    private fun getEnchantmentLevelRange(
        isAllowHighEnchantments: Boolean,
        mythicEnchantment: MythicEnchantment,
        enchantment: Enchantment
    ): IntRange {
        val minimumLevel = if (isAllowHighEnchantments) {
            max(mythicEnchantment.minimumLevel.coerceAtLeast(1), enchantment.startLevel)
        } else {
            mythicEnchantment.minimumLevel.coerceAtLeast(1).coerceAtMost(enchantment.startLevel)
        }
        val maximumLevel = if (isAllowHighEnchantments) {
            mythicEnchantment.maximumLevel.coerceAtLeast(1)
                .coerceAtMost(MythicEnchantment.HIGHEST_ENCHANTMENT_LEVEL)
        } else {
            mythicEnchantment.maximumLevel.coerceAtLeast(1).coerceAtMost(enchantment.maxLevel)
        }
        // we need to do this calculation below because sometimes minimumLevel and maximumLevel can
        // not match up :^)
        return min(minimumLevel, maximumLevel)..max(minimumLevel, maximumLevel)
    }

    private fun getAcceptableEnchantmentLevel(enchantment: Enchantment, level: Int): Int =
        level.coerceAtLeast(enchantment.startLevel).coerceAtMost(enchantment.maxLevel)
}
