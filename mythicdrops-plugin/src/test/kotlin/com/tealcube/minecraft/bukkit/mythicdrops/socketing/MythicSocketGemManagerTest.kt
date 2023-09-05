/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroupManager
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Test

internal class MythicSocketGemManagerTest {
    @Test
    fun `does loadFromConfiguration load socket gems from configuration`() {
        val socketGemYaml = YamlConfiguration()
        val rawSocketGemYaml = """
            version: 5.2.0
            socket-gems:
              Safeguard I:
                trigger-type: WHEN_HIT
                all-of-item-groups:
                  - shield
                potion-effects:
                  ABSORPTION:
                    intensity: 0
                    duration: 2100
                    target: SELF
                    radius: 0
                    chance-to-trigger: 0.5
                weight: 300
                prefix: Spartan
                lore:
                  - "&4Grants absorption when hit"
                family: Safeguard
                level: 1
        """.trimIndent()
        socketGemYaml.loadFromString(rawSocketGemYaml)
        val itemGroupManager = MythicItemGroupManager()
        val socketGemManager = MythicSocketGemManager(itemGroupManager)

        val loadedSocketGems = socketGemManager.loadFromConfiguration(socketGemYaml)

        assertThat(loadedSocketGems).all {
            hasSize(1)
            transform { it.first() }.all {
                transform { it.gemTriggerType }.isEqualTo(GemTriggerType.WHEN_HIT)
                transform { it.level }.isEqualTo(1)
                transform { it.family }.isEqualTo("Safeguard")
            }
        }
    }
}
