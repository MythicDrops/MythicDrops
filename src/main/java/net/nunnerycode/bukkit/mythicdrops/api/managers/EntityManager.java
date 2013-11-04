package net.nunnerycode.bukkit.mythicdrops.api.managers;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface EntityManager {

	boolean equipEntity(LivingEntity livingEntity, CustomItem customItem);

	boolean equipEntity(LivingEntity livingEntity, ItemStack itemStack);

	MythicDrops getPlugin();

}
