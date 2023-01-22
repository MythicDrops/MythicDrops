package com.tealcube.minecraft.bukkit.mythicdrops.tiers

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.tealcube.minecraft.bukkit.mythicdrops.api.errors.LoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.errors.MythicLoadingErrorManager
import com.tealcube.minecraft.bukkit.mythicdrops.items.MythicItemGroupManager
import io.pixeloutlaw.minecraft.spigot.loadFromResource
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MythicTierTest {
    private lateinit var itemGroupManager: ItemGroupManager
    private lateinit var loadingErrorManager: LoadingErrorManager

    @BeforeEach
    fun setup() {
        itemGroupManager = MythicItemGroupManager()
        loadingErrorManager = MythicLoadingErrorManager()
    }

    @Test
    fun `does fromConfigurationSection load custom model data`() {
        // given
        val tierYaml = YamlConfiguration().loadFromResource("config_loading/tier.yml")

        // when
        val tier = MythicTier.fromConfigurationSection(tierYaml, "tier", itemGroupManager, loadingErrorManager)

        // then
        assertThat(tier).isNotNull().all {
            transform { it.name }.isEqualTo("tier")
            transform { it.customModelData }.hasSize(3)
        }
    }
}
