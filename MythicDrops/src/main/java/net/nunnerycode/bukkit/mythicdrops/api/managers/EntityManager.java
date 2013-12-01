package net.nunnerycode.bukkit.mythicdrops.api.managers;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Used to handle giving items to mobs.
 */
public interface EntityManager {

	/**
	 * Equips a {@link LivingEntity} with a specified {@link ItemStack}.
	 * @param livingEntity LivingEntity to give item to
	 * @param itemStack ItemStack to give to LivingEntity
	 * @return if successfully gave item to LivingEntity
	 */
	boolean equipEntity(LivingEntity livingEntity, ItemStack itemStack);

}
