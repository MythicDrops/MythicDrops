package io.pixeloutlaw.minecraft.spigot

import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import io.mockk.every
import io.mockk.mockk
import io.pixeloutlaw.minecraft.spigot.exceptions.SomethingWentWrongException
import org.bukkit.Bukkit
import org.bukkit.Server
import java.lang.reflect.Field

object MockBukkit {
    @Suppress("detekt.ThrowsCount")

    fun unmockBukkitServer() {
        // too many to throw
        try {
            val server: Field = Bukkit::class.java.getDeclaredField("server")
            server.isAccessible = true
            server.set(null, null)
        } catch (e: NoSuchFieldException) {
            throw SomethingWentWrongException(e)
        } catch (e: IllegalArgumentException) {
            throw SomethingWentWrongException(e)
        } catch (e: IllegalAccessException) {
            throw SomethingWentWrongException(e)
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
