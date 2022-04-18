package com.tealcube.minecraft.bukkit.mythicdrops.koin

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin
import io.mockk.every
import io.mockk.mockk
import io.pixeloutlaw.minecraft.spigot.MockBukkit
import org.bukkit.Server
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class MythicKoinModuleTest : KoinTest {
    val pluginManager: PluginManager = mockk(relaxUnitFun = true)
    lateinit var server: Server

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mockBukkitServer()
        every { server.pluginManager } returns pluginManager
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmockBukkitServer()
    }

    @Test
    fun verifyKoinApp() {
        val mythicDropsPlugin = mockk<MythicDropsPlugin>()
        val pluginDescriptionFile = mockk<PluginDescriptionFile>()
        val consoleCommandSender = mockk<ConsoleCommandSender>()

        every { mythicDropsPlugin.name } returns "MythicDrops"
        every { mythicDropsPlugin.description } returns pluginDescriptionFile
        every { mythicDropsPlugin.server } returns server
        every { server.consoleSender } returns consoleCommandSender
        every { server.onlinePlayers } returns emptyList()
        every { pluginDescriptionFile.name } returns "MythicDrops"
        every { pluginManager.isPluginEnabled(any<String>()) } returns false

        koinApplication {
            modules(listOf(pluginModule(mythicDropsPlugin)) + featureModules)
            checkModules()
        }
    }
}
