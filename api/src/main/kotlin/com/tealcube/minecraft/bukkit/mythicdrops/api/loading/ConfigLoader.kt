package com.tealcube.minecraft.bukkit.mythicdrops.api.loading

interface ConfigLoader {
    fun reloadStartupSettings()

    fun reloadSettings()

    fun reloadTiers()

    fun reloadCustomItems()

    fun saveCustomItems()

    fun reloadNames()

    fun reloadRepairCosts()

    fun reloadItemGroups()

    fun reloadSocketGemCombiners()

    fun saveSocketGemCombiners()

    fun reloadSocketGems()

    fun reloadRelations()
}
