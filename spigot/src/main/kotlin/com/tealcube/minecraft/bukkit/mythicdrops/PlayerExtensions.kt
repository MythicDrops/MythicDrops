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
package com.tealcube.minecraft.bukkit.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.debug.MythicDebugManager
import io.pixeloutlaw.minecraft.spigot.mythicdrops.scheduleSyncDelayedTask
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * Dispatches a command for this player as if they have permission to run it.
 *
 * @param plugin Plugin instance
 * @param cmd Command to run
 */
internal fun Player.sudoDispatchCommand(
    plugin: Plugin,
    cmd: SocketCommand
) {
    val permissionAttachment = addAttachment(plugin)
    cmd.permissions.forEach {
        permissionAttachment.setPermission(it, true)
    }
    Bukkit.dispatchCommand(this, cmd.command)
    permissionAttachment.remove()
}

internal fun Player.isDebug(mythicDebugManager: MythicDebugManager) = mythicDebugManager.isInDebug(uniqueId)

internal fun Player.toggleDebug(mythicDebugManager: MythicDebugManager) {
    if (isDebug(mythicDebugManager)) {
        mythicDebugManager.disableDebug(uniqueId)
    } else {
        mythicDebugManager.enableDebug(uniqueId)
    }
}

internal fun Player.sendDebugMessage(
    mythicDebugManager: MythicDebugManager,
    msg: String,
    args: Collection<Pair<String, String>> = emptyList()
) {
    if (isDebug(mythicDebugManager)) {
        sendMythicMessage(msg, args)
    }
}

internal const val SERVER_TICKS_TIL_UNOWNED: Long = 600

internal fun Player.giveItemOrDrop(
    item: ItemStack,
    ticksLived: Int = 0
): Boolean {
    val world = world
    val location = location
    val drops = inventory.addItem(item)
    drops.values.forEach {
        if (it == null || it.type.isAir) {
            return@forEach
        }
        val itemEntity = world.dropItem(location, item)
        if (ticksLived > 0) {
            itemEntity.ticksLived = ticksLived
        }
        itemEntity.owner = uniqueId
        MythicDropsPlugin.getInstance().scheduleSyncDelayedTask(SERVER_TICKS_TIL_UNOWNED) {
            if (itemEntity.isValid) {
                itemEntity.owner = null
            }
        }
    }
    return drops.isNotEmpty()
}

internal fun Player.giveItemsOrDrop(
    items: List<ItemStack>,
    ticksLived: Int = 0
): Boolean = items.map { giveItemOrDrop(it, ticksLived) }.reduceOrNull { acc, isSuccess -> acc && isSuccess } ?: true
