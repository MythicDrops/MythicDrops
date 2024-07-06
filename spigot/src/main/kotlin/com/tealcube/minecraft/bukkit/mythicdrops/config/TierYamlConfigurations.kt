package com.tealcube.minecraft.bukkit.mythicdrops.config

import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import io.pixeloutlaw.minecraft.spigot.klob.Glob
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Single

@Single
internal class TierYamlConfigurations(private val plugin: Plugin) {
    private val glob = Glob.from("tiers/**/*.yml")
    private val tierYamlConfigurations: MutableList<VersionedFileAwareYamlConfiguration> = mutableListOf()

    fun load() {
        tierYamlConfigurations.clear()
        tierYamlConfigurations.addAll(
            glob.iterate(plugin.dataFolder.toPath())
                .asSequence()
                .toList()
                .map { VersionedFileAwareYamlConfiguration(it.toFile()) }
        )
    }

    fun getTierYamlConfigurations(): List<VersionedFileAwareYamlConfiguration> {
        return tierYamlConfigurations.toList()
    }
}
