package com.tealcube.minecraft.bukkit.mythicdrops.tokens

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.choices.WeightedChoice
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.Token
import com.tealcube.minecraft.bukkit.mythicdrops.api.tokens.TokenManager
import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import com.tealcube.minecraft.bukkit.mythicdrops.managers.MythicManager
import io.pixeloutlaw.kindling.Log
import org.bukkit.configuration.Configuration

internal class MythicTokenManager(private val mythicDrops: MythicDrops) : MythicManager<Token, String>(), TokenManager {
    override fun randomByWeight(block: (Token) -> Boolean): Token? =
        WeightedChoice.between(get()).choose(block)

    override fun contains(id: String): Boolean = managed.containsKey(id.lowercase())

    override fun remove(id: String) {
        managed.remove(id.lowercase())
    }

    override fun getById(id: String): Token? = managed[id.lowercase()]

    override fun getId(item: Token): String = item.name.lowercase()

    override fun loadFromConfiguration(configuration: Configuration): Set<Token> {
        Log.debug("Loading tokens")
        val tokenConfigurationSection = configuration.getOrCreateSection("tokens")
        val tokens = tokenConfigurationSection.getKeys(false).mapNotNull {
            MythicToken.fromConfigurationSection(
                tokenConfigurationSection.getOrCreateSection(it),
                it,
                mythicDrops
            )
        }
        Log.info("Loaded tokens (${tokens.size}): ${tokens.joinToString { it.name }}")
        return tokens.toSet()
    }
}
