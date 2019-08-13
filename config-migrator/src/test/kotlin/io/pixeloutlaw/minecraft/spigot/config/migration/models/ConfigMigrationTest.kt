package io.pixeloutlaw.minecraft.spigot.config.migration.models

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import org.junit.Assert
import org.junit.Test

class ConfigMigrationTest {
    private val moshi = Moshi.Builder().add(ConfigMigrationStep.adapterFactory).build()

    @Test
    fun `can migration with no steps be parsed correctly`() {
        val jsonString = javaClass.getResource("/config_migration_no_steps.json").readText()
        val configMigration = moshi.adapter(ConfigMigration::class.java).fromJson(jsonString)
        if (configMigration == null) {
            Assert.fail("configMigration == null")
            return
        }
        assertThat(configMigration.fileName).isEqualTo("config.yml")
        assertThat(configMigration.fromVersion).isEqualTo("1.2.3")
        assertThat(configMigration.toVersion).isEqualTo("4.5.6")
        assertThat(configMigration.configMigrationSteps).isEmpty()
        ConfigMigration(
            "config.yml",
            "6.0.0",
            "7.0.0",
            listOf(
                RenameConfigMigrationStep("components.socketting-enabled", "components.socketing-enabled"),
                DeleteConfigMigrationStep("options.allow-equipping-items-via-right-click")
            )
        )
    }

    @Test
    fun `can migration with multiple steps be parsed correctly`() {
        val jsonString = javaClass.getResource("/config_migration_multiple_steps.json").readText()
        val configMigration = moshi.adapter(ConfigMigration::class.java).fromJson(jsonString)
        if (configMigration == null) {
            Assert.fail("configMigration == null")
            return
        }
        assertThat(configMigration.fileName).isEqualTo("config.yml")
        assertThat(configMigration.fromVersion).isEqualTo("1.2.3")
        assertThat(configMigration.toVersion).isEqualTo("4.5.6")
        assertThat(configMigration.configMigrationSteps).isNotEmpty()
        assertThat(configMigration.configMigrationSteps).hasSize(2)
        assertThat(configMigration.configMigrationSteps.any { it is RenameConfigMigrationStep }).isTrue()
        assertThat(configMigration.configMigrationSteps.any { it is DeleteConfigMigrationStep }).isTrue()
    }
}
