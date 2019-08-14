package io.pixeloutlaw.minecraft.spigot.config.migration

import com.squareup.moshi.Moshi
import org.reflections.Reflections
import java.io.File
import java.util.regex.Pattern

class ReflectionsConfigMigrator @JvmOverloads constructor(
    dataFolder: File,
    reflections: Reflections,
    moshi: Moshi = defaultMoshi
) : ConfigMigrator(dataFolder, reflections.getResources(configMigrationPattern), moshi) {
    companion object {
        private const val configMigrationPatternString = "V\\d+__.+\\.json"
        private val configMigrationPattern = Pattern.compile(configMigrationPatternString)
    }
}
