package com.tealcube.minecraft.bukkit.mythicdrops.api.tokens

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.CUSTOM_ITEM
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.IDENTITY_TOME
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.SOCKET_EXTENDER
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.SOCKET_GEM
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.TIER
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.UNIDENTIFIED_ITEM
import com.tealcube.minecraft.bukkit.mythicdrops.api.weight.Weighted
import org.bukkit.Material

/**
 * Represents an item that can be consumed to grant other MythicDrops items.
 */
interface Token : Weighted {
    val name: String
    val material: Material
    val displayName: String
    val displayNameChatColorized: String
    val displayNameChatStripped: String
    val lore: List<String>
    val loreChatColorized: List<String>
    val loreStripped: List<String>
    val items: List<Item>

    /**
     * The types that token items can be.
     */
    enum class ItemType {
        TIER,
        CUSTOM_ITEM,
        SOCKET_GEM,
        SOCKET_EXTENDER,
        UNIDENTIFIED_ITEM,
        IDENTITY_TOME
    }

    /**
     * Items that this token can drop.
     */
    sealed interface Item : Weighted {
        val type: ItemType
    }

    /**
     * Contains data about a tiered item that can drop.
     */
    data class TieredItem(override val weight: Double, val tier: Tier) : Item {
        override val type: ItemType = TIER
    }

    /**
     * Contains data about a custom item that can drop.
     */
    data class CustomItemItem(
        override val weight: Double,
        val customItem: CustomItem
    ) : Item {
        override val type: ItemType = CUSTOM_ITEM
    }

    /**
     * Contains data about a socket gem that can drop.
     */
    data class SocketGemItem(
        override val weight: Double,
        val socketGem: SocketGem
    ) : Item {
        override val type: ItemType = SOCKET_GEM
    }

    /**
     * Contains data about a socket extender that can drop.
     */
    data class SocketExtenderItem(
        override val weight: Double,
        val socketExtenderType: SocketExtenderType
    ) : Item {
        override val type: ItemType = SOCKET_EXTENDER
    }

    /**
     * Contains data about an unidentified item that can drop.
     */
    data class UnidentifiedItemItem(
        override val weight: Double
    ) : Item {
        override val type: ItemType = UNIDENTIFIED_ITEM
    }

    /**
     * Contains data about an unidentified item that can drop.
     */
    data class IdentityTomeItem(
        override val weight: Double
    ) : Item {
        override val type: ItemType = IDENTITY_TOME
    }
}
