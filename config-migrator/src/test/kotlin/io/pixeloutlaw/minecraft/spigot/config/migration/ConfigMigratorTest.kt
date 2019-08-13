package io.pixeloutlaw.minecraft.spigot.config.migration

import com.github.zafarkhaja.semver.Version
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ConfigMigratorTest {
    @Test
    fun `does not find when no resources given`(@TempDir tempDir: File) {
        val configMigrator = ConfigMigrator(tempDir, emptySet())

        assertThat(configMigrator.configMigrationContents).hasSize(0)
    }

    @Test
    fun `does find the available migrations`(@TempDir tempDir: File) {
        val configMigrator = ConfigMigrator(tempDir, setOf("config/migration/V1__Config_YAML_for_6.0.0.json"))

        assertThat(configMigrator.configMigrationContents).hasSize(1)
        assertThat(configMigrator.configMigrationsWithName).hasSize(1)
        assertThat(configMigrator.configMigrationsByFile).hasSize(1)
    }

    @Test
    fun `does cache non-empty string for existent YAML resources`(@TempDir tempDir: File) {
        val configMigrator = ConfigMigrator(tempDir, setOf("migrate_existent_resource.json"))
        assertThat(configMigrator.configMigrationContents).hasSize(1)
        assertThat(configMigrator.configMigrationsWithName).hasSize(1)
        assertThat(configMigrator.configContentsFromResources["config_existent.yml"]).isNotEmpty()
    }

    @Test
    fun `does cache empty string for non-existent YAML resources`(@TempDir tempDir: File) {
        val configMigrator = ConfigMigrator(tempDir, setOf("migrate_nonexistent_resource.json"))
        assertThat(configMigrator.configMigrationContents).hasSize(1)
        assertThat(configMigrator.configMigrationsWithName).hasSize(1)
        assertThat(configMigrator.configContentsFromResources["config_nonexistent.yml"]).isEmpty()
    }

    @Test
    fun `does cache empty SmartYamlConfiguration for non-existent file and non-existent resource`(@TempDir tempDir: File) {
        val configMigrator = ConfigMigrator(tempDir, setOf("migrate_nonexistent_resource.json"))
        assertThat(configMigrator.configMigrationContents).hasSize(1)
        assertThat(configMigrator.configMigrationsWithName).hasSize(1)
        val configNonexistentYml = configMigrator.yamlConfigurationsByFile["config_nonexistent.yml"]
        assertThat(configNonexistentYml).isNotNull()
        if (configNonexistentYml == null) {
            return Assertions.fail("configNonexistentYml is somehow null")
        }
        assertThat(configNonexistentYml.getKeys(true)).isEmpty()
    }

    @Test
    fun `does cache non-empty SmartYamlConfiguration for non-empty file`(@TempDir tempDir: File) {
        val nonexistentResourceFile = """version: 1.0.0"""
        tempDir.toPath().resolve("config_nonexistent.yml").toFile().writeText(nonexistentResourceFile)
        val configMigrator = ConfigMigrator(tempDir, setOf("migrate_nonexistent_resource.json"))
        assertThat(configMigrator.configMigrationContents).hasSize(1)
        assertThat(configMigrator.configMigrationsWithName).hasSize(1)
        val configNonexistentYml = configMigrator.yamlConfigurationsByFile["config_nonexistent.yml"]
        assertThat(configNonexistentYml).isNotNull()
        if (configNonexistentYml == null) {
            return Assertions.fail("configNonexistentYml is somehow null")
        }
        assertThat(configNonexistentYml.getKeys(true)).isNotEmpty()
        assertThat(configNonexistentYml.getString("version")).isNotNull()
        assertThat(configNonexistentYml.getString("version")).isEqualTo("1.0.0")
    }

    @Test
    fun `does cache non-empty SmartYamlConfiguration for non-empty resource`(@TempDir tempDir: File) {
        val nonexistentResourceFile = """version: 1.0.0"""
        tempDir.toPath().resolve("config_nonexistent.yml").toFile().writeText(nonexistentResourceFile)
        val configMigrator = ConfigMigrator(tempDir, setOf("migrate_existent_resource.json"))
        assertThat(configMigrator.configMigrationContents).hasSize(1)
        assertThat(configMigrator.configMigrationsWithName).hasSize(1)
        val configExistentYml = configMigrator.yamlConfigurationsByFile["config_existent.yml"]
        assertThat(configExistentYml).isNotNull()
        if (configExistentYml == null) {
            return Assertions.fail("configExistentYml is somehow null")
        }
        assertThat(configExistentYml.getKeys(true)).isNotEmpty()
        assertThat(configExistentYml.getString("version")).isNotNull()
        assertThat(configExistentYml.getString("version")).isEqualTo("1.0.0")
    }

    @Test
    fun `does findNextApplicableMigration return null for no resources`(@TempDir tempDir: File) {
        val configMigrator = ConfigMigrator(tempDir, emptySet())
        assertThat(configMigrator.findNextApplicableMigration("test.yml")).isNull()
    }

    @Test
    fun `does findNextApplicableMigration return null when file exists and does not match version`(@TempDir tempDir: File) {
        val nonexistentResourceFile = """version: 2.0.0"""
        tempDir.toPath().resolve("test.yml").toFile().writeText(nonexistentResourceFile)
        val configMigrator = ConfigMigrator(tempDir, setOf("1_0_0_to_1_0_1.json"))
        assertThat(configMigrator.findNextApplicableMigration("test.yml")).isNull()
    }

    @Test
    fun `does findNextApplicableMigration return non-null when file exists and matches version`(@TempDir tempDir: File) {
        val nonexistentResourceFile = """version: 1.0.0"""
        tempDir.toPath().resolve("test.yml").toFile().writeText(nonexistentResourceFile)
        val configMigrator = ConfigMigrator(tempDir, setOf("1_0_0_to_1_0_1.json"))
        val nextApplicableMigration = configMigrator.findNextApplicableMigration("test.yml")
        assertThat(nextApplicableMigration).isNotNull()
        if (nextApplicableMigration == null) {
            return Assertions.fail("nextApplicableMigration is somehow null")
        }
        assertThat(nextApplicableMigration.toVersion).isEqualTo(Version.valueOf("1.0.1"))
    }

    @Test
    fun `does findNextApplicableMigration return non-null and highest toVersion when file exists and matches version`(@TempDir tempDir: File) {
        val nonexistentResourceFile = """version: 1.0.0"""
        tempDir.toPath().resolve("test.yml").toFile().writeText(nonexistentResourceFile)
        val configMigrator = ConfigMigrator(tempDir, setOf("1_0_0_to_1_0_1.json", "1_0_0_to_1_1_0.json"))
        val nextApplicableMigration = configMigrator.findNextApplicableMigration("test.yml")
        assertThat(nextApplicableMigration).isNotNull()
        if (nextApplicableMigration == null) {
            return Assertions.fail("nextApplicableMigration is somehow null")
        }
        assertThat(nextApplicableMigration.toVersion).isEqualTo(Version.valueOf("1.1.0"))
    }
}
