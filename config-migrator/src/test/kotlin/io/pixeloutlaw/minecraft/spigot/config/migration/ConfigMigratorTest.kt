package io.pixeloutlaw.minecraft.spigot.config.migration

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

class ConfigMigratorTest {
    private val reflections =
        Reflections(ConfigurationBuilder().addUrls(ClasspathHelper.forPackage("")).setScanners(ResourcesScanner()))

    @Test
    fun `does ConfigMigrator find the available migrations`() {
        val configMigrator = ConfigMigrator(reflections)

        assertThat(configMigrator.configMigrationResources).hasSize(1)
        assertThat(configMigrator.configMigrationReadResources).hasSize(1)
        assertThat(configMigrator.configMigrationsWithName).hasSize(1)
        assertThat(configMigrator.configMigrationsByFile).hasSize(1)
    }
}
