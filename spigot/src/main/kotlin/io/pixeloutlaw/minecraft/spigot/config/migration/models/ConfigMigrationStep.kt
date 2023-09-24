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
package io.pixeloutlaw.minecraft.spigot.config.migration.models

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.serialization.ConfigurationSerializable

/**
 * Represents a step that can be run as part of a migration.
 */
sealed class ConfigMigrationStep : ConfigurationSerializable {
    companion object {
        val defaultSteps =
            listOf(
                DeleteConfigMigrationStep::class,
                ForEachConfigMigrationStep::class,
                RenameConfigMigrationStep::class,
                RenameEachConfigMigrationStep::class,
                RenameEachGroupConfigMigrationStep::class,
                SetBooleanConfigMigrationStep::class,
                SetIntConfigMigrationStep::class,
                SetDoubleConfigMigrationStep::class,
                SetStringConfigMigrationStep::class,
                SetStringListConfigMigrationStep::class,
                SetStringIfEqualsConfigMigrationStep::class,
                SetStringListIfKeyEqualsStringConfigMigrationStep::class,
                ReplaceStringInStringConfigMigrationStep::class
            )
    }

    abstract fun migrate(configuration: ConfigurationSection)

    data class DeleteConfigMigrationStep(val path: String) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): DeleteConfigMigrationStep =
                DeleteConfigMigrationStep(map.getOrDefault("path", "").toString())
        }

        override fun migrate(configuration: ConfigurationSection) {
            configuration[path] = null
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("path" to path)
    }

    data class ForEachConfigMigrationStep(
        val matchRegex: String,
        val configMigrationSteps: List<ConfigMigrationStep> = emptyList()
    ) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): ForEachConfigMigrationStep {
                val matchRegex = map.getOrDefault("matchRegex", "").toString()
                val configMigrationStepsFromMap =
                    map.getOrDefault("configMigrationSteps", emptyList<ConfigMigrationStep>())
                val configMigrationSteps =
                    if (configMigrationStepsFromMap is List<*>) {
                        configMigrationStepsFromMap.filterIsInstance<ConfigMigrationStep>()
                    } else {
                        emptyList()
                    }
                return ForEachConfigMigrationStep(matchRegex, configMigrationSteps)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            val parentRegex = matchRegex.toRegex()
            val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
            for (keyThatMatchesParent in keysThatMatchParent) {
                if (!configuration.isConfigurationSection(keyThatMatchesParent)) {
                    continue
                }
                val matchesSection =
                    configuration.getConfigurationSection(keyThatMatchesParent) ?: configuration.createSection(
                        keyThatMatchesParent
                    )
                for (configMigrationStep in configMigrationSteps) {
                    configMigrationStep.migrate(matchesSection)
                }
            }
        }

        override fun serialize(): MutableMap<String, Any> =
            mutableMapOf("matchRegex" to matchRegex, "configMigrationSteps" to configMigrationSteps)
    }

    data class RenameEachConfigMigrationStep(val matchRegex: String, val to: String) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): RenameEachConfigMigrationStep {
                val matchRegex = map.getOrDefault("matchRegex", "").toString()
                val to = map.getOrDefault("to", "").toString()
                return RenameEachConfigMigrationStep(matchRegex, to)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            val parentRegex = matchRegex.toRegex()
            val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
            for (keyThatMatchesParent in keysThatMatchParent) {
                val oldValue = configuration[keyThatMatchesParent]
                configuration[keyThatMatchesParent] = null
                configuration[to.replace("%self%", keyThatMatchesParent)] = oldValue
            }
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("matchRegex" to matchRegex, "to" to to)
    }

    data class RenameEachGroupConfigMigrationStep(val matchRegex: String, val to: String) : ConfigMigrationStep() {
        companion object {
            const val MAXIMUM_NUMBER_OF_MATCHES = 5

            @JvmStatic
            fun deserialize(map: Map<String, Any>): RenameEachGroupConfigMigrationStep {
                val matchRegex = map.getOrDefault("matchRegex", "").toString()
                val to = map.getOrDefault("to", "").toString()
                return RenameEachGroupConfigMigrationStep(matchRegex, to)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            val parentRegex = matchRegex.toRegex()
            val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
            for (keyThatMatchesParent in keysThatMatchParent) {
                val match = parentRegex.matchEntire(keyThatMatchesParent)

                val newKey =
                    (1..MAXIMUM_NUMBER_OF_MATCHES).fold(to) { key, idx ->
                        key.replace("%match$idx%", match?.groupValues?.getOrNull(idx) ?: "")
                    }.replace("%self%", keyThatMatchesParent)
                val oldValue = configuration[keyThatMatchesParent]
                configuration[newKey] = oldValue
                configuration[keyThatMatchesParent] = null
            }
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("matchRegex" to matchRegex, "to" to to)
    }

    data class RenameConfigMigrationStep(val from: String, val to: String) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): RenameConfigMigrationStep {
                val from = map.getOrDefault("from", "").toString()
                val to = map.getOrDefault("to", "").toString()
                return RenameConfigMigrationStep(from, to)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            if (configuration[from] == null) {
                return
            }
            configuration[to] = configuration[from]
            configuration[from] = null
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("from" to from, "to" to to)
    }

    data class SetBooleanConfigMigrationStep(val key: String, val value: Boolean) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): SetBooleanConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val value = map.getOrDefault("value", false) as? Boolean ?: false
                return SetBooleanConfigMigrationStep(key, value)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("key" to key, "value" to value)
    }

    data class SetDoubleConfigMigrationStep(val key: String, val value: Double) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): SetDoubleConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val value = map.getOrDefault("value", 0.0) as? Double ?: 0.0
                return SetDoubleConfigMigrationStep(key, value)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("key" to key, "value" to value)
    }

    data class SetIntConfigMigrationStep(val key: String, val value: Int) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): SetIntConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val value = map.getOrDefault("value", 0) as? Int ?: 0
                return SetIntConfigMigrationStep(key, value)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("key" to key, "value" to value)
    }

    data class SetStringListConfigMigrationStep(val key: String, val value: List<String>) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): SetStringListConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val rawValue = map.getOrDefault("value", emptyList<String>())
                val value = if (rawValue is List<*>) rawValue.filterIsInstance<String>() else emptyList()
                return SetStringListConfigMigrationStep(key, value)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("key" to key, "value" to value)
    }

    data class SetStringListIfKeyEqualsStringConfigMigrationStep(
        val key: String,
        val value: List<String>,
        val ifKey: String,
        val ifValue: String
    ) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): SetStringListIfKeyEqualsStringConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val rawValue = map.getOrDefault("value", emptyList<String>())
                val value = if (rawValue is List<*>) rawValue.filterIsInstance<String>() else emptyList()
                val ifKey = map.getOrDefault("ifKey", "").toString()
                val ifValue = map.getOrDefault("ifValue", "").toString()
                return SetStringListIfKeyEqualsStringConfigMigrationStep(key, value, ifKey, ifValue)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            if (configuration.getString(ifKey) == ifValue) {
                configuration[key] = value
            }
        }

        override fun serialize(): MutableMap<String, Any> =
            mutableMapOf("key" to key, "value" to value, "ifKey" to ifKey, "ifValue" to ifValue)
    }

    data class SetStringConfigMigrationStep(val key: String, val value: String) : ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): SetStringConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val value = map.getOrDefault("value", "").toString()
                return SetStringConfigMigrationStep(key, value)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("key" to key, "value" to value)
    }

    data class SetStringIfEqualsConfigMigrationStep(val key: String, val value: String, val ifValue: String) :
        ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): SetStringIfEqualsConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val value = map.getOrDefault("value", "").toString()
                val ifValue = map.getOrDefault("ifValue", "").toString()
                return SetStringIfEqualsConfigMigrationStep(key, value, ifValue)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            if (configuration.getString(key) == ifValue) {
                configuration[key] = value
            }
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("key" to key, "value" to value, "ifValue" to value)
    }

    data class ReplaceStringInStringConfigMigrationStep(val key: String, val from: String, val to: String) :
        ConfigMigrationStep() {
        companion object {
            @JvmStatic
            fun deserialize(map: Map<String, Any>): ReplaceStringInStringConfigMigrationStep {
                val key = map.getOrDefault("key", "").toString()
                val from = map.getOrDefault("from", "").toString()
                val to = map.getOrDefault("to", "").toString()
                return ReplaceStringInStringConfigMigrationStep(key, from, to)
            }
        }

        override fun migrate(configuration: ConfigurationSection) {
            if (!configuration.isList(key)) {
                return
            }
            configuration[key] = configuration.getStringList(key).map { it.replace(from, to) }
        }

        override fun serialize(): MutableMap<String, Any> = mutableMapOf("key" to key, "from" to from, "to" to to)
    }
}
