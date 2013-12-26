package net.nunnerycode.bukkit.mythicdrops.spawning;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.utils.EntityUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public final class ItemSpawningListener implements Listener {

	private MythicDrops mythicDrops;

	public ItemSpawningListener(MythicDropsPlugin mythicDrops) {
		this.mythicDrops = mythicDrops;
	}

	public MythicDrops getMythicDrops() {
		return mythicDrops;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawnEventLowest(CreatureSpawnEvent event) {
		if (mythicDrops.getConfigSettings().isBlankMobSpawnEnabled()) {
			if (event.getEntity() instanceof Skeleton) {
				event.getEntity().getEquipment().clear();
				if (mythicDrops.getConfigSettings().isBlankMobSpawnSkeletonsSpawnWithBows()) {
					event.getEntity().getEquipment().setItemInHand(new ItemStack(Material.BOW, 1));
				}
			} else {
				event.getEntity().getEquipment().clear();
			}
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
				&& mythicDrops.getConfigSettings().isPreventSpawner()) {
			event.getEntity().setCanPickupItems(mythicDrops.getConfigSettings().isCanMobsPickUpEquipment());
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
				&& mythicDrops.getConfigSettings().isPreventSpawner()) {
			event.getEntity().setCanPickupItems(mythicDrops.getConfigSettings().isCanMobsPickUpEquipment());
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
				mythicDrops.getConfigSettings().isPreventSpawner()) {
			event.getEntity().setCanPickupItems(mythicDrops.getConfigSettings().isCanMobsPickUpEquipment());
			return;
		}
		if (mythicDrops.getConfigSettings().getSpawnHeightLimit(event.getEntity().getWorld().getName()) <= event
				.getEntity().getLocation().getY()) {
			event.getEntity().setCanPickupItems(mythicDrops.getConfigSettings().isCanMobsPickUpEquipment());
			return;
		}
		event.getEntity().setCanPickupItems(mythicDrops.getConfigSettings().isCanMobsPickUpEquipment());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
				&& mythicDrops.getConfigSettings().isPreventSpawner()) {
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
				&& mythicDrops.getConfigSettings().isPreventSpawner()) {
			return;
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM &&
				mythicDrops.getConfigSettings().isPreventSpawner()) {
			return;
		}
		if (mythicDrops.getConfigSettings().getSpawnHeightLimit(event.getEntity().getWorld().getName()) <= event
				.getEntity().getLocation().getY()) {
			return;
		}
		double chance = mythicDrops.getConfigSettings().getGlobalSpawnChance() * mythicDrops.getConfigSettings()
				.getEntityTypeChanceToSpawn(event.getEntityType());
		if (mythicDrops.getConfigSettings().isOnlyCustomItemsSpawn()) {
			if (mythicDrops.getConfigSettings().isCustomItemsSpawn() && RandomUtils.nextDouble() < mythicDrops
					.getConfigSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance().isEmpty() &&
					mythicDrops.getConfigSettings().getEntityTypeChanceToSpawn(event.getEntityType()) > 0 &&
					!mythicDrops.getConfigSettings().getEntityTypeTiers(event.getEntityType()).isEmpty()) {
				for (int i = 0; i < 5; i++) {
					if (RandomUtils.nextDouble() < chance) {
						EntityUtil.equipEntity(event.getEntity(), CustomItemMap.getInstance().getRandomWithChance()
								.toItemStack());
						chance *= 0.5;
						continue;
					}
					break;
				}
			}
			return;
		}
		for (int i = 0; i < 5; i++) {
			if (RandomUtils.nextDouble() < chance) {
				Tier tier = TierUtil.randomTierWithChance(mythicDrops.getConfigSettings().getEntityTypeTiers(event
						.getEntityType()));
				if (tier == null) {
					continue;
				}
				try {
					EntityUtil.equipEntity(event.getEntity(), new MythicDropBuilder().inWorld(event.getEntity()
							.getWorld()).useDurability(true).withTier(tier).build());
				} catch (Exception e) {
					continue;
				}
				chance *= 0.5;
				continue;
			}
			break;
		}
		if (mythicDrops.getConfigSettings().isCustomItemsSpawn() && RandomUtils.nextDouble() < mythicDrops
				.getConfigSettings().getCustomItemSpawnChance() && !CustomItemMap.getInstance().isEmpty()) {
			for (int i = 0; i < 5; i++) {
				if (RandomUtils.nextDouble() < chance) {
					EntityUtil.equipEntity(event.getEntity(), CustomItemMap.getInstance().getRandomWithChance()
							.toItemStack());
					chance *= 0.5;
					continue;
				}
				break;
			}
		}
	}

}
