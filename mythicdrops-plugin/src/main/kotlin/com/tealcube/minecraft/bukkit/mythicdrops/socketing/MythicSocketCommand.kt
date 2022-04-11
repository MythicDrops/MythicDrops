package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommandRunner
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

data class MythicSocketCommand(
    override val command: String,
    override val runner: SocketCommandRunner,
    override val permissions: List<String> = emptyList()
) : SocketCommand {
    companion object {
        fun of(string: String): SocketCommand {
            val indexOfFirstColon = string.indexOf(":")
            return if (indexOfFirstColon == -1) {
                val runner = SocketCommandRunner.DEFAULT
                val command = string.trim { it <= ' ' }
                MythicSocketCommand(command, runner)
            } else {
                val run: SocketCommandRunner = SocketCommandRunner.fromName(string.substring(0, indexOfFirstColon))
                var commandS: String
                commandS = if (string.substring(0, run.name.length).equals(run.name, ignoreCase = true)) {
                    string.substring(run.name.length, string.length).trim { it <= ' ' }
                } else {
                    string.trim { it <= ' ' }
                }
                if (commandS.substring(0, 1).equals(":", ignoreCase = true)) {
                    commandS = commandS.substring(1, commandS.length).trim { it <= ' ' }
                }
                val command = commandS.trim { it <= ' ' }
                MythicSocketCommand(command, run)
            }
        }

        fun fromConfigurationSection(configurationSection: ConfigurationSection): SocketCommand {
            return MythicSocketCommand(
                runner = SocketCommandRunner.fromName(configurationSection.getNonNullString("runner")),
                command = configurationSection.getNonNullString("command"),
                permissions = configurationSection.getStringList("permissions")
            )
        }
    }
}
