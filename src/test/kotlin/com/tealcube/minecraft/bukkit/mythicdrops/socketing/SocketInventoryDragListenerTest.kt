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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing

import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SocketingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.socketing.SocketingOptions
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SocketInventoryDragListenerTest {
    @MockK
    private lateinit var configSettings: ConfigSettings

    @MockK
    private lateinit var itemGroupManager: ItemGroupManager

    @MockK
    private lateinit var settingsManager: SettingsManager

    @MockK
    private lateinit var socketGemManager: SocketGemManager

    @MockK
    private lateinit var socketingSettings: SocketingSettings

    @MockK
    private lateinit var socketingOptions: SocketingOptions

    @MockK
    private lateinit var tierManager: TierManager

    @InjectMockKs
    private lateinit var socketInventoryDragListener: SocketInventoryDragListener

    @BeforeEach
    fun setup() {
        every { settingsManager.configSettings } returns configSettings
        every { settingsManager.socketingSettings } returns socketingSettings
        every { socketingSettings.options } returns socketingOptions
    }

    @Test
    fun `does applySocketGemLore not return different lore for indexOfFirstSocket less than 0`() {
        val previousLore = listOf("Hi", "Mom")
        val indexOfFirstSocket = -1
        val socketGem = mockk<SocketGem>()

        val manipulatedSocketGemLore =
            socketInventoryDragListener.applySocketGemLore(previousLore, indexOfFirstSocket, socketGem)
        assertThat(manipulatedSocketGemLore).isEqualTo(previousLore)
    }

    @Test
    fun `does applySocketGemLore return different lore for indexOfFirstSocket greater than or equal to 0`() {
        val previousLore = listOf("Hi", "Mom")
        val indexOfFirstSocket = 0
        val socketGem = mockk<SocketGem>()
        val socketGemName = "Dank Memes"
        val socketGemLore = listOf("Dank Memes Lore 1", "Dank Memes Lore 2")
        val expectedLore = listOf("${ChatColor.GOLD}Dank Memes", "Dank Memes Lore 1", "Dank Memes Lore 2", "Mom")
        every { socketGem.name } returns socketGemName
        every { socketGem.lore } returns socketGemLore
        every { socketingOptions.useTierColorForSocketName } returns false
        every { socketingOptions.defaultSocketNameColorOnItems } returns ChatColor.GOLD.toString()

        val manipulatedSocketGemLore =
            socketInventoryDragListener.applySocketGemLore(previousLore, indexOfFirstSocket, socketGem)
        assertThat(manipulatedSocketGemLore).isNotEqualTo(previousLore)
        assertThat(manipulatedSocketGemLore).isEqualTo(expectedLore)
    }

    @Test
    fun `does applySocketGemLore use tier color if available and configured to use`() {
        val previousLore = listOf("Hi", "Mom")
        val indexOfFirstSocket = 0
        val socketGem = mockk<SocketGem>()
        val tier = mockk<Tier>()
        val socketGemName = "Dank Memes"
        val socketGemLore = listOf("Dank Memes Lore 1", "Dank Memes Lore 2")
        val expectedLore = listOf("${ChatColor.AQUA}Dank Memes", "Dank Memes Lore 1", "Dank Memes Lore 2", "Mom")
        every { socketGem.name } returns socketGemName
        every { socketGem.lore } returns socketGemLore
        every { socketingOptions.useTierColorForSocketName } returns true
        every { socketingOptions.defaultSocketNameColorOnItems } returns ChatColor.GOLD.toString()
        every { tier.itemDisplayNameFormat } returns "${ChatColor.AQUA}%generalprefix% %generalsuffix%${ChatColor.AQUA}"

        val manipulatedSocketGemLore =
            socketInventoryDragListener.applySocketGemLore(previousLore, indexOfFirstSocket, socketGem, tier)
        assertThat(manipulatedSocketGemLore).isNotEqualTo(previousLore)
        assertThat(manipulatedSocketGemLore).isEqualTo(expectedLore)
    }

    @Test
    fun `does applySocketGemLore not use tier color if available and configured to use`() {
        val previousLore = listOf("Hi", "Mom")
        val indexOfFirstSocket = 0
        val socketGem = mockk<SocketGem>()
        val tier = mockk<Tier>()
        val socketGemName = "Dank Memes"
        val socketGemLore = listOf("Dank Memes Lore 1", "Dank Memes Lore 2")
        val expectedLore = listOf("${ChatColor.GOLD}Dank Memes", "Dank Memes Lore 1", "Dank Memes Lore 2", "Mom")
        every { socketGem.name } returns socketGemName
        every { socketGem.lore } returns socketGemLore
        every { socketingOptions.useTierColorForSocketName } returns false
        every { socketingOptions.defaultSocketNameColorOnItems } returns ChatColor.GOLD.toString()

        val manipulatedSocketGemLore =
            socketInventoryDragListener.applySocketGemLore(previousLore, indexOfFirstSocket, socketGem, tier)
        assertThat(manipulatedSocketGemLore).isNotEqualTo(previousLore)
        assertThat(manipulatedSocketGemLore).isEqualTo(expectedLore)
    }

    @Test
    fun `does applySocketGemLore colorize added lines of lore`() {
        val previousLore = listOf("Hi", "Mom")
        val indexOfFirstSocket = 0
        val socketGem = mockk<SocketGem>()
        val tier = mockk<Tier>()
        val socketGemName = "Dank Memes"
        val socketGemLore = listOf("&4Dank Memes Lore 1", "Dank Memes Lore 2")
        val expectedLore =
            listOf("${ChatColor.GOLD}Dank Memes", "${ChatColor.DARK_RED}Dank Memes Lore 1", "Dank Memes Lore 2", "Mom")
        every { socketGem.name } returns socketGemName
        every { socketGem.lore } returns socketGemLore
        every { socketingOptions.useTierColorForSocketName } returns false
        every { socketingOptions.defaultSocketNameColorOnItems } returns ChatColor.GOLD.toString()

        val manipulatedSocketGemLore =
            socketInventoryDragListener.applySocketGemLore(previousLore, indexOfFirstSocket, socketGem, tier)
        assertThat(manipulatedSocketGemLore).isNotEqualTo(previousLore)
        assertThat(manipulatedSocketGemLore).isEqualTo(expectedLore)
    }

    @Test
    fun `does applySocketGemDisplayNamePrefix not add prefix if not available`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.prefix } returns ""

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNamePrefix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isEqualTo(previousDisplayName)
    }

    @Test
    fun `does applySocketGemDisplayNamePrefix add prefix if available`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.prefix } returns "Extra"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns false

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNamePrefix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("Extra${ChatColor.RESET} Dank Memes")
    }

    @Test
    fun `does applySocketGemDisplayNamePrefix not add duplicate prefix if available and configured to block multiple changes`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.prefix } returns "Dank"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns true

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNamePrefix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isEqualTo(previousDisplayName)
    }

    @Test
    fun `does applySocketGemDisplayNamePrefix add duplicate prefix if available and configured to not block multiple changes`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.prefix } returns "Dank"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns false

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNamePrefix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("Dank${ChatColor.RESET} Dank Memes")
    }

    @Test
    fun `does applySocketGemDisplayNamePrefix respect starting ChatColors`() {
        val previousDisplayName = "${ChatColor.AQUA}Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.prefix } returns "Extra"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns false

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNamePrefix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("${ChatColor.AQUA}Extra${ChatColor.RESET} ${ChatColor.AQUA}Dank Memes")
    }

    @Test
    fun `does applySocketGemDisplayNameSuffix not add suffix if not available`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.suffix } returns ""

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNameSuffix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isEqualTo(previousDisplayName)
    }

    @Test
    fun `does applySocketGemDisplayNameSuffix add suffix if available`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.suffix } returns "Extra"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns false

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNameSuffix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("Dank Memes Extra")
    }

    @Test
    fun `does applySocketGemDisplayNameSuffix not add duplicate suffix if available and configured to block multiple changes`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.suffix } returns "Memes"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns true

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNameSuffix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isEqualTo(previousDisplayName)
    }

    @Test
    fun `does applySocketGemDisplayNameSuffix add duplicate suffix if available and configured to not block multiple changes`() {
        val previousDisplayName = "Dank Memes"
        val socketGem = mockk<SocketGem>()
        every { socketGem.suffix } returns "Memes"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns false

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNameSuffix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("Dank Memes Memes")
    }

    @Test
    fun `does applySocketGemDisplayNameSuffix respect ending ChatColors`() {
        val previousDisplayName = "Dank Memes${ChatColor.AQUA}"
        val socketGem = mockk<SocketGem>()
        every { socketGem.suffix } returns "Memes"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns false

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayNameSuffix(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("Dank Memes${ChatColor.AQUA} Memes${ChatColor.AQUA}")
    }

    @Test
    fun `does applySocketGemDisplayName apply both prefix and suffix`() {
        val previousDisplayName = "${ChatColor.AQUA}Dank Memes${ChatColor.BLUE}"
        val socketGem = mockk<SocketGem>()
        every { socketGem.prefix } returns "Extra"
        every { socketGem.suffix } returns "of Dankness"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns false

        val manipulatedDisplayName =
            socketInventoryDragListener.applySocketGemDisplayName(previousDisplayName, socketGem)
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("${ChatColor.AQUA}Extra${ChatColor.RESET} ${ChatColor.AQUA}Dank Memes${ChatColor.BLUE} ${ChatColor.AQUA}of Dankness${ChatColor.BLUE}")
    }

    @Test
    fun `does applySocketGemDisplayName respect preventing multiple changes from sockets`() {
        val previousDisplayName = "${ChatColor.AQUA}Dank Memes${ChatColor.BLUE}"
        val socketGem = mockk<SocketGem>()
        every { socketGem.prefix } returns "Extra"
        every { socketGem.suffix } returns "of Dankness"
        every { socketGemManager.get() } returns setOf(socketGem)
        every { socketingOptions.isPreventMultipleNameChangesFromSockets } returns true

        val manipulatedDisplayName = socketInventoryDragListener.applySocketGemDisplayName(
            socketInventoryDragListener.applySocketGemDisplayName(
                previousDisplayName,
                socketGem
            ),
            socketGem
        )
        assertThat(manipulatedDisplayName).isNotEqualTo(previousDisplayName)
        assertThat(manipulatedDisplayName).isEqualTo("${ChatColor.AQUA}Extra${ChatColor.RESET} ${ChatColor.AQUA}Dank Memes${ChatColor.BLUE} ${ChatColor.AQUA}of Dankness${ChatColor.BLUE}")
    }

    @Test
    fun `does applySocketGemEnchantments add new entry to Map when key does not exist`() {
        val previousEnchantments = mapOf(
            Enchantment.DAMAGE_ALL to 5
        )
        val socketGem = mockk<SocketGem>()
        every { socketGem.enchantments } returns setOf(
            MythicEnchantment(Enchantment.ARROW_DAMAGE, 1, 1)
        )

        val manipulatedEnchantments =
            socketInventoryDragListener.applySocketGemEnchantments(previousEnchantments, socketGem)
        assertThat(manipulatedEnchantments).isNotEqualTo(previousEnchantments)
        assertThat(manipulatedEnchantments).containsEntry(Enchantment.DAMAGE_ALL, 5)
        assertThat(manipulatedEnchantments).containsEntry(Enchantment.ARROW_DAMAGE, 1)
    }

    @Test
    fun `does applySocketGemEnchantments add to existing levels in Map when key does exist`() {
        val previousEnchantments = mapOf(
            Enchantment.DAMAGE_ALL to 5
        )
        val socketGem = mockk<SocketGem>()
        every { socketGem.enchantments } returns setOf(
            MythicEnchantment(Enchantment.DAMAGE_ALL, 1, 1)
        )
        val manipulatedEnchantments =
            socketInventoryDragListener.applySocketGemEnchantments(previousEnchantments, socketGem)
        assertThat(manipulatedEnchantments).isNotEqualTo(previousEnchantments)
        assertThat(manipulatedEnchantments).containsEntry(Enchantment.DAMAGE_ALL, 6)
    }
}
