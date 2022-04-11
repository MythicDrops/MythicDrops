package com.tealcube.minecraft.bukkit.mythicdrops.commands.cloud.arguments

import cloud.commandframework.context.CommandContext
import java.util.function.BiFunction

typealias SuggestionProvider<C> = BiFunction<CommandContext<C>, String, List<String>>
