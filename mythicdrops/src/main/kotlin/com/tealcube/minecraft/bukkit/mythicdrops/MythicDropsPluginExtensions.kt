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
package com.tealcube.minecraft.bukkit.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggingFormatter
import com.tealcube.minecraft.bukkit.mythicdrops.logging.rebelliousAddHandler
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap
import org.bstats.bukkit.Metrics
import java.util.logging.FileHandler
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.Logger

fun MythicDropsPlugin.setupLogHandler(): Handler? = try {
    val pathToLogOutput = String.format("%s/mythicdrops.log", dataFolder.absolutePath)
    val fileHandler = FileHandler(pathToLogOutput, true)
    fileHandler.formatter = MythicLoggingFormatter()

    Logger.getLogger("com.tealcube.minecraft.bukkit.mythicdrops").rebelliousAddHandler(fileHandler)
    Logger.getLogger("io.pixeloutlaw.minecraft.spigot").rebelliousAddHandler(fileHandler)
    Logger.getLogger("po.io.pixeloutlaw.minecraft.spigot").rebelliousAddHandler(fileHandler)

    logger.info("MythicDrops logging has been setup")
    fileHandler
} catch (ex: Exception) {
    logger.log(Level.SEVERE, "Unable to setup logging for MythicDrops", ex)
    null
}

fun MythicDropsPlugin.setupMetrics() {
    val metrics = Metrics(this)
    metrics.addCustomChart(Metrics.SingleLineChart("amount_of_tiers") { TierMap.size })
    metrics.addCustomChart(Metrics.SingleLineChart("amount_of_custom_items") { customItemManager.get().size })
    metrics.addCustomChart(Metrics.SingleLineChart("amount_of_socket_gems") { socketGemManager.getSocketGems().size })
}
