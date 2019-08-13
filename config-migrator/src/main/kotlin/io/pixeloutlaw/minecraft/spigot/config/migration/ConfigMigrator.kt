package io.pixeloutlaw.minecraft.spigot.config.migration

import com.github.zafarkhaja.semver.Version
import com.squareup.moshi.Moshi
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration
import io.pixeloutlaw.minecraft.spigot.config.migration.adapters.VersionAdapter
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigrationStep
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigrationWithName
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

open class ConfigMigrator @JvmOverloads constructor(
    private val dataFolder: File,
    private val configMigrationResources: Set<String>,
    private val moshi: Moshi = defaultMoshi
) {
    companion object {
        val defaultMoshi: Moshi = Moshi.Builder().add(VersionAdapter()).add(ConfigMigrationStep.adapterFactory).build()
        val logger: Logger by lazy { Logger.getLogger(ConfigMigrator::class.java.canonicalName) }
    }

    /**
     * Lazy cache of contents derived from [configMigrationResources].
     */
    val configMigrationContents: List<Pair<String, String>> by lazy {
        configMigrationResources.mapNotNull {
            try {
                it to javaClass.classLoader.getResource(it).readText()
            } catch (throwable: Throwable) {
                logger.log(Level.WARNING, "Unable to load migration resource: $it", throwable)
                null
            }
        }
    }

    /**
     * Lazy cache of parsed migrations derived from [configMigrationContents].
     */
    val configMigrationsWithName: List<ConfigMigrationWithName> by lazy {
        configMigrationContents.mapNotNull {
            val configMigration = try {
                moshi.adapter(ConfigMigration::class.java).fromJson(it.second)
            } catch (throwable: Throwable) {
                logger.log(Level.WARNING, "Unable to read resource JSON: ${it.first}", throwable)
                null
            }
            if (configMigration != null) {
                ConfigMigrationWithName(it.first, configMigration)
            } else {
                logger.log(Level.WARNING, "Resource was not a valid config migration: ${it.first}")
                null
            }
        }
    }

    /**
     * Lazy cache of [configMigrationsWithName] split by [ConfigMigrationWithName.name].
     */
    val configMigrationsByFile: Map<String, List<ConfigMigration>> by lazy {
        val mutableMap = mutableMapOf<String, List<ConfigMigration>>()
        configMigrationsWithName.forEach {
            mutableMap[it.configMigration.fileName] =
                mutableMap.getOrDefault(it.configMigration.fileName, emptyList()).plus(it.configMigration)
        }
        mutableMap.toMap()
    }

    /**
     * Lazy cache of the [String] contents of each of the configurations in the resources.
     *
     * Does not `mapNonNull` due to an empty config being valid.
     */
    val configContentsFromResources: Map<String, String> by lazy {
        configMigrationsByFile.mapValues {
            try {
                javaClass.classLoader.getResource(it.key).readText()
            } catch (throwable: Throwable) {
                logger.log(Level.WARNING, "Unable to load configuration resource: ${it.key}", throwable)
                ""
            }
        }
    }

    /**
     * Lazy cache of the [SmartYamlConfiguration] of each configuration in the resources.
     *
     * Attempts to load from the file first, then the resources.
     */
    val yamlConfigurationsByFile: Map<String, SmartYamlConfiguration> by lazy {
        configMigrationsByFile.filterKeys { fileName ->
            val configFile = dataFolder.toPath().resolve(fileName).toFile()
            configFile.exists() || configFile.parentFile.exists() || configFile.parentFile.mkdirs()
        }.mapValues {
            val configFile = dataFolder.toPath().resolve(it.key).toFile()
            val smartYamlConfiguration = SmartYamlConfiguration(configFile)
            // load it from resources if one doesn't exist
            if (!configFile.exists()) {
                smartYamlConfiguration.loadFromString(configContentsFromResources[it.key] ?: "")
            }
            smartYamlConfiguration
        }.toMap()
    }

    /**
     * Attempts to find the next applicable migration for a given config file name.
     *
     * @param config Name of config file to attempt to find migrations for
     *
     * @return next applicable migration if one is available, null otherwise
     */
    fun findNextApplicableMigration(config: String): ConfigMigration? {
        val yamlConfiguration = yamlConfigurationsByFile[config] ?: return null
        val versionString = yamlConfiguration.getString("version") ?: return null
        val version = try {
            Version.valueOf(versionString)
        } catch (ex: Exception) {
            return null
        }

        val configMigrationsForConfig = configMigrationsByFile[config] ?: return null
        val applicableMigrations = configMigrationsForConfig.filter { it.fromVersion == version }
        return applicableMigrations.maxBy { it.toVersion }
    }
}
