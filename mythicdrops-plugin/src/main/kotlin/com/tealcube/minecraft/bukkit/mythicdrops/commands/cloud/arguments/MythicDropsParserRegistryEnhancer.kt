package com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.arguments

import cloud.commandframework.arguments.parser.ParserRegistry
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import io.leangen.geantyref.TypeToken

object MythicDropsParserRegistryEnhancer {
    fun <C : Any> registerArguments(parserRegistry: ParserRegistry<C>) {
        parserRegistry.registerParserSupplier(TypeToken.get(CustomItem::class.java)) {
            CustomItemArgument.CustomItemParser()
        }
        parserRegistry.registerParserSupplier(TypeToken.get(ItemGroup::class.java)) {
            ItemGroupArgument.ItemGroupParser()
        }
        parserRegistry.registerParserSupplier(TypeToken.get(SocketGem::class.java)) {
            SocketGemArgument.SocketGemParser()
        }
        parserRegistry.registerParserSupplier(TypeToken.get(Tier::class.java)) {
            TierArgument.TierParser()
        }
    }
}
