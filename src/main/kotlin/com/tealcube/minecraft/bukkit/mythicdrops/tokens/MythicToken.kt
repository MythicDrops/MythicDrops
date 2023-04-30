package com.tealcube.minecraft.bukkit.mythicdrops.tokens

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.CustomItemItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.IdentityTomeItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.Item
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.CUSTOM_ITEM
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.IDENTITY_TOME
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.SOCKET_EXTENDER
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.SOCKET_GEM
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.TIER
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.ItemType.UNIDENTIFIED_ITEM
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.SocketExtenderItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.SocketGemItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.TieredItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token.UnidentifiedItemItem
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getMaterial
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import dev.mythicdrops.Either
import dev.mythicdrops.Either.Companion.left
import dev.mythicdrops.Either.Companion.right
import dev.mythicdrops.Either.Left
import dev.mythicdrops.Either.Right
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getOrDefaultAsDefaultValueType
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

internal data class MythicToken(
    override val name: String,
    override val material: Material = Material.COAL,
    override val displayName: String = "",
    override val displayNameChatColorized: String = displayName.chatColorize(),
    override val displayNameChatStripped: String = displayNameChatColorized.stripColors(),
    override val lore: List<String> = emptyList(),
    override val loreChatColorized: List<String> = lore.chatColorize(),
    override val loreStripped: List<String> = loreChatColorized.stripColors(),
    override val items: List<Item> = emptyList(),
    override val weight: Double = 0.0
) : Token {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String,
            mythicDrops: MythicDrops
        ): Token? {
            val weight = configurationSection.getDouble("weight")
            val displayName = configurationSection.getNonNullString("display-name")
            val material = configurationSection.getMaterial("material", Material.COAL)
            val lore = configurationSection.getStringList("lore")
            val itemResults =
                configurationSection.getList("items")?.filterIsInstance<Map<String, Any>>()
                    ?.map { itemFromMap(it, mythicDrops) }
                    ?: emptyList()
            val items = itemResults.filterIsInstance<Right<Item>>().map { it.right }

            // report bad items
            itemResults.filterIsInstance<Left<String>>().forEach {
                mythicDrops.loadingErrorManager.add("Skipped loading an item for token $key because: ${it.left}")
            }

            if (items.isEmpty()) {
                mythicDrops.loadingErrorManager.add("Token $key is invalid due to having no valid items")
                return null
            }

            return MythicToken(
                name = key,
                material = material,
                displayName = displayName,
                lore = lore,
                items = items,
                weight = weight
            )
        }

        private fun itemFromMap(map: Map<String, Any>, mythicDrops: MythicDrops): Either<String, Item> {
            if (!map.containsKey("type")) {
                return left("type was not present")
            }
            val type = enumValueOrNull<ItemType>(map.getOrDefaultAsDefaultValueType("type", ""))
                ?: return left("type was present and invalid: ${map["type"]}")
            val weight = map.getOrDefaultAsDefaultValueType("weight", 0.0 as Number).toDouble()
            val name = map.getOrDefaultAsDefaultValueType("name", "")
            return when (type) {
                TIER -> {
                    val tier = mythicDrops.tierManager.getById(name)
                    if (tier != null) {
                        right(TieredItem(tier = tier, weight = weight))
                    } else {
                        left("no tier available by ID: $name")
                    }
                }

                CUSTOM_ITEM -> {
                    val customItem = mythicDrops.customItemManager.getById(name)
                    if (customItem != null) {
                        right(CustomItemItem(customItem = customItem, weight = weight))
                    } else {
                        left("no custom item available by ID: $name")
                    }
                }

                SOCKET_GEM -> {
                    val socketGem = mythicDrops.socketGemManager.getById(name)
                    if (socketGem != null) {
                        right(SocketGemItem(socketGem = socketGem, weight = weight))
                    } else {
                        left("no socket gem available by ID: $name")
                    }
                }

                SOCKET_EXTENDER -> {
                    val socketExtenderType = mythicDrops.socketExtenderTypeManager.getById(name)
                    if (socketExtenderType != null) {
                        right(SocketExtenderItem(socketExtenderType = socketExtenderType, weight = weight))
                    } else {
                        left("no socket extender available by ID: $name")
                    }
                }

                UNIDENTIFIED_ITEM -> {
                    right(UnidentifiedItemItem(weight = weight))
                }

                IDENTITY_TOME -> {
                    right(IdentityTomeItem(weight = weight))
                }
            }
        }
    }
}
