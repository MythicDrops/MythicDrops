package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import com.github.shyiko.klob.Glob
import com.squareup.moshi.Moshi
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.adapters.VersionAdapter
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigration
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigrationStep
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.NamedConfigMigration
import java.io.File
import java.nio.file.Path
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Migrates Spigot YAML configurations across versions.
 *
 * @property dataFolder data folder for a plugin
 * @property baseConfigMigrationResources any default config migration resources to include
 * @property moshi Moshi instance to use to parse migration instructions
 */
open class ConfigMigrator @JvmOverloads constructor(
    private val dataFolder: File,
    private val baseConfigMigrationResources: List<String> = emptyList(),
    private val moshi: Moshi = defaultMoshi
) {
    companion object {
        val defaultMoshi: Moshi = Moshi.Builder().add(VersionAdapter).add(ConfigMigrationStep.adapterFactory).build()
        val logger: Logger by lazy { Logger.getLogger(ConfigMigrator::class.java.canonicalName) }
    }

    /**
     * Lazy cache of contents derived from [getConfigMigrationResources].
     */
    val configMigrationContents: List<Pair<String, String>> by lazy {
        getConfigMigrationResources().mapNotNull {
            try {
                it to javaClass.classLoader.getResource(it).readText()
            } catch (throwable: Throwable) {
                logger.log(Level.WARNING, "Unable to load migration resource: $it", throwable)
                null
            }
        }
    }

    /**
     * Lazy cache of parsed migrations derived from [configMigrationContents]. Sorted by name.
     */
    val namedConfigMigrations: List<NamedConfigMigration> by lazy {
        configMigrationContents.mapNotNull {
            val configMigration = try {
                moshi.adapter(ConfigMigration::class.java).fromJson(it.second)
            } catch (throwable: Throwable) {
                logger.log(Level.WARNING, "Unable to read resource JSON: ${it.first}", throwable)
                null
            }
            if (configMigration != null) {
                NamedConfigMigration(
                    it.first,
                    configMigration
                )
            } else {
                logger.log(Level.WARNING, "Resource was not a valid config migration: ${it.first}")
                null
            }
        }.sortedBy { it.migrationName }
    }

    /**
     * Gets the config migration resources to load.
     */
    open fun getConfigMigrationResources(): List<String> = baseConfigMigrationResources

    /**
     * Attempts to run all of the migrations that it is aware of. Will not copy configurations from resources
     * into [dataFolder]. Iterates through [ConfigMigration] sorted by name from [namedConfigMigrations] in order,
     * finding any files that match [ConfigMigration.fileGlobs] with a matching [ConfigMigration.fromVersion], and
     * running applicable [ConfigMigrationStep]s.
     */
    fun migrate() {
        logger.info("Beginning migration process")
        for (namedConfigMigration in namedConfigMigrations) {
            runMigration(namedConfigMigration)
        }
        logger.info("Finished migration process!")
    }

    fun writeYamlFromResourcesIfNotExists(resource: String) {
        val targetFile = dataFolder.toPath().resolve(resource).toFile()
        if (targetFile.exists() || !targetFile.parentFile.exists() && !targetFile.parentFile.mkdirs()) {
            return
        }
        try {
            val text = javaClass.classLoader.getResource(resource).readText()
            SmarterYamlConfiguration(targetFile).apply { loadFromString(text); save() }
        } catch (throwable: Throwable) {
            logger.log(Level.WARNING, "Unable to write resource: $resource", throwable)
            return
        }
    }

    // suppress spread operator warning because Glob has no other interface we can use
    @Suppress("SpreadOperator")
    private fun runMigration(namedConfigMigration: NamedConfigMigration) {
        logger.fine("> Running migration: ${namedConfigMigration.migrationName}")
        val configMigration = namedConfigMigration.configMigration
        logger.fine("=> Looking for files in dataFolder matching ${configMigration.fileGlobs}")
        val matchingPaths =
            Glob.from(*configMigration.fileGlobs.toTypedArray()).iterate(dataFolder.toPath()).asSequence().toList()
        logger.fine("=> Found matching files: ${matchingPaths.joinToString(", ")}")
        logger.fine("=> Loading matched files to check their versions")
        val yamlConfigurations = matchingPaths.map {
            val pathToFile = it.toFile()
            VersionedSmarterYamlConfiguration(
                pathToFile
            )
        }.filter {
            logger.finest(
                """
                    ==> Checking if ${it.fileName} (${it.version}) has a version matching ${configMigration.fromVersion}
                """.trimLog()
            )
            it.version == configMigration.fromVersion
        }
        logger.fine("=> Found configurations matching versions: ${yamlConfigurations.map { it.fileName }}")
        logger.fine("=> Running migration over matching configurations")
        runMigrationOverConfigurations(yamlConfigurations, configMigration)
        logger.fine("=> Finished running migration over matching configurations!")
        logger.fine("> Finished running migration!")
    }

    private fun runMigrationOverConfigurations(
        yamlConfigurations: List<VersionedSmarterYamlConfiguration>,
        configMigration: ConfigMigration
    ) {
        for (yamlConfiguration in yamlConfigurations) {
            handleBackups(configMigration, yamlConfiguration)
            if (handleOverwrites(configMigration, yamlConfiguration, dataFolder.toPath())) {
                logger.finest(
                    """
                    |=> Canceling migration for ${yamlConfiguration.fileName} as it has been overwritten!
                    """.trimLog()
                )
                continue
            }
            logger.finest("==> Running migration steps over ${yamlConfiguration.fileName}")
            for (step in configMigration.configMigrationSteps) {
                step.migrate(yamlConfiguration)
            }
            logger.finest(
                """
                |==> Setting configuration version to target version:
                |configuration=${yamlConfiguration.fileName} version=${configMigration.toVersion}
                """.trimLog()
            )
            yamlConfiguration.version = configMigration.toVersion
            logger.finest("==> Saving configuration")
            yamlConfiguration.save()
            logger.finest("==> Finished running migration steps!")
        }
    }

    private fun handleBackups(
        configMigration: ConfigMigration,
        yamlConfiguration: VersionedSmarterYamlConfiguration
    ) {
        if (configMigration.createBackup) {
            val lastDot = yamlConfiguration.fileName.lastIndexOf(".")
            val backupFilename = yamlConfiguration.fileName.substring(
                0,
                lastDot
            ) + "_${yamlConfiguration.version.toString().replace(
                ".",
                "_"
            )}" + yamlConfiguration.fileName.substring(lastDot) + ".backup"
            logger.fine("==> Creating backup of file as $backupFilename")
            try {
                yamlConfiguration.file?.let {
                    it.copyTo(File(it.parentFile, backupFilename), overwrite = true)
                }
            } catch (ex: Exception) {
                logger.log(Level.SEVERE, "Unable to create a backup of a config!", ex)
            }
        }
    }

    // if this returns true, we should skip the migration as we're just overwriting the existing file
    private fun handleOverwrites(
        configMigration: ConfigMigration,
        yamlConfiguration: VersionedSmarterYamlConfiguration,
        pathToDataFolder: Path
    ): Boolean {
        if (!configMigration.overwrite) {
            return false
        }
        val pathToYamlFile = yamlConfiguration.file?.toPath()?.toAbsolutePath()?.normalize() ?: return false
        val normalizedPathToDataFolder = pathToDataFolder.toAbsolutePath().normalize()
        val relativizedPathToYamlFile = normalizedPathToDataFolder.relativize(pathToYamlFile)
        val pathToResource = relativizedPathToYamlFile.toString().replace("\\", "/")
        try {
            val resourceContents = javaClass.classLoader.getResource(pathToResource).readText()
            yamlConfiguration.file?.writeText(resourceContents)
            logger.fine(
                """
                |==> Overwrote contents of ${yamlConfiguration.fileName} with
                |contents of (hopefully) same file from plugin!
                """.trimLog()
            )
        } catch (ex: Exception) {
            logger.log(Level.SEVERE, "Unable to overwrite a config!", ex)
        }
        return true
    }
}

private fun String.trimLog(): String {
    return this.trimMargin().replace("\n", " ")
}
