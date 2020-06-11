/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.api.socketing

open class SocketCommand(string: String) {
    var runner: SocketCommandRunner
    var command: String
    var permissions: List<String> = emptyList()

    init {
        val indexOfFirstColon = string.indexOf(":")
        if (indexOfFirstColon == -1) {
            runner = SocketCommandRunner.DEFAULT
            command = string.trim { it <= ' ' }
        } else {
            var run: SocketCommandRunner? = SocketCommandRunner.fromName(string.substring(0, indexOfFirstColon))
            if (run == null) {
                run = SocketCommandRunner.DEFAULT
            }
            runner = run
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
    }

    @Deprecated("Unused")
    fun toConfigString(): String {
        return "${runner.name}:${command.trim { it <= ' ' }}"
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
