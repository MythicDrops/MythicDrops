package com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories

import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token
import org.bukkit.inventory.ItemStack

/**
 * Used for creating converting [Token]s into [org.bukkit.inventory.ItemStack]s.
 */
interface TokenItemFactory {
    fun buildToken(token: Token): ItemStack

    fun buildTokenItem(tokenItem: Token.Item): ItemStack?
}
