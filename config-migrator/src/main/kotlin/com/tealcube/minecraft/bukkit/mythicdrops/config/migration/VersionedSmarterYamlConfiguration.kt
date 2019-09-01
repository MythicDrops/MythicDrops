package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import java.io.File

class VersionedSmarterYamlConfiguration @JvmOverloads constructor(file: File? = null) :
    SmarterYamlConfiguration(file), VersionedConfiguration
