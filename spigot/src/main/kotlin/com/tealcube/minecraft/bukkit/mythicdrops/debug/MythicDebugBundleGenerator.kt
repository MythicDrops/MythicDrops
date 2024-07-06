package com.tealcube.minecraft.bukkit.mythicdrops.debug

import com.tealcube.minecraft.bukkit.mythicdrops.api.debug.DebugBundleGenerator
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import dev.mythicdrops.prettyPrint
import io.pixeloutlaw.kindling.Log
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Single
internal class MythicDebugBundleGenerator(
    private val plugin: Plugin,
    private val settingsManager: SettingsManager,
    private val customItemManager: CustomItemManager,
    private val socketGemManager: SocketGemManager,
    private val tierManager: TierManager,
    private val socketTypeManager: SocketTypeManager,
    private val socketExtenderTypeManager: SocketExtenderTypeManager
) : DebugBundleGenerator {
    companion object {
        private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    }

    override fun generateDebugBundle(): String {
        Log.info("Generating debug bundle...")
        val debugDirectory =
            plugin.dataFolder.resolve("debug").resolve(dateTimeFormatter.format(LocalDateTime.now(ZoneOffset.UTC)))
        if (!debugDirectory.mkdirs()) {
            Log.error("Unable to create debug directory")
            return debugDirectory.absolutePath
        }

        // Basic data first
        debugDirectory.resolve("basic.txt").writeText(
            """
            version: ${plugin.description.version}
            server package: ${Bukkit.getServer().javaClass.getPackage()}
            """.trimIndent()
        )
        debugDirectory
            .resolve("settings.txt")
            .writeText(settingsManager.prettyPrint())
        debugDirectory
            .resolve("customItems.txt")
            .writeText(customItemManager.prettyPrint())
        debugDirectory
            .resolve("socketGems.txt")
            .writeText(socketGemManager.prettyPrint())
        debugDirectory
            .resolve("tiers.txt")
            .writeText(tierManager.prettyPrint())
        debugDirectory
            .resolve("socketTypes.txt")
            .writeText(socketTypeManager.prettyPrint())
        debugDirectory
            .resolve("socketExtenderTypes.txt")
            .writeText(socketExtenderTypeManager.prettyPrint())

        Log.info("Wrote debug bundle to $debugDirectory")
        return debugDirectory.absolutePath
    }
}
