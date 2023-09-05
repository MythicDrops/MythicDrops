/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.hdb

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getThenSetItemMetaAs
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.UUID

/**
 * Implementation used when there is no HeadDatabase plugin installed.
 */
internal object NotInstalledHeadDatabaseAdapter : HeadDatabaseAdapter {
    private const val TOPPLETHENUN_UUID_STRING = "a8289ae1-dbfb-4807-ac21-b458796ea73c"
    private val toppleTheNunUuid = UUID.fromString(TOPPLETHENUN_UUID_STRING)

    override fun getItemFromId(id: String): ItemStack {
        return ItemStack(Material.PLAYER_HEAD).apply {
            getThenSetItemMetaAs<SkullMeta> { owningPlayer = Bukkit.getOfflinePlayer(toppleTheNunUuid) }
        }
    }

    override fun getIdFromItem(itemStack: ItemStack): String? = null

    override fun register(plugin: MythicDropsPlugin) {
        // do nothing, we're a no-op
    }
}
