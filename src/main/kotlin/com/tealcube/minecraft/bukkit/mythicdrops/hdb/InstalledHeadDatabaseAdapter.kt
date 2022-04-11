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
import io.pixeloutlaw.kindling.Log
import me.arcaniax.hdb.api.DatabaseLoadEvent
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

/**
 * Implementation used when HeadDatabase is installed.
 */
internal object InstalledHeadDatabaseAdapter : HeadDatabaseAdapter, Listener {
    private lateinit var hdbApi: HeadDatabaseAPI

    override fun getItemFromId(id: String): ItemStack = if (!this::hdbApi.isInitialized) {
        Log.warn("Attempting to use HDB before the database has loaded")
        NotInstalledHeadDatabaseAdapter.getItemFromId(id)
    } else if (hdbApi.isHead(id)) {
        // delegate to not installed adapter if one can't be found just to be safe
        hdbApi.getItemHead(id) ?: NotInstalledHeadDatabaseAdapter.getItemFromId(id)
    } else {
        Log.warn("Unable to find head in HDB by id: '$id'")
        // delegate to not installed adapter if one can't be found
        NotInstalledHeadDatabaseAdapter.getItemFromId(id)
    }

    override fun getIdFromItem(itemStack: ItemStack): String? = if (!this::hdbApi.isInitialized) {
        Log.warn("Attempting to use HDB before the database has loaded")
        null
    } else {
        hdbApi.getItemID(itemStack)
    }

    override fun register(plugin: MythicDropsPlugin) {
        Log.info("Enabling HeadDatabase support")
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onDatabaseLoadEvent(databaseLoadEvent: DatabaseLoadEvent) {
        hdbApi = HeadDatabaseAPI()
    }
}
