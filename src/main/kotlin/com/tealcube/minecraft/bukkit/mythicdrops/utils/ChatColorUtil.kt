package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.google.common.base.Preconditions
import org.apache.commons.lang3.RandomUtils
import org.bukkit.ChatColor

object ChatColorUtil {
    /**
     * Returns the [ChatColor] associated with the given [str] with an optional fallback.
     *
     * @param str String to convert to [ChatColor]
     * @param fallback fallback [ChatColor], defaults to null
     */
    @JvmOverloads
    fun getChatColor(str: String?, fallback: ChatColor? = null): ChatColor? {
        if (str == null) {
            return null
        }
        return try {
            ChatColor.valueOf(str)
        } catch (e: Exception) {
            fallback
        }
    }

    /**
     * Returns a random [ChatColor] from a non-empty [Collection].
     *
     * @param chatColors [Collection] of [ChatColor]s
     * @throws IllegalArgumentException if [chatColors] is empty
     */
    fun getRandomChatColor(chatColors: Collection<ChatColor>): ChatColor {
        Preconditions.checkArgument(!chatColors.isEmpty())
        return chatColors.toTypedArray()[RandomUtils.nextInt(0, chatColors.size)]
    }
}