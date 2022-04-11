package com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.captions

import cloud.commandframework.captions.FactoryDelegatingCaptionRegistry

object MythicDropsCaptionRegistryEnhancer {
    val ARGUMENT_PARSE_FAILURE_TIER = "'{input}' is not a valid tier"
    val ARGUMENT_PARSE_FAILURE_CUSTOM_ITEM = "'{input}' is not a valid custom item"
    val ARGUMENT_PARSE_FAILURE_SOCKET_GEM = "'{input}' is not a valid socket gem"
    val ARGUMENT_PARSE_FAILURE_ITEM_GROUP = "'{input}' is not a valid item group"

    fun <C : Any> enhanceCaptions(captionRegistry: FactoryDelegatingCaptionRegistry<C>) {
        captionRegistry.registerMessageFactory(MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_TIER) { _, _ ->
            ARGUMENT_PARSE_FAILURE_TIER
        }
        captionRegistry.registerMessageFactory(MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_CUSTOM_ITEM) { _, _ ->
            ARGUMENT_PARSE_FAILURE_CUSTOM_ITEM
        }
        captionRegistry.registerMessageFactory(MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_SOCKET_GEM) { _, _ ->
            ARGUMENT_PARSE_FAILURE_SOCKET_GEM
        }
        captionRegistry.registerMessageFactory(MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_ITEM_GROUP) { _, _ ->
            ARGUMENT_PARSE_FAILURE_ITEM_GROUP
        }
    }
}
