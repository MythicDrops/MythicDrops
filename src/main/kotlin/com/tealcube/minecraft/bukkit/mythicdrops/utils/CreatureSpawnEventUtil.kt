/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Ghast
import org.bukkit.entity.Monster
import org.bukkit.entity.Slime
import org.bukkit.event.entity.CreatureSpawnEvent

object CreatureSpawnEventUtil {
    fun shouldCancelDropsBasedOnCreatureSpawnEvent(event: CreatureSpawnEvent): Boolean {
        if (event.isCancelled) {
            return true
        }
        val isEnderDragon = event.entity is EnderDragon
        val isGhast = event.entity is Ghast
        val isMonster = event.entity is Monster
        val isSlime = event.entity is Slime
        return !isEnderDragon && !isGhast && !isMonster && !isSlime
    }
}