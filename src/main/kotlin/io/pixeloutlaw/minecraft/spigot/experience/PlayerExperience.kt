/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package io.pixeloutlaw.minecraft.spigot.experience

import org.bukkit.entity.Player
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

/**
 * Utility for getting/setting/manipulating the experience that a Player has.
 */
// Suppress detekt.MagicNumber because we're using magic numbers from a Minecraft formula.
@Suppress("detekt.MagicNumber")
internal object PlayerExperience {

    /**
     * Determines the experience threshold for a specific [level] based off of the formula from the
     * [Minecraft Wiki](https://minecraft.gamepedia.com/Experience#Leveling_up).
     *
     * [level] will be coerced to be minimum 0 and maximum 10000.
     *
     * @param level level to check
     * @return experience required to be at the passed in level
     */
    fun getExpFromLevel(level: Int): Int {
        // we hard limit the level to 1000 and even that is incredibly high
        val coercedLevel = level.coerceAtLeast(0).coerceAtMost(10000).toDouble()

        // due to how when works, it hits the first statement and traverses down the cases from there,
        // it will only ever hit one of the below cases.
        return when {
            coercedLevel >= 32 -> ((4.5 * coercedLevel.pow(2)) - (162.5 * coercedLevel) + 2220).toInt()
            coercedLevel >= 17 -> ((2.5 * coercedLevel.pow(2)) - (40.5 * coercedLevel) + 360).toInt()
            else -> (coercedLevel.pow(2) + (6 * coercedLevel)).toInt()
        }
    }

    /**
     * Determines how much experience is required to get to the next level from the given [level] based off
     * the formula from the [Minecraft Wiki](https://minecraft.gamepedia.com/Experience#Leveling_up).
     *
     * [level] will be coerced to be minimum 0 and maximum 10000.
     *
     * @param level level to check
     * @return experience required to get to the next level
     */
    fun getExpForNextLevel(level: Int): Int {
        // we hard limit the level to 1000 and even that is incredibly high
        val coercedLevel = level.coerceAtLeast(0).coerceAtMost(10000)

        // due to how when works, it hits the first statement and traverses down the cases from there,
        // it will only ever hit one of the below cases.
        return when {
            coercedLevel >= 31 -> ((9 * coercedLevel) - 158)
            coercedLevel >= 16 -> ((5 * coercedLevel) - 38)
            else -> ((2 * coercedLevel) + 7)
        }
    }

    /**
     * Determines the level based off of the given amount of experience. The integer digits (left hand side of the
     * Double) represent the level and the fractional digits (right hand side of the Double) represent the
     * fractional experience.
     *
     * [exp] will be coerced to be minimum 0.
     *
     * @param exp amount of experience
     * @return level and fractional experience
     */
    fun getLevelFromExp(exp: Int): Double {
        val coercedExp = exp.coerceAtLeast(0).toDouble()

        // due to how when works, it hits the first statement and traverses down the cases from there,
        // it will only ever hit one of the below cases.
        return when {
            coercedExp > 1395 -> {
                (sqrt(72 * coercedExp - 54215) + 325) / 18
            }

            coercedExp > 315 -> {
                sqrt(40 * coercedExp - 7839) / 10 + 8.1
            }

            coercedExp > 0 -> {
                sqrt(coercedExp + 9) - 3
            }

            else -> 0.0
        }
    }

    /**
     * Gets the experience from a player based off of their current level and fractional exp.
     *
     * @param player Player to get experience from
     * @return experience picked up by player
     */
    fun getExp(player: Player): Int {
        return getExpFromLevel(player.level) + round(getExpForNextLevel(player.level) * player.exp).toInt()
    }

    /**
     * Changes a Player's exp by an amount.
     *
     * @param player player to change experience for
     * @param exp amount to change by
     */
    fun changeExp(player: Player, exp: Int) {
        val expToSet = (getExp(player) + exp).coerceAtLeast(0)

        // level and exp are integer digits and fractional digits of the return value
        val levelAndExp = getLevelFromExp(expToSet)
        val newPlayerLevel = levelAndExp.toInt()
        val newPlayerExp = (levelAndExp - newPlayerLevel).toFloat()

        player.level = newPlayerLevel
        player.exp = newPlayerExp
    }

    /**
     * Attempts to determine if the given Player has the given amount of experience.
     *
     * @param player player to check experience
     * @param amount amount to check
     * @return if player has amount
     */
    fun hasExp(player: Player, amount: Int): Boolean {
        return getExp(player) >= amount
    }
}
