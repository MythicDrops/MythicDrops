package com.tealcube.minecraft.bukkit.mythicdrops.utils

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem
import com.tealcube.minecraft.bukkit.mythicdrops.items.CustomItemMap
import org.bukkit.inventory.ItemStack

object CustomItemUtil {
    fun getCustomItemFromItemStack(itemStack: ItemStack): CustomItem? {
        return CustomItemMap.getInstance().values.find { it.toItemStack().isSimilar(itemStack) }
    }
}
