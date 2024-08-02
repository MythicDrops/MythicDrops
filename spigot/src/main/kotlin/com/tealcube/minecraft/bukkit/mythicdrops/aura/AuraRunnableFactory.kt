package com.tealcube.minecraft.bukkit.mythicdrops.aura

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCacheManager
import com.tealcube.minecraft.bukkit.mythicdrops.debug.MythicDebugManager
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import org.koin.core.annotation.Single

@Single
internal class AuraRunnableFactory(
    private val mythicDebugManager: MythicDebugManager,
    private val plugin: Plugin,
    private val settingsManager: SettingsManager,
    private val socketGemCacheManager: SocketGemCacheManager,
    private val socketGemManager: SocketGemManager
) {
    fun createAuraRunnableTask(): BukkitTask? {
        val hasAuraGem =
            socketGemManager
                .get()
                .any { it.gemTriggerType == GemTriggerType.AURA }
        if (!hasAuraGem) {
            return null
        }

        return AuraRunnable(
            mythicDebugManager,
            socketGemCacheManager
        ).runTaskTimer(
            plugin,
            20,
            20 *
                settingsManager.socketingSettings.options.auraRefreshInSeconds
                    .toLong()
        )
    }
}
