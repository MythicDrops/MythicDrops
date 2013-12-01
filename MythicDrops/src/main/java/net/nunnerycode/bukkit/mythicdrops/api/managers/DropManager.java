package net.nunnerycode.bukkit.mythicdrops.api.managers;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

/**
 * Used to handle all creation of items to be given.
 */
public interface DropManager {

	/**
	 * Generates an {@link MythicItemStack} from a {@link CustomItem} with an {@link ItemGenerationReason} for
	 * creating the item.
	 *
	 * @param customItem CustomItem to use
	 * @param reason     reason for item generation
	 * @return resulting ItemStack
	 */
	MythicItemStack generateItemStackFromCustomItem(CustomItem customItem, ItemGenerationReason reason);

	/**
	 * Generates a random {@link MythicItemStack} with an {@link ItemGenerationReason} for creating the item.
	 *
	 * @param reason reason for item generation
	 * @return resulting ItemStack
	 */
	MythicItemStack generateItemStack(ItemGenerationReason reason);

	/**
	 * Generates a random {@link MythicItemStack} from a {@link Tier} with an {@link ItemGenerationReason} for
	 * creating the item.
	 *
	 * @param tier   Tier of the item
	 * @param reason reason for item generation
	 * @return resulting ItemStack
	 */
	MythicItemStack generateItemStackFromTier(Tier tier, ItemGenerationReason reason);

	/**
	 * Generates a random {@link MythicItemStack} from a {@link Tier} and {@link MaterialData} with an {@link
	 * ItemGenerationReason} for creating the item.
	 *
	 * @param tier         Tier of the item
	 * @param materialData MaterialData of the item
	 * @param reason       reason for item generation
	 * @return resulting ItemStack
	 */
	MythicItemStack generateItemStackFromTierAndMaterialData(Tier tier, MaterialData materialData,
															 ItemGenerationReason reason);
	/**
	 * Generates a random {@link MythicItemStack} from a {@link MaterialData} with an {@link
	 * ItemGenerationReason} for creating the item.
	 *
	 * @param materialData MaterialData of the item
	 * @param reason       reason for item generation
	 * @return resulting ItemStack
	 */
	MythicItemStack generateItemStackFromMaterialData(MaterialData materialData, ItemGenerationReason reason);
}
