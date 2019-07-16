package com.tealcube.minecraft.bukkit.mythicdrops.worldguard

import com.sk89q.worldguard.protection.flags.StateFlag

object WorldGuardFlags {
    val flagMap = mapOf(
        mythicDrops to StateFlag(mythicDrops, true),
        mythicDropsTiered to StateFlag(mythicDropsTiered, true),
        mythicDropsCustom to StateFlag(mythicDropsCustom, true),
        mythicDropsSocketGem to StateFlag(mythicDropsSocketGem, true),
        mythicDropsIdentityTome to StateFlag(mythicDropsIdentityTome, true),
        mythicDropsUnidentifiedItem to StateFlag(mythicDropsUnidentifiedItem, true)
    )

    fun registerAllFlags() {
        flagMap.values.forEach { it.register() }
    }
}
