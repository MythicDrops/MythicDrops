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
package com.tealcube.minecraft.bukkit.mythicdrops.items

import com.tealcube.minecraft.bukkit.mythicdrops.getOrCreateSection
import org.assertj.core.api.Assertions.assertThat
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class MythicCustomItemTest {
    companion object {
        private val customItemsYaml = YamlConfiguration()

        @BeforeAll
        @JvmStatic
        fun setupAll() {
            val customItems510YamlText =
                this.javaClass.classLoader.getResource("customItems_5_1_0.yml")?.readText() ?: ""
            customItemsYaml.loadFromString(customItems510YamlText)
        }
    }

    @Test
    fun `does fromConfigurationSection not return Material_AIR`() {
        val customItem = MythicCustomItem.fromConfigurationSection(customItemsYaml.getOrCreateSection("socketsword"), "socketsword")
        assertThat(customItem.material).isNotEqualTo(Material.AIR)
    }
}
