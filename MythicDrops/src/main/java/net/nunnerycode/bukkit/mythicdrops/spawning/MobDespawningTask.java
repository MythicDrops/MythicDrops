package net.nunnerycode.bukkit.mythicdrops.spawning;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public final class MobDespawningTask extends BukkitRunnable {

	private MythicDrops mythicDrops;

	public MobDespawningTask(MythicDrops mythicDrops) {
		this.mythicDrops = mythicDrops;
	}

	public MythicDrops getMythicDrops() {
		return mythicDrops;
	}

	@Override
	public void run() {
		for (World w : Bukkit.getWorlds()) {
			for (LivingEntity le : w.getLivingEntities()) {
				if (le.getEquipment().getItemInHand().getType() == Material.AIR) {
					if (shouldDespawn(le)) {
						le.remove();
					}
				}
			}
		}
	}

	private boolean shouldDespawn(LivingEntity livingEntity) {
		boolean b = false;
		for (ItemStack itemStack : livingEntity.getEquipment().getArmorContents()) {
			if (itemStack.getType() == Material.AIR) {
				continue;
			}
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
				b = true;
			}
		}

		List<Entity> entities = livingEntity.getNearbyEntities(128, 128, 128);

		for (Entity e : entities) {
			if (e instanceof Player) {
				b = false;
				break;
			}
		}

		return b;
	}

}
