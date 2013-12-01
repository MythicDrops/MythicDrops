package net.nunnerycode.bukkit.mythicdrops.api.utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * A utility class containing various methods dealing with entities.
 */
public final class EntityUtil {

	private EntityUtil() {
		// do nothing
	}

	/**
	 * Equips a {@link org.bukkit.entity.LivingEntity} with a specified {@link org.bukkit.inventory.ItemStack}.
	 * @param livingEntity LivingEntity to give item to
	 * @param itemStack ItemStack to give to LivingEntity
	 * @return if successfully gave item to LivingEntity
	 */
	public static boolean equipEntity(LivingEntity livingEntity, ItemStack itemStack) {
		if (livingEntity == null || itemStack == null) {
			return false;
		}
		if (itemStack.getType().name().toUpperCase().contains("BOOTS")) {
			livingEntity.getEquipment().setBoots(itemStack);
			livingEntity.getEquipment().setBootsDropChance(0.0F);
		} else if (itemStack.getType().name().toUpperCase().contains("LEGGINGS")) {
			livingEntity.getEquipment().setLeggings(itemStack);
			livingEntity.getEquipment().setLeggingsDropChance(0.0F);
		} else if (itemStack.getType().name().toUpperCase().contains("CHESTPLATE")) {
			livingEntity.getEquipment().setChestplate(itemStack);
			livingEntity.getEquipment().setChestplateDropChance(0.0F);
		} else if (itemStack.getType().name().toUpperCase().contains("HELMET")) {
			livingEntity.getEquipment().setHelmet(itemStack);
			livingEntity.getEquipment().setHelmetDropChance(0.0F);
		} else {
			livingEntity.getEquipment().setItemInHand(itemStack);
			livingEntity.getEquipment().setItemInHandDropChance(0.0F);
		}
		return true;
	}

}
