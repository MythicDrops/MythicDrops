package com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.arguments

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.captions.CaptionVariable
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import cloud.commandframework.exceptions.parsing.ParserException
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.captions.MythicDropsCaptionKeys
import java.util.Queue

internal class CustomItemArgument<C : Any> private constructor(
    required: Boolean,
    name: String,
    defaultValue: String,
    suggestionsProvider: SuggestionProvider<C>,
    defaultDescription: ArgumentDescription
) : CommandArgument<C, CustomItem>(
    required,
    name,
    CustomItemParser<C>(),
    defaultValue,
    CustomItem::class.java,
    suggestionsProvider,
    defaultDescription
) {
    companion object {
        @JvmStatic
        fun <C : Any> newBuilder(name: String) = Builder<C>(name)

        @JvmStatic
        fun <C : Any> of(name: String) = newBuilder<C>(name).asRequired().build()

        @JvmStatic
        fun <C : Any> optional(name: String) = newBuilder<C>(name).asOptionalWithDefault("*").build()

        @JvmStatic
        fun <C : Any> optional(name: String, customItem: CustomItem) = newBuilder<C>(name).asOptionalWithDefault(customItem.name).build()
    }

    internal class Builder<C : Any> internal constructor(name: String) :
        CommandArgument.Builder<C, CustomItem>(CustomItem::class.java, name) {
        override fun build(): CommandArgument<C, CustomItem> {
            return CustomItemArgument(
                this.isRequired,
                this.name,
                this.defaultValue,
                this.suggestionsProvider,
                this.defaultDescription
            )
        }
    }

    internal class CustomItemParser<C : Any> : ArgumentParser<C, CustomItem> {
        override fun parse(
            commandContext: CommandContext<C>,
            inputQueue: Queue<String>
        ): ArgumentParseResult<CustomItem> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(
                    NoInputProvidedException(
                        CustomItemParser::class.java,
                        commandContext
                    )
                )
            if (input == "*") {
                val random = MythicDropsApi.mythicDrops.customItemManager.random()
                    ?: return ArgumentParseResult.failure(CustomItemParseException(input, commandContext))
                return ArgumentParseResult.success(random)
            }
            val customItem =
                MythicDropsApi.mythicDrops.customItemManager.getById(input) ?: return ArgumentParseResult.failure(
                    CustomItemParseException(input, commandContext)
                )
            return ArgumentParseResult.success(customItem)
        }
    }

    internal class CustomItemParseException(input: String, context: CommandContext<*>) : ParserException(
        CustomItemParser::class.java,
        context,
        MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_CUSTOM_ITEM,
        CaptionVariable.of("input", input)
    )
}
