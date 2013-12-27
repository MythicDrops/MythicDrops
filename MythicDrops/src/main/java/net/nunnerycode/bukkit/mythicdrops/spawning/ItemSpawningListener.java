package net.nunnerycode.bukkit.mythicdrops.spawning;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.CustomItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.EntityUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
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
		if (event.getEntity().getLocation().getY() > mythicDrops.getConfigSettings().getSpawnHeightLimit(event.getEntity
				().getWorld().getName())) {
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
				Tier tier = getTier("*", event.getEntity().getWorld().getName());
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

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player || event.getEntity().getLastDamageCause() == null || event.getEntity()
				.getLastDamageCause()
				.isCancelled()) {
			return;
		}

		EntityDamageEvent.DamageCause damageCause = event.getEntity().getLastDamageCause().getCause();

		if (damageCause == EntityDamageEvent.DamageCause.CONTACT || damageCause == EntityDamageEvent.DamageCause
				.SUFFOCATION || damageCause == EntityDamageEvent.DamageCause.FALL || damageCause == EntityDamageEvent
				.DamageCause.FIRE_TICK || damageCause == EntityDamageEvent.DamageCause.MELTING || damageCause ==
				EntityDamageEvent.DamageCause.LAVA || damageCause == EntityDamageEvent.DamageCause.DROWNING ||
				damageCause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || damageCause == EntityDamageEvent
				.DamageCause.BLOCK_EXPLOSION || damageCause == EntityDamageEvent.DamageCause.VOID || damageCause ==
				EntityDamageEvent.DamageCause.LIGHTNING || damageCause == EntityDamageEvent.DamageCause.SUICIDE ||
				damageCause == EntityDamageEvent.DamageCause.STARVATION || damageCause == EntityDamageEvent
				.DamageCause.WITHER || damageCause == EntityDamageEvent.DamageCause.FALLING_BLOCK || damageCause ==
				EntityDamageEvent.DamageCause.CUSTOM) {
			return;
		}

		ItemStack[] newDrops = new ItemStack[]{new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1),
				new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1)};

		ItemStack[] array = new ItemStack[5];
		System.arraycopy(event.getEntity().getEquipment().getArmorContents(), 0, array, 0, 4);
		array[4] = event.getEntity().getEquipment().getItemInHand();

		event.getEntity().getEquipment().setBootsDropChance(0.0F);
		event.getEntity().getEquipment().setLeggingsDropChance(0.0F);
		event.getEntity().getEquipment().setChestplateDropChance(0.0F);
		event.getEntity().getEquipment().setHelmetDropChance(0.0F);
		event.getEntity().getEquipment().setItemInHandDropChance(0.0F);

		for (int i = 0; i < array.length; i++) {
			ItemStack is = array[i];
			if (is == null || is.getType() == Material.AIR) {
				continue;
			}
			if (!is.hasItemMeta()) {
				continue;
			}
			CustomItem ci;
			try {
				ci = CustomItemUtil.getCustomItemFromItemStack(is);
			} catch (NullPointerException e) {
				ci = null;
			}
			if (ci != null) {
				if (RandomUtils.nextDouble() < ci.getChanceToDropOnDeath()) {
					newDrops[i] = ci.toItemStack();
					continue;
				}
			}
			Tier tier = TierUtil.getTierFromItemStack(is);
			if (tier == null) {
				continue;
			}
			if (RandomUtils.nextDouble() < getTierDropChance(tier, event.getEntity().getWorld().getName())) {
				ItemStack newItemStack = is.getData().toItemStack(is.getAmount());
				newItemStack.setItemMeta(is.getItemMeta().clone());
				short minimumDurability = (short) (is.getType().getMaxDurability() - is.getType().getMaxDurability()
						* Math.max(tier.getMinimumDurabilityPercentage(), tier.getMaximumDurabilityPercentage()));
				short maximumDurability = (short) (is.getType().getMaxDurability() - is.getType().getMaxDurability()
						* Math.min(tier.getMinimumDurabilityPercentage(), tier.getMaximumDurabilityPercentage()));
				if (!(is.getDurability() <= maximumDurability && is.getDurability() >= minimumDurability)) {
					newItemStack.setDurability(ItemStackUtil.getDurabilityForMaterial(is.getType(),
							tier.getMinimumDurabilityPercentage(), tier.getMaximumDurabilityPercentage()));
				}
//				newItemStack.addUnsafeEnchantments(is.getEnchantments());
				newDrops[i] = newItemStack;
			}
		}

		Location location = event.getEntity().getLocation();

		for (ItemStack itemstack : newDrops) {
			if (itemstack.getData().getItemTypeId() == 0) {
				continue;
			}
			location.getWorld().dropItemNaturally(location, itemstack);
		}
	}

	private double getTierDropChance(Tier t, String worldName) {
		if (t.getWorldDropChanceMap().containsKey(worldName)) {
			return t.getWorldDropChanceMap().get(worldName);
		}
		if (t.getWorldDropChanceMap().containsKey("default")) {
			return t.getWorldDropChanceMap().get("default");
		}
		return 1.0;
	}

	private Tier getTier(String tierName, String worldName) {
		Tier tier;
		if (tierName.equals("*")) {
			tier = TierMap.getInstance().getRandomWithChance(worldName);
			if (tier == null) {
				tier = TierMap.getInstance().getRandomWithChance("default");
			}
		} else {
			tier = TierMap.getInstance().get(tierName.toLowerCase());
			if (tier == null) {
				tier = TierMap.getInstance().get(tierName);
			}
		}
		return tier;
	}


}
