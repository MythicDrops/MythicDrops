package com.tealcube.minecraft.bukkit.mythicdrops.templating

internal abstract class NonBlankStringTemplate(operation: String) : Template(operation) {
    override fun invoke(arguments: String): String {
        if (arguments.isBlank()) {
            return arguments
        }
        return nonBlankInvoke(arguments)
    }

    abstract fun nonBlankInvoke(arguments: String): String
}