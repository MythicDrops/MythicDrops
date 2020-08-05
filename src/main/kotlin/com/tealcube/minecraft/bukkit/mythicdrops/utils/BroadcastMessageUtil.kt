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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.LanguageSettings
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import io.pixeloutlaw.minecraft.spigot.hilt.getDisplayName
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.apache.commons.text.WordUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object BroadcastMessageUtil {
    private val craftItemStackClazz: Class<*>? = ReflectionUtil.getCbClass("inventory.CraftItemStack")
    private val craftItemStackAsNmsCopyMethod: Method? = craftItemStackClazz?.let {
        ReflectionUtil.getMethod(it, "asNMSCopy", ItemStack::class.java)
    }
    private val nmsItemStackClazz: Class<*>? = ReflectionUtil.getNmsClass("ItemStack")
    private val nmsNbtTagCompoundClazz: Class<*>? = ReflectionUtil.getNmsClass("NBTTagCompound")

    // this one's a bit nasty since we want to ensure that we have both classes as not null
    // before we try and fetch the method
    private val saveNmsItemStackMethod: Method? = nmsItemStackClazz?.let { itemStackClazz ->
        nmsNbtTagCompoundClazz?.let { nbtTagCompountClazz ->
            ReflectionUtil.getMethod(itemStackClazz, "save", nbtTagCompountClazz)
        }
    }

    /**
     * Broadcasts that an item was found to all players in the player's world.
     */
    fun broadcastItem(languageSettings: LanguageSettings, player: Player?, itemStack: ItemStack) {
        val displayName = player?.displayName ?: languageSettings.command.unknownPlayer
        val locale = languageSettings.general.foundItemBroadcast.replaceArgs("%receiver%" to displayName).chatColorize()
        val messages = locale.split("%item%")
        val broadcastComponent = TextComponent("")
        val itemStackName = itemStack.getDisplayName() ?: WordUtils.capitalizeFully(
            itemStack.type.name.split("_").joinToString(" ")
        )
        val itemStackNameComponent = TextComponent()
        TextComponent.fromLegacyText(itemStackName).forEach {
            itemStackNameComponent.addExtra(it)
        }
        val itemStackAsJson = convertItemStackToJson(itemStack)
        if (itemStackAsJson != null) {
            itemStackNameComponent.hoverEvent =
                HoverEvent(HoverEvent.Action.SHOW_ITEM, arrayOf(TextComponent(itemStackAsJson)))
        }
        messages.indices.forEach { idx ->
            val key = messages[idx]
            TextComponent.fromLegacyText(key).forEach {
                broadcastComponent.addExtra(it)
            }
            if (idx < messages.size - 1) {
                broadcastComponent.addExtra(itemStackNameComponent)
            }
        }
        player?.world?.players?.forEach { p ->
            p.spigot().sendMessage(broadcastComponent)
        }
    }

    private fun convertItemStackToJson(itemStack: ItemStack): String? {
        if (craftItemStackAsNmsCopyMethod == null || nmsNbtTagCompoundClazz == null || saveNmsItemStackMethod == null) {
            return null
        }
        val itemAsJsonObject = try {
            val nmsNbtTagCompoundObj = nmsNbtTagCompoundClazz.newInstance() // nbt tag
            val nmsItemStackObj = craftItemStackAsNmsCopyMethod.invoke(null, itemStack) // CraftItemStack
            saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj)
        } catch (iae: IllegalAccessException) {
            null
        } catch (ie: InstantiationException) {
            null
        } catch (ite: InvocationTargetException) {
            null
        }
        return itemAsJsonObject?.toString()
    }
}
