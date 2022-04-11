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
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.captions.MythicDropsCaptionKeys
import java.util.Queue

internal class ItemGroupArgument<C : Any> private constructor(
    required: Boolean,
    name: String,
    defaultValue: String,
    suggestionsProvider: SuggestionProvider<C>,
    defaultDescription: ArgumentDescription
) : CommandArgument<C, ItemGroup>(
    required,
    name,
    ItemGroupParser<C>(),
    defaultValue,
    ItemGroup::class.java,
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
        fun <C : Any> optional(name: String, defaultItemGroup: ItemGroup) =
            newBuilder<C>(name).asOptionalWithDefault(defaultItemGroup.name).build()
    }

    internal class Builder<C : Any> internal constructor(name: String) :
        CommandArgument.Builder<C, ItemGroup>(ItemGroup::class.java, name) {
        override fun build(): CommandArgument<C, ItemGroup> {
            return ItemGroupArgument(
                this.isRequired,
                this.name,
                this.defaultValue,
                this.suggestionsProvider,
                this.defaultDescription
            )
        }
    }

    internal class ItemGroupParser<C : Any> : ArgumentParser<C, ItemGroup> {
        override fun parse(
            commandContext: CommandContext<C>,
            inputQueue: Queue<String>
        ): ArgumentParseResult<ItemGroup> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(
                    NoInputProvidedException(
                        ItemGroupParser::class.java,
                        commandContext
                    )
                )
            if (input == "*") {
                val random = MythicDropsApi.mythicDrops.itemGroupManager.random()
                    ?: return ArgumentParseResult.failure(
                        ItemGroupParseException(
                            input,
                            commandContext
                        )
                    )
                return ArgumentParseResult.success(random)
            }
            val itemGroup =
                MythicDropsApi.mythicDrops.itemGroupManager.getById(input) ?: return ArgumentParseResult.failure(
                    ItemGroupParseException(input, commandContext)
                )
            return ArgumentParseResult.success(itemGroup)
        }
    }

    internal class ItemGroupParseException(input: String, context: CommandContext<*>) : ParserException(
        ItemGroupParser::class.java,
        context,
        MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_CUSTOM_ITEM,
        CaptionVariable.of("input", input)
    )
}
