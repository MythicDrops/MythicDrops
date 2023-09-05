package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommandRunner
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import org.bukkit.configuration.ConfigurationSection

internal class MythicSocketCommand(
    override var runner: SocketCommandRunner,
    override var command: String,
    override var permissions: List<String> = emptyList()
) : SocketCommand {
    companion object {
        fun fromString(string: String): MythicSocketCommand {
            val runner: SocketCommandRunner
            val command: String

            val indexOfFirstColon = string.indexOf(":")
            if (indexOfFirstColon == -1) {
                runner = SocketCommandRunner.DEFAULT
                command = string.trim { it <= ' ' }
            } else {
                runner = SocketCommandRunner.fromName(string.substring(0, indexOfFirstColon))
                var commandS: String
                commandS = if (string.substring(0, runner.name.length).equals(runner.name, ignoreCase = true)) {
                    string.substring(runner.name.length, string.length).trim { it <= ' ' }
                } else {
                    string.trim { it <= ' ' }
                }
                if (commandS.substring(0, 1).equals(":", ignoreCase = true)) {
                    commandS = commandS.substring(1, commandS.length).trim { it <= ' ' }
                }
                command = commandS.trim { it <= ' ' }
            }

            return MythicSocketCommand(runner = runner, command = command)
        }

        fun fromConfigurationSection(configurationSection: ConfigurationSection): MythicSocketCommand {
            return MythicSocketCommand(
                runner = SocketCommandRunner.fromName(configurationSection.getNonNullString("runner")),
                command = configurationSection.getNonNullString("command"),
                permissions = configurationSection.getStringList("permissions")
            )
        }
    }

    override fun toDebugString(): String {
        return "$runner:$command"
    }

    override fun toString(): String {
        return "SocketCommand(runner=$runner, command='$command', permissions=$permissions)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SocketCommand) return false

        if (runner != other.runner) return false
        if (command != other.command) return false
        if (permissions != other.permissions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = runner.hashCode()
        result = 31 * result + command.hashCode()
        result = 31 * result + permissions.hashCode()
        return result
    }
}

