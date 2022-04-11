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
package io.pixeloutlaw.minecraft.spigot.config.migration

import com.github.shyiko.klob.Glob
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import io.pixeloutlaw.kindling.Log
import io.pixeloutlaw.minecraft.spigot.config.FileAwareYamlConfiguration
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.ConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.NamedConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.post.NamedPostConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.pre.NamedPreConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.models.pre.PreConfigMigration
import io.pixeloutlaw.minecraft.spigot.config.migration.steps.post.PostConfigMigrationStep
import io.pixeloutlaw.minecraft.spigot.config.migration.steps.pre.PreConfigMigrationStep
import java.io.File
import java.nio.file.Path

/**
 * Migrates Spigot YAML configurations across versions.
 *
 * @property dataFolder data folder for a plugin
 * @property backupOnMigrate should a backup file be created when migrating?
 */
abstract class ConfigMigrator @JvmOverloads constructor(
    private val dataFolder: File,
    private val backupOnMigrate: Boolean = true
) {
    /**
     * Migrations that this instance will run through in the given order.
     */
    abstract val namedPreConfigMigrations: List<NamedPreConfigMigration>

    /**
     * Migrations that this instance will run through in the given order, intended to be invoked after
     * configurations are loaded.
     */
    abstract val namedPostConfigMigrations: List<NamedPostConfigMigration>

    /**
     * Attempts to run all the migrations that it is aware of. Will not copy configurations from resources
     * into [dataFolder]. Iterates through [PreConfigMigration] sorted by name from [namedPreConfigMigrations] in order,
     * finding any files that match [PreConfigMigration.fileGlobs] with a matching [PreConfigMigration.fromVersion], and
     * running applicable [io.pixeloutlaw.minecraft.spigot.config.migration.steps.PreConfigMigrationStep]s.
     */
    fun preMigrate() {
        Log.info("Beginning migration process")
        for (namedConfigMigration in namedPreConfigMigrations) {
            runMigration(namedConfigMigration)
        }
        Log.info("Finished migration process!")
    }

    /**
     * Attempts to run all the migrations that it is aware of. Will not copy configurations from resources
     * into [dataFolder]. Iterates through [PreConfigMigration] sorted by name from [namedPreConfigMigrations] in order,
     * finding any files that match [PreConfigMigration.fileGlobs] with a matching [PreConfigMigration.fromVersion], and
     * running applicable [io.pixeloutlaw.minecraft.spigot.config.migration.steps.PreConfigMigrationStep]s.
     */
    fun postMigrate() {
        Log.info("Beginning post-migration process")
        for (namedPostConfigMigration in namedPostConfigMigrations) {
            runMigration(namedPostConfigMigration)
        }
        Log.info("Finished post-migration process!")
    }

    fun writeYamlFromResourcesIfNotExists(resource: String) {
        val targetFile = dataFolder.toPath().resolve(resource).toFile()
        if (targetFile.exists() || !targetFile.parentFile.exists() && !targetFile.parentFile.mkdirs()) {
            return
        }
        try {
            val text = javaClass.classLoader?.getResource(resource)?.readText()
            if (text != null) {
                FileAwareYamlConfiguration(targetFile).apply {
                    loadFromString(text)
                    save()
                }
            } else {
                Log.warn("Unable to write resource because text was unreadable: $resource")
            }
        } catch (expected: Throwable) {
            Log.warn("Unable to write resource: $resource", expected)
            return
        }
    }

    // suppress spread operator warning because Glob has no other interface we can use
    @Suppress("SpreadOperator")
    private fun runMigration(namedConfigMigration: NamedConfigMigration) {
        Log.debug("> Running migration: ${namedConfigMigration.migrationName}")
        val configMigration = namedConfigMigration.configMigration
        Log.debug("=> Looking for files in dataFolder matching ${configMigration.fileGlobs}")
        val matchingPaths =
            Glob.from(*configMigration.fileGlobs.toTypedArray()).iterate(dataFolder.toPath()).asSequence().toList()
        Log.debug("=> Found matching files: ${matchingPaths.joinToString(", ")}")
        Log.debug("=> Loading matched files to check their versions")
        val yamlConfigurations = matchingPaths.map {
            val pathToFile = it.toFile()
            VersionedFileAwareYamlConfiguration(
                pathToFile
            )
        }.filter {
            Log.verbose(
                """
                    ==> Checking if ${it.fileName} (${it.version}) has a version matching ${configMigration.fromVersion}
                """.trimLog()
            )
            it.version == configMigration.fromVersion
        }
        Log.debug("=> Found configurations matching versions: ${yamlConfigurations.map { it.fileName }}")
        Log.debug("=> Running migration over matching configurations")
        runMigrationOverConfigurations(yamlConfigurations, configMigration)
        Log.debug("=> Finished running migration over matching configurations!")
        Log.debug("> Finished running migration!")
    }

    private fun runMigrationOverConfigurations(
        yamlConfigurations: List<VersionedFileAwareYamlConfiguration>,
        configMigration: ConfigMigration
    ) {
        for (yamlConfiguration in yamlConfigurations) {
            handleBackups(configMigration, yamlConfiguration)
            if (handleOverwrites(configMigration, yamlConfiguration, dataFolder.toPath())) {
                Log.verbose(
                    """
                    |=> Canceling migration for ${yamlConfiguration.fileName} as it has been overwritten!
                    """.trimLog()
                )
                continue
            }
            Log.verbose("==> Running migration steps over ${yamlConfiguration.fileName}")
            for (step in configMigration.configMigrationSteps) {
                if (step is PreConfigMigrationStep) {
                    step.migrate(yamlConfiguration)
                } else if (step is PostConfigMigrationStep) {
                    step.migrate(yamlConfiguration, MythicDropsApi.mythicDrops)
                }
            }
            Log.verbose(
                """
                |==> Setting configuration version to target version:
                |configuration=${yamlConfiguration.fileName} version=${configMigration.toVersion}
                """.trimLog()
            )
            yamlConfiguration.version = configMigration.toVersion
            Log.verbose("==> Saving configuration")
            yamlConfiguration.save()
            Log.verbose("==> Finished running migration steps!")
        }
    }

    private fun handleBackups(
        configMigration: ConfigMigration,
        yamlConfiguration: VersionedFileAwareYamlConfiguration
    ) {
        if (configMigration.createBackup && backupOnMigrate) {
            val lastDot = yamlConfiguration.fileName.lastIndexOf(".")
            val nameBeforeLastDot = yamlConfiguration.fileName.substring(
                0,
                lastDot
            )
            val version = yamlConfiguration.version.toString().replace(
                ".",
                "_"
            )
            val previousName = yamlConfiguration.fileName.substring(lastDot)
            val backupFilename =
                "${nameBeforeLastDot}_$version$previousName.backup"
            Log.debug("==> Creating backup of file as $backupFilename")
            try {
                yamlConfiguration.file?.let {
                    it.copyTo(File(it.parentFile, backupFilename), overwrite = true)
                }
            } catch (expected: Exception) {
                Log.error("Unable to create a backup of a config!", expected)
            }
        }
    }

    // if this returns true, we should skip the migration as we're just overwriting the existing file
    private fun handleOverwrites(
        configMigration: ConfigMigration,
        yamlConfiguration: VersionedFileAwareYamlConfiguration,
        pathToDataFolder: Path
    ): Boolean {
        val pathToYamlFile = yamlConfiguration.file?.toPath()?.toAbsolutePath()?.normalize()
        if (!configMigration.overwrite || pathToYamlFile == null) {
            return false
        }
        val normalizedPathToDataFolder = pathToDataFolder.toAbsolutePath().normalize()
        val relativizedPathToYamlFile = normalizedPathToDataFolder.relativize(pathToYamlFile)
        val pathToResource = relativizedPathToYamlFile.toString().replace("\\", "/")
        try {
            val resourceContents = javaClass.classLoader?.getResource(pathToResource)?.readText() ?: ""
            yamlConfiguration.file?.writeText(resourceContents)
            Log.debug(
                """
                |==> Overwrote contents of ${yamlConfiguration.fileName} with
                |contents of (hopefully) same file from plugin!
                """.trimLog()
            )
        } catch (expected: Exception) {
            Log.error("Unable to overwrite a config!", expected)
        }
        return true
    }

    /**
     * Trims the margins from the string and replaces newlines with a space.
     */
    private fun String.trimLog(): String {
        return this.trimMargin().replace("\n", " ")
    }
}
