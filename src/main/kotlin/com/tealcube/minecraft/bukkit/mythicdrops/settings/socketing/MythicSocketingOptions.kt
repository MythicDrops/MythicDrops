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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.socketing.SocketingOptions
import com.tealcube.minecraft.bukkit.mythicdrops.getEnum
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.unChatColorize
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.inventory.ClickType

internal data class MythicSocketingOptions(
    override val isPreventCraftingWithGems: Boolean = false,
    override val isPreventMultipleNameChangesFromSockets: Boolean = false,
    override val isUseAttackerItemInHand: Boolean = false,
    override val isUseAttackerArmorEquipped: Boolean = false,
    override val isUseDefenderItemInHand: Boolean = false,
    override val isUseDefenderArmorEquipped: Boolean = false,
    override val socketGemMaterialIds: Set<Material> = emptySet(),
    override val defaultSocketNameColorOnItems: String = ChatColor.GOLD.toString().unChatColorize(),
    override val useTierColorForSocketName: Boolean = false,
    override val auraRefreshInSeconds: Int = DEFAULT_AURA_GEM_REFRESH,
    override val socketExtenderMaterialIds: Set<Material> = emptySet(),
    override val isRequireExtenderSlotsToAddSockets: Boolean = false,
    override val clickTypeToSocket: ClickType = ClickType.RIGHT,
    override val maximumNumberOfSocketsViaExtender: Int = -1
) : SocketingOptions {
    companion object {
        private const val DEFAULT_AURA_GEM_REFRESH = 30

        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketingOptions(
            isPreventCraftingWithGems = configurationSection.getBoolean("prevent-crafting-with-gems"),
            isPreventMultipleNameChangesFromSockets = configurationSection.getBoolean(
                "prevent-multiple-name-changes-from-sockets"
            ),
            isUseAttackerItemInHand = configurationSection.getBoolean("use-attacker-item-in-hand"),
            isUseAttackerArmorEquipped = configurationSection.getBoolean("use-attacker-armor-equipped"),
            isUseDefenderItemInHand = configurationSection.getBoolean("use-defender-item-in-hand"),
            isUseDefenderArmorEquipped = configurationSection.getBoolean("use-defender-armor-equipped"),
            socketGemMaterialIds = configurationSection.getStringList("socket-gem-material-ids")
                .mapNotNull { Material.getMaterial(it) }.toSet(),
            defaultSocketNameColorOnItems = configurationSection.getNonNullString(
                "default-socket-name-color-on-items",
                ChatColor.GOLD.toString().unChatColorize()
            ),
            useTierColorForSocketName = configurationSection.getBoolean("use-tier-color-for-socket-name"),
            auraRefreshInSeconds = configurationSection.getInt(
                "aura-gem-refresh-in-seconds",
                DEFAULT_AURA_GEM_REFRESH
            ),
            socketExtenderMaterialIds = configurationSection.getStringList("socket-extender-material-ids")
                .mapNotNull { Material.getMaterial(it) }.toSet(),
            isRequireExtenderSlotsToAddSockets = configurationSection.getBoolean(
                "require-extender-slots-to-add-sockets"
            ),
            clickTypeToSocket = configurationSection.getEnum("click-type-to-socket", ClickType.RIGHT),
            maximumNumberOfSocketsViaExtender = configurationSection.getInt(
                "maximum-number-of-sockets-via-extender",
                -1
            )
        )
    }
}
