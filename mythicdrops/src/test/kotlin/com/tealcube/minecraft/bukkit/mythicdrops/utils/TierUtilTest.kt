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
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicSettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierManager
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TierUtilTest {
    companion object {
        val creatureSpawningYaml5_0_0 = YamlConfiguration()
    }

    lateinit var settingsManager: SettingsManager
    lateinit var tierManager: TierManager

    @BeforeEach
    fun setup() {
        val creatureSpawningYaml5_0_0Text =
            this.javaClass.classLoader.getResource("creatureSpawning_5_0_0.yml")?.readText() ?: ""
        creatureSpawningYaml5_0_0.loadFromString(creatureSpawningYaml5_0_0Text)

        val exoticTier = MythicTier(name = "exotic", weight = 0.9)
        val legendaryTier = MythicTier(name = "legendary", weight = 0.1)

        settingsManager = MythicSettingsManager().apply {
            loadCreatureSpawningSettingsFromConfiguration(creatureSpawningYaml5_0_0)
        }
        tierManager = MythicTierManager().apply {
            add(exoticTier)
            add(legendaryTier)
        }
    }

    @Test
    fun `does getTierForEntity pick heavier tier 800 times out of 1000`() {
        val wither = mockk<LivingEntity>()
        val world = mockk<World>()
        every { world.spawnLocation } returns Location(world, 0.0, 0.0, 0.0)
        every { wither.type } returns EntityType.WITHER
        every { wither.location } returns Location(world, 0.0, 0.0, 0.0)
        every { wither.world } returns world

        val selectedTiers = mutableMapOf<String, Int>()
        repeat(1000) {
            val selectedTier =
                TierUtil.getTierForLivingEntity(wither, settingsManager.creatureSpawningSettings, tierManager)
            selectedTier?.let { tier ->
                selectedTiers[tier.name] = selectedTiers.getOrDefault(tier.name, 0) + 1
            }
        }

        assertThat(selectedTiers["exotic"]).isGreaterThanOrEqualTo(800)
        assertThat(selectedTiers["legendary"]).isLessThanOrEqualTo(200)
    }
}
