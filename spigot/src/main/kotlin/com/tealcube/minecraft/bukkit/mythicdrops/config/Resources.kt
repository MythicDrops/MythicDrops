package com.tealcube.minecraft.bukkit.mythicdrops.config

import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import io.pixeloutlaw.minecraft.spigot.klob.Glob
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.Single
import java.util.Locale

@Single
internal class Resources(private val plugin: Plugin) {
    fun loadPrefixes(): Map<out String, List<String>> {
        val prefixes = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/prefixes/general.txt").iterate(dataFolderAsPath).forEach {
            prefixes[NameType.GENERAL_PREFIX.format] = it.toFile().readLines()
        }

        Glob.from("resources/prefixes/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.MATERIAL_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        Glob.from("resources/prefixes/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ITEMTYPE_PREFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            prefixes[key] = file.readLines()
        }

        return prefixes
    }

    fun loadSuffixes(): Map<out String, List<String>> {
        val suffixes = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/suffixes/general.txt").iterate(dataFolderAsPath).forEach {
            suffixes[NameType.GENERAL_SUFFIX.format] = it.toFile().readLines()
        }

        Glob.from("resources/suffixes/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.MATERIAL_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        Glob.from("resources/suffixes/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ITEMTYPE_SUFFIX.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            suffixes[key] = file.readLines()
        }

        return suffixes
    }

    fun loadLore(): Map<out String, List<String>> {
        val lore = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/lore/general.txt").iterate(dataFolderAsPath).forEach {
            lore[NameType.GENERAL_LORE.format] = it.toFile().readLines()
        }

        Glob.from("resources/lore/tiers/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.TIER_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/materials/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.MATERIAL_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/enchantments/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.ENCHANTMENT_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        Glob.from("resources/lore/itemtypes/*.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key = "${NameType.ITEMTYPE_LORE.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            lore[key] = file.readLines()
        }

        return lore
    }

    fun loadMobNames(): Map<out String, List<String>> {
        val mobNames = mutableMapOf<String, List<String>>()
        val dataFolderAsPath = plugin.dataFolder.toPath()

        Glob.from("resources/mobnames/general.txt").iterate(dataFolderAsPath).forEach {
            mobNames[NameType.GENERAL_MOB_NAME.format] = it.toFile().readLines()
        }

        Glob.from("resources/mobnames/*.txt", "!resources/mobnames/general.txt").iterate(dataFolderAsPath).forEach {
            val file = it.toFile()
            val key =
                "${NameType.SPECIFIC_MOB_NAME.format}${file.name.replace(".txt", "").lowercase(Locale.getDefault())}"
            mobNames[key] = file.readLines()
        }

        return mobNames
    }
}
