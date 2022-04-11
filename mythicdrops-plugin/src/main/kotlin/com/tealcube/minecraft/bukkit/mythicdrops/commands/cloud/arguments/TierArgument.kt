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
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.captions.MythicDropsCaptionKeys
import java.util.Queue

internal class TierArgument<C : Any> private constructor(
    required: Boolean,
    name: String,
    defaultValue: String,
    suggestionsProvider: SuggestionProvider<C>,
    defaultDescription: ArgumentDescription
) : CommandArgument<C, Tier>(
    required,
    name,
    TierParser<C>(),
    defaultValue,
    Tier::class.java,
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
        fun <C : Any> optional(name: String, defaultTier: Tier) =
            newBuilder<C>(name).asOptionalWithDefault(defaultTier.name).build()
    }

    internal class Builder<C : Any> internal constructor(name: String) :
        CommandArgument.Builder<C, Tier>(Tier::class.java, name) {
        override fun build(): CommandArgument<C, Tier> {
            return TierArgument(
                this.isRequired,
                this.name,
                this.defaultValue,
                this.suggestionsProvider,
                this.defaultDescription
            )
        }
    }

    internal class TierParser<C : Any> : ArgumentParser<C, Tier> {
        override fun parse(commandContext: CommandContext<C>, inputQueue: Queue<String>): ArgumentParseResult<Tier> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(NoInputProvidedException(TierParser::class.java, commandContext))
            if (input == "*") {
                val random = MythicDropsApi.mythicDrops.tierManager.random()
                    ?: return ArgumentParseResult.failure(
                        TierParseException(
                            input,
                            commandContext
                        )
                    )
                return ArgumentParseResult.success(random)
            }
            val tier = MythicDropsApi.mythicDrops.tierManager.getByName(input) ?: return ArgumentParseResult.failure(
                TierParseException(input, commandContext)
            )
            return ArgumentParseResult.success(tier)
        }
    }

    internal class TierParseException(input: String, context: CommandContext<*>) : ParserException(
        TierParser::class.java,
        context,
        MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_TIER,
        CaptionVariable.of("input", input)
    )
}
