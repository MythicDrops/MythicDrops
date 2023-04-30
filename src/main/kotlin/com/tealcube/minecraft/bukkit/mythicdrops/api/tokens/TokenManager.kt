package com.tealcube.minecraft.bukkit.mythicdrops.api.tokens

import com.tealcube.minecraft.bukkit.mythicdrops.api.managers.ConfigurationBasedManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.managers.WeightedManager

/**
 * A manager for storing and retrieving [Token]s.
 */
interface TokenManager : WeightedManager<Token, String>, ConfigurationBasedManager<Token>
