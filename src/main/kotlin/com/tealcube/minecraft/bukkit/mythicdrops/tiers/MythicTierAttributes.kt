/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.tiers

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierAttributes
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicTierAttributes(
    override val baseAttributes: Set<MythicAttribute> = emptySet(),
    override val bonusAttributes: Set<MythicAttribute> = emptySet(),
    override val minimumBonusAttributes: Int = 0,
    override val maximumBonusAttributes: Int = 0
) : TierAttributes {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicTierAttributes {
            val baseAttributes = configurationSection.getOrCreateSection("base-attributes").let {
                it.getKeys(false).mapNotNull { attrKey ->
                    val attrCS = it.getOrCreateSection(attrKey)
                    MythicAttribute.fromConfigurationSection(attrCS, attrKey)
                }.toSet()
            }
            val bonusAttributes = configurationSection.getOrCreateSection("bonus-attributes").let {
                it.getKeys(false).mapNotNull { attrKey ->
                    val attrCS = it.getOrCreateSection(attrKey)
                    MythicAttribute.fromConfigurationSection(attrCS, attrKey)
                }.toSet()
            }
            return MythicTierAttributes(
                baseAttributes = baseAttributes,
                bonusAttributes = bonusAttributes,
                minimumBonusAttributes = configurationSection.getInt("minimum-bonus-attributes"),
                maximumBonusAttributes = configurationSection.getInt("maximum-bonus-attributes")
            )
        }
    }
}
