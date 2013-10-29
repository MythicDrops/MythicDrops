package net.nunnerycode.bukkit.mythicdrops.api.managers;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public interface DropManager {

	ItemStack generateItemStackFromCustomItem(CustomItem customItem, ItemGenerationReason reason);

	ItemStack generateItemStack(ItemGenerationReason reason);

	ItemStack generateItemStackFromTier(Tier tier, ItemGenerationReason reason);

	ItemStack constructItemStackFromTierAndMaterialData(Tier tier, MaterialData materialData,
														ItemGenerationReason reason);

	ItemStack constructItemStackFromMaterialData(MaterialData matData, ItemGenerationReason reason);

	MythicDrops getPlugin();

}
