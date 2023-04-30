package com.tealcube.minecraft.bukkit.mythicdrops.items.factories

import com.tealcube.minecraft.bukkit.mythicdrops.DEFAULT_REPAIR_COST
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.CustomItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.IdentificationItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.SocketGemItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TieredItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.TokenItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.CustomItemItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.IdentityTomeItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.Item
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.SocketExtenderItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.SocketGemItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.TieredItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.UnidentifiedItemItem
import com.tealcube.minecraft.bukkit.mythicdrops.setRepairCost
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.lore
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsToken
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataString
import org.bukkit.inventory.ItemStack

internal class MythicTokenItemFactory(
    private val customItemFactory: CustomItemFactory,
    private val identificationItemFactory: IdentificationItemFactory,
    private val socketGemItemFactory: SocketGemItemFactory,
    private val tieredItemFactory: TieredItemFactory,
    private val tierManager: TierManager
) : TokenItemFactory {
    override fun buildToken(token: Token): ItemStack = ItemStack(token.material).apply {
        displayName = token.displayNameChatColorized
        lore = token.loreChatColorized
        setRepairCost(DEFAULT_REPAIR_COST)
        setPersistentDataString(mythicDropsToken, token.name)
    }

    override fun buildTokenItem(tokenItem: Item): ItemStack? = when (tokenItem) {
        is CustomItemItem -> customItemFactory.toItemStack(tokenItem.customItem)
        is IdentityTomeItem -> identificationItemFactory.buildIdentityTome()
        is SocketExtenderItem -> socketGemItemFactory.buildSocketExtender(tokenItem.socketExtenderType)
        is SocketGemItem -> socketGemItemFactory.toItemStack(tokenItem.socketGem)
        is TieredItem -> tieredItemFactory.toItemStack(tokenItem.tier)
        is UnidentifiedItemItem -> tierManager.randomByIdentityWeight()
            ?.let { identificationItemFactory.buildUnidentifiedItem(it) }
    }
}
