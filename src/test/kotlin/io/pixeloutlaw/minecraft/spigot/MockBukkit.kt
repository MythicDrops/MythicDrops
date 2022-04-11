package io.pixeloutlaw.minecraft.spigot

import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import io.mockk.every
import io.mockk.mockk
import org.bukkit.Bukkit
import org.bukkit.Server
import java.lang.reflect.Field

object MockBukkit {
    fun unmockBukkitServer() {
        try {
            val server: Field = Bukkit::class.java.getDeclaredField("server")
            server.isAccessible = true
            server.set(null, null)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalArgumentException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }
    }

    fun mockBukkitServer(): Server {
        val mockkServer: Server = mockk()
        every { mockkServer.logger } returns JulLoggerFactory.getLogger(Server::class)
        every { mockkServer.name } returns "mockk"
        every { mockkServer.version } returns "mockk"
        every { mockkServer.bukkitVersion } returns "mockk"
        Bukkit.setServer(mockkServer)
        return mockkServer
    }
}