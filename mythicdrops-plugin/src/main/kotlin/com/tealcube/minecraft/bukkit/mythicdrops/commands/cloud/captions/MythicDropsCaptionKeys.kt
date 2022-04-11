package com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.captions

import cloud.commandframework.captions.Caption

object MythicDropsCaptionKeys {
    val ARGUMENT_PARSE_FAILURE_TIER by lazy { of("argument.parse.failure.tier") }
    val ARGUMENT_PARSE_FAILURE_CUSTOM_ITEM by lazy { of("argument.parse.failure.customItem") }
    val ARGUMENT_PARSE_FAILURE_SOCKET_GEM by lazy { of("argument.parse.failure.socketGem") }
    val ARGUMENT_PARSE_FAILURE_ITEM_GROUP by lazy { of("argument.parse.failure.itemGroup") }
    private val recognizedCaptions = mutableListOf<Caption>()

    fun getMythicDropsCaptionKeys(): List<Caption> {
        return recognizedCaptions.toList()
    }

    private fun of(key: String): Caption {
        val caption = Caption.of(key)
        recognizedCaptions.add(caption)
        return caption
    }
}
