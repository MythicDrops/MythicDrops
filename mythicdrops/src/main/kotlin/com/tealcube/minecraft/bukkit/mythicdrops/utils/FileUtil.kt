package com.tealcube.minecraft.bukkit.mythicdrops.utils

import java.io.File

object FileUtil {
    fun renameFile(file: File, newName: String) {
        file.renameTo(File(file.parentFile, newName))
    }
}