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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.socketing

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.socketing.SocketingOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getChatColor
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicSocketingOptions internal constructor(
    override val isPreventCraftingWithGems: Boolean = false,
    override val isPreventMultipleNameChangesFromSockets: Boolean = false,
    override val isUseAttackerItemInHand: Boolean = false,
    override val isUseAttackerArmorEquipped: Boolean = false,
    override val isUseDefenderItemInHand: Boolean = false,
    override val isUseDefenderArmorEquipped: Boolean = false,
    override val socketGemMaterialIds: Set<Material> = emptySet(),
    override val defaultSocketNameColorOnItems: ChatColor = ChatColor.GOLD,
    override val useTierColorForSocketName: Boolean = false,
    override val auraRefreshInSeconds: Int = DEFAULT_AURA_GEM_REFRESH
) : SocketingOptions {
    companion object {
        private const val DEFAULT_AURA_GEM_REFRESH = 30

        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketingOptions(
            configurationSection.getBoolean("prevent-crafting-with-gems"),
            configurationSection.getBoolean("prevent-multiple-name-changes-from-sockets"),
            configurationSection.getBoolean("use-attacker-item-in-hand"),
            configurationSection.getBoolean("use-attacker-armor-equipped"),
            configurationSection.getBoolean("use-defender-item-in-hand"),
            configurationSection.getBoolean("use-defender-armor-equipped"),
            configurationSection.getStringList("socket-gem-material-ids").mapNotNull { Material.getMaterial(it) }.toSet(),
            configurationSection.getChatColor("default-socket-name-color-on-items", ChatColor.GOLD),
            configurationSection.getBoolean("use-tier-color-for-socket-name"),
            configurationSection.getInt("aura-gem-refresh-in-seconds", DEFAULT_AURA_GEM_REFRESH)
        )
    }
}
