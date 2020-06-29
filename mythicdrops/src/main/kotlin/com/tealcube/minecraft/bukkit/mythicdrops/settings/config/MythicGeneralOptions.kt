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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.config

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.BlankMobSpawn
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.config.GeneralOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getChatColor
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicGeneralOptions internal constructor(
    override val isGiveMobsNames: Boolean = false,
    override val isGiveMobsColoredNames: Boolean = false,
    override val isGiveAllMobsNames: Boolean = false,
    override val isDisplayMobEquipment: Boolean = false,
    override val isCanMobsPickUpEquipment: Boolean = false,
    override val blankMobSpawn: BlankMobSpawn = MythicBlankMobSpawn(),
    override val isAllowItemsToBeRepairedByAnvil: Boolean = false,
    override val isRandomizeLeatherColors: Boolean = false,
    override val defaultSocketGemColorOnItems: ChatColor = ChatColor.GOLD,
    override val isUseTierColorForSocketName: Boolean = false,
    override val isRequirePlayerKillForDrops: Boolean = false,
    override val isOnlyRollBonusEnchantmentsOnce: Boolean = false,
    override val isOnlyRollBonusAttributesOnce: Boolean = false,
    override val isAllowItemsToHaveRepairCostRemovedByGrindstone: Boolean = false
) : GeneralOptions {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicGeneralOptions =
            MythicGeneralOptions(
                configurationSection.getBoolean("give-mobs-names"),
                configurationSection.getBoolean("give-mobs-colored-names"),
                configurationSection.getBoolean("give-all-mobs-names"),
                configurationSection.getBoolean("display-mob-equipment"),
                configurationSection.getBoolean("can-mobs-pick-up-equipment"),
                MythicBlankMobSpawn.fromConfigurationSection(configurationSection.getOrCreateSection("blank-mob-spawn")),
                configurationSection.getBoolean("allow-items-to-be-repaired-by-anvil"),
                configurationSection.getBoolean("randomize-leather-colors"),
                configurationSection.getChatColor("default-socket-name-color-on-items", ChatColor.GOLD),
                configurationSection.getBoolean("use-tier-color-for-socket-name"),
                configurationSection.getBoolean("require-player-kill-for-drops"),
                configurationSection.getBoolean("only-roll-bonus-enchantments-once"),
                configurationSection.getBoolean("only-roll-bonus-attributes-once"),
                configurationSection.getBoolean("allow-items-to-have-repair-cost-removed-by-grindstone")
            )
    }
}
