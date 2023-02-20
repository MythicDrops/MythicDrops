package com.tealcube.minecraft.bukkit.mythicdrops.api.items

import org.bukkit.inventory.ItemStack

/**
 * Migrates items from old formats using lore to new formats using persistent data.
 */
interface ItemMigrator {
    /**
     * Returns a copy of the item with applied Socket Gems attached as persistent data.
     */
    fun migrateAppliedSocketGems(itemStack: ItemStack): ItemStack

    /**
     * Returns a copy of the item with the Custom Item it represents attached as persistent data.
     */
    fun migrateCustomItem(itemStack: ItemStack): ItemStack

    /**
     * Returns a copy of the item with the Socket Gem it represents attached as persistent data.
     */
    fun migrateSocketGem(itemStack: ItemStack): ItemStack

    /**
     * Returns a copy of the item with the tier of the item attached as persistent data.
     */
    fun migrateTier(itemStack: ItemStack): ItemStack
}
