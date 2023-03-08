package com.tealcube.minecraft.bukkit.mythicdrops.items

import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItemManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemMigrator
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getCustomItem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.hasPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.hasPersistentDataStringList
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsAppliedSocketGems
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsCustomItem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsSocketGem
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataStringList
import org.bukkit.inventory.ItemStack

internal class MythicItemMigrator(
    private val customItemManager: CustomItemManager,
    private val tierManager: TierManager
) : ItemMigrator {
    override fun migrateAppliedSocketGems(itemStack: ItemStack): ItemStack {
        val cloned = itemStack.clone()
        if (cloned.hasPersistentDataStringList(mythicDropsAppliedSocketGems)) {
            return cloned
        }
        val socketGems = GemUtil.getSocketGemsFromItemStackLore(itemStack)
        cloned.setPersistentDataStringList(mythicDropsAppliedSocketGems, socketGems.map { it.name })
        return cloned
    }

    override fun migrateCustomItem(itemStack: ItemStack): ItemStack {
        val cloned = itemStack.clone()
        if (cloned.hasPersistentDataString(mythicDropsCustomItem)) {
            return cloned
        }
        val customItem = itemStack.getCustomItem(customItemManager, false)
        cloned.setPersistentDataString(mythicDropsCustomItem, customItem?.name ?: "")
        return cloned
    }

    override fun migrateSocketGem(itemStack: ItemStack): ItemStack {
        val cloned = itemStack.clone()
        if (cloned.hasPersistentDataString(mythicDropsSocketGem)) {
            return cloned
        }
        val socketGem = GemUtil.getSocketGemFromPotentialSocketItem(itemStack)
        cloned.setPersistentDataString(mythicDropsSocketGem, socketGem?.name ?: "")
        return cloned
    }

    override fun migrateTier(itemStack: ItemStack): ItemStack {
        val cloned = itemStack.clone()
        if (cloned.hasPersistentDataString(mythicDropsTier)) {
            return cloned
        }
        val tier = itemStack.getTier(tierManager, false)
        cloned.setPersistentDataString(mythicDropsTier, tier?.name ?: "")
        return cloned
    }
}
