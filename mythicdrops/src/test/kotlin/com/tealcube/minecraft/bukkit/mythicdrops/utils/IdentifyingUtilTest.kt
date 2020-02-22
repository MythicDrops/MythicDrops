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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.CreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.settings.MythicCreatureSpawningSettings
import com.tealcube.minecraft.bukkit.mythicdrops.settings.identification.items.MythicUnidentifiedItemOptions
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTier
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.MythicTierManager
import org.assertj.core.api.Assertions.assertThat
import org.bukkit.entity.EntityType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class IdentifyingUtilTest {
    lateinit var tierManager: TierManager

    @BeforeEach
    fun setup() {
        tierManager = MythicTierManager()
    }

    @Test
    fun `does determineTierForIdentify return null if nothing matches`() {
        assertThat(
            IdentifyingUtil.determineTierForIdentify(
                MythicCreatureSpawningSettings(),
                tierManager,
                emptyList(),
                null,
                emptyList(),
                null
            )
        ).isNull()
    }

    @Test
    fun `does determineTierForIdentify return null if allowableTiers is empty`() {
        assertThat(
            IdentifyingUtil.determineTierForIdentify(
                MythicCreatureSpawningSettings(),
                tierManager,
                emptyList(),
                null,
                emptyList(),
                null
            )
        ).isNull()
    }

    @Test
    fun `does determineTierForIdentify return from allowableTiers if not null and not empty`() {
        val commonTier: Tier = MythicTier(name = "common", displayName = "Common")

        assertThat(
            IdentifyingUtil.determineTierForIdentify(
                MythicCreatureSpawningSettings(),
                tierManager,
                listOf(commonTier),
                null,
                emptyList(),
                null
            )
        ).isEqualTo(commonTier)
    }

    @Test
    fun `does determineTierForIdentify return from droppedBy if entity type has valid tiers`() {
        val commonTier: Tier = MythicTier(name = "common", displayName = "Common")
        val creatureSpawningSettings: CreatureSpawningSettings =
            MythicCreatureSpawningSettings(tierDrops = mapOf(EntityType.ZOMBIE to listOf("common")))
        tierManager.add(commonTier)

        assertThat(
            IdentifyingUtil.determineTierForIdentify(
                creatureSpawningSettings,
                tierManager,
                null,
                EntityType.ZOMBIE,
                emptyList(),
                null
            )
        ).isEqualTo(commonTier)
    }

    @Test
    fun `does determineTierForIdentify return from tiersFromMaterial if not empty`() {
        val commonTier: Tier = MythicTier(name = "common", displayName = "Common")
        val uncommonTier: Tier = MythicTier(name = "uncommon", displayName = "Uncommon")
        val creatureSpawningSettings: CreatureSpawningSettings =
            MythicCreatureSpawningSettings(tierDrops = mapOf(EntityType.ZOMBIE to listOf("common")))
        tierManager.add(commonTier)

        assertThat(
            IdentifyingUtil.determineTierForIdentify(
                creatureSpawningSettings,
                tierManager,
                null,
                EntityType.SPIDER,
                listOf(uncommonTier),
                null
            )
        ).isEqualTo(uncommonTier)
    }

    @Test
    fun `does determineTierForIdentify return from potentialTierFromLastLoreLine if not null`() {
        val commonTier: Tier = MythicTier(name = "common", displayName = "Common")
        val uncommonTier: Tier = MythicTier(name = "uncommon", displayName = "Uncommon")
        val creatureSpawningSettings: CreatureSpawningSettings =
            MythicCreatureSpawningSettings(tierDrops = mapOf(EntityType.ZOMBIE to listOf("common")))
        tierManager.add(commonTier)

        assertThat(
            IdentifyingUtil.determineTierForIdentify(
                creatureSpawningSettings,
                tierManager,
                null,
                EntityType.SPIDER,
                emptyList(),
                uncommonTier
            )
        ).isEqualTo(uncommonTier)
    }

    @Test
    fun `does getUnidentifiedItemDroppedBy find null for item without dropped by`() {
        val exampleLore =
            listOf("&7Find an &5Identity Tome &7to", "&7identify this item!", "")
        val unidentifiedItemOptions =
            MythicUnidentifiedItemOptions(droppedByPrefix = "&7Dropped by: &F", droppedBySuffix = "")
        val displayNames = emptyMap<String, String>()

        assertThat(
            IdentifyingUtil.getUnidentifiedItemDroppedBy(
                exampleLore,
                unidentifiedItemOptions,
                displayNames
            )
        ).isNull()
    }

    @Test
    fun `does getUnidentifiedItemDroppedBy find ZOMBIE for item dropped by default entity name`() {
        val exampleLore =
            listOf("&7Find an &5Identity Tome &7to", "&7identify this item!", "", "&7Dropped by: &FZombie")
        val unidentifiedItemOptions =
            MythicUnidentifiedItemOptions(droppedByPrefix = "&7Dropped by: &F", droppedBySuffix = "")
        val displayNames = emptyMap<String, String>()

        assertThat(
            IdentifyingUtil.getUnidentifiedItemDroppedBy(
                exampleLore,
                unidentifiedItemOptions,
                displayNames
            )
        ).isEqualTo(
            EntityType.ZOMBIE
        )
    }

    @Test
    fun `does getUnidentifiedItemDroppedBy find ZOMBIE for item dropped by ZoMbIe`() {
        val exampleLore =
            listOf("&7Find an &5Identity Tome &7to", "&7identify this item!", "", "&7Dropped by: &FZoMbIe")
        val unidentifiedItemOptions =
            MythicUnidentifiedItemOptions(droppedByPrefix = "&7Dropped by: &F", droppedBySuffix = "")
        val displayNames = mapOf("ZOMBIE" to "ZoMbIe")

        assertThat(
            IdentifyingUtil.getUnidentifiedItemDroppedBy(
                exampleLore,
                unidentifiedItemOptions,
                displayNames
            )
        ).isEqualTo(
            EntityType.ZOMBIE
        )
    }

    @Test
    fun `does getUnidentifiedItemDroppedBy find CAVE_SPIDER for item dropped by default entity name`() {
        val exampleLore =
            listOf("&7Find an &5Identity Tome &7to", "&7identify this item!", "", "&7Dropped by: &FCave Spider")
        val unidentifiedItemOptions =
            MythicUnidentifiedItemOptions(droppedByPrefix = "&7Dropped by: &F", droppedBySuffix = "")
        val displayNames = emptyMap<String, String>()

        assertThat(
            IdentifyingUtil.getUnidentifiedItemDroppedBy(
                exampleLore,
                unidentifiedItemOptions,
                displayNames
            )
        ).isEqualTo(
            EntityType.CAVE_SPIDER
        )
    }

    @Test
    fun `does getAllowableTiers find null for item with no allowable tiers`() {
        val exampleLore =
            listOf("&7Find an &5Identity Tome &7to", "&7identify this item!", "")
        val unidentifiedItemOptions =
            MythicUnidentifiedItemOptions(
                allowableTiersPrefix = "&7Allowable Tiers: (&F",
                allowableTiersSeparator = "&7, &F",
                allowableTiersSuffix = "&7)"
            )
        val commonTier: Tier = MythicTier(name = "common", displayName = "Common")
        tierManager.add(commonTier)

        assertThat(
            IdentifyingUtil.getAllowableTiers(
                exampleLore,
                unidentifiedItemOptions,
                tierManager
            )
        ).isNull()
    }

    @Test
    fun `does getAllowableTiers find nothing for item with allowable tiers of Nonexistent`() {
        val exampleLore =
            listOf("&7Find an &5Identity Tome &7to", "&7identify this item!", "", "&7Allowable Tiers: (&Nonexistent&7)")
        val unidentifiedItemOptions =
            MythicUnidentifiedItemOptions(
                allowableTiersPrefix = "&7Allowable Tiers: (&F",
                allowableTiersSeparator = "&7, &F",
                allowableTiersSuffix = "&7)"
            )
        val commonTier: Tier = MythicTier(name = "common", displayName = "Common")
        tierManager.add(commonTier)

        assertThat(
            IdentifyingUtil.getAllowableTiers(
                exampleLore,
                unidentifiedItemOptions,
                tierManager
            )
        ).isNotNull.isEmpty()
    }

    @Test
    fun `does getAllowableTiers find Common for item with allowable tiers of Common`() {
        val exampleLore =
            listOf("&7Find an &5Identity Tome &7to", "&7identify this item!", "", "&7Allowable Tiers: (&FCommon&7)")
        val unidentifiedItemOptions =
            MythicUnidentifiedItemOptions(
                allowableTiersPrefix = "&7Allowable Tiers: (&F",
                allowableTiersSeparator = "&7, &F",
                allowableTiersSuffix = "&7)"
            )
        val commonTier: Tier = MythicTier(name = "common", displayName = "Common")
        tierManager.add(commonTier)

        assertThat(IdentifyingUtil.getAllowableTiers(exampleLore, unidentifiedItemOptions, tierManager)).isEqualTo(
            listOf(commonTier)
        )
    }
}
