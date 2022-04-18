package com.tealcube.minecraft.bukkit.mythicdrops.templating

internal abstract class DashStringTemplate(operation: String) : NonBlankStringTemplate(operation) {
    companion object {
        private const val dashPatternString = "\\s*-\\s*"
        private val dashPattern = dashPatternString.toRegex()
    }

    override fun nonBlankInvoke(arguments: String): String {
        val split = arguments.split(dashPattern).map { it.trim() }.filter(String::isNotEmpty)
        return splitDashInvoke(arguments, split)
    }

    abstract fun splitDashInvoke(original: String, split: List<String>): String
}
