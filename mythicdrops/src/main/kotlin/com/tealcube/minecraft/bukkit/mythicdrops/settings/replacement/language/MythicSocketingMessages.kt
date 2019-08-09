package com.tealcube.minecraft.bukkit.mythicdrops.settings.replacement.language

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.replacement.language.SocketingMessages
import org.bukkit.configuration.ConfigurationSection

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