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
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.captions.MythicDropsCaptionKeys
import java.util.Queue

internal class SocketGemArgument<C : Any> private constructor(
    required: Boolean,
    name: String,
    defaultValue: String,
    suggestionsProvider: SuggestionProvider<C>,
    defaultDescription: ArgumentDescription
) : CommandArgument<C, SocketGem>(
    required,
    name,
    SocketGemParser<C>(),
    defaultValue,
    SocketGem::class.java,
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
        fun <C : Any> optional(name: String, defaultSocketGem: SocketGem) =
            newBuilder<C>(name).asOptionalWithDefault(defaultSocketGem.name).build()
    }

    internal class Builder<C : Any> internal constructor(name: String) :
        CommandArgument.Builder<C, SocketGem>(SocketGem::class.java, name) {
        override fun build(): CommandArgument<C, SocketGem> {
            return SocketGemArgument(
                this.isRequired,
                this.name,
                this.defaultValue,
                this.suggestionsProvider,
                this.defaultDescription
            )
        }
    }

    internal class SocketGemParser<C : Any> : ArgumentParser<C, SocketGem> {
        override fun parse(
            commandContext: CommandContext<C>,
            inputQueue: Queue<String>
        ): ArgumentParseResult<SocketGem> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(
                    NoInputProvidedException(
                        SocketGemParser::class.java,
                        commandContext
                    )
                )
            if (input == "*") {
                val random = MythicDropsApi.mythicDrops.socketGemManager.random()
                    ?: return ArgumentParseResult.failure(
                        SocketGemParseException(
                            input,
                            commandContext
                        )
                    )
                return ArgumentParseResult.success(random)
            }
            val socketGem =
                MythicDropsApi.mythicDrops.socketGemManager.getById(input) ?: return ArgumentParseResult.failure(
                    SocketGemParseException(input, commandContext)
                )
            return ArgumentParseResult.success(socketGem)
        }
    }

    internal class SocketGemParseException(input: String, context: CommandContext<*>) : ParserException(
        SocketGemParser::class.java,
        context,
        MythicDropsCaptionKeys.ARGUMENT_PARSE_FAILURE_SOCKET_GEM,
        CaptionVariable.of("input", input)
    )
}
