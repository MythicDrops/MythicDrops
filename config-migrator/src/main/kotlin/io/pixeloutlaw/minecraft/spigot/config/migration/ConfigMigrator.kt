package io.pixeloutlaw.minecraft.spigot.config.migration

import com.squareup.moshi.Moshi
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigrationStep
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigrationWithName
import org.reflections.Reflections
import java.util.regex.Pattern

class ConfigMigrator @JvmOverloads constructor(
    private val reflections: Reflections,
    private val moshi: Moshi = defaultMoshi
) {
    companion object {
        val defaultMoshi: Moshi = Moshi.Builder().add(ConfigMigrationStep.adapterFactory).build()
        private const val configMigrationPatternString = "V\\d+__.+\\.json"
        private val configMigrationPattern = Pattern.compile(configMigrationPatternString)
    }

    val configMigrationResources: Set<String> by lazy {
        reflections.getResources(configMigrationPattern)
    }
    val configMigrationReadResources: List<Pair<String, String>> by lazy {
        configMigrationResources.mapNotNull {
            try {
                it to javaClass.classLoader.getResource("$it").readText()
            } catch (ignored: Throwable) {
                null
            }
        }
    }
    val configMigrationsWithName: List<ConfigMigrationWithName> by lazy {
        configMigrationReadResources.mapNotNull {
            val configMigration = try {
                moshi.adapter(ConfigMigration::class.java).fromJson(it.second)
            } catch (ignored: Throwable) {
                null
            }
            if (configMigration != null) {
                ConfigMigrationWithName(it.first, configMigration)
            } else {
                null
            }
        }
    }
    val configMigrationsByFile: Map<String, List<ConfigMigrationWithName>> by lazy {
        val mutableMap = mutableMapOf<String, List<ConfigMigrationWithName>>()
        configMigrationsWithName.forEach {
            mutableMap[it.configMigration.fileName] =
                mutableMap.getOrDefault(it.configMigration.fileName, emptyList()).plus(it)
        }
        mutableMap.toMap()
    }
}
