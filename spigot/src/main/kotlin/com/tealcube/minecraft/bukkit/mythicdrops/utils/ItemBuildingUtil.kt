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
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.Relation
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.enchantments.MythicMythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicKoinComponent
import com.tealcube.minecraft.bukkit.mythicdrops.koin.inject
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import kotlin.math.max
import kotlin.math.min

internal object ItemBuildingUtil : MythicKoinComponent {
    private val settingsManager: SettingsManager by inject()
    private val spaceRegex = " ".toRegex()

    fun getBaseEnchantments(
        itemStack: ItemStack,
        tier: Tier
    ): Map<Enchantment, Int> {
        if (tier.enchantments.baseEnchantments.isEmpty()) {
            return emptyMap()
        }
        val safeEnchantments =
            getSafeEnchantments(
                tier.enchantments.isSafeBaseEnchantments,
                tier.enchantments.baseEnchantments,
                itemStack
            )
        return safeEnchantments.associate { mythicEnchantment ->
            val enchantment = mythicEnchantment.enchantment
            val isAllowHighEnchantments = tier.enchantments.isAllowHighBaseEnchantments
            val levelRange = getEnchantmentLevelRange(isAllowHighEnchantments, mythicEnchantment, enchantment)

            when {
                !tier.enchantments.isSafeBaseEnchantments -> enchantment to levelRange.safeRandom()
                tier.enchantments.isAllowHighBaseEnchantments -> {
                    enchantment to levelRange.safeRandom()
                }

                else ->
                    enchantment to
                        getAcceptableEnchantmentLevel(
                            enchantment,
                            levelRange.safeRandom()
                        )
            }
        }
    }

    fun getBonusEnchantments(
        itemStack: ItemStack,
        tier: Tier
    ): Map<Enchantment, Int> {
        if (tier.enchantments.bonusEnchantments.isEmpty()) {
            return emptyMap()
        }
        val bonusEnchantmentsToAdd =
            (tier.enchantments.minimumBonusEnchantments..tier.enchantments.maximumBonusEnchantments).random()
        var tierBonusEnchantments =
            getSafeEnchantments(
                tier.enchantments.isSafeBonusEnchantments,
                tier.enchantments.bonusEnchantments,
                itemStack
            )
        val bonusEnchantments = mutableMapOf<Enchantment, Int>()
        repeat(bonusEnchantmentsToAdd) {
            if (tierBonusEnchantments.isEmpty()) {
                return@repeat
            }
            val mythicEnchantment = tierBonusEnchantments.random()
            if (settingsManager.configSettings.options.isOnlyRollBonusEnchantmentsOnce) {
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
            val trimmedLevel =
                if (!tier.enchantments.isAllowHighBonusEnchantments) {
                    getAcceptableEnchantmentLevel(enchantment, randomizedLevelOfEnchantment)
                } else {
                    randomizedLevelOfEnchantment
                }
            bonusEnchantments[enchantment] = trimmedLevel
        }
        return bonusEnchantments.toMap()
    }

    fun getRelations(
        itemStack: ItemStack,
        relationManager: RelationManager
    ): List<Relation> {
        val name = itemStack.displayName ?: "" // empty string has no relations
        return getRelations(name, relationManager)
    }

    fun getRelations(
        displayName: String,
        relationManager: RelationManager
    ): List<Relation> =
        displayName
            .stripColors()
            .split(spaceRegex)
            .dropLastWhile { it.isEmpty() }
            .mapNotNull { relationManager.getById(it) }

    fun getRelationEnchantments(
        itemStack: ItemStack,
        relations: List<Relation>,
        tier: Tier
    ): Map<Enchantment, Int> {
        val relationMythicEnchantments = relations.flatMap { it.enchantments }
        val safeEnchantments =
            getSafeEnchantments(tier.enchantments.isSafeRelationEnchantments, relationMythicEnchantments, itemStack)
        if (safeEnchantments.isEmpty()) {
            return emptyMap()
        }
        val relationEnchantments = mutableMapOf<Enchantment, Int>()
        safeEnchantments.forEach { mythicEnchantment ->
            val enchantment = mythicEnchantment.enchantment
            val randomizedLevelOfEnchantment =
                relationEnchantments[enchantment]?.let {
                    min(
                        mythicEnchantment.maximumLevel,
                        it + mythicEnchantment.getRandomLevel()
                    )
                } ?: mythicEnchantment.getRandomLevel()
            val trimmedLevel =
                if (!tier.enchantments.isAllowHighRelationEnchantments) {
                    getAcceptableEnchantmentLevel(enchantment, randomizedLevelOfEnchantment)
                } else {
                    randomizedLevelOfEnchantment
                }
            relationEnchantments[enchantment] = trimmedLevel
        }
        return relationEnchantments.toMap()
    }

    fun getRelationEnchantments(
        itemStack: ItemStack,
        tier: Tier,
        relationManager: RelationManager
    ): Map<Enchantment, Int> {
        val relations = getRelations(itemStack, relationManager)
        return getRelationEnchantments(itemStack, relations, tier)
    }

    @Suppress("UnstableApiUsage")
    fun getBaseAttributeModifiers(tier: Tier): Multimap<Attribute, AttributeModifier> {
        val baseAttributeModifiers: Multimap<Attribute, AttributeModifier> =
            MultimapBuilder.hashKeys().arrayListValues().build()
        if (tier.attributes.baseAttributes.isEmpty()) {
            return baseAttributeModifiers
        }
        tier.attributes.baseAttributes.forEach {
            val (attribute, attributeModifier) = it.toAttributeModifier()
            baseAttributeModifiers.put(attribute, attributeModifier)
        }
        return baseAttributeModifiers
    }

    @Suppress("UnstableApiUsage")
    fun getBonusAttributeModifiers(tier: Tier): Multimap<Attribute, AttributeModifier> {
        val bonusAttributeModifiers: Multimap<Attribute, AttributeModifier> =
            MultimapBuilder.hashKeys().arrayListValues().build()
        if (tier.attributes.bonusAttributes.isEmpty()) {
            return bonusAttributeModifiers
        }
        val bonusAttributes = tier.attributes.bonusAttributes.toMutableSet()
        val bonusAttributesToAdd =
            (tier.attributes.minimumBonusAttributes..tier.attributes.maximumBonusAttributes).random()
        repeat(bonusAttributesToAdd) {
            if (bonusAttributes.isEmpty()) {
                return@repeat
            }
            val mythicAttribute = bonusAttributes.random()
            if (settingsManager.configSettings.options.isOnlyRollBonusAttributesOnce) {
                bonusAttributes -= mythicAttribute
            }
            val (attribute, attributeModifier) = mythicAttribute.toAttributeModifier()
            bonusAttributeModifiers.put(attribute, attributeModifier)
        }
        return bonusAttributeModifiers
    }

    private fun getSafeEnchantments(
        isSafe: Boolean,
        enchantments: Collection<MythicEnchantment>,
        itemStack: ItemStack
    ): Collection<MythicEnchantment> =
        enchantments.filter {
            if (isSafe) {
                it.enchantment.canEnchantItem(itemStack)
            } else {
                true
            }
        }

    private fun getEnchantmentLevelRange(
        isAllowHighEnchantments: Boolean,
        mythicEnchantment: MythicEnchantment,
        enchantment: Enchantment
    ): IntRange {
        val minimumLevel =
            if (isAllowHighEnchantments) {
                max(mythicEnchantment.minimumLevel.coerceAtLeast(1), enchantment.startLevel)
            } else {
                mythicEnchantment.minimumLevel.coerceAtLeast(1).coerceAtMost(enchantment.startLevel)
            }
        val maximumLevel =
            if (isAllowHighEnchantments) {
                mythicEnchantment.maximumLevel
                    .coerceAtLeast(1)
                    .coerceAtMost(MythicMythicEnchantment.HIGHEST_ENCHANTMENT_LEVEL)
            } else {
                mythicEnchantment.maximumLevel.coerceAtLeast(1).coerceAtMost(enchantment.maxLevel)
            }
        // we need to do this calculation below because sometimes minimumLevel and maximumLevel can
        // not match up :^)
        return min(minimumLevel, maximumLevel)..max(minimumLevel, maximumLevel)
    }

    private fun getAcceptableEnchantmentLevel(
        enchantment: Enchantment,
        level: Int
    ): Int = level.coerceAtLeast(enchantment.startLevel).coerceAtMost(enchantment.maxLevel)
}
