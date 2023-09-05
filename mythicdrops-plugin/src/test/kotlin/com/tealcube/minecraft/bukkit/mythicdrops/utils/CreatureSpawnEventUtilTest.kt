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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Cow
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Ghast
import org.bukkit.entity.MagmaCube
import org.bukkit.entity.Piglin
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Ravager
import org.bukkit.entity.Slime
import org.bukkit.entity.Wither
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.CreatureSpawnEvent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreatureSpawnEventUtilTest {
    private val spawnReason = CreatureSpawnEvent.SpawnReason.DEFAULT

    @MockK
    lateinit var slime: Slime

    @MockK
    lateinit var magmaCube: MagmaCube

    @MockK
    lateinit var enderDragon: EnderDragon

    @MockK
    lateinit var zombie: Zombie

    @MockK
    lateinit var ghast: Ghast

    @MockK
    lateinit var cow: Cow

    @MockK
    lateinit var wither: Wither

    @MockK
    lateinit var ravager: Ravager

    @MockK
    lateinit var armorStand: ArmorStand

    @MockK
    lateinit var piglin: Piglin

    @MockK
    lateinit var piglinBrute: PiglinBrute

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnTrueForCancelled() {
        val event = CreatureSpawnEvent(zombie, spawnReason)
        event.isCancelled = true

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isTrue()
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForSlime() {
        val event = CreatureSpawnEvent(slime, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForMagmaCube() {
        val event = CreatureSpawnEvent(magmaCube, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForEnderDragon() {
        val event = CreatureSpawnEvent(enderDragon, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForZombie() {
        val event = CreatureSpawnEvent(zombie, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForWither() {
        val event = CreatureSpawnEvent(wither, spawnReason)
        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForGhast() {
        val event = CreatureSpawnEvent(ghast, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun `does should cancel drops based on creature spawn event return false for Ravager`() {
        val event = CreatureSpawnEvent(ravager, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForCow() {
        val event = CreatureSpawnEvent(cow, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun `does shouldCancelDropsBasedOnCreatureSpawnEvent return true for ArmorStand`() {
        val event = CreatureSpawnEvent(armorStand, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isTrue()
    }

    @Test
    fun `does shouldCancelDropsBasedOnCreatureSpawnEvent return false for Piglin`() {
        val event = CreatureSpawnEvent(piglin, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }

    @Test
    fun `does shouldCancelDropsBasedOnCreatureSpawnEvent return false for PiglinBrute`() {
        val event = CreatureSpawnEvent(piglinBrute, spawnReason)

        assertThat(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event)).isFalse()
    }
}
