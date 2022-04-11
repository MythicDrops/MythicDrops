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

import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getMaterials
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ItemUtilTest {
    companion object {
        private val itemGroupsYaml = YamlConfiguration().apply {
            loadFromString(
                ItemUtilTest::class.java.classLoader.getResource("itemGroups.yml")!!.readText()
            )
        }
        private val legendaryTierYaml = YamlConfiguration().apply {
            loadFromString(
                ItemUtilTest::class.java.classLoader.getResource("tiers/legendary.yml")!!.readText()
            )
        }
        private val itemGroupManager = MythicItemGroupManager()
    }

    @BeforeEach
    fun setup() {
        itemGroupManager.clear()
        itemGroupsYaml.getKeys(false).forEach { key ->
            if (!itemGroupsYaml.isConfigurationSection(key)) {
                return@forEach
            }
            val itemGroupCs: ConfigurationSection = itemGroupsYaml.getConfigurationSection(key) ?: return@forEach
            itemGroupManager.add(MythicItemGroup.fromConfigurationSection(itemGroupCs, key))
        }
    }

    @Test
    fun `ensure that group doesn't contain wrong material`() {
        val tier =
            MythicTier.fromConfigurationSection(legendaryTierYaml, "legendary", itemGroupManager)
        val materials = tier.getMaterials()
        Assertions.assertFalse(materials.contains(Material.STONE_SHOVEL))
    }
}
