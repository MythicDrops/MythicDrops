package com.tealcube.minecraft.bukkit.mythicdrops.api.events

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.TieredItemGenerationData
import org.bukkit.event.HandlerList

class PreTieredItemGenerationEvent(var tieredItemGenerationData: TieredItemGenerationData) :
    MythicDropsCancellableEvent() {
        companion object {
            @JvmStatic
            val handlerList = HandlerList()
        }

        override fun getHandlers(): HandlerList = handlerList
    }
