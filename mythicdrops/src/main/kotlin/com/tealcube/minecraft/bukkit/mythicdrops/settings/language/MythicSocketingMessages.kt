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
package com.tealcube.minecraft.bukkit.mythicdrops.settings.language

import com.squareup.moshi.JsonClass
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.language.SocketingMessages
import org.bukkit.configuration.ConfigurationSection

@JsonClass(generateAdapter = true)
data class MythicSocketingMessages internal constructor(
    override val success: String = "&6[MythicDrops] &AYou successfully socketted your item!",
    override val notInItemGroup: String = "&6[MythicDrops] &CYou cannot use that gem on that type of item!",
    override val noOpenSockets: String = "&6[MythicDrops] &CThat item does not have any open sockets!",
    override val preventedCrafting: String = "&6[MythicDrops] &CYou cannot craft items with &6Socket Gems&C!",
    override val combinerMustBeGem: String = "&6[MythicDrops] &CYou can only put &6Socket Gems &Cinside the combiner!",
    override val combinerClaimOutput: String = "&6[MythicDrops] &CPlease claim your combined gem before adding more gems!"
) : SocketingMessages {
    companion object {
        fun fromConfigurationSection(configurationSection: ConfigurationSection) = MythicSocketingMessages(
            configurationSection.getString("success") ?: "&6[MythicDrops] &AYou successfully socketted your item!",
            configurationSection.getString("not-in-item-group")
                ?: "&6[MythicDrops] &CYou cannot use that gem on that type of item!",
            configurationSection.getString("no-open-sockets")
                ?: "&6[MythicDrops] &CThat item does not have any open sockets!",
            configurationSection.getString("prevented-crafting")
                ?: "&6[MythicDrops] &CYou cannot craft items with &6Socket Gems&C!",
            configurationSection.getString("combiner-must-be-gem")
                ?: "&6[MythicDrops] &CYou can only put &6Socket Gems &Cinside the combiner!",
            configurationSection.getString("combiner-claim-output")
                ?: "&6[MythicDrops] &CPlease claim your combined gem before adding more gems!"
        )
    }
}
